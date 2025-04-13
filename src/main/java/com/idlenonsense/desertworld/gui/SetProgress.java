package com.idlenonsense.desertworld.gui;

import com.idlenonsense.desertworld.bar.DesertBar;
import com.mojang.brigadier.CommandDispatcher;
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
                .then(CommandManager.argument("percent", IntegerArgumentType.integer())
                        .executes(SetProgress::execute)));
    }

    private static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        int percent = IntegerArgumentType.getInteger(context, "percent");
        ServerPlayerEntity player = source.getPlayer();
        if (percent < 100) {
            DesertBar.setProgress(percent);
        } else {
            source.sendFeedback(Text.literal("Неверный аргумент").formatted(Formatting.RED), false);
        }
        return 1;
    }
}
