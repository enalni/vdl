package com.github.engatec.vdl.controller.postprocessing;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import com.github.engatec.vdl.controller.StageAwareController;
import com.github.engatec.vdl.model.downloadable.Downloadable;
import com.github.engatec.vdl.model.postprocessing.Postprocessing;
import com.github.engatec.vdl.stage.postprocessing.PostprocessingStageFactory;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

public class PostprocessingController extends StageAwareController {

    private Downloadable downloadable;

    private final Map<Class<? extends Postprocessing>, Postprocessing> appliedPostprocessingsMap = new HashMap<>();

    @FXML private ListView<PostprocessingStageFactory> availableItemsListView;
    @FXML private ListView<PostprocessingStageFactory> activeItemsListView;

    private PostprocessingController() {
    }

    public PostprocessingController(Stage stage, Downloadable downloadable) {
        super(stage);
        this.downloadable = downloadable;
        for (Postprocessing item : downloadable.getPostprocessingSteps()) {
            appliedPostprocessingsMap.put(item.getClass(), item);
        }
    }

    @FXML
    public void initialize() throws ParseException {
        availableItemsListView.getItems().addAll(PostprocessingStageFactory.values());

        setOnItemsListViewMouseClicked(availableItemsListView);
        setOnItemsListViewMouseClicked(activeItemsListView);
    }

    private void setOnItemsListViewMouseClicked(ListView<PostprocessingStageFactory> listView) {
        listView.setOnMouseClicked(event -> {
            if (event.getButton() != MouseButton.PRIMARY) {
                return;
            }

            if (event.getClickCount() != 2) {
                return;
            }

            PostprocessingStageFactory factory = listView.getSelectionModel().getSelectedItem();
            if (factory != null) {
                factory.create(
                        appliedPostprocessingsMap.get(factory.getPostprocessingClass()),
                        param -> {
                            appliedPostprocessingsMap.put(factory.getPostprocessingClass(), param);
                            ObservableList<PostprocessingStageFactory> activeItems = activeItemsListView.getItems();
                            if (!activeItems.contains(factory)) {
                                activeItems.add(factory);
                            }
                        }
                ).modal(stage).showAndWait();
            }
        });
    }
}
