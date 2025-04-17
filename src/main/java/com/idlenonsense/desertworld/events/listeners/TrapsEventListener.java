package com.idlenonsense.desertworld.events.listeners;

import com.idlenonsense.desertworld.block.traps.resource.ITrapBlock;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class TrapsEventListener {
    private static final int SCAN_RANGE = 20;


    public static void register() {
        ServerTickEvents.END_WORLD_TICK.register(world -> {
            for (PlayerEntity player : world.getPlayers()) {
                scanPlayer(player, world);
            }
        });
    }

    private static void scanPlayer(PlayerEntity player, World world) {
        scanBelow(player, world);
        scanPlayerRange(player, world);
    }

    private static void scanBelow(PlayerEntity player, World world) {
        BlockPos below = player.getBlockPos().down();
        Block block = world.getBlockState(below).getBlock();
        if (block instanceof ITrapBlock b) {
            b.stepOn((ServerWorld) world, player, below);
        }
    }

    private static void scanPlayerRange(PlayerEntity player, World world) {
        BlockPos start = new BlockPos(player.getX() - SCAN_RANGE, player.getY() - 1, player.getZ() - SCAN_RANGE);
        BlockPos end = new BlockPos(player.getX() + SCAN_RANGE, player.getY() + 1, player.getZ() + SCAN_RANGE);

        for (BlockPos pos : BlockPos.iterate(start, end)) {
            Block block = world.getBlockState(pos).getBlock();
            if (block instanceof ITrapBlock b) {
                b.trapActiveTick((ServerWorld) world, player, pos);
            }
        }
    }

    private static Box getScanBox(PlayerEntity player) {
        return new Box(
                player.getBlockPos().getX() - SCAN_RANGE,
                player.getBlockPos().getY() - 1,
                player.getBlockPos().getZ() - SCAN_RANGE,
                player.getBlockPos().getX() + SCAN_RANGE,
                player.getBlockPos().getY() + 2,
                player.getBlockPos().getZ() + SCAN_RANGE
        );
    }
}