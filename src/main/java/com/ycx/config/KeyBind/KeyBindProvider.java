package com.ycx.config.KeyBind;

import com.ycx.config.Configs;
import fi.dy.masa.malilib.hotkeys.IKeybindManager;
import fi.dy.masa.malilib.hotkeys.IKeybindProvider;

public class KeyBindProvider implements IKeybindProvider {
    @Override
    public void addKeysToMap(IKeybindManager manager) {
        manager.addKeybindToMap(Configs.MENU_OPEN_KEY.getKeybind());
        manager.addKeybindToMap(Configs.ELYTRA.getKeybind());
        manager.addKeybindToMap(Configs.SLOWDOWN.getKeybind());
        manager.addKeybindToMap(Configs.AUTOEAT.getKeybind());
        manager.addKeybindToMap(Configs.TEST.getKeybind());
        manager.addKeybindToMap(Configs.AUTOSTORE.getKeybind());
    }

    @Override
    public void addHotkeys(IKeybindManager manager) {
        // Not necessary
    }
}