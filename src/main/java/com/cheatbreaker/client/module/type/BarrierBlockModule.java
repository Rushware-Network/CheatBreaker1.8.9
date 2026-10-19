package com.cheatbreaker.client.module.type;

import com.cheatbreaker.client.config.Setting;
import com.cheatbreaker.client.config.SettingsDetailLevel;
import com.cheatbreaker.client.module.AbstractModule;
import java.util.Locale;
import net.minecraft.block.BlockBarrier;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;

public class BarrierBlockModule extends AbstractModule {
   public final Setting glassType;

   public BarrierBlockModule() {
      super("Barrier Block");
      this.setDefaultState(false);
      this.method_28821("Show barrier blocks using the selected glass texture from your resource pack. "
         + "Only their appearance changes; collision and world data stay the same. "
         + "Disable this mod to restore invisible barriers.");
      new Setting(this, "label").setValue("Barrier Appearance").method_08914(SettingsDetailLevel.SIMPLE);
      this.glassType = new Setting(this, "Glass Type", "Choose regular glass or a stained glass color for barrier blocks. "
         + "Applies while this mod is enabled; disabling it restores invisible barriers.")
         .setValue("Regular")
         .acceptedValues("Regular", "Cyan", "Pink", "White", "Orange", "Magenta", "Light Blue", "Yellow", "Lime",
            "Gray", "Light Gray", "Purple", "Blue", "Brown", "Green", "Red", "Black")
         .method_08914(SettingsDetailLevel.SIMPLE)
         .onChange(value -> {
            if (this.isEnabled()) {
               this.refreshRenderState();
            }
         });
      this.setPreviewIcon(new ResourceLocation("textures/items/barrier.png"), 32, 32);
   }

   public IBlockState getSelectedGlassState() {
      String type = this.glassType.method_08874();
      if ("Regular".equals(type)) {
         return Blocks.glass.getDefaultState();
      }

      EnumDyeColor color = "Light Gray".equals(type) ? EnumDyeColor.SILVER
         : EnumDyeColor.valueOf(type.replace(' ', '_').toUpperCase(Locale.ROOT));
      return Blocks.stained_glass.getDefaultState().withProperty(BlockStainedGlass.COLOR, color);
   }

   @Override
   public void setState(boolean enabled) {
      boolean changed = this.isEnabled() != enabled;
      super.setState(enabled);
      if (changed) {
         this.refreshRenderState();
      }
   }

   private void refreshRenderState() {
      BlockBarrier barrier = (BlockBarrier)Blocks.barrier;
      IBlockState state = this.isEnabled() ? this.getSelectedGlassState() : null;
      if (barrier.getRenderState() == state) {
         return;
      }

      // Stop asynchronous mesh compilation before changing its shared model cache.
      boolean inWorld = this.minecraft != null && this.minecraft.theWorld != null && this.minecraft.renderGlobal != null;
      if (inWorld) {
         this.minecraft.renderGlobal.stopChunkUpdates();
      }

      barrier.setRenderState(state);
      if (this.minecraft != null && this.minecraft.getBlockRendererDispatcher() != null) {
         this.minecraft.getBlockRendererDispatcher().getBlockModelShapes().updateBarrierModel();
      }

      if (inWorld) {
         this.minecraft.renderGlobal.loadRenderers();
      }
   }
}
