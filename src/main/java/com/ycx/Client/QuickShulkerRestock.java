package com.ycx.Client;

//import com.ycx.Handler.ClickSlot;
//import com.ycx.Handler.QuickShulkerUtils;
//import com.ycx.MainClient;
//import net.minecraft.client.MinecraftClient;
//import net.minecraft.client.network.ClientPlayerEntity;
//import net.minecraft.item.ItemStack;
//import net.minecraft.screen.ScreenHandler;
//import net.minecraft.screen.slot.Slot;
//import net.minecraft.util.collection.DefaultedList;



public class QuickShulkerRestock {
//    private static final MinecraftClient mc = MainClient.MC;
//    public static boolean runningRestock = false;
//    private static int boxSlot = -1;
//    public static void itemRestock(){
//        ClientPlayerEntity player = mc.player;
//        if (player == null || !player.currentScreenHandler.equals(player.playerScreenHandler)) return;
//
//
//        DefaultedList<Slot> slots = player.playerScreenHandler.slots;
//
//        for (int invSlot = 9; invSlot < slots.size(); invSlot++) {
//
//            ItemStack invStack = slots.get(invSlot).getStack();
//            if (invStack.isEmpty()) continue;
//            if (invStack.getCount() < invStack.getMaxCount()/2){
//                boxSlot = QuickShulkerUtils.findSlotWithItem(invStack,true);
//                if (boxSlot!=-1){
//                    runningRestock=true;
//                }
//            }
//        }
//
//
//
//    }
//    public static void itemRestockRunning(){
//        ClientPlayerEntity player = mc.player;
//        runningRestock=false;
//        System.out.print(boxSlot);
//        ScreenHandler sc = player.currentScreenHandler;
//        ClickSlot.PICKUP_HALF(sc.syncId,boxSlot,player);
//        player.closeHandledScreen();
//    }
}
