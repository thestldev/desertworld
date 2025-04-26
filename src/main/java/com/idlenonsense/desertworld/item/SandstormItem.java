package com.idlenonsense.desertworld.item;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SandstormItem extends Item {
    private static final int PARTICLE_SPAWN_INTERVAL = 20;
    private static final int TOTAL_TICKS = 120;

    private int ticksElapsed = 0;

    public SandstormItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (!world.isClient) {
            ticksElapsed = 0;
            BlockPos userPos = user.getBlockPos();
            startSandstorm(world, userPos, user);
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        return TypedActionResult.success(itemStack, world.isClient());
    }

    private void startSandstorm(World world, BlockPos pos, PlayerEntity user) {
        if (world instanceof ServerWorld serverWorld) {
            spawnParticles(serverWorld, pos);
            ServerTickEvents.START_SERVER_TICK.register(server -> {
                if (ticksElapsed < TOTAL_TICKS) {
                    if (ticksElapsed % PARTICLE_SPAWN_INTERVAL == 0) {
                        spawnParticles(serverWorld, pos);
                    }
                    ticksElapsed++;
                }
            });
        }
        world.playSound(null, pos, SoundEvents.BLOCK_SAND_PLACE, SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
    }

    private void spawnParticles(ServerWorld world, BlockPos pos) {
        int radius = 5;
        int count = 200;
        int layers = 7;
        double step = radius / (double) layers;
        for (int j = 0; j < layers; j++) {
            double currentRadius = radius - step * j;
            for (int i = 0; i < count; ++i) {
                float angle = (float) i / count * 6.2831855F;
                double cos = Math.cos(angle) * currentRadius;
                double sin = Math.sin(angle) * currentRadius;
                double height = pos.getY() + 7.5D - (j * 1.0D);
                world.spawnParticles(ParticleTypes.CLOUD,
                        pos.getX() + cos,
                        height,
                        pos.getZ() + sin,
                        1, 0.4D, 0.4D, 0.4D, 0.0D);
            }
        }
    }
}
