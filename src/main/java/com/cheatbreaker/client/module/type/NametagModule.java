package com.cheatbreaker.client.module.type;

import com.cheatbreaker.client.config.Setting;
import com.cheatbreaker.client.module.AbstractModule;
import com.cheatbreaker.client.util.UuidParser;
import com.cheatbreaker.client.util.player.PlayerNametagStyle;
import java.util.UUID;
import java.util.regex.Pattern;
import net.minecraft.util.EnumChatFormatting;

public class NametagModule extends AbstractModule {
   private final PlayerNametagStyle localLogoStyle = new PlayerNametagStyle("", "", new int[]{0xFFFFFF, 0xFFFFFF});
   public Setting recoveredField2911;
   public String[] recoveredField2912 = new String[]{
      " ", "[", "]", "!", "=", "-", "@", "#", "$", "%", "^", "&", "*", "(", ")", "~", "`", "|", ";", ":", "'", "\"", ",", "<", ">", ".", "?", "/"
   };
   public Setting recoveredField2913;
   public Setting recoveredField2914;
   public Setting recoveredField2915;
   public Setting recoveredField2916;
   public Setting recoveredField2917;
   public Setting recoveredField2918;
   public Setting recoveredField2919;
   public Setting recoveredField2920;
   public Setting recoveredField2921;
   public Setting recoveredField2922;

   public NametagModule() {
      super("Nametag");
      this.setDefaultState(true);
      this.recoveredField2919 = new Setting(this, "label").setValue("General Options");
      this.recoveredField2920 = new Setting(this, "Show Background").setValue(true);
      this.recoveredField2918 = new Setting(this, "Text Shadow").setValue(false);
      this.recoveredField2922 = new Setting(this, "Show CheatBreaker Logo", "Show the logo for yourself and recognized CheatBreaker players.").setValue(true);
      this.recoveredField2921 = new Setting(this, "Show Sub Icons").setValue(false).method_08894(this.recoveredField2922::method_08908);
      this.recoveredField2915 = new Setting(this, "Shown own Nametag").setValue(false);
      this.recoveredField2916 = new Setting(this, "Show Nametags when GUI is hidden").setValue(false);
      this.recoveredField2913 = new Setting(this, "label").setValue("Color Options");
      this.recoveredField2917 = new Setting(this, "Custom Text Color").setValue(false);
      this.recoveredField2914 = new Setting(this, "Text Color")
         .setValue(-1)
         .setMinMax(Integer.MIN_VALUE, Integer.MAX_VALUE)
         .method_08894(() -> (Boolean)this.recoveredField2917.getValue());
      this.recoveredField2911 = new Setting(this, "Background Color")
         .setValue(1073741824)
         .setMinMax(Integer.MIN_VALUE, Integer.MAX_VALUE)
         .method_08894(() -> (Boolean)this.recoveredField2920.getValue());
      this.method_28821("Displays the username above players.");
   }

   public boolean method_21275(String var1) {
      for (String var5 : this.recoveredField2912) {
         if (var1.contains(var5)) {
            return false;
         }
      }

      return true;
   }

   public PlayerNametagStyle getLogoStyle(UUID playerId, String playerName, String label, boolean localPlayer,
      String localAlias, UuidParser styles) {
      if (!this.isEnabled() || !this.recoveredField2922.method_08908()
         || !(containsPlayerName(label, playerName) || localPlayer && containsPlayerName(label, localAlias))) {
         return null;
      }
      // Only our own client is known locally; remote players require UUID-based service data.
      PlayerNametagStyle style = styles == null ? null : styles.method_21030().get(playerId);
      return style != null ? style : localPlayer ? this.localLogoStyle : null;
   }

   private static boolean containsPlayerName(String label, String name) {
      if (label == null || name == null || name.isEmpty()) return false;
      String plain = EnumChatFormatting.getTextWithoutFormattingCodes(label);
      return Pattern.compile("(?<![A-Za-z0-9_])" + Pattern.quote(name) + "(?![A-Za-z0-9_])").matcher(plain).find();
   }
}
