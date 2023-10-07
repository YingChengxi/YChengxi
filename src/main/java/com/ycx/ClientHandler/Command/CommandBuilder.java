package com.ycx.ClientHandler.Command;


import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;

import java.util.function.Function;

public class CommandBuilder {
    private final String commandName;

    public CommandBuilder(String commandName) {
        this.commandName = commandName;
    }

    public void register(CommandSegment[] segments, Function<CommandContext<FabricClientCommandSource>, Integer> execution) {
        LiteralArgumentBuilder<FabricClientCommandSource> command = ClientCommandManager.literal(commandName);
        ArgumentBuilder<FabricClientCommandSource, ?> current = command;

        for (CommandSegment segment : segments) {
            RequiredArgumentBuilder<FabricClientCommandSource, ?> argument = ClientCommandManager.argument(segment.name, segment.argumentType);
            if (segment.suggestionProvider != null) {
                argument.suggests(segment.suggestionProvider);
            }
            //current = current.then(argument);  // Make sure that you update 'current' here
            current.then(argument);
            current = argument;
        }


        // 将函数包装为命令对象
        current.executes(execution::apply);
        ClientCommandManager.DISPATCHER.register(command);
        // 使用这个方法来输出current
        System.out.println(argumentBuilderToString(current.build()));



    }

    private String argumentBuilderToString(CommandNode<FabricClientCommandSource> node) {
        StringBuilder result = new StringBuilder();

        if (node instanceof LiteralCommandNode) {
            result.append(((LiteralCommandNode<FabricClientCommandSource>) node).getLiteral());
        } else if (node instanceof ArgumentCommandNode) {
            result.append("<").append(((ArgumentCommandNode<FabricClientCommandSource, ?>) node).getName()).append(">");
        }

        // 遍历子命令
        for (CommandNode<FabricClientCommandSource> child : node.getChildren()) {
            result.append(" ").append(argumentBuilderToString(child));
        }

        return result.toString();
    }



}
