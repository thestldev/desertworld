package com.idlenonsense.desertworld.events.impl;

import com.idlenonsense.desertworld.abilities.Abilities;
import com.idlenonsense.desertworld.events.ServerTickEvent;
import com.idlenonsense.desertworld.events.listeners.BreakBlockListener;
import com.idlenonsense.desertworld.events.listeners.StepBlockListener;
import com.idlenonsense.desertworld.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.thesalutyt.utils.SkinManager;

import java.util.List;

public class AbilitiesEventImpl {
    private static final int SANDSTONE_DELAY = 20;
    private static int lastSandstoneTick = 0;
    private static double cachedHealth;

    public static void register() {
        Abilities.getInstance().addAbility(
                new Abilities.SpecialAbility("sandstone", 0)
        );

        StepBlockListener.addCallback(AbilitiesEventImpl::sandstoneStepOn);
    }

    private static void sandstoneStepOn(ServerWorld world, PlayerEntity player, BlockPos pos, Block block) {
        float currentPercent = Abilities.getInstance().getPercent("sandstone");

        //System.out.println("!! cached health: " + cachedHealth + ", current health: " + player.getHealth());
        if (world.getTime() - lastSandstoneTick < SANDSTONE_DELAY) return;

        if (cachedHealth == 0) cachedHealth = player.getHealth();
        if (cachedHealth < player.getHealth()) {
            cachedHealth = player.getHealth();
            return;
        }

        if (cachedHealth - player.getHealth() >= 2) {
            affectSandstone(world, currentPercent - 1, player);
            cachedHealth = player.getHealth();
            return;
        }

        if (!block.getDefaultState().equals(Blocks.SANDSTONE.getDefaultState())) return;

        affectSandstone(world, currentPercent + 1, player);
    }

    private static void affectSandstone(ServerWorld world, float percent, PlayerEntity player) {
        float currentPercent = Abilities.getInstance().getPercent("sandstone");
        if (percent < 0 || percent > 5) return;
        if (currentPercent == percent) return;

        lastSandstoneTick = (int) world.getTime();

        Abilities.getInstance().set("sandstone", percent, true);

        player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, Integer.MAX_VALUE, (int) percent/3, false, false));
        processSkin(percent);
    }

    private static void processSkin(float percent) {
        if (percent == 0) {
            SkinManager.resetSkin();
            return;
        }

        SkinManager.setCurrentSkin(new Identifier("desertworld", "textures/entity/abilities/uni_sandstone" + (int)percent + ".png"));
    }

    private static boolean isCursedTool(ItemStack stack) {
        return stack.getItem().equals(ModItems.CURSED_PICKAXE)
                || stack.getItem().equals(ModItems.CURSED_AXE)
                || stack.getItem().equals(ModItems.CURSED_SHOVEL)
                || stack.getItem().equals(ModItems.CURSED_SWORD);
    }
}
