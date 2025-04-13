package com.idlenonsense.desertworld.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;


public class MummyHandItem extends Item {
    public MummyHandItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (!(entity instanceof HostileEntity)) return ActionResult.PASS;

        World world = entity.getWorld();

        for (int i = 0; i < 30; i++) {
            double d = 1 * 0.01;
            double e = 7 * 0.01;
            double f = 12 * 0.01;
            world.addParticle(ParticleTypes.LARGE_SMOKE, entity.getX(), entity.getY(), entity.getZ(), d, e, f);
        }
        entity.remove(Entity.RemovalReason.DISCARDED);
        return ActionResult.SUCCESS;
    }
}
