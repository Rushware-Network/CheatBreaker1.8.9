package com.cheatbreaker.client.ui.util;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderUtil {
   public static final double recoveredField1394 = 30.0;
   public static float recoveredField1393 = 0.0F;

   public static void method_22061(int var0, int var1, int var2, int var3, float var4, int var5) {
      int var6 = var3 - var1;
      int var7 = var2 - var0;
      int var8 = var5 - var3;
      GL11.glScissor((int)(var0 * var4), (int)(var8 * var4), (int)(var7 * var4), (int)(var6 * var4));
   }

   public static void method_22056(float var0, float var1, float var2, float var3, int var4, int var5) {
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      float var6;
      float var7;
      float var8;
      float var9;
      Gui.drawRect((var6 = var0 * 2.0F) + 1.0F, (var7 = var1 * 2.0F) + 1.0F, (var8 = var2 * 2.0F) - 1.0F, (var9 = var3 * 2.0F) - 1.0F, var5);
      method_22066(var6, var7 + 1.0F, var9 - 2.0F, var4);
      method_22066(var8 - 1.0F, var7 + 1.0F, var9 - 2.0F, var4);
      method_22059(var6 + 2.0F, var8 - 3.0F, var7, var4);
      method_22059(var6 + 2.0F, var8 - 3.0F, var9 - 1.0F, var4);
      method_22059(var6 + 1.0F, var6 + 1.0F, var7 + 1.0F, var4);
      method_22059(var8 - 2.0F, var8 - 2.0F, var7 + 1.0F, var4);
      method_22059(var8 - 2.0F, var8 - 2.0F, var9 - 2.0F, var4);
      method_22059(var6 + 1.0F, var6 + 1.0F, var9 - 2.0F, var4);
      GL11.glScalef(2.0F, 2.0F, 2.0F);
   }

   public static void method_22059(float var0, float var1, float var2, int var3) {
      if (var1 < var0) {
         float var4 = var0;
         var0 = var1;
         var1 = var4;
      }

      Gui.drawRect(var0, var2, var1 + 1.0F, var2 + 1.0F, var3);
   }

   public static void method_22058(float var0, float var1, float var2, float var3, int var4, boolean var5) {
      if (var5) {
         Gui.drawRect(
            var0 + 1.0F,
            var1 + 1.0F,
            var2 + 1.0F,
            var3 + 1.0F,
            new Color((var4 >> 16 & 0xFF) / 4, (var4 >> 8 & 0xFF) / 4, (var4 & 0xFF) / 4, var4 >> 24 & 0xFF).getRGB()
         );
      }

      Gui.drawRect(var0, var1, var2, var3, var4);
   }

   public static void method_22066(float var0, float var1, float var2, int var3) {
      if (var2 < var1) {
         float var4 = var1;
         var1 = var2;
         var2 = var4;
      }

      Gui.drawRect(var0, var1 + 1.0F, var0 + 1.0F, var2, var3);
   }

   public static void method_22064(ResourceLocation var0, float var1, float var2, float var3, float var4) {
      float var5 = var3 / 2.0F;
      float var6 = 0.0F;
      float var7 = 0.0F;
      GL11.glEnable(3042);
      Minecraft.getMinecraft().renderEngine.bindTexture(var0);
      GL11.glBegin(7);
      GL11.glTexCoord2d(var6 / var5, var7 / var5);
      GL11.glVertex2d(var1, var2);
      GL11.glTexCoord2d(var6 / var5, (var7 + var5) / var5);
      GL11.glVertex2d(var1, var2 + var4);
      GL11.glTexCoord2d((var6 + var5) / var5, (var7 + var5) / var5);
      GL11.glVertex2d(var1 + var3, var2 + var4);
      GL11.glTexCoord2d((var6 + var5) / var5, var7 / var5);
      GL11.glVertex2d(var1 + var3, var2);
      GL11.glEnd();
      GL11.glDisable(3042);
   }

   public static void drawNametagIcon(ResourceLocation resource, float x, float y, float width, float height) {
      TextureManager textures = Minecraft.getMinecraft().getTextureManager();
      textures.bindTexture(resource);
      ITextureObject texture = textures.getTexture(resource);
      texture.setBlurMipmap(true, false);
      // Match the label pass: the visible pass must write depth so translucent
      // blocks rendered later cannot blend over the icon while sparing the text.
      // The through-wall pass already disables depth writes at the call site.
      try {
         Tessellator tessellator = Tessellator.getInstance();
         WorldRenderer renderer = tessellator.getWorldRenderer();
         renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
         renderer.pos(x, y, 0.0).tex(0.0, 0.0).endVertex();
         renderer.pos(x, y + height, 0.0).tex(0.0, 1.0).endVertex();
         renderer.pos(x + width, y + height, 0.0).tex(1.0, 1.0).endVertex();
         renderer.pos(x + width, y, 0.0).tex(1.0, 0.0).endVertex();
         tessellator.draw();
      } finally {
         texture.restoreLastBlurMipmap();
      }
   }

   public static void method_22063(ResourceLocation var0, float var1, float var2, float var3) {
      float var4 = var1 * 2.0F;
      float var5 = var1 * 2.0F;
      float var6 = 0.0F;
      float var7 = 0.0F;
      GL11.glEnable(3042);
      Minecraft.getMinecraft().getTextureManager().bindTexture(var0);
      GL11.glBegin(7);
      GL11.glTexCoord2d(var6 / var1, var7 / var1);
      GL11.glVertex2d(var2, var3);
      GL11.glTexCoord2d(var6 / var1, (var7 + var1) / var1);
      GL11.glVertex2d(var2, var3 + var5);
      GL11.glTexCoord2d((var6 + var1) / var1, (var7 + var1) / var1);
      GL11.glVertex2d(var2 + var4, var3 + var5);
      GL11.glTexCoord2d((var6 + var1) / var1, var7 / var1);
      GL11.glVertex2d(var2 + var4, var3);
      GL11.glEnd();
      GL11.glDisable(3042);
   }

   public static void method_22065(float var0, float var1, float var2, float var3, int var4, int var5) {
      float var6 = 0.00390625F;
      Tessellator var7 = Tessellator.getInstance();
      WorldRenderer var8 = var7.getWorldRenderer();
      var8.begin(7, DefaultVertexFormats.POSITION_TEX);
      var8.pos(var0, var1 + var5, recoveredField1393).tex(var2 * var6, (var3 + var5) * var6).endVertex();
      var8.pos(var0 + var4, var1 + var5, recoveredField1393).tex((var2 + var4) * var6, (var3 + var5) * var6).endVertex();
      var8.pos(var0 + var4, var1, recoveredField1393).tex((var2 + var4) * var6, var3 * var6).endVertex();
      var8.pos(var0, var1, recoveredField1393).tex(var2 * var6, var3 * var6).endVertex();
      var7.draw();
   }

   public static void drawIcon(ResourceLocation var0, float var1, float var2, float var3) {
      float var4 = var1 * 2.0F;
      float var5 = var1 * 2.0F;
      float var6 = 0.0F;
      float var7 = 0.0F;
      GL11.glEnable(3042);
      Minecraft.getMinecraft().renderEngine.bindTexture(var0);
      GL11.glBegin(7);
      GL11.glTexCoord2d(var6 / var1, var7 / var1);
      GL11.glVertex2d(var2, var3);
      GL11.glTexCoord2d(var6 / var1, (var7 + var1) / var1);
      GL11.glVertex2d(var2, var3 + var5);
      GL11.glTexCoord2d((var6 + var1) / var1, (var7 + var1) / var1);
      GL11.glVertex2d(var2 + var4, var3 + var5);
      GL11.glTexCoord2d((var6 + var1) / var1, var7 / var1);
      GL11.glVertex2d(var2 + var4, var3);
      GL11.glEnd();
      GL11.glDisable(3042);
   }

   public static void method_22053(double var0, double var2, double var4, double var6, double var8, double var10) {
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      Tessellator var12 = Tessellator.getInstance();

      for (double var13 = var8; var13 < var10; var13 += 0.5) {
         double var15 = var13 * Math.PI / 180.0;
         double var17 = (var13 - 1.0) * Math.PI / 180.0;
         double[] var19 = new double[]{Math.cos(var15) * var4, -Math.sin(var15) * var4, Math.cos(var17) * var4, -Math.sin(var17) * var4};
         double[] var20 = new double[]{Math.cos(var15) * var6, -Math.sin(var15) * var6, Math.cos(var17) * var6, -Math.sin(var17) * var6};
         WorldRenderer var21 = var12.getWorldRenderer();
         var21.begin(7, DefaultVertexFormats.POSITION);
         var21.pos(var0 + var20[0], var2 + var20[1], 0.0).endVertex();
         var21.pos(var0 + var20[2], var2 + var20[3], 0.0).endVertex();
         var21.pos(var0 + var19[2], var2 + var19[3], 0.0).endVertex();
         var21.pos(var0 + var19[0], var2 + var19[1], 0.0).endVertex();
         var12.draw();
      }

      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glDisable(2848);
      GL11.glDisable(3042);
      GL11.glEnable(3553);
   }

   public static void method_22052(double var0, double var2, double var4) {
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      Tessellator var6 = Tessellator.getInstance();
      WorldRenderer var7 = var6.getWorldRenderer();
      var7.begin(6, DefaultVertexFormats.POSITION);
      var7.pos(var0, var2, recoveredField1393).endVertex();
      double var8 = Math.PI * 2;
      double var10 = var8 / 30.0;

      for (double var12 = -var10; var12 < var8; var12 += var10) {
         var7.pos(var0 + var4 * Math.cos(-var12), var2 + var4 * Math.sin(-var12), recoveredField1393).endVertex();
      }

      var6.draw();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
   }

   public static void method_22062(int var0, int var1, int var2, int var3, ScaledResolution var4) {
      int var5 = var4.getScaleFactor();
      int var6 = var3 - var1;
      int var7 = var2 - var0;
      int var8 = var4.getScaledHeight() - var3;
      GL11.glScissor(var0 * var5, var8 * var5, var7 * var5, var6 * var5);
   }

   public static Color method_22060(int var0) {
      float var1 = (var0 >> 24 & 0xFF) / 255.0F;
      float var2 = (var0 >> 16 & 0xFF) / 255.0F;
      float var3 = (var0 >> 8 & 0xFF) / 255.0F;
      float var4 = (var0 & 0xFF) / 255.0F;
      return new Color(var2, var3, var4, var1);
   }

   public static void method_22057(float var0, float var1, float var2, float var3, int var4, int var5, int var6) {
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      float var7;
      float var8;
      float var9;
      float var10;
      Minecraft.getMinecraft()
         .ingameGUI
         .method_00889((var7 = var0 * 2.0F) + 1.0F, (var8 = var1 * 2.0F) + 1.0F, (var9 = var2 * 2.0F) - 1.0F, (var10 = var3 * 2.0F) - 1.0F, var5, var6);
      method_22066(var7, var8 + 1.0F, var10 - 2.0F, var4);
      method_22066(var9 - 1.0F, var8 + 1.0F, var10 - 2.0F, var4);
      method_22059(var7 + 2.0F, var9 - 3.0F, var8, var4);
      method_22059(var7 + 2.0F, var9 - 3.0F, var10 - 1.0F, var4);
      method_22059(var7 + 1.0F, var7 + 1.0F, var8 + 1.0F, var4);
      method_22059(var9 - 2.0F, var9 - 2.0F, var8 + 1.0F, var4);
      method_22059(var9 - 2.0F, var9 - 2.0F, var10 - 2.0F, var4);
      method_22059(var7 + 1.0F, var7 + 1.0F, var10 - 2.0F, var4);
      GL11.glScalef(2.0F, 2.0F, 2.0F);
   }

   public static void method_22055(double var0, double var2, double var4, double var6, double var8, int var10, double var11) {
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      var8 = (var8 + var10) % var10;
      Tessellator var13 = Tessellator.getInstance();

      for (double var14 = 360.0 / var10 * var8; var14 < 360.0 / var10 * (var8 + var11); var14++) {
         double var16 = var14 * Math.PI / 180.0;
         double var18 = (var14 - 1.0) * Math.PI / 180.0;
         double[] var20 = new double[]{Math.cos(var16) * var4, -Math.sin(var16) * var4, Math.cos(var18) * var4, -Math.sin(var18) * var4};
         double[] var21 = new double[]{Math.cos(var16) * var6, -Math.sin(var16) * var6, Math.cos(var18) * var6, -Math.sin(var18) * var6};
         WorldRenderer var22 = var13.getWorldRenderer();
         var22.begin(7, DefaultVertexFormats.POSITION);
         var22.pos(var0 + var21[0], var2 + var21[1], 0.0).endVertex();
         var22.pos(var0 + var21[2], var2 + var21[3], 0.0).endVertex();
         var22.pos(var0 + var20[2], var2 + var20[3], 0.0).endVertex();
         var22.pos(var0 + var20[0], var2 + var20[1], 0.0).endVertex();
         var13.draw();
      }

      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glDisable(2848);
      GL11.glDisable(3042);
      GL11.glEnable(3553);
   }

   public static void method_22054(double var0, double var2, double var4, double var6, double var8, int var10) {
      float var12 = (var10 >> 24 & 0xFF) / 255.0F;
      float var13 = (var10 >> 16 & 0xFF) / 255.0F;
      float var14 = (var10 >> 8 & 0xFF) / 255.0F;
      float var15 = (var10 & 0xFF) / 255.0F;
      GL11.glPushAttrib(0);
      GL11.glScaled(0.5, 0.5, 0.5);
      var0 *= 2.0;
      var2 *= 2.0;
      var4 *= 2.0;
      var6 *= 2.0;
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GL11.glColor4f(var13, var14, var15, var12);
      GL11.glEnable(2848);
      GL11.glBegin(9);

      for (int var11 = 0; var11 <= 90; var11 += 3) {
         GL11.glVertex2d(var0 + var8 + Math.sin(var11 * Math.PI / 180.0) * (var8 * -1.0), var2 + var8 + Math.cos(var11 * Math.PI / 180.0) * (var8 * -1.0));
      }

      for (int var20 = 90; var20 <= 180; var20 += 3) {
         GL11.glVertex2d(var0 + var8 + Math.sin(var20 * Math.PI / 180.0) * (var8 * -1.0), var6 - var8 + Math.cos(var20 * Math.PI / 180.0) * (var8 * -1.0));
      }

      for (int var21 = 0; var21 <= 90; var21 += 3) {
         GL11.glVertex2d(var4 - var8 + Math.sin(var21 * Math.PI / 180.0) * var8, var6 - var8 + Math.cos(var21 * Math.PI / 180.0) * var8);
      }

      for (int var22 = 90; var22 <= 180; var22 += 3) {
         GL11.glVertex2d(var4 - var8 + Math.sin(var22 * Math.PI / 180.0) * var8, var2 + var8 + Math.cos(var22 * Math.PI / 180.0) * var8);
      }

      GL11.glEnd();
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
      GL11.glDisable(2848);
      GL11.glScaled(2.0, 2.0, 2.0);
      GL11.glPopAttrib();
   }
}
