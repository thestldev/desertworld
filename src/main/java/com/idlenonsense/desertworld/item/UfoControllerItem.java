package com.idlenonsense.desertworld.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.thesalutyt.network.packets.UfoAttackPacket;
import org.thesalutyt.utils.ServerUfoController;

import java.util.UUID;

public class UfoControllerItem extends Item {
    private UUID ufoID = null;

    public UfoControllerItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) {
            processClient(user);
        } else {
            processServer(world, user, hand);
        }
        return TypedActionResult.success(user.getStackInHand(hand), world.isClient());
    }

    private void processClient(PlayerEntity user) {
        if (ufoID == null) {
            return;
        }

        BlockHitResult hit = ((BlockHitResult) user.raycast(10.0D, 0.0F, false));
        UfoAttackPacket.send(hit, 1, ufoID);
    }

    private void processServer(World world, PlayerEntity user, Hand hand) {
        if (ufoID == null) {
            createUfo((ServerWorld) world, user.getBlockPos());
        }
    }

    private void createUfo(ServerWorld world, BlockPos pos) {
        ufoID = ServerUfoController
                .createUfo(world, pos)
                .uuid();
    }

    public UUID getUfoID() {
        return ufoID;
    }

}
