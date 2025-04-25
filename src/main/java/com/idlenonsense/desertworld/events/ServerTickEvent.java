package com.idlenonsense.desertworld.events;

import com.idlenonsense.desertworld.abilities.Abilities;
import com.idlenonsense.desertworld.bar.DesertBar;
import com.idlenonsense.desertworld.currency.DesertCurrency;
import com.idlenonsense.desertworld.item.ModItems;
import com.idlenonsense.desertworld.util.WorldUpdater;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.LinkedList;

public class ServerTickEvent {
    private static BlockPos cachedPos = null;
    private static final LinkedList<ServerTickCallback> callbacks = new LinkedList<>();

    public static void register() {
        ServerTickEvents
                .START_SERVER_TICK
                .register(minecraftServer -> {
                    try {
                        if (minecraftServer
                                .getPlayerManager()
                                .getPlayerList()
                                .get(0) == null) return;

                        tickPlayer(
                                minecraftServer
                                        .getPlayerManager()
                                        .getPlayerList()
                                        .get(0)
                        );
                    } catch (Exception ignored) {}
                });
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
        System.out.println(currency.get());
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
