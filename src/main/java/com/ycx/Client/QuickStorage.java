package com.ycx.Client;

import com.ycx.Handler.ClickSlot;
import com.ycx.MainClient;
import fi.dy.masa.itemscroller.recipes.RecipePattern;
import fi.dy.masa.itemscroller.recipes.RecipeStorage;
import fi.dy.masa.malilib.util.InventoryUtils;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.*;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import static net.minecraft.block.ShulkerBoxBlock.FACING;

public class QuickStorage {
    public static boolean isLoadMod = FabricLoader.getInstance().isModLoaded("itemscroller");
    public static MinecraftClient mc = MainClient.MC;
    static RecipePattern recipe = null;
    public static boolean runningDrop = false;
    public static boolean runningStore = false;
    public static boolean recipeIsEmpty(){
        recipe = RecipeStorage.getInstance().getSelectedRecipe();
        return recipe.getResult().isEmpty();
    }
    public static void dropItem(){
        runningDrop = false;

        ClientPlayerEntity player = mc.player;
        if (player == null)return;

        ScreenHandler sc = player.currentScreenHandler;

        if (recipeIsEmpty()) return;
        if (sc.equals(player.playerScreenHandler)) return;

        int size = sc.slots.get(0).inventory.size();

        for (ItemStack recipeItem : recipe.getRecipeItems()) {
            for (int i = 0; i < size; i++) {
                if (InventoryUtils.areStacksEqual(sc.slots.get(i).getStack(), recipeItem)) {
                    ClickSlot.THROW_ALL(sc.syncId, i, player);
                }
            }
        }
        player.closeHandledScreen();

        //System.out.println("Drop finish\n");
    }
    public static void storeItem(){
        runningStore = false;

        ClientPlayerEntity player = mc.player;
        if (player == null)return;

        ScreenHandler sc = player.currentScreenHandler;

        if (recipeIsEmpty()) return;
        if (sc.equals(player.playerScreenHandler)) return;

        int size = sc.slots.size();

        if (size < 36) return;

        for (int i = size-36; i < size; i++) {
            if (InventoryUtils.areStacksEqual(sc.slots.get(i).getStack(), recipe.getResult())){
                ClickSlot.QUICK_MOVE(sc.syncId, i, player);
            }
        }

        player.closeHandledScreen();

        //System.out.println("Store finish\n");

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
        Block block = state.getBlock();

        if (block instanceof ChestBlock ) {
            BlockPos blockPos = pos.up();
            return !world.getBlockState(blockPos).isSolidBlock(world, blockPos);
        }
        if (block instanceof ShulkerBoxBlock){
            ShulkerBoxBlockEntity entity = (ShulkerBoxBlockEntity) mc.world.getBlockEntity(pos);
            if (entity != null && entity.getAnimationStage() != ShulkerBoxBlockEntity.AnimationStage.CLOSED) {
                return true;
            }
            Box box = ShulkerEntity.calculateBoundingBox(state.get(FACING), 0.0f, 0.5f)
                    .offset(pos).contract(1.0E-6);
            return world.isSpaceEmpty(box);
        }
        if (block instanceof BarrelBlock){
            return true;
        }
        return false;
    }
}
