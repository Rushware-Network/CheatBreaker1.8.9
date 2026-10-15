package net.minecraft.client.renderer.entity;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.module.type.HitboxSettings;
import com.cheatbreaker.client.module.type.HitboxesModule;
import com.cheatbreaker.client.ui.util.RenderUtil;
import com.google.common.collect.Maps;
import java.awt.Color;
import java.util.Collections;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelChicken;
import net.minecraft.client.model.ModelCow;
import net.minecraft.client.model.ModelHorse;
import net.minecraft.client.model.ModelOcelot;
import net.minecraft.client.model.ModelPig;
import net.minecraft.client.model.ModelRabbit;
import net.minecraft.client.model.ModelSheep2;
import net.minecraft.client.model.ModelSlime;
import net.minecraft.client.model.ModelSquid;
import net.minecraft.client.model.ModelWolf;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.RenderEnderCrystal;
import net.minecraft.client.renderer.tileentity.RenderItemFrame;
import net.minecraft.client.renderer.tileentity.RenderWitherSkull;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityMinecartMobSpawner;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ReportedException;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.optifine.entity.model.CustomEntityModels;
import net.optifine.player.PlayerItemsLayer;
import net.optifine.reflect.Reflector;
import net.optifine.shaders.Shaders;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.entity.RenderFish;
import net.minecraft.client.renderer.entity.RenderFallingBlock;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.renderer.entity.RenderTNTPrimed;
import com.cheatbreaker.client.util.render.FreelookController;
import net.minecraft.client.renderer.entity.RenderLightningBolt;

public class RenderManager {
   public static RenderManager recoveredField998;
   public double viewerPosZ;
   public boolean recoveredField999;
   public double viewerPosX;
   public TextureManager renderEngine;
   public FontRenderer textRenderer;
   public double viewerPosY;
   public boolean recoveredField1000;
   public Map<String, RenderPlayer> skinMap;
   public float playerViewX;
   public Entity pointedEntity;
   public Entity livingPlayer;
   public Render renderRender;
   public double renderPosY;
   public float playerViewY;
   public double renderPosX;
   public Map<Class, Render> entityRenderMap = Maps.newHashMap();
   public boolean recoveredField1001;
   public RenderPlayer playerRenderer;
   public double renderPosZ;
   public GameSettings options;
   public World worldObj;

   public static RenderManager method_21288() {
      return recoveredField998;
   }

   public void renderDebugBoundingBox(Entity var1, double var2, double var4, double var6, float var8, float var9) {
      if (var1 instanceof EntityArrow) {
         Render renderer = this.getEntityRenderObject(var1);
         if (renderer instanceof RenderArrow && ((RenderArrow)renderer).method_01981((EntityArrow)var1)) {
            return;
         }
      }
      HitboxesModule var10 = CheatBreaker.getInstance().getModuleManager().recoveredField1703;
      boolean var11 = var10.recoveredField331.recoveredField3276.method_08908();
      boolean var12 = var1 == Minecraft.getMinecraft().thePlayer;
      HitboxSettings var13 = var10.method_09734(var1);
      Color var14 = RenderUtil.method_22060((var12 ? var13.recoveredField3278 : var13.recoveredField3269).method_08901());
      Color var15 = RenderUtil.method_22060(var13.recoveredField3270.method_08901());
      Color var16 = RenderUtil.method_22060(var13.recoveredField3273.method_08901());
      if (var13.recoveredField3272.method_08908()) {
         if (var11 || !var12) {
            if (!Shaders.isShadowPass) {
               GlStateManager.depthMask(false);
               GlStateManager.disableTexture2D();
               GlStateManager.disableLighting();
               GlStateManager.disableCull();
               GlStateManager.disableBlend();
               float var17 = var1.J / 2.0F;
               AxisAlignedBB var18 = var1.getEntityBoundingBox();
               GlStateManager.enableBlend();
               GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
               if (var13.recoveredField3279.method_08908()) {
                  if (var13.recoveredField3268.method_08908()) {
                     GL11.glPushAttrib(8192);
                     GL11.glLineStipple(var13.recoveredField3277.method_08912(), (short)-21846);
                     GL11.glEnable(2852);
                     GL11.glBegin(1);
                     GL11.glEnd();
                  }

                  AxisAlignedBB var19 = new AxisAlignedBB(
                     var18.a - var1.s + var2,
                     var18.b - var1.t + var4,
                     var18.c - var1.u + var6,
                     var18.d - var1.s + var2,
                     var18.e - var1.t + var4,
                     var18.f - var1.u + var6
                  );
                  RenderGlobal.drawOutlinedBoundingBox(var19, var14.getRed(), var14.getGreen(), var14.getBlue(), var14.getAlpha());
                  if (var13.recoveredField3268.method_08908()) {
                     GL11.glPopAttrib();
                  }
               }

               if (var13.recoveredField3274.method_08908()) {
                  AxisAlignedBB var22 = new AxisAlignedBB(
                     var2 - var17, var4 + var1.getEyeHeight() - 0.01F, var6 - var17, var2 + var17, var4 + var1.getEyeHeight() + 0.01F, var6 + var17
                  );
                  RenderGlobal.drawOutlinedBoundingBox(var22, var15.getRed(), var15.getGreen(), var15.getBlue(), var15.getAlpha());
               }

               if (!var13.recoveredField3271.method_08908()) {
                  GlStateManager.enableTexture2D();
                  GlStateManager.enableLighting();
                  GlStateManager.enableCull();
                  GlStateManager.disableBlend();
                  GlStateManager.depthMask(true);
                  return;
               }

               Tessellator var23 = Tessellator.getInstance();
               WorldRenderer var20 = var23.getWorldRenderer();
               Vec3 var21 = var1.getLook(var9);
               var20.begin(3, DefaultVertexFormats.POSITION_COLOR);
               var20.pos(var2, var4 + var1.getEyeHeight(), var6)
                  .color(var16.getRed(), var16.getGreen(), var16.getBlue(), var16.getAlpha()).endVertex();
               var20.pos(var2 + var21.xCoord * 2.0, var4 + var1.getEyeHeight() + var21.yCoord * 2.0, var6 + var21.zCoord * 2.0)
                  .color(var16.getRed(), var16.getGreen(), var16.getBlue(), var16.getAlpha())
                  .endVertex();
               var23.draw();
               GlStateManager.enableTexture2D();
               GlStateManager.enableLighting();
               GlStateManager.enableCull();
               GlStateManager.disableBlend();
               GlStateManager.depthMask(true);
            }
         }
      }
   }

