package com.ycx.ClientHandler.BelowBlock;

import com.ycx.MainClient;
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
import java.util.Arrays;

public class BelowBlock {
    private static final MinecraftClient mc = MainClient.MC;
    public static void water(){
        assert mc.player != null;
        BlockPos belowPlayer = new BlockPos(mc.player.getPos()).down();

        // check if block is already placed
        assert mc.world != null;
        if(!mc.world.getBlockState(belowPlayer).getMaterial().isReplaceable())
            return;

        // search blocks in hotbar
        int newSlot = -1;
        for(int i = 0; i < 9; i++)
        {
            // filter out non-block items
            ItemStack stack = mc.player.getInventory().getStack(i);
            if(stack.isEmpty() || !(stack.getItem() instanceof BlockItem))
                continue;

            // filter out non-solid blocks
            Block block = Block.getBlockFromItem(stack.getItem());
            BlockState state = block.getDefaultState();
            if(!state.isFullCube(EmptyBlockView.INSTANCE, BlockPos.ORIGIN))
                continue;

            // filter out blocks that would fall
            if(block instanceof FallingBlock && FallingBlock
                    .canFallThrough(mc.world.getBlockState(belowPlayer.down())))
                continue;

            newSlot = i;
            break;
        }

        // check if any blocks were found
        if(newSlot == -1)
            return;

        // set slot
        int oldSlot = mc.player.getInventory().selectedSlot;
        mc.player.getInventory().selectedSlot = newSlot;

        scaffoldTo(belowPlayer);

        // reset slot
        mc.player.getInventory().selectedSlot = oldSlot;
    }
    private static void scaffoldTo(BlockPos belowPlayer)
    {
        // tries to place a block directly under the player
        if(placeBlock(belowPlayer))
            return;

        // if that doesn't work, tries to place a block next to the block that's
        // under the player
        Direction[] sides = Direction.values();
        for(Direction side : sides)
        {
            BlockPos neighbor = belowPlayer.offset(side);
            if(placeBlock(neighbor))
                return;
        }

        // if that doesn't work, tries to place a block next to a block that's
        // next to the block that's under the player
        for(Direction side : sides)
            for(Direction side2 : Arrays.copyOfRange(sides, side.ordinal(), 6))
            {
                if(side.getOpposite().equals(side2))
                    continue;

                BlockPos neighbor = belowPlayer.offset(side).offset(side2);
                if(placeBlock(neighbor))
                    return;
            }
    }
    private static boolean placeBlock(BlockPos pos)
    {
        assert mc.player != null;
        Vec3d eyesPos = new Vec3d(mc.player.getX(),
                mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose()),
                mc.player.getZ());

        for(Direction side : Direction.values())
        {
            BlockPos neighbor = pos.offset(side);
            Direction side2 = side.getOpposite();

            // check if side is visible (facing away from player)
            if(eyesPos.squaredDistanceTo(Vec3d.ofCenter(pos)) >= eyesPos
                    .squaredDistanceTo(Vec3d.ofCenter(neighbor)))
                continue;

            // check if neighbor can be right-clicked
            assert mc.world != null;
            if(mc.world.getBlockState(neighbor).getOutlineShape(mc.world, neighbor) == VoxelShapes.empty())
                continue;

            Vec3d hitVec = Vec3d.ofCenter(neighbor)
                    .add(Vec3d.of(side2.getVector()).multiply(0.5));

            // check if hitVec is within range (4.25 blocks)
            if(eyesPos.squaredDistanceTo(hitVec) > 18.0625)
                continue;

            // place block
            PlayerMoveC2SPacket.LookAndOnGround packet =
                    new PlayerMoveC2SPacket.LookAndOnGround(mc.player.getYaw(),
                            mc.player.getPitch(), mc.player.isOnGround());
            mc.player.networkHandler.sendPacket(packet);
            BlockHitResult hitResult = new BlockHitResult(hitVec, side, pos, false);
            assert mc.interactionManager != null;
            mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND, hitResult);
            mc.interactionManager.interactItem(mc.player, mc.world, Hand.MAIN_HAND);
            mc.player.swingHand(Hand.MAIN_HAND);

            return true;
        }

        return false;
    }


}
