package com.idlenonsense.desertworld.gui;

import com.idlenonsense.desertworld.bar.DesertBar;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class SetProgress {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("setprogress")
                .then(CommandManager.argument("percent", FloatArgumentType.floatArg(0.0f, 100.0f)) // Аргумент команды (от 0 до 100)
                        .executes(SetProgress::execute)));
    }

    private static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        float percent = FloatArgumentType.getFloat(context, "percent");
        DesertBar.setProgress(Math.min(percent, 100f) * 0.01f);
        DesertBar.update(source.getPlayer(), DesertBar.getPercent());
        source.sendFeedback(Text.literal("Прогресс опустынивания изменен на " + percent + "%"), false);

        return 1;
    }
}
