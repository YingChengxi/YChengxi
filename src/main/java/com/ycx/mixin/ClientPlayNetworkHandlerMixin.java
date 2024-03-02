package com.ycx.mixin;

import com.ycx.Client.AutoDupe;
import com.ycx.Client.QuickStorage;
import com.ycx.Handler.config.Configs;
import com.ycx.MainClient;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC < 12002
//$$ import net.minecraft.network.ClientConnection;
//$$ import net.minecraft.network.Packet;
//$$ import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
//$$ import org.spongepowered.asm.mixin.Final;
//$$ import org.spongepowered.asm.mixin.Shadow;
//#endif
@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    //#if MC < 12002
    //$$ @Final
    //$$ @Shadow
    //$$ private ClientConnection connection;
    //$$
    //$$ @Inject(at = @At("HEAD"),method = "sendPacket", cancellable = true)
    //$$ public void sendPacket(Packet<?> packet, CallbackInfo ci) {
    //$$
    //$$     ClientPlayerEntity player = MainClient.MC.player;
    //$$     if (player == null) return;
    //$$
    //$$     double x = player.getX();
    //$$     double y = player.getY();
    //$$     double z = player.getZ();
    //$$     float yaw = player.getYaw();
    //$$     float pitch = player.getPitch();
    //$$     boolean onGround = player.isOnGround();
    //$$
    //$$     Packet<?> newPacket = packet;
    //$$
    //$$     if (Configs.ELYTRA.getBooleanValue() && player.isFallFlying()) {
    //$$
    //$$         float newPitch = (Math.min(pitch, (float) -Configs.ELYTRA_PITCH.getDoubleValue()));
    //$$
    //$$         if (packet instanceof PlayerMoveC2SPacket.Full){
    //$$             newPacket = new PlayerMoveC2SPacket.Full(x, y, z, yaw, newPitch, onGround);
    //$$         }else if  (packet instanceof PlayerMoveC2SPacket.LookAndOnGround){
    //$$             newPacket = new PlayerMoveC2SPacket.LookAndOnGround(yaw, newPitch, onGround);
    //$$         }
    //$$
    //$$         this.connection.send(newPacket);
    //$$         ci.cancel();
    //$$     }
    //$$     if (Configs.ANTIHUNGER.getBooleanValue() && onGround
    //$$             && MainClient.MC.interactionManager != null && !MainClient.MC.interactionManager.isBreakingBlock()) {
    //$$
    //$$         if (packet instanceof PlayerMoveC2SPacket.Full) {
    //$$             newPacket = new PlayerMoveC2SPacket.Full(x, y, z, yaw, pitch, false);
    //$$         } else if (packet instanceof PlayerMoveC2SPacket.PositionAndOnGround) {
    //$$             newPacket = new PlayerMoveC2SPacket.PositionAndOnGround(x, y, z, false);
    //$$         } else if (packet instanceof PlayerMoveC2SPacket.LookAndOnGround) {
    //$$             newPacket = new PlayerMoveC2SPacket.LookAndOnGround(yaw, pitch, false);
    //$$         } else if (packet instanceof PlayerMoveC2SPacket.OnGroundOnly) {
    //$$             newPacket = new PlayerMoveC2SPacket.OnGroundOnly(false);
    //$$         }
    //$$
    //$$         this.connection.send(newPacket);
    //$$         ci.cancel();
    //$$
    //$$     }
    //$$
    //$$ }
    //#endif

    @Inject(at = @At("TAIL"),method = "onInventory")
    public void onInventory(InventoryS2CPacket packet, CallbackInfo ci){
        if (QuickStorage.runningDrop) QuickStorage.dropItem();
        if (QuickStorage.runningStore) QuickStorage.storeItem();
        if (AutoDupe.runningDupe) AutoDupe.dupeItem();
      //  if (QuickShulkerRestock.runningRestock) QuickShulkerRestock.itemRestockRunning();
    }

    @Inject(at = @At("HEAD"), method = "onEntityVelocityUpdate", cancellable = true)
    private void elytraNoSlowdown(EntityVelocityUpdateS2CPacket packet, CallbackInfo ci) {

        ClientPlayerEntity player = MainClient.MC.player;
        if (player == null) return;

        if (Configs.ELYTRA.getBooleanValue() && player.isFallFlying()) {

            if (packet.getId() == player.getId()) ci.cancel();
        }

    }

}
