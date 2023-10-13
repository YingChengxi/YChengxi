package com.ycx.Client.Command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;

import java.util.Arrays;

import static com.ycx.Handler.CommandHelper.registerCommand;

public class Players {
    public static void command() {
        registerCommand(
                "example",
                Arrays.asList(StringArgumentType.string(), IntegerArgumentType.integer()),  //参数类型
                Arrays.asList("playerName", "someNumber"),  //字段名称
                Arrays.asList(
                        Arrays.asList("Steve", "Alex"),     // Suggestions for playerName
                        Arrays.asList("number","1")                            // No suggestions for someNumber
                ),
                (context, argumentNames) -> {
                    String playerName = StringArgumentType.getString(context, argumentNames.get(0));
                    int someNumber = IntegerArgumentType.getInteger(context, argumentNames.get(1));


                    System.out.print(playerName+someNumber);

                    return 1;
                }
        );


    }
}
