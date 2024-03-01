package com.ycx.Client;

import com.ycx.MainClient;
import com.ycx.Handler.config.Configs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;


public class Elytra {
    private static final MinecraftClient mc = MainClient.MC;

    public static void addSpeed() {
        ClientPlayerEntity player = mc.player;
        if (player == null) return;

        Vec3d rotation = player.getRotationVector().normalize();
        Vec3d currentVelocity = player.getVelocity();
        double speedAdjustmentValue = Configs.ELYTRA_ADD.getDoubleValue();

        if (player.isSwimming()) {
            speedAdjustmentValue *= 2;
        }

        player.setVelocity(currentVelocity.add(
                (Math.abs(currentVelocity.x) >= speedAdjustmentValue * 30 ? 0 : rotation.x * speedAdjustmentValue),
                (Math.abs(currentVelocity.y) >= speedAdjustmentValue * 30 ? 0 : rotation.y * speedAdjustmentValue * 2),
                (Math.abs(currentVelocity.z) >= speedAdjustmentValue * 30 ? 0 : rotation.z * speedAdjustmentValue)
        ));

    }

    public static void reduceSpeed() {
        ClientPlayerEntity player = mc.player;
        if (player == null) return;

        Vec3d currentVelocity = player.getVelocity();
        double reductionFactor = 1 - Configs.ELYTRA_REDUCE.getDoubleValue();

        player.setVelocity(currentVelocity.multiply(reductionFactor, reductionFactor, reductionFactor));
    }

}
