package com.cheatbreaker.client.module.type;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.config.Setting;
import com.cheatbreaker.client.config.SettingsDetailLevel;
import com.cheatbreaker.client.event.type.GuiDrawEvent;
import com.cheatbreaker.client.module.AbstractModule;
import com.cheatbreaker.client.ui.module.CBGuiAnchor;
import com.mojang.authlib.GameProfile;
import java.util.List;
import java.util.regex.Pattern;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldSettings;
import org.lwjgl.opengl.GL11;
import com.cheatbreaker.client.event.type.HudPreviewDrawEvent;
import com.cheatbreaker.client.util.player.PlayerNametagStyle;

public class TabListModule extends AbstractModule {
   public Setting recoveredField1206;
   public Setting recoveredField1207;
   public Setting recoveredField1208;
   public Setting recoveredField1209;
   public Pattern recoveredField1210 = Pattern.compile("(?i)§[0-9A-F]");
   public Setting recoveredField1211;
   public Setting recoveredField1212;
   public Setting recoveredField1213;
   public Setting recoveredField1214;
   public Setting recoveredField1215;
   public Setting recoveredField1216;
   public Setting recoveredField1217;
   public Setting recoveredField1218;
   public Setting recoveredField1219;
   public Setting recoveredField1220;
   public static ResourceLocation recoveredField1221 = new ResourceLocation("textures/gui/icons.png");
   private static final ResourceLocation CLIENT_LOGO = new ResourceLocation("client/logo_white.png");
   public Setting recoveredField1222;
   public Setting recoveredField1223;
   public Setting recoveredField1224;
   public Setting recoveredField1225;
   public Setting recoveredField1226;
   public Setting recoveredField1227;
   public Setting recoveredField1228;
   public Setting recoveredField1229;
   public Setting recoveredField1230;
   public Setting recoveredField1231;

   public void method_20825(GuiDrawEvent event) {
      // The vanilla Tab overlay owns live rendering; retain this entry point for callers.
   }

   public String method_20833() {
      return null;
   }

   public String method_20818(String var1) {
      return var1 == null ? null : this.recoveredField1210.matcher(var1).replaceAll("§r");
   }

