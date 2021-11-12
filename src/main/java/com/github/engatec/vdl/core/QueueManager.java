package com.github.engatec.vdl.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.github.engatec.vdl.db.DbManager;
import com.github.engatec.vdl.db.mapper.QueueMapper;
import com.github.engatec.vdl.model.DownloadStatus;
import com.github.engatec.vdl.model.QueueItem;
import com.github.engatec.vdl.util.YouDlUtils;
import com.github.engatec.vdl.worker.service.QueueItemDownloadService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import org.apache.commons.collections4.ListUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.github.engatec.vdl.model.DownloadStatus.FINISHED;
import static com.github.engatec.vdl.model.DownloadStatus.IN_PROGRESS;
import static com.github.engatec.vdl.model.DownloadStatus.READY;
import static com.github.engatec.vdl.model.DownloadStatus.SCHEDULED;

public class QueueManager extends VdlManager {

    private static final Logger LOGGER = LogManager.getLogger(QueueManager.class);

    public static final QueueManager INSTANCE = new QueueManager();

    private final ObservableList<QueueItem> queueItems = FXCollections.observableList(new LinkedList<>());
    private final Map<QueueItem, Service<?>> itemServiceMap = new HashMap<>();

    private Consumer<Integer> onQueueItemsChangeListener;

    private DbManager dbManager;

    private QueueManager() {
        queueItems.addListener((ListChangeListener<QueueItem>) change -> {
            while (change.next()) {
                List<? extends QueueItem> removedItems = change.getRemoved();

                List<Process> processes = new ArrayList<>();
                for (QueueItem ri : removedItems) {
                    Service<?> service = itemServiceMap.remove(ri);
                    if (service != null) {
                        processes.addAll(((QueueItemDownloadService) service).getProcesses());
                        service.cancel();
                    }
                }

                CompletableFuture<?>[] onExitCompletableFutures = processes.stream()
                        .map(Process::onExit)
                        .toArray(CompletableFuture[]::new);

                CompletableFuture.allOf(onExitCompletableFutures).thenRunAsync(() -> deleteTempData(removedItems), AppExecutors.SYSTEM_EXECUTOR);
            }

            notifyItemsChanged(change.getList());
        });
    }

    private void deleteTempData(List<? extends QueueItem> removedItems) {
        List<Long> ids = removedItems.stream()
                .map(QueueItem::getId)
                .toList();
        dbManager.doQueryAsync(QueueMapper.class, mapper -> mapper.deleteQueueItems(ids));

        for (QueueItem ri : removedItems) {
            if (ri.getStatus() != FINISHED) {
                YouDlUtils.deleteTempFiles(ri.getDestinationsForTraversal());
            }
        }
    }

    @Override
    public void init() {
        dbManager = ApplicationContext.getManager(DbManager.class);
        dbManager.doQueryAsync(QueueMapper.class, QueueMapper::fetchQueueItems)
                .thenAccept(dbItems -> {
                    fixState(dbItems);
                    Platform.runLater(() -> addAll(dbItems));
                });
    }

    public void addItem(QueueItem item) {
        addItem(item, true);
    }

    private void addItem(QueueItem item, boolean dbEntryRequired) {
        Service<?> service = itemServiceMap.get(item);
        if (service != null && service.isRunning()) { // Sanity check. Should never happen.
            LOGGER.warn("Ooops! Service exists and running!");
            return;
        }

        if (dbEntryRequired) {
            dbManager.doQueryAsync(QueueMapper.class, mapper -> mapper.insertQueueItems(List.of(item)));
        }

        queueItems.add(item);

        if (item.getStatus() == READY) {
            startDownload(item);
        }
    }

    public void addAll(List<QueueItem> items) {
        for (QueueItem item : items) {
            addItem(item, false);
        }
    }

    public void removeItem(QueueItem item) {
        queueItems.remove(item);
    }

    public void removeAll() {
        queueItems.clear();
    }

    public void addDestination(QueueItem item, String destination) {
        dbManager.doQueryAsync(QueueMapper.class, mapper -> mapper.insertQueueTempFile(item.getId(), destination));
        item.getDestinations().add(destination);
    }

    public ObservableList<QueueItem> getQueueItems() {
        return queueItems;
    }

    public boolean hasItem(Predicate<QueueItem> predicate) {
        if (!Platform.isFxApplicationThread()) {
            throw new IllegalStateException("Method must only be used from the FX Application Thread");
        }
        return queueItems.stream().anyMatch(predicate);
    }

    private void fixState(List<QueueItem> items) {
        for (QueueItem item : ListUtils.emptyIfNull(items)) {
            DownloadStatus status = item.getStatus();
            if (status == SCHEDULED || status == IN_PROGRESS) {
                item.setStatus(READY);
            }
            if (item.getProgress() < 0) {
                item.setProgress(0);
            }
        }
    }

    public void startDownload(QueueItem item) {
        itemServiceMap.computeIfAbsent(item, QueueItemDownloadService::new).start();
    }

    public void cancelDownload(QueueItem item) {
        Service<?> service = itemServiceMap.get(item);
        if (service == null) {
            LOGGER.warn("No service associated with the queue item");
        } else {
            service.cancel();
        }
    }

    public void resumeDownload(QueueItem item) {
        itemServiceMap.computeIfAbsent(item, QueueItemDownloadService::new).restart();
    }

    public void setOnQueueItemsChangeListener(Consumer<Integer> onQueueItemsChangeListener) {
        this.onQueueItemsChangeListener = onQueueItemsChangeListener;
        notifyItemsChanged(queueItems);
    }

    private void notifyItemsChanged(ObservableList<? extends QueueItem> list) {
        if (onQueueItemsChangeListener == null) {
            return;
        }

        onQueueItemsChangeListener.accept(list.size());
    }
}
