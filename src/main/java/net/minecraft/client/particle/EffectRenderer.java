package net.minecraft.client.particle;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.module.type.ParticlesModule;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.src.Config;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.optifine.reflect.Reflector;

public class EffectRenderer {
   public static ResourceLocation particleTextures = new ResourceLocation("textures/particle/particles.png");
   public Random rand;
   public TextureManager renderer;
   public World worldObj;
   public List<EntityParticleEmitter> particleEmitters;
   public Map<Integer, IParticleFactory> particleTypes;
   public List<EntityFX>[][] fxLayers = new List[4][];

   public String getStatistics() {
      int var1 = 0;

      for (int var2 = 0; var2 < 4; var2++) {
         for (int var3 = 0; var3 < 2; var3++) {
            var1 += this.fxLayers[var2][var3].size();
         }
      }

      return "" + var1;
   }

   public void moveToLayer(EntityFX var1, int var2, int var3) {
      for (int var4 = 0; var4 < 4; var4++) {
         if (this.fxLayers[var4][var2].contains(var1)) {
            this.fxLayers[var4][var2].remove(var1);
            this.fxLayers[var4][var3].add(var1);
         }
      }
   }

   public void addBlockHitEffects(BlockPos var1, MovingObjectPosition var2) {
      IBlockState var3 = this.worldObj.getBlockState(var1);
      if (var3 != null) {
         boolean var4 = Reflector.callBoolean(var3.getBlock(), Reflector.ForgeBlock_addHitEffects, this.worldObj, var2, this);
         if (var3 != null && !var4) {
            this.addBlockHitEffects(var1, var2.sideHit);
         }
      }
   }

   public void renderLitParticles(Entity var1, float var2) {
      float var3 = (float) (Math.PI / 180.0);
      float var4 = MathHelper.cos(var1.y * (float) (Math.PI / 180.0));
      float var5 = MathHelper.sin(var1.y * (float) (Math.PI / 180.0));
      float var6 = -var5 * MathHelper.sin(var1.z * (float) (Math.PI / 180.0));
      float var7 = var4 * MathHelper.sin(var1.z * (float) (Math.PI / 180.0));
      float var8 = MathHelper.cos(var1.z * (float) (Math.PI / 180.0));

      for (int var9 = 0; var9 < 2; var9++) {
         List var10 = this.fxLayers[3][var9];
         if (!var10.isEmpty()) {
            Tessellator var11 = Tessellator.getInstance();
            WorldRenderer var12 = var11.getWorldRenderer();

            for (int var13 = 0; var13 < var10.size(); var13++) {
               EntityFX var14 = (EntityFX)var10.get(var13);
               var14.renderParticle(var12, var1, var2, var4, var8, var5, var6, var7);
            }
         }
      }
   }

   public void addEffect(EntityFX var1) {
      if (var1 != null && (!(var1 instanceof EntityFirework_SparkFX) || Config.isFireworkParticles())) {
         int var2 = var1.getFXLayer();
         int var3 = var1.method_10059() != 1.0F ? 0 : 1;
         if (this.fxLayers[var2][var3].size() >= 4000) {
            this.fxLayers[var2][var3].remove(0);
         }

         this.fxLayers[var2][var3].add(var1);
      }
   }

