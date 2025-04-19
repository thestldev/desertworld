package org.thesalutyt.features.sandstorm;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class Sandstorm {
    private int age = 0;
    private Vec3d velocity;
    private LivingEntity owner;
    private float size = 6.0F;
    private float yLevel = 5.0F;
    private World world;
    private Vec3d pos;

    private static final int LIFE_DURATION = 300;

    public Sandstorm(LivingEntity owner, BlockPos pos, World world) {
        this.owner = owner;
        this.world = world;
        this.pos = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
    }

    public void tick() {}

    public Vec3d getPos() {
        return this.pos;
    }
}
