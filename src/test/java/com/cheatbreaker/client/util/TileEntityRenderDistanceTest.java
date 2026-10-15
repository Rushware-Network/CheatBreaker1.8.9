package com.cheatbreaker.client.util;

import junit.framework.TestCase;

public class TileEntityRenderDistanceTest extends TestCase {
   public void testVanillaAndDisabledDistances() {
      assertEquals(1.0, TileEntityRenderDistance.squaredMultiplier(true, 100), 0.0);
      assertEquals(1.0, TileEntityRenderDistance.squaredMultiplier(false, 5), 0.0);
   }

   public void testLinearDistanceScalingIncludingBeaconOverride() {
      double half = TileEntityRenderDistance.squaredMultiplier(true, 50);
      assertEquals(32.0, Math.sqrt(4096.0 * half), 0.000001);
      assertEquals(128.0, Math.sqrt(65536.0 * half), 0.000001);
      assertEquals(3.2, Math.sqrt(4096.0 * TileEntityRenderDistance.squaredMultiplier(true, 5)), 0.000001);
   }

   public void testInvalidConfigurationCannotExpandOrRemoveRenderRange() {
      assertEquals(0.0025, TileEntityRenderDistance.squaredMultiplier(true, -1), 0.000001);
      assertEquals(1.0, TileEntityRenderDistance.squaredMultiplier(true, 200), 0.0);
      assertEquals(1.0, TileEntityRenderDistance.squaredMultiplier(true, Double.NaN), 0.0);
   }
}
