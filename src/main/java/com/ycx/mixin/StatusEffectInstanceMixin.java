package com.ycx.mixin;

import com.ycx.Handler.config.Configs;
import net.minecraft.entity.effect.StatusEffectInstance;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StatusEffectInstance.class)
public class StatusEffectInstanceMixin {
	@Shadow
	private int duration;
	
	@Inject(at = {@At("HEAD")}, method = {"updateDuration()I"}, cancellable = true)
	private void onUpdateDuration(CallbackInfoReturnable<Integer> cir)
	{
		if(Configs.POTIONFROZEN.getBooleanValue())
			cir.setReturnValue(duration);
	}
}
