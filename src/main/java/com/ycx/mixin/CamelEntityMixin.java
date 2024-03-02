package com.ycx.mixin;

import com.ycx.Handler.config.Configs;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.passive.CamelEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CamelEntity.class)
public class CamelEntityMixin {
    @Shadow private int dashCooldown;

    @Inject(at = @At("TAIL"), method = "jump")
    protected void jumpDashCooldown(float strength, Vec3d movementInput, CallbackInfo ci) {
        setDashCooldown ();
    }


    @Inject(at = @At("TAIL"), method = "onTrackedDataSet")
    public void onTrackedDataSetPost(TrackedData<?> data, CallbackInfo ci) {
        setDashCooldown ();
    }

    @Unique
    private void setDashCooldown (){
        if (Configs.CAMELDASHCOOLDOWN.getBooleanValue()){
            dashCooldown = 0;
        }
    }

}
