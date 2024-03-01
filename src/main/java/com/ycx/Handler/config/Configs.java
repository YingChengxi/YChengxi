package com.ycx.Handler.config;


import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.options.*;


public class Configs {
    //打开配置界面快捷键
    public static final ConfigHotkey                MENU_OPEN_KEY           = Category.GENERAL.add(new ConfigHotkey("打开设置界面", "F7", "打开配置界面"));


    //喷气鞘翅
    public static final ConfigBooleanHotkeyed       ELYTRA                  = Category.GENERAL.add(new ConfigBooleanHotkeyed("喷气鞘翅", false, "", "白嫖烟花动力，空格加速，Shift减速"));
    public static final ConfigDouble                ELYTRA_ADD              = Category.FACTOR.add(new ConfigDouble("喷气鞘翅-加速系数", 0.15, 0, 10, "鞘翅加速系数，数值越大加速越快"));
    public static final ConfigDouble                ELYTRA_REDUCE           = Category.FACTOR.add(new ConfigDouble("喷气鞘翅-减速系数", 0.15, 0, 1, "鞘翅减速系数，数值越大减速越快"));
    public static final ConfigDouble                ELYTRA_PITCH            = Category.FACTOR.add(new ConfigDouble("喷气鞘翅-最小俯仰角度", -30, -90, 90, "鞘翅最小俯仰角度，关乎落地是否有摔落伤害，数值越小，飞翔姿态越自然"));
    public static final ConfigBoolean               ELYTRA_HUTRTVIEW        = Category.FACTOR.add(new ConfigBoolean("喷气鞘翅-受伤视角摇晃", false, "禁用飞行时受伤视角摇晃"));


    //使用物品减速
    public static final ConfigBooleanHotkeyed       SLOWDOWN                = Category.BAN.add(new ConfigBooleanHotkeyed("禁用使用物品减速", false, "", "取消使用物品时的减速"));

    //使用物品可攻击
    public static final ConfigBooleanHotkeyed       SHOULDATTACK            = Category.BAN.add(new ConfigBooleanHotkeyed("禁用使用物品不可攻击", false, "", "取消使用物品时不能攻击的限制"));

    //自动进食
    public static final ConfigBooleanHotkeyed       AUTOEAT                 = Category.GENERAL.add(new ConfigBooleanHotkeyed("自动进食", false, "", "自动食用最合适的食物。\n注：当玩家按住右键或左键时，为不影响玩家操作，不会自动进食"));
    public static final ConfigStringList            AUTOEAT_LIST            = Category.FACTOR.add(new ConfigStringList("自动进食-食物列表", ImmutableList.of("apple", "bread", "cooked_porkchop", "cooked_beef"), "只会自动进食食物列表里的食物。\n若为空，会根据当前饱食度选择最佳食物，不包括紫菘果及含中毒效果的食物。"));
    public static final ConfigBoolean               AUTOEAT_SELECT          = Category.FACTOR.add(new ConfigBoolean("自动进食-搜索背包", false, "自动进食是否能从背包拿食物，若关闭，则只从快捷栏拿取"));
    public static final ConfigBoolean               AUTOEAT_WALKING         = Category.FACTOR.add(new ConfigBoolean("自动进食-行走开关", false, "走路的时候是否自动进食，可以配合“禁用使用物品减速”不会减速"));
    public static final ConfigBoolean               AUTOEAT_FIGHT           = Category.FACTOR.add(new ConfigBoolean("自动进食-瞄准开关", false, "瞄准实体的时候是否自动进食"));

    //减缓饥饿
    public static final ConfigBooleanHotkeyed       ANTIHUNGER              = Category.GENERAL.add(new ConfigBooleanHotkeyed("减缓饥饿", false, "", "减缓走路时饱食度下降"));

    //存取助手
    public static final ConfigBooleanHotkeyed       AUTOSTORE               = Category.GENERAL.add(new ConfigBooleanHotkeyed("合成存取助手", false, "", "根据ItemScroller模组的当前选中配方，\n鼠标左键点击扔出配方物品，右键存储合成物品"));

    //DEBUG
    public static final ConfigBoolean               DEBUG                   = Category.DEBUG.add(new ConfigBoolean("DEBUG显示开关", false, "DEBUG显示开关"));
    public static final ConfigHotkey                TEST                    = Category.DEBUG.add(new ConfigHotkey("Test", "", "没有任何作用"));


    //自动搭路
    public static final ConfigBooleanHotkeyed       AUTOBRIDGE              = Category.GENERAL.add(new ConfigBooleanHotkeyed("自动搭路", false, "", "使用快捷栏中的方块自动搭路，Shift临时关闭"));
    public static final ConfigBoolean               AUTOBRIDGE_INAIR        = Category.FACTOR.add(new ConfigBoolean("自动搭路-浮空搭路", false, "斗宗强者"));


    //暂停药水时间
    public static final ConfigBooleanHotkeyed       POTIONFROZEN             = Category.CLIENT.add(new ConfigBooleanHotkeyed("冻结效果状态", false, "", "冻结效果状态时间"));


    //合成复制助手
    public static final ConfigBooleanHotkeyed       AUTODUPE                 = Category.GENERAL.add(new ConfigBooleanHotkeyed("合成复制助手", false, "", "根据ItemScroller模组的当前选中配方，\n手拿潜影盒右键合成台复制物品，\n依赖快捷潜影盒mod"));


    //禁用骆驼跳跃冷却
    public static final ConfigBooleanHotkeyed       CAMELDASHCOOLDOWN        = Category.BAN.add(new ConfigBooleanHotkeyed("禁用骆驼跳跃冷却", false, "", "使骆驼可以连续跳跃"));


    //坐骑蓄力修改
    public static final ConfigBooleanHotkeyed       MOUNTJUMPSTRENGTH        = Category.GENERAL.add(new ConfigBooleanHotkeyed("坐骑蓄力修改", false, "", "使坐骑跳跃蓄力进度条始终保持在设定位置，\n详细设置见参数。"));
    public static final ConfigDouble                JUMPSTRENGTH_FACTOR      = Category.FACTOR.add(new ConfigDouble("坐骑蓄力修改-进度条百分比", 1, 0, 1, "使坐骑跳跃蓄力进度条始终保持在设定位置"));


}
