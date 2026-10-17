package com.cheatbreaker.client.module.type;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.config.Setting;
import com.cheatbreaker.client.module.AbstractModule;

public class AnimationsModule extends AbstractModule {
   public Setting recoveredField3721;
   public Setting recoveredField3722;
   public Setting recoveredField3723;
   public Setting recoveredField3724;
   public Setting recoveredField3725;
   public Setting recoveredField3726;
   public Setting recoveredField3727;
   public Setting recoveredField3728;
   public Setting recoveredField3729;
   public Setting recoveredField3730;
   public Setting recoveredField3731;
   public Setting recoveredField3732;
   public Setting recoveredField3733;
   public Setting recoveredField3734;
   public Setting enchantmentGlint;

   public AnimationsModule() {
      super("Animations");
      this.method_28821("Set visuals to legacy and modern versions alike.");
      this.setPreviewLabel("1.7 <-> 1.8", 1.0F);
      this.method_28829("OrangeMarshall (Original)", "Sk1er (Revised)");
      this.method_28807("1.7 Visuals", "Old Animations");
      this.method_28823("Punch During Usage is disabled.", "minemenclub");
      this.setDefaultState(true);
      new Setting(this, "label").setValue("Position Settings");
      this.recoveredField3732 = new Setting(this, "Item Positions", "Change all item models to be in the same position as 1.7.")
         .setValue(true)
         .method_08916("1.8", "1.7");
      this.recoveredField3722 = new Setting(this, "Bow Pullback", "Change the bow pullback animation to be like 1.7.")
         .setValue(true)
         .method_08916("1.8", "1.7");
      this.recoveredField3734 = new Setting(this, "Block Animation", "Change the sword block animation to be like 1.7.")
         .setValue(true)
         .method_08916("1.8", "1.7");
      this.recoveredField3733 = new Setting(this, "Rod Position", "Change all item models to be in the same position as 1.7.")
         .setValue(true)
         .method_08916("1.8", "1.7");
      this.recoveredField3723 = new Setting(this, "3rd Person Block Animation", "Change the 3rd person blocking animation to be like 1.7.")
         .setValue(true)
         .method_08916("1.8", "1.7");
      new Setting(this, "label").setValue("Interaction Settings");
      this.recoveredField3726 = new Setting(this, "Consume Animation", "Change the eating and drinking animation to look like 1.7.")
         .setValue(true)
         .method_08916("1.8", "1.7");
      this.recoveredField3724 = new Setting(this, "Block-Hitting Animation", "Makes block hitting look much smoother, like it did in 1.7.")
         .setValue(true)
         .method_08916("1.8", "1.7");
      this.recoveredField3727 = new Setting(
            this, "Smooth Sneaking", "Makes the transition between sneaking/not sneaking smooth.\\n§eCombine with longer unsneak to match 1.7."
         )
         .setValue(true);
      this.recoveredField3721 = new Setting(
            this, "Longer Unsneak", "Makes moving up take longer than moving down\\n§eCombine with smooth sneaking to match 1.7."
         )
         .setValue(true)
         .method_08894(() -> this.recoveredField3727.method_08908());
      this.recoveredField3729 = new Setting(this, "Punching During Usage", "Allows you to punch blocks whilst using an item.\\n§eVisual only.")
         .setValue(true)
         .method_08916("1.8", "1.7");
      this.recoveredField3728 = new Setting(
            this, "Item Switching Animation", "Stop held items from playing the switching animation when right clicking on blocks."
         )
         .setValue(true)
         .method_08916("1.8", "1.7");
      this.recoveredField3725 = new Setting(
            this, "Hit Color Armor", "1.7: Make the hit color apply to the armor.\n1.8: Make the hit color only apply to the player skin."
         )
         .setValue(true)
         .method_08917(var0 -> CheatBreaker.getInstance().getModuleManager().recoveredField1731.recoveredField3678.setValue(var0))
         .method_08916("1.8", "1.7");
      this.recoveredField3730 = new Setting(this, "Hit Color Brightness", "Set the hit color to use 1.7's brightness system, darkening the hit color.")
         .setValue(true)
         .method_08916("1.8", "1.7");
      new Setting(this, "label").setValue("HUD Settings");
      this.enchantmentGlint = new Setting(this, "Enchantment Glint", "Use native 1.7.10 inventory and hotbar glint, including its texture motion and potion brightness.")
         .setValue(true)
         .method_08916("1.8", "1.7");
      this.recoveredField3731 = new Setting(this, "Health Bar Flashing", "Stops your health bar flashing when you take damage.")
         .setValue(true)
         .method_08916("1.8", "1.7");
   }
}
