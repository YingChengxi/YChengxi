package com.ycx.Client;

import com.ycx.Handler.ClickSlot;
import com.ycx.MainClient;
import fi.dy.masa.itemscroller.recipes.RecipePattern;
import fi.dy.masa.itemscroller.recipes.RecipeStorage;
import fi.dy.masa.malilib.util.InventoryUtils;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;



public class AutoDupe {
    public static boolean isLoadMod = FabricLoader.getInstance().isModLoaded("itemscroller");
    public static MinecraftClient mc = MainClient.MC;
    static RecipePattern recipe = null;

    public static boolean runningDupe = false;

    public static boolean recipeIsEmpty(){
        recipe = RecipeStorage.getInstance().getSelectedRecipe();
        return recipe.getResult().isEmpty();
    }
    public static void dupeItem(){
        runningDupe = false;
        ClientPlayerEntity player = mc.player;
        if (player == null)return;

        ScreenHandler sc = player.currentScreenHandler;
        PlayerInventory inventory = player.getInventory();

        if (recipeIsEmpty()) return;

        int boxSlot = inventory.selectedSlot;

        if (!(Block.getBlockFromItem(inventory.getStack(boxSlot).getItem()) instanceof ShulkerBoxBlock)) return;

        int i = 0;

        int b = -1;
        for (ItemStack recipeItem : recipe.getRecipeItems()){
            i++;
            if (recipeItem.isEmpty()) continue;
            for (int a = 10; a < 46; a++){
                if (InventoryUtils.areStacksEqual(sc.slots.get(a).getStack(), recipeItem)) {
                    b = i;
                    ClickSlot.PICKUP_ALL(sc.syncId, a, player);
                    ClickSlot.PICKUP_HALF(sc.syncId, i, player);
                    ClickSlot.PICKUP_ALL(sc.syncId, a, player);
                }
            }
        }
        if (b == -1)return;
        ItemStack recipeItemResult = recipe.getResult();
        ClickSlot.PICKUP_ALL(sc.syncId, boxSlot+37, player);
        ClickSlot.PICKUP_HALF(sc.syncId, 0, player);
        ClickSlot.PICKUP_ALL(sc.syncId, boxSlot+37, player);

        for (int c = 0; c<=1728; c++){
            if (player.currentScreenHandler.getSlot(0).getStack() == recipeItemResult) break;
            ClickSlot.PICKUP_ALL(sc.syncId, b, player);
            ClickSlot.PICKUP_ALL(sc.syncId, b, player);
            ClickSlot.PICKUP_ALL(sc.syncId, boxSlot+37, player);
            ClickSlot.PICKUP_HALF(sc.syncId, 0, player);
            ClickSlot.PICKUP_ALL(sc.syncId, boxSlot+37, player);
        }
        player.closeHandledScreen();
    }
    public static boolean invAvailable(){

        HitResult aimBlock = mc.crosshairTarget;
        ClientWorld world = mc.world;
        if (aimBlock == null) return false;
        if (world == null) return false;

        if (!(aimBlock instanceof BlockHitResult)) return false;

        BlockPos pos = ((BlockHitResult)aimBlock).getBlockPos();
        if (pos == null) return false;

        BlockState state = mc.world.getBlockState(pos);

        return state.isOf(Blocks.CRAFTING_TABLE);
    }

}
