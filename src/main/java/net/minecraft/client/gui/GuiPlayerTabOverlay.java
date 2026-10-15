package net.minecraft.client.gui;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.module.type.NickHiderModule;
import com.cheatbreaker.client.module.type.TabListModule;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import com.mojang.authlib.GameProfile;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldSettings;

public class GuiPlayerTabOverlay extends Gui {
   public static Ordering<NetworkPlayerInfo> field_175252_a = Ordering.from(new GuiPlayerTabOverlay.PlayerComparator());
   public Minecraft mc;
   public long lastTimeOpened;
   public boolean isBeingRendered;
   public GuiIngame guiIngame;
   public IChatComponent header;
   public IChatComponent footer;

   public void renderPlayerlist(int var1, Scoreboard var2, ScoreObjective var3) {
      CheatBreaker client = CheatBreaker.getInstance();
      TabListModule tabList = client == null || client.getModuleManager() == null ? null : client.getModuleManager().tabList;
      if (tabList != null && tabList.isEnabled()) {
         if (tabList.method_28866()) {
            tabList.method_20823(var1, var2, var3, 0, 0);
         }
         return;
      }
      NetHandlerPlayClient var4 = this.mc.thePlayer.sendQueue;
      List var5 = field_175252_a.sortedCopy(var4.getPlayerInfoMap());
      boolean heads = this.mc.isIntegratedServerRunning() || this.mc.getNetHandler().getNetworkManager().getIsencrypted();
      this.renderVanillaPlayerlist(var1, var2, var3, var5, heads, this.header, this.footer, false);
   }

