package com.ycx.Handler.Command;

import com.mojang.brigadier.arguments.ArgumentType;

import java.util.List;

public class CommandSegment {
    String name;
    ArgumentType<?> argumentType;
    List<String> suggestions;

    public CommandSegment(String name, ArgumentType<?> argumentType, List<String> suggestions) {
        this.name = name;
        this.argumentType = argumentType;
        this.suggestions = suggestions;
    }
}
