package net.minecraft.client.multiplayer;

import net.minecraft.block.BlockBarrier;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.event.type.DisconnectEvent;
import com.google.common.collect.Sets;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MovingSoundMinecart;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.particle.EntityFirework_StarterFX;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.profiler.Profiler;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.src.Config;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.storage.SaveDataMemoryStorage;
import net.minecraft.world.storage.SaveHandlerMP;
import net.minecraft.world.storage.WorldInfo;
import net.optifine.CustomGuis;
import net.optifine.DynamicLights;
import net.optifine.override.PlayerControllerOF;
import net.optifine.reflect.Reflector;

public class WorldClient extends World {
   public Set<ChunkCoordIntPair> previousActiveChunkSet;
   public ChunkProviderClient clientChunkProvider;
   public boolean playerUpdate;
   public Set<Entity> entityList = Sets.newHashSet();
   public Minecraft mc;
   public NetHandlerPlayClient sendQueue;
   public Set<Entity> entitySpawnQueue = Sets.newHashSet();

   @Override
   public void onEntityAdded(Entity var1) {
      super.onEntityAdded(var1);
      if (this.entitySpawnQueue.contains(var1)) {
         this.entitySpawnQueue.remove(var1);
      }
   }

   public void setWorldScoreboard(Scoreboard var1) {
      this.C = var1;
   }

   @Override
   public void removeEntity(Entity var1) {
      super.removeEntity(var1);
      this.entityList.remove(var1);
   }

   public boolean invalidateRegionAndSetBlock(BlockPos var1, IBlockState var2) {
      int var3 = var1.getX();
      int var4 = var1.getY();
      int var5 = var1.getZ();
      this.invalidateBlockReceiveRegion(var3, var4, var5, var3, var4, var5);
      return super.a(var1, var2, 3);
   }

   public Entity removeEntityFromWorld(int var1) {
      Entity var2 = this.l.removeObject(var1);
      if (var2 != null) {
         this.entityList.remove(var2);
         this.removeEntity(var2);
      }

      return var2;
   }

   @Override
   public void playSound(double var1, double var3, double var5, String var7, float var8, float var9, boolean var10) {
      double var11 = this.mc.getRenderViewEntity().e(var1, var3, var5);
      PositionedSoundRecord var13 = new PositionedSoundRecord(new ResourceLocation(var7), var8, var9, (float)var1, (float)var3, (float)var5);
      if (var10 && var11 > 100.0) {
         double var14 = Math.sqrt(var11) / 40.0;
         this.mc.getSoundHandler().playDelayedSound(var13, (int)(var14 * 20.0));
      } else {
         this.mc.getSoundHandler().playSound(var13);
      }
   }

   public boolean isPlayerActing() {
      if (this.mc.playerController instanceof PlayerControllerOF) {
         PlayerControllerOF var1 = (PlayerControllerOF)this.mc.playerController;
         return var1.isActing();
      } else {
         return false;
      }
   }

   @Override
   public void setWorldTime(long var1) {
      if (var1 < 0L) {
         var1 = -var1;
         this.Q().setOrCreateGameRule("doDaylightCycle", "false");
      } else {
         this.Q().setOrCreateGameRule("doDaylightCycle", "true");
      }

      super.setWorldTime(var1);
   }

   @Override
   public void updateWeather() {
   }

   @Override
   public boolean spawnEntityInWorld(Entity var1) {
      boolean var2 = super.spawnEntityInWorld(var1);
      this.entityList.add(var1);
      if (!var2) {
         this.entitySpawnQueue.add(var1);
      } else if (var1 instanceof EntityMinecart) {
         this.mc.getSoundHandler().playSound(new MovingSoundMinecart((EntityMinecart)var1));
      }

      return var2;
   }

   @Override
   public void onEntityRemoved(Entity var1) {
      super.onEntityRemoved(var1);
      boolean var2 = false;
      if (this.entityList.contains(var1)) {
         if (var1.isEntityAlive()) {
            this.entitySpawnQueue.add(var1);
            var2 = true;
         } else {
            this.entityList.remove(var1);
         }
      }
   }

   public void doPreChunk(int var1, int var2, boolean var3) {
      if (var3) {
         this.clientChunkProvider.loadChunk(var1, var2);
      } else {
         this.clientChunkProvider.unloadChunk(var1, var2);
      }

      if (!var3) {
         this.markBlockRangeForRenderUpdate(var1 * 16, 0, var2 * 16, var1 * 16 + 15, 256, var2 * 16 + 15);
      }
   }

