package net.minecraft.client.renderer.entity;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.module.type.NametagModule;
import com.cheatbreaker.client.module.type.NickHiderModule;
import com.cheatbreaker.client.module.type.TextureOptionsModule;
import com.cheatbreaker.client.ui.util.RenderUtil;
import java.awt.Color;
import java.util.regex.Pattern;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.src.Config;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.optifine.entity.model.IEntityRenderer;
import net.optifine.shaders.Shaders;
import org.lwjgl.opengl.GL11;
import com.cheatbreaker.client.util.player.PlayerNametagStyle;

public abstract class Render<T extends Entity> implements IEntityRenderer {
   public RenderManager b;
   public float c;
   public Class entityClass;
   public Pattern recoveredField1876;
   public float d = 1.0F;
   public static ResourceLocation shadowTextures = new ResourceLocation("textures/misc/shadow.png");
   public ResourceLocation locationTextureCustom;

   public FontRenderer c() {
      return this.b.getFontRenderer();
   }

   public void renderEntityOnFire(Entity var1, double var2, double var4, double var6, float var8) {
      GlStateManager.disableLighting();
      TextureMap var9 = Minecraft.getMinecraft().getTextureMapBlocks();
      TextureAtlasSprite var10 = var9.getAtlasSprite("minecraft:blocks/fire_layer_0");
      TextureAtlasSprite var11 = var9.getAtlasSprite("minecraft:blocks/fire_layer_1");
      GlStateManager.pushMatrix();
      GlStateManager.translate((float)var2, (float)var4, (float)var6);
      float var12 = var1.J * 1.4F;
      GlStateManager.scale(var12, var12, var12);
      Tessellator var13 = Tessellator.getInstance();
      WorldRenderer var14 = var13.getWorldRenderer();
      float var15 = 0.5F;
      float var16 = 0.0F;
      float var17 = var1.K / var12;
      float var18 = (float)(var1.t - var1.getEntityBoundingBox().b);
      GlStateManager.rotate(-this.b.playerViewY, 0.0F, 1.0F, 0.0F);
      GlStateManager.translate(0.0F, 0.0F, -0.3F + (int)var17 * 0.02F);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      float var19 = 0.0F;
      int var20 = 0;
      boolean var21 = Config.isMultiTexture();
      if (var21) {
         var14.setBlockLayer(EnumWorldBlockLayer.SOLID);
      }

      var14.begin(7, DefaultVertexFormats.POSITION_TEX);

      while (var17 > 0.0F) {
         TextureAtlasSprite var22 = var20 % 2 == 0 ? var10 : var11;
         var14.setSprite(var22);
         this.a(TextureMap.locationBlocksTexture);
         float var23 = var22.getMinU();
         float var24 = var22.getMinV();
         float var25 = var22.getMaxU();
         float var26 = var22.getMaxV();
         if (var20 / 2 % 2 == 0) {
            float var27 = var25;
            var25 = var23;
            var23 = var27;
         }

         var14.pos(var15 - var16, 0.0F - var18, var19).tex(var25, var26).endVertex();
         var14.pos(-var15 - var16, 0.0F - var18, var19).tex(var23, var26).endVertex();
         var14.pos(-var15 - var16, 1.4F - var18, var19).tex(var23, var24).endVertex();
         var14.pos(var15 - var16, 1.4F - var18, var19).tex(var25, var24).endVertex();
         var17 -= 0.45F;
         var18 -= 0.45F;
         var15 *= 0.9F;
         var19 += 0.03F;
         var20++;
      }

      var13.draw();
      if (var21) {
         var14.setBlockLayer(null);
         GlStateManager.bindCurrentTexture();
      }

      GlStateManager.popMatrix();
      GlStateManager.enableLighting();
   }

   @Override
   public ResourceLocation getLocationTextureCustom() {
      return this.locationTextureCustom;
   }