   public TabListModule() {
      super("Tablist");
      this.setDefaultAnchor(CBGuiAnchor.MIDDLE_TOP);
      this.setDefaultTranslations(0.0F, 12.0F);
      this.recoveredField3912 = false;
      new Setting(this, "label").setValue("General Options").method_08914(SettingsDetailLevel.SIMPLE);
      this.recoveredField1209 = new Setting(this, "Show Background").setValue(true).method_08914(SettingsDetailLevel.SIMPLE);
      this.recoveredField1226 = new Setting(this, "Show Row Background").setValue(true).method_08914(SettingsDetailLevel.SIMPLE);
      this.recoveredField1228 = new Setting(this, "Highlight Own Row").setValue(false).method_08914(SettingsDetailLevel.SIMPLE);
      this.recoveredField1206 = new Setting(this, "Highlight CheatBreaker Players' Row").setValue(false).method_08914(SettingsDetailLevel.SIMPLE);
      this.recoveredField1207 = new Setting(this, "Use Ping Color for Row Background")
         .setValue(false)
         .method_08914(SettingsDetailLevel.SIMPLE)
         .method_08894(() -> (Boolean)this.recoveredField1226.getValue());
      this.recoveredField1211 = new Setting(this, "Row Background Color")
         .setValue(553648127)
         .setMinMax(Integer.MIN_VALUE, Integer.MAX_VALUE)
         .method_08914(SettingsDetailLevel.SIMPLE)
         .method_08894(() -> (Boolean)this.recoveredField1226.getValue());
      this.recoveredField1230 = new Setting(this, "Self Row Color")
         .setValue(1084882944)
         .setMinMax(Integer.MIN_VALUE, Integer.MAX_VALUE)
         .method_08914(SettingsDetailLevel.MEDIUM)
         .method_08894(() -> (Boolean)this.recoveredField1226.getValue() && (Boolean)this.recoveredField1228.getValue());
      this.recoveredField1212 = new Setting(this, "CheatBreaker Row Color")
         .setValue(1088045651)
         .setMinMax(Integer.MIN_VALUE, Integer.MAX_VALUE)
         .method_08914(SettingsDetailLevel.MEDIUM)
         .method_08894(() -> (Boolean)this.recoveredField1226.getValue() && (Boolean)this.recoveredField1206.getValue());
      this.recoveredField1225 = new Setting(this, "Background Color")
         .setValue(Integer.MIN_VALUE)
         .setMinMax(Integer.MIN_VALUE, Integer.MAX_VALUE)
         .method_08914(SettingsDetailLevel.SIMPLE);
      new Setting(this, "label").setValue("Name Options").method_08914(SettingsDetailLevel.SIMPLE);
      this.recoveredField1227 = new Setting(this, "Name Text Shadow").setValue(true).method_08914(SettingsDetailLevel.SIMPLE);
      this.recoveredField1224 = new Setting(this, "Show CheatBreaker Logo").setValue(true).method_08914(SettingsDetailLevel.SIMPLE);
      this.recoveredField1215 = new Setting(this, "Highlight Own Name").setValue(false).method_08914(SettingsDetailLevel.SIMPLE);
      this.recoveredField1222 = new Setting(this, "Highlight CheatBreaker Players' Name").setValue(false).method_08914(SettingsDetailLevel.SIMPLE);
      this.recoveredField1231 = new Setting(this, "Self Text Color")
         .setValue(-3399134)
         .setMinMax(Integer.MIN_VALUE, Integer.MAX_VALUE)
         .method_08914(SettingsDetailLevel.MEDIUM)
         .method_08894(() -> (Boolean)this.recoveredField1215.getValue());
      this.recoveredField1229 = new Setting(this, "CheatBreaker Text Color")
         .setValue(-301706)
         .setMinMax(Integer.MIN_VALUE, Integer.MAX_VALUE)
         .method_08914(SettingsDetailLevel.MEDIUM)
         .method_08894(() -> (Boolean)this.recoveredField1222.getValue());
      new Setting(this, "label")
         .setValue("Ping Options")
         .method_08894(() -> !this.minecraft.isIntegratedServerRunning())
         .method_08914(SettingsDetailLevel.SIMPLE);
      this.recoveredField1214 = new Setting(this, "Show Ping")
         .setValue(true)
         .method_08894(() -> !this.minecraft.isIntegratedServerRunning())
         .method_08914(SettingsDetailLevel.SIMPLE);
      this.recoveredField1219 = new Setting(this, "Show Ping as Number")
         .setValue(false)
         .method_08914(SettingsDetailLevel.SIMPLE)
         .method_08894(() -> (Boolean)this.recoveredField1214.getValue() && !this.minecraft.isIntegratedServerRunning());
      this.recoveredField1213 = new Setting(this, "Ping Text Shadow")
         .setValue(true)
         .method_08914(SettingsDetailLevel.MEDIUM)
         .method_08894(
            () -> (Boolean)this.recoveredField1214.getValue() && (Boolean)this.recoveredField1219.getValue() && !this.minecraft.isIntegratedServerRunning()
         );
      this.recoveredField1216 = new Setting(this, "Dynamic Ping Number Color")
         .setValue(true)
         .method_08914(SettingsDetailLevel.MEDIUM)
         .method_08894(
            () -> (Boolean)this.recoveredField1214.getValue() && (Boolean)this.recoveredField1219.getValue() && !this.minecraft.isIntegratedServerRunning()
         );
      this.recoveredField1220 = new Setting(this, "Static Ping Color")
         .setValue(-171)
         .setMinMax(Integer.MIN_VALUE, Integer.MAX_VALUE)
         .method_08914(SettingsDetailLevel.ADVANCED)
         .method_08894(
            () -> (Boolean)this.recoveredField1214.getValue()
               && (Boolean)this.recoveredField1219.getValue()
               && !(Boolean)this.recoveredField1216.getValue()
               && !this.minecraft.isIntegratedServerRunning()
         );
      this.recoveredField1223 = new Setting(this, "Low Ping Color")
         .setValue(-11141291)
         .setMinMax(Integer.MIN_VALUE, Integer.MAX_VALUE)
         .method_08914(SettingsDetailLevel.ADVANCED)
         .method_08894(
            () -> (Boolean)this.recoveredField1214.getValue()
               && (Boolean)this.recoveredField1219.getValue()
               && (Boolean)this.recoveredField1216.getValue()
               && !this.minecraft.isIntegratedServerRunning()
         );
      this.recoveredField1218 = new Setting(this, "Medium Ping Color")
         .setValue(-171)
         .setMinMax(Integer.MIN_VALUE, Integer.MAX_VALUE)
         .method_08914(SettingsDetailLevel.ADVANCED)
         .method_08894(
            () -> (Boolean)this.recoveredField1214.getValue()
               && (Boolean)this.recoveredField1219.getValue()
               && (Boolean)this.recoveredField1216.getValue()
               && !this.minecraft.isIntegratedServerRunning()
         );
      this.recoveredField1217 = new Setting(this, "High Ping Color")
         .setValue(-43691)
         .setMinMax(Integer.MIN_VALUE, Integer.MAX_VALUE)
         .method_08914(SettingsDetailLevel.ADVANCED)
         .method_08894(
            () -> (Boolean)this.recoveredField1214.getValue()
               && (Boolean)this.recoveredField1219.getValue()
               && (Boolean)this.recoveredField1216.getValue()
               && !this.minecraft.isIntegratedServerRunning()
         );
      this.recoveredField1208 = new Setting(this, "Extremely High Ping Color")
         .setValue(-5636096)
         .setMinMax(Integer.MIN_VALUE, Integer.MAX_VALUE)
         .method_08914(SettingsDetailLevel.ADVANCED)
         .method_08894(
            () -> (Boolean)this.recoveredField1214.getValue()
               && (Boolean)this.recoveredField1219.getValue()
               && (Boolean)this.recoveredField1216.getValue()
               && !this.minecraft.isIntegratedServerRunning()
         );
      this.method_28821("Customize the player list tab.");
      this.method_28807("Tab", "Player List");
      this.method_28820(HudPreviewDrawEvent.class, this::method_20824);
   }

