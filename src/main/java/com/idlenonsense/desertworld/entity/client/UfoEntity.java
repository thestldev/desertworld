package com.idlenonsense.desertworld.entity.client;

import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Arm;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.Iterator;
import java.util.List;

public class UfoEntity extends LivingEntity implements IAnimatable {
    private int age = 0;
    public Vec3d offset = new Vec3d(0.0D, 0.0D, 0.0D);
    public LivingEntity owner;
    private boolean canMove = true;
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public UfoEntity(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
    }

    public static DefaultAttributeContainer.Builder createUfoAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 10000.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.0D);
    }

    public void moveToPlayer(Vec3d playerPos) {
        double distance = this.getPos().distanceTo(playerPos);

        if (distance > 10) {
            double offsetX = (Math.random() - 0.5) * 10;
            double offsetZ = (Math.random() - 0.5) * 10;
            Vec3d randomPos = new Vec3d(playerPos.x + offsetX, playerPos.y + 3, playerPos.z + offsetZ);
            this.setPos(randomPos.x, randomPos.y, randomPos.z);
        } else {
            double speed = 0.1;
            Vec3d direction = playerPos.subtract(this.getPos()).normalize();
            Vec3d newPos = this.getPos().add(direction.multiply(speed));
            double targetY = playerPos.y + 3;
            double currentY = this.getPos().y;
            double newY = currentY + (targetY - currentY) * speed;
            this.setPos(newPos.x, newY, newPos.z);
        }
    }

    @Override
    public void registerControllers(AnimationData animationData) { }

    @Override
    public void tick() {
        super.tick();
        World world = this.world;
        if (world instanceof ServerWorld) {
            if (!canMove) return;

//            ServerWorld serverWorld = (ServerWorld) world;
//            ++this.age;
//            if (this.owner == null) {
//                this.remove(RemovalReason.DISCARDED);
//            } else {
//                this.offset = this.offset.multiply(0.95F);
//                if (this.age % 20 == 0) {
//                    LivingEntity closest = null;
//                    double closestDistance = Double.MAX_VALUE;
//                    Iterator<Entity> iterator = serverWorld.getOtherEntities(this, Box.from(this.getPos().add(12.0D, 12.0D, 12.0D))).iterator();
//
//                    double distance;
//                    while (iterator.hasNext()) {
//                        Entity entity = iterator.next();
//                        if (entity instanceof LivingEntity) {
//                            LivingEntity livingEntity = (LivingEntity) entity;
//                            if (!(livingEntity instanceof PlayerEntity) && livingEntity.canSee(this)) {
//                                distance = entity.getPos().distanceTo(this.getPos());
//                                if (distance < closestDistance) {
//                                    closest = livingEntity;
//                                    closestDistance = distance;
//                                }
//                            }
//                        }
//                    }
//
//                    if (closest != null) {
//                        closest.damage(DamageSource.mob(this.owner), 8.0F);
//                        Vec3d offset = closest.getPos().add(0.0D, closest.getHeight() / 2.0D, 0.0D).subtract(this.getPos());
//                        offset = offset.normalize();
//
//                        for (distance = 0.0D; distance < distance; distance += 0.1D) {
//                            serverWorld.spawnParticles(ParticleTypes.FLAME, this.getX() + offset.x * distance, this.getY() + offset.y * distance, this.getZ() + offset.z * distance, 3, 0.05D, 0.05D, 0.05D, 0.0D);
//                        }
//
//                        this.setVelocity(this.getVelocity().add(offset.x * 0.5F, offset.y * 0.5F, offset.z * 0.5F));
//                        this.offset = this.offset.add((float) (-offset.x), (float) (-offset.y), (float) (-offset.z));
//                    }
//                }
//            }
        }
    }

    public void freeze() {
        this.canMove = false;
    }

    public void unfreeze() {
        this.canMove = true;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        return false;
    }

    @Override
    public Iterable<ItemStack> getArmorItems() {
        return List.of();
    }

    @Override
    public ItemStack getEquippedStack(EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void equipStack(EquipmentSlot slot, ItemStack stack) { }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        return EntityDimensions.fixed(1.0F, 1.0F);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public Arm getMainArm() {
        return Arm.RIGHT;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}