   @Override
   public void tick() {
      super.tick();
      this.method_09997(this.K() + 1L);
      if (this.Q().getBoolean("doDaylightCycle")) {
         this.setWorldTime(this.L() + 1L);
      }

      this.B.startSection("reEntryProcessing");

      for (int var1 = 0; var1 < 10 && !this.entitySpawnQueue.isEmpty(); var1++) {
         Entity var2 = this.entitySpawnQueue.iterator().next();
         this.entitySpawnQueue.remove(var2);
         if (!this.f.contains(var2)) {
            this.spawnEntityInWorld(var2);
         }
      }

      this.B.endStartSection("chunkCache");
      this.clientChunkProvider.unloadQueuedChunks();
      this.B.endStartSection("blocks");
      this.updateBlocks();
      this.B.endSection();
   }

   public boolean isPlayerUpdate() {
      return this.playerUpdate;
   }

   public void removeAllEntities() {
      this.f.removeAll(this.g);

      for (int var1 = 0; var1 < this.g.size(); var1++) {
         Entity var2 = this.g.get(var1);
         int var3 = var2.chunkCoordX;
         int var4 = var2.chunkCoordZ;
         if (var2.addedToChunk && this.a(var3, var4, true)) {
            this.a(var3, var4).removeEntity(var2);
         }
      }

      for (int var5 = 0; var5 < this.g.size(); var5++) {
         this.onEntityRemoved(this.g.get(var5));
      }

      this.g.clear();

      for (int var6 = 0; var6 < this.f.size(); var6++) {
         Entity var7 = this.f.get(var6);
         if (var7.m != null) {
            if (!var7.m.I && var7.m.l == var7) {
               continue;
            }

            var7.m.l = null;
            var7.m = null;
         }

         if (var7.I) {
            int var8 = var7.chunkCoordX;
            int var9 = var7.chunkCoordZ;
            if (var7.addedToChunk && this.a(var8, var9, true)) {
               this.a(var8, var9).removeEntity(var7);
            }

            this.f.remove(var6--);
            this.onEntityRemoved(var7);
         }
      }
   }

   @Override
   public int getCombinedLight(BlockPos var1, int var2) {
      int var3 = super.getCombinedLight(var1, var2);
      if (Config.isDynamicLights()) {
         var3 = DynamicLights.getCombinedLight(var1, var3);
      }

      return var3;
   }

   @Override
   public void makeFireworks(double var1, double var3, double var5, double var7, double var9, double var11, NBTTagCompound var13) {
      this.mc.effectRenderer.addEffect(new EntityFirework_StarterFX(this, var1, var3, var5, var7, var9, var11, this.mc.effectRenderer, var13));
   }

   @Override
   public int getRenderDistanceChunks() {
      return this.mc.gameSettings.renderDistanceChunks;
   }

   @Override
   public Entity getEntityByID(int var1) {
      return (Entity)(var1 == this.mc.thePlayer.F() ? this.mc.thePlayer : super.getEntityByID(var1));
   }

   @Override
   public CrashReportCategory addWorldInfoToCrashReport(CrashReport var1) {
      CrashReportCategory var2 = super.addWorldInfoToCrashReport(var1);
      var2.addCrashSectionCallable("Forced entities", new Callable<String>() {
         public String call() {
            return WorldClient.this.entityList.size() + " total; " + WorldClient.this.entityList.toString();
         }
      });
      var2.addCrashSectionCallable("Retry entities", new Callable<String>() {
         public String call() {
            return WorldClient.this.entitySpawnQueue.size() + " total; " + WorldClient.this.entitySpawnQueue.toString();
         }
      });
      var2.addCrashSectionCallable("Server brand", new Callable<String>() {
         public String call() {
            return WorldClient.this.mc.thePlayer.getClientBrand();
         }
      });
      var2.addCrashSectionCallable("Server type", new Callable<String>() {
         public String call() {
            return WorldClient.this.mc.getIntegratedServer() == null ? "Non-integrated multiplayer server" : "Integrated singleplayer server";
         }
      });
      return var2;
   }

   @Override
   public boolean a(BlockPos var1, IBlockState var2, int var3) {
      this.playerUpdate = this.isPlayerActing();
      boolean var4 = super.a(var1, var2, var3);
      this.playerUpdate = false;
      return var4;
   }

