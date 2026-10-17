package net.minecraft.client;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.config.Setting;
import com.cheatbreaker.client.util.render.TextureAnimationTicker;
import com.cheatbreaker.client.event.type.LoadWorldEvent;
import com.cheatbreaker.client.event.type.TickEvent;
import com.cheatbreaker.client.module.type.AutoTextModule;
import com.cheatbreaker.client.ui.mainmenu.LegacyMainMenu;
import com.cheatbreaker.client.ui.module.CBModulesGui;
import com.cheatbreaker.client.ui.overlay.OverlayGui;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.net.SocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import javax.imageio.ImageIO;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiControls;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMemoryErrorScreen;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSleepMP;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.achievement.GuiAchievement;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.gui.stream.GuiStreamUnavailable;
import net.minecraft.client.main.GameConfiguration;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.FoliageColorReloadListener;
import net.minecraft.client.resources.GrassColorReloadListener;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.resources.ResourceIndex;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.client.resources.data.AnimationMetadataSectionSerializer;
import net.minecraft.client.resources.data.FontMetadataSection;
import net.minecraft.client.resources.data.FontMetadataSectionSerializer;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.client.resources.data.LanguageMetadataSection;
import net.minecraft.client.resources.data.LanguageMetadataSectionSerializer;
import net.minecraft.client.resources.data.PackMetadataSection;
import net.minecraft.client.resources.data.PackMetadataSectionSerializer;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.client.resources.data.TextureMetadataSectionSerializer;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.stream.IStream;
import net.minecraft.client.stream.NullStream;
import net.minecraft.client.stream.TwitchStream;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Bootstrap;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.profiler.IPlayerUsage;
import net.minecraft.profiler.PlayerUsageSnooper;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.IStatStringFormat;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.FrameTimer;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MinecraftError;
import net.minecraft.util.MouseHelper;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ScreenShotHelper;
import net.minecraft.util.Session;
import net.minecraft.util.Util;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.storage.AnvilSaveConverter;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.opengl.OpenGLException;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.glu.GLU;
import net.minecraft.util.Timer;
import com.cheatbreaker.client.event.type.KeyPressEvent;
import com.cheatbreaker.client.event.type.MouseClickEvent;
import net.minecraft.client.StreamingConfirmCallback;
import com.cheatbreaker.client.ui.loading.StartupLoadingGui;
import com.cheatbreaker.client.event.type.WorldChangeEvent;
import com.cheatbreaker.client.ui.mainmenu.MainMenuMode;

public class Minecraft implements IThreadListener, IPlayerUsage {
   private final TextureAnimationTicker textureAnimationTicker = new TextureAnimationTicker();
   public LanguageManager mcLanguageManager;
   public int recoveredField3809;
   public List<IResourcePack> defaultResourcePacks;
   public File recoveredField3810;
   public MouseHelper mouseHelper;
   public String debug;
   public ModelManager modelManager;
   public GuiIngame ingameGUI;
   public RenderGlobal renderGlobal;
   public ItemRenderer itemRenderer;
   public IStream stream;
   public static Logger logger = LogManager.getLogger();
   public boolean integratedServerIsRunning;
   public volatile boolean running;
   public String recoveredField3811;
   public MovingObjectPosition objectMouseOver;
   public static ResourceLocation locationMojangPng = new ResourceLocation("textures/gui/title/mojang.png");
   public MusicTicker mcMusicTicker;
   public DefaultResourcePack mcDefaultResourcePack;
   public SkinManager skinManager;
   public Entity renderViewEntity;
   public boolean recoveredField3812;
   public PropertyMap profileProperties;
   public ServerData currentServerData;
   public PlayerControllerMP playerController;
   public IMetadataSerializer metadataSerializer_;
   public boolean recoveredField3813;
   public String recoveredField3814;
   public boolean hasCrashed;
   public IReloadableResourceManager mcResourceManager;
   public static boolean isRunningOnMac = Util.getOSType() == Util.EnumOS.OSX;
   public boolean recoveredField3815;
   public Thread mcThread;
   public long recoveredField3816;
   public ResourcePackRepository mcResourcePackRepository;
   public NetworkManager myNetworkManager;
   public static Minecraft theMinecraft;
   public File recoveredField3817;
   public static byte[] memoryReserve = new byte[10485760];
   public IntegratedServer theIntegratedServer;
   public boolean recoveredField3818;
   public boolean recoveredField3819;
   public StartupLoadingGui recoveredField3820;
   public boolean connectedToRealms;
   public WorldClient theWorld;
   public FontRenderer fontRendererObj;
   public String launchedVersion;
   public static int debugFPS;
   public int displayHeight;
   public EffectRenderer effectRenderer;
   public boolean renderChunksMany;
   public FontRenderer standardGalacticFontRenderer;
   public Session session;
   public Proxy proxy;
   public PropertyMap twitchDetails;
   public CrashReport crashReporter;
   public Timer recoveredField3821;
   public ResourceLocation mojangLogo;
   public Profiler mcProfiler;
   public int recoveredField3822;
   public GuiAchievement guiAchievement;
   public BlockRendererDispatcher blockRenderDispatcher;
   public Entity pointedEntity;
   public long recoveredField3823;
   public boolean fullscreen;
   public boolean recoveredField3824;
   public int recoveredField3825;
   public static List<DisplayMode> macDisplayModes = Lists.newArrayList(new DisplayMode(2560, 1600), new DisplayMode(2880, 1800));
   public RenderManager renderManager;
   public GameSettings gameSettings;
   public int recoveredField3826;
   public ISaveFormat saveLoader;
   public long recoveredField3827;
   public LoadingScreenRenderer loadingScreen;
   public PlayerUsageSnooper usageSnooper;
   public int recoveredField3828;
   public SoundHandler mcSoundHandler;
   public Queue<FutureTask<?>> recoveredField3829;
   public File mcDataDir;
   public long recoveredField3830;
   public long recoveredField3831;
   public EntityRenderer entityRenderer;
   public EntityPlayerSP thePlayer;
   public long recoveredField3832;
   public boolean recoveredField3833 = true;
   public FrameTimer recoveredField3834;
   public MinecraftSessionService sessionService;
   public GuiScreen currentScreen;
   public int displayWidth;
   public RenderItem renderItem;
   public int recoveredField3835;
   public boolean recoveredField3836;
   public TextureMap textureMapBlocks;
   public boolean recoveredField3837;
   public int recoveredField3838;
   public TextureManager renderEngine;
   public boolean recoveredField3839;
   public Framebuffer framebufferMc;

   @Override
   public void addServerStatsToSnooper(PlayerUsageSnooper var1) {
      var1.addClientStat("fps", debugFPS);
      var1.addClientStat("vsync_enabled", this.gameSettings.enableVsync);
      var1.addClientStat("display_frequency", Display.getDisplayMode().getFrequency());
      var1.addClientStat("display_type", this.fullscreen ? "fullscreen" : "windowed");
      var1.addClientStat("run_time", (MinecraftServer.getCurrentTimeMillis() - var1.getMinecraftStartTimeMillis()) / 60L * 1000L);
      var1.addClientStat("current_action", this.method_20429());
      String var2 = ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN ? "little" : "big";
      var1.addClientStat("endianness", var2);
      var1.addClientStat("resource_packs", this.mcResourcePackRepository.getRepositoryEntries().size());
      int var3 = 0;

      for (ResourcePackRepository.Entry var5 : this.mcResourcePackRepository.getRepositoryEntries()) {
         var1.addClientStat("resource_pack[" + var3++ + "]", var5.getResourcePackName());
      }

      if (this.theIntegratedServer != null && this.theIntegratedServer.getPlayerUsageSnooper() != null) {
         var1.addClientStat("snooper_partner", this.theIntegratedServer.getPlayerUsageSnooper().getUniqueID());
      }
   }

   public void drawSplashScreen(TextureManager var1) throws org.lwjgl.LWJGLException {
      ScaledResolution var2 = new ScaledResolution(this);
      int var3 = var2.getScaleFactor();
      Framebuffer var4 = new Framebuffer(var2.getScaledWidth() * var3, var2.getScaledHeight() * var3, true);
      var4.bindFramebuffer(false);
      GlStateManager.matrixMode(5889);
      GlStateManager.loadIdentity();
      GlStateManager.ortho(0.0, var2.getScaledWidth(), var2.getScaledHeight(), 0.0, 1000.0, 3000.0);
      GlStateManager.matrixMode(5888);
      GlStateManager.loadIdentity();
      GlStateManager.translate(0.0F, 0.0F, -2000.0F);
      GlStateManager.disableLighting();
      GlStateManager.disableFog();
      GlStateManager.disableDepth();
      GlStateManager.enableTexture2D();
      InputStream var5 = null;

      try {
         var5 = this.mcDefaultResourcePack.getInputStream(locationMojangPng);
         this.mojangLogo = var1.getDynamicTextureLocation("logo", new DynamicTexture(ImageIO.read(var5)));
         var1.bindTexture(this.mojangLogo);
      } catch (IOException var12) {
         logger.error("Unable to load logo: " + locationMojangPng, var12);
      } finally {
         IOUtils.closeQuietly(var5);
      }

      Tessellator var6 = Tessellator.getInstance();
      WorldRenderer var7 = var6.getWorldRenderer();
      var7.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
      var7.pos(0.0, this.displayHeight, 0.0).tex(0.0, 0.0).color(255, 255, 255, 255).endVertex();
      var7.pos(this.displayWidth, this.displayHeight, 0.0).tex(0.0, 0.0).color(255, 255, 255, 255).endVertex();
      var7.pos(this.displayWidth, 0.0, 0.0).tex(0.0, 0.0).color(255, 255, 255, 255).endVertex();
      var7.pos(0.0, 0.0, 0.0).tex(0.0, 0.0).color(255, 255, 255, 255).endVertex();
      var6.draw();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      short var8 = 256;
      short var9 = 256;
      this.draw((var2.getScaledWidth() - var8) / 2, (var2.getScaledHeight() - var9) / 2, 0, 0, var8, var9, 255, 255, 255, 255);
      GlStateManager.disableLighting();
      GlStateManager.disableFog();
      var4.unbindFramebuffer();
      var4.framebufferRender(var2.getScaledWidth() * var3, var2.getScaledHeight() * var3);
      GlStateManager.enableAlpha();
      GlStateManager.alphaFunc(516, 0.1F);
      this.updateDisplay();
   }

   public void setServerData(ServerData var1) {
      this.currentServerData = var1;
   }

   public static int getDebugFPS() {
      return debugFPS;
   }

   public void loadWorld(WorldClient var1, String var2) {
      if (var1 == null) {
         NetHandlerPlayClient var3 = this.getNetHandler();
         if (var3 != null) {
            var3.cleanup();
         }

         if (this.theIntegratedServer != null && this.theIntegratedServer.isAnvilFileSet()) {
            this.theIntegratedServer.initiateShutdown();
            this.theIntegratedServer.setStaticInstance();
         }

         this.theIntegratedServer = null;
         this.guiAchievement.clearAchievements();
         this.entityRenderer.getMapItemRenderer().clearLoadedMaps();
      }

      this.renderViewEntity = null;
      this.myNetworkManager = null;
      if (this.loadingScreen != null) {
         this.loadingScreen.resetProgressAndMessage(var2);
         this.loadingScreen.displayLoadingString("");
      }

      if (var1 == null && this.theWorld != null) {
         this.mcResourcePackRepository.clearResourcePack();
         this.ingameGUI.resetPlayersOverlayFooterHeader();
         this.setServerData((ServerData)null);
         this.integratedServerIsRunning = false;
      }

      this.mcSoundHandler.stopSounds();
      this.theWorld = var1;
      if (var1 != null) {
         CheatBreaker.getInstance().method_19817().method_21935(new LoadWorldEvent(var1));
         CheatBreaker.getInstance().getNetHandler().getNametagsMap().clear();
         if (this.renderGlobal != null) {
            this.renderGlobal.setWorldAndLoadRenderers(var1);
         }

         if (this.effectRenderer != null) {
            this.effectRenderer.clearEffects(var1);
         }

         if (this.thePlayer == null) {
            this.thePlayer = this.playerController.func_178892_a(var1, new StatFileWriter());
            this.playerController.flipPlayer(this.thePlayer);
         }

         this.thePlayer.I();
         var1.spawnEntityInWorld(this.thePlayer);
         this.thePlayer.movementInput = new MovementInputFromOptions(this.gameSettings);
         this.playerController.setPlayerCapabilities(this.thePlayer);
         this.renderViewEntity = this.thePlayer;
      } else {
         this.saveLoader.flushCache();
         this.thePlayer = null;
      }

      this.recoveredField3830 = 0L;
      CheatBreaker.getInstance().method_19817().method_21935(new WorldChangeEvent());
   }

   public void method_20418() {
      if (this.currentScreen == null) {
         this.displayGuiScreen(new GuiIngameMenu());
         if (this.isSingleplayer() && !this.theIntegratedServer.getPublic()) {
            this.mcSoundHandler.method_27814();
         }
      }
   }

