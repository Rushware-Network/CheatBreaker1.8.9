package com.cheatbreaker.client.util;

public final class TileEntityRenderDistance {
   private TileEntityRenderDistance() {
   }

   public static double squaredMultiplier(boolean enabled, double percentage) {
      if (!enabled || Double.isNaN(percentage)) {
         return 1.0;
      }
      double scale = Math.max(5.0, Math.min(100.0, percentage)) / 100.0;
      // The setting scales distance, whereas Minecraft compares squared distances.
      return scale * scale;
   }
}
