package com.ycx.config.ModMenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.ycx.config.ConfigHandler.GuiConfig;

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