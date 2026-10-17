package net.minecraft.client.renderer.entity;

import java.util.Random;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class RenderEntityItem extends Render<EntityItem> {
   public RenderItem itemRenderer;
   public Random field_177079_e = new Random();

   public RenderEntityItem(RenderManager var1, RenderItem var2) {
      super(var1);
      this.itemRenderer = var2;
      this.c = 0.15F;
      this.d = 0.75F;
   }

   public ResourceLocation getEntityTexture(EntityItem var1) {
      return TextureMap.locationBlocksTexture;
   }

   public int func_177078_a(ItemStack var1) {
      byte var2 = 1;
      if (var1.stackSize > 48) {
         var2 = 5;
      } else if (var1.stackSize > 32) {
         var2 = 4;
      } else if (var1.stackSize > 16) {
         var2 = 3;
      } else if (var1.stackSize > 1) {
         var2 = 2;
      }

      return var2;
   }

   public void doRender(EntityItem var1, double var2, double var4, double var6, float var8, float var9) {
      ItemStack var10 = var1.getEntityItem();
      this.field_177079_e.setSeed(187L);
      boolean var11 = false;
      if (this.bindEntityTexture(var1)) {
         this.b.renderEngine.getTexture(this.getEntityTexture(var1)).setBlurMipmap(false, false);
         var11 = true;
      }

      GlStateManager.enableRescaleNormal();
      GlStateManager.alphaFunc(516, 0.1F);
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.pushMatrix();
      IBakedModel var12 = this.itemRenderer.getAppearanceModel(var10, "dropped");
      int var13 = this.func_177077_a(var1, var2, var4, var6, var9, var12);

      for (int var14 = 0; var14 < var13; var14++) {
         if (var12.isGui3d()) {
            GlStateManager.pushMatrix();
            if (var14 > 0) {
               float var15 = (this.field_177079_e.nextFloat() * 2.0F - 1.0F) * 0.15F;
               float var16 = (this.field_177079_e.nextFloat() * 2.0F - 1.0F) * 0.15F;
               float var17 = (this.field_177079_e.nextFloat() * 2.0F - 1.0F) * 0.15F;
               GlStateManager.translate(var15, var16, var17);
            }

            GlStateManager.scale(0.5F, 0.5F, 0.5F);
            var12.getItemCameraTransforms().applyTransform(ItemCameraTransforms.TransformType.GROUND);
            this.itemRenderer.method_26134(var10, var12, "dropped");
            GlStateManager.popMatrix();
         } else {
            GlStateManager.pushMatrix();
            var12.getItemCameraTransforms().applyTransform(ItemCameraTransforms.TransformType.GROUND);
            this.itemRenderer.method_26134(var10, var12, "dropped");
            GlStateManager.popMatrix();
            float var18 = var12.getItemCameraTransforms().ground.scale.x;
            float var19 = var12.getItemCameraTransforms().ground.scale.y;
            float var20 = var12.getItemCameraTransforms().ground.scale.z;
            GlStateManager.translate(0.0F * var18, 0.0F * var19, 0.046875F * var20);
         }
      }

      GlStateManager.popMatrix();
      GlStateManager.disableRescaleNormal();
      GlStateManager.disableBlend();
      this.bindEntityTexture(var1);
      if (var11) {
         this.b.renderEngine.getTexture(this.getEntityTexture(var1)).restoreLastBlurMipmap();
      }

      super.doRender(var1, var2, var4, var6, var8, var9);
   }

   public int func_177077_a(EntityItem var1, double var2, double var4, double var6, float var8, IBakedModel var9) {
      ItemStack var10 = var1.getEntityItem();
      Item var11 = var10.getItem();
      if (var11 == null) {
         return 0;
      } else {
         boolean var12 = var9.isGui3d();
         int var13 = this.func_177078_a(var10);
         float var14 = 0.25F;
         float var15 = MathHelper.sin((var1.getAge() + var8) / 10.0F + var1.hoverStart) * 0.1F + 0.1F;
         float var16 = var9.getItemCameraTransforms().getTransform(ItemCameraTransforms.TransformType.GROUND).scale.y;
         GlStateManager.translate((float)var2, (float)var4 + var15 + 0.25F * var16, (float)var6);
         if (var12 || this.b.options != null) {
            float var17 = ((var1.getAge() + var8) / 20.0F + var1.hoverStart) * (180.0F / (float)Math.PI);
            GlStateManager.rotate(var17, 0.0F, 1.0F, 0.0F);
         }

         if (!var12) {
            float var20 = -0.0F * (var13 - 1) * 0.5F;
            float var18 = -0.0F * (var13 - 1) * 0.5F;
            float var19 = -0.046875F * (var13 - 1) * 0.5F;
            GlStateManager.translate(var20, var18, var19);
         }

         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         return var13;
      }
   }
}
