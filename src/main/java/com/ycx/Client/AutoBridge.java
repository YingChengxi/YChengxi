package com.ycx.Client;

import com.ycx.MainClient;
import com.ycx.Handler.config.Configs;
import fi.dy.masa.malilib.util.PositionUtils;
import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.EmptyBlockView;

public class AutoBridge {
    private static final MinecraftClient mc = MainClient.MC;

    public static void bridge() {
        if (mc.player == null || mc.world == null) return;

        BlockPos belowPlayer = new BlockPos(mc.player.getBlockPos()).down();
        if (!mc.world.getBlockState(belowPlayer).isReplaceable()) return;

        int newSlot = findValidBlockSlot();
        if (newSlot == -1) return;

        int oldSlot = mc.player.getInventory().selectedSlot;
        mc.player.getInventory().selectedSlot = newSlot;

        scaffoldTo(belowPlayer);

        mc.player.getInventory().selectedSlot = oldSlot;
    }

    private static int findValidBlockSlot() {
        if (mc.player == null || mc.world == null) return -1;

        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.isEmpty() || !(stack.getItem() instanceof BlockItem)) continue;

            Block block = Block.getBlockFromItem(stack.getItem());
            BlockState state = block.getDefaultState();
            if (!state.isFullCube(EmptyBlockView.INSTANCE, BlockPos.ORIGIN)) continue;

            if (block instanceof FallingBlock && FallingBlock.canFallThrough(mc.world.getBlockState(new BlockPos(mc.player.getBlockPos()).down().down())))
                continue;

            return i;
        }
        return -1;
    }

    private static void scaffoldTo(BlockPos belowPlayer) {
        if (placeBlock(belowPlayer)) return;

        for (Direction side : Direction.values()) {
            if (placeBlock(belowPlayer.offset(side))) return;

            for (Direction side2 : Direction.values()) {
                if (!side.getOpposite().equals(side2) && placeBlock(belowPlayer.offset(side).offset(side2))) return;
            }
        }
    }

    private static boolean placeBlock(BlockPos pos) {
        if (mc.player == null || mc.world == null || mc.interactionManager == null) return false;

        Vec3d eyesPos = new Vec3d(mc.player.getX(), mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose()), mc.player.getZ());

        for (Direction side : Direction.values()) {
            BlockPos neighbor = pos.offset(side);
            Direction oppositeSide = side.getOpposite();

            if (eyesPos.squaredDistanceTo(Vec3d.ofCenter(pos)) >= eyesPos.squaredDistanceTo(Vec3d.ofCenter(neighbor))) continue;

            if (mc.world.getBlockState(neighbor).getOutlineShape(mc.world, neighbor) == VoxelShapes.empty()) continue;

            Vec3d hitVec = Vec3d.ofCenter(neighbor).add(Vec3d.of(oppositeSide.getVector()).multiply(0.5));
            if (eyesPos.squaredDistanceTo(hitVec) > 18.0625) continue;

            PlayerMoveC2SPacket.LookAndOnGround packet = new PlayerMoveC2SPacket.LookAndOnGround(mc.player.getYaw(), mc.player.getPitch(), mc.player.isOnGround());
            mc.player.networkHandler.sendPacket(packet);

            BlockHitResult hitResult = new BlockHitResult(hitVec, side, pos, false);
            mc.interactionManager.interactBlock(mc.player,  Hand.MAIN_HAND, hitResult);
            mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
            mc.player.swingHand(Hand.MAIN_HAND);

            return true;
        }

        if (Configs.AUTOBRIDGE_INAIR.getBooleanValue()) {
            Direction facing = PositionUtils.getClosestLookingDirection(mc.player).getOpposite();
            Vec3d hitVec = PositionUtils.getHitVecCenter(pos, facing);
            BlockHitResult context = new BlockHitResult(hitVec, facing, pos, false);

            ItemStack stack = mc.player.getMainHandStack();
            if (!stack.isEmpty() && stack.getItem() instanceof BlockItem) {
                mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, context);
                return true;
            }
        }

        return false;
    }
}
