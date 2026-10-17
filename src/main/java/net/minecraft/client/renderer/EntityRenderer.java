package net.minecraft.client.renderer;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.module.type.EnvironmentModule;
import com.cheatbreaker.client.module.type.PerspectiveModule;
import com.cheatbreaker.client.module.type.HurtcamModule;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.MapItemRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderLinkHelper;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.src.Config;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MouseFilter;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.biome.BiomeGenBase;
import net.optifine.CustomColors;
import net.optifine.GlErrors;
import net.optifine.Lagometer;
import net.optifine.RandomEntities;
import net.optifine.gui.GuiChatOF;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorForge;
import net.optifine.reflect.ReflectorResolver;
import net.optifine.shaders.Shaders;
import net.optifine.shaders.ShadersRender;
import net.optifine.util.MemoryMonitor;
import net.optifine.util.TextureUtils;
import net.optifine.util.TimedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.Project;
import com.cheatbreaker.client.util.render.EyeHeightAnimationController;
import com.cheatbreaker.client.util.render.FreelookController;

public class EntityRenderer implements IResourceManagerReloadListener {
   public float fogColor2;
   public Minecraft mc;
   public World updatedWorld;
   public float fogColorBlue;
   public long recoveredField3133;
   public int frameCount;
   public float torchFlickerDX;
   public boolean fogStandard;
   public float fogColorRed;
   public ShaderGroup[] fxaaShaders;
   public int[] lightmapColors;
   public static Logger recoveredField3134 = LogManager.getLogger();
   public static int anaglyphField;
   public DynamicTexture lightmapTexture;
   public static ResourceLocation recoveredField3158 = new ResourceLocation("textures/environment/rain.png");
   public IResourceManager recoveredField3135;
   public float clipDistance;
   public MouseFilter mouseFilterYAxis;
   public float farPlaneDistance;
   public float bossColorModifierPrev;
   public float smoothCamFilterY;
   public boolean cloudFog;
   public float[] recoveredField3136;
   public int shaderIndex;
   public int recoveredField3137;
   public boolean useShader;
   public double recoveredField3138;
   public double recoveredField3139;
   public float smoothCamFilterX;
   public float recoveredField3140;
   public int rendererUpdateCount;
   public static ResourceLocation recoveredField3153 = new ResourceLocation("textures/environment/snow.png");
   public ShaderGroup theShaderGroup;
   public float smoothCamPartialTicks;
   public MapItemRenderer recoveredField3142;
   public double recoveredField3143;
   public boolean recoveredField3144;
   public int recoveredField3145;
   public int recoveredField3146;
   public float fogColorGreen;
   public boolean recoveredField3147;
   public float smoothCamYaw;
   public static ResourceLocation[] shaderResourceLocations = new ResourceLocation[]{
      new ResourceLocation("shaders/post/notch.json"),
      new ResourceLocation("shaders/post/fxaa.json"),
      new ResourceLocation("shaders/post/art.json"),
      new ResourceLocation("shaders/post/bumpy.json"),
      new ResourceLocation("shaders/post/blobs2.json"),
      new ResourceLocation("shaders/post/pencil.json"),
      new ResourceLocation("shaders/post/color_convolve.json"),
      new ResourceLocation("shaders/post/deconverge.json"),
      new ResourceLocation("shaders/post/flip.json"),
      new ResourceLocation("shaders/post/invert.json"),
      new ResourceLocation("shaders/post/ntsc.json"),
      new ResourceLocation("shaders/post/outline.json"),
      new ResourceLocation("shaders/post/phosphor.json"),
      new ResourceLocation("shaders/post/scan_pincushion.json"),
      new ResourceLocation("shaders/post/sobel.json"),
      new ResourceLocation("shaders/post/bits.json"),
      new ResourceLocation("shaders/post/desaturate.json"),
      new ResourceLocation("shaders/post/green.json"),
      new ResourceLocation("shaders/post/blur.json"),
      new ResourceLocation("shaders/post/wobble.json"),
      new ResourceLocation("shaders/post/blobs.json"),
      new ResourceLocation("shaders/post/antialias.json"),
      new ResourceLocation("shaders/post/creeper.json"),
      new ResourceLocation("shaders/post/spider.json")
   };
   public boolean recoveredField3148;
   public static boolean anaglyphEnable;
   public float smoothCamPitch;
   public float bossColorModifier;
   public Random random = new Random();
   public MouseFilter mouseFilterXAxis = new MouseFilter();
   public float[] recoveredField3149;
   public int recoveredField3150;
   public float fovModifierHand;
   public float fogColor1;
   public boolean drawBlockOutline;
   public static ResourceLocation recoveredField3154 = new ResourceLocation("shaders/post/animblur.json");
   public FloatBuffer fogColorBuffer;
   public float thirdPersonDistanceTemp;
   public boolean recoveredField3152;
   public static ResourceLocation recoveredField3151 = new ResourceLocation("shaders/post/motionblur.json");
   public float fovModifierHandPrev;
   public float thirdPersonDistance;
   public Entity pointedEntity;
   public boolean loadVisibleChunks;
   public static ResourceLocation recoveredField3141 = new ResourceLocation("shaders/post/lunar_motionblur.json");
   public boolean lightmapUpdateNeeded;
   public ResourceLocation locationLightMap;
   public float torchFlickerX;
   public long recoveredField3155;
   public int recoveredField3156;
   public float recoveredField3157;
   public ItemRenderer itemRenderer;
   public static int shaderCount = EntityRenderer.shaderResourceLocations.length;
   public long recoveredField3159;

