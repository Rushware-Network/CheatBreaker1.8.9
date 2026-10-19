package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBarrier extends Block {
   // Client rendering only. The actual barrier state and physical properties never change.
   private volatile IBlockState renderState;

   public IBlockState getRenderState() {
      return this.renderState;
   }

   public void setRenderState(IBlockState state) {
      this.renderState = state;
   }

   public BlockBarrier() {
      super(Material.barrier);
      this.setBlockUnbreakable();
      this.setResistance(6000001.0F);
      this.disableStats();
      this.translucent = true;
   }

   @Override
   public float getAmbientOcclusionLightValue() {
      return 1.0F;
   }

   @Override
   public int getRenderType() {
      return this.renderState == null ? -1 : 3;
   }

   @Override
   public EnumWorldBlockLayer getBlockLayer() {
      IBlockState state = this.renderState;
      return state == null ? super.getBlockLayer() : state.getBlock().getBlockLayer();
   }

   @Override
   public boolean shouldSideBeRendered(IBlockAccess world, BlockPos neighborPos, EnumFacing side) {
      if (this.renderState == null) {
         return super.shouldSideBeRendered(world, neighborPos, side);
      }

      Block neighbor = world.getBlockState(neighborPos).getBlock();
      return neighbor != this && !neighbor.isOpaqueCube();
   }

   @Override
   public void dropBlockAsItemWithChance(World var1, BlockPos var2, IBlockState var3, float var4, int var5) {
   }

   @Override
   public boolean isOpaqueCube() {
      return false;
   }
}
