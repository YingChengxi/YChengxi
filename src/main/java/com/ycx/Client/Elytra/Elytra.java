package com.ycx.Client.Elytra;

import com.ycx.MainClient;
import com.ycx.Handler.config.Configs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.util.LinkedList;
import java.util.Queue;

public class Elytra {
    private static final MinecraftClient mc = MainClient.MC;
    private static final int AVERAGE_SPEED_INTERVAL = 10;
    private static final Queue<Double> speedQueue = new LinkedList<>();
    private static double averageSpeed = 0;

    /**
     * 调整玩家的速度基于特定的条件和平均速度
     */
    public static void addSpeed() {
        ClientPlayerEntity player = mc.player;
        if (player == null) return;

        Vec3d rotation = player.getRotationVector().normalize();
        Vec3d currentVelocity = player.getVelocity();
        double currentSpeed = currentVelocity.length();
        double speedAdjustmentValue = Configs.ELYTRA_ADD.getDoubleValue();

        if (player.isSwimming()) {
            speedAdjustmentValue *= 2;
        }

        // 更新平均速度队列
        if (speedQueue.size() >= AVERAGE_SPEED_INTERVAL) {
            averageSpeed -= speedQueue.poll();
        }
        speedQueue.add(currentSpeed);
        averageSpeed += currentSpeed;

        double targetSpeed = averageSpeed / speedQueue.size();

        // 如果玩家受伤，调整方向并返回
        if (player.hurtTime > 0) {
            adjustPlayerVelocityBasedOnHurt(player, rotation, currentVelocity, speedAdjustmentValue, targetSpeed);
            return;
        }


        adjustPlayerVelocity(player, rotation, currentVelocity, speedAdjustmentValue);
    }

    /**
     * 减少玩家的速度
     */
    public static void reduceSpeed() {
        ClientPlayerEntity player = mc.player;
        if (player == null) return;

        Vec3d currentVelocity = player.getVelocity();
        double reductionFactor = 1 - Configs.ELYTRA_REDUCE.getDoubleValue();

        player.setVelocity(currentVelocity.multiply(reductionFactor, reductionFactor, reductionFactor));
    }

    /**
     * 根据受伤状态调整玩家的速度。
     */
    private static void adjustPlayerVelocityBasedOnHurt(ClientPlayerEntity player, Vec3d rotation, Vec3d currentVelocity, double speedAdjustmentValue, double targetSpeed) {
        Vec3d desiredDirection = new Vec3d(rotation.x, 0, rotation.z).normalize();
        Vec3d currentDirection = new Vec3d(currentVelocity.x, 0, currentVelocity.z).normalize();

        double angleDifference = Math.acos(currentDirection.dotProduct(desiredDirection));
        double dynamicAlpha = angleDifference / Math.PI;

        Vec3d smoothDirection = slerp(currentDirection, desiredDirection, dynamicAlpha).normalize();
        Vec3d finalVelocity = new Vec3d(
                smoothDirection.x * targetSpeed + rotation.x * speedAdjustmentValue,
                currentVelocity.y + rotation.y * speedAdjustmentValue * 2,
                smoothDirection.z * targetSpeed + rotation.z * speedAdjustmentValue);
        player.setVelocity(finalVelocity);

    }
    /**
     * 执行两个向量之间的球形线性插值（slerp）
     */
    public static Vec3d slerp(Vec3d start, Vec3d end, double alpha) {
        double dot = start.dotProduct(end);
        if (Math.abs(dot) > 0.9995) {
            return start.add(end.subtract(start).multiply(alpha)).normalize();
        }

        double theta_0 = Math.acos(dot);
        double theta = theta_0 * alpha;
        Vec3d relativeVec = end.subtract(start.multiply(dot)).normalize();
        return (start.multiply(Math.cos(theta))).add(relativeVec.multiply(Math.sin(theta)));
    }

    /**
     * 根据当前和目标速度调整玩家的速度。
     */
    private static void adjustPlayerVelocity(ClientPlayerEntity player, Vec3d rotation, Vec3d currentVelocity, double speedAdjustmentValue) {

        player.setVelocity(currentVelocity.add(
                (Math.abs(currentVelocity.x) >= speedAdjustmentValue * 30 ? 0 : rotation.x * speedAdjustmentValue),
                (Math.abs(currentVelocity.y) >= speedAdjustmentValue * 30 ? 0 : rotation.y * speedAdjustmentValue * 2),
                (Math.abs(currentVelocity.z) >= speedAdjustmentValue * 30 ? 0 : rotation.z * speedAdjustmentValue)
        ));
    }
}
