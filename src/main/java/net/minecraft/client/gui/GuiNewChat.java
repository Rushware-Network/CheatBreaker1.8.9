package net.minecraft.client.gui;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.module.type.ChatModule;
import com.cheatbreaker.client.module.type.NickHiderModule;
import com.cheatbreaker.client.ui.module.CBModulesGui;
import com.cheatbreaker.client.ui.util.RenderUtil;
import com.google.common.collect.Lists;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import com.cheatbreaker.client.event.type.ChatMessageEvent;
import com.cheatbreaker.client.util.chat.ClientChatLine;

public class GuiNewChat extends Gui {
   public List<ChatLine> drawnChatLines;
   public int scrollPos;
   public List<ChatLine> chatLines;
   public static Logger logger = LogManager.getLogger();
   public int recoveredField25;
   public List<ChatLine> recoveredField26;
   public boolean isScrolled;
   public float recoveredField27;
   public ResourceLocation recoveredField28 = new ResourceLocation("client/logo_white.png");
   public float recoveredField29;
   public int recoveredField30;
   public long recoveredField31;
   public List<String> sentMessages = Lists.newArrayList();
   public Minecraft mc;
   public boolean recoveredField32;

   public void printChatMessage(IChatComponent var1) {
      this.printChatMessageWithOptionalDeletion(var1, 0);
   }

   public void clearChatMessages() {
      this.drawnChatLines.clear();
      this.chatLines.clear();
      this.sentMessages.clear();
   }

   public void deleteChatLine(int var1) {
      Iterator var2 = this.drawnChatLines.iterator();

      while (var2.hasNext()) {
         ChatLine var3 = (ChatLine)var2.next();
         if (var3.getChatLineID() == var1) {
            var2.remove();
         }
      }

      var2 = this.chatLines.iterator();

      while (var2.hasNext()) {
         ChatLine var5 = (ChatLine)var2.next();
         if (var5.getChatLineID() == var1) {
            var2.remove();
            break;
         }
      }
   }

