package com.ycx.Client.Command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.ycx.Handler.Command.CommandBuilder;
import com.ycx.Handler.Command.CommandSegment;

import java.util.Arrays;


public class Players {
    public static void command() {
        CommandBuilder ycx = new CommandBuilder("ycx");
        ycx.register(
                new CommandSegment[]{
                        new CommandSegment("playerName", StringArgumentType.string(), Arrays.asList("Steve", "Alex")),
                        new CommandSegment("someNumber", IntegerArgumentType.integer(), null),

                },
                (context, argumentNames) -> {
                    String playerName = StringArgumentType.getString(context, argumentNames.get(0));
                    int someNumber = IntegerArgumentType.getInteger(context, argumentNames.get(1));


                    System.out.print(playerName+someNumber);

                    return 1;
                }
        );

    }

}
