package com.github.engatec.vdl.controller.preferences.youtubedl;

import java.util.ResourceBundle;

import com.github.engatec.vdl.core.ApplicationContext;
import com.github.engatec.vdl.handler.textformatter.IntegerTextFormatter;
import com.github.engatec.vdl.ui.Icons;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.ContentDisplay;
import org.apache.commons.lang3.BooleanUtils;

class Initializer {

    static void initialize(Context ctx) {
        initNetworkSettings(ctx);
        initAuthenticationSettings(ctx);
        initConfigFileSettings(ctx);
    }

    private static void initNetworkSettings(Context ctx) {
        ResourceBundle resourceBundle = ApplicationContext.INSTANCE.getResourceBundle();

        ctx.getProxyUrlTextField().textProperty().addListener((observable, oldValue, newValue) -> ctx.getProxyUrlTextField().clearError());
        ctx.getProxyUrlTextField().setHint(resourceBundle.getString("preferences.youtubedl.network.proxy.hint"));

        ctx.getSocketTimoutTextField().setTextFormatter(new IntegerTextFormatter());
        ctx.getSocketTimoutTextField().setHint(resourceBundle.getString("preferences.youtubedl.network.socket.timeout.hint"));

        ctx.getSourceAddressTextField().textProperty().addListener((observable, oldValue, newValue) -> ctx.getSourceAddressTextField().clearError());
        ctx.getSourceAddressTextField().setHint(resourceBundle.getString("preferences.youtubedl.network.sourceaddress.hint"));

        ctx.getForceIpV4CheckBox().selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (BooleanUtils.isTrue(newValue)) {
                ctx.getForceIpV6CheckBox().setSelected(false);
            }
        });
        ctx.getForceIpV6CheckBox().selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (BooleanUtils.isTrue(newValue)) {
                ctx.getForceIpV4CheckBox().setSelected(false);
            }
        });
    }

    private static void initAuthenticationSettings(Context ctx) {
        ctx.getUsernameTextField().textProperty().addListener((observable, oldValue, newValue) -> ctx.getUsernameTextField().clearError());
        ctx.getPasswordTextField().textProperty().addListener((observable, oldValue, newValue) -> ctx.getPasswordTextField().clearError());
    }

    private static void initConfigFileSettings(Context ctx) {
        ctx.getUseConfigFileCheckBox().setGraphic(Icons.infoWithTooltip("preferences.youtubedl.checkbox.configitem.tooltip"));
        ctx.getUseConfigFileCheckBox().setContentDisplay(ContentDisplay.RIGHT);

        BooleanBinding configFileCheckBoxUnselected = ctx.getUseConfigFileCheckBox().selectedProperty().not();
        ctx.getConfigFileTextField().disableProperty().bind(configFileCheckBoxUnselected);
        ctx.getConfigFileChooseBtn().disableProperty().bind(configFileCheckBoxUnselected);
        ctx.getConfigFileChooseBtn().setOnAction(event -> Handler.handleConfigFileChooseBtnClick(ctx, event));
    }
}
