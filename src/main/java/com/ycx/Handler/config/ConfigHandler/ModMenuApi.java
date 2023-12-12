package com.ycx.Handler.config.ConfigHandler;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;

public class ModMenuApi implements com.terraformersmc.modmenu.api.ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (screen) -> {
            GuiConfig gui = new GuiConfig();
//            gui.setParentGui(screen);
            gui.setParent(screen);
            return gui;
        };
    }
}