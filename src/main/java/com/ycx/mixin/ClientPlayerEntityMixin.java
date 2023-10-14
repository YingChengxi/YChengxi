package com.ycx.mixin;


import com.ycx.Client.AutoEat;
import com.ycx.Client.Elytra;
import com.ycx.Client.AutoBridge;
import com.ycx.MainClient;
import com.ycx.Handler.config.Configs;

import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    @Shadow
    public Input input;

    @Inject(at = @At("HEAD"),method = "tick")
    public void tickMixin(CallbackInfo ci) {

        ClientPlayerEntity player = MainClient.MC.player;
        if (player == null) return;


        if (Configs.ELYTRA.getBooleanValue() && (player.isFallFlying() || player.isSwimming())) {
            if (input.jumping) Elytra.addSpeed();
            if (input.sneaking) Elytra.reduceSpeed();
        }

        if (Configs.AUTOEAT.getBooleanValue()){
            AutoEat.autoEat();
        }

        if (Configs.AUTOBRIDGE.getBooleanValue() && !input.sneaking) {
            AutoBridge.bridge();
        }
    }



    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;hasVehicle()Z",ordinal = 0), method = "tickMovement()V")
    private boolean usingItemSlowDown(ClientPlayerEntity player)
    {
        if(Configs.SLOWDOWN.getBooleanValue())
            return true;

        return player.hasVehicle();
    }
}