   public void updateDebugProfilerName(int var1) {
      List var2 = this.mcProfiler.getProfilingData(this.recoveredField3814);
      if (var2 != null && !var2.isEmpty()) {
         Profiler.Result var3 = (Profiler.Result)var2.remove(0);
         if (var1 == 0) {
            if (var3.field_76331_c.length() > 0) {
               int var4 = this.recoveredField3814.lastIndexOf(".");
               if (var4 >= 0) {
                  this.recoveredField3814 = this.recoveredField3814.substring(0, var4);
               }
            }
         } else {
            var1--;
            if (var1 < var2.size() && !((Profiler.Result)var2.get(var1)).field_76331_c.equals("unspecified")) {
               if (this.recoveredField3814.length() > 0) {
                  this.recoveredField3814 = this.recoveredField3814 + ".";
               }

               this.recoveredField3814 = this.recoveredField3814 + ((Profiler.Result)var2.get(var1)).field_76331_c;
            }
         }
      }
   }

   public void setConnectedToRealms(boolean var1) {
      this.connectedToRealms = var1;
   }

   public void method_20362() {
      this.recoveredField3809 = 0;
      if (this.recoveredField3809 <= 0) {
         this.thePlayer.swingItem();
         if (this.objectMouseOver == null) {
            logger.error("Null returned as 'hitResult', this shouldn't happen!");
            if (this.playerController.isNotCreative()) {
               this.recoveredField3809 = 10;
            }
         } else {
            switch (this.objectMouseOver.typeOfHit) {
               case ENTITY:
                  this.playerController.attackEntity(this.thePlayer, this.objectMouseOver.entityHit);
                  break;
               case BLOCK:
                  BlockPos var1 = this.objectMouseOver.getBlockPos();
                  if (this.theWorld.getBlockState(var1).getBlock().getMaterial() != Material.air) {
                     this.playerController.clickBlock(var1, this.objectMouseOver.sideHit);
                     break;
                  }
               case MISS:
               default:
                  if (this.playerController.isNotCreative()) {
                     this.recoveredField3809 = 10;
                  }
            }
         }
      }
   }

   public void method_20414() throws java.io.IOException {
      WindowsImeSupport.updateGameInput(this.currentScreen);
      if (this.recoveredField3825 > 0) {
         this.recoveredField3825--;
      }

      this.mcProfiler.startSection("gui");
      if (!this.recoveredField3824) {
         this.ingameGUI.updateTick();
      }

      this.mcProfiler.endSection();
      this.entityRenderer.getMouseOver(1.0F);
      this.mcProfiler.startSection("gameMode");
      if (!this.recoveredField3824 && this.theWorld != null) {
         this.playerController.updateController();
      }

      this.mcProfiler.endStartSection("textures");
      if (!this.recoveredField3824) {
         this.textureAnimationTicker.tick(this.renderEngine);
      }

      if (this.currentScreen == null && this.thePlayer != null) {
         if (this.thePlayer.getHealth() <= 0.0F) {
            this.displayGuiScreen(null);
         } else if (this.thePlayer.bJ() && this.theWorld != null) {
            this.displayGuiScreen(new GuiSleepMP());
         }
      } else if (this.currentScreen != null && this.currentScreen instanceof GuiSleepMP && !this.thePlayer.bJ()) {
         this.displayGuiScreen(null);
      }

      if (this.currentScreen != null) {
         this.recoveredField3809 = 10000;
      }

      CheatBreaker.getInstance().method_19817().method_21935(new TickEvent());
      if (this.currentScreen != null) {
         try {
            this.currentScreen.handleInput();
            OverlayGui.getInstance().method_26658();
         } catch (Throwable var10) {
            CrashReport var2 = CrashReport.makeCrashReport(var10, "Updating screen events");
            CrashReportCategory var3 = var2.makeCategory("Affected screen");
            var3.addCrashSectionCallable("Screen name", () -> this.currentScreen.getClass().getCanonicalName());
            throw new ReportedException(var2);
         }

         if (this.currentScreen != null) {
            try {
               this.currentScreen.updateScreen();
            } catch (Throwable var9) {
               CrashReport var15 = CrashReport.makeCrashReport(var9, "Ticking screen");
               CrashReportCategory var19 = var15.makeCategory("Affected screen");
               var19.addCrashSectionCallable("Screen name", () -> this.currentScreen.getClass().getCanonicalName());
               throw new ReportedException(var15);
            }
         }
      }

      if (this.currentScreen == null || this.currentScreen.p) {
         OverlayGui.getInstance().method_26658();
         this.mcProfiler.endStartSection("mouse");

         while (Mouse.next()) {
            int var1 = Mouse.getEventButton();
            KeyBinding.setKeyBindState(var1 - 100, Mouse.getEventButtonState());
            if (Mouse.getEventButtonState()) {
               if (this.thePlayer.isSpectator() && var1 == 2) {
                  this.ingameGUI.getSpectatorGui().func_175261_b();
               } else {
                  KeyBinding.onTick(var1 - 100);
                  CheatBreaker.getInstance().method_19817().method_21935(new MouseClickEvent(var1));
               }
            }

            AutoTextModule var16 = CheatBreaker.getInstance().getModuleManager().recoveredField1701;
            if (var16.isEnabled()) {
               for (Setting var4 : var16.recoveredField1863) {
                  if (var4.method_08911() != null && Mouse.getButtonName(var4.method_08877()) != null && Mouse.isButtonDown(var4.method_08877())) {
                     if (var4.method_08879() && var4.method_08877() != 0 && var1 == var4.method_08877() && var16.recoveredField1862.size() < 3) {
                        try {
                           var16.method_26044(var4.method_08874());
                        } catch (Exception var8) {
                        }

                        var16.recoveredField1862.add(System.currentTimeMillis());
                     } else {
                        var16.recoveredField1862.removeIf(var0 -> var0 < System.currentTimeMillis() - 2000L);
                     }
                  }
               }
            }

            long var21 = getSystemTime() - this.recoveredField3830;
            if (var21 <= 200L) {
               int var5 = Mouse.getEventDWheel();
               if (var5 != 0) {
                  if (this.thePlayer.isSpectator()) {
                     var5 = var5 < 0 ? -1 : 1;
                     if (this.ingameGUI.getSpectatorGui().func_175262_a()) {
                        this.ingameGUI.getSpectatorGui().func_175259_b(-var5);
                     } else {
                        float var6 = MathHelper.clamp_float(this.thePlayer.bA.getFlySpeed() + var5 * 0.005F, 0.0F, 0.2F);
                        this.thePlayer.bA.setFlySpeed(var6);
                     }
                  } else {
                     this.thePlayer.bi.changeCurrentItem(var5);
                  }
               }

               if (this.currentScreen == null) {
                  if (!this.recoveredField3812 && Mouse.getEventButtonState()) {
                     this.method_20340();
                  }
               } else {
                  this.currentScreen.handleMouseInput();
               }
            }
         }

         if (this.recoveredField3809 > 0) {
            this.recoveredField3809--;
         }

         this.mcProfiler.endStartSection("keyboard");
         this.method_20358();

         while (Keyboard.next()) {
            int var12 = Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey();
            KeyBinding.setKeyBindState(var12, Keyboard.getEventKeyState());
            if (Keyboard.getEventKeyState()) {
               KeyBinding.onTick(var12);
            }

            if (this.recoveredField3816 > 0L) {
               if (getSystemTime() - this.recoveredField3816 >= 6000L) {
                  throw new ReportedException(new CrashReport("Manually triggered debug crash", new Throwable()));
               }

               if (!Keyboard.isKeyDown(46) || !Keyboard.isKeyDown(61)) {
                  this.recoveredField3816 = -1L;
               }
            } else if (Keyboard.isKeyDown(46) && Keyboard.isKeyDown(61)) {
               this.recoveredField3816 = getSystemTime();
            }

            this.dispatchKeypresses();
            if (Keyboard.getEventKeyState()) {
               CheatBreaker.getInstance().method_19817().method_21935(new KeyPressEvent(Keyboard.getEventKey()));
               if (com.cheatbreaker.client.config.FriendListKeybind.isPressed(Keyboard.getEventKey())) {
                  this.displayGuiScreen(OverlayGui.createInstance(this.currentScreen));
               }

               AutoTextModule var17 = CheatBreaker.getInstance().getModuleManager().recoveredField1701;
               if (var17.isEnabled()) {
                  for (Setting var25 : var17.recoveredField1863) {
                     if (var25.method_08911() != null
                        && Keyboard.getKeyName(var25.method_08877()) != null
                        && Keyboard.getEventKey() == var25.method_08877()
                        && var17.recoveredField1862.size() < 3) {
                        try {
                           var17.method_26044(var25.method_08874());
                        } catch (Exception var7) {
                        }

                        var17.recoveredField1862.add(System.currentTimeMillis());
                     } else {
                        var17.recoveredField1862.removeIf(var0 -> var0 < System.currentTimeMillis() - 2000L);
                     }
                  }
               }

               if (var12 == 62 && this.entityRenderer != null) {
                  this.entityRenderer.stopUseShader();
               }

               if (this.currentScreen != null) {
                  this.currentScreen.handleKeyboardInput();
               } else {
                  if (var12 == 1) {
                     this.method_20418();
                  }

                  if (var12 == 32 && Keyboard.isKeyDown(61) && this.ingameGUI != null) {
                     this.ingameGUI.getChatGUI().clearChatMessages();
                  }

                  if (var12 == 31 && Keyboard.isKeyDown(61)) {
                     this.refreshResources();
                  }

                  if (var12 == 17 && Keyboard.isKeyDown(61)) {
                  }

                  if (var12 == 18 && Keyboard.isKeyDown(61)) {
                  }

                  if (var12 == 47 && Keyboard.isKeyDown(61)) {
                  }

                  if (var12 == 38 && Keyboard.isKeyDown(61)) {
                  }

                  if (var12 == 22 && Keyboard.isKeyDown(61)) {
                  }

                  if (var12 == 20 && Keyboard.isKeyDown(61)) {
                     this.refreshResources();
                  }

                  if (var12 == 33 && Keyboard.isKeyDown(61)) {
                     this.gameSettings.setOptionValue(GameSettings.Options.RENDER_DISTANCE, GuiScreen.isShiftKeyDown() ? -1 : 1);
                  }

                  if (var12 == 30 && Keyboard.isKeyDown(61)) {
                     this.renderGlobal.loadRenderers();
                  }

                  if (var12 == 35 && Keyboard.isKeyDown(61)) {
                     this.gameSettings.advancedItemTooltips = !this.gameSettings.advancedItemTooltips;
                     this.gameSettings.saveOptions();
                  }

                  if (var12 == 48 && Keyboard.isKeyDown(61)) {
                     CheatBreaker.getInstance()
                        .getModuleManager()
                        .recoveredField1703
                        .method_28806(!CheatBreaker.getInstance().getModuleManager().recoveredField1703.isEnabled());
                  }

                  if (var12 == 25 && Keyboard.isKeyDown(61)) {
                     this.gameSettings.recoveredField2704 = !this.gameSettings.recoveredField2704;
                     this.gameSettings.saveOptions();
                  }

                  if (var12 == 59) {
                     this.gameSettings.hideGUI = !this.gameSettings.hideGUI;
                  }

                  if (var12 == 61) {
                     this.gameSettings.recoveredField2681 = !this.gameSettings.recoveredField2681;
                     this.gameSettings.recoveredField2689 = GuiScreen.isShiftKeyDown();
                     this.gameSettings.showLagometer = GuiScreen.isAltKeyDown();
                  }

                  if (this.gameSettings.recoveredField2685.isPressed()) {
                     this.gameSettings.smoothCamera = !this.gameSettings.smoothCamera;
                  }
               }

               if (this.gameSettings.recoveredField2681 && this.gameSettings.recoveredField2689) {
                  if (var12 == 11) {
                     this.updateDebugProfilerName(0);
                  }

                  for (int var23 = 0; var23 < 9; var23++) {
                     if (var12 == 2 + var23) {
                        this.updateDebugProfilerName(var23 + 1);
                     }
                  }
               }
            }
         }

         for (int var13 = 0; var13 < 9; var13++) {
            if (this.gameSettings.keyBindsHotbar[var13].isPressed()) {
               if (this.thePlayer.isSpectator()) {
                  this.ingameGUI.getSpectatorGui().func_175260_a(var13);
               } else {
                  this.thePlayer.bi.currentItem = var13;
               }
            }
         }

         boolean var14 = this.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN;

         while (this.gameSettings.keyBindInventory.isPressed()) {
            if (this.playerController.isRidingHorse()) {
               this.thePlayer.sendHorseInventory();
            } else {
               this.getNetHandler().addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
               this.displayGuiScreen(new GuiInventory(this.thePlayer));
            }
         }

         while (this.gameSettings.keyBindDrop.isPressed()) {
            if (!this.thePlayer.isSpectator()) {
               this.thePlayer.dropOneItem(GuiScreen.isCtrlKeyDown());
            }
         }

         while (this.gameSettings.recoveredField2688.isPressed() && var14) {
            this.displayGuiScreen(new GuiChat());
         }

         if (this.currentScreen == null && this.gameSettings.recoveredField2693.isPressed() && var14) {
            this.displayGuiScreen(new GuiChat("/"));
         }

         if (this.thePlayer.isUsingItem()) {
            if (!this.gameSettings.recoveredField2680.isKeyDown()) {
               this.playerController.onStoppedUsingItem(this.thePlayer);
            }

            do {
               while (this.gameSettings.recoveredField2699.isPressed()) {
               }
            } while (this.gameSettings.recoveredField2680.isPressed() || this.gameSettings.keyBindPickBlock.isPressed());
         } else {
            while (this.gameSettings.recoveredField2680.isPressed()) {
               this.method_20407();
            }

            if (this.gameSettings.recoveredField2680.isKeyDown() && this.recoveredField3825 == 0 && !this.thePlayer.isUsingItem()) {
               this.method_20407();
            }

            while (this.gameSettings.recoveredField2699.isPressed()) {
               if (!this.thePlayer.isUsingItem()) {
                  this.method_20362();
               }
            }

            while (this.gameSettings.keyBindPickBlock.isPressed()) {
               this.method_20415();
            }
         }

         this.sendClickBlockToController(this.currentScreen == null && this.gameSettings.recoveredField2699.isKeyDown() && this.recoveredField3812);
      }

      if (this.theWorld != null) {
         if (this.thePlayer != null) {
            this.recoveredField3828++;
            if (this.recoveredField3828 == 30) {
               this.recoveredField3828 = 0;
               this.theWorld.joinEntityInSurroundings(this.thePlayer);
            }
         }

         this.mcProfiler.endStartSection("gameRenderer");
         if (!this.recoveredField3824) {
            this.entityRenderer.updateRenderer();
         }

         this.mcProfiler.endStartSection("levelRenderer");
         if (!this.recoveredField3824) {
            this.renderGlobal.method_24620();
         }

         this.mcProfiler.endStartSection("level");
         if (!this.recoveredField3824) {
            if (this.theWorld.getLastLightningBolt() > 0) {
               this.theWorld.setLastLightningBolt(this.theWorld.getLastLightningBolt() - 1);
            }

            this.theWorld.updateEntities();
         }
      } else if (this.entityRenderer.isShaderActive()) {
         this.entityRenderer.stopUseShader();
      }

      if (!this.recoveredField3824) {
         this.mcMusicTicker.update();
         this.mcSoundHandler.update();
      }

      if (this.theWorld != null) {
         if (!this.recoveredField3824) {
            this.theWorld.setAllowedSpawnTypes(this.theWorld.getDifficulty() != EnumDifficulty.PEACEFUL, true);

            try {
               this.theWorld.tick();
            } catch (Throwable var11) {
               CrashReport var18 = CrashReport.makeCrashReport(var11, "Exception in world tick");
               if (this.theWorld == null) {
                  CrashReportCategory var24 = var18.makeCategory("Affected level");
                  var24.addCrashSection("Problem", "Level is null!");
               } else {
                  this.theWorld.addWorldInfoToCrashReport(var18);
               }

               throw new ReportedException(var18);
            }
         }

         this.mcProfiler.endStartSection("animateTick");
         if (!this.recoveredField3824 && this.theWorld != null) {
            this.theWorld
               .doVoidFogParticles(
                  MathHelper.floor_double(this.thePlayer.s), MathHelper.floor_double(this.thePlayer.t), MathHelper.floor_double(this.thePlayer.u)
               );
         }

         this.mcProfiler.endStartSection("particles");
         if (!this.recoveredField3824) {
            this.effectRenderer.updateEffects();
         }
      } else if (this.myNetworkManager != null) {
         this.mcProfiler.endStartSection("pendingConnection");
         this.myNetworkManager.processReceivedPackets();
      }

      this.mcProfiler.endSection();
      this.recoveredField3830 = getSystemTime();
   }

