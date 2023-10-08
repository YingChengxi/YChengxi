package com.ycx.Handler.config.ConfigHandler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ycx.Handler.config.Category;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.util.JsonUtils;

import java.io.File;

import static com.ycx.MainClient.MOD_ID;

public class ConfigHandler implements IConfigHandler {
    private static final String FILE_PATH = "./config/" + MOD_ID + ".json";
    private static final File CONFIG_DIR = new File("./config");

    @Override
    public void load() {
        loadFile();
    }

    public static void loadFile() {
        File settingFile = new File(FILE_PATH);
        if (settingFile.isFile() && settingFile.exists()) {
            JsonElement jsonElement = JsonUtils.parseJsonFile(settingFile);
            if (jsonElement instanceof JsonObject) {

                for (Category category : Category.values())
                    ConfigUtils.readConfigBase((JsonObject)jsonElement, category.name(), category.getConfigs());
            }
        }
    }

    @Override
    public void save() {
        saveFile();
    }

    private static void saveFile() {
        if ((CONFIG_DIR.exists() && CONFIG_DIR.isDirectory()) || CONFIG_DIR.mkdirs()) {
            JsonObject configRoot = new JsonObject();

            for (Category category : Category.values())
                    ConfigUtils.writeConfigBase(configRoot, category.name(), category.getConfigs());

            JsonUtils.writeJsonToFile(configRoot, new File(FILE_PATH));
        }
    }

}
