package com.ycx.Handler;

import com.ycx.MainClient;
import fi.dy.masa.malilib.util.InventoryUtils;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.block.Block;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;


public class QuickShulkerUtils {

    private static final MinecraftClient mc = MainClient.MC;
    public static boolean loadQuickShulker = FabricLoader.getInstance().isModLoaded("quickshulker");


    //打开盒子   itemStack：要打开的盒子 slot 盒子所在槽
    public static boolean openShulker( ItemStack itemStack, int slot ) {

        if (!loadQuickShulker) {
            mc.inGameHud.setOverlayMessage(Text.of("没有安装快捷盒子！！"), false);
            return false;
        }
        if (Block.getBlockFromItem(itemStack.getItem()) instanceof ShulkerBoxBlock){
            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            buf.writeInt(slot);
            ClientPlayNetworking.send(
                    new Identifier("quickshulker", "open_shulker_packet"),
                    new PacketByteBuf(buf)
            );
            return true;
        }
        return false;

    }

    //根据传入的物品寻找对应的盒子并打开
    public static int findSlotWithItem(ItemStack itemStack, boolean ignoreNbt){
        ClientPlayerEntity player = mc.player;
        if (player == null || !player.currentScreenHandler.equals(player.playerScreenHandler)) return -1;

        DefaultedList<Slot> slots = player.playerScreenHandler.slots;

        for (int invSlot = 9; invSlot < slots.size(); invSlot++) {

            ItemStack invStack = slots.get(invSlot).getStack();
            if (!(Block.getBlockFromItem(invStack.getItem()) instanceof ShulkerBoxBlock)) continue;
            DefaultedList<ItemStack> storedItems = InventoryUtils.getStoredItems(invStack,-1);

            for (int boxSlot = 0; boxSlot < storedItems.size(); boxSlot++) {
                ItemStack boxStack = storedItems.get(boxSlot);
                if (itemStack.getItem().equals(boxStack.getItem())
                        && (ignoreNbt || itemStack.getNbt() == null || itemStack.getNbt().equals(boxStack.getNbt()))) {

                    if(openShulker(invStack, invSlot)){
                        return boxSlot;
                    }

                }
            }
        }
        return -1;
    }



}
