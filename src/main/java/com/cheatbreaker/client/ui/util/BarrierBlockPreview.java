package com.cheatbreaker.client.ui.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public final class BarrierBlockPreview {
   private static final ResourceLocation BARRIER = new ResourceLocation("textures/items/barrier.png");
   private static final ResourceLocation GLASS = new ResourceLocation("textures/blocks/glass.png");

   private BarrierBlockPreview() {}

   public static void draw(float x, float y, float width, float height) {
      GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT | GL11.GL_CURRENT_BIT | GL11.GL_LINE_BIT);
      try {
         GL11.glEnable(GL11.GL_TEXTURE_2D);
         GL11.glEnable(GL11.GL_BLEND);
         GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
         GL11.glDisable(GL11.GL_ALPHA_TEST);
         GL11.glDisable(GL11.GL_CULL_FACE);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

         // Upper-left half: x/width + y/height <= 1.
         Minecraft.getMinecraft().getTextureManager().bindTexture(BARRIER);
         GL11.glBegin(GL11.GL_TRIANGLES);
         GL11.glTexCoord2f(0.0F, 0.0F);
         GL11.glVertex2f(x, y);
         GL11.glTexCoord2f(0.0F, 1.0F);
         GL11.glVertex2f(x, y + height);
         GL11.glTexCoord2f(1.0F, 0.0F);
         GL11.glVertex2f(x + width, y);
         GL11.glEnd();

         Minecraft.getMinecraft().getTextureManager().bindTexture(GLASS);
         GL11.glBegin(GL11.GL_TRIANGLES);
         GL11.glTexCoord2f(1.0F, 0.0F);
         GL11.glVertex2f(x + width, y);
         GL11.glTexCoord2f(0.0F, 1.0F);
         GL11.glVertex2f(x, y + height);
         GL11.glTexCoord2f(1.0F, 1.0F);
         GL11.glVertex2f(x + width, y + height);
         GL11.glEnd();

         // A one-pixel divider from the top-right corner to the bottom-left corner.
         GL11.glDisable(GL11.GL_TEXTURE_2D);
         GL11.glLineWidth(1.0F);
         GL11.glColor4f(0.72F, 0.76F, 0.80F, 1.0F);
         GL11.glBegin(GL11.GL_LINES);
         GL11.glVertex2f(x + width, y);
         GL11.glVertex2f(x, y + height);
         GL11.glEnd();
      } finally {
         GL11.glPopAttrib();
      }
   }
}
