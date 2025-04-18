package com.idlenonsense.desertworld.block.traps.resource;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ITrapBlock {
    void stepOn(ServerWorld world, PlayerEntity player, BlockPos pos);
    void trapActiveTick(ServerWorld world, PlayerEntity player, BlockPos pos);
    int getDelay();
}