   public void addBlockDestroyEffects(BlockPos var1, IBlockState var2) {
      ParticlesModule var3 = CheatBreaker.getInstance().getModuleManager().recoveredField1730;
      if (!var3.isEnabled() || var3.recoveredField1635.method_08908()) {
         boolean var4;
         if (Reflector.ForgeBlock_addDestroyEffects.exists() && Reflector.ForgeBlock_isAir.exists()) {
            Block var5 = var2.getBlock();
            var4 = !Reflector.callBoolean(var5, Reflector.ForgeBlock_isAir, this.worldObj, var1)
               && !Reflector.callBoolean(var5, Reflector.ForgeBlock_addDestroyEffects, this.worldObj, var1, this);
         } else {
            var4 = var2.getBlock().getMaterial() != Material.air;
         }

         if (var4) {
            var2 = var2.getBlock().getActualState(var2, this.worldObj, var1);
            byte var16 = 4;

            for (int var6 = 0; var6 < var16; var6++) {
               for (int var7 = 0; var7 < var16; var7++) {
                  for (int var8 = 0; var8 < var16; var8++) {
                     double var9 = var1.getX() + (var6 + 0.5) / var16;
                     double var11 = var1.getY() + (var7 + 0.5) / var16;
                     double var13 = var1.getZ() + (var8 + 0.5) / var16;
                     this.addEffect(
                        new EntityDiggingFX(
                              this.worldObj, var9, var11, var13, var9 - var1.getX() - 0.5, var11 - var1.getY() - 0.5, var13 - var1.getZ() - 0.5, var2
                           )
                           .setBlockPos(var1)
                     );
                  }
               }
            }
         }
      }
   }

   public void addBlockHitEffects(BlockPos var1, EnumFacing var2) {
      ParticlesModule var3 = CheatBreaker.getInstance().getModuleManager().recoveredField1730;
      if (!var3.isEnabled() || var3.recoveredField1635.method_08908()) {
         IBlockState var4 = this.worldObj.getBlockState(var1);
         Block var5 = var4.getBlock();
         if (var5.getRenderType() != -1) {
            int var6 = var1.getX();
            int var7 = var1.getY();
            int var8 = var1.getZ();
            float var9 = 0.1F;
            double var10 = var6
               + this.rand.nextDouble() * (var5.getBlockBoundsMaxX() - var5.getBlockBoundsMinX() - var9 * 2.0F)
               + var9
               + var5.getBlockBoundsMinX();
            double var12 = var7
               + this.rand.nextDouble() * (var5.getBlockBoundsMaxY() - var5.getBlockBoundsMinY() - var9 * 2.0F)
               + var9
               + var5.getBlockBoundsMinY();
            double var14 = var8
               + this.rand.nextDouble() * (var5.getBlockBoundsMaxZ() - var5.getBlockBoundsMinZ() - var9 * 2.0F)
               + var9
               + var5.getBlockBoundsMinZ();
            if (var2 == EnumFacing.DOWN) {
               var12 = var7 + var5.getBlockBoundsMinY() - var9;
            }

            if (var2 == EnumFacing.UP) {
               var12 = var7 + var5.getBlockBoundsMaxY() + var9;
            }

            if (var2 == EnumFacing.NORTH) {
               var14 = var8 + var5.getBlockBoundsMinZ() - var9;
            }

            if (var2 == EnumFacing.SOUTH) {
               var14 = var8 + var5.getBlockBoundsMaxZ() + var9;
            }

            if (var2 == EnumFacing.WEST) {
               var10 = var6 + var5.getBlockBoundsMinX() - var9;
            }

            if (var2 == EnumFacing.EAST) {
               var10 = var6 + var5.getBlockBoundsMaxX() + var9;
            }

            this.addEffect(
               new EntityDiggingFX(this.worldObj, var10, var12, var14, 0.0, 0.0, 0.0, var4)
                  .setBlockPos(var1)
                  .multiplyVelocity(0.2F)
                  .multipleParticleScaleBy(0.6F)
            );
         }
      }
   }

