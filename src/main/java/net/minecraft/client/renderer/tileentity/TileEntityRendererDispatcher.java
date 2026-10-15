package net.minecraft.client.renderer.tileentity;

import com.google.common.collect.Maps;
import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.config.GlobalSettings;
import com.cheatbreaker.client.util.TileEntityRenderDistance;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ReportedException;
import net.minecraft.world.World;
import net.optifine.EmissiveTextures;
import net.optifine.reflect.Reflector;

public class TileEntityRendererDispatcher {
   private double distanceSquaredMultiplier = 1.0;
   public static double staticPlayerX;
   public Map<Class, TileEntitySpecialRenderer> mapSpecialRenderers = Maps.newHashMap();
   public double entityZ;
   public Tessellator batchBuffer = new Tessellator(2097152);
   public boolean drawingBatch = false;
   public static double staticPlayerY;
   public static TileEntityRendererDispatcher instance = new TileEntityRendererDispatcher();
   public static double staticPlayerZ;
   public double entityY;
   public float entityYaw;
   public TileEntity tileEntityRendered;
   public FontRenderer fontRenderer;
   public World worldObj;
   public Entity entity;
   public double entityX;
   public TextureManager renderEngine;
   public float entityPitch;

   public void cacheActiveRenderInfo(World var1, TextureManager var2, FontRenderer var3, Entity var4, float var5) {
      CheatBreaker client = CheatBreaker.getInstance();
      GlobalSettings settings = client == null ? null : client.getGlobalSettings();
      this.distanceSquaredMultiplier = settings == null || settings.recoveredField563 == null || settings.tileEntityDistance == null
         ? 1.0 : TileEntityRenderDistance.squaredMultiplier(settings.recoveredField563.method_08908(),
            ((Number)settings.tileEntityDistance.getValue()).doubleValue());
      if (this.worldObj != var1) {
         this.setWorld(var1);
      }

      this.renderEngine = var2;
      this.entity = var4;
      this.fontRenderer = var3;
      this.entityYaw = var4.A + (var4.y - var4.A) * var5;
      this.entityPitch = var4.B + (var4.z - var4.B) * var5;
      this.entityX = var4.P + (var4.s - var4.P) * var5;
      this.entityY = var4.Q + (var4.t - var4.Q) * var5;
      this.entityZ = var4.R + (var4.u - var4.R) * var5;
   }

   public FontRenderer getFontRenderer() {
      return this.fontRenderer;
   }

   public void renderTileEntity(TileEntity var1, float var2, int var3) {
      if (var1.a(this.entityX, this.entityY, this.entityZ) < var1.getMaxRenderDistanceSquared() * this.distanceSquaredMultiplier) {
         boolean var4 = true;
         if (Reflector.ForgeTileEntity_hasFastRenderer.exists()) {
            var4 = !this.drawingBatch || !Reflector.callBoolean(var1, Reflector.ForgeTileEntity_hasFastRenderer);
         }

         if (var4) {
            RenderHelper.enableStandardItemLighting();
            int var5 = this.worldObj.getCombinedLight(var1.v(), 0);
            int var6 = var5 % 65536;
            int var7 = var5 / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var6 / 1.0F, var7 / 1.0F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         }

         BlockPos var8 = var1.v();
         if (!this.worldObj.isBlockLoaded(var8, false)) {
            return;
         }

         if (EmissiveTextures.isActive()) {
            EmissiveTextures.beginRender();
         }

         this.renderTileEntityAt(var1, var8.getX() - staticPlayerX, var8.getY() - staticPlayerY, var8.getZ() - staticPlayerZ, var2, var3);
         if (EmissiveTextures.isActive()) {
            if (EmissiveTextures.hasEmissive()) {
               EmissiveTextures.beginRenderEmissive();
               this.renderTileEntityAt(var1, var8.getX() - staticPlayerX, var8.getY() - staticPlayerY, var8.getZ() - staticPlayerZ, var2, var3);
               EmissiveTextures.endRenderEmissive();
            }

            EmissiveTextures.endRender();
         }
      }
   }

