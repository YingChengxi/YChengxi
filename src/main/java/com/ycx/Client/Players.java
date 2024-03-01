package com.ycx.Client;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.ycx.Handler.Command.CommandBuilder;
import com.ycx.Handler.Command.CommandSegment;
import com.ycx.MainClient;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Players {
    private static final MinecraftClient mc = MainClient.MC;
    public static boolean isLoadMod = FabricLoader.getInstance().isModLoaded("carpet");
    public static void command() {
        CommandBuilder players = new CommandBuilder("players");

        players.register(
                new CommandSegment[]{
                        new CommandSegment("playerName", StringArgumentType.string(), Arrays.asList("Steve", "Alex")),
                        new CommandSegment("from", IntegerArgumentType.integer(), List.of("1")),
                        new CommandSegment("to", IntegerArgumentType.integer(),  List.of("10")),
                        new CommandSegment("command", StringArgumentType.string(), Arrays.asList("spawn", "drop", "kill"))
                },
                (context, argumentNames) -> {

                    if (mc.player == null) return 0;

                    if (!isLoadMod){
                        mc.player.sendMessage(Text.of("未加载carpe模组"), false);
                        return 1;
                    }

                    String name = StringArgumentType.getString(context, "playerName");
                    int start = IntegerArgumentType.getInteger(context, "from");
                    int end = IntegerArgumentType.getInteger(context, "to");
                    String operate = StringArgumentType.getString(context, "command");

                    String operate1 = null;

                    if (start <= end) {
                        switch (operate) {
                            case "spawn"    ->  operate1 = "spawn";
                            case "drop"     ->  operate1 = "dropStack all";
                            case "kill"     ->  operate1 = "kill";
                        }
                        ExecutorService executor = Executors.newFixedThreadPool(1);
                        for (int i = start; i <= end; i++) {
                            String playerName = name + i;
                            String finalOperate = operate1;
                            executor.submit(() -> {
                                try {
                                    Thread.sleep(20); // 延时
                                    String commandString = "player " + playerName + " " + finalOperate;
//                                    mc.player.sendChatMessage(commandString);
                                    mc.player.networkHandler.sendChatCommand(commandString);
//                                    mc.player.sendMessage(Text.of(commandString));

                                } catch (Exception e) {
                                    mc.player.sendMessage(Text.of("未知错误"), false);
                                }
                            });

                        }
                        executor.shutdown();
                    } else {
                        mc.player.sendMessage(Text.of("错误的参数范围！"), false);
                    }
                    return 1;
                }
        );
    }

}
