package com.idlenonsense.desertworld.events.impl;

import com.idlenonsense.desertworld.abilities.Abilities;
import com.idlenonsense.desertworld.events.listeners.StepBlockListener;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.thesalutyt.utils.SkinManager;

public class AbilitiesEventImpl {
    private static final int SANDSTONE_DELAY = 20;
    private static int lastSandstoneTick = 0;

    public static void register() {
        Abilities.getInstance().addAbility(
                new Abilities.SpecialAbility("sandstone", 0)
        );

        StepBlockListener.addCallback(AbilitiesEventImpl::sandstoneStepOn);
    }

    private static void sandstoneStepOn(ServerWorld world, PlayerEntity player, BlockPos pos, Block block) {
        float currentPercent = Abilities.getInstance().getPercent("sandstone");

        if (!block.getDefaultState().equals(Blocks.SANDSTONE.getDefaultState())) {
            if (!(currentPercent > 0) || world.getTime() - lastSandstoneTick < SANDSTONE_DELAY) return;
            affectSandstone(world, currentPercent - 1);
            return;
        }
        if (world.getTime() - lastSandstoneTick < SANDSTONE_DELAY) return;

        affectSandstone(world, currentPercent + 1);
    }

    private static void affectSandstone(ServerWorld world, float percent) {
        float currentPercent = Abilities.getInstance().getPercent("sandstone");
        if (percent < 0 || percent > 5) return;
        if (currentPercent == percent) return;

        lastSandstoneTick = (int) world.getTime();

        Abilities.getInstance().set("sandstone", percent, true);
        processSkin(percent);
    }

    private static void processSkin(float percent) {
        if (percent == 0) {
            SkinManager.resetSkin();
            return;
        }

        SkinManager.setCurrentSkin(new Identifier("desertworld", "textures/entity/abilities/uni_sandstone" + (int)percent + ".png"));
    }
}
