package com.idlenonsense.desertworld.events;

import com.idlenonsense.desertworld.abilities.Abilities;
import com.idlenonsense.desertworld.bar.DesertBar;
import com.idlenonsense.desertworld.currency.DesertCurrency;
import com.idlenonsense.desertworld.util.WorldUpdater;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

public class ServerTickEvent {
    private static BlockPos cachedPos = null;

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

    private static void tickPlayer(ServerPlayerEntity player) {
        pollPos(player);
        pollAbilities(player);
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
        DesertBar.update(player, currency.get());

        System.out.println(currency.get());
    }
}
