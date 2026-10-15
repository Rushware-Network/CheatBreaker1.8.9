package net.minecraft.client.renderer.entity;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.module.type.DamageTintModule;
import com.cheatbreaker.client.module.type.NametagModule;
import com.cheatbreaker.client.module.type.NickHiderModule;
import com.cheatbreaker.client.util.player.PlayerNametagStyle;
import com.google.common.collect.Lists;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSpider;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.src.Config;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.optifine.EmissiveTextures;
import net.optifine.entity.model.CustomEntityModels;
import net.optifine.reflect.Reflector;
import net.optifine.shaders.Shaders;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.entity.RendererLivingEntity$EnumSwitch;

public abstract class RendererLivingEntity<T extends EntityLivingBase> extends Render<T> {
   public static Logger logger = LogManager.getLogger();
   public ModelBase f;
   public float recoveredField18;
   public boolean renderOutlines;
   public float recoveredField19;
   public EntityLivingBase renderEntity;
   public float recoveredField20;
   public float recoveredField21;
   public boolean renderModelPushMatrix;
   public static DynamicTexture textureBrightness = new DynamicTexture(16, 16);
   public static float NAME_TAG_RANGE = 64.0F;
   public List<LayerRenderer<T>> h;
   public float recoveredField22;
   public float recoveredField23;
   public float recoveredField24;
   public FloatBuffer brightnessBuffer = GLAllocation.createDirectFloatBuffer(4);
   public boolean renderLayersPushMatrix;
   public static float NAME_TAG_RANGE_SNEAK = 32.0F;
   public static boolean animateModelLiving = Boolean.getBoolean("animate.model.living");

   public void renderModel(T var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      boolean var8 = !var1.isInvisible();
      boolean var9 = !var8 && !var1.f(Minecraft.getMinecraft().thePlayer);
      if (var8 || var9) {
         if (!this.bindEntityTexture((T)var1)) {
            return;
         }

         if (var9) {
            GlStateManager.pushMatrix();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 0.15F);
            GlStateManager.depthMask(false);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 771);
            GlStateManager.alphaFunc(516, 0.003921569F);
         }

