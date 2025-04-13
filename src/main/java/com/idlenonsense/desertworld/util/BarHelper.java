package com.idlenonsense.desertworld.util;

import com.idlenonsense.desertworld.abilities.Abilities;
import com.idlenonsense.desertworld.abilities.IAbilitiesProvider;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.HashMap;

public class BarHelper {
    private static final DecimalFormat DF = new DecimalFormat("#.#");
    private static final HashMap<Integer, Text> messages = new HashMap<>();

    static {
        messages.put(2, Text.literal("Разблокирована новая способность: песчаная кожа!").formatted(Formatting.GOLD));
        messages.put(5, Text.literal("Разблокированы песчаные инструменты! Новая структура появилась где-то поблизости").formatted(Formatting.GOLD));
        messages.put(10, Text.literal("Разблокирован новый предмет: сердце пустыни!").formatted(Formatting.GOLD));
        messages.put(15, Text.literal("Новая структура появилась где-то поблизости").formatted(Formatting.RED));
        messages.put(25, Text.literal("Новая структура появилась где-то поблизости").formatted(Formatting.RED));
        messages.put(35, Text.literal("Новая структура появилась где-то поблизости").formatted(Formatting.RED));
    }

    @Nullable
    public static Text format(float percent) {
        int p = (int) (percent*100);

        if (p > 100) p = 100;
        if (p < 0) p = 0;

        if (messages.containsKey(p))
            return Text.literal("Прогресс опустынивания: " + DF.format(p) + "%")
                .formatted(Formatting.GREEN);
        else return null;
    }

    @Nullable
    public static Text getSpecialMessageText(float percent) {
        return messages.getOrDefault(((int) percent*100), null);
    }

    public static void update(float percent) {
        Abilities.getInstance().enable(percent);
    }

    private static boolean floatEquals(float a, float b) {
        return Float.compare(a, b) == 0;
    }
}
