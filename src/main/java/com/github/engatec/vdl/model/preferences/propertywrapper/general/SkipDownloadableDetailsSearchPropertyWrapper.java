package com.github.engatec.vdl.model.preferences.propertywrapper.general;

import com.github.engatec.vdl.model.preferences.ConfigItem;
import com.github.engatec.vdl.model.preferences.general.SkipDownloadableDetailsSearchConfigItem;
import com.github.engatec.vdl.model.preferences.propertywrapper.ConfigItemPropertyWrapper;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class SkipDownloadableDetailsSearchPropertyWrapper extends ConfigItemPropertyWrapper<BooleanProperty, Boolean> {

    private static final ConfigItem<Boolean> CONFIG_ITEM = new SkipDownloadableDetailsSearchConfigItem();

    private final BooleanProperty property = new SimpleBooleanProperty();

    public SkipDownloadableDetailsSearchPropertyWrapper() {
        restore();
    }

    @Override
    protected ConfigItem<Boolean> getConfigItem() {
        return CONFIG_ITEM;
    }

    @Override
    public BooleanProperty getProperty() {
        return property;
    }

    @Override
    public Boolean getValue() {
        return property.get();
    }

    @Override
    public void setValue(Boolean value) {
        property.set(value);
    }
}
