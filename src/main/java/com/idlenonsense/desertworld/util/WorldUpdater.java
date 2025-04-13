package com.idlenonsense.desertworld.util;

import com.idlenonsense.desertworld.converter.BlocksConverter;
import com.idlenonsense.desertworld.converter.EntitiesConverter;
import com.idlenonsense.desertworld.currency.DesertCurrency;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

public class WorldUpdater {
    public static final int RADIUS = 3;
    public static final float BLOCK_PERCENTAGE_AFFECT = 0.00001f;

    public static void worldDeserted(BlockPos pos, World world, ServerPlayerEntity player) {
        worldDeserted(pos, world, RADIUS, player);
    }

    public static void worldDeserted(BlockPos pos, World world, int radius, ServerPlayerEntity player) {
        try {
            if (world == null || player == null
                    || world.isClient
                    || pos == null
                    || radius < 1) return;

            BlocksConverter.convertBlocksWithAffectOnCurrency(pos, world, radius);
            EntitiesConverter.convertEntities(pos, radius, world);
        } catch (Exception ignored) {

        }
    }

    private static void updateCurrency() {
        updateCurrency(BLOCK_PERCENTAGE_AFFECT);
    }

    private static void updateCurrency(float amount) {
        DesertCurrency.getInstance().add(amount);
    }
}
