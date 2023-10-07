package com.ycx.ClientHandler.Command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;


import java.util.Arrays;

public class CommandRegister {
    public static void register(){
        CommandBuilder ycx = new CommandBuilder("ycx");
        ycx.register(
                new CommandSegment[]{
                        new CommandSegment("paramA", StringArgumentType.string(), null),
                        new CommandSegment("paramB", StringArgumentType.string(), null),

                },
                context -> {
                    String stringA = StringArgumentType.getString(context, "paramA");

                    // 这里添加你的命令逻辑
                    System.out.print(stringA);
                    return 1;
                }
        );

    }

}
