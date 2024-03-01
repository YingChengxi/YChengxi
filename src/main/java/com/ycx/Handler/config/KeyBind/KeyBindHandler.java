package com.ycx.Handler.config.KeyBind;

import com.ycx.Handler.config.ConfigHandler.GuiConfig;
import fi.dy.masa.malilib.hotkeys.IHotkeyCallback;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import net.minecraft.client.MinecraftClient;

public class KeyBindHandler implements IHotkeyCallback {

    @Override
    public boolean onKeyAction(KeyAction action, IKeybind key) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.currentScreen instanceof GuiConfig)
            client.currentScreen.close(); // actually has no effect
        else
            client.setScreen(new GuiConfig());
        return true;
    }
}
