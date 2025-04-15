package com.idlenonsense.desertworld.block.traps;

import net.minecraft.block.Block;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import java.util.Arrays;

public class ArrowTrapBlock extends Block {
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
                case NORTH -> offsetZ = -0.7;
                case SOUTH -> offsetZ = 1.7;
                case EAST -> offsetX = 1.7;
                case WEST -> offsetX = -0.7;
            }
            ArrowEntity arrow = new ArrowEntity(world, pos.getX() + offsetX, pos.getY() + offsetY, pos.getZ() + offsetZ);
            switch (direction) {
                case NORTH -> velocityZ = -0.6;
                case SOUTH -> velocityZ = 0.6;
                case EAST -> velocityX = 0.6;
                case WEST -> velocityX = -0.6;
            }
            BlockPos adjacentPos = pos.offset(direction);
            if (!world.getBlockState(adjacentPos).isAir()) return;
            arrow.setVelocity(velocityX, 0.0, velocityZ);
            arrow.setDamage(2.0);
            arrow.setNoGravity(true);
            arrow.pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED;
            world.spawnEntity(arrow);
        });
    }
}
