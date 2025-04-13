package com.idlenonsense.desertworld.bar;

import com.idlenonsense.desertworld.util.BarHelper;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DesertBar {
    private static final DecimalFormat DF = new DecimalFormat("#.#");
    private static final ServerBossBar BAR = new ServerBossBar(
            Text.literal("Прогресс опустынивания").formatted(Formatting.WHITE),
            BossBar.Color.GREEN,
            BossBar.Style.PROGRESS
    );
    private static final ConcurrentHashMap<Float, Text> MESSAGES = new ConcurrentHashMap<>();

    public static void update(ServerPlayerEntity player, float percent) {
        if (!BAR.getPlayers().contains(player)) addPlayer(player);

        if (BAR.getPercent() < 1f) {
            setProgress(percent);
            BAR.addPlayer(player);
        }
        pollMessage(player);
    }

    public static void addPlayer(ServerPlayerEntity player) {
        BAR.addPlayer(player);
    }

    public static void updateBar(float percent) {
        BAR.setPercent(BAR.getPercent() + percent);
        BAR.setName(Text.literal("Прогресс опустынивания: " + DF.format(BAR.getPercent() * 100) + "%").formatted(Formatting.GREEN));
    }

    public static void initBar() {
        BAR.setPercent(0f);
        BAR.setName(Text.literal("Прогресс опустынивания: " + DF.format(0) + "%").formatted(Formatting.GREEN));

        addMessage(2.5f, Text.literal("Разблокирована новая способность: песчаная кожа!").formatted(Formatting.GOLD));
        addMessage(5f,
                Text.literal("Разблокированы песчаные инструменты! Новая структура появилась где-то поблизости").formatted(Formatting.GOLD)
        );
        addMessage(10f, Text.literal("Разблокирован новый предмет: сердце пустыни!").formatted(Formatting.GOLD));
        addMessage(15f, Text.literal("Новая структура появилась где-то поблизости").formatted(Formatting.RED));
        addMessage(25f, Text.literal("Новая структура появилась где-то поблизости").formatted(Formatting.RED));
        addMessage(35f, Text.literal("Новая структура появилась где-то поблизости").formatted(Formatting.RED));
    }

    private static void addMessage(float percent, Text message) {
        MESSAGES.put(percent, message);
    }

    private static void pollMessage(ServerPlayerEntity player) {
        float percent = BAR.getPercent();
        Text message = getMessage(percent);
        if (message != null) {
            player.sendMessage(Text.literal("Прогресс опустынивания: " +
                                    DF.format(percent * 100) + "%")
                            .formatted(Formatting.GREEN),
                    false);
            player.sendMessage(message);
        }
    }

    private static Text getMessage(float percent) {

        for (Map.Entry<Float, Text> entry : MESSAGES.entrySet()) {
            Float key = entry.getKey();
            Text value = entry.getValue();
            if (Float.compare(key, percent) == 0) return value;
        }
        return null;
    }

    public static void setProgress(float percent) {
        BAR.setPercent(percent);
    }

    public static void setProgress(int percent) {
        BAR.setPercent(percent * 0.01f);
    }

    public static float getPercent() {
        return BAR.getPercent();
    }

    public static ServerBossBar getBar() {
        return BAR;
    }
}