   public double method_21304() {
      return this.renderPosX;
   }

   public void method_21301(boolean var1) {
      this.recoveredField1001 = var1;
   }

   public double method_21286() {
      return this.renderPosY;
   }

   public FontRenderer getFontRenderer() {
      return this.textRenderer;
   }

   public void setRenderShadow(boolean var1) {
      this.recoveredField1000 = var1;
   }

   public <T extends Entity> Render<T> getEntityClassRenderObject(Class<? extends Entity> var1) {
      Render var2 = this.entityRenderMap.get(var1);
      if (var2 == null && var1 != Entity.class) {
         var2 = this.getEntityClassRenderObject((Class)var1.getSuperclass());
         this.entityRenderMap.put(var1, var2);
      }

      return var2;
   }

   public void cacheActiveRenderInfo(World var1, FontRenderer var2, Entity var3, Entity var4, GameSettings var5, float var6) {
      this.worldObj = var1;
      this.options = var5;
      this.livingPlayer = var3;
      this.pointedEntity = var4;
      this.textRenderer = var2;
      if (var3 instanceof EntityLivingBase && ((EntityLivingBase)var3).bJ()) {
         IBlockState var11 = var1.getBlockState(new BlockPos(var3));
         Block var8 = var11.getBlock();
         if (Reflector.callBoolean(var8, Reflector.ForgeBlock_isBed, var11, var1, new BlockPos(var3), var3)) {
            EnumFacing var9 = (EnumFacing)Reflector.call(var8, Reflector.ForgeBlock_getBedDirection, var11, var1, new BlockPos(var3));
            int var10 = var9.getHorizontalIndex();
            this.playerViewY = var10 * 90 + 180;
            this.playerViewX = 0.0F;
         } else if (var8 == Blocks.bed) {
            int var12 = var11.getValue(BlockBed.O).getHorizontalIndex();
            this.playerViewY = var12 * 90 + 180;
            this.playerViewX = 0.0F;
         }
      } else {
         FreelookController var7 = CheatBreaker.getInstance().getModuleManager().recoveredField1728;
         if (var7.recoveredField3503) {
            this.playerViewY = var7.recoveredField3505 + (var7.recoveredField3508 - var7.recoveredField3505) * var6;
            this.playerViewX = var7.recoveredField3506 + (var7.recoveredField3509 - var7.recoveredField3506) * var6;
         } else {
            this.playerViewY = var3.A + (var3.y - var3.A) * var6;
            this.playerViewX = var3.B + (var3.z - var3.B) * var6;
         }
      }

      if (var5.thirdPersonView == 2) {
         this.playerViewY += 180.0F;
         this.playerViewX = -this.playerViewX;
      }

      this.viewerPosX = var3.P + (var3.s - var3.P) * var6;
      this.viewerPosY = var3.Q + (var3.t - var3.Q) * var6;
      this.viewerPosZ = var3.R + (var3.u - var3.R) * var6;
   }