   public void registerVanillaParticles() {
      this.registerParticle(EnumParticleTypes.EXPLOSION_NORMAL.getParticleID(), new EntityExplodeFX.Factory());
      this.registerParticle(EnumParticleTypes.WATER_BUBBLE.getParticleID(), new EntityBubbleFX.Factory());
      this.registerParticle(EnumParticleTypes.WATER_SPLASH.getParticleID(), new EntitySplashFX.Factory());
      this.registerParticle(EnumParticleTypes.WATER_WAKE.getParticleID(), new EntityFishWakeFX.Factory());
      this.registerParticle(EnumParticleTypes.WATER_DROP.getParticleID(), new EntityRainFX.Factory());
      this.registerParticle(EnumParticleTypes.SUSPENDED.getParticleID(), new EntitySuspendFX.Factory());
      this.registerParticle(EnumParticleTypes.SUSPENDED_DEPTH.getParticleID(), new EntityAuraFX.Factory());
      this.registerParticle(EnumParticleTypes.CRIT.getParticleID(), new EntityCrit2FX.Factory());
      this.registerParticle(EnumParticleTypes.CRIT_MAGIC.getParticleID(), new EntityCrit2FX.MagicFactory());
      this.registerParticle(EnumParticleTypes.SMOKE_NORMAL.getParticleID(), new EntitySmokeFX.Factory());
      this.registerParticle(EnumParticleTypes.SMOKE_LARGE.getParticleID(), new EntityCritFX.Factory());
      this.registerParticle(EnumParticleTypes.SPELL.getParticleID(), new EntitySpellParticleFX.Factory());
      this.registerParticle(EnumParticleTypes.SPELL_INSTANT.getParticleID(), new EntitySpellParticleFX.InstantFactory());
      this.registerParticle(EnumParticleTypes.SPELL_MOB.getParticleID(), new EntitySpellParticleFX.MobFactory());
      this.registerParticle(EnumParticleTypes.SPELL_MOB_AMBIENT.getParticleID(), new EntitySpellParticleFX.AmbientMobFactory());
      this.registerParticle(EnumParticleTypes.SPELL_WITCH.getParticleID(), new EntitySpellParticleFX.WitchFactory());
      this.registerParticle(EnumParticleTypes.DRIP_WATER.getParticleID(), new EntityDropParticleFX.WaterFactory());
      this.registerParticle(EnumParticleTypes.DRIP_LAVA.getParticleID(), new EntityDropParticleFX.LavaFactory());
      this.registerParticle(EnumParticleTypes.VILLAGER_ANGRY.getParticleID(), new EntityHeartFX.AngryVillagerFactory());
      this.registerParticle(EnumParticleTypes.VILLAGER_HAPPY.getParticleID(), new EntityAuraFX.HappyVillagerFactory());
      this.registerParticle(EnumParticleTypes.TOWN_AURA.getParticleID(), new EntityAuraFX.Factory());
      this.registerParticle(EnumParticleTypes.NOTE.getParticleID(), new EntityNoteFX.Factory());
      this.registerParticle(EnumParticleTypes.PORTAL.getParticleID(), new EntityPortalFX.Factory());
      this.registerParticle(EnumParticleTypes.ENCHANTMENT_TABLE.getParticleID(), new EntityEnchantmentTableParticleFX.EnchantmentTable());
      this.registerParticle(EnumParticleTypes.FLAME.getParticleID(), new EntityFlameFX.Factory());
      this.registerParticle(EnumParticleTypes.LAVA.getParticleID(), new EntityLavaFX.Factory());
      this.registerParticle(EnumParticleTypes.FOOTSTEP.getParticleID(), new EntityFootStepFX.Factory());
      this.registerParticle(EnumParticleTypes.CLOUD.getParticleID(), new EntityCloudFX.Factory());
      this.registerParticle(EnumParticleTypes.REDSTONE.getParticleID(), new EntityReddustFX.Factory());
      this.registerParticle(EnumParticleTypes.SNOWBALL.getParticleID(), new EntityBreakingFX.SnowballFactory());
      this.registerParticle(EnumParticleTypes.SNOW_SHOVEL.getParticleID(), new EntitySnowShovelFX.Factory());
      this.registerParticle(EnumParticleTypes.SLIME.getParticleID(), new EntityBreakingFX.SlimeFactory());
      this.registerParticle(EnumParticleTypes.HEART.getParticleID(), new EntityHeartFX.Factory());
      this.registerParticle(EnumParticleTypes.BARRIER.getParticleID(), new Barrier.Factory());
      this.registerParticle(EnumParticleTypes.ITEM_CRACK.getParticleID(), new EntityBreakingFX.Factory());
      this.registerParticle(EnumParticleTypes.BLOCK_CRACK.getParticleID(), new EntityDiggingFX.Factory());
      this.registerParticle(EnumParticleTypes.BLOCK_DUST.getParticleID(), new EntityBlockDustFX.Factory());
      this.registerParticle(EnumParticleTypes.EXPLOSION_HUGE.getParticleID(), new EntityHugeExplodeFX.Factory());
      this.registerParticle(EnumParticleTypes.EXPLOSION_LARGE.getParticleID(), new EntityLargeExplodeFX.Factory());
      this.registerParticle(EnumParticleTypes.FIREWORKS_SPARK.getParticleID(), new EntityFirework_Factory());
      this.registerParticle(EnumParticleTypes.MOB_APPEARANCE.getParticleID(), new MobAppearance.Factory());
   }

