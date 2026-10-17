package com.cheatbreaker.client.util.render;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.config.GlobalSettings;
import net.minecraft.client.renderer.texture.ITickable;

/** Limits texture animation updates within the game's existing 20 Hz tick loop. */
public final class TextureAnimationTicker {
   private int pendingTicks;
   private int previousRate = 20;

   public void tick(ITickable textures) {
      int rate = 20;
      CheatBreaker client = CheatBreaker.getInstance();
      GlobalSettings settings = client == null ? null : client.getGlobalSettings();
      if (settings != null && settings.recoveredField563 != null
         && settings.recoveredField563.method_08908() && settings.lowerAnimationTicks != null) {
         rate = Math.max(0, Math.min(20, settings.lowerAnimationTicks.method_08912()));
      }

      // Switching rates or resuming must not replay time accumulated while frozen.
      if (rate != this.previousRate) {
         this.pendingTicks = 0;
         this.previousRate = rate;
      }
      this.pendingTicks += rate;
      if (this.pendingTicks >= 20) {
         this.pendingTicks -= 20;
         textures.tick();
      }
   }
}
