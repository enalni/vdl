package com.github.engatec.vdl.ui.controller.component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.engatec.vdl.core.ApplicationContext;
import com.github.engatec.vdl.core.Engine;
import com.github.engatec.vdl.model.preferences.wrapper.misc.DownloaderPref;
import com.github.engatec.vdl.model.preferences.wrapper.youtubedl.ProxyEnabledPref;
import com.github.engatec.vdl.model.preferences.wrapper.youtubedl.ProxyUrlPref;
import com.github.engatec.vdl.ui.stage.AboutStage;
import com.github.engatec.vdl.ui.stage.PreferencesStage;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.apache.commons.lang3.StringUtils;

public class ServicebarComponentController extends HBox {

    private final ApplicationContext ctx = ApplicationContext.getInstance();

    private final Stage stage;

    @FXML private CheckBox proxyEnabledCheckBox;
    @FXML private ComboBox<Integer> downloaderComboBox;
    @FXML private ImageView helpButton;
    @FXML private ImageView preferencesButton;

    public ServicebarComponentController(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        StringProperty proxyUrlProperty = ctx.getConfigRegistry().get(ProxyUrlPref.class).getProperty();
        BooleanBinding proxyUrlIsNotBlankBinding = Bindings.createBooleanBinding(() -> StringUtils.isNotBlank(proxyUrlProperty.getValue()), proxyUrlProperty);
        proxyEnabledCheckBox.visibleProperty().bind(proxyUrlIsNotBlankBinding);
        proxyEnabledCheckBox.managedProperty().bind(proxyUrlIsNotBlankBinding);
        proxyEnabledCheckBox.selectedProperty().bindBidirectional(ctx.getConfigRegistry().get(ProxyEnabledPref.class).getProperty());

        List<Integer> configValues = Stream.of(Engine.values())
                .map(Engine::getConfigValue)
                .collect(Collectors.toList());
        downloaderComboBox.setItems(FXCollections.observableArrayList(configValues));
        downloaderComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Integer value) {
                if (value == null) {
                    return null;
                }
                return Engine.getByConfigValue(value).getDisplayValue();
            }

            @Override
            public Integer fromString(String value) {
                return Engine.getByDisplaValue(value).getConfigValue();
            }
        });
        downloaderComboBox.valueProperty().bindBidirectional(ctx.getConfigRegistry().get(DownloaderPref.class).getProperty());

        helpButton.setOnMouseClicked(e -> {
            new AboutStage().modal(stage).showAndWait();
            e.consume();
        });

        preferencesButton.setOnMouseClicked(e -> {
            new PreferencesStage().modal(stage).showAndWait();
            e.consume();
        });
    }
}
