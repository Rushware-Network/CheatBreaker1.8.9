package com.cheatbreaker.client.module.type;

import com.cheatbreaker.client.config.Setting;
import com.cheatbreaker.client.config.SettingsDetailLevel;
import com.cheatbreaker.client.module.AbstractModule;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import com.cheatbreaker.client.event.type.ChatMessageEvent;

public class HypixelModule extends AbstractModule {
   public Setting recoveredField439;
   public Setting recoveredField440;
   public Setting recoveredField441;
   public Setting recoveredField442;
   public volatile List<String> recoveredField443 = new ArrayList<>();
   public Setting autoGgUseDefaultTriggers;
   public Setting autoGgCustomTriggers;
   public Setting recoveredField444;
   public Setting recoveredField445;
   public Setting recoveredField446;
   public Pattern recoveredField447;
   public Setting recoveredField448;
   public Setting recoveredField449;
   public Setting recoveredField450;
   public Setting recoveredField451;
   public Setting recoveredField452;
   public Pattern recoveredField453;
   public Setting recoveredField454;
   public Setting recoveredField455;
   public Setting recoveredField456;
   public Pattern recoveredField457 = Pattern.compile(
      "§m----------------------------------------------------Friend request from (?<name>.+)\\[ACCEPT\\] - \\[DENY\\] - \\[IGNORE\\].*"
   );
   public Setting recoveredField458;
   public Setting recoveredField459;
   public Setting recoveredField460;
   public Setting recoveredField461;
   public Setting recoveredField462;
   public Setting recoveredField463;
   public Setting recoveredField464;
   public Pattern recoveredField465;
   public String[] recoveredField466;

   public void method_25694(String var1, float var2) {
      new Thread(() -> {
         try {
            Thread.sleep((long)var2 * 1000L);
         } catch (InterruptedException var4) {
            var4.printStackTrace();
         }

         this.minecraft.thePlayer.sendChatMessage(var1);
      }).start();
   }

   public HypixelModule() {
      super("Hypixel Collection");
      this.recoveredField453 = Pattern.compile("BED DESTRUCTION > Your Bed .*");
      this.recoveredField466 = new String[]{"You have been eliminated!", "Reward Summary"};
      this.recoveredField447 = Pattern.compile("Guild > (?<name>.+) joined\\..*");
      this.recoveredField465 = Pattern.compile("Friend > (?<name>.+) joined\\..*");
      this.setDefaultState(false);
      this.recoveredField460 = new Setting(this, "label").setValue("Auto Command Options");
      this.recoveredField451 = new Setting(this, "Auto Tip").setValue(false);
      this.recoveredField441 = new Setting(this, "Auto Friend").setValue(false);
      this.recoveredField464 = new Setting(this, "Auto Friend Mode").setValue("Accept").acceptedValues("Accept", "Deny", "Ignore");
      this.recoveredField454 = new Setting(this, "Auto Friend Delay")
         .method_08892("s")
         .setValue(0.0F)
         .setMinMax(0.0F, 15.0F)
         .method_08894(this.recoveredField441::method_08908);
      this.recoveredField446 = new Setting(this, "label").setValue("Auto Chat Options");
      this.recoveredField450 = new Setting(this, "Auto GG").setValue(false);
      this.autoGgUseDefaultTriggers = new Setting(this, "Use Default GG Triggers",
         "Use the downloaded triggers as well as your custom triggers.")
         .setValue(true)
         .method_08894(this.recoveredField450::method_08908);
      this.autoGgCustomTriggers = new Setting(this, "GG Triggers String",
         "Separate phrases with ;. Matches chat text with exact case. Empty entries are ignored.")
         .setValue("")
         .setMaxStringLength(512)
         .method_08894(this.recoveredField450::method_08908);
      this.recoveredField449 = new Setting(this, "Send GG Message Delay")
         .method_08892("s")
         .setValue(0.0F)
         .setMinMax(0.0F, 5.0F)
         .method_08894(this.recoveredField450::method_08908);
      this.recoveredField445 = new Setting(this, "Auto GG String")
         .setValue("gg")
         .method_08894(this.recoveredField450::method_08908)
         .method_08914(SettingsDetailLevel.MEDIUM);
      this.recoveredField440 = new Setting(this, "Auto GL").setValue(false);
      this.recoveredField461 = new Setting(this, "Send GL Message Delay")
         .method_08892("s")
         .setValue(0.0F)
         .setMinMax(0.0F, 15.0F)
         .method_08894(this.recoveredField440::method_08908);
      this.recoveredField458 = new Setting(this, "Auto GL String")
         .setValue("gl")
         .method_08894(this.recoveredField440::method_08908)
         .method_08914(SettingsDetailLevel.MEDIUM);
      this.recoveredField448 = new Setting(this, "Auto WB").setValue(false);
      this.recoveredField444 = new Setting(this, "Welcome Back Friends").setValue(true).method_08894(this.recoveredField448::method_08908);
      this.recoveredField459 = new Setting(this, "Welcome Back Guild Members").setValue(true).method_08894(this.recoveredField448::method_08908);
      this.recoveredField456 = new Setting(this, "Send WB Message Delay")
         .method_08892("s")
         .setValue(2.0F)
         .setMinMax(2.0F, 15.0F)
         .method_08894(this.recoveredField448::method_08908);
      this.recoveredField452 = new Setting(this, "Auto WB String")
         .setValue("Welcome back %USER%!")
         .method_08894(this.recoveredField448::method_08908)
         .method_08914(SettingsDetailLevel.MEDIUM);
      this.recoveredField442 = new Setting(this, "label").setValue("Chat Filter Options");
      this.recoveredField462 = new Setting(this, "Hide GG").setValue(false);
      this.recoveredField463 = new Setting(this, "Hide GL").setValue(false);
      this.recoveredField455 = new Setting(this, "label").setValue("Bed Wars Options");
      this.recoveredField439 = new Setting(this, "Hardcore Hearts", "Changes your hearts to show hardcore hearts when you lose your own bed.")
         .setValue(false)
         .method_08914(SettingsDetailLevel.SIMPLE);
      this.setPreviewIcon(new ResourceLocation("client/icons/mods/hypixel.png"), 21, 40);
      this.method_28821("A collection of mods made for the Hypixel Network.");
      this.method_28829("2Pi (Auto GG, Auto Friend, Auto Tip)", "Sk1er (Auto GL, Hardcore Hearts)", "Maximusbarcz (Auto WB)");
      this.method_28820(ChatMessageEvent.class, this::method_25691);
      this.method_25693();
   }

