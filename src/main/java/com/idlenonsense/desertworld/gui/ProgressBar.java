package com.idlenonsense.desertworld.gui;

import com.idlenonsense.desertworld.DesertWorld;
import com.idlenonsense.desertworld.util.BarHelper;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.text.DecimalFormat;

public class ProgressBar {
    static DecimalFormat df = new DecimalFormat("#.#");
    public static ServerBossBar progressBar = new ServerBossBar(
            Text.literal("Прогресс опустынивания").formatted(Formatting.WHITE),
            BossBar.Color.GREEN,
            BossBar.Style.PROGRESS
    );

    public static void updateProgressBar(ServerBossBar bossBar, Float percent, ServerPlayerEntity player) {
        if (progressBar.getPercent() < 1f) {
            bossBar.setPercent(bossBar.getPercent() + percent);
            bossBar.setName(Text.literal("Прогресс опустынивания: " + df.format(bossBar.getPercent() * 100) + "%").formatted(Formatting.GREEN));
            bossBar.addPlayer(player);
        }
        float p = bossBar.getPercent();
        System.out.println(p);
        Text msg = BarHelper.format(bossBar.getPercent());
        if (msg != null) player.sendMessage(BarHelper.format(bossBar.getPercent()), false);
        Text message = BarHelper.getSpecialMessageText(bossBar.getPercent());
        if (message != null) player.sendMessage(BarHelper.getSpecialMessageText(bossBar.getPercent()), false);
        BarHelper.update(bossBar.getPercent());
    }

    public static void setProgress(ServerBossBar bossBar, int percent, ServerPlayerEntity player) {
        bossBar.setPercent(percent * 0.01f);
    }

    public static void registerBossBar() {
        DesertWorld.LOGGER.info("Регистрация босс-бара " + DesertWorld.MOD_ID);
        progressBar.setPercent(0f);
    }
}
