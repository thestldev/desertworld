package com.idlenonsense.desertworld.events.listeners;

import com.idlenonsense.desertworld.item.ModItems;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

public class BreakBlockListener {

    public static void register() {
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, entity) -> {
            if (world.isClient()) return;

            if (!isCursedTool(player.getMainHandStack())) return;

            List<ItemStack> drops = Block.getDroppedStacks(state, (ServerWorld) world, pos, entity, player, player.getMainHandStack());
            for (ItemStack drop : drops) {
                drop.setCount(drop.getCount() * 3);
                Block.dropStack(world, pos, drop);
            }
        });
    }

    private static boolean isCursedTool(ItemStack stack) {
        return stack.getItem().equals(ModItems.CURSED_PICKAXE)
                || stack.getItem().equals(ModItems.CURSED_AXE)
                || stack.getItem().equals(ModItems.CURSED_SHOVEL)
                || stack.getItem().equals(ModItems.CURSED_SWORD);
    }

    @FunctionalInterface
    public interface BreakBlockCallback {
        void breakBlock(ServerWorld world, PlayerEntity entity, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity);
    }
}
