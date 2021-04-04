package com.github.engatec.vdl.controller.preferences.youtubedl;

import com.github.engatec.fxcontrols.FxTextField;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

class Context {

    private final Stage stage;

    private final FxTextField proxyUrlTextField;
    private final FxTextField socketTimoutTextField;
    private final FxTextField sourceAddressTextField;
    private final CheckBox forceIpV4CheckBox;
    private final CheckBox forceIpV6CheckBox;

    private final FxTextField usernameTextField;
    private final FxTextField passwordTextField;
    private final TextField twoFactorTextField;
    private final FxTextField videoPasswordTextField;
    private final CheckBox netrcCheckbox;

    private final CheckBox markWatchedCheckbox;
    private final CheckBox noContinueCheckbox;
    private final CheckBox noPartCheckBox;
    private final CheckBox noMTimeCheckBox;
    private final CheckBox useConfigFileCheckBox;
    private final TextField configFileTextField;
    private final Button configFileChooseBtn;

    Context(
            Stage stage,
            FxTextField proxyUrlTextField,
            FxTextField socketTimoutTextField,
            FxTextField sourceAddressTextField,
            CheckBox forceIpV4CheckBox,
            CheckBox forceIpV6CheckBox,
            FxTextField usernameTextField,
            FxTextField passwordTextField,
            TextField twoFactorTextField,
            FxTextField videoPasswordTextField,
            CheckBox netrcCheckbox,
            CheckBox markWatchedCheckbox,
            CheckBox noContinueCheckbox,
            CheckBox noPartCheckBox,
            CheckBox noMTimeCheckBox,
            CheckBox useConfigFileCheckBox,
            TextField configFileTextField,
            Button configFileChooseBtn
    ) {
        this.stage = stage;
        this.proxyUrlTextField = proxyUrlTextField;
        this.socketTimoutTextField = socketTimoutTextField;
        this.sourceAddressTextField = sourceAddressTextField;
        this.forceIpV4CheckBox = forceIpV4CheckBox;
        this.forceIpV6CheckBox = forceIpV6CheckBox;
        this.usernameTextField = usernameTextField;
        this.passwordTextField = passwordTextField;
        this.twoFactorTextField = twoFactorTextField;
        this.videoPasswordTextField = videoPasswordTextField;
        this.netrcCheckbox = netrcCheckbox;
        this.markWatchedCheckbox = markWatchedCheckbox;
        this.noContinueCheckbox = noContinueCheckbox;
        this.noPartCheckBox = noPartCheckBox;
        this.noMTimeCheckBox = noMTimeCheckBox;
        this.useConfigFileCheckBox = useConfigFileCheckBox;
        this.configFileTextField = configFileTextField;
        this.configFileChooseBtn = configFileChooseBtn;
    }

    Stage getStage() {
        return stage;
    }

    FxTextField getProxyUrlTextField() {
        return proxyUrlTextField;
    }

    FxTextField getSocketTimoutTextField() {
        return socketTimoutTextField;
    }

    FxTextField getSourceAddressTextField() {
        return sourceAddressTextField;
    }

    CheckBox getForceIpV4CheckBox() {
        return forceIpV4CheckBox;
    }

    CheckBox getForceIpV6CheckBox() {
        return forceIpV6CheckBox;
    }

    FxTextField getUsernameTextField() {
        return usernameTextField;
    }

    FxTextField getPasswordTextField() {
        return passwordTextField;
    }

    TextField getTwoFactorTextField() {
        return twoFactorTextField;
    }

    FxTextField getVideoPasswordTextField() {
        return videoPasswordTextField;
    }

    CheckBox getNetrcCheckbox() {
        return netrcCheckbox;
    }

    CheckBox getMarkWatchedCheckbox() {
        return markWatchedCheckbox;
    }

    CheckBox getNoContinueCheckbox() {
        return noContinueCheckbox;
    }

    CheckBox getNoPartCheckBox() {
        return noPartCheckBox;
    }

    CheckBox getNoMTimeCheckBox() {
        return noMTimeCheckBox;
    }

    CheckBox getUseConfigFileCheckBox() {
        return useConfigFileCheckBox;
    }

    TextField getConfigFileTextField() {
        return configFileTextField;
    }

    Button getConfigFileChooseBtn() {
        return configFileChooseBtn;
    }
}
