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
    private static float currentProgress = 0f;
    public static final float RATE_OF_PROGRESS = 0.00001f;
    private static final DecimalFormat DF = new DecimalFormat("#.#");
    private static final ServerBossBar BAR = new ServerBossBar(
            Text.literal("Прогресс опустынивания").formatted(Formatting.WHITE),
            BossBar.Color.GREEN,
            BossBar.Style.PROGRESS
    );
    private static final ConcurrentHashMap<Float, Text> MESSAGES = new ConcurrentHashMap<>();

    private static void addMessage(float percent, Text message) {
        MESSAGES.put(percent, message);
    }
    static {
        addMessage(2.5f, Text.literal("Разблокирована новая способность: песчаная кожа!").formatted(Formatting.GOLD));
        addMessage(5f, Text.literal("Разблокированы песчаные инструменты! Новая структура появилась где-то поблизости").formatted(Formatting.GOLD));
        addMessage(10f, Text.literal("Разблокирован новый предмет: сердце пустыни!").formatted(Formatting.GOLD));
        addMessage(15f, Text.literal("Новая структура появилась где-то поблизости").formatted(Formatting.RED));
        addMessage(25f, Text.literal("Новая структура появилась где-то поблизости").formatted(Formatting.RED));
        addMessage(35f, Text.literal("Новая структура появилась где-то поблизости").formatted(Formatting.RED));
    }

    public static void update(ServerPlayerEntity player, float percent) {
        if (!BAR.getPlayers().contains(player)) addPlayer(player);


        currentProgress = Math.min(currentProgress + percent, 1f);
        // Устанавливаем прогресс, но не более 100% (1f)
        // Если прогресс больше 100%, то ограничиваем его
        BAR.setPercent(Math.min(percent, 1f));
        BAR.addPlayer(player);
        pollMessage(player);
    }

    public static void addPlayer(ServerPlayerEntity player) {
        BAR.addPlayer(player);
    }

//    public static void updateBar(float percent) {
//        BAR.setPercent(BAR.getPercent() + percent);
//        BAR.setName(Text.literal("Прогресс опустынивания: " + DF.format(BAR.getPercent() * 100) + "%").formatted(Formatting.GREEN));
//    }

    public static void updateBar(float percent) {
        currentProgress = Math.min(currentProgress + percent, 1f);  // Обновляем переменную прогресса
        BAR.setPercent(currentProgress);
        BAR.setName(Text.literal("Прогресс опустынивания: " + DF.format(currentProgress * 100) + "%").formatted(Formatting.GREEN));
    }



    public static void initBar() {
        currentProgress = 0f;
        BAR.setPercent(0f);
        BAR.setName(Text.literal("Прогресс опустынивания: 0%").formatted(Formatting.GREEN));
    }

//    public static void initBar() {
//        BAR.setPercent(0f);
//        BAR.setName(Text.literal("Прогресс опустынивания: " + DF.format(0) + "%").formatted(Formatting.GREEN));
//
//        addMessage(2.5f, Text.literal("Разблокирована новая способность: песчаная кожа!").formatted(Formatting.GOLD));
//        addMessage(5f,
//                Text.literal("Разблокированы песчаные инструменты! Новая структура появилась где-то поблизости").formatted(Formatting.GOLD)
//        );
//        addMessage(10f, Text.literal("Разблокирован новый предмет: сердце пустыни!").formatted(Formatting.GOLD));
//        addMessage(15f, Text.literal("Новая структура появилась где-то поблизости").formatted(Formatting.RED));
//        addMessage(25f, Text.literal("Новая структура появилась где-то поблизости").formatted(Formatting.RED));
//        addMessage(35f, Text.literal("Новая структура появилась где-то поблизости").formatted(Formatting.RED));
//    }

    private static void pollMessage(ServerPlayerEntity player) {
        float percent = BAR.getPercent();
        Text message = getMessage(percent);
        if (message != null) {
            player.sendMessage(Text.literal("Прогресс опустынивания: " +
                    DF.format(percent * 100) + "%").formatted(Formatting.GREEN),false);
            player.sendMessage(message);
        }
    }

//    private static Text getMessage(float percent) {
//
//        for (Map.Entry<Float, Text> entry : MESSAGES.entrySet()) {
//            Float key = entry.getKey();
//            Text value = entry.getValue();
//            if (Float.compare(key, percent) == 0) return value;
//        }
//        return null;
//    }

    private static Text getMessage(float percent) { return MESSAGES.getOrDefault(percent, null); }

    public static void setProgress(float percent) {
        currentProgress = Math.min(percent, 1f);  // Устанавливаем прогресс в переменной
        BAR.setPercent(currentProgress);
    }

    public static void setProgress(int percent) {
        currentProgress = Math.min(percent * 0.01f, 1f);  // Устанавливаем прогресс в переменной
        BAR.setPercent(currentProgress);
    }

    public static float getPercent() {
        return currentProgress;
    }

    public static ServerBossBar getBar() {
        return BAR;
    }
}
