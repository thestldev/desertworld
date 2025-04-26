package com.idlenonsense.desertworld.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Arm;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import java.util.ArrayList;

public class SandstormEntity extends LivingEntity {
    private int age = 0;
    private float size = 6.0F;
    private float yLevel = 5.0F;
    private LivingEntity owner;
    private Vec3d velocity = new Vec3d(0.0D, 0.0D, 0.0D);

    private final Iterable<ItemStack> armor = new ArrayList<>();
    private final ItemStack currentStack = new ItemStack(ItemStack.EMPTY.getItem());

    public SandstormEntity(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
        this.age = 0;
        this.size = 6.0F;
        this.yLevel = 5.0F;
        this.owner = null;
    }

    @Override
    protected void initDataTracker() {

    }

    public static DefaultAttributeContainer.Builder createSandstormAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 10000.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.0D);
    }

    @Override
    public void tick() {
        ++this.age;
        if (world instanceof ServerWorld serverWorld) {
            if (this.age > 300) {
                this.discard();
                return;
            }

            if (this.age % 3 == 0) {
                for (int i = 0; (double) i < (double) this.size * 1.4D; ++i) {
                    spawnTornadoParticles(serverWorld, (float) i / 1.4F, (float) i / 6.0F);
                }
            }
        }
    }

    @Override
    public Arm getMainArm() {
        return null;
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Override
    public Iterable<ItemStack> getArmorItems() {
        return null;
    }

    @Override
    public ItemStack getEquippedStack(EquipmentSlot slot) {
        return null;
    }

    @Override
    public void equipStack(EquipmentSlot slot, ItemStack stack) {

    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {

    }

    @Override
    public Packet<?> createSpawnPacket() {
        return null;
    }

    private void spawnTornadoParticles(ServerWorld world, float y, float radius) {
        int count = 20;
        for (int i = 0; i < count; ++i) {
            float angle = (float) i / (float) count * 6.2831855F + this.getYaw() * 3.1415927F / 180.0F;
            double cos = Math.cos(angle) * radius;
            double sin = Math.sin(angle) * radius;
            world.spawnParticles(ParticleTypes.WHITE_ASH,
                    this.getX() + cos,
                    this.getY() + y,
                    this.getZ() + sin,
                    1, 0.4D, 0.4D, 0.4D, 0.0D);
        }
    }
}
