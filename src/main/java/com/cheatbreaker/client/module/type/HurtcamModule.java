package com.cheatbreaker.client.module.type;

import com.cheatbreaker.client.config.Setting;
import com.cheatbreaker.client.config.SettingsDetailLevel;
import com.cheatbreaker.client.module.AbstractModule;
import net.minecraft.util.ResourceLocation;

public class HurtcamModule extends AbstractModule {
   public final Setting damageAffectsCamera;
   public final Setting hurtCameraIntensity;

   public HurtcamModule() {
      super("Hurtcam");
      this.setDefaultState(false);
      this.method_28821("Adjust camera shake when taking damage.");
      this.method_28823("Damage affects camera is enabled.", "hypixel");
      new Setting(this, "label").setValue("Hurt Camera Options");
      this.damageAffectsCamera = new Setting(this, "Damage affects camera").setValue(true).method_08914(SettingsDetailLevel.SIMPLE);
      this.hurtCameraIntensity = new Setting(this, "Hurt Camera Intensity")
         .method_08892("%")
         .setValue(14.0F)
         .setMinMax(5.0F, 35.0F)
         .method_08894(() -> this.damageAffectsCamera.method_08908());
      this.setPreviewIcon(new ResourceLocation("client/icons/mods/hurtcam.png"), 32, 32);
   }
}
