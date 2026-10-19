package com.cheatbreaker.client.module.type;

import com.cheatbreaker.client.config.Setting;
import com.cheatbreaker.client.config.SettingsDetailLevel;
import com.cheatbreaker.client.module.AbstractModule;
import net.minecraft.util.ResourceLocation;

/** Module entry reserved for the upcoming barrier-to-glass rendering feature. */
public class BarrierBlockModule extends AbstractModule {
   public BarrierBlockModule() {
      super("Barrier Block");
      this.setDefaultState(false);
      this.method_28821("Placeholder for showing barrier blocks with a glass texture. Rendering is not implemented yet.");
      new Setting(this, "label").setValue("Glass Rendering (Coming Soon)").method_08914(SettingsDetailLevel.SIMPLE);
      this.setPreviewIcon(new ResourceLocation("textures/items/barrier.png"), 32, 32);
   }
}
