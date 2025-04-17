package com.idlenonsense.desertworld.events.listeners;

import com.idlenonsense.desertworld.abilities.Abilities;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.LinkedList;

public class StepBlockListener {
    private static final LinkedList<StepBlockCallback> callbacks = new LinkedList<>();

    public static void register() {
        ServerTickEvents.END_WORLD_TICK.register(world -> {
            for (PlayerEntity player : world.getPlayers()) {
                BlockPos pos = player.getBlockPos().down();
                Block block = world.getBlockState(pos).getBlock();
                // System.out.println("!! block: " + block);
                for (StepBlockCallback callback : callbacks) {
                    // System.out.println("!! " + callback);
                    callback.stepOn(world, player, pos, block);
                }
            }
        });
    }

    public static void addCallback(StepBlockCallback callback) {
        callbacks.add(callback);
    }

    @FunctionalInterface
    public interface StepBlockCallback {
        void stepOn(ServerWorld world, PlayerEntity player, BlockPos pos, Block block);
    }
}
