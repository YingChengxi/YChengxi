package com.ycx.config;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.IConfigBase;

import java.util.ArrayList;
import java.util.List;


public enum TrueAdd {
    ELYTRA(),
    AUTOEAT();

    private final List<IConfigBase> configs;

    TrueAdd() {
        configs = new ArrayList<>();
    }

    <T extends IConfigBase> T add(T config) {
        this.configs.add(config);
        return config;
    }
    public List<IConfigBase> getConfigs() {
        return ImmutableList.copyOf(this.configs);
    }

}
