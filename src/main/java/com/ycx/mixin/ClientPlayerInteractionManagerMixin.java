package com.ycx.mixin;

import com.ycx.ClientHandler.AutoEat.AutoEat;
import com.ycx.config.Configs;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.ycx.ClientHandler.QuickStore.QuickStorage.*;


@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {


    @Inject(at = @At("HEAD"),method = "interactBlock")
    public void interactBlock(ClientPlayerEntity player, ClientWorld world, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir){
        if(isLoadMod && Configs.SYNTH.getBooleanValue() && !runningDrop && invAvailable()){
            runningStore = true;
            //System.out.println("Store start\n");

        }

    }
    @Inject(at = @At("HEAD"),method = "attackBlock")
    public void attackBlock(CallbackInfoReturnable<Boolean> cir){
        if(isLoadMod && Configs.SYNTH.getBooleanValue() && !runningStore && invAvailable()){
            runningDrop = true;
            //System.out.println("Drop start\n");
            assert mc.interactionManager != null;
            mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND, (BlockHitResult) mc.crosshairTarget);
        }
    }
    @Inject(at = {@At("HEAD")},
            method = "stopUsingItem", cancellable = true)
    private void onStopUsingItem(PlayerEntity player, CallbackInfo ci)
    {
        if (AutoEat.eat){
            AutoEat.eat=false;
            ci.cancel();
        }
    }

}