   public void method_20824(HudPreviewDrawEvent event) {
      if (this.method_28866() && this.minecraft.theWorld != null && this.minecraft.thePlayer != null) {
         Scoreboard scoreboard = this.minecraft.theWorld.Z();
         this.method_20823(event.method_01054().getScaledWidth(), scoreboard, scoreboard.getObjectiveInDisplaySlot(0), 0, 0);
      }
   }

   public void method_20823(int screenWidth, Scoreboard scoreboard, ScoreObjective objective, int unusedX, int unusedY) {
      if (this.minecraft.thePlayer == null || this.minecraft.theWorld == null || this.minecraft.getNetHandler() == null) {
         return;
      }
      GuiPlayerTabOverlay overlay = this.minecraft.ingameGUI.overlayPlayerList;
      List<NetworkPlayerInfo> players = GuiPlayerTabOverlay.field_175252_a.sortedCopy(this.minecraft.thePlayer.sendQueue.getPlayerInfoMap());
      players = players.subList(0, Math.min(players.size(), 80));
      boolean heads = this.minecraft.isIntegratedServerRunning() || this.minecraft.getNetHandler().getNetworkManager().getIsencrypted();
      boolean showPing = this.recoveredField1214.method_08908();
      boolean numberPing = this.recoveredField1219.method_08908();
      int nameWidth = 0;
      int scoreWidth = objective != null && objective.getRenderType() == IScoreObjectiveCriteria.EnumRenderType.HEARTS ? 90 : 0;
      int pingWidth = showPing ? (numberPing ? 0 : 13) : 0;
      for (NetworkPlayerInfo player : players) {
         nameWidth = Math.max(nameWidth, this.minecraft.fontRendererObj.getStringWidth(this.getDisplayName(player)));
         if (objective != null && objective.getRenderType() != IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
            scoreWidth = Math.max(scoreWidth, this.minecraft.fontRendererObj.getStringWidth(" " + scoreboard.getValueFromObjective(player.getGameProfile().getName(), objective).getScorePoints()));
         }
         if (showPing && numberPing) {
            pingWidth = Math.max(pingWidth, this.minecraft.fontRendererObj.getStringWidth(pingText(player.method_13010())) + 3);
         }
      }
      int logoWidth = this.recoveredField1224.method_08908() ? 10 : 0;
      int availableWidth = Math.max(1, (int)(screenWidth / this.method_28770()) - 50);
      TabListLayout layout = new TabListLayout(players.size(), (heads ? 9 : 0) + logoWidth + nameWidth + scoreWidth + pingWidth + 4, availableWidth);
      List<String> header = overlay.header == null ? java.util.Collections.emptyList()
         : this.minecraft.fontRendererObj.listFormattedStringToWidth(overlay.header.getFormattedText(), availableWidth);
      List<String> footer = overlay.footer == null ? java.util.Collections.emptyList()
         : this.minecraft.fontRendererObj.listFormattedStringToWidth(overlay.footer.getFormattedText(), availableWidth);
      int panelWidth = layout.width;
      for (String line : header) panelWidth = Math.max(panelWidth, this.minecraft.fontRendererObj.getStringWidth(line));
      for (String line : footer) panelWidth = Math.max(panelWidth, this.minecraft.fontRendererObj.getStringWidth(line));
      int headerHeight = header.isEmpty() ? 0 : header.size() * this.minecraft.fontRendererObj.FONT_HEIGHT + 1;
      int footerHeight = footer.isEmpty() ? 0 : footer.size() * this.minecraft.fontRendererObj.FONT_HEIGHT + 1;
      this.method_28812(panelWidth + 2, headerHeight + layout.rows * 9 + footerHeight + 2);
      GL11.glPushMatrix();
      try {
         this.scaleAndTranslate(new ScaledResolution(this.minecraft));
         GlStateManager.enableAlpha();
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
         if (this.recoveredField1209.method_08908()) {
            Gui.drawRect(0, 0, panelWidth + 2, this.recoveredField3894, this.recoveredField1225.method_08901());
         }
         int y = 1;
         for (String line : header) {
            this.minecraft.fontRendererObj.drawStringWithShadow(line, (panelWidth + 2 - this.minecraft.fontRendererObj.getStringWidth(line)) / 2.0F, y, -1);
            y += this.minecraft.fontRendererObj.FONT_HEIGHT;
         }
         if (!header.isEmpty()) y++;
         int startX = 1 + (panelWidth - layout.width) / 2;
         for (int index = 0; index < players.size(); index++) {
            NetworkPlayerInfo player = players.get(index);
            GameProfile profile = player.getGameProfile();
            boolean own = profile.getId().equals(this.minecraft.getSession().getProfile().getId());
            boolean clientPlayer = own || this.method_20834(profile.getName());
            int x = startX + layout.columnX(index);
            int rowY = y + layout.rowY(index);
            if (this.recoveredField1226.method_08908()) {
               Gui.drawRect(x, rowY, x + layout.columnWidth, rowY + 8, this.getRowColor(own, clientPlayer, player.method_13010()));
            }
            GlStateManager.color(1, 1, 1, 1);
            GlStateManager.enableBlend();
            int nameX = x;
            if (heads) {
               EntityPlayer entity = this.minecraft.theWorld.getPlayerEntityByUUID(profile.getId());
               boolean flipped = entity != null && entity.isWearing(EnumPlayerModelParts.CAPE)
                  && (profile.getName().equals("Dinnerbone") || profile.getName().equals("Grumm"));
               this.minecraft.getTextureManager().bindTexture(player.getLocationSkin());
               Gui.drawScaledCustomSizeModalRect(nameX, rowY, 8, flipped ? 16 : 8, 8, flipped ? -8 : 8, 8, 8, 64, 64);
               if (entity != null && entity.isWearing(EnumPlayerModelParts.HAT)) {
                  Gui.drawScaledCustomSizeModalRect(nameX, rowY, 40, flipped ? 16 : 8, 8, flipped ? -8 : 8, 8, 8, 64, 64);
               }
               nameX += 9;
            }
            if (logoWidth > 0) {
               if (clientPlayer) {
                  this.minecraft.getTextureManager().bindTexture(CLIENT_LOGO);
                  Gui.drawModalRectWithCustomSizedTexture(nameX, rowY + 2, 0, 0, 8, 5, 8, 5);
               }
               nameX += logoWidth;
            }
            String name = this.getDisplayName(player);
            boolean highlighted = own && this.recoveredField1215.method_08908() || clientPlayer && this.recoveredField1222.method_08908();
            if (highlighted) name = this.method_20818(name);
            boolean spectator = player.getGameType() == WorldSettings.GameType.SPECTATOR;
            if (spectator) name = EnumChatFormatting.ITALIC + name;
            int color = this.getNameColor(own, clientPlayer, spectator);
            int scoreStart = nameX + nameWidth + 1;
            int scoreEnd = x + layout.columnWidth - pingWidth - 1;
            String fittedName = this.minecraft.fontRendererObj.trimStringToWidth(name, Math.max(0, Math.min(nameWidth, scoreEnd - nameX)));
            this.minecraft.fontRendererObj.drawString(fittedName, nameX, rowY, color, this.recoveredField1227.method_08908());
            if (objective != null && !spectator && scoreEnd - scoreStart > 5) {
               overlay.drawScoreboardValues(objective, rowY, profile.getName(), scoreStart, scoreEnd, player);
            }
            if (showPing) {
               if (numberPing) {
                  String text = pingText(player.method_13010());
                  this.minecraft.fontRendererObj.drawString(text, x + layout.columnWidth - this.minecraft.fontRendererObj.getStringWidth(text) - 1,
                     rowY, this.getPingColor(player.method_13010()), this.recoveredField1213.method_08908());
               } else {
                  overlay.drawPing(layout.columnWidth, x, rowY, player);
               }
            }
         }
         y += layout.rows * 9 + (footer.isEmpty() ? 0 : 1);
         for (String line : footer) {
            this.minecraft.fontRendererObj.drawStringWithShadow(line, (panelWidth + 2 - this.minecraft.fontRendererObj.getStringWidth(line)) / 2.0F, y, -1);
            y += this.minecraft.fontRendererObj.FONT_HEIGHT;
         }
      } finally {
         GlStateManager.color(1, 1, 1, 1);
         GL11.glPopMatrix();
      }
   }