   public void setWorld(World var1) {
      this.worldObj = var1;
   }

   public <T extends TileEntity> TileEntitySpecialRenderer<T> getSpecialRendererByClass(Class<? extends TileEntity> var1) {
      TileEntitySpecialRenderer var2 = this.mapSpecialRenderers.get(var1);
      if (var2 == null && var1 != TileEntity.class) {
         var2 = this.getSpecialRendererByClass((Class)var1.getSuperclass());
         this.mapSpecialRenderers.put(var1, var2);
      }

      return var2;
   }

   public void preDrawBatch() {
      this.batchBuffer.getWorldRenderer().begin(7, DefaultVertexFormats.BLOCK);
      this.drawingBatch = true;
   }

   public TileEntityRendererDispatcher() {
      this.mapSpecialRenderers.put(TileEntitySign.class, new TileEntitySignRenderer());
      this.mapSpecialRenderers.put(TileEntityMobSpawner.class, new TileEntityMobSpawnerRenderer());
      this.mapSpecialRenderers.put(TileEntityPiston.class, new TileEntityPistonRenderer());
      this.mapSpecialRenderers.put(TileEntityChest.class, new TileEntityChestRenderer());
      this.mapSpecialRenderers.put(TileEntityEnderChest.class, new TileEntityEnderChestRenderer());
      this.mapSpecialRenderers.put(TileEntityEnchantmentTable.class, new TileEntityEnchantmentTableRenderer());
      this.mapSpecialRenderers.put(TileEntityEndPortal.class, new TileEntityEndPortalRenderer());
      this.mapSpecialRenderers.put(TileEntityBeacon.class, new TileEntityBeaconRenderer());
      this.mapSpecialRenderers.put(TileEntitySkull.class, new TileEntitySkullRenderer());
      this.mapSpecialRenderers.put(TileEntityBanner.class, new TileEntityBannerRenderer());

      for (TileEntitySpecialRenderer var2 : this.mapSpecialRenderers.values()) {
         var2.setRendererDispatcher(this);
      }
   }

   public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8) {
      this.renderTileEntityAt(var1, var2, var4, var6, var8, -1);
   }

   public <T extends TileEntity> TileEntitySpecialRenderer<T> getSpecialRenderer(TileEntity var1) {
      return var1 != null && !var1.isInvalid() ? this.getSpecialRendererByClass((Class<? extends TileEntity>)var1.getClass()) : null;
   }

   public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8, int var9) {
      TileEntitySpecialRenderer var10 = this.getSpecialRenderer(var1);
      if (var10 != null) {
         try {
            this.tileEntityRendered = var1;
            if (this.drawingBatch && Reflector.callBoolean(var1, Reflector.ForgeTileEntity_hasFastRenderer)) {
               var10.renderTileEntityFast(var1, var2, var4, var6, var8, var9, this.batchBuffer.getWorldRenderer());
            } else {
               var10.renderTileEntityAt(var1, var2, var4, var6, var8, var9);
            }

            this.tileEntityRendered = null;
         } catch (Throwable var14) {
            CrashReport var12 = CrashReport.makeCrashReport(var14, "Rendering Block Entity");
            CrashReportCategory var13 = var12.makeCategory("Block Entity Details");
            var1.addInfoToCrashReport(var13);
            throw new ReportedException(var12);
         }
      }
   }

   public void drawBatch(int var1) {
      this.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
      RenderHelper.disableStandardItemLighting();
      GlStateManager.blendFunc(770, 771);
      GlStateManager.enableBlend();
      GlStateManager.disableCull();
      if (Minecraft.isAmbientOcclusionEnabled()) {
         GlStateManager.shadeModel(7425);
      } else {
         GlStateManager.shadeModel(7424);
      }

      if (var1 > 0) {
         this.batchBuffer.getWorldRenderer().sortVertexData((float)staticPlayerX, (float)staticPlayerY, (float)staticPlayerZ);
      }

      this.batchBuffer.draw();
      RenderHelper.enableStandardItemLighting();
      this.drawingBatch = false;
   }
}
