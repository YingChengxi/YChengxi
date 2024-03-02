package com.ycx.Client;

import com.ycx.Handler.ClickSlot;
import com.ycx.MainClient;
import com.ycx.Handler.config.Configs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.FoodComponents;
import net.minecraft.item.Item;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.IntStream;

public class AutoEat {
    private static final MinecraftClient mc = MainClient.MC;
    private static int oldSlot = -1;
    private static int timer;
    private static boolean move = false;
    public static boolean eat = false;

    public static void autoEat() {
        ClientPlayerEntity player = mc.player;
        if (player == null) return;

        if (!shouldEat()) {
            if (isEating()) stopEating();
            timer = 20;
            return;
        }


        if(timer > 0)
        {
            timer--;
            return;
        }


        HungerManager hungerManager = player.getHungerManager();
        int foodLevel = hungerManager.getFoodLevel();

        if (player.getHealth() < player.getMaxHealth() - 3 && hungerManager.isNotFull()) {
            eat(-1, player);
            return;
        }

        if (hungerManager.isNotFull()) {
            int maxPoints = 20 - foodLevel;
            eat(maxPoints, player);
        }
    }
    private static boolean shouldEat()
    {
        ClientPlayerEntity player = mc.player;
        if (player == null) return false;

        //创造模式不吃
        if(player.getAbilities().creativeMode)
            return false;

        //饥饿值满不吃
        if(!player.canConsume(false))
            return false;

        //挖掘中和使用中不吃
        if(mc.options.attackKey.isPressed() || mc.options.useKey.isPressed())
            return false;

        //走路
        if(!Configs.AUTOEAT_WALKING.getBooleanValue()
                && (player.forwardSpeed != 0 || player.sidewaysSpeed != 0))
            return false;

        //注视实体
        if(!Configs.AUTOEAT_FIGHT.getBooleanValue()
                && mc.crosshairTarget instanceof EntityHitResult)
            return false;

        return true;
    }
    private static void eat(int maxPoints , ClientPlayerEntity player)
    {

        PlayerInventory inventory = player.getInventory();
        int foodSlot = findBestFoodSlot(maxPoints , player);

        if(foodSlot == -1)
        {
            if(isEating())
                stopEating();
            timer = 20;
            return;
        }

        if(foodSlot < 9)
        {
            if(!isEating())
                oldSlot = inventory.selectedSlot;

            inventory.selectedSlot = foodSlot;
            eat=true;
            assert mc.interactionManager != null;
            //#if MC >= 12002
            mc.interactionManager.interactItem(player,Hand.MAIN_HAND);
            //#else
            //$$ mc.interactionManager.interactItem(player,player.world, Hand.MAIN_HAND);
            //#endif

        }else if(foodSlot == 40)
        {
            if(!isEating())
                oldSlot = inventory.selectedSlot;
            eat=true;
            assert mc.interactionManager != null;
            //#if MC >= 12002
            mc.interactionManager.interactItem(player, Hand.OFF_HAND);
            //#else
            //$$ mc.interactionManager.interactItem(player,player.world, Hand.OFF_HAND);
            //#endif

        }else
        {
            moveFoodToHotbar(foodSlot);
        }

    }
    private static int findBestFoodSlot(int maxPoints, ClientPlayerEntity player) {
        PlayerInventory inventory = player.getInventory();
        FoodComponent bestFood = null;
        int bestSlot = -1;
        int maxInvSlot = Configs.AUTOEAT_SELECT.getBooleanValue() ? 36 : 9;

        ArrayList<Integer> slots = new ArrayList<>();
        slots.add(40);
        IntStream.range(0, maxInvSlot).forEach(slots::add);

        Comparator<FoodComponent> comparatorHunger = Comparator.comparingInt(FoodComponent::getHunger);
        Comparator<FoodComponent> comparatorSaturationModifier = Comparator.comparingDouble(FoodComponent::getSaturationModifier);

        for (int slot : slots) {
            Item item = inventory.getStack(slot).getItem();
            if (!item.isFood()) continue;

            FoodComponent food = item.getFoodComponent();
            if (!Configs.AUTOEAT_LIST.getStrings().isEmpty()) {
                if (!Configs.AUTOEAT_LIST.getStrings().contains(item.toString())) continue;
            } else {
                if (food == FoodComponents.CHORUS_FRUIT) continue;
                assert food != null;
                if (food.getStatusEffects().stream().anyMatch(pair -> pair.getFirst().getEffectType() == StatusEffects.POISON)) continue;
            }

            if (maxPoints >= 0) {
                assert food != null;
                if (food.getHunger() > maxPoints) continue;
            }

            if ((bestFood == null || comparatorHunger.compare(food, bestFood) > 0)
                    || (comparatorHunger.compare(food, bestFood) == 0 && comparatorSaturationModifier.compare(food, bestFood) > 0)) {
                bestFood = food;
                bestSlot = slot;
            }
        }
        return bestSlot;
    }

    private static void moveFoodToHotbar(int foodSlot)
    {
        ClientPlayerEntity player = mc.player;
        if (player == null) return;

        PlayerInventory inventory = player.getInventory();
        if (move){
            move = false;
            ClickSlot.SWAP(0, foodSlot, inventory.selectedSlot, player);
        }else{
            if(inventory.getEmptySlot() < 9) {
                oldSlot = inventory.selectedSlot;
                ClickSlot.QUICK_MOVE(0, foodSlot, player);
            }else
            {
                move = true;
                oldSlot = foodSlot;
                ClickSlot.SWAP(0, foodSlot, inventory.selectedSlot, player);
            }
        }

    }

    public static boolean isEating()
    {

        return oldSlot != -1;

    }
    private static void stopEating()
    {
        ClientPlayerEntity player = mc.player;
        if (player == null) return;

        if (move){
            moveFoodToHotbar(oldSlot);
        }else{
            player.getInventory().selectedSlot = oldSlot;
        }
        oldSlot = -1;


    }
}