   @Override
   public void addServerTypeToSnooper(PlayerUsageSnooper var1) {
      var1.addStatToSnooper("opengl_version", GL11.glGetString(7938));
      var1.addStatToSnooper("opengl_vendor", GL11.glGetString(7936));
      var1.addStatToSnooper("client_brand", ClientBrandRetriever.getClientModName());
      var1.addStatToSnooper("launched_version", this.launchedVersion);
      ContextCapabilities var2 = GLContext.getCapabilities();
      var1.addStatToSnooper("gl_caps[ARB_arrays_of_arrays]", var2.GL_ARB_arrays_of_arrays);
      var1.addStatToSnooper("gl_caps[ARB_base_instance]", var2.GL_ARB_base_instance);
      var1.addStatToSnooper("gl_caps[ARB_blend_func_extended]", var2.GL_ARB_blend_func_extended);
      var1.addStatToSnooper("gl_caps[ARB_clear_buffer_object]", var2.GL_ARB_clear_buffer_object);
      var1.addStatToSnooper("gl_caps[ARB_color_buffer_float]", var2.GL_ARB_color_buffer_float);
      var1.addStatToSnooper("gl_caps[ARB_compatibility]", var2.GL_ARB_compatibility);
      var1.addStatToSnooper("gl_caps[ARB_compressed_texture_pixel_storage]", var2.GL_ARB_compressed_texture_pixel_storage);
      var1.addStatToSnooper("gl_caps[ARB_compute_shader]", var2.GL_ARB_compute_shader);
      var1.addStatToSnooper("gl_caps[ARB_copy_buffer]", var2.GL_ARB_copy_buffer);
      var1.addStatToSnooper("gl_caps[ARB_copy_image]", var2.GL_ARB_copy_image);
      var1.addStatToSnooper("gl_caps[ARB_depth_buffer_float]", var2.GL_ARB_depth_buffer_float);
      var1.addStatToSnooper("gl_caps[ARB_compute_shader]", var2.GL_ARB_compute_shader);
      var1.addStatToSnooper("gl_caps[ARB_copy_buffer]", var2.GL_ARB_copy_buffer);
      var1.addStatToSnooper("gl_caps[ARB_copy_image]", var2.GL_ARB_copy_image);
      var1.addStatToSnooper("gl_caps[ARB_depth_buffer_float]", var2.GL_ARB_depth_buffer_float);
      var1.addStatToSnooper("gl_caps[ARB_depth_clamp]", var2.GL_ARB_depth_clamp);
      var1.addStatToSnooper("gl_caps[ARB_depth_texture]", var2.GL_ARB_depth_texture);
      var1.addStatToSnooper("gl_caps[ARB_draw_buffers]", var2.GL_ARB_draw_buffers);
      var1.addStatToSnooper("gl_caps[ARB_draw_buffers_blend]", var2.GL_ARB_draw_buffers_blend);
      var1.addStatToSnooper("gl_caps[ARB_draw_elements_base_vertex]", var2.GL_ARB_draw_elements_base_vertex);
      var1.addStatToSnooper("gl_caps[ARB_draw_indirect]", var2.GL_ARB_draw_indirect);
      var1.addStatToSnooper("gl_caps[ARB_draw_instanced]", var2.GL_ARB_draw_instanced);
      var1.addStatToSnooper("gl_caps[ARB_explicit_attrib_location]", var2.GL_ARB_explicit_attrib_location);
      var1.addStatToSnooper("gl_caps[ARB_explicit_uniform_location]", var2.GL_ARB_explicit_uniform_location);
      var1.addStatToSnooper("gl_caps[ARB_fragment_layer_viewport]", var2.GL_ARB_fragment_layer_viewport);
      var1.addStatToSnooper("gl_caps[ARB_fragment_program]", var2.GL_ARB_fragment_program);
      var1.addStatToSnooper("gl_caps[ARB_fragment_shader]", var2.GL_ARB_fragment_shader);
      var1.addStatToSnooper("gl_caps[ARB_fragment_program_shadow]", var2.GL_ARB_fragment_program_shadow);
      var1.addStatToSnooper("gl_caps[ARB_framebuffer_object]", var2.GL_ARB_framebuffer_object);
      var1.addStatToSnooper("gl_caps[ARB_framebuffer_sRGB]", var2.GL_ARB_framebuffer_sRGB);
      var1.addStatToSnooper("gl_caps[ARB_geometry_shader4]", var2.GL_ARB_geometry_shader4);
      var1.addStatToSnooper("gl_caps[ARB_gpu_shader5]", var2.GL_ARB_gpu_shader5);
      var1.addStatToSnooper("gl_caps[ARB_half_float_pixel]", var2.GL_ARB_half_float_pixel);
      var1.addStatToSnooper("gl_caps[ARB_half_float_vertex]", var2.GL_ARB_half_float_vertex);
      var1.addStatToSnooper("gl_caps[ARB_instanced_arrays]", var2.GL_ARB_instanced_arrays);
      var1.addStatToSnooper("gl_caps[ARB_map_buffer_alignment]", var2.GL_ARB_map_buffer_alignment);
      var1.addStatToSnooper("gl_caps[ARB_map_buffer_range]", var2.GL_ARB_map_buffer_range);
      var1.addStatToSnooper("gl_caps[ARB_multisample]", var2.GL_ARB_multisample);
      var1.addStatToSnooper("gl_caps[ARB_multitexture]", var2.GL_ARB_multitexture);
      var1.addStatToSnooper("gl_caps[ARB_occlusion_query2]", var2.GL_ARB_occlusion_query2);
      var1.addStatToSnooper("gl_caps[ARB_pixel_buffer_object]", var2.GL_ARB_pixel_buffer_object);
      var1.addStatToSnooper("gl_caps[ARB_seamless_cube_map]", var2.GL_ARB_seamless_cube_map);
      var1.addStatToSnooper("gl_caps[ARB_shader_objects]", var2.GL_ARB_shader_objects);
      var1.addStatToSnooper("gl_caps[ARB_shader_stencil_export]", var2.GL_ARB_shader_stencil_export);
      var1.addStatToSnooper("gl_caps[ARB_shader_texture_lod]", var2.GL_ARB_shader_texture_lod);
      var1.addStatToSnooper("gl_caps[ARB_shadow]", var2.GL_ARB_shadow);
      var1.addStatToSnooper("gl_caps[ARB_shadow_ambient]", var2.GL_ARB_shadow_ambient);
      var1.addStatToSnooper("gl_caps[ARB_stencil_texturing]", var2.GL_ARB_stencil_texturing);
      var1.addStatToSnooper("gl_caps[ARB_sync]", var2.GL_ARB_sync);
      var1.addStatToSnooper("gl_caps[ARB_tessellation_shader]", var2.GL_ARB_tessellation_shader);
      var1.addStatToSnooper("gl_caps[ARB_texture_border_clamp]", var2.GL_ARB_texture_border_clamp);
      var1.addStatToSnooper("gl_caps[ARB_texture_buffer_object]", var2.GL_ARB_texture_buffer_object);
      var1.addStatToSnooper("gl_caps[ARB_texture_cube_map]", var2.GL_ARB_texture_cube_map);
      var1.addStatToSnooper("gl_caps[ARB_texture_cube_map_array]", var2.GL_ARB_texture_cube_map_array);
      var1.addStatToSnooper("gl_caps[ARB_texture_non_power_of_two]", var2.GL_ARB_texture_non_power_of_two);
      var1.addStatToSnooper("gl_caps[ARB_uniform_buffer_object]", var2.GL_ARB_uniform_buffer_object);
      var1.addStatToSnooper("gl_caps[ARB_vertex_blend]", var2.GL_ARB_vertex_blend);
      var1.addStatToSnooper("gl_caps[ARB_vertex_buffer_object]", var2.GL_ARB_vertex_buffer_object);
      var1.addStatToSnooper("gl_caps[ARB_vertex_program]", var2.GL_ARB_vertex_program);
      var1.addStatToSnooper("gl_caps[ARB_vertex_shader]", var2.GL_ARB_vertex_shader);
      var1.addStatToSnooper("gl_caps[EXT_bindable_uniform]", var2.GL_EXT_bindable_uniform);
      var1.addStatToSnooper("gl_caps[EXT_blend_equation_separate]", var2.GL_EXT_blend_equation_separate);
      var1.addStatToSnooper("gl_caps[EXT_blend_func_separate]", var2.GL_EXT_blend_func_separate);
      var1.addStatToSnooper("gl_caps[EXT_blend_minmax]", var2.GL_EXT_blend_minmax);
      var1.addStatToSnooper("gl_caps[EXT_blend_subtract]", var2.GL_EXT_blend_subtract);
      var1.addStatToSnooper("gl_caps[EXT_draw_instanced]", var2.GL_EXT_draw_instanced);
      var1.addStatToSnooper("gl_caps[EXT_framebuffer_multisample]", var2.GL_EXT_framebuffer_multisample);
      var1.addStatToSnooper("gl_caps[EXT_framebuffer_object]", var2.GL_EXT_framebuffer_object);
      var1.addStatToSnooper("gl_caps[EXT_framebuffer_sRGB]", var2.GL_EXT_framebuffer_sRGB);
      var1.addStatToSnooper("gl_caps[EXT_geometry_shader4]", var2.GL_EXT_geometry_shader4);
      var1.addStatToSnooper("gl_caps[EXT_gpu_program_parameters]", var2.GL_EXT_gpu_program_parameters);
      var1.addStatToSnooper("gl_caps[EXT_gpu_shader4]", var2.GL_EXT_gpu_shader4);
      var1.addStatToSnooper("gl_caps[EXT_multi_draw_arrays]", var2.GL_EXT_multi_draw_arrays);
      var1.addStatToSnooper("gl_caps[EXT_packed_depth_stencil]", var2.GL_EXT_packed_depth_stencil);
      var1.addStatToSnooper("gl_caps[EXT_paletted_texture]", var2.GL_EXT_paletted_texture);
      var1.addStatToSnooper("gl_caps[EXT_rescale_normal]", var2.GL_EXT_rescale_normal);
      var1.addStatToSnooper("gl_caps[EXT_separate_shader_objects]", var2.GL_EXT_separate_shader_objects);
      var1.addStatToSnooper("gl_caps[EXT_shader_image_load_store]", var2.GL_EXT_shader_image_load_store);
      var1.addStatToSnooper("gl_caps[EXT_shadow_funcs]", var2.GL_EXT_shadow_funcs);
      var1.addStatToSnooper("gl_caps[EXT_shared_texture_palette]", var2.GL_EXT_shared_texture_palette);
      var1.addStatToSnooper("gl_caps[EXT_stencil_clear_tag]", var2.GL_EXT_stencil_clear_tag);
      var1.addStatToSnooper("gl_caps[EXT_stencil_two_side]", var2.GL_EXT_stencil_two_side);
      var1.addStatToSnooper("gl_caps[EXT_stencil_wrap]", var2.GL_EXT_stencil_wrap);
      var1.addStatToSnooper("gl_caps[EXT_texture_3d]", var2.GL_EXT_texture_3d);
      var1.addStatToSnooper("gl_caps[EXT_texture_array]", var2.GL_EXT_texture_array);
      var1.addStatToSnooper("gl_caps[EXT_texture_buffer_object]", var2.GL_EXT_texture_buffer_object);
      var1.addStatToSnooper("gl_caps[EXT_texture_integer]", var2.GL_EXT_texture_integer);
      var1.addStatToSnooper("gl_caps[EXT_texture_lod_bias]", var2.GL_EXT_texture_lod_bias);
      var1.addStatToSnooper("gl_caps[EXT_texture_sRGB]", var2.GL_EXT_texture_sRGB);
      var1.addStatToSnooper("gl_caps[EXT_vertex_shader]", var2.GL_EXT_vertex_shader);
      var1.addStatToSnooper("gl_caps[EXT_vertex_weighting]", var2.GL_EXT_vertex_weighting);
      var1.addStatToSnooper("gl_caps[gl_max_vertex_uniforms]", GL11.glGetInteger(35658));
      GL11.glGetError();
      var1.addStatToSnooper("gl_caps[gl_max_fragment_uniforms]", GL11.glGetInteger(35657));
      GL11.glGetError();
      var1.addStatToSnooper("gl_caps[gl_max_vertex_attribs]", GL11.glGetInteger(34921));
      GL11.glGetError();
      var1.addStatToSnooper("gl_caps[gl_max_vertex_texture_image_units]", GL11.glGetInteger(35660));
      GL11.glGetError();
      var1.addStatToSnooper("gl_caps[gl_max_texture_image_units]", GL11.glGetInteger(34930));
      GL11.glGetError();
      var1.addStatToSnooper("gl_caps[gl_max_texture_image_units]", GL11.glGetInteger(35071));
      GL11.glGetError();
      var1.addStatToSnooper("gl_max_texture_size", getGLMaximumTextureSize());
   }

