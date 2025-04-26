package com.idlenonsense.desertworld.block;

import com.idlenonsense.desertworld.bar.DesertBar;
import com.idlenonsense.desertworld.events.ServerTickEvent;
import com.idlenonsense.desertworld.util.WorldUpdater;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.List;

public class HourglassBlock extends Block {
    private static int currentRadius = 1;

    private static final VoxelShape COLLISION_SHAPE = VoxelShapes.union(
            VoxelShapes.cuboid(0.1, 0.1, 0.1, 0.9, 0.9, 0.9), // Пример формы коллизии
            VoxelShapes.cuboid(0.2, 0.2, 0.2, 0.8, 0.8, 0.8)
    );

    private final List<Block> sandList = List.of(Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.PODZOL);
    private final List<Block> sandstoneList = List.of(Blocks.STONE, Blocks.GRANITE, Blocks.ANDESITE, Blocks.COBBLESTONE);
    private final List<Block> cactusList = List.of(Blocks.OAK_LOG, Blocks.ACACIA_LOG, Blocks.SPRUCE_LOG, Blocks.BIRCH_LOG,
            Blocks.DARK_OAK_LOG, Blocks.JUNGLE_LOG, Blocks.MANGROVE_LOG);

    public HourglassBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return COLLISION_SHAPE;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return COLLISION_SHAPE;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }

        if (currentRadius < 15) {
            world.playSound(
                    null,
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    SoundEvents.BLOCK_AMETHYST_CLUSTER_HIT,
                    SoundCategory.NEUTRAL,
                    0.6F,
                    0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F)
            );
            //Потом поправим))
            //WorldUpdater.worldDeserted(pos, world, currentRadius, ((ServerPlayerEntity) player));
            ServerTickEvent.syncCurrencyWithBar((ServerPlayerEntity) player);
            currentRadius++;
        } else {
            currentRadius = 1;
            world.breakBlock(pos, true);
        }
        return ActionResult.SUCCESS;
    }
}