         this.f.render(var1, var2, var3, var4, var5, var6, var7);
         if (var9) {
            GlStateManager.disableBlend();
            GlStateManager.alphaFunc(516, 0.1F);
            GlStateManager.popMatrix();
            GlStateManager.depthMask(true);
         }
      }
   }

   static {
      int[] var0 = textureBrightness.getTextureData();

      for (int var1 = 0; var1 < 256; var1++) {
         var0[var1] = -1;
      }

      textureBrightness.updateDynamicTexture();
   }

   public boolean setDoRenderBrightness(T var1, float var2) {
      return this.setBrightness((T)var1, var2, true);
   }

   public int getColorMultiplier(T var1, float var2, float var3) {
      return 0;
   }

   public void doRender(T var1, double var2, double var4, double var6, float var8, float var9) {
      if (!Reflector.RenderLivingEvent_Pre_Constructor.exists()
         || !Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Pre_Constructor, var1, this, var2, var4, var6)) {
         if (animateModelLiving) {
            var1.aB = 1.0F;
         }

         GlStateManager.pushMatrix();
         GlStateManager.disableCull();
         this.f.p = this.getSwingProgress((T)var1, var9);
         this.f.isRiding = var1.au();
         if (Reflector.ForgeEntity_shouldRiderSit.exists()) {
            this.f.isRiding = var1.au() && var1.m != null && Reflector.callBoolean(var1.m, Reflector.ForgeEntity_shouldRiderSit);
         }

         this.f.r = var1.o_();

         try {
            float var10 = this.interpolateRotation(var1.aJ, var1.aI, var9);
            float var11 = this.interpolateRotation(var1.prevRotationYawHead, var1.aK, var9);
            float var12 = var11 - var10;
            if (this.f.isRiding && var1.m instanceof EntityLivingBase) {
               EntityLivingBase var13 = (EntityLivingBase)var1.m;
               var10 = this.interpolateRotation(var13.aJ, var13.aI, var9);
               var12 = var11 - var10;
               float var14 = MathHelper.wrapAngleTo180_float(var12);
               if (var14 < -85.0F) {
                  var14 = -85.0F;
               }

               if (var14 >= 85.0F) {
                  var14 = 85.0F;
               }

               var10 = var11 - var14;
               if (var14 * var14 > 2500.0F) {
                  var10 += var14 * 0.2F;
               }

               var12 = var11 - var10;
            }

            float var22 = var1.B + (var1.z - var1.B) * var9;
            this.renderLivingAt((T)var1, var2, var4, var6);
            float var23 = this.handleRotationFloat((T)var1, var9);
            this.rotateCorpse((T)var1, var23, var10, var9);
            GlStateManager.enableRescaleNormal();
            GlStateManager.scale(-1.0F, -1.0F, 1.0F);
            this.preRenderCallback((T)var1, var9);
            float var15 = 0.0625F;
            GlStateManager.translate(0.0F, -1.5078125F, 0.0F);
            float var16 = var1.aA + (var1.aB - var1.aA) * var9;
            float var17 = var1.aC - var1.aB * (1.0F - var9);
            if (var1.o_()) {
               var17 *= 3.0F;
            }

            if (var16 > 1.0F) {
               var16 = 1.0F;
            }

            GlStateManager.enableAlpha();
            this.f.setLivingAnimations(var1, var17, var16, var9);
            this.f.setRotationAngles(var17, var16, var23, var12, var22, 0.0625F, var1);
            if (CustomEntityModels.isActive()) {
               this.renderEntity = var1;
               this.recoveredField20 = var17;
               this.recoveredField18 = var16;
               this.recoveredField23 = var23;
               this.recoveredField19 = var12;
               this.recoveredField21 = var22;
               this.recoveredField24 = var15;
               this.recoveredField22 = var9;
            }

            if (this.renderOutlines) {
               boolean var24 = this.setScoreTeamColor((T)var1);
               this.renderModel((T)var1, var17, var16, var23, var12, var22, 0.0625F);
               if (var24) {
                  this.unsetScoreTeamColor();
               }
            } else {
               boolean var18 = this.setDoRenderBrightness((T)var1, var9);
               if (EmissiveTextures.isActive()) {
                  EmissiveTextures.beginRender();
               }

               if (this.renderModelPushMatrix) {
                  GlStateManager.pushMatrix();
               }

               this.renderModel((T)var1, var17, var16, var23, var12, var22, 0.0625F);
               if (this.renderModelPushMatrix) {
                  GlStateManager.popMatrix();
               }

               if (EmissiveTextures.isActive()) {
                  if (EmissiveTextures.hasEmissive()) {
                     this.renderModelPushMatrix = true;
                     EmissiveTextures.beginRenderEmissive();
                     GlStateManager.pushMatrix();
                     this.renderModel((T)var1, var17, var16, var23, var12, var22, var15);
                     GlStateManager.popMatrix();
                     EmissiveTextures.endRenderEmissive();
                  }

                  EmissiveTextures.endRender();
               }

               if (var18) {
                  this.unsetBrightness();
               }

               GlStateManager.depthMask(true);
               if (!(var1 instanceof EntityPlayer) || !((EntityPlayer)var1).isSpectator()) {
                  this.renderLayers((T)var1, var17, var16, var9, var23, var12, var22, 0.0625F);
               }
            }

            if (CustomEntityModels.isActive()) {
               this.renderEntity = null;
            }

            GlStateManager.disableRescaleNormal();
         } catch (Exception var19) {
            logger.error("Couldn't render entity", var19);
         }

         GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
         GlStateManager.enableTexture2D();
         GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
         GlStateManager.enableCull();
         GlStateManager.popMatrix();
         if (!this.renderOutlines) {
            super.doRender((T)var1, var2, var4, var6, var8, var9);
         }

         if (Reflector.RenderLivingEvent_Post_Constructor.exists()) {
            Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Post_Constructor, var1, this, var2, var4, var6);
         }
      }
   }

   public void setRenderOutlines(boolean var1) {
      this.renderOutlines = var1;
   }

   public float getSwingProgress(T var1, float var2) {
      return var1.getSwingProgress(var2);
   }

   public boolean setBrightness(T var1, float var2, boolean var3) {
      float var4 = var1.a_(var2);
      int var5 = this.getColorMultiplier((T)var1, var4, var2);
      boolean var6 = (var5 >> 24 & 0xFF) > 0;
      boolean var7 = var1.au > 0 || var1.ax > 0;
      if (!var6 && !var7) {
         return false;
      } else if (!var6 && !var3) {
         return false;
      } else {
         GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
         GlStateManager.enableTexture2D();
         GL11.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.defaultTexUnit);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PRIMARY_COLOR);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 7681);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.defaultTexUnit);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
         GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
         GlStateManager.enableTexture2D();
         GL11.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, OpenGlHelper.GL_INTERPOLATE);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.GL_CONSTANT);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE2_RGB, OpenGlHelper.GL_CONSTANT);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND2_RGB, 770);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 7681);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.GL_PREVIOUS);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
         ((Buffer)this.brightnessBuffer).position(0);
         float var8 = CheatBreaker.getInstance().getModuleManager().recoveredField1717.recoveredField3730.method_08908() ? 0.73F : 1.0F;
         if (var7) {
            DamageTintModule var9 = CheatBreaker.getInstance().getModuleManager().recoveredField1731;
            if (var9.isEnabled()) {
               var4 = var9.recoveredField3680.method_08908() ? var4 : 1.0F;
               float var10 = (var9.recoveredField3679.method_08901() >> 24 & 0xFF) / 255.0F;
               if (!var9.recoveredField3681.getValue().equals("None")) {
                  float var11 = 1.0F - var1.au / 10.0F;
                  if (var9.recoveredField3681.getValue().equals("Linear In/Out")) {
                     var11 = var11 < 0.5F ? var11 / 0.5F : (1.0F - var11) / 0.5F;
                  } else if (var9.recoveredField3681.getValue().equals("Linear Out")) {
                     var11 = 1.0F - var11;
                  }

                  var10 *= var11;
               }

               this.brightnessBuffer.put((var9.recoveredField3679.method_08901() >> 16 & 0xFF) / 255.0F * var8 * var4);
               this.brightnessBuffer.put((var9.recoveredField3679.method_08901() >> 8 & 0xFF) / 255.0F * var8 * var4);
               this.brightnessBuffer.put((var9.recoveredField3679.method_08901() & 0xFF) / 255.0F * var8 * var4);
               this.brightnessBuffer.put(var10);
            } else {
               this.brightnessBuffer.put(var8);
               this.brightnessBuffer.put(0.0F);
               this.brightnessBuffer.put(0.0F);
               this.brightnessBuffer.put(0.3F);
            }

            if (Config.isShaders()) {
               Shaders.setEntityColor(1.0F, 0.0F, 0.0F, 0.3F);
            }
         } else {
            float var14 = (var5 >> 24 & 0xFF) / 255.0F;
            float var15 = (var5 >> 16 & 0xFF) / 255.0F;
            float var16 = (var5 >> 8 & 0xFF) / 255.0F;
            float var12 = (var5 & 0xFF) / 255.0F;
            this.brightnessBuffer.put(var15);
            this.brightnessBuffer.put(var16);
            this.brightnessBuffer.put(var12);
            this.brightnessBuffer.put(1.0F - var14);
            if (Config.isShaders()) {
               Shaders.setEntityColor(var15, var16, var12, 1.0F - var14);
            }
         }

         ((Buffer)this.brightnessBuffer).flip();
         GL11.glTexEnv(8960, 8705, this.brightnessBuffer);
         GlStateManager.setActiveTexture(OpenGlHelper.GL_TEXTURE2);
         GlStateManager.enableTexture2D();
         GlStateManager.bindTexture(textureBrightness.getGlTextureId());
         GL11.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.GL_PREVIOUS);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.lightmapTexUnit);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 7681);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.GL_PREVIOUS);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
         GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
         return true;
      }
   }

   public <V extends EntityLivingBase, U extends LayerRenderer<V>> boolean b(U var1) {
      return this.h.remove(var1);
   }

   public void rotateCorpse(T var1, float var2, float var3, float var4) {
      GlStateManager.rotate(180.0F - var3, 0.0F, 1.0F, 0.0F);
      if (var1.ax > 0) {
         float var5 = (var1.ax + var4 - 1.0F) / 20.0F * 1.6F;
         var5 = MathHelper.sqrt_float(var5);
         if (var5 > 1.0F) {
            var5 = 1.0F;
         }

         GlStateManager.rotate(var5 * this.getDeathMaxRotation((T)var1), 0.0F, 0.0F, 1.0F);
      } else {
         String var7 = EnumChatFormatting.getTextWithoutFormattingCodes(var1.z_());
         if (var7 != null
            && (var7.equals("Dinnerbone") || var7.equals("Grumm"))
            && (!(var1 instanceof EntityPlayer) || ((EntityPlayer)var1).isWearing(EnumPlayerModelParts.CAPE))) {
            GlStateManager.translate(0.0F, var1.K + 0.1F, 0.0F);
            GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
         }
      }
   }

   public void preRenderCallback(T var1, float var2) {
   }

   public void unsetBrightness() {
      GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
      GlStateManager.enableTexture2D();
      GL11.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.defaultTexUnit);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PRIMARY_COLOR);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 8448);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.defaultTexUnit);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_ALPHA, OpenGlHelper.GL_PRIMARY_COLOR);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_ALPHA, 770);
      GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
      GL11.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, 5890);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 8448);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, 5890);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.setActiveTexture(OpenGlHelper.GL_TEXTURE2);
      GlStateManager.disableTexture2D();
      GlStateManager.bindTexture(0);
      GL11.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, 5890);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 8448);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, 5890);
      GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
      if (Config.isShaders()) {
         Shaders.setEntityColor(0.0F, 0.0F, 0.0F, 0.0F);
      }
   }

   public void y_() {
   }

   public void renderName(T var1, double var2, double var4, double var6) {
      NametagModule var8 = CheatBreaker.getInstance().getModuleManager().recoveredField1713;
      if (!Reflector.RenderLivingEvent_Specials_Pre_Constructor.exists()
         || !Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Specials_Pre_Constructor, var1, this, var2, var4, var6)) {
         if (var1.equals(Minecraft.getMinecraft().thePlayer)
            ? var8.isEnabled() && var8.recoveredField2915.method_08908()
            : this.canRenderName((T)var1)) {
            double var9 = var1.h(this.b.livingPlayer);
            float var11 = var1.isSneaking() ? NAME_TAG_RANGE_SNEAK : NAME_TAG_RANGE;
            if (var9 < var11 * var11 && !CheatBreaker.getInstance().recoveredField1571) {
               String var12 = var1.getDisplayName().getFormattedText();
               NickHiderModule var13 = CheatBreaker.getInstance().getModuleManager().recoveredField1705;
               if (var13.isEnabled() && var13.recoveredField850.method_08908()) {
                  if (!var13.recoveredField849.method_08874().equals(Minecraft.getMinecraft().getSession().getUsername())) {
                     var12 = var12.replaceAll(Minecraft.getMinecraft().getSession().getUsername(), var13.recoveredField849.method_08874());
                  } else {
                     var12 = var12.replaceAll(Minecraft.getMinecraft().getSession().getUsername(), "You");
                  }
               }

               GlStateManager.alphaFunc(516, 0.1F);
               if (var1.isSneaking()) {
                  FontRenderer var14 = this.c();
                  GlStateManager.pushMatrix();
                  GlStateManager.translate((float)var2, (float)var4 + var1.K + 0.5F - (var1.o_() ? var1.K / 2.0F : 0.0F), (float)var6);
                  GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                  GlStateManager.rotate(-this.b.playerViewY, 0.0F, 1.0F, 0.0F);
                  GlStateManager.rotate(this.b.playerViewX, 1.0F, 0.0F, 0.0F);
                  GlStateManager.scale(-0.02666667F, -0.02666667F, 0.02666667F);
                  GlStateManager.translate(0.0F, 9.374999F, 0.0F);
                  GlStateManager.disableLighting();
                  GlStateManager.depthMask(false);
                  GlStateManager.enableBlend();
                  GlStateManager.disableTexture2D();
                  GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                  PlayerNametagStyle logoStyle = this.getNametagLogoStyle(var1, var12, var8);
                  int logoPadding = logoStyle != null ? 7 : 0;
                  int var15 = var14.getStringWidth(var12) / 2 + logoPadding;
                  Tessellator var16 = Tessellator.getInstance();
                  WorldRenderer var17 = var16.getWorldRenderer();
                  var17.begin(7, DefaultVertexFormats.POSITION_COLOR);
                  var17.pos(-var15 - 1, -1.0, 0.0).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                  var17.pos(-var15 - 1, 8.0, 0.0).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                  var17.pos(var15 + 1, 8.0, 0.0).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                  var17.pos(var15 + 1, -1.0, 0.0).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                  var16.draw();
                  GlStateManager.enableTexture2D();
                  GlStateManager.depthMask(true);
                  this.method_08010(var15, 0, logoPadding, var14, var12, logoStyle, var8, logoStyle != null, 32);
                  GlStateManager.enableLighting();
                  GlStateManager.disableBlend();
                  GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                  GlStateManager.popMatrix();
               } else {
                  this.renderOffsetLivingLabel((T)var1, var2, var4 - (var1.o_() ? var1.K / 2.0F : 0.0), var6, var12, 0.02666667F, var9);
               }
            }
         }

         if (Reflector.RenderLivingEvent_Specials_Post_Constructor.exists()) {
            Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Specials_Post_Constructor, var1, this, var2, var4, var6);
         }
      }
   }

   public void renderLivingAt(T var1, double var2, double var4, double var6) {
      GlStateManager.translate((float)var2, (float)var4, (float)var6);
   }

   public float getDeathMaxRotation(T var1) {
      return 90.0F;
   }

   public <V extends EntityLivingBase, U extends LayerRenderer<V>> boolean a(U var1) {
      return ((List)this.h).add(var1);
   }

   public float handleRotationFloat(T var1, float var2) {
      return var1.W + var2;
   }

   public boolean canRenderName(T var1) {
      EntityPlayerSP var2 = Minecraft.getMinecraft().thePlayer;
      if (var1 instanceof EntityPlayer) {
         Team var3 = var1.getTeam();
         Team var4 = var2.getTeam();
         if (var3 != null) {
            Team.EnumVisible var5 = var3.getNameTagVisibility();
            switch (RendererLivingEntity$EnumSwitch.recoveredField72[var5.ordinal()]) {
               case 1:
                  return false;
               case 2:
                  return var4 == null || var3.isSameTeam(var4);
               case 3:
                  return var4 == null || !var3.isSameTeam(var4);
               default:
                  return true;
            }
         }
      }

      return Minecraft.isGuiEnabled() && var1 != this.b.livingPlayer && !var1.f(var2) && var1.l == null;
   }

   public void renderLayers(T var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      for (LayerRenderer var10 : this.h) {
         boolean var11 = this.setBrightness((T)var1, var4, var10.shouldCombineTextures());
         if (EmissiveTextures.isActive()) {
            EmissiveTextures.beginRender();
         }

         if (this.renderLayersPushMatrix) {
            GlStateManager.pushMatrix();
         }

         var10.doRenderLayer(var1, var2, var3, var4, var5, var6, var7, var8);
         if (this.renderLayersPushMatrix) {
            GlStateManager.popMatrix();
         }

         if (EmissiveTextures.isActive()) {
            if (EmissiveTextures.hasEmissive()) {
               this.renderLayersPushMatrix = true;
               EmissiveTextures.beginRenderEmissive();
               GlStateManager.pushMatrix();
               var10.doRenderLayer(var1, var2, var3, var4, var5, var6, var7, var8);
               GlStateManager.popMatrix();
               EmissiveTextures.endRenderEmissive();
            }

            EmissiveTextures.endRender();
         }

         if (var11) {
            this.unsetBrightness();
         }
      }
   }

   public ModelBase getMainModel() {
      return this.f;
   }

   public RendererLivingEntity(RenderManager var1, ModelBase var2, float var3) {
      super(var1);
      this.h = Lists.newArrayList();
      this.renderOutlines = false;
      this.f = var2;
      this.c = var3;
      this.renderModelPushMatrix = this.f instanceof ModelSpider;
   }

   public float interpolateRotation(float var1, float var2, float var3) {
      float var4 = var2 - var1;

      while (var4 < -180.0F) {
         var4 += 360.0F;
      }

      while (var4 >= 180.0F) {
         var4 -= 360.0F;
      }

      return var1 + var3 * var4;
   }

   public void unsetScoreTeamColor() {
      GlStateManager.enableLighting();
      GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
      GlStateManager.enableTexture2D();
      GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
      GlStateManager.enableTexture2D();
      GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
   }

   public boolean setScoreTeamColor(T var1) {
      int var2 = 16777215;
      if (var1 instanceof EntityPlayer) {
         ScorePlayerTeam var3 = (ScorePlayerTeam)var1.getTeam();
         if (var3 != null) {
            String var4 = FontRenderer.getFormatFromString(var3.getColorPrefix());
            if (var4.length() >= 2) {
               var2 = this.c().getColorCode(var4.charAt(1));
            }
         }
      }

      float var6 = (var2 >> 16 & 0xFF) / 255.0F;
      float var7 = (var2 >> 8 & 0xFF) / 255.0F;
      float var5 = (var2 & 0xFF) / 255.0F;
      GlStateManager.disableLighting();
      GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
      GlStateManager.color(var6, var7, var5, 1.0F);
      GlStateManager.disableTexture2D();
      GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
      GlStateManager.disableTexture2D();
      GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
      return true;
   }

   public List<LayerRenderer<T>> getLayerRenderers() {
      return this.h;
   }
}