   public void method_29145() {
      GlErrors.frameStart();
      if (!this.recoveredField3147) {
         ReflectorResolver.resolve();
         TextureUtils.registerResourceListener();
         if (Config.method_03978() == 64 && Config.method_04030() == 32) {
            Config.setNotify64BitJava(true);
         }

         this.recoveredField3147 = true;
      }

      Config.checkDisplayMode();
      WorldClient var1 = this.mc.theWorld;
      if (var1 != null) {
         if (Config.getNewRelease() != null) {
            String var2 = "HD_U".replace("HD_U", "HD Ultra").replace("L", "Light");
            String var3 = var2 + " " + Config.getNewRelease();
            ChatComponentText var4 = new ChatComponentText(I18n.format("of.message.newVersion", "§n" + var3 + "§r"));
            var4.setChatStyle(new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://optifine.net/downloads")));
            this.mc.ingameGUI.getChatGUI().printChatMessage(var4);
            Config.setNewRelease((String)null);
         }

         if (Config.isNotify64BitJava()) {
            Config.setNotify64BitJava(false);
            ChatComponentText var5 = new ChatComponentText(I18n.format("of.message.java64Bit"));
            this.mc.ingameGUI.getChatGUI().printChatMessage(var5);
         }
      }

      if (this.mc.currentScreen instanceof GuiMainMenu) {
         this.updateMainMenu((GuiMainMenu)this.mc.currentScreen);
      }

      if (this.updatedWorld != var1) {
         RandomEntities.worldChanged(this.updatedWorld, var1);
         Config.updateThreadPriorities();
         this.recoveredField3159 = 0L;
         this.recoveredField3137 = 0;
         this.updatedWorld = var1;
      }

      if (!this.setFxaaShader(Shaders.configAntialiasingLevel)) {
         Shaders.configAntialiasingLevel = 0;
      }

      if (this.mc.currentScreen != null && this.mc.currentScreen.getClass() == GuiChat.class) {
         this.mc.displayGuiScreen(new GuiChatOF((GuiChat)this.mc.currentScreen));
      }
   }

   public void activateNextShader() {
      if (OpenGlHelper.shadersSupported && this.mc.getRenderViewEntity() instanceof EntityPlayer) {
         if (this.theShaderGroup != null) {
            this.theShaderGroup.deleteShaderGroup();
         }

         this.shaderIndex = (this.shaderIndex + 1) % (shaderResourceLocations.length + 1);
         if (this.shaderIndex != shaderCount) {
            this.loadShader(shaderResourceLocations[this.shaderIndex]);
         } else {
            this.theShaderGroup = null;
         }
      }
   }

   public void renderCloudsCheck(RenderGlobal var1, float var2, int var3) {
      if (this.mc.gameSettings.renderDistanceChunks >= 4 && !Config.isCloudsOff() && Shaders.shouldRenderClouds(this.mc.gameSettings)) {
         this.mc.mcProfiler.endStartSection("clouds");
         GlStateManager.matrixMode(5889);
         GlStateManager.loadIdentity();
         Project.gluPerspective(this.getFOVModifier(var2, true), (float)this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.clipDistance * 4.0F);
         GlStateManager.matrixMode(5888);
         GlStateManager.pushMatrix();
         this.setupFog(0, var2);
         var1.renderClouds(var2, var3);
         GlStateManager.disableFog();
         GlStateManager.popMatrix();
         GlStateManager.matrixMode(5889);
         GlStateManager.loadIdentity();
         Project.gluPerspective(this.getFOVModifier(var2, true), (float)this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.clipDistance);
         GlStateManager.matrixMode(5888);
      }
   }

   public void switchUseShader() {
      this.useShader = !this.useShader;
   }

   public void method_29146(float var1) {
      EnvironmentModule var2 = CheatBreaker.getInstance().getModuleManager().recoveredField1726;
      boolean var3 = var2.recoveredField2035.method_08908();
      if (Reflector.ForgeWorldProvider_getWeatherRenderer.exists()) {
         WorldProvider var4 = this.mc.theWorld.t;
         Object var5 = Reflector.call(var4, Reflector.ForgeWorldProvider_getWeatherRenderer);
         if (var5 != null) {
            Reflector.callVoid(var5, Reflector.IRenderHandler_render, var1, this.mc.theWorld, this.mc);
            return;
         }
      }

      float var52 = this.mc.theWorld.j(var1);
      if (var52 > 0.0F || var2.isEnabled()) {
         if (var2.isEnabled() && var2.recoveredField2036.method_08908() && !var2.recoveredField2031.method_08874().equalsIgnoreCase("None")) {
            var52 = 1.0F;
         }

         if (var2.isEnabled() && var2.recoveredField2036.method_08908() && var2.recoveredField2031.method_08874().equals("Clear")) {
            return;
         }

         if (Config.isRainOff()) {
            return;
         }

         this.enableLightmap();
         Entity var53 = this.mc.getRenderViewEntity();
         WorldClient var6 = this.mc.theWorld;
         int var7 = MathHelper.floor_double(var53.s);
         int var8 = MathHelper.floor_double(var53.t);
         int var9 = MathHelper.floor_double(var53.u);
         Tessellator var10 = Tessellator.getInstance();
         WorldRenderer var11 = var10.getWorldRenderer();
         GlStateManager.disableCull();
         GL11.glNormal3f(0.0F, 1.0F, 0.0F);
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
         GlStateManager.alphaFunc(516, 0.1F);
         double var12 = var53.P + (var53.s - var53.P) * var1;
         double var14 = var53.Q + (var53.t - var53.Q) * var1;
         double var16 = var53.R + (var53.u - var53.R) * var1;
         int var18 = MathHelper.floor_double(var14);
         byte var19 = 5;
         if (Config.method_03856()) {
            var19 = 10;
         }

         int var20 = -1;
         float var21 = this.rendererUpdateCount + var1;
         var11.setTranslation(-var12, -var14, -var16);
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         BlockPos.MutableBlockPos var22 = new BlockPos.MutableBlockPos();

         for (int var23 = var9 - var19; var23 <= var9 + var19; var23++) {
            for (int var24 = var7 - var19; var24 <= var7 + var19; var24++) {
               int var25 = (var23 - var9 + 16) * 32 + var24 - var7 + 16;
               double var26 = this.recoveredField3149[var25] * 0.5;
               double var28 = this.recoveredField3136[var25] * 0.5;
               var22.set(var24, 0, var23);
               BiomeGenBase var30 = var6.getBiomeGenForCoords(var22);
               if (var30.canRain() || var30.getEnableSnow()) {
                  int var31 = var6.getPrecipitationHeight(var22).getY();
                  int var32 = var8 - var19;
                  int var33 = var8 + var19;
                  if (var32 < var31) {
                     var32 = var2.isEnabled() && var3 ? 0 : var31;
                  }

                  if (var33 < var31) {
                     var33 = var31;
                  }

                  int var34 = var31;
                  if (var31 < var18) {
                     var34 = var18;
                  }

                  if (var32 != var33 || var2.isEnabled() && var2.recoveredField2036.method_08908()) {
                     this.random.setSeed(var24 * var24 * 3121 + var24 * 45238971 ^ var23 * var23 * 418711 + var23 * 13761);
                     var22.set(var24, var32, var23);
                     float var35 = var30.getFloatTemperature(var22);
                     boolean var36;
                     if (var2.recoveredField2036.method_08908() && var2.isEnabled() && !var2.recoveredField2031.method_08874().equals("Clear")) {
                        var36 = var2.recoveredField2031.method_08874().equals("Rain");
                     } else {
                        var36 = var6.getWorldChunkManager().getTemperatureAtHeight(var35, var31) >= 0.15F;
                     }

                     if (var36) {
                        if (var20 != 0) {
                           if (var20 >= 0) {
                              var10.draw();
                           }

                           var20 = 0;
                           this.mc.getTextureManager().bindTexture(recoveredField3158);
                           var11.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                        }

                        double var37 = (
                              (double)(this.rendererUpdateCount + var24 * var24 * 3121 + var24 * 45238971 + var23 * var23 * 418711 + var23 * 13761 & 31) + var1
                           )
                           / 32.0
                           * (3.0 + this.random.nextDouble());
                        double var39 = var24 + 0.5F - var53.s;
                        double var41 = var23 + 0.5F - var53.u;
                        float var43 = MathHelper.sqrt_double(var39 * var39 + var41 * var41) / var19;
                        float var44 = ((1.0F - var43 * var43) * 0.5F + 0.5F) * var52;
                        var22.set(var24, var34, var23);
                        int var45 = var6.getCombinedLight(var22, 0);
                        int var46 = var45 >> 16 & 65535;
                        int var47 = var45 & 65535;
                        var11.pos(var24 - var26 + 0.5, var32, var23 - var28 + 0.5)
                           .tex(0.0, var32 * 0.25 + var37)
                           .color(1.0F, 1.0F, 1.0F, var44)
                           .lightmap(var46, var47)
                           .endVertex();
                        var11.pos(var24 + var26 + 0.5, var32, var23 + var28 + 0.5)
                           .tex(1.0, var32 * 0.25 + var37)
                           .color(1.0F, 1.0F, 1.0F, var44)
                           .lightmap(var46, var47)
                           .endVertex();
                        var11.pos(var24 + var26 + 0.5, var33, var23 + var28 + 0.5)
                           .tex(1.0, var33 * 0.25 + var37)
                           .color(1.0F, 1.0F, 1.0F, var44)
                           .lightmap(var46, var47)
                           .endVertex();
                        var11.pos(var24 - var26 + 0.5, var33, var23 - var28 + 0.5)
                           .tex(0.0, var33 * 0.25 + var37)
                           .color(1.0F, 1.0F, 1.0F, var44)
                           .lightmap(var46, var47)
                           .endVertex();
                     } else if (var6.getWorldChunkManager().getTemperatureAtHeight(var35, var31) < 0.15F
                        || var2.recoveredField2031.method_08874().equals("Snow") && var2.recoveredField2036.method_08908() && var2.isEnabled()) {
                        if (var20 != 1) {
                           if (var20 >= 0) {
                              var10.draw();
                           }

                           var20 = 1;
                           this.mc.getTextureManager().bindTexture(recoveredField3153);
                           var11.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                        }

                        double var54 = ((this.rendererUpdateCount & 511) + var1) / 512.0F;
                        double var55 = this.random.nextDouble() + var21 * 0.01 * (float)this.random.nextGaussian();
                        double var56 = this.random.nextDouble() + var21 * (float)this.random.nextGaussian() * 0.001;
                        double var57 = var24 + 0.5F - var53.s;
                        double var58 = var23 + 0.5F - var53.u;
                        float var59 = MathHelper.sqrt_double(var57 * var57 + var58 * var58) / var19;
                        float var48 = ((1.0F - var59 * var59) * 0.3F + 0.5F) * var52;
                        var22.set(var24, var34, var23);
                        int var49 = (var6.getCombinedLight(var22, 0) * 3 + 15728880) / 4;
                        int var50 = var49 >> 16 & 65535;
                        int var51 = var49 & 65535;
                        var11.pos(var24 - var26 + 0.5, var32, var23 - var28 + 0.5)
                           .tex(0.0 + var55, var32 * 0.25 + var54 + var56)
                           .color(1.0F, 1.0F, 1.0F, var48)
                           .lightmap(var50, var51)
                           .endVertex();
                        var11.pos(var24 + var26 + 0.5, var32, var23 + var28 + 0.5)
                           .tex(1.0 + var55, var32 * 0.25 + var54 + var56)
                           .color(1.0F, 1.0F, 1.0F, var48)
                           .lightmap(var50, var51)
                           .endVertex();
                        var11.pos(var24 + var26 + 0.5, var33, var23 + var28 + 0.5)
                           .tex(1.0 + var55, var33 * 0.25 + var54 + var56)
                           .color(1.0F, 1.0F, 1.0F, var48)
                           .lightmap(var50, var51)
                           .endVertex();
                        var11.pos(var24 - var26 + 0.5, var33, var23 - var28 + 0.5)
                           .tex(0.0 + var55, var33 * 0.25 + var54 + var56)
                           .color(1.0F, 1.0F, 1.0F, var48)
                           .lightmap(var50, var51)
                           .endVertex();
                     }
                  }
               }
            }
         }

         if (var20 >= 0) {
            var10.draw();
         }

         var11.setTranslation(0.0, 0.0, 0.0);
         GlStateManager.enableCull();
         GlStateManager.disableBlend();
         GlStateManager.alphaFunc(516, 0.1F);
         this.disableLightmap();
      }
   }

   public void disableLightmap() {
      GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
      GlStateManager.disableTexture2D();
      GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
      if (Config.isShaders()) {
         Shaders.disableLightmap();
      }
   }

   public void setupOverlayRendering() {
      ScaledResolution var1 = new ScaledResolution(this.mc);
      GlStateManager.clear(256);
      GlStateManager.matrixMode(5889);
      GlStateManager.loadIdentity();
      GlStateManager.ortho(0.0, var1.getScaledWidth_double(), var1.getScaledHeight_double(), 0.0, 1000.0, 3000.0);
      GlStateManager.matrixMode(5888);
      GlStateManager.loadIdentity();
      GlStateManager.translate(0.0F, 0.0F, -2000.0F);
   }

   public void renderHand(float var1, int var2, boolean var3, boolean var4, boolean var5) {
      if (!this.recoveredField3152) {
         GlStateManager.matrixMode(5889);
         GlStateManager.loadIdentity();
         float var6 = 0.07F;
         if (this.mc.gameSettings.anaglyph) {
            GlStateManager.translate(-(var2 * 2 - 1) * var6, 0.0F, 0.0F);
         }

         if (Config.isShaders()) {
            Shaders.applyHandDepth();
         }

         Project.gluPerspective(this.getFOVModifier(var1, false), (float)this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.farPlaneDistance * 2.0F);
         GlStateManager.matrixMode(5888);
         GlStateManager.loadIdentity();
         if (this.mc.gameSettings.anaglyph) {
            GlStateManager.translate((var2 * 2 - 1) * 0.1F, 0.0F, 0.0F);
         }

         boolean var7 = false;
         PerspectiveModule var8 = CheatBreaker.getInstance().getModuleManager().recoveredField1723;
         if (var3) {
            GlStateManager.pushMatrix();
            this.method_29171(var1);
            if (this.mc.gameSettings.viewBobbing) {
               if (var8.isEnabled()) {
                  var8.method_24319(var1, var8.recoveredField3186);
               } else {
                  this.setupViewBobbing(var1);
               }
            }

            var7 = this.mc.getRenderViewEntity() instanceof EntityLivingBase && ((EntityLivingBase)this.mc.getRenderViewEntity()).bJ();
            boolean var9 = !ReflectorForge.renderFirstPersonHand(this.mc.renderGlobal, var1, var2);
            if (var9 && this.mc.gameSettings.thirdPersonView == 0 && !var7 && !this.mc.gameSettings.hideGUI && !this.mc.playerController.method_19872()) {
               this.enableLightmap();
               if (Config.isShaders()) {
                  ShadersRender.renderItemFP(this.itemRenderer, var1, var5);
               } else {
                  this.itemRenderer.renderItemInFirstPerson(var1);
               }

               this.disableLightmap();
            }

            GlStateManager.popMatrix();
         }

         if (!var4) {
            return;
         }

         this.disableLightmap();
         if (this.mc.gameSettings.thirdPersonView == 0 && !var7) {
            this.itemRenderer.method_25646(var1);
            this.method_29171(var1);
         }

         if (this.mc.gameSettings.viewBobbing) {
            if (var8.isEnabled()) {
               var8.method_24319(var1, var8.recoveredField3186);
            } else {
               this.setupViewBobbing(var1);
            }
         }
      }
   }

   public boolean isDrawBlockOutline() {
      if (!this.drawBlockOutline) {
         return false;
      } else {
         Entity var1 = this.mc.getRenderViewEntity();
         boolean var2 = var1 instanceof EntityPlayer && !this.mc.gameSettings.hideGUI;
         if (var2 && !((EntityPlayer)var1).bA.allowEdit) {
            ItemStack var3 = ((EntityPlayer)var1).getCurrentEquippedItem();
            if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
               BlockPos var4 = this.mc.objectMouseOver.getBlockPos();
               IBlockState var5 = this.mc.theWorld.getBlockState(var4);
               Block var6 = var5.getBlock();
               if (this.mc.playerController.getCurrentGameType() == WorldSettings.GameType.SPECTATOR) {
                  var2 = ReflectorForge.blockHasTileEntity(var5) && this.mc.theWorld.getTileEntity(var4) instanceof IInventory;
               } else {
                  var2 = var3 != null && (var3.canDestroy(var6) || var3.canPlaceOn(var6));
               }
            }
         }

         return var2;
      }
   }

