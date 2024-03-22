package com.ycx.Handler.config.ConfigHandler;

import com.ycx.MainClient;
import com.ycx.Handler.config.Category;
import com.ycx.Handler.config.Configs;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.util.StringUtils;

import java.util.List;


public class GuiConfig extends GuiConfigsBase {
    private static Category currentTab = Category.GENERAL;

    public GuiConfig() {
        super(10, 50, MainClient.MOD_ID, null,  "YChengxi 设置界面");
    }

    @Override
    public void initGui() {
        super.initGui();
        this.clearOptions();
        int x = 10, y = 26;
        // tab buttons are set
        for (Category category : Category.values()) {
            if (category == Category.HIDEFACTOR) continue;
            if (category == Category.DEBUG && !Configs.DEBUG.getBooleanValue()) continue;
            ButtonGeneric tabButton = new TabButton(category, x, y, -1, 20, StringUtils.translate(category.getKey()));
            tabButton.setEnabled(GuiConfig.currentTab != category );
            this.addButton(tabButton, (buttonBase, i) -> {
                this.onSettingsChanged();
                // reload the GUI when tab button is clicked
                currentTab = ((TabButton) buttonBase).category;
                this.reCreateListWidget();
                //noinspection ConstantConditions
                this.getListWidget().resetScrollbarPosition();
                this.initGui();
            });
            x += tabButton.getWidth() + 2;
        }
    }


    @Override
    public List<ConfigOptionWrapper> getConfigs() {
        // option buttons are set
        return ConfigOptionWrapper.createFor(currentTab.getConfigs());
    }

    public static class TabButton extends ButtonGeneric {
        private final Category category;

        public TabButton(Category category, int x, int y, int width, int height, String text, String... hoverStrings) {
            super(x, y, width, height, text, hoverStrings);
            this.category = category;
        }
    }
}
