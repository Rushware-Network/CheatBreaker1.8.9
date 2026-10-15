package com.cheatbreaker.client.module.type;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.config.Setting;
import com.cheatbreaker.client.module.AbstractModule;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

public class AutoTextModule extends AbstractModule {
   public List<Long> recoveredField1862;
   public List<Setting> recoveredField1863;
   public int recoveredField1864;
   public String recoveredField1865;
   public String[] recoveredField1866 = new String[]{"/skyblock", "/play", "/lobby", "/hub", "/spawn", "/main", "/leave", "/warp", "/rejoin"};
   public int recoveredField1867;

   public String method_26045() {
      return this.recoveredField1865;
   }

   public AutoTextModule() {
      super("Auto Text");
      this.recoveredField1865 = "";
      this.recoveredField1863 = new ArrayList<>();
      this.recoveredField1862 = new ArrayList<>();
      this.recoveredField1867 = 3;
      this.recoveredField1864 = 1;
      this.setDefaultState(false);
      this.setPreviewLabel("/command\n/command\n/command", 1.0F);
      this.method_28821("Send a command or message with a press of a key or button!");
      this.method_28823("The allowed commands are: " + Arrays.toString((Object[])this.recoveredField1866).replaceAll("\\[", "").replaceAll("]", ""), "hypixel");
      this.recoveredField1863.addAll(this.getSettingsList());
   }

   public void method_26044(String var1) {
      if (Minecraft.getMinecraft().isIntegratedServerRunning()) {
         Minecraft.getMinecraft().thePlayer.sendChatMessage(var1);
      } else {
         if (Minecraft.getMinecraft().getCurrentServerData().serverIP.contains("hypixel")) {
            if (this.method_26047(var1)) {
               Minecraft.getMinecraft().thePlayer.sendChatMessage(var1);
            } else {
               CheatBreaker.getInstance()
                  .getModuleManager()
                  .notifications
                  .queueNotification("error", EnumChatFormatting.RED + "\"" + var1 + "\" is not allowed on Hypixel.", 3000L);
            }
         } else {
            Minecraft.getMinecraft().thePlayer.sendChatMessage(var1);
         }
      }
   }

   public void method_26048(String var1) {
      this.recoveredField1865 = var1;
   }

   public void method_26046() {
      if (this.recoveredField1864 >= 50) {
         CheatBreaker.getInstance()
            .getModuleManager()
            .notifications
            .queueNotification("error", EnumChatFormatting.RED + "You have hit the limit of 50 hotkeys.", 5000L);
      } else {
         Setting var1 = new Setting(this, "Hot key " + this.recoveredField1864).setValue("/Command").method_08887(0).method_08909(false);
         this.recoveredField1863.add(var1);
         this.recoveredField1864++;

         for (Setting var3 : this.getSettingsList()) {
            if (var3.method_08911().equalsIgnoreCase(var1.method_08911())) {
               return;
            }
         }

         this.getSettingsList().add(var1);
         CheatBreaker.getInstance().getConfigManager().method_25109();
      }
   }

   public boolean method_26047(String var1) {
      for (String var5 : this.recoveredField1866) {
         if (var1.startsWith(var5)) {
            return true;
         }
      }

      return false;
   }
}
