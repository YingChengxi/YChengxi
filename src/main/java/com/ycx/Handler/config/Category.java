package com.ycx.Handler.config;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.IConfigBase;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


public enum Category {
    GENERAL("通用"),
    CLIENT("仅客户端"),
    FACTOR("参数"),
    BAN("禁用"),
    DEBUG("实验性");


    @Getter
    private final String key;
    private final List<IConfigBase> configs;

    Category(String key) {
        this.key = key;
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
