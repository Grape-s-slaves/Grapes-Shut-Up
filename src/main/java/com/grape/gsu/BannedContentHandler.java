package com.grape.gsu;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.TickEvent;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class BannedContentHandler {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onEntitySpawn(MobSpawnEvent.FinalizeSpawn event) {
        Entity entity = event.getEntity();
        ResourceLocation entityId = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());

        if (BannedContentManager.isEntityBanned(entityId)) {
            event.setSpawnCancelled(true);
            LOGGER.debug("Prevented spawn of banned entity: {}", entityId);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onEntityJoinLevel(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();

        ResourceLocation entityId = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
        if (BannedContentManager.isEntityBanned(entityId)) {
            event.setCanceled(true);
            LOGGER.debug("Prevented banned entity from joining level: {}", entityId);
            return;
        }

        if (entity instanceof ItemEntity itemEntity) {
            ItemStack itemStack = itemEntity.getItem();
            ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(itemStack.getItem());

            if (BannedContentManager.isItemBanned(itemId)) {
                event.setCanceled(true);
                LOGGER.debug("Prevented banned item entity from spawning: {}", itemId);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onChunkLoad(ChunkEvent.Load event) {
        ChunkAccess chunk = event.getChunk();
        Level level = (Level) event.getLevel();

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = chunk.getMinBuildHeight(); y < chunk.getMaxBuildHeight(); y++) {
                    BlockPos pos = new BlockPos(chunk.getPos().getMinBlockX() + x, y, chunk.getPos().getMinBlockZ() + z);
                    BlockState blockState = chunk.getBlockState(pos);
                    Block block = blockState.getBlock();
                    ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(block);

                    if (BannedContentManager.isBlockBanned(blockId)) {
                        level.setBlockAndUpdate(pos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState());
                        LOGGER.debug("Removed banned block {} at position {}", blockId, pos);
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        Block block = event.getPlacedBlock().getBlock();
        ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(block);

        if (BannedContentManager.isBlockBanned(blockId)) {
            event.setCanceled(true);
            LOGGER.debug("Prevented placement of banned block: {}", blockId);
        }
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        if (event.getServer().getTickCount() % 20 != 0) return;

        event.getServer().getAllLevels().forEach(level -> {
            List<Entity> toRemove = new ArrayList<>();

            level.getEntities().getAll().forEach(entity -> {
                if (entity instanceof ItemEntity itemEntity) {
                    ItemStack itemStack = itemEntity.getItem();
                    ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(itemStack.getItem());

                    if (BannedContentManager.isItemBanned(itemId)) {
                        toRemove.add(entity);
                    }
                }
            });

            toRemove.forEach(entity -> {
                entity.discard();
                ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(((ItemEntity) entity).getItem().getItem());
                LOGGER.debug("Cleaned up banned item entity: {}", itemId);
            });
        });
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onFurnaceFuelBurnTime(FurnaceFuelBurnTimeEvent event) {
        ItemStack itemStack = event.getItemStack();
        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(itemStack.getItem());

        if (BannedContentManager.isItemBanned(itemId)) {
            event.setBurnTime(0);
            LOGGER.debug("Prevented banned item from being used as fuel: {}", itemId);
        }
    }
}