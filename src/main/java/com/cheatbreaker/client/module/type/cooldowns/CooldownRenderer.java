package com.cheatbreaker.client.module.type.cooldowns;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.config.Setting;
import com.cheatbreaker.client.module.type.armourstatus.ArmourStatusModule;
import com.cheatbreaker.client.ui.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class CooldownRenderer {
   public long time;
   public Minecraft recoveredField2376 = Minecraft.getMinecraft();
   public String name;
   public int itemId;
   public ItemStack item;
   public long duration;

   public int method_06969() {
      return this.itemId;
   }

   public void method_06970(long var1) {
      this.duration = var1;
   }

   public boolean isTimeOver() {
      return this.time < System.currentTimeMillis() - this.duration;
   }

   public void method_06971(Setting var1, float var2, float var3, int var4) {
      this.render(var1, var2, var3, var4, this.duration - (System.currentTimeMillis() - this.time), null);
   }

   public void renderPreview(Setting theme, float x, float y, int color) {
      this.render(theme, x, y, color, this.duration, "16.0");
   }

   private void render(Setting theme, float x, float y, int color, double remaining, String previewText) {
      boolean depthEnabled = GlStateManager.depthState.depthTest.currentState;
      boolean depthMask = GlStateManager.depthState.maskEnabled;
      try {
         this.renderContents(theme, x, y, color, remaining, previewText);
      } finally {
         GlStateManager.depthMask(depthMask);
         GlStateManager.depthState.depthTest.setState(depthEnabled);
      }
   }

   private void renderContents(Setting var1, float var2, float var3, int var4, double var8, String previewText) {
      byte var5 = 17;
      GL11.glPushMatrix();
      float var6 = ArmourStatusModule.renderItem.zLevel;
      try {
         ArmourStatusModule.renderItem.zLevel = -150.50002F;
         float var7 = 1.35F;
         GL11.glTranslatef(var2 + var5, var3 + var5, 0.0F);
         GL11.glScalef(var7, var7, var7);
         RenderHelper.enableStandardItemLighting();
         ArmourStatusModule.renderItem.renderItemAndEffectIntoGUI(this.item, -8, -8);
      } finally {
         RenderHelper.disableStandardItemLighting();
         GlStateManager.disableBlend();
         GL11.glPopMatrix();
         ArmourStatusModule.renderItem.zLevel = var6;
      }
      // The ring and timer are HUD overlays, so item depth must not hide them.
      GlStateManager.disableDepth();
      GlStateManager.depthMask(false);
      if (!(var8 <= 0.0)) {
         String var10 = var1.method_08874();
         switch (var10) {
            case "Bright":
               GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.2F);
               RenderUtil.method_22055(var2 + var5, var3 + var5, var5, 0.0, (float)this.duration / 3.95F, (int)this.duration, var8);
               GL11.glColor4f(0.9F, 0.9F, 0.9F, 1.0F);
               RenderUtil.method_22055(var2 + var5, var3 + var5, var5 + 0.1F, var5 - 2, (float)this.duration / 3.95F, (int)this.duration, this.duration);
               GL11.glColor4f(0.35F, 0.35F, 0.35F, 0.6F);
               RenderUtil.method_22055(var2 + var5, var3 + var5, var5 + 0.1F, var5 - 2, (float)this.duration / 3.95F, (int)this.duration, var8);
               break;
            case "Dark":
               GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.2F);
               RenderUtil.method_22052(var2 + var5, var3 + var5, var5);
               GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.2F);
               RenderUtil.method_22055(var2 + var5, var3 + var5, var5, 0.0, (float)this.duration / 3.95F, (int)this.duration, var8);
               GL11.glColor4f(0.0F, 0.9F, 0.0F, 1.0F);
               RenderUtil.method_22055(var2 + var5, var3 + var5, var5 + 0.1F, var5 - 2, (float)this.duration / 3.95F, (int)this.duration, this.duration);
               GL11.glColor4f(0.0F, 0.5F, 0.0F, 1.0F);
               RenderUtil.method_22055(var2 + var5, var3 + var5, var5 + 0.1F, var5 - 2, (float)this.duration / 3.95F, (int)this.duration, var8);
               break;
            case "Colored":
               float var12 = (var4 >> 24 & 0xFF) / 255.0F;
               float var13 = (var4 >> 16 & 0xFF) / 255.0F;
               float var14 = (var4 >> 8 & 0xFF) / 255.0F;
               float var15 = (var4 & 0xFF) / 255.0F;
               GL11.glColor4f(var13, var14, var15, 0.15F * var12);
               RenderUtil.method_22052(var2 + var5, var3 + var5, var5);
               GL11.glColor4f(var13, var14, var15, 0.25F * var12);
               RenderUtil.method_22055(var2 + var5, var3 + var5, var5, 0.0, (float)this.duration / 3.95F, (int)this.duration, var8);
               GL11.glColor4f(var13, var14, var15, var12);
               RenderUtil.method_22055(var2 + var5, var3 + var5, var5 + 0.1F, var5 - 2, (float)this.duration / 3.95F, (int)this.duration, this.duration);
               GL11.glColor4f(var13, var14, var15, 0.15F * var12);
               RenderUtil.method_22055(var2 + var5, var3 + var5, var5 + 0.1F, var5 - 2, (float)this.duration / 3.95F, (int)this.duration, var8);
            case "No Ring":
         }

         var10 = previewText != null ? previewText
            : String.format("%." + CheatBreaker.getInstance().getModuleManager().cooldowns.recoveredField653.getValue() + "f", var8 / 1000.0);
         CheatBreaker.getInstance()
            .recoveredField1589
            .drawStringWithShadow(
               var10, var2 + var5 - CheatBreaker.getInstance().recoveredField1589.getStringWidth(var10) / 2.0F, var3 + var5 / 2 + 4.0F, -1, 1862270976
            );
      }
   }

   public long method_06968() {
      return this.duration;
   }

   public long method_06973() {
      return this.time;
   }

   public Minecraft method_06966() {
      return this.recoveredField2376;
   }

   public void method_06967() {
      this.time = System.currentTimeMillis();
   }

   public String method_06975() {
      return this.name;
   }

   public CooldownRenderer(String var1, int var2, long var3) {
      this.name = var1;
      this.itemId = var2;
      this.duration = var3;
      this.time = System.currentTimeMillis();
      this.item = new ItemStack(Item.getItemById(var2));
   }

   public ItemStack method_06972() {
      return this.item;
   }
}