   public void registerMetadataSerializers() {
      this.metadataSerializer_.registerMetadataSectionType(new TextureMetadataSectionSerializer(), TextureMetadataSection.class);
      this.metadataSerializer_.registerMetadataSectionType(new FontMetadataSectionSerializer(), FontMetadataSection.class);
      this.metadataSerializer_.registerMetadataSectionType(new AnimationMetadataSectionSerializer(), AnimationMetadataSection.class);
      this.metadataSerializer_.registerMetadataSectionType(new PackMetadataSectionSerializer(), PackMetadataSection.class);
      this.metadataSerializer_.registerMetadataSectionType(new LanguageMetadataSectionSerializer(), LanguageMetadataSection.class);
   }

   public <V> ListenableFuture<V> addScheduledTask(Callable<V> var1) {
      Validate.notNull(var1);
      if (!this.isCallingFromMinecraftThread()) {
         ListenableFutureTask var2 = ListenableFutureTask.create(var1);
         synchronized (this.recoveredField3829) {
            this.recoveredField3829.add(var2);
            return var2;
         }
      } else {
         try {
            return Futures.immediateFuture((V)var1.call());
         } catch (Exception var6) {
            return Futures.immediateFailedCheckedFuture(var6);
         }
      }
   }

   public Entity getRenderViewEntity() {
      return this.renderViewEntity;
   }

   public boolean isSingleplayer() {
      return this.integratedServerIsRunning && this.theIntegratedServer != null;
   }

   public void method_20403() {
      try {
         this.fullscreen = !this.fullscreen;
         if (this.fullscreen) {
            this.method_20416();
            this.displayWidth = Display.getDisplayMode().getWidth();
            this.displayHeight = Display.getDisplayMode().getHeight();
            if (CheatBreaker.getInstance().getGlobalSettings().recoveredField576.method_08908()) {
               this.recoveredField3815 = true;
               System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
               GraphicsEnvironment var1 = GraphicsEnvironment.getLocalGraphicsEnvironment();
               Rectangle var2 = var1.getDefaultScreenDevice().getDefaultConfiguration().getBounds();
               this.displayWidth = (int)var2.getWidth();
               this.displayHeight = (int)var2.getHeight();
               Display.setLocation(var2.x, var2.y);
               Display.setResizable(false);
               Display.setDisplayMode(new DisplayMode(this.displayWidth, this.displayHeight));
            } else {
               this.recoveredField3815 = false;
            }
         } else {
            if (this.recoveredField3815) {
               System.setProperty("org.lwjgl.opengl.Window.undecorated", "false");
               Display.setResizable(true);
               Display.setLocation(Display.getWidth() / 4, Display.getHeight() / 4);
            }

            this.recoveredField3815 = false;
            Display.setDisplayMode(new DisplayMode(this.recoveredField3838, this.recoveredField3835));
            this.displayWidth = this.recoveredField3838;
            this.displayHeight = this.recoveredField3835;
         }

         if (this.displayWidth <= 0) {
            this.displayWidth = 1;
         }

         if (this.displayHeight <= 0) {
            this.displayHeight = 1;
         }

         if (this.currentScreen != null) {
            this.resize(this.displayWidth, this.displayHeight);
         } else {
            this.updateFramebufferSize();
         }

         Display.setFullscreen(this.fullscreen);
         Display.setVSyncEnabled(this.gameSettings.enableVsync);
         this.updateDisplay();
      } catch (Exception var3) {
         logger.error("Couldn't toggle fullscreen", var3);
      }
   }

   public void startTimerHackThread() {
      Thread var1 = new Thread("Timer hack thread") {
         @Override
         public void run() {
            while (Minecraft.this.running) {
               try {
                  Thread.sleep(2147483647L);
               } catch (InterruptedException var2) {
               }
            }
         }
      };
      var1.setDaemon(true);
      var1.start();
   }

   public FrameTimer getFrameTimer() {
      return this.recoveredField3834;
   }

   public int getLimitFramerate() {
      if (this.theWorld == null && this.currentScreen != null) {
         return CheatBreaker.getInstance().getGlobalSettings().recoveredField535.method_08912();
      } else if (!Display.isActive() && CheatBreaker.getInstance().getGlobalSettings().recoveredField596.method_08908()) {
         return CheatBreaker.getInstance().getGlobalSettings().recoveredField591.method_08912();
      } else if (this.currentScreen instanceof GuiIngameMenu && CheatBreaker.getInstance().getGlobalSettings().recoveredField521.method_08908()) {
         return CheatBreaker.getInstance().getGlobalSettings().recoveredField539.method_08912();
      } else {
         return CheatBreaker.getInstance().getGlobalSettings().recoveredField562.method_08908()
            ? CheatBreaker.getInstance().getGlobalSettings().recoveredField528.method_08912()
            : this.gameSettings.limitFramerate;
      }
   }

   public MusicTicker getMusicTicker() {
      return this.mcMusicTicker;
   }

   public PlayerUsageSnooper getPlayerUsageSnooper() {
      return this.usageSnooper;
   }

   public static boolean isFancyGraphicsEnabled() {
      return theMinecraft != null && theMinecraft.gameSettings.fancyGraphics;
   }

   public MusicTicker.MusicType getAmbientMusicType() {
      return this.thePlayer != null
         ? (
            this.thePlayer.o.t instanceof WorldProviderHell
               ? MusicTicker.MusicType.NETHER
               : (
                  this.thePlayer.o.t instanceof WorldProviderEnd
                     ? (BossStatus.bossName != null && BossStatus.statusBarTime > 0 ? MusicTicker.MusicType.END_BOSS : MusicTicker.MusicType.END)
                     : (this.thePlayer.bA.isCreativeMode && this.thePlayer.bA.allowFlying ? MusicTicker.MusicType.CREATIVE : MusicTicker.MusicType.GAME)
               )
         )
         : MusicTicker.MusicType.MENU;
   }

   public MinecraftSessionService getSessionService() {
      return this.sessionService;
   }

   public boolean method_20336() {
      return this.recoveredField3839;
   }

   public void checkGLError(String var1) {
      if (this.recoveredField3833) {
         int var2 = GL11.glGetError();
         if (var2 != 0) {
            String var3 = GLU.gluErrorString(var2);
            logger.error("########## GL ERROR ##########");
            logger.error("@ " + var1);
            logger.error(var2 + ": " + var3);
         }
      }
   }

   public IStream getTwitchStream() {
      return this.stream;
   }

   public TextureManager getTextureManager() {
      return this.renderEngine;
   }

   public void method_20416() throws org.lwjgl.LWJGLException {
      HashSet var1 = Sets.newHashSet();
      Collections.addAll(var1, Display.getAvailableDisplayModes());
      DisplayMode var2 = Display.getDesktopDisplayMode();
      if (!var1.contains(var2) && Util.getOSType() == Util.EnumOS.OSX) {
         for (DisplayMode var4 : macDisplayModes) {
            boolean var5 = true;

            for (DisplayMode var7 : (Iterable<DisplayMode>)(Iterable<?>)(var1)) {
               if (var7.getBitsPerPixel() == 32 && var7.getWidth() == var4.getWidth() && var7.getHeight() == var4.getHeight()) {
                  var5 = false;
                  break;
               }
            }

            if (!var5) {
               for (DisplayMode var9 : (Iterable<DisplayMode>)(Iterable<?>)(var1)) {
                  if (var9.getBitsPerPixel() == 32 && var9.getWidth() == var4.getWidth() / 2 && var9.getHeight() == var4.getHeight() / 2) {
                     var2 = var9;
                     break;
                  }
               }
            }
         }
      }

      Display.setDisplayMode(var2);
      this.displayWidth = var2.getWidth();
      this.displayHeight = var2.getHeight();
   }

   public void updateFramebufferSize() {
      this.framebufferMc.createBindFramebuffer(this.displayWidth, this.displayHeight);
      if (this.entityRenderer != null) {
         this.entityRenderer.updateShaderGroupSize(this.displayWidth, this.displayHeight);
      }
   }

