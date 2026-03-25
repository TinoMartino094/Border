package com.tino.border.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class BorderConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(),
            "growing_border.json");

    public double chunks_per_item = 512.0;
    public String expansion_currency = "minecraft:recovery_compass";

    public static BorderConfig load() {
        if (!CONFIG_FILE.exists()) {
            BorderConfig defaultConfig = new BorderConfig();
            defaultConfig.save();
            return defaultConfig;
        }

        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            return GSON.fromJson(reader, BorderConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new BorderConfig();
        }
    }

    public void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            System.err.println("Failed to save Growing Border config!");
            e.printStackTrace();
        }
    }
}
