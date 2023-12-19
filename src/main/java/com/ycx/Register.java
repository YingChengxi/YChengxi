package com.ycx;


import com.mojang.brigadier.CommandDispatcher;
import com.ycx.Client.Players;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.impl.command.client.ClientCommandInternals;

public class Register {
    public static CommandDispatcher<FabricClientCommandSource> dispatcher =new CommandDispatcher<>();
    public static void register(){

        Players.command();

    }
}
