package com.ycx.mixin;

import com.ycx.Client.AutoDupe;
import com.ycx.Client.QuickStorage;
import net.minecraft.client.network.ClientPlayNetworkHandler;
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
    }

}
