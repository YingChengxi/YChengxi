package com.ycx;

import com.ycx.Client.Test;
import com.ycx.Handler.config.ConfigHandler.ConfigHandler;
import com.ycx.Handler.config.Configs;
import com.ycx.Handler.config.KeyBind.KeyBindHandler;
import com.ycx.Handler.config.KeyBind.KeyBindProvider;
import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.event.InputEventHandler;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;


public class MainClient implements ClientModInitializer {
    public static final String MOD_ID = "ycx";
    public static MinecraftClient MC = MinecraftClient.getInstance();


    @Override
    public void onInitializeClient() {

        ConfigManager.getInstance().registerConfigHandler(MOD_ID, new ConfigHandler());
        //noinspection InstantiationOfUtilityClass
        new Configs();  // just load the class and run static code block
        ConfigHandler.loadFile();
        InputEventHandler.getKeybindManager().registerKeybindProvider(new KeyBindProvider());
        Configs.MENU_OPEN_KEY.getKeybind().setCallback(new KeyBindHandler());
        Configs.TEST.getKeybind().setCallback(new Test());

        Register.register();

    }
}
