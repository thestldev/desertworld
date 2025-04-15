package com.idlenonsense.desertworld.events;

import com.idlenonsense.desertworld.block.traps.TrapJawBlock;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import com.idlenonsense.desertworld.block.traps.ArrowTrapBlock;
import com.idlenonsense.desertworld.block.traps.LaserTrapBlock;

public class ModEventListener {
    private static int lastTriggerTime = -7;
    private static int lastArrowTriggerTime = -15;
    public static void register() {
        ServerTickEvents.END_WORLD_TICK.register(world -> {
            if (!(world instanceof ServerWorld)) return;
            int currentTick = world.getServer().getTicks();

            // TrapJawBlock (челюсти)
            // можно сделать прикольную бензопилу, если убрать задержку для напольной ловушки хаха
            if (currentTick - lastTriggerTime >= 7) {
                for (PlayerEntity player : world.getPlayers()) {
                    BlockPos below = player.getBlockPos().down();
                    Block block = world.getBlockState(below).getBlock();
                    if (block instanceof TrapJawBlock) {
                        TrapJawBlock.spawnFangsAt(world, below);
                        lastTriggerTime = currentTick;
                        break;
                    }
                }
            }

            // ArrowTrapBlock (стрелы)
            if (currentTick - lastArrowTriggerTime >= 15) {
                for (PlayerEntity player : world.getPlayers()) {
                    int range = 20;
                    BlockPos start = player.getBlockPos().add(-range, -1, -range);
                    BlockPos end = player.getBlockPos().add(range, 2, range);
                    for (BlockPos pos : BlockPos.iterate(start, end)) {
                        Block block = world.getBlockState(pos).getBlock();
                        if (block instanceof ArrowTrapBlock) {
                            ArrowTrapBlock.tick(world, pos);
                        }
                    }
                }
                lastArrowTriggerTime = currentTick;
            }

            // LaserTrapBlock (лазер)
            for (PlayerEntity player : world.getPlayers()) {
                int range = 20;
                BlockPos start = player.getBlockPos().add(-range, -1, -range);
                BlockPos end = player.getBlockPos().add(range, 2, range);
                for (BlockPos pos : BlockPos.iterate(start, end)) {
                    Block block = world.getBlockState(pos).getBlock();
                    if (block instanceof LaserTrapBlock) {
                        LaserTrapBlock.tick(world, pos);
                    }
                }
            }
        });
    }
}