   private void renderVanillaPlayerlist(int var1, Scoreboard var2, ScoreObjective var3, List<NetworkPlayerInfo> var5,
         boolean var11, IChatComponent header, IChatComponent footer, boolean preview) {
      int var6 = 0;
      int var7 = 0;

      for (NetworkPlayerInfo var9 : (Iterable<NetworkPlayerInfo>)(Iterable<?>)(var5)) {
         int var10 = this.mc.fontRendererObj.getStringWidth(this.getPlayerName(var9));
         var6 = Math.max(var6, var10);
         if (var3 != null && var3.getRenderType() != IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
            var10 = this.mc.fontRendererObj.getStringWidth(" " + var2.getValueFromObjective(var9.getGameProfile().getName(), var3).getScorePoints());
            var7 = Math.max(var7, var10);
         }
      }

      var5 = var5.subList(0, Math.min(var5.size(), 80));
      int var35 = var5.size();
      int var36 = var35;

      int var38;
      for (var38 = 1; var36 > 20; var36 = (var35 + var38 - 1) / var38) {
         var38++;
      }

      int var12;
      if (var3 != null) {
         if (var3.getRenderType() == IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
            var12 = 90;
         } else {
            var12 = var7;
         }
      } else {
         var12 = 0;
      }

      int pingWidth = preview ? this.mc.fontRendererObj.getStringWidth("250") + 4 : 13;
      int var13 = Math.min(var38 * ((var11 ? 9 : 0) + var6 + var12 + pingWidth), var1 - 50) / var38;
      int var14 = var1 / 2 - (var13 * var38 + (var38 - 1) * 5) / 2;
      int var15 = 10;
      int var16 = var13 * var38 + (var38 - 1) * 5;
      List var17 = null;
      List var18 = null;
      if (header != null) {
         var17 = this.mc.fontRendererObj.listFormattedStringToWidth(header.getFormattedText(), var1 - 50);

         for (String var20 : (Iterable<String>)(Iterable<?>)(var17)) {
            var16 = Math.max(var16, this.mc.fontRendererObj.getStringWidth(var20));
         }
      }

      if (footer != null) {
         var18 = this.mc.fontRendererObj.listFormattedStringToWidth(footer.getFormattedText(), var1 - 50);

         for (String var44 : (Iterable<String>)(Iterable<?>)(var18)) {
            var16 = Math.max(var16, this.mc.fontRendererObj.getStringWidth(var44));
         }
      }

      if (var17 != null) {
         a(var1 / 2 - var16 / 2 - 1, var15 - 1, var1 / 2 + var16 / 2 + 1, var15 + var17.size() * this.mc.fontRendererObj.FONT_HEIGHT, Integer.MIN_VALUE);

         for (String var45 : (Iterable<String>)(Iterable<?>)(var17)) {
            int var21 = this.mc.fontRendererObj.getStringWidth(var45);
            this.mc.fontRendererObj.drawStringWithShadow(var45, var1 / 2 - var21 / 2, var15, -1);
            var15 += this.mc.fontRendererObj.FONT_HEIGHT;
         }

         var15++;
      }

      a(var1 / 2 - var16 / 2 - 1, var15 - 1, var1 / 2 + var16 / 2 + 1, var15 + var36 * 9, Integer.MIN_VALUE);

      for (int var42 = 0; var42 < var35; var42++) {
         int var46 = var42 / var36;
         int var48 = var42 % var36;
         int var22 = var14 + var46 * var13 + var46 * 5;
         int var23 = var15 + var48 * 9;
         a(var22, var23, var22 + var13, var23 + 8, 553648127);
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.enableAlpha();
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
         if (var42 < var5.size()) {
            NetworkPlayerInfo var24 = (NetworkPlayerInfo)var5.get(var42);
            String var25 = this.getPlayerName(var24);
            NickHiderModule var26 = CheatBreaker.getInstance().getModuleManager().recoveredField1705;
            if (!preview && var26.isEnabled() && var26.recoveredField850.method_08908()) {
               if (!var26.recoveredField849.method_08874().equals(Minecraft.getMinecraft().getSession().getUsername())) {
                  var25 = var25.replaceAll(Minecraft.getMinecraft().getSession().getUsername(), var26.recoveredField849.method_08874());
               } else {
                  var25 = var25.replaceAll(Minecraft.getMinecraft().getSession().getUsername(), "You");
               }
            }

            GameProfile var27 = var24.getGameProfile();
            if (var11) {
               EntityPlayer var28 = this.mc.theWorld.getPlayerEntityByUUID(var27.getId());
               boolean var29 = var28 != null
                  && var28.isWearing(EnumPlayerModelParts.CAPE)
                  && (var27.getName().equals("Dinnerbone") || var27.getName().equals("Grumm"));
               this.mc.getTextureManager().bindTexture(var24.getLocationSkin());
               int var30 = 8 + (var29 ? 8 : 0);
               int var31 = 8 * (var29 ? -1 : 1);
               Gui.drawScaledCustomSizeModalRect(var22, var23, 8.0F, var30, 8, var31, 8, 8, 64.0F, 64.0F);
               if (var28 != null && var28.isWearing(EnumPlayerModelParts.HAT)) {
                  int var32 = 8 + (var29 ? 8 : 0);
                  int var33 = 8 * (var29 ? -1 : 1);
                  Gui.drawScaledCustomSizeModalRect(var22, var23, 40.0F, var32, 8, var33, 8, 8, 64.0F, 64.0F);
               }

               var22 += 9;
            }

            if (var24.getGameType() == WorldSettings.GameType.SPECTATOR) {
               var25 = EnumChatFormatting.ITALIC + var25;
               this.mc.fontRendererObj.drawStringWithShadow(var25, var22, var23, -1862270977);
            } else {
               this.mc.fontRendererObj.drawStringWithShadow(var25, var22, var23, -1);
            }

            if (var3 != null && var24.getGameType() != WorldSettings.GameType.SPECTATOR) {
               int var51 = var22 + var6 + 1;
               int var52 = var51 + var12;
               if (var52 - var51 > 5) {
                  this.drawScoreboardValues(var3, var23, var27.getName(), var51, var52, var24);
               }
            }

            if (preview) {
               String ping = Integer.toString(var24.method_13010());
               this.mc.fontRendererObj.drawStringWithShadow(ping,
                  var22 + var13 - this.mc.fontRendererObj.getStringWidth(ping) - 1, var23,
                  var24.method_13010() < 150 ? 0xFF55FF55 : 0xFFFFFF55);
            } else {
               this.drawPing(var13, var22 - (var11 ? 9 : 0), var23, var24);
            }
         }
      }

      if (var18 != null) {
         var15 = var15 + var36 * 9 + 1;
         a(var1 / 2 - var16 / 2 - 1, var15 - 1, var1 / 2 + var16 / 2 + 1, var15 + var18.size() * this.mc.fontRendererObj.FONT_HEIGHT, Integer.MIN_VALUE);

         for (String var47 : (Iterable<String>)(Iterable<?>)(var18)) {
            int var49 = this.mc.fontRendererObj.getStringWidth(var47);
            this.mc.fontRendererObj.drawStringWithShadow(var47, var1 / 2 - var49 / 2, var15, -1);
            var15 += this.mc.fontRendererObj.FONT_HEIGHT;
         }
      }
   }

