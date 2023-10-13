package com.ycx.Handler;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;

import java.util.List;

public class CommandHelper {
    public static void registerCommand(String commandName,
                                       List<ArgumentType<?>> argumentTypes,
                                       List<String> argumentNames,
                                       List<List<String>> suggestions,
                                       CommandExecutor commandExecutor) {

        LiteralArgumentBuilder<FabricClientCommandSource> command = ClientCommandManager.literal(commandName);
        buildArgumentRecursive(command, argumentTypes, argumentNames, suggestions, 0, commandExecutor);

        ClientCommandManager.DISPATCHER.register(command);
    }

    private static ArgumentBuilder<FabricClientCommandSource, ?> buildArgumentRecursive(
            ArgumentBuilder<FabricClientCommandSource, ?> argument,
            List<ArgumentType<?>> argumentTypes,
            List<String> argumentNames,
            List<List<String>> suggestions,
            int index,
            CommandExecutor commandExecutor) {

        if (index < argumentNames.size()) {
            RequiredArgumentBuilder<FabricClientCommandSource, ?> newArgument =
                    ClientCommandManager.argument(argumentNames.get(index), argumentTypes.get(index));

            if (index < suggestions.size() && suggestions.get(index) != null) {
                List<String> argumentSuggestions = suggestions.get(index);
                newArgument.suggests((context, builder) -> {
                    argumentSuggestions.forEach(builder::suggest);
                    return builder.buildFuture();
                });
            }

            argument.then(buildArgumentRecursive(newArgument, argumentTypes, argumentNames, suggestions, index + 1, commandExecutor));
        } else {
            argument.executes(context -> commandExecutor.execute(context, argumentNames));
        }
        return argument;
    }

    public interface CommandExecutor {
        int execute(CommandContext<FabricClientCommandSource> context, List<String> argumentNames);
    }
}
