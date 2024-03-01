package com.ycx.mixin;

import com.ycx.Handler.config.Configs;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(value= EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Shadow @Final public GameOptions options;

    @Redirect(method = {"handleInputEvents"}, at = @At(value = "INVOKE",target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z",ordinal = 0))
    private boolean shouldAttack(ClientPlayerEntity player)
    {
        if(Configs.SHOULDATTACK.getBooleanValue()){
            if(!this.options.useKey.isPressed()){
                return player.isUsingItem();
            }
            return false;
        }
        return player.isUsingItem();
    }
}
