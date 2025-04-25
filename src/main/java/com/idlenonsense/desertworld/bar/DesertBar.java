package com.idlenonsense.desertworld.bar;

import com.idlenonsense.desertworld.currency.DesertCurrency;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import java.text.DecimalFormat;
import java.util.concurrent.ConcurrentHashMap;

public class DesertBar {
    public static final float RATE_OF_PROGRESS = 0.00001f;
    private static final DecimalFormat DF = new DecimalFormat("#.#");
    private static final ServerBossBar BAR = new ServerBossBar(
            Text.literal("Прогресс опустынивания").formatted(Formatting.WHITE),
            BossBar.Color.GREEN,
            BossBar.Style.PROGRESS
    );
    private static final ConcurrentHashMap<Float, Text> MESSAGES = new ConcurrentHashMap<>();
    private static float lastNotifiedProgress = 0f;

    static {
        addMessage(0.025f, Text.literal("Разблокирована новая способность: песчаная кожа!").formatted(Formatting.GOLD));
        addMessage(0.05f, Text.literal("Разблокированы песчаные инструменты! Новая структура появилась где-то поблизости").formatted(Formatting.GOLD));
        addMessage(0.1f, Text.literal("Разблокирован новый предмет: сердце пустыни!").formatted(Formatting.GOLD));
        addMessage(0.15f, Text.literal("Новая структура появилась где-то поблизости").formatted(Formatting.RED));
        addMessage(0.25f, Text.literal("Новая структура появилась где-то поблизости").formatted(Formatting.RED)); // тут поменять на нужные сообщения
        addMessage(0.35f, Text.literal("Новая структура появилась где-то поблизости").formatted(Formatting.RED));
    }
    private static void addMessage(float percent, Text message) {
        MESSAGES.put(percent, message);
    }
    private static void pollMessage(ServerPlayerEntity player, float currentProgress) {
        for (float threshold : MESSAGES.keySet()) {
            if (currentProgress >= threshold && lastNotifiedProgress < threshold) {
                Text message = MESSAGES.get(threshold);
                if (message != null) {
                    player.sendMessage(Text.literal("Прогресс опустынивания: " + DF.format(currentProgress * 100) + "%").formatted(Formatting.GREEN), false);
                    player.sendMessage(message);
                    lastNotifiedProgress = threshold;
                }
            }
        }
    }

    public static void update(ServerPlayerEntity player, float percent) {
        if (!BAR.getPlayers().contains(player)) addPlayer(player);

        float currentProgress = Math.min(percent, 1f);
        BAR.setPercent(currentProgress);

        BAR.addPlayer(player);
        pollMessage(player, currentProgress);
    }

    public static void addPlayer(ServerPlayerEntity player) {
        BAR.addPlayer(player);
    }

    public static void setProgress(float percent) {
        BAR.setPercent(Math.min(percent, 1f));
    }

    public static float getPercent() {
        return BAR.getPercent();
    }

    public static ServerBossBar getBar() {
        return BAR;
    }
}
