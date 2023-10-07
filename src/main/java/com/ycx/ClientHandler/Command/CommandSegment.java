package com.ycx.ClientHandler.Command;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;

import java.util.List;

public class CommandSegment {
    String name;
    ArgumentType<?> argumentType;
    SuggestionProvider<FabricClientCommandSource> suggestionProvider;

    public CommandSegment(String name, ArgumentType<?> argumentType, List<String> suggestions) {
        this.name = name;
        this.argumentType = argumentType;
        this.suggestionProvider = (suggestions != null) ? (context, builder) -> {
            for (String suggestion : suggestions) {
                builder.suggest(suggestion);
            }
            return builder.buildFuture();
        } : null;
    }
}
