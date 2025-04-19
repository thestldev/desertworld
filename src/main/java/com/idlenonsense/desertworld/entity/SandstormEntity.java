package com.idlenonsense.desertworld.entity;

import net.fabricmc.loader.impl.game.minecraft.Hooks;
import net.minecraft.client.util.NetworkUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Arm;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class SandstormEntity extends PathAwareEntity {
    private int age = 0;
    public float size = 6.0F;
    public float yLevel = 5.0F;
    public LivingEntity owner;
    public Vec3d velocity = new Vec3d(0.0D, 0.0D, 0.0D);

    private final Iterable<ItemStack> armor = new ArrayList<>();
    private final ItemStack currentStack = new ItemStack(ItemStack.EMPTY.getItem());

    public SandstormEntity(EntityType<? extends PathAwareEntity> type, World world) {
        super(type, world);
    }

    public static DefaultAttributeContainer.Builder createSandstormAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 10000.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.0D)
                ;
    }

    @Override
    public void tick() {
        ++this.age;
        if (world instanceof ServerWorld serverWorld) {
            List<Entity> entities;
            if (this.age > 300) {
                this.discard();
                entities = serverWorld.getOtherEntities(this.owner, new Box(this.getPos().x, this.getPos().y, this.getPos().z, this.getPos().x + 5, this.getPos().y + 5, this.getPos().z + 5));
                entities.forEach((entity) -> {
                    entity.setNoGravity(false);
                });
                return;
            }

            if (this.age % 3 == 0) {
                for (int i = 0; (double) i < (double) this.size * 1.4D; ++i) {
                    this.spawnParticleRing((float) i / 1.4F, (float) i / 6.0F, Math.max(1, i / 4) * 2, serverWorld);
                }
            }

            if (this.age % 20 == 0) {
                this.yLevel = Math.min(this.yLevel + (float) this.random.nextBetween(-2, 2), 3.0F);
                this.size = this.size * 0.8F;
            }

            this.setYaw(this.getYaw() - 3.0F);
            entities = serverWorld.getOtherEntities(this, new Box(this.getBlockPos()).expand(this.size), (entity) -> {
                return entity instanceof PathAwareEntity && !(entity instanceof PlayerEntity);
            });
            entities.forEach(this::computeEntity);
            this.setVelocity(this.velocity);
        }
    }

    @Override
    public Arm getMainArm() {
        return null;
    }

    public void spawnParticleRing(float y, float radius, int count, ServerWorld serverWorld) {
        for (int i = 0; i < count; ++i) {
            float radians = (float) i / (float) count * 6.2831855F + this.getYaw() * 3.1415927F / 180.0F;
            double cos = Math.cos(radians) * radius;
            double sin = Math.sin(radians) * radius;
            if (Math.random() > 0.04D) {
                serverWorld.getPlayers().forEach((serverPlayer) -> {
                    serverWorld.spawnParticles(serverPlayer, ParticleTypes.FLASH, true, this.getX() + (double) cos, this.getY() + (double) y, this.getZ() + (double) sin, 1, 0.4D, 0.4D, 0.4D, 0.0D);
                });
            } else {
                serverWorld.getPlayers().forEach((serverPlayer) -> {
                    serverWorld.spawnParticles(serverPlayer, ParticleTypes.FIREWORK, true, this.getX() + (double) cos, this.getY() + (double) y, this.getZ() + (double) sin, 1, 0.4D, 0.4D, 0.4D, 0.0D);
                });
            }
        }
    }

    public void computeEntity(Entity entity) {
        Vec3d towards = this.getPos().add(0.0D, (double) this.yLevel, 0.0D).subtract(entity.getPos()).normalize();
        Vec3d move = towards.rotateY(75.0F);
        entity.setNoGravity(true);
        entity.setVelocity(move.multiply(0.5D, 0.5D, 0.5D));
        double distance = entity.getPos().distanceTo(this.getPos().add(0.0D, (double) this.yLevel, 0.0D));
        if (distance < 25.0D) {
            if (this.owner == null) {
                entity.damage(DamageSource.MAGIC, 2.0F);
            } else {
                entity.damage(DamageSource.mob(this.owner), 2.0F);
            }
        }
    }

    @Override
    protected void initDataTracker() {
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound tag) {
        this.size = tag.getFloat("Size");
        this.age = tag.getInt("Ticks");
    }

    @Override
    public Iterable<ItemStack> getArmorItems() {
        return armor;
    }

    @Override
    public ItemStack getEquippedStack(EquipmentSlot slot) {
        return currentStack;
    }

    @Override
    public void equipStack(EquipmentSlot slot, ItemStack stack) {

    }

    @Override
    public void writeCustomDataToNbt(NbtCompound tag) {
        tag.putFloat("Size", this.size);
        tag.putInt("Ticks", this.age);
    }
}
