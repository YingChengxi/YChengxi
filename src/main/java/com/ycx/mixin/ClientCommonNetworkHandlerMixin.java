package com.ycx.mixin;

import com.ycx.Handler.config.Configs;
import com.ycx.MainClient;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientCommonNetworkHandler.class)
public class ClientCommonNetworkHandlerMixin {
    @Final
    @Shadow
    protected ClientConnection connection;
    @Inject(at = @At("HEAD"),method = "sendPacket", cancellable = true)
    public void sendPacket(Packet<?> packet, CallbackInfo ci) {

        ClientPlayerEntity player = MainClient.MC.player;
        if (player == null) return;

        if (Configs.ELYTRA.getBooleanValue()
                && (packet instanceof PlayerMoveC2SPacket.Full || packet instanceof PlayerMoveC2SPacket.PositionAndOnGround)
                && player.isFallFlying()) {

            this.connection.send(new PlayerMoveC2SPacket.Full(
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    player.getYaw(),
                    (Math.min(player.getPitch(), (float) -Configs.ELYTRA_PITCH.getDoubleValue())),
                    player.isOnGround()
            ));
            ci.cancel();
        }

    }
}