   public void method_20334() throws org.lwjgl.LWJGLException {
      this.gameSettings = new GameSettings(this, this.mcDataDir);
      this.defaultResourcePacks.add(this.mcDefaultResourcePack);
      this.startTimerHackThread();
      if (this.gameSettings.recoveredField2684 > 0 && this.gameSettings.recoveredField2686 > 0) {
         this.displayWidth = this.gameSettings.recoveredField2686;
         this.displayHeight = this.gameSettings.recoveredField2684;
      }

      logger.info("LWJGL Version: " + Sys.getVersion());
      this.method_20365();
      this.setInitialDisplayMode();
      this.createDisplay();
      OpenGlHelper.initializeTextures();
      this.framebufferMc = new Framebuffer(this.displayWidth, this.displayHeight, true);
      this.framebufferMc.setFramebufferColor(0.0F, 0.0F, 0.0F, 0.0F);
      this.registerMetadataSerializers();
      this.mcResourcePackRepository = new ResourcePackRepository(
         this.recoveredField3817, new File(this.mcDataDir, "server-resource-packs"), this.mcDefaultResourcePack, this.metadataSerializer_, this.gameSettings
      );
      this.mcResourceManager = new SimpleReloadableResourceManager(this.metadataSerializer_);
      this.mcLanguageManager = new LanguageManager(this.metadataSerializer_, this.gameSettings.language);
      this.mcResourceManager.registerReloadListener(this.mcLanguageManager);
      this.refreshResources();
      this.renderEngine = new TextureManager(this.mcResourceManager);
      this.mcResourceManager.registerReloadListener(this.renderEngine);
      this.recoveredField3820 = new StartupLoadingGui(115);
      this.recoveredField3820.drawMenu(0.0F, 0.0F);
      this.recoveredField3820.method_25824("Skins");
      this.initStream();
      this.skinManager = new SkinManager(this.renderEngine, new File(this.recoveredField3810, "skins"), this.sessionService);
      this.recoveredField3820.method_25824("Sound Handler");
      this.saveLoader = new AnvilSaveConverter(new File(this.mcDataDir, "saves"));
      this.mcSoundHandler = new SoundHandler(this.mcResourceManager, this.gameSettings);
      this.mcResourceManager.registerReloadListener(this.mcSoundHandler);
      this.mcMusicTicker = new MusicTicker(this);
      this.recoveredField3820.method_25824("Font Renderers");
      this.fontRendererObj = new FontRenderer(this.gameSettings, new ResourceLocation("textures/font/ascii.png"), this.renderEngine, false);
      if (this.gameSettings.language != null) {
         this.fontRendererObj.setUnicodeFlag(this.isUnicode());
         this.fontRendererObj.setBidiFlag(this.mcLanguageManager.isCurrentLanguageBidirectional());
      }

      this.standardGalacticFontRenderer = new FontRenderer(this.gameSettings, new ResourceLocation("textures/font/ascii_sga.png"), this.renderEngine, false);
      this.mcResourceManager.registerReloadListener(this.fontRendererObj);
      this.mcResourceManager.registerReloadListener(this.standardGalacticFontRenderer);
      this.mcResourceManager.registerReloadListener(new GrassColorReloadListener());
      this.mcResourceManager.registerReloadListener(new FoliageColorReloadListener());
      this.recoveredField3820.method_25824("Items");
      AchievementList.openInventory.setStatStringFormatter(new IStatStringFormat() {
         @Override
         public String formatString(String var1) {
            try {
               return String.format(var1, GameSettings.getKeyDisplayString(Minecraft.this.gameSettings.keyBindInventory.getKeyCode()));
            } catch (Exception var3) {
               return "Error: " + var3.getLocalizedMessage();
            }
         }
      });
      this.mouseHelper = new MouseHelper();
      this.checkGLError("Pre startup");
      GlStateManager.enableTexture2D();
      GlStateManager.shadeModel(7425);
      GlStateManager.clearDepth(1.0);
      GlStateManager.enableDepth();
      GlStateManager.depthFunc(515);
      GlStateManager.enableAlpha();
      GlStateManager.alphaFunc(516, 0.1F);
      GlStateManager.cullFace(1029);
      GlStateManager.matrixMode(5889);
      GlStateManager.loadIdentity();
      GlStateManager.matrixMode(5888);
      this.checkGLError("Startup");
      this.recoveredField3820.method_25824("Textures");
      this.textureMapBlocks = new TextureMap("textures");
      this.textureMapBlocks.setMipmapLevels(this.gameSettings.mipmapLevels);
      this.renderEngine.loadTickableTexture(TextureMap.locationBlocksTexture, this.textureMapBlocks);
      this.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
      this.textureMapBlocks.setBlurMipmapDirect(false, this.gameSettings.mipmapLevels > 0);
      this.modelManager = new ModelManager(this.textureMapBlocks);
      this.mcResourceManager.registerReloadListener(this.modelManager);
      this.renderItem = new RenderItem(this.renderEngine, this.modelManager);
      this.renderManager = new RenderManager(this.renderEngine, this.renderItem);
      this.itemRenderer = new ItemRenderer(this);
      this.mcResourceManager.registerReloadListener(this.renderItem);
      this.recoveredField3820.method_25824("Entities");
      this.entityRenderer = new EntityRenderer(this, this.mcResourceManager);
      this.mcResourceManager.registerReloadListener(this.entityRenderer);
      this.recoveredField3820.method_25824("Blocks");
      this.blockRenderDispatcher = new BlockRendererDispatcher(this.modelManager.getBlockModelShapes(), this.gameSettings);
      this.mcResourceManager.registerReloadListener(this.blockRenderDispatcher);
      this.recoveredField3820.method_25824("World");
      this.renderGlobal = new RenderGlobal(this);
      this.mcResourceManager.registerReloadListener(this.renderGlobal);
      this.recoveredField3820.method_25824("Achievements");
      this.guiAchievement = new GuiAchievement(this);
      GlStateManager.viewport(0, 0, this.displayWidth, this.displayHeight);
      this.recoveredField3820.method_25824("Effects");
      this.effectRenderer = new EffectRenderer(this.theWorld, this.renderEngine);
      this.checkGLError("Post startup");
      this.ingameGUI = new GuiIngame(this);
      new CheatBreaker().method_19788();
      if (this.recoveredField3811 != null) {
         this.displayGuiScreen(new GuiConnecting(new LegacyMainMenu(), this, this.recoveredField3811, this.recoveredField3822));
      } else {
         this.displayGuiScreen(new LegacyMainMenu());
      }

      this.recoveredField3820 = null;
      this.renderEngine.deleteTexture(this.mojangLogo);
      this.mojangLogo = null;
      this.loadingScreen = new LoadingScreenRenderer(this);
      if (this.gameSettings.fullScreen && !this.fullscreen) {
         this.method_20403();
      }

      try {
         Display.setVSyncEnabled(this.gameSettings.enableVsync);
      } catch (OpenGLException var2) {
         this.gameSettings.enableVsync = false;
         this.gameSettings.saveOptions();
      }

      this.renderGlobal.makeEntityOutlineShader();
   }

   public static Map<String, String> getSessionInfo() {
      HashMap var0 = Maps.newHashMap();
      var0.put("X-Minecraft-Username", getMinecraft().getSession().getUsername());
      var0.put("X-Minecraft-UUID", getMinecraft().getSession().getPlayerID());
      var0.put("X-Minecraft-Version", "1.8.9");
      return var0;
   }

   @Override
   public boolean isSnooperEnabled() {
      return this.gameSettings.snooperEnabled;
   }

   @Override
   public ListenableFuture<Object> addScheduledTask(Runnable var1) {
      Validate.notNull(var1);
      return this.addScheduledTask(Executors.callable(var1));
   }

   public static long getSystemTime() {
      return Sys.getTime() * 1000L / Sys.getTimerResolution();
   }

   public boolean isFullScreen() {
      return this.fullscreen;
   }

   public void method_20407() {
      this.playerController.resetBlockRemoving();
      if (!this.playerController.getIsHittingBlock()) {
         this.recoveredField3825 = 4;
         boolean var1 = true;
         ItemStack var2 = this.thePlayer.bi.getCurrentItem();
         if (this.objectMouseOver == null) {
            logger.warn("Null returned as 'hitResult', this shouldn't happen!");
         } else {
            switch (this.objectMouseOver.typeOfHit) {
               case ENTITY:
                  if (this.playerController.isPlayerRightClickingOnEntity(this.thePlayer, this.objectMouseOver.entityHit, this.objectMouseOver)) {
                     var1 = false;
                  } else if (this.playerController.interactWithEntitySendPacket(this.thePlayer, this.objectMouseOver.entityHit)) {
                     var1 = false;
                  }
                  break;
               case BLOCK:
                  BlockPos var3 = this.objectMouseOver.getBlockPos();
                  if (this.theWorld.getBlockState(var3).getBlock().getMaterial() != Material.air) {
                     int var4 = var2 != null ? var2.stackSize : 0;
                     if (this.playerController
                        .onPlayerRightClick(this.thePlayer, this.theWorld, var2, var3, this.objectMouseOver.sideHit, this.objectMouseOver.hitVec)) {
                        var1 = false;
                        this.thePlayer.swingItem();
                     }

                     if (var2 == null) {
                        return;
                     }

                     if (var2.stackSize == 0) {
                        this.thePlayer.bi.mainInventory[this.thePlayer.bi.currentItem] = null;
                     } else if (var2.stackSize != var4 || this.playerController.isInCreativeMode()) {
                        this.entityRenderer.itemRenderer.method_25647();
                     }
                  }
            }
         }

         if (var1) {
            ItemStack var5 = this.thePlayer.bi.getCurrentItem();
            if (var5 != null && this.playerController.sendUseItem(this.thePlayer, this.theWorld, var5)) {
               this.entityRenderer.itemRenderer.method_25662();
            }
         }
      }
   }

   public LanguageManager getLanguageManager() {
      return this.mcLanguageManager;
   }

   public String method_20429() {
      return this.theIntegratedServer != null
         ? (this.theIntegratedServer.getPublic() ? "hosting_lan" : "singleplayer")
         : (this.currentServerData != null ? (this.currentServerData.isOnLAN() ? "playing_lan" : "multiplayer") : "out_of_game");
   }

   public static boolean isJvm64bit() {
      String[] var0 = new String[]{"sun.arch.data.model", "com.ibm.vm.bitmode", "os.arch"};

      for (String var4 : var0) {
         String var5 = System.getProperty(var4);
         if (var5 != null && var5.contains("64")) {
            return true;
         }
      }

      return false;
   }

   public boolean method_20351() {
      return this.recoveredField3819;
   }

   public void method_20340() {
      if (Display.isActive() && !this.recoveredField3812) {
         this.recoveredField3812 = true;
         this.mouseHelper.grabMouseCursor();
         this.displayGuiScreen((GuiScreen)null);
         this.recoveredField3809 = 10000;
      }
   }

   public void method_20358() {
      while (this.gameSettings.recoveredField2694.isPressed()) {
         this.gameSettings.thirdPersonView++;
         if (this.gameSettings.thirdPersonView > 2) {
            this.gameSettings.thirdPersonView = 0;
         }

         if (!CheatBreaker.getInstance().getModuleManager().recoveredField1715.isEnabled()) {
            if (this.gameSettings.thirdPersonView == 0) {
               this.entityRenderer.loadEntityShader(this.getRenderViewEntity());
            } else if (this.gameSettings.thirdPersonView == 1) {
               this.entityRenderer.loadEntityShader(null);
            }
         }

         this.renderGlobal.setDisplayListEntitiesDirty();
      }

      while (CheatBreaker.getInstance().getGlobalSettings().recoveredField494.isPressed()) {
         if (!CheatBreaker.getInstance().getGlobalSettings().recoveredField480.method_08908()
            || !CheatBreaker.getInstance().getGlobalSettings().recoveredField527.method_08908()) {
            this.displayGuiScreen(new CBModulesGui());
         } else if (CheatBreaker.getInstance().getGlobalSettings().recoveredField505.method_08908()) {
            String var1 = CheatBreaker.getInstance().getGlobalSettings().recoveredField497.method_08908()
               ? "hold "
                  + EnumChatFormatting.GOLD
                  + "Mods"
                  + EnumChatFormatting.RESET
                  + " for "
                  + EnumChatFormatting.AQUA
                  + CheatBreaker.getInstance().getGlobalSettings().recoveredField551.method_08905()
                  + " seconds"
               : "click " + EnumChatFormatting.GOLD + "Mods";
            CheatBreaker.getInstance()
               .getModuleManager()
               .notifications
               .queueNotification(
                  "info",
                  "Streamer mode is "
                     + EnumChatFormatting.GREEN
                     + "enabled"
                     + EnumChatFormatting.RESET
                     + "! Press escape and "
                     + var1
                     + EnumChatFormatting.RESET
                     + " to open the Mod Menu.",
                  5000L
               );
         }
      }

      while (CheatBreaker.getInstance().getGlobalSettings().recoveredField498.isPressed()) {
         if (CheatBreaker.getInstance().getModuleManager().recoveredField1723.isEnabled()
            && !CheatBreaker.getInstance().getModuleManager().recoveredField1723.method_28796()) {
            CheatBreaker.getInstance().getModuleManager().recoveredField1728.method_23799();
         }
      }

      while (CheatBreaker.getInstance().getGlobalSettings().recoveredField578.isPressed()) {
         CheatBreaker.getInstance().recoveredField1571 = !CheatBreaker.getInstance().recoveredField1571;
      }

      while (CheatBreaker.getInstance().getGlobalSettings().recoveredField482.isPressed()) {
         CheatBreaker.getInstance().getModuleManager().recoveredField1702.method_21021(1);
      }

      while (CheatBreaker.getInstance().getGlobalSettings().recoveredField479.isPressed()) {
         CheatBreaker.getInstance().getModuleManager().recoveredField1702.method_21021(2);
      }
   }

   public SoundHandler getSoundHandler() {
      return this.mcSoundHandler;
   }

   public boolean isGamePaused() {
      return this.recoveredField3824;
   }

   public void refreshResources() {
      ArrayList var1 = Lists.newArrayList(this.defaultResourcePacks);

      for (ResourcePackRepository.Entry var3 : this.mcResourcePackRepository.getRepositoryEntries()) {
         var1.add(var3.getResourcePack());
      }

      if (this.mcResourcePackRepository.getResourcePackInstance() != null) {
         var1.add(this.mcResourcePackRepository.getResourcePackInstance());
      }

      try {
         this.mcResourceManager.reloadResources(var1);
      } catch (RuntimeException var4) {
         logger.info("Caught error stitching, removing all assigned resourcepacks", var4);
         var1.clear();
         var1.addAll(this.defaultResourcePacks);
         this.mcResourcePackRepository.setRepositories(Collections.emptyList());
         this.mcResourceManager.reloadResources(var1);
         this.gameSettings.resourcePacks.clear();
         this.gameSettings.incompatibleResourcePacks.clear();
         this.gameSettings.saveOptions();
      }

      this.mcLanguageManager.parseLanguageMetadata(var1);
      if (this.renderGlobal != null) {
         this.renderGlobal.loadRenderers();
      }
   }

   public void initStream() {
      try {
         this.stream = new TwitchStream(this, Iterables.getFirst(this.twitchDetails.get("twitch_access_token"), null));
      } catch (Throwable var2) {
         this.stream = new NullStream(var2);
         logger.error("Couldn't initialize twitch stream");
      }
   }

   public void method_20434() {
      try {
         memoryReserve = new byte[0];
         this.renderGlobal.deleteAllDisplayLists();
      } catch (Throwable var3) {
      }

      try {
         System.gc();
         this.loadWorld((WorldClient)null);
      } catch (Throwable var2) {
      }

      System.gc();
   }

   public static Minecraft getMinecraft() {
      return theMinecraft;
   }

   public ISaveFormat getSaveLoader() {
      return this.saveLoader;
   }

