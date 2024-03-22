package com.ycx.Handler.config.KeyBind;

import com.ycx.Handler.config.Configs;
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
        manager.addKeybindToMap(Configs.AUTOBRIDGE.getKeybind());
        manager.addKeybindToMap(Configs.AUTODUPE.getKeybind());
        manager.addKeybindToMap(Configs.SHOULDATTACK.getKeybind());
        manager.addKeybindToMap(Configs.ANTIHUNGER.getKeybind());
        //#if MC >= 12002
        manager.addKeybindToMap(Configs.CAMELDASHCOOLDOWN.getKeybind());
        //#endif
        manager.addKeybindToMap(Configs.MOUNTJUMPSTRENGTH.getKeybind());
        manager.addKeybindToMap(Configs.MUTEATNOFOCUS.getKeybind());
    }

    @Override
    public void addHotkeys(IKeybindManager manager) {
        // Not necessary
    }
}