   public void method_25693() {
      new Thread(() -> {
         try {
            String var1 = IOUtils.toString(new URL("https://gist.githubusercontent.com/minemanpi/72c38b0023f5062a5f3eba02a5132603/raw/triggers.txt"));
            List<String> triggers = new ArrayList<>();
            for (String line : var1.split("\n")) {
               String trigger = line.trim();
               if (!trigger.isEmpty()) {
                  triggers.add(trigger);
               }
            }
            this.recoveredField443 = triggers;
         } catch (Exception var2) {
            var2.printStackTrace();
         }
      }).start();
   }

   public static boolean matchesCustomGgTrigger(String message, String triggers) {
      for (String entry : triggers.split("[;；\\r\\n]")) {
         String trigger = entry.trim();
         if (!trigger.isEmpty() && message.contains(trigger)) {
            return true;
         }
      }
      return false;
   }

   public boolean matchesAutoGgTrigger(String message) {
      return matchesCustomGgTrigger(message, this.autoGgCustomTriggers.method_08874())
         || this.autoGgUseDefaultTriggers.method_08908()
            && this.recoveredField443.stream().anyMatch(message::contains);
   }

   public void method_25691(ChatMessageEvent var1) {
      String var3 = var1.method_00228();
      if (this.minecraft.getCurrentServerData() != null && this.minecraft.getCurrentServerData().serverIP.toLowerCase().contains("hypixel")) {
         if (var3.contains(this.recoveredField466[0]) || var3.contains(this.recoveredField466[1])) {
            Minecraft.getMinecraft().thePlayer.o.P().setHardcore(false);
         }

         if (this.recoveredField439.method_08908() && this.recoveredField453.matcher(var3).matches()) {
            this.minecraft.thePlayer.o.P().setHardcore(true);
         }

         if (this.recoveredField450.method_08908() && this.matchesAutoGgTrigger(var3)) {
            this.method_25694("/achat " + this.recoveredField445.method_08874(), this.recoveredField449.method_08905());
         }

         if (this.recoveredField440.method_08908() && var3.startsWith("The game starts in 5 seconds!")) {
            this.method_25694("/achat " + this.recoveredField458.method_08874(), this.recoveredField461.method_08905());
         }

         Matcher var2;
         if (this.recoveredField448.method_08908() && this.recoveredField459.method_08908() && (var2 = this.recoveredField447.matcher(var3)).matches()) {
            String var4 = var2.group("name");
            this.method_25694("/gc " + this.recoveredField452.method_08874().replaceAll("%USER%", var4), this.recoveredField456.method_08905());
         }

         if (this.recoveredField448.method_08908() && this.recoveredField444.method_08908() && (var2 = this.recoveredField465.matcher(var3)).matches()) {
            String var7 = var2.group("name");
            this.method_25694("/msg " + var7 + " " + this.recoveredField452.method_08874().replaceAll("%USER%", var7), this.recoveredField456.method_08905());
         }

         if (this.recoveredField441.method_08908() && (var2 = this.recoveredField457.matcher(var3.replace("\n", ""))).matches()) {
            String var8 = var2.group("name");
            if (var8.startsWith("[")) {
               var8 = var8.substring(var8.indexOf("] ") + 2);
            }

            this.method_25694("/friend " + this.recoveredField464.method_08874().toLowerCase() + " " + var8, this.recoveredField454.method_08905());
         }
      }
   }
}
