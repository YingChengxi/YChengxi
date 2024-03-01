package com.ycx.Handler.Command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import java.util.ArrayList;
import java.util.List;

public class CommandBuilder {
    private final String commandName;

    public CommandBuilder(String commandName) {
        this.commandName = commandName;
    }

    public void register(CommandSegment[] segments, CommandExecutor commandExecutor) {

        LiteralArgumentBuilder<FabricClientCommandSource> command = ClientCommandManager.literal(commandName);
        buildArgumentRecursive(command, segments, 0, commandExecutor);

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(command));

    }

    private static ArgumentBuilder<FabricClientCommandSource, ?> buildArgumentRecursive(
            ArgumentBuilder<FabricClientCommandSource, ?> argument,
            CommandSegment[] segments,
            int index,
            CommandExecutor commandExecutor) {

        if (index < segments.length) {
            CommandSegment segment = segments[index];

            RequiredArgumentBuilder<FabricClientCommandSource, ?> newArgument =
                    ClientCommandManager.argument(segment.name, segment.argumentType);

            if (segment.suggestions != null) {
                newArgument.suggests((context, builder) -> {
                    segment.suggestions.forEach(builder::suggest);
                    return builder.buildFuture();
                });
            }

            argument.then(buildArgumentRecursive(newArgument, segments, index + 1, commandExecutor));
        } else {
            argument.executes(context -> commandExecutor.execute(context, getArgumentNames(segments)));
        }
        return argument;
    }

    private static List<String> getArgumentNames(CommandSegment[] segments) {
        List<String> names = new ArrayList<>();
        for (CommandSegment segment : segments) {
            names.add(segment.name);
        }
        return names;
    }

    public interface CommandExecutor {
        int execute(CommandContext<FabricClientCommandSource> context, List<String> argumentNames);
    }

}
