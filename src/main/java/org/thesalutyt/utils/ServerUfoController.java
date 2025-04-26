package org.thesalutyt.utils;

import com.idlenonsense.desertworld.entity.ModEntities;
import com.idlenonsense.desertworld.entity.client.UfoEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record ServerUfoController(UfoEntity entity, ServerWorld world, UUID playerUuid) {
    private static final List<ServerUfoController> controllers = new ArrayList<>();

    public ServerUfoController(UfoEntity entity, ServerWorld world, UUID playerUuid) {
        this.entity = entity;
        this.world = world;
        this.playerUuid = playerUuid;

        controllers.add(this);
    }

    public UUID uuid() {
        return entity.getUuid();
    }

    public void attackEntity(UUID entity) {
        Entity target = world.getEntity(entity);
        if (target != null) {
            this.attackEntity(target);
        }
    }

    public void attackEntity(Entity target) {
        target.damage(DamageSource.MAGIC, 2.0F);
    }

    public void attackBlock(BlockPos pos) {
        world.breakBlock(new BlockPos(pos), false);
    }

    private void sendLaserParticles(Vec3d from, Vec3d to) {
        sendLaserParticles(new BlockPos(from), new BlockPos(to));
    }

    public void sendLaserParticles(BlockPos from, BlockPos to) {
        int count = 10;
        BlockPos step = getStepForParticle(from, to, count);
        for (int i = 0; i < count; i++) {
            world.addParticle(
                    ParticleTypes.FLAME,
                    from.getX() + step.getX(),
                    from.getY() + step.getY(),
                    from.getZ() + step.getZ(),
                    0.0D,
                    0.0D,
                    0.0D
            );
        }
    }

    private static BlockPos getStepForParticle(BlockPos from, BlockPos to, int count) {
        return new BlockPos(
                from.getX() + (to.getX() - from.getX()) / count,
                from.getY() + (to.getY() - from.getY()) / count,
                from.getZ() + (to.getZ() - from.getZ()) / count
        );
    }

    private Entity getEntityByPos(BlockPos pos) {
        Box box = new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 2, pos.getY() + 2, pos.getZ() + 2);
        return this.world.getOtherEntities(this.entity, box)
                .stream()
                .filter(e -> e != this.entity && !(e instanceof PlayerEntity) && e.isAttackable())
                .findAny()
                .orElseGet(() -> null);
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public static ServerUfoController getControllerByUUID(UUID uuid) {
        return controllers.stream()
                .filter(controller -> controller.getPlayerUuid().equals(uuid))
                .findFirst()
                .orElse(null);
    }

    public static ServerUfoController createController(UfoEntity entity, ServerWorld world, UUID playerUuid) {
        return new ServerUfoController(entity, world, playerUuid);
    }

    public static ServerUfoController createUfo(ServerWorld world, BlockPos pos, UUID playerUuid) {
        UfoEntity entity = new UfoEntity(ModEntities.UFO_ENTITY, world);
        world.spawnEntity(entity);
        entity.setPos(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
        ServerUfoController controller = new ServerUfoController(entity, world, playerUuid);
        controllers.add(controller);
        //System.out.println("UFO created at: " + pos + " with UUID: " + playerUuid);

        return controller;
    }
}