   public boolean method_03290(String var1) {
      if (CheatBreaker.getInstance().getModuleManager().recoveredField1719.isEnabled()
         && Minecraft.getMinecraft().getCurrentServerData() != null
         && Minecraft.getMinecraft().getCurrentServerData().serverIP.toLowerCase().contains("hypixel")) {
         if (!CheatBreaker.getInstance().getModuleManager().recoveredField1719.recoveredField451.method_08908()
            || !var1.startsWith("You can't tip the same person")
               && !var1.equals("Still processing your most recent request!")
               && !var1.startsWith("You've already tipped that person")
               && !var1.equals("You cannot tip yourself!")
               && !var1.startsWith("You can only use the /tip command")
               && !var1.equals("You are not allowed to use commands as a spectator!")
               && !var1.equals("Slow down! You can only use /tip every few seconds.")
               && !var1.startsWith("You've already tipped someone in the past hour in ")) {
            return CheatBreaker.getInstance().getModuleManager().recoveredField1719.recoveredField463.method_08908()
                  && Arrays.stream(var1.split(" ")).anyMatch(var0 -> var0.equalsIgnoreCase("gl"))
               ? true
               : CheatBreaker.getInstance().getModuleManager().recoveredField1719.recoveredField462.method_08908()
                  && Arrays.stream(var1.split(" ")).anyMatch(var0 -> var0.equalsIgnoreCase("gg"));
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   public float getChatScale() {
      ChatModule var1 = CheatBreaker.getInstance().getModuleManager().chatModule;
      return var1.method_28866() && var1.isEnabled() ? var1.recoveredField835.method_08905() / 100.0F : this.mc.gameSettings.chatScale;
   }

   public int method_03289(int var1) {
      if (this.recoveredField32 && this.recoveredField30 <= this.recoveredField25) {
         int var2 = var1 >> 24 & 0xFF;
         var2 = (int)(var2 * this.recoveredField29);
         return var1 & 16777215 | var2 << 24;
      } else {
         return var1;
      }
   }

   public int method_03268() {
      ChatModule var1 = CheatBreaker.getInstance().getModuleManager().chatModule;
      return var1.method_28866() && var1.isEnabled() ? var1.recoveredField838.method_08912() : calculateChatboxWidth(this.mc.gameSettings.chatWidth);
   }

   public int getChatHeight() {
      ChatModule var1 = CheatBreaker.getInstance().getModuleManager().chatModule;
      return var1.method_28866() && var1.isEnabled()
         ? (this.getChatOpen() ? var1.recoveredField818.method_08912() : var1.recoveredField833.method_08912())
         : calculateChatboxHeight(this.getChatOpen() ? this.mc.gameSettings.chatHeightFocused : this.mc.gameSettings.chatHeightUnfocused);
   }

   public int getLineCount() {
      return this.getChatHeight() / 9;
   }

   public int method_03269(int var1) {
      ChatModule var2 = CheatBreaker.getInstance().getModuleManager().chatModule;
      if (this.recoveredField32 && this.recoveredField30 <= this.recoveredField25) {
         int var4 = var1 >> 24 & 0xFF;
         var4 = (int)(var4 * this.recoveredField29 * var2.recoveredField817.method_08905() / 100.0F);
         return var1 & 16777215 | var4 << 24;
      } else {
         int var3 = (int)((var1 >> 24 & 0xFF) * var2.recoveredField817.method_08905() / 100.0F);
         return new Color(var1 >> 16 & 0xFF, var1 >> 8 & 0xFF, var1 & 0xFF, var3).getRGB();
      }
   }

   public void setChatLine(IChatComponent var1, int var2, int var3, boolean var4) {
      if (var2 != 0 && !var4) {
         this.deleteChatLine(var2);
      }

      ChatModule chatModule = CheatBreaker.getInstance().getModuleManager().chatModule;
      boolean unlimited = chatModule.isEnabled() && chatModule.recoveredField828.method_08908();
      boolean clientMessage = var1 instanceof ChatComponentText && ((ChatComponentText)var1).method_07468();
      int var5 = MathHelper.floor_float(this.method_03268() / this.getChatScale());
      List<IChatComponent> var6 = GuiUtilRenderComponents.splitText(var1, var5, this.mc.fontRendererObj, false, false);
      boolean var7 = this.getChatOpen();

      for (IChatComponent var9 : var6) {
         if (var7 && this.scrollPos > 0) {
            this.isScrolled = true;
            this.scroll(1);
         }

         this.drawnChatLines.add(0, createChatLine(var3, var9, var2, clientMessage));
      }

      if (!unlimited) {
         while (this.drawnChatLines.size() > 100) {
            this.drawnChatLines.remove(this.drawnChatLines.size() - 1);
         }
      }

      if (!var4) {
         this.chatLines.add(0, createChatLine(var3, var1, var2, clientMessage));
         if (!unlimited) {
            while (this.chatLines.size() > 100) {
               this.chatLines.remove(this.chatLines.size() - 1);
            }
         }
      }

      this.method_03279(var6);
   }

   private static ChatLine createChatLine(int updateCounter, IChatComponent component, int id, boolean clientMessage) {
      if (clientMessage) {
         ClientChatLine line = new ClientChatLine(updateCounter, component, id);
         line.method_28279(true);
         return line;
      }
      return new ChatLine(updateCounter, component, id);
   }

   public void printChatMessageWithOptionalDeletion(IChatComponent var1, int var2) {
      if (!this.method_03290(var1.getUnformattedText())) {
         CheatBreaker.getInstance().method_19817().method_21935(new ChatMessageEvent(var1.getUnformattedText()));
         this.recoveredField27 = 0.0F;
         this.setChatLine(var1, var2, this.mc.ingameGUI.getUpdateCounter(), false);
         logger.info("[CHAT] " + var1.getUnformattedText());
      }
   }

   public List<String> getSentMessages() {
      return this.sentMessages;
   }

   public void method_03291(IChatComponent var1) {
      this.recoveredField27 = 0.0F;
      this.setChatLine(var1, 0, this.mc.ingameGUI.getUpdateCounter(), false);
   }

   public void drawChat(int var1) {
      if (this.mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN) {
         ScaledResolution var2 = new ScaledResolution(this.mc);
         int var3 = var2.getScaledWidth();
         int var4 = var2.getScaledHeight();
         GuiScreen var5 = this.mc.currentScreen;
         if (var5 instanceof CBModulesGui
               && ((CBModulesGui)var5)
                  .recoveredField788
                  .isMouseInside(Mouse.getX() * var3 / this.mc.displayWidth, var4 - Mouse.getY() * var4 / this.mc.displayHeight - 1)
            || CheatBreaker.getInstance().getModuleManager().chatModule.recoveredField3905 && !(var5 instanceof GuiChat)) {
            return;
         }

         int var6 = this.getLineCount();
         boolean var7 = false;
         int var8 = 0;
         int var9 = this.drawnChatLines.size();
         float var10 = this.mc.gameSettings.chatOpacity * 0.9F + 0.1F;
         if (var9 > 0) {
            if (this.getChatOpen()) {
               var7 = true;
            }

            float var11 = this.getChatScale();
            int var12 = MathHelper.ceiling_float_int(this.method_03268() / var11);
            GlStateManager.pushMatrix();
            GlStateManager.translate(2.0F, 20.0F, 0.0F);
            GlStateManager.scale(var11, var11, 1.0F);

            for (int var13 = 0; var13 + this.scrollPos < this.drawnChatLines.size() && var13 < var6; var13++) {
               ChatLine var14 = this.drawnChatLines.get(var13 + this.scrollPos);
               if (var14 != null) {
                  int var15 = var1 - var14.getUpdatedCounter();
                  if (var15 < 200 || var7) {
                     double var16 = var15 / 200.0;
                     var16 = 1.0 - var16;
                     var16 *= 10.0;
                     var16 = MathHelper.clamp_double(var16, 0.0, 1.0);
                     var16 *= var16;
                     int var18 = (int)(255.0 * var16);
                     if (var7) {
                        var18 = 255;
                     }

                     var18 = (int)(var18 * var10);
                     var8++;
                     if (var18 > 3) {
                        byte var19 = 0;
                        int var20 = -var13 * 9;
                        a(var19, var20 - 9, var19 + var12 + 4, var20, var18 / 2 << 24);
                        String var21 = var14.getChatComponent().getFormattedText();
                        GlStateManager.enableBlend();
                        NickHiderModule var22 = CheatBreaker.getInstance().getModuleManager().recoveredField1705;
                        if (var22.isEnabled() && var22.recoveredField850.method_08908()) {
                           var21 = var21.replaceAll(Minecraft.getMinecraft().getSession().getUsername(), var22.recoveredField849.method_08874());
                        }

                        this.mc.fontRendererObj.drawStringWithShadow(var21, var19, var20 - 8, 16777215 + (var18 << 24));
                        GlStateManager.disableAlpha();
                        GlStateManager.disableBlend();
                     }
                  }
               }
            }

            if (var7) {
               int var23 = this.mc.fontRendererObj.FONT_HEIGHT;
               GlStateManager.translate(-3.0F, 0.0F, 0.0F);
               int var24 = var9 * var23 + var9;
               int var25 = var8 * var23 + var8;
               int var30 = this.scrollPos * var25 / var9;
               int var17 = var25 * var25 / var24;
               if (var24 != var25) {
                  int var32 = var30 > 0 ? 170 : 96;
                  int var33 = this.isScrolled ? 13382451 : 3355562;
                  a(0, -var30, 2, -var30 - var17, var33 + (var32 << 24));
                  a(2, -var30, 1, -var30 - var17, 13421772 + (var32 << 24));
               }
            }

            GlStateManager.popMatrix();
         }
      }
   }

   public static float method_03273(float var0, float var1, float var2) {
      return var0 < var1 ? var1 : Math.min(var0, var2);
   }

   public void scroll(int var1) {
      this.scrollPos += var1;
      int var2 = this.drawnChatLines.size();
      if (this.scrollPos > var2 - this.getLineCount()) {
         this.scrollPos = var2 - this.getLineCount();
      }

      if (this.scrollPos <= 0) {
         this.scrollPos = 0;
         this.isScrolled = false;
      }
   }

   public boolean getChatOpen() {
      return this.mc.currentScreen instanceof GuiChat;
   }

   public IChatComponent getChatComponent(int var1, int var2) {
      if (!this.getChatOpen()) {
         return null;
      } else {
         ScaledResolution var3 = new ScaledResolution(this.mc);
         int var4 = var3.getScaleFactor();
         float var5 = this.getChatScale();
         int var6 = var1 / var4 - 2;
         int var7 = var2 / var4 - 28;
         ChatModule var8 = CheatBreaker.getInstance().getModuleManager().chatModule;
         if (var8.method_28866() && var8.isEnabled()) {
            float[] var9 = var8.getScaledPoints(var3, true);
            var6 = (int)(var1 / var4 / var8.method_28770() - var9[0]);
            var7 = (int)(var2 / var4 / var8.method_28770() - this.mc.displayHeight / var4 / var8.method_28770() + var8.recoveredField3894 + var9[1]);
         } else if (!var8.method_28866() && var8.isEnabled()) {
            var7 = (int)(var7 - var8.recoveredField831.method_08905());
         }

         var6 = MathHelper.floor_float(var6 / var5);
         var7 = MathHelper.floor_float(var7 / var5);
         if (var6 >= 0 && var7 >= 0) {
            int var17 = Math.min(this.getLineCount(), this.drawnChatLines.size());
            if (var6 <= MathHelper.floor_float(this.method_03268() / this.getChatScale()) && var7 < this.mc.fontRendererObj.FONT_HEIGHT * var17 + var17) {
               int var10 = var7 / this.mc.fontRendererObj.FONT_HEIGHT + this.scrollPos;
               if (var10 >= 0 && var10 < this.drawnChatLines.size()) {
                  ChatLine var11 = this.drawnChatLines.get(var10);
                  int var12 = 0;

                  for (IChatComponent var14 : var11.getChatComponent()) {
                     if (var14 instanceof ChatComponentText) {
                        var12 += this.mc
                           .fontRendererObj
                           .getStringWidth(GuiUtilRenderComponents.func_178909_a(((ChatComponentText)var14).method_07470(), false));
                        if (var12 > var6) {
                           return var14;
                        }
                     }
                  }
               }

               return null;
            } else {
               return null;
            }
         } else {
            return null;
         }
      }
   }

   public void method_03277(long var1) {
      if (this.recoveredField27 < 1.0F) {
         this.recoveredField27 = this.recoveredField27
            + 0.001F * CheatBreaker.getInstance().getModuleManager().chatModule.recoveredField837.method_08905() / 2.0F * (float)var1;
      }

      this.recoveredField27 = method_03273(this.recoveredField27, 0.0F, 1.0F);
   }

   public GuiNewChat(Minecraft var1) {
      this.chatLines = Lists.newArrayList();
      this.drawnChatLines = Lists.newArrayList();
      this.recoveredField26 = new ArrayList<>();
      this.recoveredField32 = false;
      this.recoveredField31 = System.currentTimeMillis();
      this.mc = var1;
   }

   public void resetScroll() {
      this.scrollPos = 0;
      this.isScrolled = false;
   }

   public void method_03276(int var1, boolean var2) {
      this.recoveredField32 = var2;
      ChatModule var3 = CheatBreaker.getInstance().getModuleManager().chatModule;
      NickHiderModule var4 = CheatBreaker.getInstance().getModuleManager().recoveredField1705;
      if (this.mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN) {
         ScaledResolution var5 = new ScaledResolution(this.mc);
         int var6 = var5.getScaledWidth();
         int var7 = var5.getScaledHeight();
         GuiScreen var8 = this.mc.currentScreen;
         boolean var9 = !CheatBreaker.getInstance().getModuleManager().chatModule.method_28866() || !(var8 instanceof CBModulesGui);
         boolean var10 = CheatBreaker.getInstance().getModuleManager().chatModule.recoveredField3905 && !(var8 instanceof GuiChat) && var9;
         if (var8 instanceof CBModulesGui
               && ((CBModulesGui)var8)
                  .recoveredField788
                  .isMouseInside(Mouse.getX() * var6 / this.mc.displayWidth, var7 - Mouse.getY() * var7 / this.mc.displayHeight - 1)
            || var10) {
            return;
         }

         int var11 = this.getLineCount();
         boolean var12 = false;
         int var13 = 0;
         int var14 = this.drawnChatLines.size();
         float var15 = this.mc.gameSettings.chatOpacity * var3.recoveredField815.method_08905() / 100.0F * 0.9F + 0.1F;
         long var16 = System.currentTimeMillis();
         long var18 = var16 - this.recoveredField31;
         this.recoveredField31 = var16;
         this.method_03277(var18);
         float var20 = this.recoveredField27;
         this.recoveredField29 = method_03273(1.0F - --var20 * var20 * var20 * var20, 0.0F, 1.0F);
         if (var14 > 0) {
            if (this.getChatOpen()) {
               var12 = true;
            }

            float var21 = this.getChatScale();
            int var22 = MathHelper.ceiling_float_int(this.method_03268() / var21);
            GL11.glPushMatrix();
            float var23 = var3.method_28866() ? var3.recoveredField3894 : 20.0F - var3.recoveredField831.method_08905();
            if (this.recoveredField32 && !this.isScrolled) {
               var23 += (9.0F - 9.0F * this.recoveredField29) * this.getChatScale();
            }

            GL11.glTranslatef(var3.method_28866() ? 0.0F : 2.0F, var23, 0.0F);
            GL11.glScalef(var21, var21, 1.0F);

            for (int var24 = 0; var24 + this.scrollPos < this.drawnChatLines.size() && var24 < var11; var24++) {
               ChatLine var27 = this.drawnChatLines.get(this.method_03266(var24 + this.scrollPos));
               if (var27 != null) {
                  int var25 = var1 - var27.getUpdatedCounter();
                  if (var25 < 200 || var12) {
                     double var28 = var25 / 200.0;
                     var28 = 1.0 - var28;
                     var28 *= 10.0;
                     if (var28 < 0.0) {
                        var28 = 0.0;
                     }

                     if (var28 > 1.0) {
                        var28 = 1.0;
                     }

                     var28 *= var28;
                     int var26 = (int)(255.0 * var28);
                     if (var12) {
                        var26 = 255;
                     }

                     var26 = (int)(var26 * var15);
                     var13++;
                     if (var26 > 3) {
                        byte var30 = 0;
                        int var31 = -var24 * 9;
                        String var32 = var27.getChatComponent().getFormattedText();
                        if (this.mc.currentScreen instanceof GuiChat && (Boolean)var3.recoveredField820.getValue().equals("While Typing")
                           || (Boolean)var3.recoveredField820.getValue().equals("ON")) {
                           int var33 = (int)((var3.recoveredField834.method_08901() >> 24 & 0xFF) * (var26 / 255.0F));
                           int var34 = var3.recoveredField834.method_08901() >> 16 & 0xFF;
                           int var35 = var3.recoveredField834.method_08901() >> 8 & 0xFF;
                           int var36 = var3.recoveredField834.method_08901() & 0xFF;
                           int var37 = var3.recoveredField821.getValue().equals("Full") ? var22 + 4 : this.mc.fontRendererObj.getStringWidth(var32);
                           a(var30, var31 - 9, var30 + var37, var31, this.method_03289(new Color(var34, var35, var36, var33).getRGB()));
                        }

                        if (var4.isEnabled() && var4.recoveredField850.method_08908()) {
                           if (!var4.recoveredField849.method_08874().equals(Minecraft.getMinecraft().getSession().getUsername())) {
                              var32 = var32.replaceAll(Minecraft.getMinecraft().getSession().getUsername(), var4.recoveredField849.method_08874());
                           } else {
                              var32 = var32.replaceAll(Minecraft.getMinecraft().getSession().getUsername(), "You");
                           }
                        }

                        boolean var53 = var3.recoveredField819.method_08908();
                        boolean var54 = CheatBreaker.getInstance().getModuleManager().recoveredField1705.isEnabled()
                           && CheatBreaker.getInstance().getModuleManager().recoveredField1705.recoveredField850.method_08908();
                        java.lang.String var55 = CheatBreaker.getInstance().getModuleManager().recoveredField1705.recoveredField849.method_08874();
                        if ((var32.contains(Minecraft.getMinecraft().getSession().getUsername()) || var32.contains((CharSequence)var55))
                           && !var3.recoveredField829.method_08874().equalsIgnoreCase("None")) {
                           var32 = var32.replaceAll(
                              (String)(var54 ? var55 : Minecraft.getMinecraft().getSession().getUsername()),
                              var3.method_04704() + (var54 ? var55 : Minecraft.getMinecraft().getSession().getUsername()) + EnumChatFormatting.RESET
                           );
                        }

                        for (int var56 = 0; var56 + this.scrollPos < this.drawnChatLines.size() && var56 < this.getLineCount(); var56++) {
                           if ((var12 || 10.0 - (var1 - var27.getUpdatedCounter()) / 20.0 > 1.0) && var27 instanceof ClientChatLine) {
                              ClientChatLine var57 = (ClientChatLine)var27;
                              if (var57.method_28277()) {
                                 this.recoveredField26.add(var27);
                              }
                           }
                        }

                        GlStateManager.enableBlend();
                        this.mc
                           .fontRendererObj
                           .drawString(
                              var53 ? var3.method_04695(var32) : var32,
                              var30 + (this.recoveredField26.contains(var27) ? 16.0F : 0.0F),
                              var31 - 8,
                              this.method_03269(var53 ? var3.recoveredField816.method_08901() : 16777215 + (var26 << 24)),
                              var3.recoveredField839.method_08908()
                           );
                        GlStateManager.disableAlpha();
                        GlStateManager.disableBlend();
                     }
                  }
               }
            }

            if (var12) {
               int var39 = this.mc.fontRendererObj.FONT_HEIGHT;
               GL11.glTranslatef(-3.0F, 0.0F, 0.0F);
               int var43 = var14 * var39 + var14;
               int var40 = var13 * var39 + var13;
               int var48 = this.scrollPos * var40 / var14;
               int var29 = var40 * var40 / var43;
               if (var43 != var40) {
                  int var42 = var48 > 0 ? 170 : 96;
                  int var51 = this.isScrolled ? 13382451 : 3355562;
                  a(0, -var48, 2, -var48 - var29, var51 + (var42 << 24));
                  a(2, -var48, 1, -var48 - var29, 13421772 + (var42 << 24));
               }
            }

            int var44 = 0;

            for (int var49 = 0; var49 + this.scrollPos < this.drawnChatLines.size() && var49 < this.getLineCount(); var49++) {
               ChatLine var50 = this.drawnChatLines.get(var49 + this.scrollPos);
               if (var12 || 10.0 - (var1 - var50.getUpdatedCounter()) / 20.0 > 1.0) {
                  var44 -= Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT;
                  if (var50 instanceof ClientChatLine) {
                     ClientChatLine var52 = (ClientChatLine)var50;
                     if (var52.method_28277()) {
                        if (var3.recoveredField839.method_08908()) {
                           GL11.glColor4f(0.0F, 0.0F, 0.0F, 1.0F);
                           RenderUtil.method_22064(this.recoveredField28, this.getChatOpen() ? 4.105F : 1.5F, var44 + 1.5F, 14.0F, 6.9F);
                        }

                        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                        RenderUtil.method_22064(this.recoveredField28, this.getChatOpen() ? 4.105F : 1.3F, var44 + 0.9F, 14.0F, 6.9F);
                     }
                  }
               }
            }

            GL11.glPopMatrix();
         }
      }
   }

   public void addToSentMessages(String var1) {
      if (this.sentMessages.isEmpty() || !this.sentMessages.get(this.sentMessages.size() - 1).equals(var1)) {
         this.sentMessages.add(var1);
      }
   }

   public int method_03266(int var1) {
      this.recoveredField30 = var1;
      return var1;
   }

   public List<IChatComponent> method_03279(List<IChatComponent> var1) {
      this.recoveredField25 = var1.size() - 1;
      return var1;
   }

   public void refreshChat() {
      this.drawnChatLines.clear();
      this.resetScroll();

      for (int var1 = this.chatLines.size() - 1; var1 >= 0; var1--) {
         ChatLine var2 = this.chatLines.get(var1);
         this.setChatLine(var2.getChatComponent(), var2.getChatLineID(), var2.getUpdatedCounter(), true);
      }
   }

   public static int calculateChatboxWidth(float var0) {
      short var1 = 320;
      byte var2 = 40;
      return MathHelper.floor_float(var0 * (var1 - var2) + var2);
   }

   public static int calculateChatboxHeight(float var0) {
      short var1 = 180;
      byte var2 = 20;
      return MathHelper.floor_float(var0 * (var1 - var2) + var2);
   }
}
