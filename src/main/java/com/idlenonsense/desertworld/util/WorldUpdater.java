package com.idlenonsense.desertworld.util;

import com.idlenonsense.desertworld.converter.BlocksConverter;
import com.idlenonsense.desertworld.converter.EntitiesConverter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldUpdater {
    public static final int RADIUS = 3;
    public static final int RADIUS_TEST = 15;

    /*public static void worldDeserted(BlockPos pos, World world) {
        worldDeserted(pos, world, RADIUS);
    }*/

    public static void worldDeserted(BlockPos pos, World world, int radius) {
        try {
            if (world == null || pos == null || radius < 1) return;
            BlocksConverter.convertBlocksWithAffectOnCurrency(pos, world, radius);
            EntitiesConverter.convertEntities(pos, radius, world);
        } catch (Exception ignored) { }
    }

    public static void worldDesertedTest(BlockPos pos, World world) {
        try {
            if (world == null || pos == null || RADIUS_TEST < 1) return;
            BlocksConverter.convertBlocksWithAffectOnCurrency(pos, world, RADIUS_TEST);
            EntitiesConverter.convertEntities(pos, RADIUS_TEST, world);
        } catch (Exception ignored) { }
    }
}