   public void renderShadowBlock(
      Block var1, double var2, double var4, double var6, BlockPos var8, float var9, float var10, double var11, double var13, double var15
   ) {
      if (var1.isFullCube()) {
         Tessellator var17 = Tessellator.getInstance();
         WorldRenderer var18 = var17.getWorldRenderer();
         double var19 = (var9 - (var4 - (var8.getY() + var13)) / 2.0) * 0.5 * this.getWorldFromRenderManager().o(var8);
         if (var19 >= 0.0) {
            if (var19 > 1.0) {
               var19 = 1.0;
            }

            double var21 = var8.getX() + var1.getBlockBoundsMinX() + var11;
            double var23 = var8.getX() + var1.getBlockBoundsMaxX() + var11;
            double var25 = var8.getY() + var1.getBlockBoundsMinY() + var13 + 0.015625;
            double var27 = var8.getZ() + var1.getBlockBoundsMinZ() + var15;
            double var29 = var8.getZ() + var1.getBlockBoundsMaxZ() + var15;
            float var31 = (float)((var2 - var21) / 2.0 / var10 + 0.5);
            float var32 = (float)((var2 - var23) / 2.0 / var10 + 0.5);
            float var33 = (float)((var6 - var27) / 2.0 / var10 + 0.5);
            float var34 = (float)((var6 - var29) / 2.0 / var10 + 0.5);
            var18.pos(var21, var25, var27).tex(var31, var33).color(1.0F, 1.0F, 1.0F, (float)var19).endVertex();
            var18.pos(var21, var25, var29).tex(var31, var34).color(1.0F, 1.0F, 1.0F, (float)var19).endVertex();
            var18.pos(var23, var25, var29).tex(var32, var34).color(1.0F, 1.0F, 1.0F, (float)var19).endVertex();
            var18.pos(var23, var25, var27).tex(var32, var33).color(1.0F, 1.0F, 1.0F, (float)var19).endVertex();
         }
      }
   }

   @Override
   public Class getEntityClass() {
      return this.entityClass;
   }

   public abstract ResourceLocation getEntityTexture(T var1);