   public static List<NetworkPlayerInfo> createPreviewPlayers(GameProfile ownProfile) {
      List<NetworkPlayerInfo> players = new java.util.ArrayList<>();
      String[] names = {ownProfile.getName(), "Steve", "Alex", "Notch"};
      int[] pings = {15, 75, 155, 250};
      for (int row = 0; row < names.length; row++) {
         GameProfile profile = row == 0 ? ownProfile : new GameProfile(new java.util.UUID(0, row), names[row]);
         NetworkPlayerInfo player = new NetworkPlayerInfo(profile);
         player.setDisplayName(new net.minecraft.util.ChatComponentText(names[row]));
         player.gameType = WorldSettings.GameType.SURVIVAL;
         player.setResponseTime(pings[row]);
         players.add(player);
      }
      return players;
   }

   /** Render the module-card sample through the same row renderer as vanilla Tab. */
   public void renderPlayerlistPreview(float centerX, float centerY, float maxWidth, float maxHeight) {
      List<NetworkPlayerInfo> players = createPreviewPlayers(this.mc.getSession().getProfile());
      int nameWidth = 0;
      for (NetworkPlayerInfo player : players) {
         nameWidth = Math.max(nameWidth, this.mc.fontRendererObj.getStringWidth(this.getPlayerName(player)));
      }
      int contentWidth = nameWidth + this.mc.fontRendererObj.getStringWidth("250") + 4;
      int logicalWidth = contentWidth + 50;
      float scale = Math.min(1.0F, Math.min(maxWidth / (contentWidth + 2.0F), maxHeight / 37.0F));
      GlStateManager.pushMatrix();
      try {
         GlStateManager.translate(centerX, centerY, 0);
         GlStateManager.scale(scale, scale, 1);
         GlStateManager.translate(-logicalWidth / 2.0F, -27.5F, 0);
         this.renderVanillaPlayerlist(logicalWidth, null, null, players, false, null, null, true);
      } finally {
         GlStateManager.popMatrix();
         GlStateManager.color(1, 1, 1, 1);
      }
   }

   public void drawPing(int var1, int var2, int var3, NetworkPlayerInfo var4) {
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(icons);
      byte var5 = 0;
      byte var6 = 0;
      if (var4.method_13010() < 0) {
         var6 = 5;
      } else if (var4.method_13010() < 150) {
         var6 = 0;
      } else if (var4.method_13010() < 300) {
         var6 = 1;
      } else if (var4.method_13010() < 600) {
         var6 = 2;
      } else if (var4.method_13010() < 1000) {
         var6 = 3;
      } else {
         var6 = 4;
      }

      recoveredField2942 += 100.0F;
      this.drawTexturedModalRect(var2 + var1 - 11, var3, 0 + var5 * 10, 176 + var6 * 8, 10, 8);
      recoveredField2942 -= 100.0F;
   }

   public void setFooter(IChatComponent var1) {
      this.footer = var1;
   }

   public String getPlayerName(NetworkPlayerInfo var1) {
      return var1.getDisplayName() != null
         ? var1.getDisplayName().getFormattedText()
         : ScorePlayerTeam.formatPlayerName(var1.getPlayerTeam(), var1.getGameProfile().getName());
   }

   public void setHeader(IChatComponent var1) {
      this.header = var1;
   }

   public void resetFooterHeader() {
      this.header = null;
      this.footer = null;
   }

   public GuiPlayerTabOverlay(Minecraft var1, GuiIngame var2) {
      this.mc = var1;
      this.guiIngame = var2;
   }

   public void updatePlayerList(boolean var1) {
      if (var1 && !this.isBeingRendered) {
         this.lastTimeOpened = Minecraft.getSystemTime();
      }

      this.isBeingRendered = var1;
   }