   @Override
   public IChunkProvider createChunkProvider() {
      this.clientChunkProvider = new ChunkProviderClient(this);
      return this.clientChunkProvider;
   }

   public void doVoidFogParticles(int var1, int var2, int var3) {
      byte var4 = 16;
      Random var5 = new Random();
      ItemStack var6 = this.mc.thePlayer.getHeldItem();
      boolean var7 = this.mc.playerController.getCurrentGameType() == WorldSettings.GameType.CREATIVE
         && var6 != null
         && Block.getBlockFromItem(var6.getItem()) == Blocks.barrier
         && ((BlockBarrier)Blocks.barrier).getRenderState() == null;
      BlockPos.MutableBlockPos var8 = new BlockPos.MutableBlockPos();

      for (int var9 = 0; var9 < 1000; var9++) {
         int var10 = var1 + this.s.nextInt(var4) - this.s.nextInt(var4);
         int var11 = var2 + this.s.nextInt(var4) - this.s.nextInt(var4);
         int var12 = var3 + this.s.nextInt(var4) - this.s.nextInt(var4);
         var8.set(var10, var11, var12);
         IBlockState var13 = this.getBlockState(var8);
         var13.getBlock().randomDisplayTick(this, var8, var13, var5);
         if (var7 && var13.getBlock() == Blocks.barrier) {
            this.spawnParticle(EnumParticleTypes.BARRIER, var10 + 0.5F, var11 + 0.5F, var12 + 0.5F, 0.0, 0.0, 0.0);
         }
      }
   }

   @Override
   public void method_05035() {
      CheatBreaker.getInstance().method_19817().method_21935(new DisconnectEvent());
      this.sendQueue.getNetworkManager().closeChannel(new ChatComponentText("Quitting"));
   }

   public WorldClient(NetHandlerPlayClient var1, WorldSettings var2, int var3, EnumDifficulty var4, Profiler var5) {
      super(new SaveHandlerMP(), new WorldInfo(var2, "MpServer"), WorldProvider.getProviderForDimension(var3), var5, true);
      this.mc = Minecraft.getMinecraft();
      this.previousActiveChunkSet = Sets.newHashSet();
      this.playerUpdate = false;
      this.sendQueue = var1;
      this.P().setDifficulty(var4);
      this.t.registerWorld(this);
      this.B(new BlockPos(8, 64, 8));
      this.v = this.createChunkProvider();
      this.z = new SaveDataMemoryStorage();
      this.calculateInitialSkylight();
      this.C();
      Reflector.postForgeBusEvent(Reflector.WorldEvent_Load_Constructor, this);
      if (this.mc.playerController != null && this.mc.playerController.getClass() == PlayerControllerMP.class) {
         this.mc.playerController = new PlayerControllerOF(this.mc, var1);
         CustomGuis.setPlayerControllerOF((PlayerControllerOF)this.mc.playerController);
      }
   }

   public void invalidateBlockReceiveRegion(int var1, int var2, int var3, int var4, int var5, int var6) {
   }

   public void addEntityToWorld(int var1, Entity var2) {
      Entity var3 = this.getEntityByID(var1);
      if (var3 != null) {
         this.removeEntity(var3);
      }

      this.entityList.add(var2);
      var2.setEntityId(var1);
      if (!this.spawnEntityInWorld(var2)) {
         this.entitySpawnQueue.add(var2);
      }

      this.l.addKey(var1, var2);
   }

   @Override
   public void updateBlocks() {
      super.updateBlocks();
      this.previousActiveChunkSet.retainAll(this.E);
      if (this.previousActiveChunkSet.size() == this.E.size()) {
         this.previousActiveChunkSet.clear();
      }

      int var1 = 0;

      for (ChunkCoordIntPair var3 : this.E) {
         if (!this.previousActiveChunkSet.contains(var3)) {
            int var4 = var3.chunkXPos * 16;
            int var5 = var3.chunkZPos * 16;
            this.B.startSection("getChunk");
            Chunk var6 = this.a(var3.chunkXPos, var3.chunkZPos);
            this.a(var4, var5, var6);
            this.B.endSection();
            this.previousActiveChunkSet.add(var3);
            if (++var1 >= 10) {
               return;
            }
         }
      }
   }

   public void playSoundAtPos(BlockPos var1, String var2, float var3, float var4, boolean var5) {
      this.playSound(var1.getX() + 0.5, var1.getY() + 0.5, var1.getZ() + 0.5, var2, var3, var4, var5);
   }
}
