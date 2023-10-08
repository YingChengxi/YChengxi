package com.ycx.mixin;

import com.ycx.MainClient;
import com.ycx.Handler.config.Configs;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.ycx.Client.QuickStore.QuickStorage.*;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Final
    @Shadow
    private ClientConnection connection;

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

    @Inject(at = @At("TAIL"),method = "onInventory")
    public void onInventory(InventoryS2CPacket packet, CallbackInfo ci){
        if (runningDrop) dropItem();
        if (runningStore) storeItem();
    }

}
