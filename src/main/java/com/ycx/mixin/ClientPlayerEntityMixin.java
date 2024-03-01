package com.ycx.mixin;

import com.ycx.Client.AutoEat;
import com.ycx.Client.Elytra;
import com.ycx.Client.AutoBridge;
import com.ycx.MainClient;
import com.ycx.Handler.config.Configs;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    @Shadow
    public Input input;
    @Unique
    private static final MinecraftClient mc = MainClient.MC;

    @Inject(at = @At("HEAD"),method = "tick")
    public void tickMixin(CallbackInfo ci) {

        ClientPlayerEntity player = mc.player;
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
//        if (mc.options.sprintKey.isPressed()){
//
//            if (timer == 0){
//                Vec3d normalizedVector = player.getRotationVector().multiply(1.0, 0.0, 1.0).normalize();
//                Vec3d velocity = normalizedVector.multiply(20);
//                player.addVelocity(velocity);
//                timer = 20;
//            }else{
//                timer--;
//            }
//
//        }
    }



    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;hasVehicle()Z",ordinal = 1), method = "tickMovement()V")
    private boolean usingItemSlowDown(ClientPlayerEntity player)
    {
        if(Configs.SLOWDOWN.getBooleanValue())
            return true;
        return player.hasVehicle();
    }
    @Inject(at = @At("HEAD"), method = "getMountJumpStrength", cancellable = true)
    public void startRidingJump(CallbackInfoReturnable<Float> cir) {
        if (Configs.MOUNTJUMPSTRENGTH.getBooleanValue()){
            cir.setReturnValue((float) Configs.JUMPSTRENGTH_FACTOR.getDoubleValue());
        }

    }



}
