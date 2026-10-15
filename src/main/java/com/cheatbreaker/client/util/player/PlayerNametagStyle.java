package com.cheatbreaker.client.util.player;

import net.minecraft.util.ResourceLocation;

public class PlayerNametagStyle {
   public static final ResourceLocation LOGO_TEXTURE = new ResourceLocation("client/logo_white.png");
   public String recoveredField3341;
   public int[] recoveredField3342;
   public String recoveredField3343;

   public String method_26420() {
      return this.recoveredField3341;
   }

   public PlayerNametagStyle(String var1, String var2, int[] var3) {
      this.recoveredField3341 = var1;
      this.recoveredField3343 = var2;
      this.recoveredField3342 = var3;
   }

   public String method_26422() {
      return this.recoveredField3343;
   }

   public void method_26421(String var1) {
      this.recoveredField3341 = var1;
   }

   public int[] method_26419() {
      return this.recoveredField3342;
   }
}
