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
// 获取玩家脚下的方块位置
        BlockPos blockPos = player.getBlockPos().down();

        // 创建一个 PlayerInteractBlockC2SPacket 用于模拟右击
        Direction direction = Direction.DOWN;
        Vec3d vec = Vec3d.ofBottomCenter(blockPos);
        PlayerInteractBlockC2SPacket packet = new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, new BlockHitResult(vec, direction, blockPos, false));

        // 发送包
        mc.getNetworkHandler().sendPacket(packet);

        // 延迟一段时间后再次发送包来收回水
        mc.execute(() -> mc.getNetworkHandler().sendPacket(packet));

       return true;
    }
}
