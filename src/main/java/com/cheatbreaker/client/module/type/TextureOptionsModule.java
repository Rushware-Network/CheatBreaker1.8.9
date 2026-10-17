package com.cheatbreaker.client.module.type;

import com.cheatbreaker.client.config.Setting;
import com.cheatbreaker.client.config.SettingsDetailLevel;
import com.cheatbreaker.client.module.AbstractModule;
import net.minecraft.util.ResourceLocation;

public class TextureOptionsModule extends AbstractModule {
   public Setting alwaysDiamondSword;
   public Setting recoveredField1506;
   public Setting recoveredField1507;
   public Setting recoveredField1508;
   public Setting recoveredField1509;
   public Setting recoveredField1510;
   public Setting recoveredField1511;
   public Setting recoveredField1512;
   public Setting recoveredField1513;
   public Setting recoveredField1514;
   public Setting recoveredField1515;
   public Setting recoveredField1516;
   public Setting recoveredField1517;
   public Setting recoveredField1518;
   public Setting recoveredField1519;
   public Setting recoveredField1520;
   public Setting recoveredField1521;
   public Setting recoveredField1522;
   public Setting recoveredField1523;
   public Setting recoveredField1524;

   public TextureOptionsModule() {
      super("Pack Tweaks");
      this.method_28821("Change how specific textures appear.");
      this.setDefaultState(true);
      new Setting(this, "label").setValue("Sword Options").method_08914(SettingsDetailLevel.SIMPLE);
      this.alwaysDiamondSword = new Setting(this, "Always Diamond Sword",
            "Changes sword appearance only, using the current resource pack's diamond sword.\n\nSelf: Your held and inventory swords.\nAll: Every sword.\nOFF: Actual sword appearance.")
         .setValue("OFF")
         .acceptedValues("Self", "All", "OFF")
         .onChange(value -> {
            if ("OFF".equalsIgnoreCase(String.valueOf(value))) this.alwaysDiamondSword.recoveredField3086 = "OFF";
         })
         .method_08914(SettingsDetailLevel.SIMPLE);
      new Setting(this, "label").setValue("Glass Options").method_08914(SettingsDetailLevel.SIMPLE);
      this.recoveredField1523 = new Setting(
            this,
            "Clear Glass",
            "Removes the glass texture.\n\n§bOFF:§r No glass textures are removed.\n§bREGULAR:§r Only normal glass texture is removed.\n§bALL:§r Normal and stained glass textures are removed."
         )
         .setValue("OFF")
         .acceptedValues("OFF", "REGULAR", "ALL")
         .onChange(var1 -> this.minecraft.renderGlobal.loadRenderers())
         .method_08914(SettingsDetailLevel.SIMPLE);
      new Setting(this, "label").setValue("String Options").method_08914(SettingsDetailLevel.SIMPLE).method_08894(() -> "1.8.9".equals("1.7.10"));
      this.recoveredField1513 = new Setting(this, "Colored String", "Show a solid color line instead of the texture pack's string texture.")
         .setValue(false)
         .method_08894(() -> "1.8.9".equals("1.7.10"))
         .onChange(var1 -> this.minecraft.renderGlobal.loadRenderers())
         .method_08914(SettingsDetailLevel.SIMPLE);
      this.recoveredField1509 = new Setting(this, "Colored String Color")
         .setValue(-65536)
         .setMinMax(Integer.MIN_VALUE, Integer.MAX_VALUE)
         .method_08914(SettingsDetailLevel.SIMPLE)
         .method_08894(() -> (Boolean)this.recoveredField1513.getValue() && "1.8.9".equals("1.7.10"));
      new Setting(this, "label").setValue("Fire Options").method_08914(SettingsDetailLevel.SIMPLE);
      this.recoveredField1519 = new Setting(this, "First Person Fire").setValue(true).method_08914(SettingsDetailLevel.SIMPLE);
      this.recoveredField1521 = new Setting(this, "Fire Opacity")
         .setValue(90.0F)
         .setMinMax(0.0F, 100.0F)
         .method_08892("%")
         .method_08914(SettingsDetailLevel.SIMPLE)
         .method_08894(() -> (Boolean)this.recoveredField1519.getValue());
      this.recoveredField1515 = new Setting(this, "Fire Height")
         .setValue(1.0F)
         .setMinMax(0.0F, 2.0F)
         .method_08914(SettingsDetailLevel.SIMPLE)
         .method_08894(() -> (Boolean)this.recoveredField1519.getValue());
      this.recoveredField1514 = new Setting(this, "Fire on Entities").setValue(true).method_08914(SettingsDetailLevel.SIMPLE);
      new Setting(this, "label").setValue("Vignette Options").method_08914(SettingsDetailLevel.SIMPLE);
      this.recoveredField1522 = new Setting(this, "Vignette Type")
         .setValue("Vanilla")
         .acceptedValues("Vanilla", "Static", "Amplified")
         .method_08914(SettingsDetailLevel.SIMPLE);
      this.recoveredField1520 = new Setting(this, "Vignette Opacity")
         .setValue(75.0F)
         .setMinMax(0.0F, 100.0F)
         .method_08892("%")
         .method_08914(SettingsDetailLevel.SIMPLE)
         .method_08894(() -> this.recoveredField1522.getValue().equals("Static"));
      this.recoveredField1517 = new Setting(this, "Vignette Minimum Opacity")
         .setValue(0.0F)
         .setMinMax(0.0F, 100.0F)
         .method_08892("%")
         .method_08914(SettingsDetailLevel.SIMPLE)
         .method_08894(() -> this.recoveredField1522.getValue().equals("Amplified"));
      this.recoveredField1524 = new Setting(this, "Vignette Maximum Opacity")
         .setValue(100.0F)
         .setMinMax(0.0F, 100.0F)
         .method_08892("%")
         .method_08914(SettingsDetailLevel.SIMPLE)
         .method_08894(() -> this.recoveredField1522.getValue().equals("Amplified"));
      this.recoveredField1508 = new Setting(this, "Vignette Opacity Multiplier")
         .setValue(1.0F)
         .setMinMax(0.1F, 10.0F)
         .method_08892("x")
         .method_08914(SettingsDetailLevel.SIMPLE)
         .method_08894(() -> this.recoveredField1522.getValue().equals("Amplified"));
      new Setting(this, "label").setValue("Low Health Options").method_08914(SettingsDetailLevel.MEDIUM);
      this.recoveredField1512 = new Setting(this, "Low Health Overlay").setValue(false).method_08914(SettingsDetailLevel.MEDIUM);
      this.recoveredField1507 = new Setting(this, "Low Health Start")
         .setValue(3.5F)
         .setMinMax(1.0F, 10.0F)
         .method_08892(" hearts")
         .method_08914(SettingsDetailLevel.MEDIUM)
         .method_08894(() -> this.recoveredField1512.method_08908());
      this.recoveredField1510 = new Setting(this, "Low Health Opacity")
         .setValue(75.0F)
         .setMinMax(0.0F, 100.0F)
         .method_08892("%")
         .method_08914(SettingsDetailLevel.MEDIUM)
         .method_08894(() -> this.recoveredField1512.method_08908());
      new Setting(this, "label").setValue("Pumpkin Overlay Options").method_08914(SettingsDetailLevel.SIMPLE);
      this.recoveredField1516 = new Setting(this, "Show Pumpkin Overlay").setValue(true).method_08914(SettingsDetailLevel.SIMPLE);
      this.recoveredField1518 = new Setting(this, "Pumpkin Overlay Opacity")
         .setValue(100.0F)
         .setMinMax(0.0F, 100.0F)
         .method_08892("%")
         .method_08914(SettingsDetailLevel.SIMPLE)
         .method_08894(() -> (Boolean)this.recoveredField1516.getValue());
      new Setting(this, "label").setValue("Inventory Options").method_08914(SettingsDetailLevel.SIMPLE);
      this.recoveredField1506 = new Setting(this, "Transparent Inventory").setValue(false).method_08914(SettingsDetailLevel.SIMPLE);
      this.recoveredField1511 = new Setting(this, "Show Inventory Tooltips").setValue(true).method_08914(SettingsDetailLevel.SIMPLE);
      this.setPreviewIcon(new ResourceLocation("client/icons/mods/packtweaks.png"), 32, 32);
   }
}
