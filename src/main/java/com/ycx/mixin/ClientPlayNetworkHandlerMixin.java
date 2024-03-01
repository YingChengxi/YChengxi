package com.ycx.mixin;

import com.ycx.Client.AutoDupe;

import com.ycx.Client.QuickStorage;
import com.ycx.Handler.config.Configs;
import com.ycx.MainClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {



    @Inject(at = @At("TAIL"),method = "onInventory")
    public void onInventory(InventoryS2CPacket packet, CallbackInfo ci){
        if (QuickStorage.runningDrop) QuickStorage.dropItem();
        if (QuickStorage.runningStore) QuickStorage.storeItem();
        if (AutoDupe.runningDupe) AutoDupe.dupeItem();
      //  if (QuickShulkerRestock.runningRestock) QuickShulkerRestock.itemRestockRunning();
    }

    @Inject(at = @At("HEAD"), method = "onEntityVelocityUpdate", cancellable = true)
    private void test(EntityVelocityUpdateS2CPacket packet, CallbackInfo ci) {

        ClientPlayerEntity player = MainClient.MC.player;
        if (player == null) return;

        if (Configs.ELYTRA.getBooleanValue() && player.isFallFlying()) {

            if (packet.getId() == player.getId()) ci.cancel();
        }

    }

}
