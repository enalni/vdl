package com.github.engatec.vdl.model.preferences.wrapper.general;

import com.github.engatec.vdl.model.preferences.ConfigItem;
import com.github.engatec.vdl.model.preferences.general.DownloadThreadsConfigItem;
import com.github.engatec.vdl.model.preferences.wrapper.ConfigItemWrapper;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;

public class DownloadThreadsPref extends ConfigItemWrapper<Property<Integer>, Integer> {

    private static final ConfigItem<Integer> CONFIG_ITEM = new DownloadThreadsConfigItem();

    private final Property<Integer> property = new SimpleObjectProperty<>();

    public DownloadThreadsPref() {
        restore();
    }

    @Override
    protected ConfigItem<Integer> getConfigItem() {
        return CONFIG_ITEM;
    }

    @Override
    public Property<Integer> getProperty() {
        return property;
    }

    @Override
    public Integer getValue() {
        return property.getValue();
    }

    @Override
    public void setValue(Integer value) {
        property.setValue(value);
    }
}