   public void method_08016(Entity var1, double var2, double var4, double var6, float var8, float var9) {
      if (!Config.isShaders() || !Shaders.recoveredField2178) {
         GlStateManager.enableBlend();
         GlStateManager.blendFunc(770, 771);
         this.b.renderEngine.bindTexture(shadowTextures);
         World var10 = this.getWorldFromRenderManager();
         GlStateManager.depthMask(false);
         float var11 = this.c;
         if (var1 instanceof EntityLiving) {
            EntityLiving var12 = (EntityLiving)var1;
            var11 *= var12.getRenderSizeModifier();
            if (var12.o_()) {
               var11 *= 0.5F;
            }
         }

         double var35 = var1.P + (var1.s - var1.P) * var9;
         double var14 = var1.Q + (var1.t - var1.Q) * var9;
         double var16 = var1.R + (var1.u - var1.R) * var9;
         int var18 = MathHelper.floor_double(var35 - var11);
         int var19 = MathHelper.floor_double(var35 + var11);
         int var20 = MathHelper.floor_double(var14 - var11);
         int var21 = MathHelper.floor_double(var14);
         int var22 = MathHelper.floor_double(var16 - var11);
         int var23 = MathHelper.floor_double(var16 + var11);
         double var24 = var2 - var35;
         double var26 = var4 - var14;
         double var28 = var6 - var16;
         Tessellator var30 = Tessellator.getInstance();
         WorldRenderer var31 = var30.getWorldRenderer();
         var31.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);

         for (BlockPos var33 : BlockPos.getAllInBoxMutable(new BlockPos(var18, var20, var22), new BlockPos(var19, var21, var23))) {
            Block var34 = var10.getBlockState(var33.down()).getBlock();
            if (var34.getRenderType() != -1 && var10.getLightFromNeighbors(var33) > 3) {
               this.renderShadowBlock(var34, var2, var4, var6, var33, var8, var11, var24, var26, var28);
            }
         }

         var30.draw();
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.disableBlend();
         GlStateManager.depthMask(true);
      }
   }

   public void method_08026(Entity var1, double var2, double var4, double var6, float var8, float var9) {
      if (this.b.options != null) {
         if (this.b.options.entityShadows && this.c > 0.0F && !var1.isInvisible() && this.b.method_21284()) {
            double var10 = this.b.getDistanceToCamera(var1.s, var1.t, var1.u);
            float var12 = (float)((1.0 - var10 / 256.0) * this.d);
            if (var12 > 0.0F) {
               this.method_08016(var1, var2, var4, var6, var12, var9);
            }
         }

         TextureOptionsModule var13 = CheatBreaker.getInstance().getModuleManager().recoveredField1727;
         boolean var11 = var13.isEnabled();
         boolean var14 = var11 ? (Boolean)var13.recoveredField1514.getValue() : true;
         if (var1.method_10440() && var14 && (!(var1 instanceof EntityPlayer) || !((EntityPlayer)var1).isSpectator())) {
            this.renderEntityOnFire(var1, var2, var4, var6, var9);
         }
      }
   }

   public World getWorldFromRenderManager() {
      return this.b.worldObj;
   }

   public void renderMultipass(T var1, double var2, double var4, double var6, float var8, float var9) {
   }

   public String method_08022(String var1) {
      return var1 == null ? null : this.recoveredField1876.matcher(var1).replaceAll("§r");
   }

   public void doRender(T var1, double var2, double var4, double var6, float var8, float var9) {
      this.renderName((T)var1, var2, var4, var6);
   }

   public static void setModelBipedMain(RenderBiped var0, ModelBiped var1) {
      var0.a = var1;
   }

   public boolean isMultipass() {
      return false;
   }

   public boolean canRenderName(T var1) {
      return var1.aO() && var1.u_();
   }

   @Override
   public void setEntityClass(Class var1) {
      this.entityClass = var1;
   }

   public static void renderOffsetAABB(AxisAlignedBB var0, double var1, double var3, double var5) {
      GlStateManager.disableTexture2D();
      Tessellator var7 = Tessellator.getInstance();
      WorldRenderer var8 = var7.getWorldRenderer();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      var8.setTranslation(var1, var3, var5);
      var8.begin(7, DefaultVertexFormats.POSITION_NORMAL);
      var8.pos(var0.a, var0.e, var0.c).normal(0.0F, 0.0F, -1.0F).endVertex();
      var8.pos(var0.d, var0.e, var0.c).normal(0.0F, 0.0F, -1.0F).endVertex();
      var8.pos(var0.d, var0.b, var0.c).normal(0.0F, 0.0F, -1.0F).endVertex();
      var8.pos(var0.a, var0.b, var0.c).normal(0.0F, 0.0F, -1.0F).endVertex();
      var8.pos(var0.a, var0.b, var0.f).normal(0.0F, 0.0F, 1.0F).endVertex();
      var8.pos(var0.d, var0.b, var0.f).normal(0.0F, 0.0F, 1.0F).endVertex();
      var8.pos(var0.d, var0.e, var0.f).normal(0.0F, 0.0F, 1.0F).endVertex();
      var8.pos(var0.a, var0.e, var0.f).normal(0.0F, 0.0F, 1.0F).endVertex();
      var8.pos(var0.a, var0.b, var0.c).normal(0.0F, -1.0F, 0.0F).endVertex();
      var8.pos(var0.d, var0.b, var0.c).normal(0.0F, -1.0F, 0.0F).endVertex();
      var8.pos(var0.d, var0.b, var0.f).normal(0.0F, -1.0F, 0.0F).endVertex();
      var8.pos(var0.a, var0.b, var0.f).normal(0.0F, -1.0F, 0.0F).endVertex();
      var8.pos(var0.a, var0.e, var0.f).normal(0.0F, 1.0F, 0.0F).endVertex();
      var8.pos(var0.d, var0.e, var0.f).normal(0.0F, 1.0F, 0.0F).endVertex();
      var8.pos(var0.d, var0.e, var0.c).normal(0.0F, 1.0F, 0.0F).endVertex();
      var8.pos(var0.a, var0.e, var0.c).normal(0.0F, 1.0F, 0.0F).endVertex();
      var8.pos(var0.a, var0.b, var0.f).normal(-1.0F, 0.0F, 0.0F).endVertex();
      var8.pos(var0.a, var0.e, var0.f).normal(-1.0F, 0.0F, 0.0F).endVertex();
      var8.pos(var0.a, var0.e, var0.c).normal(-1.0F, 0.0F, 0.0F).endVertex();
      var8.pos(var0.a, var0.b, var0.c).normal(-1.0F, 0.0F, 0.0F).endVertex();
      var8.pos(var0.d, var0.b, var0.c).normal(1.0F, 0.0F, 0.0F).endVertex();
      var8.pos(var0.d, var0.e, var0.c).normal(1.0F, 0.0F, 0.0F).endVertex();
      var8.pos(var0.d, var0.e, var0.f).normal(1.0F, 0.0F, 0.0F).endVertex();
      var8.pos(var0.d, var0.b, var0.f).normal(1.0F, 0.0F, 0.0F).endVertex();
      var7.draw();
      var8.setTranslation(0.0, 0.0, 0.0);
      GlStateManager.enableTexture2D();
   }

   public boolean shouldRender(T var1, ICamera var2, double var3, double var5, double var7) {
      AxisAlignedBB var9 = var1.getEntityBoundingBox();
      if (var9.hasNaN() || var9.getAverageEdgeLength() == 0.0) {
         var9 = new AxisAlignedBB(var1.s - 2.0, var1.t - 2.0, var1.u - 2.0, var1.s + 2.0, var1.t + 2.0, var1.u + 2.0);
      }

      return var1.isInRangeToRender3d(var3, var5, var7) && (var1.ah || var2.isBoundingBoxInFrustum(var9));
   }

   public void renderName(T var1, double var2, double var4, double var6) {
      if (this.canRenderName((T)var1)) {
         this.renderLivingLabel((T)var1, var1.getDisplayName().getFormattedText(), var2, var4, var6, 64);
      }
   }

   @Override
   public void setLocationTextureCustom(ResourceLocation var1) {
      this.locationTextureCustom = var1;
   }

   public void a(ResourceLocation var1) {
      this.b.renderEngine.bindTexture(var1);
   }

   public Render(RenderManager var1) {
      this.entityClass = null;
      this.locationTextureCustom = null;
      this.recoveredField1876 = Pattern.compile("(?i)§[0-689A-E]");
      this.b = var1;
   }

   public void renderLivingLabel(T var1, String var2, double var3, double var5, double var7, int var9) {
      NametagModule var10 = CheatBreaker.getInstance().getModuleManager().recoveredField1713;
      boolean var12 = var10.isEnabled();
      PlayerNametagStyle var14 = this.getNametagLogoStyle(var1, var2, var10);
      boolean var13 = var14 != null;
      if (var12 && var10.recoveredField2915.method_08908() || !var1.equals(Minecraft.getMinecraft().thePlayer)) {
         double var15 = var1.h(this.b.livingPlayer);
         if (var15 <= var9 * var9) {
            FontRenderer var17 = this.c();
            float var18 = 1.6F;
            float var19 = 0.016666668F * var18;
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)var3 + 0.0F, (float)var5 + var1.K + 0.5F, (float)var7);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-this.b.playerViewY, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(this.b.playerViewX, 1.0F, 0.0F, 0.0F);
            GlStateManager.scale(-var19, -var19, var19);
            GlStateManager.disableLighting();
            GlStateManager.depthMask(false);
            GlStateManager.disableDepth();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            Tessellator var20 = Tessellator.getInstance();
            WorldRenderer var21 = var20.getWorldRenderer();
            boolean var22 = (Boolean)var10.recoveredField2917.getValue() && var12;
            String var23 = var22 ? this.method_08022(var2) : var2;
            NickHiderModule var24 = CheatBreaker.getInstance().getModuleManager().recoveredField1705;
            if (var24.isEnabled() && var24.recoveredField850.method_08908()) {
               if (!var24.recoveredField849.method_08874().equals(Minecraft.getMinecraft().getSession().getUsername())) {
                  var23 = var23.replaceAll(Minecraft.getMinecraft().getSession().getUsername(), var24.recoveredField849.method_08874());
               } else {
                  var23 = var23.replaceAll(Minecraft.getMinecraft().getSession().getUsername(), "You");
               }
            }

            byte var25 = 0;
            if (var2.equals("deadmau5")) {
               var25 = -10;
            }

            int var26 = var17.getStringWidth(var23) / 2;
            int var27 = var13 ? 7 : 0;
            var26 += var27;
            GlStateManager.disableTexture2D();
            if (!var12 || (Boolean)var10.recoveredField2920.getValue()) {
               var21.begin(7, DefaultVertexFormats.POSITION_COLOR);
               if (var12) {
                  float var28 = (var10.recoveredField2911.method_08901() >> 24 & 0xFF) / 255.0F;
                  float var29 = (var10.recoveredField2911.method_08901() >> 16 & 0xFF) / 255.0F;
                  float var30 = (var10.recoveredField2911.method_08901() >> 8 & 0xFF) / 255.0F;
                  float var31 = (var10.recoveredField2911.method_08901() & 0xFF) / 255.0F;
                  var21.pos(-var26 - 1, -1 + var25, 0.0).color(var29, var30, var31, var28).endVertex();
                  var21.pos(-var26 - 1, 8 + var25, 0.0).color(var29, var30, var31, var28).endVertex();
                  var21.pos(var26 + 1, 8 + var25, 0.0).color(var29, var30, var31, var28).endVertex();
                  var21.pos(var26 + 1, -1 + var25, 0.0).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
               } else {
                  var21.pos(-var26 - 1, -1 + var25, 0.0).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                  var21.pos(-var26 - 1, 8 + var25, 0.0).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                  var21.pos(var26 + 1, 8 + var25, 0.0).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                  var21.pos(var26 + 1, -1 + var25, 0.0).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
               }

               var20.draw();
            }

            GlStateManager.enableTexture2D();
            this.method_08010(var26, var25, var27, var17, var23, var14, var10, var13, 32);
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            this.method_08010(var26, var25, var27, var17, var23, var14, var10, var13, 255);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
         }
      }
   }

   protected PlayerNametagStyle getNametagLogoStyle(T entity, String label, NametagModule module) {
      if (!(entity instanceof EntityPlayer)) return null;
      Minecraft minecraft = Minecraft.getMinecraft();
      boolean localPlayer = entity == minecraft.thePlayer;
      NickHiderModule nick = CheatBreaker.getInstance().getModuleManager().recoveredField1705;
      String alias = localPlayer && nick.isEnabled() && nick.recoveredField850.method_08908()
         ? nick.recoveredField849.method_08874() : null;
      if (alias != null && alias.equals(minecraft.getSession().getUsername())) alias = "You";
      return module.getLogoStyle(entity.aK(), entity.z_(), label, localPlayer, alias,
         CheatBreaker.getInstance().method_19771());
   }

   public void method_08010(
      int var1, int var2, int var3, FontRenderer var4, String var5, PlayerNametagStyle var6, NametagModule var7, boolean var8, int var9
   ) {
      var4.drawString(
         var5,
         -(var4.getStringWidth(var5) / 2 - var3),
         var2,
         var7.recoveredField2917.method_08908()
            ? new Color(
                  var7.recoveredField2914.method_08901() >> 16 & 0xFF,
                  var7.recoveredField2914.method_08901() >> 8 & 0xFF,
                  var7.recoveredField2914.method_08901() & 0xFF,
                  Math.min(var9, var7.recoveredField2914.method_08901() >> 24 & 0xFF)
               )
               .getRGB()
            : new Color(255, 255, 255, var9).getRGB(),
         var7.isEnabled() && var7.recoveredField2918.method_08908(),
         1.0F
      );
      if (var6 != null) {
         if (var8) {
            if (var7.recoveredField2918.method_08908()) {
               GlStateManager.color(0.25F, 0.25F, 0.25F, var9 / 255.0F);
               // One label unit behind the face prevents coplanar depth fighting during view bobbing.
               RenderUtil.drawNametagIcon(PlayerNametagStyle.LOGO_TEXTURE, -var1 + 1.0F, 1.0F, 13.0F, 7.0F, 1.0F);
            }

            GlStateManager.color(1.0F, 1.0F, 1.0F, var9 / 255.0F);
            RenderUtil.drawNametagIcon(PlayerNametagStyle.LOGO_TEXTURE, -var1, 0.0F, 13.0F, 7.0F, 0.0F);

            if (var7.recoveredField2921.method_08908() && !var6.method_26422().equalsIgnoreCase("")) {
               if (var7.recoveredField2918.method_08908()) {
                  GlStateManager.color(0.25F, 0.25F, 0.25F, var9 / 255.0F);
                  RenderUtil.drawNametagIcon(
                     new ResourceLocation("client/icons/nametag/subicons/" + var6.method_26422() + ".png"),
                     -(var1 * 2) / 2.0F + 10.0F + 0.33333334F,
                     -1.6666666F,
                     4.3333335F,
                     4.3333335F,
                     1.0F
                  );
               }

               GlStateManager.color(1.0F, 1.0F, 1.0F, var9 / 255.0F);
               RenderUtil.drawNametagIcon(
                  new ResourceLocation("client/icons/nametag/subicons/" + var6.method_26422() + ".png"),
                  -(var1 * 2) / 2.0F + 10.0F,
                  -2.0F,
                  4.3333335F,
                  4.3333335F,
                  0.0F
               );
            }
         }
      }
   }

   public boolean bindEntityTexture(T var1) {
      ResourceLocation var2 = this.getEntityTexture((T)var1);
      if (this.locationTextureCustom != null) {
         var2 = this.locationTextureCustom;
      }

      if (var2 == null) {
         return false;
      } else {
         this.a(var2);
         return true;
      }
   }

   public void renderOffsetLivingLabel(T var1, double var2, double var4, double var6, String var8, float var9, double var10) {
      this.renderLivingLabel((T)var1, var8, var2, var4, var6, 64);
   }

   public RenderManager getRenderManager() {
      return this.b;
   }
}
