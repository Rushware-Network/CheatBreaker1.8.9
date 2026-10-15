package com.cheatbreaker.client.module.type;

import com.cheatbreaker.client.config.Setting;
import com.cheatbreaker.client.config.SettingsDetailLevel;
import java.awt.Color;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.network.OldServerPinger;
import net.minecraft.client.gui.Gui;
import com.cheatbreaker.client.config.IntegerRangeDefaults;

public class PingModule extends NumberHudModule {
   public OldServerPinger recoveredField112 = new OldServerPinger();
   public long recoveredField113 = -1L;
   public Setting recoveredField114;
   public Setting recoveredField115;
   public Setting recoveredField116;
   public Setting recoveredField117;
   public long recoveredField118;
   public Setting recoveredField119;
   public Setting recoveredField120;

   @Override
   public String method_00167() {
      ServerData var1 = this.minecraft.getCurrentServerData();
      this.method_26323(var1);
      if (var1 != null && var1.pingToServer > 0L) {
         if ((Boolean)this.recoveredField120.getValue()) {
            if (this.recoveredField113 <= 1L) {
               this.recoveredField113 = var1.pingToServer;
            } else if (this.recoveredField113 >= 5000L) {
               this.recoveredField113 = var1.pingToServer;
            }
         } else {
            this.recoveredField113 = var1.pingToServer;
         }
      }

      if (this.method_09815(this.recoveredField2752, (int)this.recoveredField113, this.recoveredField2753.method_08912())) {
         return null;
      } else {
         return (this.minecraft.isIntegratedServerRunning() || this.minecraft.theWorld == null) && (Boolean)this.recoveredField119.getValue()
               || this.recoveredField113 == -1L && (Boolean)this.recoveredField115.getValue()
            ? null
            : this.recoveredField113 + "";
      }
   }

   @Override
   public String method_00164() {
      return "119";
   }

   @Override
   public String method_00166() {
      return "ms";
   }

   public PingModule() {
      super("Ping", "[19 ms]");
      this.method_28821("Displays your server latency.");
      this.setPreviewIcon(Gui.icons, 40, 32);
   }

   @Override
   public int getTextOffsetY() {
      if ((Boolean)this.recoveredField114.getValue()) {
         return this.method_00167() != null
            ? Color.getHSBColor(
                  Math.max((125.0F - (float)this.recoveredField113 * 10.0F / (Float)this.recoveredField116.getValue()) / 360.0F, 0.0F), 1.0F, 1.0F
               )
               .getRGB()
            : Color.getHSBColor(Math.max((125.0F - 1190.0F / (Float)this.recoveredField116.getValue()) / 360.0F, 0.0F), 1.0F, 1.0F).getRGB();
      } else {
         return this.recoveredField2232.method_08901();
      }
   }

   @Override
   public void method_04335() {
      this.recoveredField2232.method_08894(this.recoveredField114::method_08908);
   }

   @Override
   public IntegerRangeDefaults method_00168() {
      return new IntegerRangeDefaults(0, 100, 1000);
   }

   @Override
   public void method_00165() {
      super.method_00165();
      this.recoveredField117 = new Setting(this, "Refresh Time", "Changes how many seconds the mod should refresh itself.")
         .method_08892("s")
         .setValue(15)
         .setMinMax(10, 90)
         .method_08914(SettingsDetailLevel.MEDIUM);
      this.recoveredField115 = new Setting(this, "Hide if no connection", "Hide the mod when no connection is found.")
         .setValue(true)
         .method_08914(SettingsDetailLevel.MEDIUM);
      this.recoveredField119 = new Setting(this, "Hide in Singleplayer", "Hid the mod in Singleplayer.")
         .setValue(true)
         .method_08914(SettingsDetailLevel.MEDIUM)
         .method_08894(() -> !this.recoveredField115.method_08908());
      this.recoveredField120 = new Setting(this, "Accurate Mode", "Shows ping from server list so its accurate at all times.").setValue(true);
      this.recoveredField114 = new Setting(this, "Dynamic Color Range", "Determine if the text color should change dynamically based on ping amount.")
         .setValue(false)
         .method_08914(SettingsDetailLevel.MEDIUM);
      this.recoveredField116 = new Setting(this, "Bad Ping Range Threshold")
         .setValue(25.0F)
         .setMinMax(10.0F, 100.0F)
         .method_08894(this.recoveredField114::method_08908);
   }

   public void method_26323(ServerData var1) {
      if (this.minecraft.isIntegratedServerRunning() || this.minecraft.theWorld == null) {
         this.recoveredField113 = -1L;
      } else if (var1 != null
         && System.currentTimeMillis() - this.recoveredField118 >= TimeUnit.SECONDS.toMillis(((Integer)this.recoveredField117.getValue()).intValue())) {
         new Thread(() -> {
            try {
               if ((Boolean)this.recoveredField120.getValue()) {
                  String var2 = var1.serverIP;

                  try {
                     InetAddress var3 = InetAddress.getByName(var2);
                     long var4 = System.currentTimeMillis();
                     var3.isReachable(5000);
                     long var6 = System.currentTimeMillis();
                     this.recoveredField113 = var6 - var4;
                  } catch (IOException var8) {
                     var8.printStackTrace();
                     this.recoveredField113 = 1L;
                  }
               } else {
                  this.recoveredField112.ping(var1);
               }
            } catch (UnknownHostException var9) {
               var9.printStackTrace();
            }
         }).start();
         this.recoveredField118 = System.currentTimeMillis();
      }
   }
}
