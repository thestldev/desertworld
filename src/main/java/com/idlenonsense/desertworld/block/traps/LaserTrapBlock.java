package com.idlenonsense.desertworld.block.traps;

import com.idlenonsense.desertworld.block.traps.resource.ITrapBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.List;

public class LaserTrapBlock extends Block implements ITrapBlock {
    private int lastUsageTick = 0;

    public LaserTrapBlock(Settings settings) { super(settings); }
    public static void tick(ServerWorld world, BlockPos pos) {
        Vec3d origin = new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        Direction[] directions = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};
        DustParticleEffect particle = new DustParticleEffect(new Vec3f(1.0F, 0.0F, 0.0F), 1.0F);
        for (Direction direction : directions) {
            Vec3i vec = direction.getVector();
            Vec3d end = origin.add(vec.getX() * 16.0, vec.getY() * 16.0, vec.getZ() * 16.0);

            for (int i = 1; i <= 16; i++) {
                BlockPos testPos = pos.add(vec.getX() * i, vec.getY() * i, vec.getZ() * i);
                if (!world.getBlockState(testPos).isAir()) {
                    end = new Vec3d(testPos.getX() + 0.5, testPos.getY() + 0.5, testPos.getZ() + 0.5);
                    break;
                }
            }

            Vec3d dir = end.subtract(origin).normalize();
            double distance = origin.distanceTo(end);
            int steps = (int) (distance * 16);

            for (int i = 0; i <= steps; i++) {
                double progress = (double) i / steps;
                Vec3d point = origin.add(dir.multiply(progress * distance));
                world.spawnParticles(particle, point.x, point.y, point.z, 1, 0, 0, 0, 0);
                List<PlayerEntity> players = world.getNonSpectatingEntities(
                        PlayerEntity.class,
                        new Box(point.x - 0.25, point.y - 0.25, point.z - 0.25, point.x + 0.25, point.y + 0.25, point.z + 0.25)
                );
                for (PlayerEntity player : players) {
                    player.damage(DamageSource.MAGIC, 14.0F);
                }
            }
        }
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
        return 20;
    }
}