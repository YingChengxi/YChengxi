package com.ycx.ClientHandler.AutoBridge;

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

public class AutoBridge {
    private static final MinecraftClient mc = MainClient.MC;
    public static void bridge(){
        assert mc.player != null;
        BlockPos belowPlayer = new BlockPos(mc.player.getPos()).down();


        assert mc.world != null;
        if(!mc.world.getBlockState(belowPlayer).getMaterial().isReplaceable())
            return;


        int newSlot = -1;
        for(int i = 0; i < 9; i++)
        {

            ItemStack stack = mc.player.getInventory().getStack(i);
            if(stack.isEmpty() || !(stack.getItem() instanceof BlockItem))
                continue;


            Block block = Block.getBlockFromItem(stack.getItem());
            BlockState state = block.getDefaultState();
            if(!state.isFullCube(EmptyBlockView.INSTANCE, BlockPos.ORIGIN))
                continue;


            if(block instanceof FallingBlock && FallingBlock
                    .canFallThrough(mc.world.getBlockState(belowPlayer.down())))
                continue;

            newSlot = i;
            break;
        }


        if(newSlot == -1)
            return;

        int oldSlot = mc.player.getInventory().selectedSlot;
        mc.player.getInventory().selectedSlot = newSlot;

        scaffoldTo(belowPlayer);

        mc.player.getInventory().selectedSlot = oldSlot;
    }
    private static void scaffoldTo(BlockPos belowPlayer)
    {

        if(placeBlock(belowPlayer))
            return;


        Direction[] sides = Direction.values();
        for(Direction side : sides)
        {
            BlockPos neighbor = belowPlayer.offset(side);
            if(placeBlock(neighbor))
                return;
        }


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


            if(eyesPos.squaredDistanceTo(Vec3d.ofCenter(pos)) >= eyesPos
                    .squaredDistanceTo(Vec3d.ofCenter(neighbor)))
                continue;


            assert mc.world != null;
            if(mc.world.getBlockState(neighbor).getOutlineShape(mc.world, neighbor) == VoxelShapes.empty())
                continue;

            Vec3d hitVec = Vec3d.ofCenter(neighbor)
                    .add(Vec3d.of(side2.getVector()).multiply(0.5));


            if(eyesPos.squaredDistanceTo(hitVec) > 18.0625)
                continue;


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
