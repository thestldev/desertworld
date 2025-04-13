package com.idlenonsense.desertworld.converter;

import com.idlenonsense.desertworld.currency.DesertCurrency;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.CactusBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import static com.idlenonsense.desertworld.util.WorldUpdater.BLOCK_PERCENTAGE_AFFECT;

public class BlocksConverter {
    public static final List<Block> SAND_LIST = List.of(Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.PODZOL);
    public static final List<Block> SANDSTONE_LIST = List.of(Blocks.STONE, Blocks.GRANITE, Blocks.ANDESITE, Blocks.COBBLESTONE);
    public static final List<Block> CACTUS_LIST = List.of(Blocks.OAK_LOG, Blocks.ACACIA_LOG, Blocks.SPRUCE_LOG, Blocks.BIRCH_LOG,
            Blocks.DARK_OAK_LOG, Blocks.JUNGLE_LOG, Blocks.MANGROVE_LOG);
    public static final List<Block> AFFECT_LIST = List.of(
            Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.PODZOL,
            Blocks.STONE, Blocks.GRANITE, Blocks.ANDESITE, Blocks.COBBLESTONE
    );
    private static final Random RANDOM = new Random();

    public static void convertBlocks(BlockPos center, World world, int radius) {
        BlockPos[] blocks = getBlocksForRadius(center, radius);

        for (BlockPos pos : blocks) {
            Block block = world.getBlockState(pos).getBlock();
            world.setBlockState(pos, getConvertedBlock(block).getDefaultState());
        }
    }

    public static void convertBlocksWithAffectOnCurrency(BlockPos center, World world, int radius) {
        BlockPos[] blocks = getBlocksForRadius(center, radius);

        for (BlockPos pos : blocks) {
            Block block = world.getBlockState(pos).getBlock();
            Block convertedBlock = getConvertedBlock(block);

            if (convertedBlock == block) continue;

            world.setBlockState(pos, convertedBlock.getDefaultState());
            if (AFFECT_LIST.contains(block)) updateCurrency();
            placeFeature(pos, world, block);
        }
    }

    private static void updateCurrency() {
        DesertCurrency.getInstance().add(BLOCK_PERCENTAGE_AFFECT);
    }

    public static void convertBlocks(BlockPos[] blocks, World world) {
        for (BlockPos pos : blocks) {
            Block block = world.getBlockState(pos).getBlock();
            Block convertedBlock = getConvertedBlock(block);

            if (convertedBlock == block) continue;
            world.setBlockState(pos, convertedBlock.getDefaultState());
            placeFeature(pos, world, block);
        }
    }

    public static BlockPos[] getBlocksForRadius(BlockPos center, int radius) {
        return getListBlocksForRadius(center, radius).toArray(BlockPos[]::new);
    }

    public static ArrayList<BlockPos> getListBlocksForRadius(BlockPos center, int radius) {
        ArrayList<BlockPos> blocks = new ArrayList<>();

        for (int x = -radius; x <= radius; x++) {
            for (int y = -2; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos pos = center.add(x, y, z);
                    blocks.add(pos);
                }
            }
        }

        return blocks;
    }

    private static Block getConvertedBlock(Block block) {
        if (block == Blocks.WATER
                || block == Blocks.KELP_PLANT
                || block == Blocks.KELP
                || block == Blocks.SEAGRASS
                || block == Blocks.TALL_SEAGRASS
        ) {
            return Blocks.AIR;
        }

        if (SAND_LIST.contains(block)) {
            return Blocks.SAND;
        }

        if (CACTUS_LIST.contains(block)) {
            return Blocks.CACTUS;
        }

        if (SANDSTONE_LIST.contains(block)) {
            return Blocks.SANDSTONE;
        }

        return block;
    }

    private static void placeFeature(BlockPos pos, World world, Block block) {
    }

    private record Feature(Consumer<World> consumer) {
        public void apply(World world) {
            consumer.accept(world);
        }
    }
}