   public void updateEffects() {
      for (int var1 = 0; var1 < 4; var1++) {
         this.updateEffectLayer(var1);
      }

      ArrayList var4 = Lists.newArrayList();

      for (EntityParticleEmitter var3 : this.particleEmitters) {
         var3.onUpdate();
         if (var3.I) {
            var4.add(var3);
         }
      }

      this.particleEmitters.removeAll(var4);
   }

   public EntityFX spawnEffectParticle(int var1, double var2, double var4, double var6, double var8, double var10, double var12, int... var14) {
      IParticleFactory var15 = this.particleTypes.get(var1);
      if (var15 != null) {
         EntityFX var16 = var15.getEntityFX(var1, this.worldObj, var2, var4, var6, var8, var10, var12, var14);
         if (var16 != null) {
            this.addEffect(var16);
            return var16;
         }
      }

      return null;
   }

   public void method_00737(Entity var1, EnumParticleTypes var2, float var3) {
      this.particleEmitters.add(new EntityParticleEmitter(this.worldObj, var1, var2, var3));
   }

   public void clearEffects(World var1) {
      this.worldObj = var1;

      for (int var2 = 0; var2 < 4; var2++) {
         for (int var3 = 0; var3 < 2; var3++) {
            this.fxLayers[var2][var3].clear();
         }
      }

      this.particleEmitters.clear();
   }

   public void clearBarrierParticles() {
      // Barrier markers always use terrain atlas layer 1; retain every other particle.
      for (List<EntityFX> layer : this.fxLayers[1]) {
         layer.removeIf(particle -> particle instanceof Barrier);
      }
   }

   public void moveToNoAlphaLayer(EntityFX var1) {
      this.moveToLayer(var1, 0, 1);
   }

   public void moveToAlphaLayer(EntityFX var1) {
      this.moveToLayer(var1, 1, 0);
   }

   public void registerParticle(int var1, IParticleFactory var2) {
      this.particleTypes.put(var1, var2);
   }

   public void tickParticle(final EntityFX var1) {
      try {
         var1.onUpdate();
      } catch (Throwable var6) {
         CrashReport var3 = CrashReport.makeCrashReport(var6, "Ticking Particle");
         CrashReportCategory var4 = var3.makeCategory("Particle being ticked");
         final int var5 = var1.getFXLayer();
         var4.addCrashSectionCallable("Particle", new Callable<String>() {
            public String call() throws java.lang.Exception {
               return var1.toString();
            }
         });
         var4.addCrashSectionCallable("Particle Type", new Callable<String>() {
            public String call() throws java.lang.Exception {
               return var5 == 0 ? "MISC_TEXTURE" : (var5 == 1 ? "TERRAIN_TEXTURE" : (var5 == 3 ? "ENTITY_PARTICLE_TEXTURE" : "Unknown - " + var5));
            }
         });
         throw new ReportedException(var3);
      }
   }

   public void emitParticleAtEntity(Entity var1, EnumParticleTypes var2) {
      this.particleEmitters.add(new EntityParticleEmitter(this.worldObj, var1, var2));
   }

   public EffectRenderer(World var1, TextureManager var2) {
      this.particleEmitters = Lists.newArrayList();
      this.rand = new Random();
      this.particleTypes = Maps.newHashMap();
      this.worldObj = var1;
      this.renderer = var2;

      for (int var3 = 0; var3 < 4; var3++) {
         this.fxLayers[var3] = new List[2];

         for (int var4 = 0; var4 < 2; var4++) {
            Minecraft.getMinecraft().recoveredField3820.method_25825();
            this.fxLayers[var3][var4] = Lists.newArrayList();
         }
      }

      this.registerVanillaParticles();
   }