   public void setRenderPosition(double var1, double var3, double var5) {
      this.renderPosX = var1;
      this.renderPosY = var3;
      this.renderPosZ = var5;
   }

   public void set(World var1) {
      this.worldObj = var1;
   }

   public boolean method_21283() {
      return this.recoveredField999;
   }

   public boolean renderEntityWithPosYaw(Entity var1, double var2, double var4, double var6, float var8, float var9) {
      return this.doRenderEntity(var1, var2, var4, var6, var8, var9, false);
   }

   public RenderManager(TextureManager var1, RenderItem var2) {
      this.skinMap = Maps.newHashMap();
      this.recoveredField1001 = false;
      this.recoveredField1000 = true;
      this.recoveredField999 = false;
      this.renderRender = null;
      recoveredField998 = this;
      this.renderEngine = var1;
      this.entityRenderMap.put(EntityCaveSpider.class, new RenderCaveSpider(this));
      this.entityRenderMap.put(EntitySpider.class, new RenderSpider(this));
      this.entityRenderMap.put(EntityPig.class, new RenderPig(this, new ModelPig(), 0.7F));
      this.entityRenderMap.put(EntitySheep.class, new RenderSheep(this, new ModelSheep2(), 0.7F));
      this.entityRenderMap.put(EntityCow.class, new RenderCow(this, new ModelCow(), 0.7F));
      this.entityRenderMap.put(EntityMooshroom.class, new RenderMooshroom(this, new ModelCow(), 0.7F));
      this.entityRenderMap.put(EntityWolf.class, new RenderWolf(this, new ModelWolf(), 0.5F));
      this.entityRenderMap.put(EntityChicken.class, new RenderChicken(this, new ModelChicken(), 0.3F));
      this.entityRenderMap.put(EntityOcelot.class, new RenderOcelot(this, new ModelOcelot(), 0.4F));
      this.entityRenderMap.put(EntityRabbit.class, new RenderRabbit(this, new ModelRabbit(), 0.3F));
      this.entityRenderMap.put(EntitySilverfish.class, new RenderSilverfish(this));
      this.entityRenderMap.put(EntityEndermite.class, new RenderEndermite(this));
      this.entityRenderMap.put(EntityCreeper.class, new RenderCreeper(this));
      this.entityRenderMap.put(EntityEnderman.class, new RenderEnderman(this));
      this.entityRenderMap.put(EntitySnowman.class, new RenderSnowMan(this));
      this.entityRenderMap.put(EntitySkeleton.class, new RenderSkeleton(this));
      this.entityRenderMap.put(EntityWitch.class, new RenderWitch(this));
      this.entityRenderMap.put(EntityBlaze.class, new RenderBlaze(this));
      this.entityRenderMap.put(EntityPigZombie.class, new RenderPigZombie(this));
      this.entityRenderMap.put(EntityZombie.class, new RenderZombie(this));
      this.entityRenderMap.put(EntitySlime.class, new RenderSlime(this, new ModelSlime(16), 0.25F));
      this.entityRenderMap.put(EntityMagmaCube.class, new RenderMagmaCube(this));
      this.entityRenderMap.put(EntityGiantZombie.class, new RenderGiantZombie(this, new ModelZombie(), 0.5F, 6.0F));
      this.entityRenderMap.put(EntityGhast.class, new RenderGhast(this));
      this.entityRenderMap.put(EntitySquid.class, new RenderSquid(this, new ModelSquid(), 0.7F));
      this.entityRenderMap.put(EntityVillager.class, new RenderVillager(this));
      this.entityRenderMap.put(EntityIronGolem.class, new RenderIronGolem(this));
      this.entityRenderMap.put(EntityBat.class, new RenderBat(this));
      this.entityRenderMap.put(EntityGuardian.class, new RenderGuardian(this));
      this.entityRenderMap.put(EntityDragon.class, new RenderDragon(this));
      this.entityRenderMap.put(EntityEnderCrystal.class, new RenderEnderCrystal(this));
      this.entityRenderMap.put(EntityWither.class, new RenderWither(this));
      this.entityRenderMap.put(Entity.class, new RenderEntity(this));
      this.entityRenderMap.put(EntityPainting.class, new RenderPainting(this));
      this.entityRenderMap.put(EntityItemFrame.class, new RenderItemFrame(this, var2));
      this.entityRenderMap.put(EntityLeashKnot.class, new RenderLeashKnot(this));
      this.entityRenderMap.put(EntityArrow.class, new RenderArrow(this));
      this.entityRenderMap.put(EntitySnowball.class, new RenderSnowball(this, Items.snowball, var2));
      this.entityRenderMap.put(EntityEnderPearl.class, new RenderSnowball(this, Items.ender_pearl, var2));
      this.entityRenderMap.put(EntityEnderEye.class, new RenderSnowball(this, Items.ender_eye, var2));
      this.entityRenderMap.put(EntityEgg.class, new RenderSnowball(this, Items.egg, var2));
      this.entityRenderMap.put(EntityPotion.class, new RenderPotion(this, var2));
      this.entityRenderMap.put(EntityExpBottle.class, new RenderSnowball(this, Items.experience_bottle, var2));
      this.entityRenderMap.put(EntityFireworkRocket.class, new RenderSnowball(this, Items.fireworks, var2));
      this.entityRenderMap.put(EntityLargeFireball.class, new RenderFireball(this, 2.0F));
      this.entityRenderMap.put(EntitySmallFireball.class, new RenderFireball(this, 0.5F));
      this.entityRenderMap.put(EntityWitherSkull.class, new RenderWitherSkull(this));
      this.entityRenderMap.put(EntityItem.class, new RenderEntityItem(this, var2));
      this.entityRenderMap.put(EntityXPOrb.class, new RenderXPOrb(this));
      this.entityRenderMap.put(EntityTNTPrimed.class, new RenderTNTPrimed(this));
      this.entityRenderMap.put(EntityFallingBlock.class, new RenderFallingBlock(this));
      this.entityRenderMap.put(EntityArmorStand.class, new ArmorStandRenderer(this));
      this.entityRenderMap.put(EntityMinecartTNT.class, new RenderTntMinecart(this));
      this.entityRenderMap.put(EntityMinecartMobSpawner.class, new RenderMinecartMobSpawner(this));
      this.entityRenderMap.put(EntityMinecart.class, new RenderMinecart(this));
      this.entityRenderMap.put(EntityBoat.class, new RenderBoat(this));
      this.entityRenderMap.put(EntityFishHook.class, new RenderFish(this));
      this.entityRenderMap.put(EntityHorse.class, new RenderHorse(this, new ModelHorse(), 0.75F));
      this.entityRenderMap.put(EntityLightningBolt.class, new RenderLightningBolt(this));
      this.playerRenderer = new RenderPlayer(this);
      this.skinMap.put("default", this.playerRenderer);
      this.skinMap.put("slim", new RenderPlayer(this, true));
      PlayerItemsLayer.register(this.skinMap);
      if (Reflector.RenderingRegistry_loadEntityRenderers.exists()) {
         Reflector.call(Reflector.RenderingRegistry_loadEntityRenderers, this, this.entityRenderMap);
      }
   }

