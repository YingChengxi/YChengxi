package com.ycx.config;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.IConfigBase;

import java.util.ArrayList;
import java.util.List;


public enum Category {
    GENERAL("通用"),
    CLIENT("仅客户端"),
    BAN("禁用"),
    DEBUG("实验性");


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

    public void trueAdd(int index, List<IConfigBase> list){
        this.configs.addAll(index + 1, list);
    }

    public void falseDel(List<IConfigBase> list){
        this.configs.removeAll(list);
    }

    public List<IConfigBase> getConfigs() {
        return ImmutableList.copyOf(this.configs);
    }

    public int getIndex(IConfigBase configBase){
        return this.configs.indexOf(configBase);
    }

    public String getKey() {
        return this.key;
    }
}