   public void method_20365() {
      Util.EnumOS var1 = Util.getOSType();
      if (var1 != Util.EnumOS.OSX) {
         InputStream var2 = null;
         InputStream var3 = null;

         try {
            var2 = this.mcDefaultResourcePack.getInputStream(new ResourceLocation("client/icon-1.png"));
            var3 = this.mcDefaultResourcePack.getInputStream(new ResourceLocation("client/icon-2.png"));
            if (var2 != null && var3 != null) {
               Display.setIcon(new ByteBuffer[]{this.readImageToBuffer(var2), this.readImageToBuffer(var3)});
            }
         } catch (IOException var8) {
            logger.error("Couldn't set icon", var8);
         } finally {
            IOUtils.closeQuietly(var2);
            IOUtils.closeQuietly(var3);
         }
      }
   }

   public void checkWindowResize() {
      if (!this.fullscreen && Display.wasResized()) {
         int var1 = this.displayWidth;
         int var2 = this.displayHeight;
         this.displayWidth = Display.getWidth();
         this.displayHeight = Display.getHeight();
         if (this.displayWidth != var1 || this.displayHeight != var2) {
            if (this.displayWidth <= 0) {
               this.displayWidth = 1;
            }

            if (this.displayHeight <= 0) {
               this.displayHeight = 1;
            }

            this.resize(this.displayWidth, this.displayHeight);
         }
      }
   }

   public void crashed(CrashReport var1) {
      this.hasCrashed = true;
      this.crashReporter = var1;
   }

   public PropertyMap getProfileProperties() {
      if (this.profileProperties.isEmpty()) {
         GameProfile var1 = this.getSessionService().fillProfileProperties(this.session.getProfile(), false);
         this.profileProperties.putAll(var1.getProperties());
      }

      return this.profileProperties;
   }

   public void setDimensionAndSpawnPlayer(int var1) {
      this.theWorld.setInitialSpawnLocation();
      this.theWorld.removeAllEntities();
      int var2 = 0;
      String var3 = null;
      if (this.thePlayer != null) {
         var2 = this.thePlayer.F();
         this.theWorld.removeEntity(this.thePlayer);
         var3 = this.thePlayer.getClientBrand();
      }

      this.renderViewEntity = null;
      EntityPlayerSP var4 = this.thePlayer;
      this.thePlayer = this.playerController.func_178892_a(this.theWorld, this.thePlayer == null ? new StatFileWriter() : this.thePlayer.getStatFileWriter());
      this.thePlayer.H().updateWatchedObjectsFromList(var4.H().getAllWatched());
      this.thePlayer.am = var1;
      this.renderViewEntity = this.thePlayer;
      this.thePlayer.I();
      this.thePlayer.setClientBrand(var3);
      this.theWorld.spawnEntityInWorld(this.thePlayer);
      this.playerController.flipPlayer(this.thePlayer);
      this.thePlayer.movementInput = new MovementInputFromOptions(this.gameSettings);
      this.thePlayer.setEntityId(var2);
      this.playerController.setPlayerCapabilities(this.thePlayer);
      this.thePlayer.setReducedDebug(var4.hasReducedDebug());
      if (this.currentScreen instanceof GuiGameOver) {
         this.displayGuiScreen(null);
      }
   }

   public void loadWorld(WorldClient var1) {
      this.loadWorld(var1, "");
   }

   public void setRenderViewEntity(Entity var1) {
      this.renderViewEntity = var1;
      this.entityRenderer.loadEntityShader(var1);
   }

   public static int getGLMaximumTextureSize() {
      for (int var0 = 16384; var0 > 0; var0 >>= 1) {
         GL11.glTexImage2D(32868, 0, 6408, var0, var0, 0, 6408, 5121, (ByteBuffer)null);
         int var1 = GL11.glGetTexLevelParameteri(32868, 0, 4096);
         if (var1 != 0) {
            return var0;
         }
      }

      return -1;
   }

   public void updateDisplay() {
      this.mcProfiler.startSection("display_update");
      Display.update();
      this.mcProfiler.endSection();
      this.checkWindowResize();
   }

   public void method_20346() {
      try {
         CheatBreaker.getInstance().configManager.method_25109();
         CheatBreaker.getInstance().getAssetsWebSocket().close();
         this.stream.shutdownStream();
         logger.info("Stopping!");

         try {
            this.loadWorld(null);
         } catch (Throwable var5) {
         }

         this.mcSoundHandler.method_27826();
      } finally {
         WindowsImeSupport.restoreGameInput();
         Display.destroy();
         if (!this.hasCrashed) {
            System.exit(0);
         }
      }

      System.gc();
   }

   public ByteBuffer readImageToBuffer(InputStream var1) throws java.io.IOException {
      BufferedImage var2 = ImageIO.read(var1);
      int[] var3 = var2.getRGB(0, 0, var2.getWidth(), var2.getHeight(), (int[])null, 0, var2.getWidth());
      ByteBuffer var4 = ByteBuffer.allocate(4 * var3.length);

      for (int var8 : var3) {
         var4.putInt(var8 << 8 | var8 >> 24 & 0xFF);
      }

      ((Buffer)var4).flip();
      return var4;
   }