   public void drawScoreboardValues(ScoreObjective var1, int var2, String var3, int var4, int var5, NetworkPlayerInfo var6) {
      int var7 = var1.getScoreboard().getValueFromObjective(var3, var1).getScorePoints();
      if (var1.getRenderType() == IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
         this.mc.getTextureManager().bindTexture(icons);
         if (this.lastTimeOpened == var6.method_13015()) {
            if (var7 < var6.method_13014()) {
               var6.method_13001(Minecraft.getSystemTime());
               var6.method_13017(this.guiIngame.getUpdateCounter() + 20);
            } else if (var7 > var6.method_13014()) {
               var6.method_13001(Minecraft.getSystemTime());
               var6.method_13017(this.guiIngame.getUpdateCounter() + 10);
            }
         }

         if (Minecraft.getSystemTime() - var6.method_12995() > 1000L || this.lastTimeOpened != var6.method_13015()) {
            var6.method_13016(var7);
            var6.method_13000(var7);
            var6.method_13001(Minecraft.getSystemTime());
         }

         var6.method_13004(this.lastTimeOpened);
         var6.method_13016(var7);
         int var8 = MathHelper.ceiling_float_int(Math.max(var7, var6.method_13002()) / 2.0F);
         int var9 = Math.max(MathHelper.ceiling_float_int(var7 / 2), Math.max(MathHelper.ceiling_float_int(var6.method_13002() / 2), 10));
         boolean var10 = var6.method_12994() > this.guiIngame.getUpdateCounter() && (var6.method_12994() - this.guiIngame.getUpdateCounter()) / 3L % 2L == 1L;
         if (var8 > 0) {
            float var11 = Math.min((float)(var5 - var4 - 4) / var9, 9.0F);
            if (var11 > 3.0F) {
               for (int var12 = var8; var12 < var9; var12++) {
                  this.drawTexturedModalRect(var4 + var12 * var11, var2, var10 ? 25 : 16, 0, 9, 9);
               }

               for (int var16 = 0; var16 < var8; var16++) {
                  this.drawTexturedModalRect(var4 + var16 * var11, var2, var10 ? 25 : 16, 0, 9, 9);
                  if (var10) {
                     if (var16 * 2 + 1 < var6.method_13002()) {
                        this.drawTexturedModalRect(var4 + var16 * var11, var2, 70, 0, 9, 9);
                     }

                     if (var16 * 2 + 1 == var6.method_13002()) {
                        this.drawTexturedModalRect(var4 + var16 * var11, var2, 79, 0, 9, 9);
                     }
                  }

                  if (var16 * 2 + 1 < var7) {
                     this.drawTexturedModalRect(var4 + var16 * var11, var2, var16 >= 10 ? 160 : 52, 0, 9, 9);
                  }

                  if (var16 * 2 + 1 == var7) {
                     this.drawTexturedModalRect(var4 + var16 * var11, var2, var16 >= 10 ? 169 : 61, 0, 9, 9);
                  }
               }
            } else {
               float var17 = MathHelper.clamp_float(var7 / 20.0F, 0.0F, 1.0F);
               int var13 = (int)((1.0F - var17) * 255.0F) << 16 | (int)(var17 * 255.0F) << 8;
               String var14 = "" + var7 / 2.0F;
               if (var5 - this.mc.fontRendererObj.getStringWidth(var14 + "hp") >= var4) {
                  var14 = var14 + "hp";
               }

               this.mc.fontRendererObj.drawStringWithShadow(var14, (var5 + var4) / 2 - this.mc.fontRendererObj.getStringWidth(var14) / 2, var2, var13);
            }
         }
      } else {
         String var15 = EnumChatFormatting.YELLOW + "" + var7;
         this.mc.fontRendererObj.drawStringWithShadow(var15, var5 - this.mc.fontRendererObj.getStringWidth(var15), var2, 16777215);
      }
   }

   public static class PlayerComparator implements Comparator<NetworkPlayerInfo> {
      public int compare(NetworkPlayerInfo var1, NetworkPlayerInfo var2) {
         ScorePlayerTeam var3 = var1.getPlayerTeam();
         ScorePlayerTeam var4 = var2.getPlayerTeam();
         return ComparisonChain.start()
            .compareTrueFirst(var1.getGameType() != WorldSettings.GameType.SPECTATOR, var2.getGameType() != WorldSettings.GameType.SPECTATOR)
            .compare(var3 != null ? var3.getRegisteredName() : "", var4 != null ? var4.getRegisteredName() : "")
            .compare(var1.getGameProfile().getName(), var2.getGameProfile().getName())
            .result();
      }

      public PlayerComparator() {
      }
   }
}
