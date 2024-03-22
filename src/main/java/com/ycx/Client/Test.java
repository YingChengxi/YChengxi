package com.ycx.Client;

import fi.dy.masa.malilib.hotkeys.IHotkeyCallback;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeyAction;



public class Test implements IHotkeyCallback {

    @Override
    public boolean onKeyAction(KeyAction action, IKeybind key) {

       return true;
    }
}
