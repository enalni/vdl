package com.github.engatec.vdl.core;

public enum UiComponent {

    ABOUT("/fxml/about.fxml"),
    MAIN("/fxml/main.fxml"),
    QUEUE("/fxml/queue/queue.fxml"),
    PREFERENCES("/fxml/preferences/preferences.fxml"),
    PREFERENCES_GENERAL("/fxml/preferences/preferences-general.fxml"),
    PREFERENCES_YOUTUBE_DL("/fxml/preferences/preferences-youtubedl.fxml"),
    POSTPROCESSING("/fxml/postprocessing.fxml"),
    VIDEO_DOWNLOAD_GRID("/fxml/video-download-grid.fxml"),
    DOWNLOADABLE_ITEMS_COMPONENT("/fxml/downloadable-items-component.fxml"),
    DOWNLOADING_PROGRESS("/fxml/downloading-progress.fxml"),
    DIALOG_PROGRESS("/fxml/dialog/progress-dialog.fxml");

    private final String fxml;

    UiComponent(String fxml) {
        this.fxml = fxml;
    }

    public String getFxml() {
        return fxml;
    }
}
