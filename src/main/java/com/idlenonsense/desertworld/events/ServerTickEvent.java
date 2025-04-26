package com.idlenonsense.desertworld.events;

import com.idlenonsense.desertworld.abilities.Abilities;
import com.idlenonsense.desertworld.bar.DesertBar;
import com.idlenonsense.desertworld.currency.DesertCurrency;
import com.idlenonsense.desertworld.entity.client.UfoEntity;
import com.idlenonsense.desertworld.item.ModItems;
import com.idlenonsense.desertworld.item.UfoControllerItem;
import com.idlenonsense.desertworld.util.WorldUpdater;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.thesalutyt.utils.ServerUfoController;

import java.util.LinkedList;
import java.util.UUID;

public class ServerTickEvent {
    private static BlockPos cachedPos = null;
    private static final LinkedList<ServerTickCallback> callbacks = new LinkedList<>();

    public static void register() {
        ServerTickEvents.START_SERVER_TICK
                .register(minecraftServer -> {
                    try {
                        if (minecraftServer.getPlayerManager().getPlayerList().isEmpty()) return;

                        for (ServerPlayerEntity player : minecraftServer.getPlayerManager().getPlayerList()) {
                            tickPlayer(player);
                            updateUfoMovement(player);
                            //handleUfoLaser(player);
                        }
                    } catch (Exception ignored) {}
                });
    }

    private static void updateUfoMovement(ServerPlayerEntity player) {
        ServerUfoController controller = ServerUfoController.getControllerByUUID(player.getUuid());
        //System.out.println("Controller: " + controller);
        if (controller != null) {
            //System.out.println("Test2");
            UfoEntity ufoEntity = controller.entity();
            if (ufoEntity != null) {
                //System.out.println("Test3");
                ufoEntity.moveToPlayer(player.getPos());
            }
        }
    }


    private static void handleUfoLaser(ServerPlayerEntity player) {
        ItemStack stack = player.getMainHandStack();
        if (!(stack.getItem() instanceof UfoControllerItem)) return;

        UUID ufoID = ((UfoControllerItem) stack.getItem()).getUfoID();
        ServerUfoController controller = ServerUfoController.getControllerByUUID(ufoID);
        if (controller != null) {
            HitResult hitResult = player.raycast(10, 0, false);
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockHitResult blockHitResult = (BlockHitResult) hitResult;
                BlockPos blockPos = blockHitResult.getBlockPos();
                controller.sendLaserParticles(player.getPos().add(0, 1.5, 0), new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
            } else if (hitResult.getType() == HitResult.Type.ENTITY) {
                Entity hitEntity = ((EntityHitResult) hitResult).getEntity();
                controller.sendLaserParticles(player.getPos().add(0, 1.5, 0), hitEntity.getPos());
            }
        }
    }

    public static void addCallback(ServerTickCallback callback) {
        callbacks.add(callback);
    }

    private static void tickPlayer(ServerPlayerEntity player) {
        pollPos(player);
        pollAbilities(player);
        pollCallbacks(player);
    }

    private static void pollCallbacks(ServerPlayerEntity player) {
        if (!isCursedTool(player.getMainHandStack())) return;
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 20, 1));
    }

    private static void pollPos(ServerPlayerEntity player) {
        if (cachedPos == null) cachedPos = player.getBlockPos();

        if (!player.getBlockPos().equals(cachedPos)) {
            cachedPos = player.getBlockPos();
            WorldUpdater.worldDeserted(cachedPos, player.getWorld(), player);
            syncCurrencyWithBar(player);
        }
    }

    private static void pollAbilities(ServerPlayerEntity player) {
        Abilities.getInstance().enable(DesertCurrency.getInstance().get());
    }

    public static void syncCurrencyWithBar(ServerPlayerEntity player) {
        DesertCurrency currency = DesertCurrency.getInstance();
        float progress = currency.get();
        DesertBar.update(player, progress);
        //System.out.println(currency.get());
    }

    private static boolean isCursedTool(ItemStack stack) {
        return stack.getItem().equals(ModItems.CURSED_PICKAXE)
                || stack.getItem().equals(ModItems.CURSED_AXE)
                || stack.getItem().equals(ModItems.CURSED_SHOVEL)
                || stack.getItem().equals(ModItems.CURSED_SWORD);
    }

    @FunctionalInterface
    public interface ServerTickCallback {
        void tick(ServerPlayerEntity player);
    }
}
