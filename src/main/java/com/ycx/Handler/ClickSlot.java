package com.ycx.Handler;

import com.ycx.MainClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.screen.slot.SlotActionType;

public class ClickSlot {

    private static final MinecraftClient mc = MainClient.MC;

    /**
    * 将指定位置的物品拿到光标上，相当于鼠标左键单击拿起物品
    */
    public static void PICKUP_ALL(int syncId, int slot, ClientPlayerEntity player)
    {
        if (mc.interactionManager == null) return;
        mc.interactionManager.clickSlot(syncId, slot, 0, SlotActionType.PICKUP, player);
    }

    /**
     * 将指定位置的物品拿“一半”到光标上，相当于鼠标右键单击拿起物品
     */
    public static void PICKUP_HALF(int syncId, int slot, ClientPlayerEntity player)
    {
        if (mc.interactionManager == null) return;
        mc.interactionManager.clickSlot(syncId, slot, 1, SlotActionType.PICKUP, player);
    }

    /**
     *在玩家的库存和打开的屏幕处理程序之间快速移动物品
     */
    public static void QUICK_MOVE(int syncId, int slot, ClientPlayerEntity player)
    {
        if (mc.interactionManager == null) return;
        mc.interactionManager.clickSlot(syncId, slot, 0, SlotActionType.QUICK_MOVE, player);
    }

    /**
     * 将指定位置的物品扔出，相当于Ctrl+Q
     */
    public static void THROW_ALL(int syncId, int slot, ClientPlayerEntity player)
    {
        if (mc.interactionManager == null) return;
        mc.interactionManager.clickSlot(syncId, slot, 1, SlotActionType.THROW, player);
    }
    /**
     * 将指定位置的物品扔出“一个”，相当于Q
     */
    public static void THROW_ONE(int syncId, int slot, ClientPlayerEntity player)
    {
        if (mc.interactionManager == null) return;
        mc.interactionManager.clickSlot(syncId, slot, 0, SlotActionType.THROW, player);
    }

    /**
     * 将指定位置的物品“分一个”到多个目标位置,目标位置须为空
     */
    public static void CRAFT_ONE(int syncId, int from, int[] to, ClientPlayerEntity player)
    {
        if (mc.interactionManager == null) return;

        int a = -999;
        mc.interactionManager.clickSlot(syncId, from, 0, SlotActionType.PICKUP,      player);
        mc.interactionManager.clickSlot(syncId, a,    4, SlotActionType.QUICK_CRAFT, player);
        for (int i : to)
        {
            mc.interactionManager.clickSlot(syncId, i, 5, SlotActionType.QUICK_CRAFT, player);

        }
        mc.interactionManager.clickSlot(syncId, a,    6, SlotActionType.QUICK_CRAFT, player);
        mc.interactionManager.clickSlot(syncId, from, 0, SlotActionType.PICKUP,      player);
    }

    /**
     * 将指定位置的物品“均分”到多个目标位置,目标位置须为空
     */
    public static void CRAFT_ALL(int syncId, int from, int[] to, ClientPlayerEntity player)
    {
        if (mc.interactionManager == null) return;

        int a = -999;
        mc.interactionManager.clickSlot(syncId, from,0, SlotActionType.PICKUP,      player);
        mc.interactionManager.clickSlot(syncId, a,   0, SlotActionType.QUICK_CRAFT, player);
        for (int i : to)
        {
            mc.interactionManager.clickSlot(syncId, i,1, SlotActionType.QUICK_CRAFT, player);

        }
        mc.interactionManager.clickSlot(syncId, a,   2, SlotActionType.QUICK_CRAFT, player);
        mc.interactionManager.clickSlot(syncId, from,0, SlotActionType.PICKUP,      player);
    }

    /**
     * 将指定两个位置的物品交换
     */
    public static void EXCHANGE(int syncId, int from, int to, ClientPlayerEntity player)
    {
        if (mc.interactionManager == null) return;
        mc.interactionManager.clickSlot(syncId, from,0, SlotActionType.PICKUP, player);
        mc.interactionManager.clickSlot(syncId, to,  0, SlotActionType.PICKUP, player);
        mc.interactionManager.clickSlot(syncId, from,0, SlotActionType.PICKUP, player);
    }

    /**
     * 交换指定位置与玩家库存指定位置的物品
     */
    public static void SWAP(int syncId, int screenSlot, int invSlot, ClientPlayerEntity player)
    {
        if (mc.interactionManager == null) return;
        mc.interactionManager.clickSlot(syncId, screenSlot, invSlot, SlotActionType.SWAP, player);
    }
}