   public void displayCrashReport(CrashReport var1) {
      File var2 = new File(getMinecraft().mcDataDir, "crash-reports");
      File var3 = new File(var2, "crash-" + new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date()) + "-client.txt");
      Bootstrap.printToSYSOUT(var1.getCompleteReport());
      if (var1.getFile() != null) {
         Bootstrap.printToSYSOUT("#@!@# Game crashed! Crash report saved to: #@!@# " + var1.getFile());
         System.exit(-1);
      } else if (var1.saveToFile(var3)) {
         Bootstrap.printToSYSOUT("#@!@# Game crashed! Crash report saved to: #@!@# " + var3.getAbsolutePath());
         System.exit(-1);
      } else {
         Bootstrap.printToSYSOUT("#@?@# Game crashed! Crash report could not be saved. #@?@#");
         System.exit(-2);
      }
   }

   public void displayDebugInfo(long var1) {
      if (this.mcProfiler.profilingEnabled) {
         List var3 = this.mcProfiler.getProfilingData(this.recoveredField3814);
         Profiler.Result var4 = (Profiler.Result)var3.remove(0);
         GlStateManager.clear(256);
         GlStateManager.matrixMode(5889);
         GlStateManager.enableColorMaterial();
         GlStateManager.loadIdentity();
         GlStateManager.ortho(0.0, this.displayWidth, this.displayHeight, 0.0, 1000.0, 3000.0);
         GlStateManager.matrixMode(5888);
         GlStateManager.loadIdentity();
         GlStateManager.translate(0.0F, 0.0F, -2000.0F);
         GL11.glLineWidth(1.0F);
         GlStateManager.disableTexture2D();
         Tessellator var5 = Tessellator.getInstance();
         WorldRenderer var6 = var5.getWorldRenderer();
         short var7 = 160;
         int var8 = this.displayWidth - var7 - 10;
         int var9 = this.displayHeight - var7 * 2;
         GlStateManager.enableBlend();
         var6.begin(7, DefaultVertexFormats.POSITION_COLOR);
         var6.pos(var8 - var7 * 1.1F, var9 - var7 * 0.6F - 16.0F, 0.0).color(200, 0, 0, 0).endVertex();
         var6.pos(var8 - var7 * 1.1F, var9 + var7 * 2, 0.0).color(200, 0, 0, 0).endVertex();
         var6.pos(var8 + var7 * 1.1F, var9 + var7 * 2, 0.0).color(200, 0, 0, 0).endVertex();
         var6.pos(var8 + var7 * 1.1F, var9 - var7 * 0.6F - 16.0F, 0.0).color(200, 0, 0, 0).endVertex();
         var5.draw();
         GlStateManager.disableBlend();
         double var10 = 0.0;

         for (int var12 = 0; var12 < var3.size(); var12++) {
            Profiler.Result var13 = (Profiler.Result)var3.get(var12);
            int var14 = MathHelper.floor_double(var13.field_76332_a / 4.0) + 1;
            var6.begin(6, DefaultVertexFormats.POSITION_COLOR);
            int var15 = var13.getColor();
            int var16 = var15 >> 16 & 0xFF;
            int var17 = var15 >> 8 & 0xFF;
            int var18 = var15 & 0xFF;
            var6.pos(var8, var9, 0.0).color(var16, var17, var18, 255).endVertex();

            for (int var19 = var14; var19 >= 0; var19--) {
               float var20 = (float)((var10 + var13.field_76332_a * var19 / var14) * Math.PI * 2.0 / 100.0);
               float var21 = MathHelper.sin(var20) * var7;
               float var22 = MathHelper.cos(var20) * var7 * 0.5F;
               var6.pos(var8 + var21, var9 - var22, 0.0).color(var16, var17, var18, 255).endVertex();
            }

            var5.draw();
            var6.begin(5, DefaultVertexFormats.POSITION_COLOR);

            for (int var35 = var14; var35 >= 0; var35--) {
               float var36 = (float)((var10 + var13.field_76332_a * var35 / var14) * Math.PI * 2.0 / 100.0);
               float var37 = MathHelper.sin(var36) * var7;
               float var38 = MathHelper.cos(var36) * var7 * 0.5F;
               var6.pos(var8 + var37, var9 - var38, 0.0).color(var16 >> 1, var17 >> 1, var18 >> 1, 255).endVertex();
               var6.pos(var8 + var37, var9 - var38 + 10.0F, 0.0).color(var16 >> 1, var17 >> 1, var18 >> 1, 255).endVertex();
            }

            var5.draw();
            var10 += var13.field_76332_a;
         }

         DecimalFormat var23 = new DecimalFormat("##0.00");
         GlStateManager.enableTexture2D();
         String var24 = "";
         if (!var4.field_76331_c.equals("unspecified")) {
            var24 = var24 + "[0] ";
         }

         if (var4.field_76331_c.length() == 0) {
            var24 = var24 + "ROOT ";
         } else {
            var24 = var24 + var4.field_76331_c + " ";
         }

         int var27 = 16777215;
         this.fontRendererObj.drawStringWithShadow(var24, var8 - var7, var9 - var7 / 2 - 16, var27);
         this.fontRendererObj
            .drawStringWithShadow(
               var24 = var23.format(var4.field_76330_b) + "%", var8 + var7 - this.fontRendererObj.getStringWidth(var24), var9 - var7 / 2 - 16, var27
            );

         for (int var28 = 0; var28 < var3.size(); var28++) {
            Profiler.Result var29 = (Profiler.Result)var3.get(var28);
            String var30 = "";
            if (var29.field_76331_c.equals("unspecified")) {
               var30 = var30 + "[?] ";
            } else {
               var30 = var30 + "[" + (var28 + 1) + "] ";
            }

            var30 = var30 + var29.field_76331_c;
            this.fontRendererObj.drawStringWithShadow(var30, var8 - var7, var9 + var7 / 2 + var28 * 8 + 20, var29.getColor());
            this.fontRendererObj
               .drawStringWithShadow(
                  var30 = var23.format(var29.field_76332_a) + "%",
                  var8 + var7 - 50 - this.fontRendererObj.getStringWidth(var30),
                  var9 + var7 / 2 + var28 * 8 + 20,
                  var29.getColor()
               );
            this.fontRendererObj
               .drawStringWithShadow(
                  var30 = var23.format(var29.field_76330_b) + "%",
                  var8 + var7 - this.fontRendererObj.getStringWidth(var30),
                  var9 + var7 / 2 + var28 * 8 + 20,
                  var29.getColor()
               );
         }
      }
   }

   public Session getSession() {
      return this.session;
   }

   public ItemStack pickBlockWithNBT(Item var1, int var2, TileEntity var3) {
      ItemStack var4 = new ItemStack(var1, 1, var2);
      NBTTagCompound var5 = new NBTTagCompound();
      var3.writeToNBT(var5);
      if (var1 == Items.skull && var5.hasKey("Owner")) {
         NBTTagCompound var8 = var5.getCompoundTag("Owner");
         NBTTagCompound var9 = new NBTTagCompound();
         var9.setTag("SkullOwner", var8);
         var4.setTagCompound(var9);
         return var4;
      } else {
         var4.setTagInfo("BlockEntityTag", var5);
         NBTTagCompound var6 = new NBTTagCompound();
         NBTTagList var7 = new NBTTagList();
         var7.appendTag(new NBTTagString("(+NBT)"));
         var6.setTag("Lore", var7);
         var4.setTagInfo("display", var6);
         return var4;
      }
   }

   public boolean isIntegratedServerRunning() {
      return this.integratedServerIsRunning;
   }

   public RenderManager getRenderManager() {
      return this.renderManager;
   }

   public DefaultResourcePack method_20413() {
      return this.mcDefaultResourcePack;
   }

   public Framebuffer getFramebuffer() {
      return this.framebufferMc;
   }

   @Override
   public boolean isCallingFromMinecraftThread() {
      return Thread.currentThread() == this.mcThread;
   }

   public boolean isConnectedToRealms() {
      return this.connectedToRealms;
   }

   public void method_20375(Session var1) {
      this.session = var1;
   }

   public PropertyMap getTwitchDetails() {
      return this.twitchDetails;
   }

   public IResourceManager getResourceManager() {
      return this.mcResourceManager;
   }

   public void method_20396() throws java.io.IOException {
      long var1 = System.nanoTime();
      this.mcProfiler.startSection("root");
      if (Display.isCreated() && Display.isCloseRequested()) {
         this.shutdown();
      }

      if (this.recoveredField3824 && this.theWorld != null) {
         float var3 = this.recoveredField3821.recoveredField2493;
         this.recoveredField3821.method_03202();
         this.recoveredField3821.recoveredField2493 = var3;
      } else {
         this.recoveredField3821.method_03202();
      }

      this.mcProfiler.startSection("scheduledExecutables");
      synchronized (this.recoveredField3829) {
         while (!this.recoveredField3829.isEmpty()) {
            Util.runTask(this.recoveredField3829.poll(), logger);
         }
      }

      this.mcProfiler.endSection();
      long var11 = System.nanoTime();
      this.mcProfiler.startSection("tick");

      for (int var5 = 0; var5 < this.recoveredField3821.recoveredField2495; var5++) {
         this.method_20414();
      }

      this.mcProfiler.endStartSection("preRenderErrors");
      long var12 = System.nanoTime() - var11;
      this.checkGLError("Pre render");
      this.mcProfiler.endStartSection("sound");
      this.mcSoundHandler.setListener(this.thePlayer, this.recoveredField3821.recoveredField2493);
      this.mcProfiler.endSection();
      this.mcProfiler.startSection("render");
      GlStateManager.pushMatrix();
      GlStateManager.clear(16640);
      this.framebufferMc.bindFramebuffer(true);
      this.mcProfiler.startSection("display");
      GlStateManager.enableTexture2D();
      if (this.thePlayer != null && this.thePlayer.isEntityInsideOpaqueBlock()) {
         this.gameSettings.thirdPersonView = 0;
      }

      this.mcProfiler.endSection();
      if (!this.recoveredField3836) {
         this.mcProfiler.endStartSection("gameRenderer");
         this.entityRenderer.updateCameraAndRender(this.recoveredField3821.recoveredField2493, var1);
         this.mcProfiler.endSection();
      }

      this.mcProfiler.endSection();
      if (!Display.isActive() && this.fullscreen && CheatBreaker.getInstance().getGlobalSettings().recoveredField592.method_08908()) {
         this.method_20403();
      }

      if (this.gameSettings.recoveredField2681 && this.gameSettings.recoveredField2689 && !this.gameSettings.hideGUI) {
         if (!this.mcProfiler.profilingEnabled) {
            this.mcProfiler.clearProfiling();
         }

         this.mcProfiler.profilingEnabled = true;
         this.displayDebugInfo(var12);
      } else {
         this.mcProfiler.profilingEnabled = false;
         this.recoveredField3831 = System.nanoTime();
      }

      this.guiAchievement.method_27791();
      this.framebufferMc.unbindFramebuffer();
      GlStateManager.popMatrix();
      GlStateManager.pushMatrix();
      this.framebufferMc.framebufferRender(this.displayWidth, this.displayHeight);
      GlStateManager.popMatrix();
      this.mcProfiler.startSection("root");
      this.updateDisplay();
      Thread.yield();
      this.mcProfiler.startSection("stream");
      this.mcProfiler.startSection("update");
      this.stream.func_152935_j();
      this.mcProfiler.endStartSection("submit");
      this.stream.method_02381();
      this.mcProfiler.endSection();
      this.mcProfiler.endSection();
      this.checkGLError("Post render");
      this.recoveredField3826++;
      this.recoveredField3824 = this.isSingleplayer() && this.currentScreen != null && this.currentScreen.b_() && !this.theIntegratedServer.getPublic();
      long var7 = System.nanoTime();
      this.recoveredField3834.addFrame(var7 - this.recoveredField3827);
      this.recoveredField3827 = var7;

      while (getSystemTime() >= this.recoveredField3823 + 1000L) {
         debugFPS = this.recoveredField3826;
         this.debug = String.format(
            "%d fps (%d chunk update%s) T: %s%s%s%s%s",
            debugFPS,
            RenderChunk.renderChunksUpdated,
            RenderChunk.renderChunksUpdated != 1 ? "s" : "",
            this.gameSettings.limitFramerate == GameSettings.Options.FRAMERATE_LIMIT.getValueMax() ? "inf" : this.gameSettings.limitFramerate,
            this.gameSettings.enableVsync ? " vsync" : "",
            this.gameSettings.fancyGraphics ? "" : " fast",
            this.gameSettings.clouds == 0 ? "" : (this.gameSettings.clouds == 1 ? " fast-clouds" : " fancy-clouds"),
            OpenGlHelper.useVbo() ? " vbo" : ""
         );
         RenderChunk.renderChunksUpdated = 0;
         this.recoveredField3823 += 1000L;
         this.recoveredField3826 = 0;
         this.usageSnooper.addMemoryStatsToSnooper();
         if (!this.usageSnooper.isSnooperRunning()) {
            this.usageSnooper.startSnooper();
         }
      }

      if (this.isFramerateLimitBelowMax()
         || (Boolean)CheatBreaker.getInstance().getGlobalSettings().recoveredField562.getValue()
         || !Display.isActive() && (Boolean)CheatBreaker.getInstance().getGlobalSettings().recoveredField596.getValue()
         || this.theWorld == null && this.currentScreen != null) {
         this.mcProfiler.startSection("fpslimit_wait");
         Display.sync(this.getLimitFramerate());
         this.mcProfiler.endSection();
      }

      this.mcProfiler.endSection();
   }

   public RenderItem getRenderItem() {
      return this.renderItem;
   }

   public Minecraft(GameConfiguration var1) {
      this.connectedToRealms = false;
      this.recoveredField3821 = new Timer(20.0F);
      this.usageSnooper = new PlayerUsageSnooper("client", this, MinecraftServer.getCurrentTimeMillis());
      this.recoveredField3830 = getSystemTime();
      this.recoveredField3834 = new FrameTimer();
      this.recoveredField3827 = System.nanoTime();
      this.mcProfiler = new Profiler();
      this.recoveredField3816 = -1L;
      this.metadataSerializer_ = new IMetadataSerializer();
      this.defaultResourcePacks = Lists.newArrayList();
      this.recoveredField3829 = Queues.newArrayDeque();
      this.recoveredField3832 = 0L;
      this.mcThread = Thread.currentThread();
      this.running = true;
      this.debug = "";
      this.recoveredField3837 = false;
      this.recoveredField3818 = false;
      this.recoveredField3813 = false;
      this.renderChunksMany = true;
      this.recoveredField3823 = getSystemTime();
      this.recoveredField3831 = -1L;
      this.recoveredField3814 = "root";
      theMinecraft = this;
      this.mcDataDir = var1.folderInfo.mcDataDir;
      this.recoveredField3810 = var1.folderInfo.assetsDir;
      this.recoveredField3817 = var1.folderInfo.resourcePacksDir;
      this.launchedVersion = var1.gameInfo.version;
      this.twitchDetails = var1.userInfo.userProperties;
      this.profileProperties = var1.userInfo.profileProperties;
      this.mcDefaultResourcePack = new DefaultResourcePack(
         new ResourceIndex(var1.folderInfo.assetsDir, var1.folderInfo.assetIndex).getResourceMap()
      );
      this.proxy = var1.userInfo.proxy == null ? Proxy.NO_PROXY : var1.userInfo.proxy;
      this.sessionService = new YggdrasilAuthenticationService(var1.userInfo.proxy, UUID.randomUUID().toString())
         .createMinecraftSessionService();
      this.session = var1.userInfo.session;
      logger.info("Setting user: " + this.session.getUsername());
      logger.info("Setting UUID: " + this.session.getPlayerID());
      this.recoveredField3839 = var1.gameInfo.isDemo;
      this.displayWidth = var1.displayInfo.width > 0 ? var1.displayInfo.width : 1;
      this.displayHeight = var1.displayInfo.height > 0 ? var1.displayInfo.height : 1;
      this.recoveredField3838 = var1.displayInfo.width;
      this.recoveredField3835 = var1.displayInfo.height;
      this.fullscreen = var1.displayInfo.fullscreen;
      this.recoveredField3819 = isJvm64bit();
      this.theIntegratedServer = new IntegratedServer(this);
      if (var1.serverInfo.serverName != null) {
         this.recoveredField3811 = var1.serverInfo.serverName;
         this.recoveredField3822 = var1.serverInfo.serverPort;
      }

      ImageIO.setUseCache(false);
      Bootstrap.register();
   }

   public boolean isUnicode() {
      return this.mcLanguageManager.isCurrentLocaleUnicode() || this.gameSettings.forceUnicodeFont;
   }

   public void launchIntegratedServer(String var1, String var2, WorldSettings var3) {
      this.loadWorld(null);
      System.gc();
      ISaveHandler var4 = this.saveLoader.getSaveLoader(var1, false);
      WorldInfo var5 = var4.loadWorldInfo();
      if (var5 == null && var3 != null) {
         var5 = new WorldInfo(var3, var1);
         var4.saveWorldInfo(var5);
      }

      if (var3 == null) {
         var3 = new WorldSettings(var5);
      }

      try {
         this.theIntegratedServer = new IntegratedServer(this, var1, var2, var3);
         this.theIntegratedServer.startServerThread();
         this.integratedServerIsRunning = true;
      } catch (Throwable var10) {
         CrashReport var7 = CrashReport.makeCrashReport(var10, "Starting integrated server");
         CrashReportCategory var8 = var7.makeCategory("Starting integrated server");
         var8.addCrashSection("Level ID", var1);
         var8.addCrashSection("Level Name", var2);
         throw new ReportedException(var7);
      }

      this.loadingScreen.displaySavingString(I18n.format("menu.loadingLevel"));

      while (!this.theIntegratedServer.method_06917()) {
         String var6 = this.theIntegratedServer.method_06876();
         if (var6 != null) {
            this.loadingScreen.displayLoadingString(I18n.format(var6));
         } else {
            this.loadingScreen.displayLoadingString("");
         }

         try {
            Thread.sleep(200L);
         } catch (InterruptedException var9) {
         }
      }

      this.displayGuiScreen(null);
      SocketAddress var11 = this.theIntegratedServer.getNetworkSystem().addLocalEndpoint();
      NetworkManager var12 = NetworkManager.provideLocalClient(var11);
      var12.setNetHandler(new NetHandlerLoginClient(var12, this, null));
      var12.sendPacket(new C00Handshake(47, var11.toString(), 0, EnumConnectionState.LOGIN));
      var12.sendPacket(new C00PacketLoginStart(this.getSession().getProfile()));
      this.myNetworkManager = var12;
   }

   public ListenableFuture<Object> scheduleResourcesRefresh() {
      return this.addScheduledTask(new Runnable() {
         @Override
         public void run() {
            Minecraft.this.refreshResources();
         }
      });
   }

   public void sendClickBlockToController(boolean var1) {
      this.recoveredField3809 = 0;
      if (!var1) {
         this.recoveredField3809 = 0;
      }

      if (this.recoveredField3809 <= 0 && !this.thePlayer.isUsingItem()) {
         if (var1 && this.objectMouseOver != null && this.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            BlockPos var2 = this.objectMouseOver.getBlockPos();
            if (this.theWorld.getBlockState(var2).getBlock().getMaterial() != Material.air
               && this.playerController.onPlayerDamageBlock(var2, this.objectMouseOver.sideHit)) {
               this.effectRenderer.addBlockHitEffects(var2, this.objectMouseOver.sideHit);
               this.thePlayer.swingItem();
            }
         } else {
            this.playerController.resetBlockRemoving();
         }
      }
   }

   public CrashReport addGraphicsAndWorldToCrashReport(CrashReport var1) {
      var1.getCategory().addCrashSectionCallable("Launched Version", new Callable<String>() {
         public String call() {
            return Minecraft.this.launchedVersion;
         }
      });
      var1.getCategory().addCrashSectionCallable("LWJGL", new Callable<String>() {
         public String call() {
            return Sys.getVersion();
         }
      });
      var1.getCategory().addCrashSectionCallable("OpenGL", new Callable<String>() {
         public String call() {
            return GL11.glGetString(7937) + " GL version " + GL11.glGetString(7938) + ", " + GL11.glGetString(7936);
         }
      });
      var1.getCategory().addCrashSectionCallable("GL Caps", new Callable<String>() {
         public String call() {
            return OpenGlHelper.getLogText();
         }
      });
      var1.getCategory().addCrashSectionCallable("Using VBOs", new Callable<String>() {
         public String call() {
            return Minecraft.this.gameSettings.useVbo ? "Yes" : "No";
         }
      });
      var1.getCategory()
         .addCrashSectionCallable(
            "Is Modded",
            new Callable<String>() {
               public String call() {
                  String var1 = ClientBrandRetriever.getClientModName();
                  return !var1.equals("vanilla")
                     ? "Definitely; Client brand changed to '" + var1 + "'"
                     : (
                        Minecraft.class.getSigners() == null
                           ? "Very likely; Jar signature invalidated"
                           : "Probably not. Jar signature remains and client brand is untouched."
                     );
               }
            }
         );
      var1.getCategory().addCrashSectionCallable("Type", new Callable<String>() {
         public String call() {
            return "Client (map_client.txt)";
         }
      });
      var1.getCategory().addCrashSectionCallable("Resource Packs", new Callable<String>() {
         public String call() {
            StringBuilder var1 = new StringBuilder();

            for (String var3 : Minecraft.this.gameSettings.resourcePacks) {
               if (var1.length() > 0) {
                  var1.append(", ");
               }

               var1.append(var3);
               if (Minecraft.this.gameSettings.incompatibleResourcePacks.contains(var3)) {
                  var1.append(" (incompatible)");
               }
            }

            return var1.toString();
         }
      });
      var1.getCategory().addCrashSectionCallable("Current Language", new Callable<String>() {
         public String call() {
            return Minecraft.this.mcLanguageManager.getCurrentLanguage().toString();
         }
      });
      var1.getCategory().addCrashSectionCallable("Profiler Position", new Callable<String>() {
         public String call() {
            return Minecraft.this.mcProfiler.profilingEnabled ? Minecraft.this.mcProfiler.getNameOfLastSection() : "N/A (disabled)";
         }
      });
      var1.getCategory().addCrashSectionCallable("CPU", new Callable<String>() {
         public String call() {
            return OpenGlHelper.getCpu();
         }
      });
      if (this.theWorld != null) {
         this.theWorld.addWorldInfoToCrashReport(var1);
      }

      return var1;
   }

   public void dispatchKeypresses() {
      int var1 = Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() : Keyboard.getEventKey();
      if (var1 != 0
         && !Keyboard.isRepeatEvent()
         && (!(this.currentScreen instanceof GuiControls) || ((GuiControls)this.currentScreen).time <= getSystemTime() - 20L)) {
         if (Keyboard.getEventKeyState()) {
            if (var1 == this.gameSettings.recoveredField2672.getKeyCode()) {
               if (this.getTwitchStream().isBroadcasting()) {
                  this.getTwitchStream().stopBroadcasting();
               } else if (this.getTwitchStream().method_02394()) {
                  this.displayGuiScreen(new GuiYesNo(new StreamingConfirmCallback(this), I18n.format("stream.confirm_start"), "", 0));
               } else if (!this.getTwitchStream().func_152928_D() || !this.getTwitchStream().method_02383()) {
                  GuiStreamUnavailable.func_152321_a(this.currentScreen);
               } else if (this.theWorld != null) {
                  this.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("Not ready to start streaming yet!"));
               }
            } else if (var1 == this.gameSettings.recoveredField2697.getKeyCode()) {
               if (this.getTwitchStream().isBroadcasting()) {
                  if (this.getTwitchStream().isPaused()) {
                     this.getTwitchStream().unpause();
                  } else {
                     this.getTwitchStream().pause();
                  }
               }
            } else if (var1 == this.gameSettings.recoveredField2698.getKeyCode()) {
               if (this.getTwitchStream().isBroadcasting()) {
                  this.getTwitchStream().requestCommercial();
               }
            } else if (var1 == this.gameSettings.recoveredField2676.getKeyCode()) {
               this.stream.muteMicrophone(true);
            } else if (var1 == this.gameSettings.recoveredField2701.getKeyCode()) {
               this.method_20403();
            } else if (var1 == this.gameSettings.recoveredField2682.getKeyCode()) {
               ScreenShotHelper.method_12617(this.mcDataDir, this.displayWidth, this.displayHeight, this.framebufferMc);
            }
         } else if (var1 == this.gameSettings.recoveredField2676.getKeyCode()) {
            this.stream.muteMicrophone(false);
         }
      }
   }

   public ResourcePackRepository getResourcePackRepository() {
      return this.mcResourcePackRepository;
   }

   public IntegratedServer getIntegratedServer() {
      return this.theIntegratedServer;
   }

   public String getVersion() {
      return this.launchedVersion;
   }

   public void shutdown() {
      this.running = false;
   }

   public void method_20415() {
      if (this.objectMouseOver != null) {
         boolean var1 = this.thePlayer.bA.isCreativeMode;
         int var2 = 0;
         boolean var3 = false;
         TileEntity var4 = null;
         Object var5;
         if (this.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            BlockPos var6 = this.objectMouseOver.getBlockPos();
            Block var7 = this.theWorld.getBlockState(var6).getBlock();
            if (var7.getMaterial() == Material.air) {
               return;
            }

            var5 = var7.getItem(this.theWorld, var6);
            if (var5 == null) {
               return;
            }

            if (var1 && GuiScreen.isCtrlKeyDown()) {
               var4 = this.theWorld.getTileEntity(var6);
            }

            Block var8 = var5 instanceof ItemBlock && !var7.isFlowerPot() ? Block.getBlockFromItem((Item)var5) : var7;
            var2 = var8.getDamageValue(this.theWorld, var6);
            var3 = ((Item)var5).getHasSubtypes();
         } else {
            if (this.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY || this.objectMouseOver.entityHit == null || !var1) {
               return;
            }

            if (this.objectMouseOver.entityHit instanceof EntityPainting) {
               var5 = Items.painting;
            } else if (this.objectMouseOver.entityHit instanceof EntityLeashKnot) {
               var5 = Items.lead;
            } else if (this.objectMouseOver.entityHit instanceof EntityItemFrame) {
               EntityItemFrame var9 = (EntityItemFrame)this.objectMouseOver.entityHit;
               ItemStack var12 = var9.getDisplayedItem();
               if (var12 == null) {
                  var5 = Items.item_frame;
               } else {
                  var5 = var12.getItem();
                  var2 = var12.getMetadata();
                  var3 = true;
               }
            } else if (this.objectMouseOver.entityHit instanceof EntityMinecart) {
               EntityMinecart var10 = (EntityMinecart)this.objectMouseOver.entityHit;
               switch (var10.getMinecartType()) {
                  case FURNACE:
                     var5 = Items.furnace_minecart;
                     break;
                  case CHEST:
                     var5 = Items.chest_minecart;
                     break;
                  case TNT:
                     var5 = Items.tnt_minecart;
                     break;
                  case HOPPER:
                     var5 = Items.hopper_minecart;
                     break;
                  case COMMAND_BLOCK:
                     var5 = Items.command_block_minecart;
                     break;
                  default:
                     var5 = Items.minecart;
               }
            } else if (this.objectMouseOver.entityHit instanceof EntityBoat) {
               var5 = Items.boat;
            } else if (this.objectMouseOver.entityHit instanceof EntityArmorStand) {
               var5 = Items.armor_stand;
            } else {
               var5 = Items.spawn_egg;
               var2 = EntityList.getEntityID(this.objectMouseOver.entityHit);
               var3 = true;
               if (!EntityList.entityEggs.containsKey(var2)) {
                  return;
               }
            }
         }

         InventoryPlayer var11 = this.thePlayer.bi;
         if (var4 == null) {
            var11.setCurrentItem((Item)var5, var2, var3, var1);
         } else {
            ItemStack var13 = this.pickBlockWithNBT((Item)var5, var2, var4);
            var11.setInventorySlotContents(var11.currentItem, var13);
         }

         if (var1) {
            int var14 = this.thePlayer.bj.c.size() - 9 + var11.currentItem;
            this.playerController.sendSlotPacket(var11.getStackInSlot(var11.currentItem), var14);
         }
      }
   }

   public void resize(int var1, int var2) {
      this.displayWidth = Math.max(1, var1);
      this.displayHeight = Math.max(1, var2);
      if (this.currentScreen != null) {
         ScaledResolution var3 = new ScaledResolution(this);
         this.currentScreen.onResize(this, var3.getScaledWidth(), var3.getScaledHeight());
      }

      this.loadingScreen = new LoadingScreenRenderer(this);
      this.updateFramebufferSize();
   }

   public BlockRendererDispatcher getBlockRendererDispatcher() {
      return this.blockRenderDispatcher;
   }

   public void displayGuiScreen(GuiScreen var1) {
      CheatBreaker.getInstance().recoveredField1577 = this.currentScreen;
      if (this.currentScreen != null) {
         if (this.entityRenderer.isShaderActive()) {
            try {
               this.entityRenderer.stopUseShader();
            } catch (Exception var5) {
               var5.printStackTrace();
            }
         }

         this.currentScreen.a_();
         WindowsImeSupport.beginScreen(this.currentScreen);
      }

      if (var1 == null && this.theWorld == null) {
         var1 = new LegacyMainMenu();
      } else if (var1 == null && this.thePlayer.getHealth() <= 0.0F) {
         var1 = new GuiGameOver();
      }

      if (var1 instanceof LegacyMainMenu) {
         this.gameSettings.recoveredField2681 = false;
         this.ingameGUI.getChatGUI().clearChatMessages();
      }

      if (var1 instanceof LegacyMainMenu) {
         var1 = MainMenuMode.method_22258(CheatBreaker.getInstance().getGlobalSettings().method_02705());
      }

      this.currentScreen = (GuiScreen)var1;
      if (var1 != null) {
         this.method_20394();
         ScaledResolution var2 = new ScaledResolution(this);
         int var3 = var2.getScaledWidth();
         int var4 = var2.getScaledHeight();
         ((GuiScreen)var1).setWorldAndResolution(this, var3, var4);
         this.recoveredField3836 = false;
      } else {
         this.mcSoundHandler.resumeSounds();
         this.method_20340();
      }
      WindowsImeSupport.updateGameInput(this.currentScreen);
   }

   public void draw(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10) {
      float var11 = 0.00390625F;
      float var12 = 0.00390625F;
      WorldRenderer var13 = Tessellator.getInstance().getWorldRenderer();
      var13.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
      var13.pos(var1, var2 + var6, 0.0).tex(var3 * var11, (var4 + var6) * var12).color(var7, var8, var9, var10).endVertex();
      var13.pos(var1 + var5, var2 + var6, 0.0).tex((var3 + var5) * var11, (var4 + var6) * var12).color(var7, var8, var9, var10).endVertex();
      var13.pos(var1 + var5, var2, 0.0).tex((var3 + var5) * var11, var4 * var12).color(var7, var8, var9, var10).endVertex();
      var13.pos(var1, var2, 0.0).tex(var3 * var11, var4 * var12).color(var7, var8, var9, var10).endVertex();
      Tessellator.getInstance().draw();
   }

   public boolean isFramerateLimitBelowMax() {
      return this.getLimitFramerate() < GameSettings.Options.FRAMERATE_LIMIT.getValueMax();
   }

   public TextureMap getTextureMapBlocks() {
      return this.textureMapBlocks;
   }

   public static void stopIntegratedServer() {
      if (theMinecraft != null) {
         IntegratedServer var0 = theMinecraft.getIntegratedServer();
         if (var0 != null) {
            var0.stopServer();
         }
      }
   }

   public void createDisplay() throws org.lwjgl.LWJGLException {
      Display.setResizable(true);
      Display.setTitle("Minecraft 1.8.9");

      try {
         Display.create(new PixelFormat().withDepthBits(24));
      } catch (LWJGLException var4) {
         logger.error("Couldn't set pixel format", var4);

         try {
            Thread.sleep(1000L);
         } catch (InterruptedException var3) {
         }

         if (this.fullscreen) {
            this.method_20416();
         }

         Display.create();
      }
   }

   public static boolean isAmbientOcclusionEnabled() {
      return theMinecraft != null && theMinecraft.gameSettings.ambientOcclusion != 0;
   }

   public void method_20410() {
      this.running = true;

      try {
         this.method_20334();
      } catch (Throwable var11) {
         CrashReport var2 = CrashReport.makeCrashReport(var11, "Initializing game");
         var2.makeCategory("Initialization");
         this.displayCrashReport(this.addGraphicsAndWorldToCrashReport(var2));
         return;
      }

      try {
         while (this.running) {
            if (this.hasCrashed && this.crashReporter != null) {
               this.displayCrashReport(this.crashReporter);
            } else {
               try {
                  this.method_20396();
               } catch (OutOfMemoryError var10) {
                  this.method_20434();
                  this.displayGuiScreen(new GuiMemoryErrorScreen());
                  System.gc();
               }
            }
         }

         return;
      } catch (MinecraftError var12) {
      } catch (ReportedException var13) {
         this.addGraphicsAndWorldToCrashReport(var13.getCrashReport());
         this.method_20434();
         logger.fatal("Reported exception thrown!", var13);
         this.displayCrashReport(var13.getCrashReport());
      } catch (Throwable var14) {
         CrashReport var16 = this.addGraphicsAndWorldToCrashReport(new CrashReport("Unexpected error", var14));
         this.method_20434();
         logger.fatal("Unreported exception thrown!", var14);
         this.displayCrashReport(var16);
      } finally {
         this.method_20346();
      }
   }

   public ServerData getCurrentServerData() {
      return this.currentServerData;
   }

   public NetHandlerPlayClient getNetHandler() {
      return this.thePlayer != null ? this.thePlayer.sendQueue : null;
   }

   public SkinManager getSkinManager() {
      return this.skinManager;
   }

   public static boolean isGuiEnabled() {
      return theMinecraft == null || !theMinecraft.gameSettings.hideGUI;
   }

   public Proxy getProxy() {
      return this.proxy;
   }

   public void setInitialDisplayMode() throws org.lwjgl.LWJGLException {
      if (this.fullscreen) {
         Display.setFullscreen(true);
         DisplayMode var1 = Display.getDisplayMode();
         this.displayWidth = Math.max(1, var1.getWidth());
         this.displayHeight = Math.max(1, var1.getHeight());
      } else {
         Display.setDisplayMode(new DisplayMode(this.displayWidth, this.displayHeight));
      }
   }

   public void method_20394() {
      if (this.recoveredField3812) {
         KeyBinding.unPressAllKeys();
         this.recoveredField3812 = false;
         this.mouseHelper.ungrabMouseCursor();
      }
   }

   public ItemRenderer getItemRenderer() {
      return this.itemRenderer;
   }
}
