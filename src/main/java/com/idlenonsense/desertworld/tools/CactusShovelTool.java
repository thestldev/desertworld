package com.idlenonsense.desertworld.tools;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CactusShovelTool extends ShovelItem {
    public CactusShovelTool(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        // Наносим урон игроку
        user.damage(DamageSource.CACTUS, 0.5F);

        return TypedActionResult.success(stack, world.isClient());
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        // Наносим урон игроку при атаке
        if (miner instanceof PlayerEntity) {
            miner.damage(DamageSource.CACTUS, 0.5F);
        }
        return super.postMine(stack, world, state, pos, miner);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // Наносим урон игроку при атаке
        if (attacker instanceof PlayerEntity) {
            attacker.damage(DamageSource.CACTUS, 1.0F);
        }
        return super.postHit(stack, target, attacker);
    }
}
