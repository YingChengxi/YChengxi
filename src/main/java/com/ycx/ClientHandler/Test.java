package com.ycx.ClientHandler;

import com.ycx.MainClient;
import com.ycx.config.Configs;
import fi.dy.masa.malilib.hotkeys.IHotkeyCallback;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;


public class Test implements IHotkeyCallback {
    public static MinecraftClient mc = MainClient.MC;
    @Override
    public boolean onKeyAction(KeyAction action, IKeybind key) {

        ClientPlayerEntity player = mc.player;


       return true;
    }
}
