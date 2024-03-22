package com.ycx.mixin;

import com.ycx.Handler.config.Configs;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.sound.SoundCategory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(value= EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Shadow @Final public GameOptions options;
    @Shadow private boolean windowFocused;

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
    @Inject(at = @At("HEAD"), method = "isWindowFocused")
    public void mute(CallbackInfoReturnable<Boolean> cir) {
        if(Configs.MUTEATNOFOCUS.getBooleanValue()){
            float soundVolume = options.getSoundVolume(SoundCategory.MASTER);

            if(!windowFocused && !(soundVolume == 0.0f) && (Configs.SOUNDVALUE.getDoubleValue() == -1)){
                Configs.SOUNDVALUE.setDoubleValue(soundVolume);
                //#if MC >= 12002
                options.getSoundVolumeOption(SoundCategory.MASTER).setValue(0.0);
                //#else
                //$$    options.setSoundVolume(SoundCategory.MASTER, 0.0f);
                //#endif
            } else if(windowFocused && (soundVolume == 0.0f) && Configs.SOUNDVALUE.getDoubleValue() > 0) {
                //#if MC >= 12002
                options.getSoundVolumeOption(SoundCategory.MASTER).setValue(Configs.SOUNDVALUE.getDoubleValue());
                //#else
                //$$    options.setSoundVolume(SoundCategory.MASTER, (float) Configs.SOUNDVALUE.getDoubleValue());
                //#endif
                Configs.SOUNDVALUE.setDoubleValue(-1);
            }
        }

    }

}
