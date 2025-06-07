package com.grape.gsu;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = GrapesShutUpMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue ENABLE_DEBUG_LOGGING = BUILDER
            .comment("Enable debug logging for banned content removal")
            .define("enableDebugLogging", false);

    private static final ForgeConfigSpec.BooleanValue REMOVE_FROM_LOOT_TABLES = BUILDER
            .comment("Remove banned items from loot tables (chests, mob drops, etc.)")
            .define("removeFromLootTables", true);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean enableDebugLogging;
    public static boolean removeFromLootTables;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        enableDebugLogging = ENABLE_DEBUG_LOGGING.get();
        removeFromLootTables = REMOVE_FROM_LOOT_TABLES.get();
    }
}