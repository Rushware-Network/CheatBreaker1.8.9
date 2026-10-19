package net.minecraft.client.particle;

import net.minecraft.block.BlockBarrier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class Barrier extends EntityFX {
   public Barrier(World var1, double var2, double var4, double var6, Item var8) {
      super(var1, var2, var4, var6, 0.0, 0.0, 0.0);
      this.a(Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getParticleIcon(var8));
      this.ar = this.as = this.at = 1.0F;
      this.v = this.w = this.x = 0.0;
      this.i = 0.0F;
      this.g = 80;
   }

   @Override
   public void renderParticle(WorldRenderer var1, Entity var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      float var9 = this.av.getMinU();
      float var10 = this.av.getMaxU();
      float var11 = this.av.getMinV();
      float var12 = this.av.getMaxV();
      float var13 = 0.5F;
      float var14 = (float)(this.p + (this.s - this.p) * var3 - aw);
      float var15 = (float)(this.q + (this.t - this.q) * var3 - ax);
      float var16 = (float)(this.r + (this.u - this.r) * var3 - ay);
      int var17 = this.b_(var3);
      int var18 = var17 >> 16 & 65535;
      int var19 = var17 & 65535;
      var1.pos(var14 - var4 * 0.5F - var7 * 0.5F, var15 - var5 * 0.5F, var16 - var6 * 0.5F - var8 * 0.5F)
         .tex(var10, var12)
         .color(this.ar, this.as, this.at, 1.0F)
         .lightmap(var18, var19)
         .endVertex();
      var1.pos(var14 - var4 * 0.5F + var7 * 0.5F, var15 + var5 * 0.5F, var16 - var6 * 0.5F + var8 * 0.5F)
         .tex(var10, var11)
         .color(this.ar, this.as, this.at, 1.0F)
         .lightmap(var18, var19)
         .endVertex();
      var1.pos(var14 + var4 * 0.5F + var7 * 0.5F, var15 + var5 * 0.5F, var16 + var6 * 0.5F + var8 * 0.5F)
         .tex(var9, var11)
         .color(this.ar, this.as, this.at, 1.0F)
         .lightmap(var18, var19)
         .endVertex();
      var1.pos(var14 + var4 * 0.5F - var7 * 0.5F, var15 - var5 * 0.5F, var16 + var6 * 0.5F - var8 * 0.5F)
         .tex(var9, var12)
         .color(this.ar, this.as, this.at, 1.0F)
         .lightmap(var18, var19)
         .endVertex();
   }

   @Override
   public int getFXLayer() {
      return 1;
   }

   public static class Factory implements IParticleFactory {
      @Override
      public EntityFX getEntityFX(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         if (((BlockBarrier)Blocks.barrier).getRenderState() != null) {
            return null;
         }
         return new Barrier(var2, var3, var5, var7, Item.getItemFromBlock(Blocks.barrier));
      }
   }
}
