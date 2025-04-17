package com.idlenonsense.desertworld.block.traps;

import com.idlenonsense.desertworld.block.traps.resource.ITrapBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Arrays;

public class ArrowTrapBlock extends Block implements ITrapBlock {
    private int lastUsageTick = 0;

    public ArrowTrapBlock(Settings settings) { super(settings); }
    public static void tick(ServerWorld world, BlockPos pos) {
        Direction[] directions = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};
        Arrays.stream(directions).forEach(direction -> {
            double offsetX = 0.5;
            double offsetY = 0.6;
            double offsetZ = 0.5;
            double velocityX = 0.0;
            double velocityZ = 0.0;
            switch (direction) {
                case NORTH -> {
                    offsetZ = -0.7;
                    velocityZ = -0.6;
                }
                case SOUTH -> {
                    offsetZ = 1.7;
                    velocityZ = 0.6;
                }
                case EAST -> {
                    offsetX = 1.7;
                    velocityX = 0.6;
                }
                case WEST -> {
                    offsetX = -0.7;
                    velocityX = -0.6;
                }
            }
            ArrowEntity arrow = new ArrowEntity(world, pos.getX() + offsetX, pos.getY() + offsetY, pos.getZ() + offsetZ);

            BlockPos adjacentPos = pos.offset(direction);
            if (!world.getBlockState(adjacentPos).isAir()) return;
            arrow.setVelocity(velocityX, 0.0, velocityZ);
            arrow.setDamage(2.0);
            arrow.setNoGravity(true);
            arrow.pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED;
            world.spawnEntity(arrow);
        });
    }

    @Override
    public void stepOn(ServerWorld world, PlayerEntity player, BlockPos pos) {

    }

    @Override
    public void trapActiveTick(ServerWorld world, PlayerEntity player, BlockPos pos) {
        int currentTick = world.getServer().getTicks();

        if (currentTick - lastUsageTick < getDelay()) return;
        lastUsageTick = currentTick;

        tick(world, pos);
    }

    @Override
    public int getDelay() {
        return 15;
    }
}