   public void updateEffectAlphaLayer(List<EntityFX> var1) {
      ArrayList var2 = Lists.newArrayList();
      long var3 = System.currentTimeMillis();
      int var5 = var1.size();

      for (int var6 = 0; var6 < var1.size(); var6++) {
         EntityFX var7 = (EntityFX)var1.get(var6);
         this.tickParticle(var7);
         if (var7.I) {
            var2.add(var7);
         }

         var5--;
         if (System.currentTimeMillis() > var3 + 20L) {
            break;
         }
      }

      if (var5 > 0) {
         int var9 = var5;

         for (Iterator var10 = var1.iterator(); var10.hasNext() && var9 > 0; var9--) {
            EntityFX var8 = (EntityFX)var10.next();
            var8.setDead();
            var10.remove();
         }
      }

      var1.removeAll(var2);
   }

   public void updateEffectLayer(int var1) {
      for (int var2 = 0; var2 < 2; var2++) {
         this.updateEffectAlphaLayer(this.fxLayers[var1][var2]);
      }
   }

   public void renderParticles(Entity var1, float var2) {
      float var3 = ActiveRenderInfo.getRotationX();
      float var4 = ActiveRenderInfo.getRotationZ();
      float var5 = ActiveRenderInfo.getRotationYZ();
      float var6 = ActiveRenderInfo.getRotationXY();
      float var7 = ActiveRenderInfo.getRotationXZ();
      EntityFX.aw = var1.P + (var1.s - var1.P) * var2;
      EntityFX.ax = var1.Q + (var1.t - var1.Q) * var2;
      EntityFX.ay = var1.R + (var1.u - var1.R) * var2;
      GlStateManager.enableBlend();
      GlStateManager.blendFunc(770, 771);
      GlStateManager.alphaFunc(516, 0.003921569F);

      for (int var8 = 0; var8 < 3; var8++) {
         for (int var9 = 0; var9 < 2; var9++) {
            if (!this.fxLayers[var8][var9].isEmpty()) {
               switch (var9) {
                  case 0:
                     GlStateManager.depthMask(false);
                     break;
                  case 1:
                     GlStateManager.depthMask(true);
               }

               switch (var8) {
                  case 0:
                  default:
                     this.renderer.bindTexture(particleTextures);
                     break;
                  case 1:
                     this.renderer.bindTexture(TextureMap.locationBlocksTexture);
               }

               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               Tessellator var11 = Tessellator.getInstance();
               WorldRenderer var12 = var11.getWorldRenderer();
               var12.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);

               for (int var13 = 0; var13 < this.fxLayers[var8][var9].size(); var13++) {
                  final EntityFX var14 = this.fxLayers[var8][var9].get(var13);

                  try {
                     var14.renderParticle(var12, var1, var2, var3, var7, var4, var5, var6);
                  } catch (Throwable var18) {
                     CrashReport var16 = CrashReport.makeCrashReport(var18, "Rendering Particle");
                     CrashReportCategory var17 = var16.makeCategory("Particle being rendered");
                     var17.addCrashSectionCallable("Particle", new Callable<String>() {
                        public String call() throws java.lang.Exception {
                           return var14.toString();
                        }
                     });
                     final int particleLayer = var8;
                     var17.addCrashSectionCallable("Particle Type", new Callable<String>() {
                        public String call() throws java.lang.Exception {
                           return particleLayer == 0 ? "MISC_TEXTURE" : (particleLayer == 1 ? "TERRAIN_TEXTURE" : (particleLayer == 3 ? "ENTITY_PARTICLE_TEXTURE" : "Unknown - " + particleLayer));
                        }
                     });
                     throw new ReportedException(var16);
                  }
               }

               var11.draw();
            }
         }
      }

      GlStateManager.depthMask(true);
      GlStateManager.disableBlend();
      GlStateManager.alphaFunc(516, 0.1F);
   }
}