   public boolean method_21284() {
      return this.recoveredField1000;
   }

   public void renderWitherSkull(Entity var1, float var2) {
      double var3 = var1.P + (var1.s - var1.P) * var2;
      double var5 = var1.Q + (var1.t - var1.Q) * var2;
      double var7 = var1.R + (var1.u - var1.R) * var2;
      Render var9 = this.getEntityRenderObject(var1);
      if (var9 != null && this.renderEngine != null) {
         int var10 = var1.b_(var2);
         int var11 = var10 % 65536;
         int var12 = var10 / 65536;
         OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var11 / 1.0F, var12 / 1.0F);
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         var9.renderName(var1, var3 - this.renderPosX, var5 - this.renderPosY, var7 - this.renderPosZ);
      }
   }

   public double getDistanceToCamera(double var1, double var3, double var5) {
      double var7 = var1 - this.viewerPosX;
      double var9 = var3 - this.viewerPosY;
      double var11 = var5 - this.viewerPosZ;
      return var7 * var7 + var9 * var9 + var11 * var11;
   }

   public <T extends Entity> Render<T> getEntityRenderObject(Entity var1) {
      if (var1 instanceof AbstractClientPlayer) {
         String var2 = ((AbstractClientPlayer)var1).getSkinType();
         RenderPlayer var3 = this.skinMap.get(var2);
         return (Render<T>)(Render<?>)(var3 != null ? var3 : this.playerRenderer);
      } else {
         return this.getEntityClassRenderObject((Class<? extends Entity>)var1.getClass());
      }
   }

   public boolean doRenderEntity(Entity var1, double var2, double var4, double var6, float var8, float var9, boolean var10) {
      Render var11 = null;

      try {
         var11 = this.getEntityRenderObject(var1);
         if (var11 != null && this.renderEngine != null) {
            // Hidden arrows must skip post-render effects and every Hitboxes option too.
            if (var11 instanceof RenderArrow && var1 instanceof EntityArrow
               && ((RenderArrow)var11).method_01981((EntityArrow)var1)) {
               return true;
            }
            try {
               if (var11 instanceof RendererLivingEntity) {
                  ((RendererLivingEntity)var11).setRenderOutlines(this.recoveredField1001);
               }

               if (CustomEntityModels.isActive()) {
                  this.renderRender = var11;
               }

               var11.doRender(var1, var2, var4, var6, var8, var9);
            } catch (Throwable var18) {
               throw new ReportedException(CrashReport.makeCrashReport(var18, "Rendering entity in world"));
            }

            try {
               if (!this.recoveredField1001) {
                  var11.method_08026(var1, var2, var4, var6, var8, var9);
               }
            } catch (Throwable var17) {
               throw new ReportedException(CrashReport.makeCrashReport(var17, "Post-rendering entity in world"));
            }

            if (CheatBreaker.getInstance().getModuleManager().recoveredField1703.isEnabled() && !var1.isInvisible()) {
               try {
                  this.renderDebugBoundingBox(var1, var2, var4, var6, var8, var9);
               } catch (Throwable var16) {
                  throw new ReportedException(CrashReport.makeCrashReport(var16, "Rendering entity hitbox in world"));
               }
            }

            return true;
         } else {
            return this.renderEngine == null;
         }
      } catch (Throwable var19) {
         CrashReport var13 = CrashReport.makeCrashReport(var19, "Rendering entity in world");
         CrashReportCategory var14 = var13.makeCategory("Entity being rendered");
         var1.addEntityCrashInfo(var14);
         CrashReportCategory var15 = var13.makeCategory("Renderer details");
         var15.addCrashSection("Assigned renderer", var11);
         var15.addCrashSection("Location", CrashReportCategory.getCoordinateInfo(var2, var4, var6));
         var15.addCrashSection("Rotation", var8);
         var15.addCrashSection("Delta", var9);
         throw new ReportedException(var13);
      }
   }

   public Map<Class, Render> getEntityRenderMap() {
      return this.entityRenderMap;
   }

   public void method_21287(boolean var1) {
      this.recoveredField999 = var1;
   }

   public void setEntityRenderMap(Map var1) {
      this.entityRenderMap = var1;
   }

   public Map<String, RenderPlayer> getSkinMap() {
      return Collections.unmodifiableMap(this.skinMap);
   }

   public double method_21285() {
      return this.renderPosZ;
   }

   public void setPlayerViewY(float var1) {
      this.playerViewY = var1;
   }

   public boolean renderEntityStatic(Entity var1, float var2, boolean var3) {
      if (var1.W == 0) {
         var1.P = var1.s;
         var1.Q = var1.t;
         var1.R = var1.u;
      }

      double var4 = var1.P + (var1.s - var1.P) * var2;
      double var6 = var1.Q + (var1.t - var1.Q) * var2;
      double var8 = var1.R + (var1.u - var1.R) * var2;
      float var10 = var1.A + (var1.y - var1.A) * var2;
      int var11 = var1.b_(var2);
      if (var1.isBurning()) {
         var11 = 15728880;
      }

      int var12 = var11 % 65536;
      int var13 = var11 / 65536;
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var12 / 1.0F, var13 / 1.0F);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      return this.doRenderEntity(var1, var4 - this.renderPosX, var6 - this.renderPosY, var8 - this.renderPosZ, var10, var2, var3);
   }

   public boolean shouldRender(Entity var1, ICamera var2, double var3, double var5, double var7) {
      Render var9 = this.getEntityRenderObject(var1);
      return var9 != null && var9.shouldRender(var1, var2, var3, var5, var7);
   }

   public boolean renderEntitySimple(Entity var1, float var2) {
      return this.renderEntityStatic(var1, var2, false);
   }
}
