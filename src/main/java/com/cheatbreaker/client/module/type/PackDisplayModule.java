package com.cheatbreaker.client.module.type;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.config.Setting;
import com.cheatbreaker.client.config.SettingsDetailLevel;
import com.cheatbreaker.client.event.type.TickEvent;
import java.util.regex.Pattern;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.src.Config;
import net.minecraft.util.ResourceLocation;

public class PackDisplayModule extends IconTextHudModule {
   public Setting recoveredField3104;
   public Pattern recoveredField3105 = Pattern.compile("(?i)§[0-9A-F]");
   public Setting recoveredField3106;
   public Setting recoveredField3107;
   public Setting recoveredField3108;
   public Setting recoveredField3109;
   public int recoveredField3110;
   public Pattern recoveredField3111 = Pattern.compile("(?i)§[1-69A-E]");
   public Setting recoveredField3112;
   public int recoveredField3113;

   public void method_01863(TickEvent var1) {
      this.recoveredField3110++;
      if ((Boolean)this.recoveredField3112.getValue().equals("Rotate") && this.recoveredField3110 > this.recoveredField3106.method_08905() * 20.0F) {
         int var2 = Config.getResourcePackNames().split(",").length - 1;
         String var3 = this.recoveredField3107.method_08874();
         switch (var3) {
            case "Top-Bottom":
               this.recoveredField3113--;
               if (this.recoveredField3113 < 0) {
                  this.recoveredField3113 = var2;
               }
               break;
            case "Bottom-Top":
               this.recoveredField3113++;
               if (this.recoveredField3113 > var2) {
                  this.recoveredField3113 = 0;
               }
               break;
            case "Randomized":
               this.recoveredField3113 = (int)(Math.random() * (var2 + 1));
         }

         SimpleReloadableResourceManager.method_01482();
         this.recoveredField3110 = 0;
      }
   }

   public String method_01866(String var1) {
      return var1 == null
         ? null
         : (this.recoveredField3108.getValue().equals("ALL") ? this.recoveredField3105 : this.recoveredField3111).matcher(var1).replaceAll("§r");
   }

   @Override
   public void method_00165() {
      super.method_00165();
      this.recoveredField3112 = new Setting(this, "Pack Order")
         .setValue("Bottom")
         .acceptedValues("Bottom", "Top", "Rotate")
         .method_08894(() -> Config.getResourcePackNames().split(",").length != 1)
         .onChange(var0 -> SimpleReloadableResourceManager.method_01482());
      this.recoveredField3107 = new Setting(this, "Rotation Order")
         .setValue("Top-Bottom")
         .acceptedValues("Top-Bottom", "Bottom-Top", "Randomized")
         .method_08894(() -> this.recoveredField3112.getValue().equals("Rotate") && Config.getResourcePackNames().split(",").length != 1);
      this.recoveredField3106 = new Setting(this, "Rotation Interval")
         .setValue(10.0F)
         .setMinMax(1.0F, 60.0F)
         .method_08892("s")
         .method_08894(() -> this.recoveredField3112.getValue().equals("Rotate") && Config.getResourcePackNames().split(",").length != 1);
   }

   @Override
   public void method_01862() {
      this.recoveredField3104 = new Setting(this, "Remove zip string", "Removes the \".zip\" part of a pack name if the file is in a zip.")
         .setValue(true)
         .method_08914(SettingsDetailLevel.MEDIUM);
      this.recoveredField3109 = new Setting(this, "Remove prefixes", "Removes ! and # prefixes from a pack.")
         .setValue(true)
         .method_08914(SettingsDetailLevel.MEDIUM);
      this.recoveredField3108 = new Setting(this, "Strip Color Formatting", "Remove all color formatting so you can set your own color.")
         .setValue("OFF")
         .acceptedValues("OFF", "Not Neutral", "ALL")
         .method_08914(SettingsDetailLevel.MEDIUM);
   }

   public PackDisplayModule() {
      super("Pack Display", "Default");
      this.recoveredField3110 = 0;
      this.recoveredField3113 = 0;
      this.method_28821("Displays your active Resource Pack(s).");
      this.method_28829("canelex");
      this.setPreviewIcon(new ResourceLocation("client/icons/mods/default_pack.png"), 32, 32);
      this.method_28820(TickEvent.class, this::method_01863);
   }

   @Override
   public String method_00166() {
      return "Pack";
   }

   @Override
   public ResourceLocation method_01868() {
      return CheatBreaker.recoveredField1551;
   }

   @Override
   public String method_00167() {
      String[] var1 = Config.getResourcePackNames().split(",");
      String var2 = var1[var1.length - 1];
      if ((Boolean)this.recoveredField3112.getValue().equals("Bottom")) {
         var2 = var1[0];
      } else if ((Boolean)this.recoveredField3112.getValue().equals("Rotate")) {
         var2 = var1[this.recoveredField3113];
      }

      if (this.recoveredField3104.method_08908()) {
         var2 = var2.replace(".zip", "");
      }

      if (this.recoveredField3109.method_08908()) {
         var2 = var2.substring(var2.indexOf("!") + 1).substring(var2.indexOf("#") + 1).trim();
         if (var2.substring(0, 1).contains("§") && var2.substring(2, 3).contains(" ")) {
            var2 = var2.replaceFirst(" ", "");
         }
      }

      return !this.recoveredField3108.getValue().equals("OFF") ? this.method_01866(var2) : var2;
   }
}