   public void method_29174(float var1) {
      Entity var2 = this.mc.getRenderViewEntity();
      float var3 = this.mc.getRenderViewEntity() != this.mc.thePlayer ? var2.getEyeHeight() : EyeHeightAnimationController.method_20195().method_20196(var1);
      double var4 = var2.p + (var2.s - var2.p) * var1;
      double var6 = var2.q + (var2.t - var2.q) * var1 + var3;
      double var8 = var2.r + (var2.u - var2.r) * var1;
      if (var2 instanceof EntityLivingBase && ((EntityLivingBase)var2).bJ()) {
         var3 = (float)(var3 + 1.0);
         GlStateManager.translate(0.0F, 0.3F, 0.0F);
         if (!this.mc.gameSettings.recoveredField2675) {
            BlockPos var30 = new BlockPos(var2);
            IBlockState var11 = this.mc.theWorld.getBlockState(var30);
            Block var36 = var11.getBlock();
            if (Reflector.ForgeHooksClient_orientBedCamera.exists()) {
               Reflector.callVoid(Reflector.ForgeHooksClient_orientBedCamera, this.mc.theWorld, var30, var11, var2);
            } else if (var36 == Blocks.bed) {
               int var39 = var11.getValue(BlockBed.O).getHorizontalIndex();
               GlStateManager.rotate(var39 * 90, 0.0F, 1.0F, 0.0F);
            }

            GlStateManager.rotate(var2.A + (var2.y - var2.A) * var1 + 180.0F, 0.0F, -1.0F, 0.0F);
            GlStateManager.rotate(var2.B + (var2.z - var2.B) * var1, -1.0F, 0.0F, 0.0F);
         }
      } else if (this.mc.gameSettings.thirdPersonView > 0) {
         double var10 = this.thirdPersonDistanceTemp + (this.thirdPersonDistance - this.thirdPersonDistanceTemp) * var1;
         if (this.mc.gameSettings.recoveredField2675) {
            GlStateManager.translate(0.0F, 0.0F, (float)(-var10));
         } else {
            float var12 = var2.y;
            float var13 = var2.z;
            if (this.mc.gameSettings.thirdPersonView == 2) {
               var13 += 180.0F;
            }

            double var14 = -MathHelper.sin(var12 / 180.0F * (float) Math.PI) * MathHelper.cos(var13 / 180.0F * (float) Math.PI) * var10;
            double var16 = MathHelper.cos(var12 / 180.0F * (float) Math.PI) * MathHelper.cos(var13 / 180.0F * (float) Math.PI) * var10;
            double var18 = -MathHelper.sin(var13 / 180.0F * (float) Math.PI) * var10;

            for (int var20 = 0; var20 < 8; var20++) {
               float var21 = (var20 & 1) * 2 - 1;
               float var22 = (var20 >> 1 & 1) * 2 - 1;
               float var23 = (var20 >> 2 & 1) * 2 - 1;
               var21 *= 0.1F;
               var22 *= 0.1F;
               var23 *= 0.1F;
               MovingObjectPosition var24 = this.mc
                  .theWorld
                  .rayTraceBlocks(
                     new Vec3(var4 + var21, var6 + var22, var8 + var23), new Vec3(var4 - var14 + var21 + var23, var6 - var18 + var22, var8 - var16 + var23)
                  );
               if (var24 != null) {
                  double var25 = var24.hitVec.distanceTo(new Vec3(var4, var6, var8));
                  if (var25 < var10) {
                     var10 = var25;
                  }
               }
            }

            if (this.mc.gameSettings.thirdPersonView == 2) {
               GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
            }

            GlStateManager.rotate(var2.z - var13, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(var2.y - var12, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(0.0F, 0.0F, (float)(-var10));
            GlStateManager.rotate(var12 - var2.y, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(var13 - var2.z, 1.0F, 0.0F, 0.0F);
         }
      } else {
         GlStateManager.translate(0.0F, 0.0F, -0.1F);
      }

      if (Reflector.EntityViewRenderEvent_CameraSetup_Constructor.exists()) {
         if (!this.mc.gameSettings.recoveredField2675) {
            float var31 = var2.A + (var2.y - var2.A) * var1 + 180.0F;
            float var34 = var2.B + (var2.z - var2.B) * var1;
            float var37 = 0.0F;
            if (var2 instanceof EntityAnimal) {
               EntityAnimal var40 = (EntityAnimal)var2;
               var31 = var40.prevRotationYawHead + (var40.aK - var40.prevRotationYawHead) * var1 + 180.0F;
            }

            Block var41 = ActiveRenderInfo.getBlockAtEntityViewpoint(this.mc.theWorld, var2, var1);
            Object var42 = Reflector.newInstance(Reflector.EntityViewRenderEvent_CameraSetup_Constructor, this, var2, var41, var1, var31, var34, var37);
            Reflector.postForgeBusEvent(var42);
            var37 = Reflector.getFieldValueFloat(var42, Reflector.EntityViewRenderEvent_CameraSetup_roll, var37);
            var34 = Reflector.getFieldValueFloat(var42, Reflector.EntityViewRenderEvent_CameraSetup_pitch, var34);
            var31 = Reflector.getFieldValueFloat(var42, Reflector.EntityViewRenderEvent_CameraSetup_yaw, var31);
            GlStateManager.rotate(var37, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(var34, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(var31, 0.0F, 1.0F, 0.0F);
         }
      } else if (!this.mc.gameSettings.recoveredField2675) {
         GlStateManager.rotate(var2.B + (var2.z - var2.B) * var1, 1.0F, 0.0F, 0.0F);
         if (var2 instanceof EntityAnimal) {
            EntityAnimal var33 = (EntityAnimal)var2;
            GlStateManager.rotate(var33.prevRotationYawHead + (var33.aK - var33.prevRotationYawHead) * var1 + 180.0F, 0.0F, 1.0F, 0.0F);
         } else {
            GlStateManager.rotate(var2.A + (var2.y - var2.A) * var1 + 180.0F, 0.0F, 1.0F, 0.0F);
         }
      }

      GlStateManager.translate(0.0F, -var3, 0.0F);
      var4 = var2.p + (var2.s - var2.p) * var1;
      var6 = var2.q + (var2.t - var2.q) * var1 + var3;
      var8 = var2.r + (var2.u - var2.r) * var1;
      this.cloudFog = this.mc.renderGlobal.hasCloudFog(var4, var6, var8, var1);
   }

   public void updateFovModifierHand() {
      float var1 = 1.0F;
      if (this.mc.getRenderViewEntity() instanceof AbstractClientPlayer) {
         AbstractClientPlayer var2 = (AbstractClientPlayer)this.mc.getRenderViewEntity();
         var1 = var2.getFovModifier();
      }

      this.fovModifierHandPrev = this.fovModifierHand;
      this.fovModifierHand = this.fovModifierHand + (var1 - this.fovModifierHand) * 0.5F;
      if (this.fovModifierHand > 1.5F) {
         this.fovModifierHand = 1.5F;
      }

      if (this.fovModifierHand < 0.1F) {
         this.fovModifierHand = 0.1F;
      }
   }

   public void setupViewBobbing(float var1) {
      if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
         EntityPlayer var2 = (EntityPlayer)this.mc.getRenderViewEntity();
         float var3 = var2.M - var2.L;
         float var4 = -(var2.M + var3 * var1);
         float var5 = var2.prevCameraYaw + (var2.cameraYaw - var2.prevCameraYaw) * var1;
         float var6 = var2.aE + (var2.aF - var2.aE) * var1;
         GlStateManager.translate(MathHelper.sin(var4 * (float) Math.PI) * var5 * 0.5F, -Math.abs(MathHelper.cos(var4 * (float) Math.PI) * var5), 0.0F);
         GlStateManager.rotate(MathHelper.sin(var4 * (float) Math.PI) * var5 * 3.0F, 0.0F, 0.0F, 1.0F);
         GlStateManager.rotate(Math.abs(MathHelper.cos(var4 * (float) Math.PI - 0.2F) * var5) * 5.0F, 1.0F, 0.0F, 0.0F);
         GlStateManager.rotate(var6, 1.0F, 0.0F, 0.0F);
      }
   }

   public void updateMainMenu(GuiMainMenu var1) {
      try {
         String var2 = null;
         Calendar var3 = Calendar.getInstance();
         var3.setTime(new Date());
         int var4 = var3.get(5);
         int var5 = var3.get(2) + 1;
         if (var4 == 8 && var5 == 4) {
            var2 = "Happy birthday, OptiFine!";
         }

         if (var4 == 14 && var5 == 8) {
            var2 = "Happy birthday, sp614x!";
         }

         if (var2 == null) {
            return;
         }

         Reflector.setFieldValue(var1, Reflector.GuiMainMenu_splashText, var2);
      } catch (Throwable var6) {
      }
   }

   public void renderWorldPass(int var1, float var2, long var3) {
      boolean var5 = Config.isShaders();
      if (var5) {
         Shaders.beginRenderPass(var1, var2, var3);
      }

      RenderGlobal var6 = this.mc.renderGlobal;
      EffectRenderer var7 = this.mc.effectRenderer;
      boolean var8 = this.isDrawBlockOutline();
      GlStateManager.enableCull();
      this.mc.mcProfiler.endStartSection("clear");
      if (var5) {
         Shaders.setViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
      } else {
         GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
      }

      this.updateFogColor(var2);
      GlStateManager.clear(16640);
      if (var5) {
         Shaders.method_02190();
      }

      this.mc.mcProfiler.endStartSection("camera");
      this.setupCameraTransform(var2, var1);
      if (var5) {
         Shaders.setCamera(var2);
      }

      ActiveRenderInfo.updateRenderInfo(this.mc.thePlayer, this.mc.gameSettings.thirdPersonView == 2);
      this.mc.mcProfiler.endStartSection("frustum");
      ClippingHelper var9 = ClippingHelperImpl.getInstance();
      this.mc.mcProfiler.endStartSection("culling");
      var9.disabled = Config.isShaders() && !Shaders.method_02300();
      Frustum var10 = new Frustum(var9);
      Entity var11 = this.mc.getRenderViewEntity();
      double var12 = var11.P + (var11.s - var11.P) * var2;
      double var14 = var11.Q + (var11.t - var11.Q) * var2;
      double var16 = var11.R + (var11.u - var11.R) * var2;
      if (var5) {
         ShadersRender.setFrustrumPosition(var10, var12, var14, var16);
      } else {
         var10.setPosition(var12, var14, var16);
      }

      if ((Config.isSkyEnabled() || Config.isSunMoonEnabled() || Config.isStarsEnabled()) && !Shaders.isShadowPass) {
         this.setupFog(-1, var2);
         this.mc.mcProfiler.endStartSection("sky");
         GlStateManager.matrixMode(5889);
         GlStateManager.loadIdentity();
         Project.gluPerspective(this.getFOVModifier(var2, true), (float)this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.clipDistance);
         GlStateManager.matrixMode(5888);
         if (var5) {
            Shaders.beginSky();
         }

         var6.renderSky(var2, var1);
         if (var5) {
            Shaders.endSky();
         }

         GlStateManager.matrixMode(5889);
         GlStateManager.loadIdentity();
         Project.gluPerspective(this.getFOVModifier(var2, true), (float)this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.clipDistance);
         GlStateManager.matrixMode(5888);
      } else {
         GlStateManager.disableBlend();
      }

      this.setupFog(0, var2);
      GlStateManager.shadeModel(7425);
      if (var11.t + var11.getEyeHeight() < 128.0 + this.mc.gameSettings.ofCloudsHeight * 128.0F) {
         this.renderCloudsCheck(var6, var2, var1);
      }

      this.mc.mcProfiler.endStartSection("prepareterrain");
      this.setupFog(0, var2);
      this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
      RenderHelper.disableStandardItemLighting();
      this.mc.mcProfiler.endStartSection("terrain_setup");
      this.checkLoadVisibleChunks(var11, var2, var10, this.mc.thePlayer.isSpectator());
      if (var5) {
         ShadersRender.setupTerrain(var6, var11, var2, var10, this.frameCount++, this.mc.thePlayer.isSpectator());
      } else {
         var6.setupTerrain(var11, var2, var10, this.frameCount++, this.mc.thePlayer.isSpectator());
      }

      if (var1 == 0 || var1 == 2) {
         this.mc.mcProfiler.endStartSection("updatechunks");
         Lagometer.recoveredField945.method_30327();
         this.mc.renderGlobal.updateChunks(var3);
         Lagometer.recoveredField945.method_30328();
      }

      this.mc.mcProfiler.endStartSection("terrain");
      Lagometer.recoveredField944.method_30327();
      if (this.mc.gameSettings.ofSmoothFps && var1 > 0) {
         this.mc.mcProfiler.endStartSection("finish");
         GL11.glFinish();
         this.mc.mcProfiler.endStartSection("terrain");
      }

      GlStateManager.matrixMode(5888);
      GlStateManager.pushMatrix();
      GlStateManager.disableAlpha();
      if (var5) {
         ShadersRender.beginTerrainSolid();
      }

      var6.renderBlockLayer(EnumWorldBlockLayer.SOLID, var2, var1, var11);
      GlStateManager.enableAlpha();
      if (var5) {
         ShadersRender.method_06601();
      }

      this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, this.mc.gameSettings.mipmapLevels > 0);
      var6.renderBlockLayer(EnumWorldBlockLayer.CUTOUT_MIPPED, var2, var1, var11);
      this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
      this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
      if (var5) {
         ShadersRender.method_06616();
      }

      var6.renderBlockLayer(EnumWorldBlockLayer.CUTOUT, var2, var1, var11);
      this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
      if (var5) {
         ShadersRender.method_06625();
      }

      Lagometer.recoveredField944.method_30328();
      GlStateManager.shadeModel(7424);
      GlStateManager.alphaFunc(516, 0.1F);
      if (!this.recoveredField3152) {
         GlStateManager.matrixMode(5888);
         GlStateManager.popMatrix();
         GlStateManager.pushMatrix();
         RenderHelper.enableStandardItemLighting();
         this.mc.mcProfiler.endStartSection("entities");
         if (Reflector.ForgeHooksClient_setRenderPass.exists()) {
            Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, 0);
         }

         var6.renderEntities(var11, var10, var2);
         if (Reflector.ForgeHooksClient_setRenderPass.exists()) {
            Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, -1);
         }

         RenderHelper.disableStandardItemLighting();
         this.disableLightmap();
         GlStateManager.matrixMode(5888);
         GlStateManager.popMatrix();
         GlStateManager.pushMatrix();
         if (this.mc.objectMouseOver != null && var11.a(Material.water) && var8) {
            EntityPlayer var18 = (EntityPlayer)var11;
            GlStateManager.disableAlpha();
            this.mc.mcProfiler.endStartSection("outline");
            if (CheatBreaker.getInstance().getModuleManager().recoveredField1696.isEnabled()) {
               CheatBreaker.getInstance().getModuleManager().recoveredField1696.method_12508(var18, this.mc.objectMouseOver, 0, var2, this.mc.theWorld);
            } else {
               var6.drawSelectionBox(var18, this.mc.objectMouseOver, 0, var2);
            }

            GlStateManager.enableAlpha();
         }
      }

      GlStateManager.matrixMode(5888);
      GlStateManager.popMatrix();
      if (var8 && this.mc.objectMouseOver != null && !var11.a(Material.water)) {
         EntityPlayer var19 = (EntityPlayer)var11;
         GlStateManager.disableAlpha();
         this.mc.mcProfiler.endStartSection("outline");
         if ((
               !Reflector.ForgeHooksClient_onDrawBlockHighlight.exists()
                  || !Reflector.callBoolean(Reflector.ForgeHooksClient_onDrawBlockHighlight, var6, var19, this.mc.objectMouseOver, 0, var19.getHeldItem(), var2)
            )
            && !this.mc.gameSettings.hideGUI) {
            if (CheatBreaker.getInstance().getModuleManager().recoveredField1696.isEnabled()) {
               CheatBreaker.getInstance().getModuleManager().recoveredField1696.method_12508(var19, this.mc.objectMouseOver, 0, var2, this.mc.theWorld);
            } else {
               var6.drawSelectionBox(var19, this.mc.objectMouseOver, 0, var2);
            }
         }

         GlStateManager.enableAlpha();
      }

      if (!var6.damagedBlocks.isEmpty()) {
         this.mc.mcProfiler.endStartSection("destroyProgress");
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
         this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
         var6.drawBlockDamageTexture(Tessellator.getInstance(), Tessellator.getInstance().getWorldRenderer(), var11, var2);
         this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
         GlStateManager.disableBlend();
      }

      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.disableBlend();
      if (!this.recoveredField3152) {
         this.enableLightmap();
         this.mc.mcProfiler.endStartSection("litParticles");
         if (var5) {
            Shaders.method_02182();
         }

         var7.renderLitParticles(var11, var2);
         RenderHelper.disableStandardItemLighting();
         this.setupFog(0, var2);
         this.mc.mcProfiler.endStartSection("particles");
         if (var5) {
            Shaders.beginParticles();
         }

         var7.renderParticles(var11, var2);
         if (var5) {
            Shaders.method_02158();
         }

         this.disableLightmap();
      }

      GlStateManager.depthMask(false);
      if (Config.isShaders()) {
         GlStateManager.depthMask(Shaders.method_02291());
      }

      GlStateManager.enableCull();
      this.mc.mcProfiler.endStartSection("weather");
      if (var5) {
         Shaders.beginWeather();
      }

      this.method_29146(var2);
      if (var5) {
         Shaders.endWeather();
      }

      GlStateManager.depthMask(true);
      var6.renderWorldBorder(var11, var2);
      if (var5) {
         ShadersRender.renderHand0(this, var2, var1);
         Shaders.preWater();
      }

      GlStateManager.disableBlend();
      GlStateManager.enableCull();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.alphaFunc(516, 0.1F);
      this.setupFog(0, var2);
      GlStateManager.enableBlend();
      GlStateManager.depthMask(false);
      this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
      GlStateManager.shadeModel(7425);
      this.mc.mcProfiler.endStartSection("translucent");
      if (var5) {
         Shaders.beginWater();
      }

      var6.renderBlockLayer(EnumWorldBlockLayer.TRANSLUCENT, var2, var1, var11);
      if (var5) {
         Shaders.endWater();
      }

      if (Reflector.ForgeHooksClient_setRenderPass.exists() && !this.recoveredField3152) {
         RenderHelper.enableStandardItemLighting();
         this.mc.mcProfiler.endStartSection("entities");
         Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, 1);
         this.mc.renderGlobal.renderEntities(var11, var10, var2);
         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
         Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, -1);
         RenderHelper.disableStandardItemLighting();
      }

