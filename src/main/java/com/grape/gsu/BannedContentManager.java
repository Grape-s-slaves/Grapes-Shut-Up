package com.grape.gsu;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class BannedContentManager {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new Gson();
    private static final String CONFIG_FILE = "config/banned.json";

    private static Set<ResourceLocation> bannedItems = new HashSet<>();
    private static Set<ResourceLocation> bannedBlocks = new HashSet<>();
    private static Set<ResourceLocation> bannedEntities = new HashSet<>();
    private static Set<ResourceLocation> bannedStructures = new HashSet<>();

    public static void loadBannedContent() {
        File configFile = new File(CONFIG_FILE);

        if (!configFile.exists()) {
            createDefaultConfig(configFile);
            return;
        }

        try (FileReader reader = new FileReader(configFile)) {
            JsonObject json = GSON.fromJson(reader, JsonObject.class);

            if (json.has("banned")) {
                JsonArray bannedArray = json.getAsJsonArray("banned");

                for (int i = 0; i < bannedArray.size(); i++) {
                    String resourceString = bannedArray.get(i).getAsString();
                    ResourceLocation resource = new ResourceLocation(resourceString);

                    bannedItems.add(resource);
                    bannedBlocks.add(resource);
                    bannedEntities.add(resource);
                    bannedStructures.add(resource);

                    LOGGER.info("Banned resource: {}", resourceString);
                }
            }

            LOGGER.info("Loaded {} banned resources", bannedItems.size());

        } catch (IOException e) {
            LOGGER.error("Failed to load banned content config", e);
            createDefaultConfig(configFile);
        }
    }

    private static void createDefaultConfig(File configFile) {
        try {
            configFile.getParentFile().mkdirs();

            JsonObject json = new JsonObject();
            JsonArray bannedArray = new JsonArray();
            bannedArray.add("minecraft:apple");
            bannedArray.add("minecraft:diamond");
            json.add("banned", bannedArray);

            try (FileWriter writer = new FileWriter(configFile)) {
                GSON.toJson(json, writer);
            }

            LOGGER.info("Created default banned.json config file");

        } catch (IOException e) {
            LOGGER.error("Failed to create default config", e);
        }
    }

    public static boolean isItemBanned(ResourceLocation itemId) {
        return bannedItems.contains(itemId);
    }

    public static boolean isBlockBanned(ResourceLocation blockId) {
        return bannedBlocks.contains(blockId);
    }

    public static boolean isEntityBanned(ResourceLocation entityId) {
        return bannedEntities.contains(entityId);
    }

    public static boolean isStructureBanned(ResourceLocation structureId) {
        return bannedStructures.contains(structureId);
    }

    public static Set<ResourceLocation> getBannedItems() {
        return new HashSet<>(bannedItems);
    }

    public static Set<ResourceLocation> getBannedBlocks() {
        return new HashSet<>(bannedBlocks);
    }

    public static Set<ResourceLocation> getBannedEntities() {
        return new HashSet<>(bannedEntities);
    }

    public static Set<ResourceLocation> getBannedStructures() {
        return new HashSet<>(bannedStructures);
    }
}