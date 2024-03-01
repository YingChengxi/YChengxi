package com.ycx.mixin;

import com.ycx.Handler.config.Configs;
import com.ycx.MainClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(at = {@At("HEAD")}, method = "tiltViewWhenHurt", cancellable = true)
    private void onBobViewWhenHurt(MatrixStack matrixStack, float f, CallbackInfo ci) {

        ClientPlayerEntity player = MainClient.MC.player;
        if (player == null) return;

        if(Configs.ELYTRA.getBooleanValue() && Configs.ELYTRA_HUTRTVIEW.getBooleanValue() && player.isFallFlying())
            ci.cancel();
    }


}