      GlStateManager.shadeModel(7424);
      GlStateManager.depthMask(true);
      GlStateManager.enableCull();
      GlStateManager.disableBlend();
      GlStateManager.disableFog();
      if (var11.t + var11.getEyeHeight() >= 128.0 + this.mc.gameSettings.ofCloudsHeight * 128.0F) {
         this.mc.mcProfiler.endStartSection("aboveClouds");
         this.renderCloudsCheck(var6, var2, var1);
      }

      if (Reflector.ForgeHooksClient_dispatchRenderLast.exists()) {
         this.mc.mcProfiler.endStartSection("forge_render_last");
         Reflector.callVoid(Reflector.ForgeHooksClient_dispatchRenderLast, var6, var2);
      }

      this.mc.mcProfiler.endStartSection("hand");
      if (this.recoveredField3144 && !Shaders.isShadowPass) {
         if (var5) {
            ShadersRender.renderHand1(this, var2, var1);
            Shaders.method_02178();
         }

         GlStateManager.clear(256);
         if (var5) {
            ShadersRender.renderFPOverlay(this, var2, var1);
         } else {
            this.renderHand(var2, var1);
         }

         this.method_29178(var2);
      }

      if (var5) {
         Shaders.method_02324();
      }
   }

   public float getFOVModifier(float var1, boolean var2) {
      if (this.recoveredField3152) {
         return 90.0F;
      } else {
         Entity var3 = this.mc.getRenderViewEntity();
         float var4 = 70.0F;
         if (var2) {
            var4 = this.mc.gameSettings.fovSetting;
            if (Config.isDynamicFov()) {
               var4 *= this.fovModifierHandPrev + (this.fovModifierHand - this.fovModifierHandPrev) * var1;
            }
         }

         boolean var5 = false;
         if (this.mc.currentScreen == null) {
            var5 = GameSettings.isKeyDown(this.mc.gameSettings.ofKeyBindZoom);
         }

         if (var5) {
            if (!Config.zoomMode) {
               Config.zoomMode = true;
               Config.zoomSmoothCamera = this.mc.gameSettings.smoothCamera;
               this.mc.gameSettings.smoothCamera = true;
               this.mc.renderGlobal.displayListEntitiesDirty = true;
            }

            if (Config.zoomMode) {
               var4 /= 4.0F;
            }
         } else if (Config.zoomMode) {
            Config.zoomMode = false;
            this.mc.gameSettings.smoothCamera = Config.zoomSmoothCamera;
            this.mouseFilterXAxis = new MouseFilter();
            this.mouseFilterYAxis = new MouseFilter();
            this.mc.renderGlobal.displayListEntitiesDirty = true;
         }

         if (var3 instanceof EntityLivingBase && ((EntityLivingBase)var3).getHealth() <= 0.0F) {
            float var6 = ((EntityLivingBase)var3).ax + var1;
            var4 /= (1.0F - 500.0F / (var6 + 500.0F)) * 2.0F + 1.0F;
         }

         Block var7 = ActiveRenderInfo.getBlockAtEntityViewpoint(this.mc.theWorld, var3, var1);
         if (var7.getMaterial() == Material.water) {
            var4 = var4 * 60.0F / 70.0F;
         }

         return Reflector.ForgeHooksClient_getFOVModifier.exists()
            ? Reflector.callFloat(Reflector.ForgeHooksClient_getFOVModifier, this, var3, var7, var1, var4)
            : var4;
      }
   }

   public void method_29171(float var1) {
      HurtcamModule hurtcam = CheatBreaker.getInstance().getModuleManager().hurtcam;
      boolean customHurtcam = hurtcam != null && hurtcam.isEnabled();
      boolean var3 = customHurtcam && !hurtcam.damageAffectsCamera.method_08908() && !hurtcam.method_28796();
      float hurtAngle = customHurtcam ? hurtcam.hurtCameraIntensity.method_08905() : 14.0F;
      if (!var3) {
         if (this.mc.getRenderViewEntity() instanceof EntityLivingBase) {
            EntityLivingBase var5 = (EntityLivingBase)this.mc.getRenderViewEntity();
            float var6 = var5.au - var1;
            if (var5.getHealth() <= 0.0F) {
               float var7 = var5.ax + var1;
               GlStateManager.rotate(40.0F - 8000.0F / (var7 + 200.0F), 0.0F, 0.0F, 1.0F);
            }

            if (var6 < 0.0F || hurtAngle == 0.0F) {
               return;
            }

            var6 /= var5.av;
            var6 = MathHelper.sin(var6 * var6 * var6 * var6 * (float) Math.PI);
            float var10 = var5.aw;
            GlStateManager.rotate(-var10, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-var6 * hurtAngle, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(var10, 0.0F, 1.0F, 0.0F);
         }
      }
   }

   public void updateFogColor(float var1) {
      WorldClient var2 = this.mc.theWorld;
      Entity var3 = this.mc.getRenderViewEntity();
      float var4 = 0.25F + 0.75F * this.mc.gameSettings.renderDistanceChunks / 32.0F;
      var4 = 1.0F - (float)Math.pow(var4, 0.25);
      Vec3 var5 = var2.getSkyColor(this.mc.getRenderViewEntity(), var1);
      var5 = CustomColors.getWorldSkyColor(var5, var2, this.mc.getRenderViewEntity(), var1);
      float var6 = (float)var5.xCoord;
      float var7 = (float)var5.yCoord;
      float var8 = (float)var5.zCoord;
      Vec3 var9 = var2.getFogColor(var1);
      var9 = CustomColors.getWorldFogColor(var9, var2, this.mc.getRenderViewEntity(), var1);
      this.fogColorRed = (float)var9.xCoord;
      this.fogColorGreen = (float)var9.yCoord;
      this.fogColorBlue = (float)var9.zCoord;
      if (this.mc.gameSettings.renderDistanceChunks >= 4) {
         double var10 = -1.0;
         Vec3 var12 = MathHelper.sin(var2.getCelestialAngleRadians(var1)) > 0.0F ? new Vec3(var10, 0.0, 0.0) : new Vec3(1.0, 0.0, 0.0);
         float var13 = (float)var3.getLook(var1).dotProduct(var12);
         if (var13 < 0.0F) {
            var13 = 0.0F;
         }

         if (var13 > 0.0F) {
            float[] var14 = var2.t.calcSunriseSunsetColors(var2.getCelestialAngle(var1), var1);
            if (var14 != null) {
               var13 *= var14[3];
               this.fogColorRed = this.fogColorRed * (1.0F - var13) + var14[0] * var13;
               this.fogColorGreen = this.fogColorGreen * (1.0F - var13) + var14[1] * var13;
               this.fogColorBlue = this.fogColorBlue * (1.0F - var13) + var14[2] * var13;
            }
         }
      }

      this.fogColorRed = this.fogColorRed + (var6 - this.fogColorRed) * var4;
      this.fogColorGreen = this.fogColorGreen + (var7 - this.fogColorGreen) * var4;
      this.fogColorBlue = this.fogColorBlue + (var8 - this.fogColorBlue) * var4;
      float var22 = var2.j(var1);
      if (var22 > 0.0F) {
         float var11 = 1.0F - var22 * 0.5F;
         float var24 = 1.0F - var22 * 0.4F;
         this.fogColorRed *= var11;
         this.fogColorGreen *= var11;
         this.fogColorBlue *= var24;
      }

      float var23 = var2.h(var1);
      if (var23 > 0.0F) {
         float var25 = 1.0F - var23 * 0.5F;
         this.fogColorRed *= var25;
         this.fogColorGreen *= var25;
         this.fogColorBlue *= var25;
      }

      Block var26 = ActiveRenderInfo.getBlockAtEntityViewpoint(this.mc.theWorld, var3, var1);
      if (this.cloudFog) {
         Vec3 var28 = var2.getCloudColour(var1);
         this.fogColorRed = (float)var28.xCoord;
         this.fogColorGreen = (float)var28.yCoord;
         this.fogColorBlue = (float)var28.zCoord;
      } else if (var26.getMaterial() == Material.water) {
         float var29 = EnchantmentHelper.getRespiration(var3) * 0.2F;
         var29 = Config.limit(var29, 0.0F, 0.6F);
         if (var3 instanceof EntityLivingBase && ((EntityLivingBase)var3).isPotionActive(Potion.waterBreathing)) {
            var29 = var29 * 0.3F + 0.6F;
         }

         this.fogColorRed = 0.02F + var29;
         this.fogColorGreen = 0.02F + var29;
         this.fogColorBlue = 0.2F + var29;
         Vec3 var33 = CustomColors.getUnderwaterColor(
            this.mc.theWorld, this.mc.getRenderViewEntity().s, this.mc.getRenderViewEntity().t + 1.0, this.mc.getRenderViewEntity().u
         );
         if (var33 != null) {
            this.fogColorRed = (float)var33.xCoord;
            this.fogColorGreen = (float)var33.yCoord;
            this.fogColorBlue = (float)var33.zCoord;
         }
      } else if (var26.getMaterial() == Material.lava) {
         this.fogColorRed = 0.6F;
         this.fogColorGreen = 0.1F;
         this.fogColorBlue = 0.0F;
         Vec3 var31 = CustomColors.getUnderlavaColor(
            this.mc.theWorld, this.mc.getRenderViewEntity().s, this.mc.getRenderViewEntity().t + 1.0, this.mc.getRenderViewEntity().u
         );
         if (var31 != null) {
            this.fogColorRed = (float)var31.xCoord;
            this.fogColorGreen = (float)var31.yCoord;
            this.fogColorBlue = (float)var31.zCoord;
         }
      }

      float var32 = this.fogColor2 + (this.fogColor1 - this.fogColor2) * var1;
      this.fogColorRed *= var32;
      this.fogColorGreen *= var32;
      this.fogColorBlue *= var32;
      double var34 = (var3.Q + (var3.t - var3.Q) * var1) * var2.t.getVoidFogYFactor();
      if (var3 instanceof EntityLivingBase && ((EntityLivingBase)var3).isPotionActive(Potion.blindness)) {
         int var16 = ((EntityLivingBase)var3).getActivePotionEffect(Potion.blindness).getDuration();
         if (var16 < 20) {
            var34 *= 1.0F - var16 / 20.0F;
         } else {
            var34 = 0.0;
         }
      }

      if (var34 < 1.0) {
         if (var34 < 0.0) {
            var34 = 0.0;
         }

         var34 *= var34;
         this.fogColorRed = (float)(this.fogColorRed * var34);
         this.fogColorGreen = (float)(this.fogColorGreen * var34);
         this.fogColorBlue = (float)(this.fogColorBlue * var34);
      }

      if (this.bossColorModifier > 0.0F) {
         float var36 = this.bossColorModifierPrev + (this.bossColorModifier - this.bossColorModifierPrev) * var1;
         this.fogColorRed = this.fogColorRed * (1.0F - var36) + this.fogColorRed * 0.7F * var36;
         this.fogColorGreen = this.fogColorGreen * (1.0F - var36) + this.fogColorGreen * 0.6F * var36;
         this.fogColorBlue = this.fogColorBlue * (1.0F - var36) + this.fogColorBlue * 0.6F * var36;
      }

      if (var3 instanceof EntityLivingBase && ((EntityLivingBase)var3).isPotionActive(Potion.nightVision)) {
         float var37 = this.getNightVisionBrightness((EntityLivingBase)var3, var1);
         float var17 = 1.0F / this.fogColorRed;
         if (var17 > 1.0F / this.fogColorGreen) {
            var17 = 1.0F / this.fogColorGreen;
         }

         if (var17 > 1.0F / this.fogColorBlue) {
            var17 = 1.0F / this.fogColorBlue;
         }

         if (Float.isInfinite(var17)) {
            var17 = Math.nextAfter(var17, 0.0);
         }

         this.fogColorRed = this.fogColorRed * (1.0F - var37) + this.fogColorRed * var17 * var37;
         this.fogColorGreen = this.fogColorGreen * (1.0F - var37) + this.fogColorGreen * var17 * var37;
         this.fogColorBlue = this.fogColorBlue * (1.0F - var37) + this.fogColorBlue * var17 * var37;
      }

      if (this.mc.gameSettings.anaglyph) {
         float var38 = (this.fogColorRed * 30.0F + this.fogColorGreen * 59.0F + this.fogColorBlue * 11.0F) / 100.0F;
         float var40 = (this.fogColorRed * 30.0F + this.fogColorGreen * 70.0F) / 100.0F;
         float var18 = (this.fogColorRed * 30.0F + this.fogColorBlue * 70.0F) / 100.0F;
         this.fogColorRed = var38;
         this.fogColorGreen = var40;
         this.fogColorBlue = var18;
      }

      if (Reflector.EntityViewRenderEvent_FogColors_Constructor.exists()) {
         Object var39 = Reflector.newInstance(
            Reflector.EntityViewRenderEvent_FogColors_Constructor, this, var3, var26, var1, this.fogColorRed, this.fogColorGreen, this.fogColorBlue
         );
         Reflector.postForgeBusEvent(var39);
         this.fogColorRed = Reflector.getFieldValueFloat(var39, Reflector.EntityViewRenderEvent_FogColors_red, this.fogColorRed);
         this.fogColorGreen = Reflector.getFieldValueFloat(var39, Reflector.EntityViewRenderEvent_FogColors_green, this.fogColorGreen);
         this.fogColorBlue = Reflector.getFieldValueFloat(var39, Reflector.EntityViewRenderEvent_FogColors_blue, this.fogColorBlue);
      }

      Shaders.setClearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 0.0F);
   }

   public ShaderGroup getShaderGroup() {
      return this.theShaderGroup;
   }

   public void updateCameraAndRender(float var1, long var2) {
      Config.renderPartialTicks = var1;
      this.method_29145();
      boolean var4 = Display.isActive();
      if (!var4 && this.mc.gameSettings.recoveredField2704 && (!this.mc.gameSettings.touchscreen || !Mouse.isButtonDown(1))) {
         if (Minecraft.getSystemTime() - this.recoveredField3133 > 500L) {
            this.mc.method_20418();
         }
      } else {
         this.recoveredField3133 = Minecraft.getSystemTime();
      }

      this.mc.mcProfiler.startSection("mouse");
      if (var4 && Minecraft.isRunningOnMac && this.mc.recoveredField3812 && !Mouse.isInsideWindow()) {
         Mouse.setGrabbed(false);
         Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
         Mouse.setGrabbed(true);
      }

      if (this.mc.recoveredField3812 && var4) {
         this.mc.mouseHelper.mouseXYChange();
         float var5 = this.mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
         float var6 = var5 * var5 * var5 * 8.0F;
         float var7 = this.mc.mouseHelper.recoveredField1619 * var6;
         float var8 = this.mc.mouseHelper.recoveredField1620 * var6;
         byte var9 = 1;
         if (this.mc.gameSettings.invertMouse) {
            var9 = -1;
         }

         FreelookController var10 = CheatBreaker.getInstance().getModuleManager().recoveredField1728;
         if (!var10.recoveredField3503) {
            if (this.mc.gameSettings.smoothCamera) {
               this.smoothCamYaw += var7;
               this.smoothCamPitch += var8;
               float var11 = var1 - this.smoothCamPartialTicks;
               this.smoothCamPartialTicks = var1;
               var7 = this.smoothCamFilterX * var11;
               var8 = this.smoothCamFilterY * var11;
               this.mc.thePlayer.setAngles(var7, var8 * var9);
            } else {
               this.smoothCamYaw = 0.0F;
               this.smoothCamPitch = 0.0F;
               this.mc.thePlayer.setAngles(var7, var8 * var9);
            }
         } else if (this.mc.gameSettings.smoothCamera) {
            this.smoothCamYaw += var7;
            this.smoothCamPitch += var8;
            float var27 = var1 - this.smoothCamPartialTicks;
            this.smoothCamPartialTicks = var1;
            var7 = this.smoothCamFilterX * var27;
            var8 = this.smoothCamFilterY * var27;
            var10.method_23797(var7, var8 * var9);
         } else {
            this.smoothCamYaw = 0.0F;
            this.smoothCamPitch = 0.0F;
            var10.method_23797(var7, var8 * var9);
         }
      }

      this.mc.mcProfiler.endSection();
      if (!this.mc.recoveredField3836) {
         anaglyphEnable = this.mc.gameSettings.anaglyph;
         final ScaledResolution var17 = new ScaledResolution(this.mc);
         int var18 = var17.getScaledWidth();
         int var21 = var17.getScaledHeight();
         final int var24 = Mouse.getX() * var18 / this.mc.displayWidth;
         final int var25 = var21 - Mouse.getY() * var21 / this.mc.displayHeight - 1;
         int var26 = this.mc.gameSettings.limitFramerate;
         if (this.mc.theWorld != null) {
            this.mc.mcProfiler.startSection("level");
            int var28 = Math.min(Minecraft.getDebugFPS(), var26);
            var28 = Math.max(var28, 60);
            long var12 = System.nanoTime() - var2;
            long var14 = Math.max(1000000000 / var28 / 4 - var12, 0L);
            this.renderWorld(var1, System.nanoTime() + var14);
            if (OpenGlHelper.shadersSupported) {
               this.mc.renderGlobal.renderEntityOutlineFramebuffer();
               if (this.theShaderGroup != null && this.useShader) {
                  GlStateManager.matrixMode(5890);
                  GlStateManager.pushMatrix();
                  GlStateManager.loadIdentity();
                  this.theShaderGroup.loadShaderGroup(var1);
                  GlStateManager.popMatrix();
               }

               this.mc.getFramebuffer().bindFramebuffer(true);
            }

            this.recoveredField3155 = System.nanoTime();
            this.mc.mcProfiler.endStartSection("gui");
            if (!this.mc.gameSettings.hideGUI || this.mc.currentScreen != null) {
               GlStateManager.alphaFunc(516, 0.1F);
               this.mc.ingameGUI.renderGameOverlay(var1);
               if (this.mc.gameSettings.ofShowFps && !this.mc.gameSettings.recoveredField2681) {
                  Config.drawFps();
               }

               if (this.mc.gameSettings.recoveredField2681) {
                  Lagometer.showLagometer(var17);
               }
            }

            this.mc.mcProfiler.endSection();
         } else {
            GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode(5888);
            GlStateManager.loadIdentity();
            this.setupOverlayRendering();
            this.recoveredField3155 = System.nanoTime();
            TileEntityRendererDispatcher.instance.renderEngine = this.mc.getTextureManager();
            TileEntityRendererDispatcher.instance.fontRenderer = this.mc.fontRendererObj;
         }

         if (this.mc.currentScreen != null) {
            GlStateManager.clear(256);

            try {
               if (Reflector.ForgeHooksClient_drawScreen.exists()) {
                  Reflector.callVoid(Reflector.ForgeHooksClient_drawScreen, this.mc.currentScreen, var24, var25, var1);
               } else {
                  this.mc.currentScreen.drawScreen(var24, var25, var1);
               }
            } catch (Throwable var16) {
               CrashReport var30 = CrashReport.makeCrashReport(var16, "Rendering screen");
               CrashReportCategory var13 = var30.makeCategory("Screen render details");
               var13.addCrashSectionCallable("Screen name", new Callable<String>() {
                  public String call() throws java.lang.Exception {
                     return EntityRenderer.this.mc.currentScreen.getClass().getCanonicalName();
                  }
               });
               var13.addCrashSectionCallable("Mouse location", new Callable<String>() {
                  public String call() throws java.lang.Exception {
                     return String.format("Scaled: (%d, %d). Absolute: (%d, %d)", var24, var25, Mouse.getX(), Mouse.getY());
                  }
               });
               var13.addCrashSectionCallable(
                  "Screen size",
                  new Callable<String>() {
                     public String call() throws java.lang.Exception {
                        return String.format(
                           "Scaled: (%d, %d). Absolute: (%d, %d). Scale factor of %d",
                           var17.getScaledWidth(),
                           var17.getScaledHeight(),
                           EntityRenderer.this.mc.displayWidth,
                           EntityRenderer.this.mc.displayHeight,
                           var17.getScaleFactor()
                        );
                     }
                  }
               );
               throw new ReportedException(var30);
            }
         }
      }

      this.frameFinish();
      this.method_29169();
      MemoryMonitor.update();
      Lagometer.updateLagometer();
      if (this.mc.gameSettings.ofProfiler) {
         this.mc.gameSettings.recoveredField2689 = true;
      }
   }

   public void renderHand(float var1, int var2) {
      this.renderHand(var1, var2, true, true, false);
   }

   public void frameFinish() {
      if (this.mc.theWorld != null && Config.isShowGlErrors() && TimedEvent.isActive("CheckGlErrorFrameFinish", 10000L)) {
         int var1 = GlStateManager.glGetError();
         if (var1 != 0 && GlErrors.isEnabled(var1)) {
            String var2 = Config.getGlErrorString(var1);
            ChatComponentText var3 = new ChatComponentText(I18n.format("of.message.openglError", var1, var2));
            this.mc.ingameGUI.getChatGUI().printChatMessage(var3);
         }
      }
   }

   public void stopUseShader() {
      if (this.theShaderGroup != null) {
         this.theShaderGroup.deleteShaderGroup();
      }

      this.theShaderGroup = null;
      this.shaderIndex = shaderCount;
   }

   public boolean isShaderActive() {
      return OpenGlHelper.shadersSupported && this.theShaderGroup != null;
   }

   public void updateLightmap(float var1) {
      if (this.lightmapUpdateNeeded) {
         this.mc.mcProfiler.startSection("lightTex");
         WorldClient var2 = this.mc.theWorld;
         if (var2 != null) {
            if (Config.isCustomColors()
               && CustomColors.updateLightmap(var2, this.torchFlickerX, this.lightmapColors, this.mc.thePlayer.isPotionActive(Potion.nightVision), var1)) {
               this.lightmapTexture.updateDynamicTexture();
               this.lightmapUpdateNeeded = false;
               this.mc.mcProfiler.endSection();
               return;
            }

            float var3 = var2.getSunBrightness(1.0F);
            float var4 = var3 * 0.95F + 0.05F;

            for (int var5 = 0; var5 < 256; var5++) {
               float var6 = var2.t.getLightBrightnessTable()[var5 / 16] * var4;
               float var7 = var2.t.getLightBrightnessTable()[var5 % 16] * (this.torchFlickerX * 0.1F + 1.5F);
               if (var2.getLastLightningBolt() > 0) {
                  var6 = var2.t.getLightBrightnessTable()[var5 / 16];
               }

               float var8 = var6 * (var3 * 0.65F + 0.35F);
               float var9 = var6 * (var3 * 0.65F + 0.35F);
               float var10 = var7 * ((var7 * 0.6F + 0.4F) * 0.6F + 0.4F);
               float var11 = var7 * (var7 * var7 * 0.6F + 0.4F);
               float var12 = var8 + var7;
               float var13 = var9 + var10;
               float var14 = var6 + var11;
               var12 = var12 * 0.96F + 0.03F;
               var13 = var13 * 0.96F + 0.03F;
               var14 = var14 * 0.96F + 0.03F;
               if (this.bossColorModifier > 0.0F) {
                  float var15 = this.bossColorModifierPrev + (this.bossColorModifier - this.bossColorModifierPrev) * var1;
                  var12 = var12 * (1.0F - var15) + var12 * 0.7F * var15;
                  var13 = var13 * (1.0F - var15) + var13 * 0.6F * var15;
                  var14 = var14 * (1.0F - var15) + var14 * 0.6F * var15;
               }

               if (var2.t.getDimensionId() == 1) {
                  var12 = 0.22F + var7 * 0.75F;
                  var13 = 0.28F + var10 * 0.75F;
                  var14 = 0.25F + var11 * 0.75F;
               }

               if (this.mc.thePlayer.isPotionActive(Potion.nightVision)) {
                  float var32 = this.getNightVisionBrightness(this.mc.thePlayer, var1);
                  float var16 = 1.0F / var12;
                  if (var16 > 1.0F / var13) {
                     var16 = 1.0F / var13;
                  }

                  if (var16 > 1.0F / var14) {
                     var16 = 1.0F / var14;
                  }

                  var12 = var12 * (1.0F - var32) + var12 * var16 * var32;
                  var13 = var13 * (1.0F - var32) + var13 * var16 * var32;
                  var14 = var14 * (1.0F - var32) + var14 * var16 * var32;
               }

               if (var12 > 1.0F) {
                  var12 = 1.0F;
               }

               if (var13 > 1.0F) {
                  var13 = 1.0F;
               }

               if (var14 > 1.0F) {
                  var14 = 1.0F;
               }

               float var33 = this.mc.gameSettings.method_01290();
               float var34 = 1.0F - var12;
               float var17 = 1.0F - var13;
               float var18 = 1.0F - var14;
               var34 = 1.0F - var34 * var34 * var34 * var34;
               var17 = 1.0F - var17 * var17 * var17 * var17;
               var18 = 1.0F - var18 * var18 * var18 * var18;
               var12 = var12 * (1.0F - var33) + var34 * var33;
               var13 = var13 * (1.0F - var33) + var17 * var33;
               var14 = var14 * (1.0F - var33) + var18 * var33;
               var12 = var12 * 0.96F + 0.03F;
               var13 = var13 * 0.96F + 0.03F;
               var14 = var14 * 0.96F + 0.03F;
               if (var12 > 1.0F) {
                  var12 = 1.0F;
               }

               if (var13 > 1.0F) {
                  var13 = 1.0F;
               }

               if (var14 > 1.0F) {
                  var14 = 1.0F;
               }

               if (var12 < 0.0F) {
                  var12 = 0.0F;
               }

               if (var13 < 0.0F) {
                  var13 = 0.0F;
               }

               if (var14 < 0.0F) {
                  var14 = 0.0F;
               }

               short var19 = 255;
               int var20 = (int)(var12 * 255.0F);
               int var21 = (int)(var13 * 255.0F);
               int var22 = (int)(var14 * 255.0F);
               this.lightmapColors[var5] = var19 << 24 | var20 << 16 | var21 << 8 | var22;
            }

            this.lightmapTexture.updateDynamicTexture();
            this.lightmapUpdateNeeded = false;
            this.mc.mcProfiler.endSection();
         }
      }
   }

   public MapItemRenderer getMapItemRenderer() {
      return this.recoveredField3142;
   }

   public float getNightVisionBrightness(EntityLivingBase var1, float var2) {
      int var3 = var1.getActivePotionEffect(Potion.nightVision).getDuration();
      return var3 > 200 ? 1.0F : 0.7F + MathHelper.sin((var3 - var2) * (float) Math.PI * 0.2F) * 0.3F;
   }

   public void method_29178(float var1) {
      if (this.mc.gameSettings.recoveredField2681
         && !this.mc.gameSettings.hideGUI
         && !this.mc.thePlayer.hasReducedDebug()
         && !this.mc.gameSettings.reducedDebugInfo) {
         Entity var2 = this.mc.getRenderViewEntity();
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
         GL11.glLineWidth(1.0F);
         GlStateManager.disableTexture2D();
         GlStateManager.depthMask(false);
         GlStateManager.pushMatrix();
         GlStateManager.matrixMode(5888);
         GlStateManager.loadIdentity();
         if (CheatBreaker.getInstance().getModuleManager().recoveredField1728.recoveredField3503) {
            this.method_29168(var1);
         } else {
            this.method_29174(var1);
         }

         GlStateManager.translate(0.0F, var2.getEyeHeight(), 0.0F);
         RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(0.0, 0.0, 0.0, 0.005, 1.0E-4, 1.0E-4), 255, 0, 0, 255);
         RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(0.0, 0.0, 0.0, 1.0E-4, 1.0E-4, 0.005), 0, 0, 255, 255);
         RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(0.0, 0.0, 0.0, 1.0E-4, 0.0033, 1.0E-4), 0, 255, 0, 255);
         GlStateManager.popMatrix();
         GlStateManager.depthMask(true);
         GlStateManager.enableTexture2D();
         GlStateManager.disableBlend();
      }
   }

   public void method_29166() {
      if (OpenGlHelper.isFramebufferEnabled() && OpenGlHelper.shadersSupported) {
         if (this.theShaderGroup != null) {
            this.theShaderGroup.deleteShaderGroup();
         }

         ResourceLocation var1 = (Boolean)CheatBreaker.getInstance().getModuleManager().recoveredField1715.recoveredField2744.getValue()
            ? recoveredField3151
            : recoveredField3141;

         try {
            this.theShaderGroup = new ShaderGroup(this.mc.getTextureManager(), this.recoveredField3135, this.mc.getFramebuffer(), var1);
            this.theShaderGroup.createBindFramebuffers(this.mc.displayWidth, this.mc.displayHeight);
            this.useShader = true;
         } catch (Exception var3) {
         }
      }
   }

   public boolean setFxaaShader(int var1) {
      if (!OpenGlHelper.isFramebufferEnabled()) {
         return false;
      } else if (this.theShaderGroup != null && this.theShaderGroup != this.fxaaShaders[2] && this.theShaderGroup != this.fxaaShaders[4]) {
         return true;
      } else if (var1 != 2 && var1 != 4) {
         if (this.theShaderGroup == null) {
            return true;
         } else {
            this.theShaderGroup.deleteShaderGroup();
            this.theShaderGroup = null;
            return true;
         }
      } else if (this.theShaderGroup != null && this.theShaderGroup == this.fxaaShaders[var1]) {
         return true;
      } else if (this.mc.theWorld == null) {
         return true;
      } else {
         this.loadShader(new ResourceLocation("shaders/post/fxaa_of_" + var1 + "x.json"));
         this.fxaaShaders[var1] = this.theShaderGroup;
         return this.useShader;
      }
   }

   public void enableLightmap() {
      GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
      GlStateManager.matrixMode(5890);
      GlStateManager.loadIdentity();
      float var1 = 0.00390625F;
      GlStateManager.scale(var1, var1, var1);
      GlStateManager.translate(8.0F, 8.0F, 8.0F);
      GlStateManager.matrixMode(5888);
      this.mc.getTextureManager().bindTexture(this.locationLightMap);
      GL11.glTexParameteri(3553, 10241, 9729);
      GL11.glTexParameteri(3553, 10240, 9729);
      GL11.glTexParameteri(3553, 10242, 33071);
      GL11.glTexParameteri(3553, 10243, 33071);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.enableTexture2D();
      GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
      if (Config.isShaders()) {
         Shaders.enableLightmap();
      }
   }

   public void checkLoadVisibleChunks(Entity var1, float var2, ICamera var3, boolean var4) {
      int var5 = 201435902;
      if (this.loadVisibleChunks) {
         this.loadVisibleChunks = false;
         this.loadAllVisibleChunks(var1, var2, var3, var4);
         this.mc.ingameGUI.getChatGUI().deleteChatLine(var5);
      }

      if (Keyboard.isKeyDown(61) && Keyboard.isKeyDown(38)) {
         if (this.mc.currentScreen != null) {
            return;
         }

         this.loadVisibleChunks = true;
         ChatComponentText var6 = new ChatComponentText(I18n.format("of.message.loadingVisibleChunks"));
         this.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(var6, var5);
      }
   }

   public void loadShader(ResourceLocation var1) {
      if (OpenGlHelper.isFramebufferEnabled()) {
         try {
            this.theShaderGroup = new ShaderGroup(this.mc.getTextureManager(), this.recoveredField3135, this.mc.getFramebuffer(), var1);
            this.theShaderGroup.createBindFramebuffers(this.mc.displayWidth, this.mc.displayHeight);
            this.useShader = true;
         } catch (JsonSyntaxException | IOException var3) {
            recoveredField3134.warn("Failed to load shader: " + var1, var3);
            this.shaderIndex = shaderCount;
            this.useShader = false;
         }
      }
   }

   public void method_29168(float var1) {
      FreelookController var2 = CheatBreaker.getInstance().getModuleManager().recoveredField1728;
      Entity var3 = this.mc.getRenderViewEntity();
      float var4 = this.mc.getRenderViewEntity() != this.mc.thePlayer ? var3.getEyeHeight() : EyeHeightAnimationController.method_20195().method_20196(var1);
      double var5 = var3.p + (var3.s - var3.p) * var1;
      double var7 = var3.q + (var3.t - var3.q) * var1 + var4;
      double var9 = var3.r + (var3.u - var3.r) * var1;
      if (var3 instanceof EntityLivingBase && ((EntityLivingBase)var3).bJ()) {
         var4 = (float)(var4 + 1.0);
         GlStateManager.translate(0.0F, 0.3F, 0.0F);
         if (!this.mc.gameSettings.recoveredField2675) {
            BlockPos var31 = new BlockPos(var3);
            IBlockState var12 = this.mc.theWorld.getBlockState(var31);
            Block var35 = var12.getBlock();
            if (Reflector.ForgeHooksClient_orientBedCamera.exists()) {
               Reflector.callVoid(Reflector.ForgeHooksClient_orientBedCamera, this.mc.theWorld, var31, var12, var3);
            } else if (var35 == Blocks.bed) {
               int var37 = var12.getValue(BlockBed.O).getHorizontalIndex();
               GlStateManager.rotate(var37 * 90, 0.0F, 1.0F, 0.0F);
            }

            GlStateManager.rotate(var2.recoveredField3505 + (var2.recoveredField3508 - var2.recoveredField3505) * var1 + 180.0F, 0.0F, -1.0F, 0.0F);
            GlStateManager.rotate(var2.recoveredField3506 + (var2.recoveredField3509 - var2.recoveredField3506) * var1, -1.0F, 0.0F, 0.0F);
         }
      } else if (this.mc.gameSettings.thirdPersonView > 0) {
         double var11 = this.thirdPersonDistanceTemp + (this.thirdPersonDistance - this.thirdPersonDistanceTemp) * var1;
         if (this.mc.gameSettings.recoveredField2675) {
            GlStateManager.translate(0.0F, 0.0F, (float)(-var11));
         } else {
            float var13 = var2.recoveredField3508;
            float var14 = var2.recoveredField3509;
            if (this.mc.gameSettings.thirdPersonView == 2) {
               var14 += 180.0F;
            }

            double var15 = -MathHelper.sin(var13 / 180.0F * (float) Math.PI) * MathHelper.cos(var14 / 180.0F * (float) Math.PI) * var11;
            double var17 = MathHelper.cos(var13 / 180.0F * (float) Math.PI) * MathHelper.cos(var14 / 180.0F * (float) Math.PI) * var11;
            double var19 = -MathHelper.sin(var14 / 180.0F * (float) Math.PI) * var11;

            for (int var21 = 0; var21 < 8; var21++) {
               float var22 = (var21 & 1) * 2 - 1;
               float var23 = (var21 >> 1 & 1) * 2 - 1;
               float var24 = (var21 >> 2 & 1) * 2 - 1;
               var22 *= 0.1F;
               var23 *= 0.1F;
               var24 *= 0.1F;
               MovingObjectPosition var25 = this.mc
                  .theWorld
                  .rayTraceBlocks(
                     new Vec3(var5 + var22, var7 + var23, var9 + var24), new Vec3(var5 - var15 + var22 + var24, var7 - var19 + var23, var9 - var17 + var24)
                  );
               if (var25 != null) {
                  double var26 = var25.hitVec.distanceTo(new Vec3(var5, var7, var9));
                  if (var26 < var11) {
                     var11 = var26;
                  }
               }
            }

            if (this.mc.gameSettings.thirdPersonView == 2) {
               GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
            }

            GlStateManager.rotate(var2.recoveredField3509 - var14, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(var2.recoveredField3508 - var13, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(0.0F, 0.0F, (float)(-var11));
            GlStateManager.rotate(var13 - var2.recoveredField3508, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(var14 - var2.recoveredField3509, 1.0F, 0.0F, 0.0F);
         }
      } else {
         GlStateManager.translate(0.0F, 0.0F, -0.1F);
      }

      if (Reflector.EntityViewRenderEvent_CameraSetup_Constructor.exists()) {
         if (!this.mc.gameSettings.recoveredField2675) {
            float var32 = var2.recoveredField3505 + (var2.recoveredField3508 - var2.recoveredField3505) * var1 + 180.0F;
            float var34 = var2.recoveredField3506 + (var2.recoveredField3509 - var2.recoveredField3506) * var1;
            float var36 = 0.0F;
            if (var3 instanceof EntityAnimal) {
               EntityAnimal var38 = (EntityAnimal)var3;
               var32 = var38.prevRotationYawHead + (var38.aK - var38.prevRotationYawHead) * var1 + 180.0F;
            }

            Block var39 = ActiveRenderInfo.getBlockAtEntityViewpoint(this.mc.theWorld, var3, var1);
            Object var40 = Reflector.newInstance(Reflector.EntityViewRenderEvent_CameraSetup_Constructor, this, var3, var39, var1, var32, var34, var36);
            Reflector.postForgeBusEvent(var40);
            GlStateManager.rotate(var36, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(var34, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(var32, 0.0F, 1.0F, 0.0F);
         }
      } else if (!this.mc.gameSettings.recoveredField2675) {
         GlStateManager.rotate(var2.recoveredField3506 + (var2.recoveredField3509 - var2.recoveredField3506) * var1, 1.0F, 0.0F, 0.0F);
         if (var3 instanceof EntityAnimal) {
            EntityAnimal var33 = (EntityAnimal)var3;
            GlStateManager.rotate(var33.prevRotationYawHead + (var33.aK - var33.prevRotationYawHead) * var1 + 180.0F, 0.0F, 1.0F, 0.0F);
         } else {
            GlStateManager.rotate(var2.recoveredField3505 + (var2.recoveredField3508 - var2.recoveredField3505) * var1 + 180.0F, 0.0F, 1.0F, 0.0F);
         }
      }

      GlStateManager.translate(0.0F, -var4, 0.0F);
      var5 = var3.p + (var3.s - var3.p) * var1;
      var7 = var3.q + (var3.t - var3.q) * var1 + var4;
      var9 = var3.r + (var3.u - var3.r) * var1;
      this.cloudFog = this.mc.renderGlobal.hasCloudFog(var5, var7, var9, var1);
   }

   public void loadAllVisibleChunks(Entity var1, double var2, ICamera var4, boolean var5) {
      int var6 = this.mc.gameSettings.ofChunkUpdates;
      boolean var7 = this.mc.gameSettings.ofLazyChunkLoading;

      try {
         this.mc.gameSettings.ofChunkUpdates = 1000;
         this.mc.gameSettings.ofLazyChunkLoading = false;
         RenderGlobal var8 = Config.getRenderGlobal();
         int var9 = var8.method_24632();
         long var10 = System.currentTimeMillis();
         Config.dbg("Loading visible chunks");
         long var12 = System.currentTimeMillis() + 5000L;
         int var14 = 0;
         boolean var15 = false;

         do {
            var15 = false;

            for (int var16 = 0; var16 < 100; var16++) {
               var8.displayListEntitiesDirty = true;
               var8.setupTerrain(var1, var2, var4, this.frameCount++, var5);
               if (!var8.hasNoChunkUpdates()) {
                  var15 = true;
               }

               var14 += var8.getCountChunksToUpdate();

               while (!var8.hasNoChunkUpdates()) {
                  var8.updateChunks(System.nanoTime() + 1000000000L);
               }

               var14 -= var8.getCountChunksToUpdate();
               if (!var15) {
                  break;
               }
            }

            if (var8.method_24632() != var9) {
               var15 = true;
               var9 = var8.method_24632();
            }

            if (System.currentTimeMillis() > var12) {
               Config.log("Chunks loaded: " + var14);
               var12 = System.currentTimeMillis() + 5000L;
            }
         } while (var15);

         Config.log("Chunks loaded: " + var14);
         Config.log("Finished loading visible chunks");
         RenderChunk.renderChunksUpdated = 0;
      } finally {
         this.mc.gameSettings.ofChunkUpdates = var6;
         this.mc.gameSettings.ofLazyChunkLoading = var7;
      }
   }

   public void method_29169() {
      this.recoveredField3156 = 0;
      if (!Config.isSmoothWorld() || !Config.isSingleProcessor()) {
         this.recoveredField3159 = 0L;
         this.recoveredField3137 = 0;
      } else if (this.mc.isIntegratedServerRunning()) {
         IntegratedServer var1 = this.mc.getIntegratedServer();
         if (var1 != null) {
            boolean var2 = this.mc.isGamePaused();
            if (!var2 && !(this.mc.currentScreen instanceof GuiDownloadTerrain)) {
               if (this.recoveredField3145 > 0) {
                  Lagometer.recoveredField946.method_30327();
                  Config.sleep(this.recoveredField3145);
                  Lagometer.recoveredField946.method_30328();
                  this.recoveredField3156 = this.recoveredField3145;
               }

               long var3 = System.nanoTime() / 1000000L;
               if (this.recoveredField3159 != 0L && this.recoveredField3137 != 0) {
                  long var5 = var3 - this.recoveredField3159;
                  if (var5 < 0L) {
                     this.recoveredField3159 = var3;
                     var5 = 0L;
                  }

                  if (var5 >= 50L) {
                     this.recoveredField3159 = var3;
                     int var7 = var1.getTickCounter();
                     int var8 = var7 - this.recoveredField3137;
                     if (var8 < 0) {
                        this.recoveredField3137 = var7;
                        var8 = 0;
                     }

                     if (var8 < 1 && this.recoveredField3145 < 100) {
                        this.recoveredField3145 += 2;
                     }

                     if (var8 > 1 && this.recoveredField3145 > 0) {
                        this.recoveredField3145--;
                     }

                     this.recoveredField3137 = var7;
                  }
               } else {
                  this.recoveredField3159 = var3;
                  this.recoveredField3137 = var1.getTickCounter();
                  this.recoveredField3157 = 1.0F;
                  this.recoveredField3140 = 50.0F;
               }
            } else {
               if (this.mc.currentScreen instanceof GuiDownloadTerrain) {
                  Config.sleep(20L);
               }

               this.recoveredField3159 = 0L;
               this.recoveredField3137 = 0;
            }
         }
      }
   }

   public void getMouseOver(float var1) {
      Entity var2 = this.mc.getRenderViewEntity();
      if (var2 != null && this.mc.theWorld != null) {
         this.mc.mcProfiler.startSection("pick");
         this.mc.pointedEntity = null;
         double var3 = this.mc.playerController.getBlockReachDistance();
         this.mc.objectMouseOver = var2.rayTrace(var3, var1);
         double var5 = var3;
         Vec3 var7 = var2.getPositionEyes(var1);
         boolean var8 = false;
         byte var9 = 3;
         if (this.mc.playerController.extendedReach()) {
            var3 = 6.0;
            var5 = 6.0;
         } else if (var3 > 3.0) {
            var8 = true;
         }

         if (this.mc.objectMouseOver != null) {
            var5 = this.mc.objectMouseOver.hitVec.distanceTo(var7);
         }

         Vec3 var10 = var2.getLook(var1);
         Vec3 var11 = var7.addVector(var10.xCoord * var3, var10.yCoord * var3, var10.zCoord * var3);
         this.pointedEntity = null;
         Vec3 var12 = null;
         float var13 = 1.0F;
         List var14 = this.mc
            .theWorld
            .a(
               var2,
               var2.getEntityBoundingBox().addCoord(var10.xCoord * var3, var10.yCoord * var3, var10.zCoord * var3).expand(var13, var13, var13),
               Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>() {
                  public boolean apply(Entity var1) {
                     return var1.canBeCollidedWith();
                  }
               })
            );
         double var15 = var5;

         for (int var17 = 0; var17 < var14.size(); var17++) {
            Entity var18 = (Entity)var14.get(var17);
            float var19 = var18.getCollisionBorderSize();
            AxisAlignedBB var20 = var18.getEntityBoundingBox().expand(var19, var19, var19);
            MovingObjectPosition var21 = var20.calculateIntercept(var7, var11);
            if (var20.isVecInside(var7)) {
               if (var15 >= 0.0) {
                  this.pointedEntity = var18;
                  var12 = var21 == null ? var7 : var21.hitVec;
                  var15 = 0.0;
               }
            } else if (var21 != null) {
               double var22 = var7.distanceTo(var21.hitVec);
               if (var22 < var15 || var15 == 0.0) {
                  boolean var24 = false;
                  if (Reflector.ForgeEntity_canRiderInteract.exists()) {
                     var24 = Reflector.callBoolean(var18, Reflector.ForgeEntity_canRiderInteract);
                  }

                  if (var24 || var18 != var2.m) {
                     this.pointedEntity = var18;
                     var12 = var21.hitVec;
                     var15 = var22;
                  } else if (var15 == 0.0) {
                     this.pointedEntity = var18;
                     var12 = var21.hitVec;
                  }
               }
            }
         }

         if (this.pointedEntity != null && var8 && var7.distanceTo(var12) > 3.0) {
            this.pointedEntity = null;
            this.mc.objectMouseOver = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, var12, (EnumFacing)null, new BlockPos(var12));
         }

         if (this.pointedEntity != null && (var15 < var5 || this.mc.objectMouseOver == null)) {
            this.mc.objectMouseOver = new MovingObjectPosition(this.pointedEntity, var12);
            if (this.pointedEntity instanceof EntityLivingBase || this.pointedEntity instanceof EntityItemFrame) {
               this.mc.pointedEntity = this.pointedEntity;
            }
         }

         this.mc.mcProfiler.endSection();
      }
   }

   @Override
   public void onResourceManagerReload(IResourceManager var1) {
      if (this.theShaderGroup != null) {
         this.theShaderGroup.deleteShaderGroup();
      }

      this.theShaderGroup = null;
      if (this.shaderIndex != shaderCount) {
         this.loadShader(shaderResourceLocations[this.shaderIndex]);
      } else {
         this.loadEntityShader(this.mc.getRenderViewEntity());
      }
   }

   public void updateTorchFlicker() {
      this.torchFlickerDX = (float)(this.torchFlickerDX + (Math.random() - Math.random()) * Math.random() * Math.random());
      this.torchFlickerDX = (float)(this.torchFlickerDX * 0.9);
      this.torchFlickerX = this.torchFlickerX + (this.torchFlickerDX - this.torchFlickerX) * 1.0F;
      this.lightmapUpdateNeeded = true;
   }

   public void renderWorld(float var1, long var2) {
      this.updateLightmap(var1);
      if (this.mc.getRenderViewEntity() == null) {
         this.mc.setRenderViewEntity(this.mc.thePlayer);
      }

      this.getMouseOver(var1);
      if (Config.isShaders()) {
         Shaders.beginRender(this.mc, var1, var2);
      }

      GlStateManager.enableDepth();
      GlStateManager.enableAlpha();
      GlStateManager.alphaFunc(516, 0.1F);
      this.mc.mcProfiler.startSection("center");
      if (this.mc.gameSettings.anaglyph) {
         anaglyphField = 0;
         GlStateManager.colorMask(false, true, true, false);
         this.renderWorldPass(0, var1, var2);
         anaglyphField = 1;
         GlStateManager.colorMask(true, false, false, false);
         this.renderWorldPass(1, var1, var2);
         GlStateManager.colorMask(true, true, true, false);
      } else {
         this.renderWorldPass(2, var1, var2);
      }

      this.mc.mcProfiler.endSection();
   }

   public void setupCameraTransform(float var1, int var2) {
      this.farPlaneDistance = this.mc.gameSettings.renderDistanceChunks * 16;
      if (Config.isFogFancy()) {
         this.farPlaneDistance *= 0.95F;
      }

      if (Config.isFogFast()) {
         this.farPlaneDistance *= 0.83F;
      }

      GlStateManager.matrixMode(5889);
      GlStateManager.loadIdentity();
      float var3 = 0.07F;
      if (this.mc.gameSettings.anaglyph) {
         GlStateManager.translate(-(var2 * 2 - 1) * var3, 0.0F, 0.0F);
      }

      this.clipDistance = this.farPlaneDistance * 2.0F;
      if (this.clipDistance < 173.0F) {
         this.clipDistance = 173.0F;
      }

      if (this.recoveredField3139 != 1.0) {
         GlStateManager.translate((float)this.recoveredField3138, (float)(-this.recoveredField3143), 0.0F);
         GlStateManager.scale(this.recoveredField3139, this.recoveredField3139, 1.0);
      }

      Project.gluPerspective(this.getFOVModifier(var1, true), (float)this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.clipDistance);
      GlStateManager.matrixMode(5888);
      GlStateManager.loadIdentity();
      if (this.mc.gameSettings.anaglyph) {
         GlStateManager.translate((var2 * 2 - 1) * 0.1F, 0.0F, 0.0F);
      }

      this.method_29171(var1);
      PerspectiveModule var4 = CheatBreaker.getInstance().getModuleManager().recoveredField1723;
      if (this.mc.gameSettings.viewBobbing) {
         if (var4.isEnabled()) {
            var4.method_24319(var1, var4.recoveredField3181);
         } else {
            this.setupViewBobbing(var1);
         }
      }

      float var5 = this.mc.thePlayer.recoveredField2897 + (this.mc.thePlayer.recoveredField2898 - this.mc.thePlayer.recoveredField2897) * var1;
      if (var5 > 0.0F) {
         byte var6 = 20;
         if (this.mc.thePlayer.isPotionActive(Potion.confusion)) {
            var6 = 7;
         }

         float var7 = 5.0F / (var5 * var5 + 5.0F) - var5 * 0.04F;
         var7 *= var7;
         GlStateManager.rotate((this.rendererUpdateCount + var1) * var6, 0.0F, 1.0F, 1.0F);
         GlStateManager.scale(1.0F / var7, 1.0F, 1.0F);
         GlStateManager.rotate(-(this.rendererUpdateCount + var1) * var6, 0.0F, 1.0F, 1.0F);
      }

      if (CheatBreaker.getInstance().getModuleManager().recoveredField1728.recoveredField3503) {
         this.method_29168(var1);
      } else {
         this.method_29174(var1);
      }

      if (this.recoveredField3152) {
         switch (this.recoveredField3146) {
            case 0:
               GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
               break;
            case 1:
               GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
               break;
            case 2:
               GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
               break;
            case 3:
               GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
               break;
            case 4:
               GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
         }
      }
   }

   public void updateRenderer() {
      if (OpenGlHelper.shadersSupported && ShaderLinkHelper.getStaticShaderLinkHelper() == null) {
         ShaderLinkHelper.setNewStaticShaderLinkHelper();
      }

      this.updateFovModifierHand();
      this.updateTorchFlicker();
      this.fogColor2 = this.fogColor1;
      this.thirdPersonDistanceTemp = this.thirdPersonDistance;
      if (this.mc.gameSettings.smoothCamera) {
         float var1 = this.mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
         float var2 = var1 * var1 * var1 * 8.0F;
         this.smoothCamFilterX = this.mouseFilterXAxis.smooth(this.smoothCamYaw, 0.05F * var2);
         this.smoothCamFilterY = this.mouseFilterYAxis.smooth(this.smoothCamPitch, 0.05F * var2);
         this.smoothCamPartialTicks = 0.0F;
         this.smoothCamYaw = 0.0F;
         this.smoothCamPitch = 0.0F;
      } else {
         this.smoothCamFilterX = 0.0F;
         this.smoothCamFilterY = 0.0F;
         this.mouseFilterXAxis.reset();
         this.mouseFilterYAxis.reset();
      }

      if (this.mc.getRenderViewEntity() == null) {
         this.mc.setRenderViewEntity(this.mc.thePlayer);
      }

      Entity var11 = this.mc.getRenderViewEntity();
      double var12 = var11.s;
      double var4 = var11.t + var11.getEyeHeight();
      double var6 = var11.u;
      float var8 = this.mc.theWorld.o(new BlockPos(var12, var4, var6));
      float var9 = this.mc.gameSettings.renderDistanceChunks / 16.0F;
      var9 = MathHelper.clamp_float(var9, 0.0F, 1.0F);
      float var10 = var8 * (1.0F - var9) + var9;
      this.fogColor1 = this.fogColor1 + (var10 - this.fogColor1) * 0.1F;
      this.rendererUpdateCount++;
      this.itemRenderer.updateEquippedItem();
      this.addRainParticles();
      this.bossColorModifierPrev = this.bossColorModifier;
      if (BossStatus.hasColorModifier) {
         this.bossColorModifier += 0.05F;
         if (this.bossColorModifier > 1.0F) {
            this.bossColorModifier = 1.0F;
         }

         BossStatus.hasColorModifier = false;
      } else if (this.bossColorModifier > 0.0F) {
         this.bossColorModifier -= 0.0125F;
      }
   }

   public void method_29141() {
      if (OpenGlHelper.isFramebufferEnabled() && OpenGlHelper.shadersSupported) {
         if (this.theShaderGroup != null) {
            this.theShaderGroup.deleteShaderGroup();
         }

         try {
            this.theShaderGroup = new ShaderGroup(this.mc.getTextureManager(), this.recoveredField3135, this.mc.getFramebuffer(), recoveredField3154);
            this.theShaderGroup.createBindFramebuffers(this.mc.displayWidth, this.mc.displayHeight);
            this.useShader = true;
         } catch (Exception var2) {
         }
      }
   }

   public void setupFog(int var1, float var2) {
      this.fogStandard = false;
      Entity var3 = this.mc.getRenderViewEntity();
      boolean var4 = false;
      if (var3 instanceof EntityPlayer) {
         var4 = ((EntityPlayer)var3).bA.isCreativeMode;
      }

      GL11.glFog(2918, this.setFogColorBuffer(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 1.0F));
      GL11.glNormal3f(0.0F, -1.0F, 0.0F);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      Block var5 = ActiveRenderInfo.getBlockAtEntityViewpoint(this.mc.theWorld, var3, var2);
      float var6 = -1.0F;
      if (Reflector.ForgeHooksClient_getFogDensity.exists()) {
         var6 = Reflector.callFloat(Reflector.ForgeHooksClient_getFogDensity, this, var3, var5, var2, 0.1F);
      }

      if (var6 >= 0.0F) {
         GlStateManager.setFogDensity(var6);
      } else if (var3 instanceof EntityLivingBase && ((EntityLivingBase)var3).isPotionActive(Potion.blindness)) {
         float var11 = 5.0F;
         int var12 = ((EntityLivingBase)var3).getActivePotionEffect(Potion.blindness).getDuration();
         if (var12 < 20) {
            var11 = 5.0F + (this.farPlaneDistance - 5.0F) * (1.0F - var12 / 20.0F);
         }

         GlStateManager.setFog(9729);
         if (var1 == -1) {
            GlStateManager.setFogStart(0.0F);
            GlStateManager.setFogEnd(var11 * 0.8F);
         } else {
            GlStateManager.setFogStart(var11 * 0.25F);
            GlStateManager.setFogEnd(var11);
         }

         if (GLContext.getCapabilities().GL_NV_fog_distance && Config.isFogFancy()) {
            GL11.glFogi(34138, 34139);
         }
      } else if (this.cloudFog) {
         GlStateManager.setFog(2048);
         GlStateManager.setFogDensity(0.1F);
      } else if (var5.getMaterial() == Material.water) {
         GlStateManager.setFog(2048);
         float var7 = Config.isClearWater() ? 0.02F : 0.1F;
         if (var3 instanceof EntityLivingBase && ((EntityLivingBase)var3).isPotionActive(Potion.waterBreathing)) {
            GlStateManager.setFogDensity(0.01F);
         } else {
            float var8 = 0.1F - EnchantmentHelper.getRespiration(var3) * 0.03F;
            GlStateManager.setFogDensity(Config.limit(var8, 0.0F, var7));
         }
      } else if (var5.getMaterial() == Material.lava) {
         GlStateManager.setFog(2048);
         GlStateManager.setFogDensity(2.0F);
      } else {
         float var10 = this.farPlaneDistance;
         this.fogStandard = true;
         GlStateManager.setFog(9729);
         if (var1 == -1) {
            GlStateManager.setFogStart(0.0F);
            GlStateManager.setFogEnd(var10);
         } else {
            GlStateManager.setFogStart(var10 * Config.getFogStart());
            GlStateManager.setFogEnd(var10);
         }

         if (GLContext.getCapabilities().GL_NV_fog_distance) {
            if (Config.isFogFancy()) {
               GL11.glFogi(34138, 34139);
            }

            if (Config.isFogFast()) {
               GL11.glFogi(34138, 34140);
            }
         }

         if (this.mc.theWorld.t.doesXZShowFog((int)var3.s, (int)var3.u)) {
            GlStateManager.setFogStart(var10 * 0.05F);
            GlStateManager.setFogEnd(var10);
         }

         if (Reflector.ForgeHooksClient_onFogRender.exists()) {
            Reflector.callVoid(Reflector.ForgeHooksClient_onFogRender, this, var3, var5, var2, var1, var10);
         }
      }

      GlStateManager.enableColorMaterial();
      GlStateManager.enableFog();
      GlStateManager.colorMaterial(1028, 4608);
   }

   public EntityRenderer(Minecraft var1, IResourceManager var2) {
      this.mouseFilterYAxis = new MouseFilter();
      this.thirdPersonDistance = 4.0F;
      this.thirdPersonDistanceTemp = 4.0F;
      this.recoveredField3144 = true;
      this.drawBlockOutline = true;
      this.recoveredField3133 = Minecraft.getSystemTime();
      this.recoveredField3149 = new float[1024];
      this.recoveredField3136 = new float[1024];
      this.fogColorBuffer = GLAllocation.createDirectFloatBuffer(16);
      this.recoveredField3146 = 0;
      this.recoveredField3152 = false;
      this.recoveredField3139 = 1.0;
      this.recoveredField3147 = false;
      this.updatedWorld = null;
      this.recoveredField3148 = false;
      this.fogStandard = false;
      this.clipDistance = 128.0F;
      this.recoveredField3159 = 0L;
      this.recoveredField3137 = 0;
      this.recoveredField3145 = 0;
      this.recoveredField3156 = 0;
      this.recoveredField3140 = 0.0F;
      this.recoveredField3157 = 0.0F;
      this.fxaaShaders = new ShaderGroup[10];
      this.loadVisibleChunks = false;
      this.shaderIndex = shaderCount;
      this.useShader = false;
      this.frameCount = 0;
      this.mc = var1;
      this.recoveredField3135 = var2;
      this.itemRenderer = var1.getItemRenderer();
      this.recoveredField3142 = new MapItemRenderer(var1.getTextureManager());
      this.lightmapTexture = new DynamicTexture(16, 16);
      this.locationLightMap = var1.getTextureManager().getDynamicTextureLocation("lightMap", this.lightmapTexture);
      this.lightmapColors = this.lightmapTexture.getTextureData();
      this.theShaderGroup = null;

      for (int var3 = 0; var3 < 32; var3++) {
         for (int var4 = 0; var4 < 32; var4++) {
            float var5 = var4 - 16;
            float var6 = var3 - 16;
            float var7 = MathHelper.sqrt_float(var5 * var5 + var6 * var6);
            this.recoveredField3149[var3 << 5 | var4] = -var6 / var7;
            this.recoveredField3136[var3 << 5 | var4] = var5 / var7;
         }
      }
   }

   public void addRainParticles() {
      EnvironmentModule var1 = CheatBreaker.getInstance().getModuleManager().recoveredField1726;
      if (!var1.isEnabled() || !var1.recoveredField2036.method_08908() || !var1.method_20153()) {
         float var2 = this.mc.theWorld.j(1.0F);
         if (!Config.method_03856()) {
            var2 /= 2.0F;
         }

         if (var2 != 0.0F && Config.isRainSplash()) {
            this.random.setSeed(this.rendererUpdateCount * 312987231L);
            Entity var3 = this.mc.getRenderViewEntity();
            WorldClient var4 = this.mc.theWorld;
            BlockPos var5 = new BlockPos(var3);
            byte var6 = 10;
            double var7 = 0.0;
            double var9 = 0.0;
            double var11 = 0.0;
            int var13 = 0;
            int var14 = (int)(100.0F * var2 * var2);
            if (this.mc.gameSettings.particleSetting == 1) {
               var14 >>= 1;
            } else if (this.mc.gameSettings.particleSetting == 2) {
               var14 = 0;
            }

            for (int var15 = 0; var15 < var14; var15++) {
               BlockPos var16 = var4.getPrecipitationHeight(
                  var5.add(this.random.nextInt(var6) - this.random.nextInt(var6), 0, this.random.nextInt(var6) - this.random.nextInt(var6))
               );
               BiomeGenBase var17 = var4.getBiomeGenForCoords(var16);
               BlockPos var18 = var16.down();
               Block var19 = var4.getBlockState(var18).getBlock();
               if (var16.getY() <= var5.getY() + var6 && var16.getY() >= var5.getY() - var6 && var17.canRain() && var17.getFloatTemperature(var16) >= 0.15F) {
                  double var20 = this.random.nextDouble();
                  double var22 = this.random.nextDouble();
                  if (var19.getMaterial() == Material.lava) {
                     this.mc
                        .theWorld
                        .spawnParticle(
                           EnumParticleTypes.SMOKE_NORMAL,
                           var16.getX() + var20,
                           var16.getY() + 0.1F - var19.getBlockBoundsMinY(),
                           var16.getZ() + var22,
                           0.0,
                           0.0,
                           0.0
                        );
                  } else if (var19.getMaterial() != Material.air) {
                     var19.setBlockBoundsBasedOnState(var4, var18);
                     if (this.random.nextInt(++var13) == 0) {
                        var7 = var18.getX() + var20;
                        var9 = var18.getY() + 0.1F + var19.getBlockBoundsMaxY() - 1.0;
                        var11 = var18.getZ() + var22;
                     }

                     this.mc
                        .theWorld
                        .spawnParticle(
                           EnumParticleTypes.WATER_DROP,
                           var18.getX() + var20,
                           var18.getY() + 0.1F + var19.getBlockBoundsMaxY(),
                           var18.getZ() + var22,
                           0.0,
                           0.0,
                           0.0
                        );
                  }
               }
            }

            if (var13 > 0 && this.random.nextInt(3) < this.recoveredField3150++) {
               this.recoveredField3150 = 0;
               if (var9 > var5.getY() + 1 && var4.getPrecipitationHeight(var5).getY() > MathHelper.floor_float(var5.getY())) {
                  this.mc.theWorld.playSound(var7, var9, var11, "ambient.weather.rain", 0.1F, 0.5F, false);
               } else {
                  this.mc.theWorld.playSound(var7, var9, var11, "ambient.weather.rain", 0.2F, 1.0F, false);
               }
            }
         }
      }
   }

   public FloatBuffer setFogColorBuffer(float var1, float var2, float var3, float var4) {
      if (Config.isShaders()) {
         Shaders.setFogColor(var1, var2, var3);
      }

      ((Buffer)this.fogColorBuffer).clear();
      this.fogColorBuffer.put(var1).put(var2).put(var3).put(var4);
      ((Buffer)this.fogColorBuffer).flip();
      return this.fogColorBuffer;
   }

   public void updateShaderGroupSize(int var1, int var2) {
      if (OpenGlHelper.shadersSupported) {
         if (this.theShaderGroup != null) {
            this.theShaderGroup.createBindFramebuffers(var1, var2);
         }

         this.mc.renderGlobal.createBindEntityOutlineFbs(var1, var2);
      }
   }

   public void loadEntityShader(Entity var1) {
      if (OpenGlHelper.shadersSupported) {
         if (this.theShaderGroup != null) {
            this.theShaderGroup.deleteShaderGroup();
         }

         this.theShaderGroup = null;
         if (var1 instanceof EntityCreeper) {
            this.loadShader(new ResourceLocation("shaders/post/creeper.json"));
         } else if (var1 instanceof EntitySpider) {
            this.loadShader(new ResourceLocation("shaders/post/spider.json"));
         } else if (var1 instanceof EntityEnderman) {
            this.loadShader(new ResourceLocation("shaders/post/invert.json"));
         } else if (Reflector.ForgeHooksClient_loadEntityShader.exists()) {
            Reflector.call(Reflector.ForgeHooksClient_loadEntityShader, var1, this);
         }
      }
   }
}
