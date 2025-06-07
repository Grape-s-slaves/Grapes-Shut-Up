package com.grape.gsu;

import com.google.common.base.Suppliers;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class LootTableModifier extends LootModifier {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final Supplier<Codec<LootTableModifier>> CODEC = Suppliers.memoize(() ->
            RecordCodecBuilder.create(inst -> codecStart(inst).apply(inst, LootTableModifier::new))
    );

    public LootTableModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Nonnull
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if (!Config.removeFromLootTables) {
            return generatedLoot;
        }

        ObjectArrayList<ItemStack> filteredLoot = new ObjectArrayList<>();

        for (ItemStack itemStack : generatedLoot) {
            ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(itemStack.getItem());

            if (!BannedContentManager.isItemBanned(itemId)) {
                filteredLoot.add(itemStack);
            } else {
                LOGGER.debug("Removed banned item from loot: {}", itemId);
            }
        }

        return filteredLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}