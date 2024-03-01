package com.ycx.mixin;

import com.ycx.Client.AutoDupe;
import com.ycx.Client.AutoEat;
import com.ycx.Client.QuickStorage;
import com.ycx.Handler.config.Configs;
import com.ycx.MainClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;




@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {

    @Inject(at = @At("HEAD"),method = "interactBlock")
    public void interactBlock(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir){
        if (QuickStorage.isLoadMod && Configs.AUTOSTORE.getBooleanValue() && !QuickStorage.runningDrop && QuickStorage.invAvailable()){
            QuickStorage.runningStore = true;
            //System.out.println("Store start\n");
        }
        if (AutoDupe.isLoadMod && Configs.AUTODUPE.getBooleanValue() && AutoDupe.invAvailable()){
            AutoDupe.runningDupe = true;
        }
    }
    @Inject(at = @At("HEAD"),method = "attackBlock")
    public void attackBlock(CallbackInfoReturnable<Boolean> cir){
        if(QuickStorage.isLoadMod && Configs.AUTOSTORE.getBooleanValue() && !QuickStorage.runningStore && QuickStorage.invAvailable()){
            QuickStorage.runningDrop = true;
            //System.out.println("Drop start\n");
            assert MainClient.MC.interactionManager != null;
            MainClient.MC.interactionManager.interactBlock(MainClient.MC.player, Hand.MAIN_HAND, (BlockHitResult) MainClient.MC.crosshairTarget);
        }
    }
    @Inject(at = {@At("HEAD")},
            method = "stopUsingItem", cancellable = true)
    private void onStopUsingItem(PlayerEntity player, CallbackInfo ci) {
        if (AutoEat.eat){
            AutoEat.eat=false;
            ci.cancel();
        }
    }



}
