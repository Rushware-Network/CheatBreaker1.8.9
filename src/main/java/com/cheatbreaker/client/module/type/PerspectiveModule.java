package com.cheatbreaker.client.module.type;

import com.cheatbreaker.client.config.Setting;
import com.cheatbreaker.client.module.AbstractModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

public class PerspectiveModule extends AbstractModule {
   public Setting recoveredField3165;
   public Setting recoveredField3166;
   public Setting recoveredField3167;
   public Setting recoveredField3168;
   public Setting recoveredField3173;
   public Setting recoveredField3178;
   public Setting recoveredField3169;
   public Setting recoveredField3170;
   public float recoveredField3172 = 0.0F;
   public Setting recoveredField3176;
   public Setting recoveredField3177;
   public Setting recoveredField3179;
   public Setting recoveredField3180;
   public Setting recoveredField3181;
   public Setting recoveredField3182;
   public Setting recoveredField3183;
   public Setting recoveredField3184;
   public Setting recoveredField3185;
   public Setting recoveredField3186;

   public void method_24319(float var1, Setting var2) {
      if (this.minecraft.getRenderViewEntity() instanceof EntityPlayer && var2.method_08908()) {
         EntityPlayer var3 = (EntityPlayer)this.minecraft.getRenderViewEntity();
         boolean var4 = this.recoveredField3180.getValue().equals("Only Screen");
         float var5 = this.recoveredField3176.method_08905() / 100.0F;
         if (var2 == this.recoveredField3186) {
            var4 = this.recoveredField3180.getValue().equals("Only Hand");
            var5 = this.recoveredField3182.method_08905() / 100.0F;
         }

         float var6 = var3.M - var3.L;
         float var7 = -(var3.M + var6 * var1);
         float var8 = var3.prevCameraYaw + (var3.cameraYaw - var3.prevCameraYaw) * var1;
         float var9 = var3.aE + (var3.aF - var3.aE) * var1;
         float var10 = !this.recoveredField3180.getValue().equals("ON") && !var4 ? var8 : this.recoveredField3172;
         if ((Boolean)this.recoveredField3180.getValue().equals("ON") || var4) {
            if (var3.isSprinting()) {
               this.recoveredField3172 = var8;
            } else {
               this.recoveredField3172 = (float)(this.recoveredField3172 - 9.0E-5);
            }

            if (this.recoveredField3172 < 0.0F) {
               return;
            }
         }

         GL11.glTranslatef(MathHelper.sin(var7 * (float) Math.PI) * var10 * 0.5F * var5, -Math.abs(MathHelper.cos(var7 * (float) Math.PI) * var10 * var5), 0.0F);
         GL11.glRotatef(MathHelper.sin(var7 * (float) Math.PI) * var10 * 3.0F * var5, 0.0F, 0.0F, 1.0F);
         GL11.glRotatef(Math.abs(MathHelper.cos(var7 * (float) Math.PI - 0.2F) * var10) * 5.0F * var5, 1.0F, 0.0F, 0.0F);
         GL11.glRotatef(var9, 1.0F, 0.0F, 0.0F);
      }
   }

   public PerspectiveModule() {
      super("Perspective");
      this.method_28821("Allows you to change how your perspective looks.");
      this.method_28807("Freelook", "Snaplook", "360 Perspective");
      this.method_28823("Drag to Look is disabled.", "hypixel");
      this.setDefaultState(true);
      this.setPreviewLabel("Perspective", 1.0F);
      this.recoveredField3169 = new Setting(this, "label").setValue("Third Person Options");
      this.recoveredField3185 = new Setting(this, "Toggle Back Camera", "Make the back camera keybind toggled instead of held.").setValue(false);
      this.recoveredField3170 = new Setting(this, "Toggle Front Camera", "Make the front camera keybind toggled instead of held.").setValue(false);
      this.recoveredField3183 = new Setting(this, "label").setValue("Drag to Look Options");
      this.recoveredField3184 = new Setting(this, "Look View", "Change which perspective mode to use when toggling Drag to Look.")
         .setValue("Third")
         .acceptedValues("Third", "Reverse", "First");
      this.recoveredField3179 = new Setting(this, "Toggle Drag to Look", "Make the Drag to Look keybind toggled instead of held.").setValue(false);
      this.recoveredField3177 = new Setting(this, "Pitch Lock", "Toggle the pitch axis limitation.").setValue(true);
      this.recoveredField3165 = new Setting(this, "Invert Pitch (Up and Down)", "Invert the rotation pitch when moving the camera.").setValue(false);
      this.recoveredField3167 = new Setting(this, "Invert Yaw (Left and Right)", "Invert the rotation yaw when moving the camera.").setValue(false);
      this.recoveredField3166 = new Setting(this, "label").setValue("View Bobbing Options");
      this.recoveredField3181 = new Setting(this, "Bob Screen", "Bob the camera screen when moving.").setValue(true);
      this.recoveredField3186 = new Setting(this, "Bob Hand", "Bob the hand when moving.").setValue(true);
      this.recoveredField3180 = new Setting(this, "Bob Only While Sprinting", "Bob the hand only when sprinting.")
         .setValue("OFF")
         .acceptedValues("OFF", "ON", "Only Hand", "Only Screen");
      this.recoveredField3176 = new Setting(this, "Screen Bobbing Intensity").method_08892("%").setValue(100.0F).setMinMax(0.0F, 200.0F);
      this.recoveredField3182 = new Setting(this, "Hand Bobbing Intensity").method_08892("%").setValue(100.0F).setMinMax(0.0F, 200.0F);
      new Setting(this, "label").setValue("Camera Options");
      this.recoveredField3178 = new Setting(this, "Static Swiftness", "Determines if your FOV changes as you move.").setValue(false);
      this.recoveredField3168 = new Setting(this, "Default FOV").method_08892(" FOV").setValue(30).setMinMax(30, 110);
      this.recoveredField3173 = new Setting(this, "Aiming Multiplier").method_08892("x").setValue(0.15F).setMinMax(0.15F, 0.75F);
   }
}
