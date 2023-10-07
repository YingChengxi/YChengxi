package com.ycx.config;


import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.options.*;


public class Configs {
    //打开配置界面快捷键
    public static final ConfigHotkey                MENU_OPEN_KEY        = Category.GENERAL.add(new ConfigHotkey("打开设置界面", "", "打开配置界面"));


    //喷气鞘翅
    public static final ConfigBoolean               ELYTRAINFO           = Category.GENERAL.add(new ConfigBoolean("【 喷气鞘翅 】",false, "展开“喷气鞘翅”详细设置，\n需要切换标签页刷新条目"));
    public static final ConfigBooleanHotkeyed       ELYTRA               = TrueAdd.ELYTRA.add(new ConfigBooleanHotkeyed("  喷气鞘翅（主开关）",false, "", "白嫖烟花动力，空格加速，Shift减速"));
    public static final ConfigDouble                ELYTRA_ADD           = TrueAdd.ELYTRA.add(new ConfigDouble("  → 加速系数",0.15,0,10,"鞘翅加速系数，数值越大加速越快"));
    public static final ConfigDouble                ELYTRA_REDUCE        = TrueAdd.ELYTRA.add(new ConfigDouble("  → 减速系数",0.15,0,1,"鞘翅减速系数，数值越大减速越快"));
    public static final ConfigDouble                ELYTRA_PITCH         = TrueAdd.ELYTRA.add(new ConfigDouble("  → 最小俯仰角度",-30,-90,90,"鞘翅最小俯仰角度，关乎落地是否有摔落伤害，数值越小，飞翔姿态越自然"));


    //使用物品减速
    public static final ConfigBooleanHotkeyed       SLOWDOWN             = Category.BAN.add(new ConfigBooleanHotkeyed("禁用使用物品减速",false,"","禁用使用物品时减速"));


    //自动进食
    public static final ConfigBoolean               AUTOEATINFO          = Category.GENERAL.add(new ConfigBoolean("【 自动进食 】",false, "展开“自动进食”详细设置，\n需要切换标签页刷新条目"));
    public static final ConfigBooleanHotkeyed       AUTOEAT              = TrueAdd.AUTOEAT.add(new ConfigBooleanHotkeyed("  自动进食（主开关）",false,"","饿了、残了，会自动食用最合适的食物。\n注：当玩家按住右键或左键时，为不影响玩家操作，不会自动进食"));
    public static final ConfigStringList            AUTOEAT_LIST         = TrueAdd.AUTOEAT.add(new ConfigStringList("  → 食物列表", ImmutableList.of("apple","bread","cooked_porkchop","cooked_beef"),"只会自动进食食物列表里的食物。\n若为空，会根据当前饱食度选择最佳食物，不包括紫菘果及含中毒效果的食物。"));
    public static final ConfigBoolean               AUTOEAT_SELECT       = TrueAdd.AUTOEAT.add(new ConfigBoolean("  → 从背包拿吃的？", false,"自动进食是否能从背包拿食物，若关闭，则只从快捷栏拿取"));
    public static final ConfigBoolean               AUTOEAT_WALKING      = TrueAdd.AUTOEAT.add(new ConfigBoolean("  → 走路的时候吃？",false,"走路的时候是否自动进食，可以配合“禁用使用物品减速”不会减速"));
    public static final ConfigBoolean               AUTOEAT_FIGHT        = TrueAdd.AUTOEAT.add(new ConfigBoolean("  → 打架的时候吃？",false,"瞄准实体的时候是否自动进食"));


    //合成助手
    public static final ConfigBooleanHotkeyed       SYNTH                = Category.GENERAL.add(new ConfigBooleanHotkeyed("合成助手",false,"","根据ItemScroller模组的当前选中配方，\n鼠标左键点击扔出配方物品，右键存储合成物品"));


    //TEST
    public static final ConfigHotkey                TEST                 = Category.DEBUG.add(new ConfigHotkey("Test","",""));

    //自动搭路
    public static final ConfigBooleanHotkeyed       AUTOBELOW            = Category.GENERAL.add(new ConfigBooleanHotkeyed("自动搭路",false,"","往脚下放方块"));


    public static void ADDCONFIG () {
        for (Category category : Category.values()){
            for (TrueAdd trueAdd : TrueAdd.values()){
                category.falseDel(trueAdd.getConfigs());
            }
        }

        if (ELYTRAINFO.getBooleanValue())
            Category.GENERAL.trueAdd(Category.GENERAL.getIndex(ELYTRAINFO),TrueAdd.ELYTRA.getConfigs());
        if (AUTOEATINFO.getBooleanValue())
            Category.GENERAL.trueAdd(Category.GENERAL.getIndex(AUTOEATINFO),TrueAdd.AUTOEAT.getConfigs());


    }

}


