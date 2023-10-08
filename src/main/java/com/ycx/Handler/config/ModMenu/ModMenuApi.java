package com.ycx.Handler.config.ModMenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.ycx.Handler.config.ConfigHandler.GuiConfig;

public class ModMenuApi implements com.terraformersmc.modmenu.api.ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (screen) -> {
            GuiConfig gui = new GuiConfig();
            gui.setParentGui(screen);
            return gui;
        };
    }
}