   public String getDisplayName(NetworkPlayerInfo player) {
      String name = this.minecraft.ingameGUI.overlayPlayerList.getPlayerName(player);
      NickHiderModule nick = CheatBreaker.getInstance().getModuleManager().recoveredField1705;
      if (nick != null && nick.isEnabled() && nick.recoveredField850.method_08908()) {
         String username = this.minecraft.getSession().getUsername();
         String replacement = nick.recoveredField849.method_08874();
         name = name.replace(username, replacement.equals(username) ? "You" : replacement);
      }
      return name;
   }

   public static String pingText(int ping) {
      return ping < 0 ? "?" : Integer.toString(ping);
   }

   public int getPingColor(int ping) {
      if (!this.recoveredField1216.method_08908()) return this.recoveredField1220.method_08901();
      if (ping < 0 || ping >= 600) return this.recoveredField1208.method_08901();
      if (ping < 150) return this.recoveredField1223.method_08901();
      if (ping < 300) return this.recoveredField1218.method_08901();
      return this.recoveredField1217.method_08901();
   }

   public int getRowColor(boolean own, boolean clientPlayer, int ping) {
      if (own && this.recoveredField1228.method_08908()) return this.recoveredField1230.method_08901();
      if (clientPlayer && this.recoveredField1206.method_08908()) return this.recoveredField1212.method_08901();
      int color = this.recoveredField1211.method_08901();
      return this.recoveredField1207.method_08908() ? (color & 0xFF000000) | (this.getPingColor(ping) & 0xFFFFFF) : color;
   }

   public int getNameColor(boolean own, boolean clientPlayer, boolean spectator) {
      if (own && this.recoveredField1215.method_08908()) return this.recoveredField1231.method_08901();
      if (clientPlayer && this.recoveredField1222.method_08908()) return this.recoveredField1229.method_08901();
      return spectator ? -1862270977 : -1;
   }

   public boolean method_20834(String var1) {
      if (CheatBreaker.getInstance().method_19771() == null) return false;
      for (PlayerNametagStyle var3 : CheatBreaker.getInstance().method_19771().method_21030().values()) {
         if (var3.method_26420().equalsIgnoreCase(var1)) {
            return true;
         }
      }

      return false;
   }
}
