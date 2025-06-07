package com.grape.gsu;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RegistryHandler {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, GrapesShutUpMod.MODID);
    
    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> BANNED_ITEM_MODIFIER =
            LOOT_MODIFIER_SERIALIZERS.register("banned_item_modifier", LootTableModifier.CODEC);

    public static void register(IEventBus eventBus) {
        LOOT_MODIFIER_SERIALIZERS.register(eventBus);
    }
}