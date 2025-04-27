package com.idlenonsense.desertworld.item;

import com.idlenonsense.desertworld.events.ServerTickEvent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class SandstormItem extends Item {
    public SandstormItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        ServerTickEvent.handleSandstormItemUse(user, world);
        world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_SAND_PLACE, SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
        return TypedActionResult.success(itemStack, world.isClient());
    }
}