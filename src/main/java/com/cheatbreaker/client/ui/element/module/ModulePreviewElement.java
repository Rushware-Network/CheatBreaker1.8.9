package com.cheatbreaker.client.ui.element.module;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.config.GlobalSettings;
import com.cheatbreaker.client.config.Setting;
import com.cheatbreaker.client.module.AbstractModule;
import com.cheatbreaker.client.module.type.cooldowns.CooldownRenderer;
import com.cheatbreaker.client.ui.element.AbstractModulesGuiElement;
import com.cheatbreaker.client.ui.element.AbstractScrollableElement;
import com.cheatbreaker.client.ui.module.CBModulesGui;
import com.cheatbreaker.client.ui.util.RenderUtil;
import com.cheatbreaker.client.ui.util.font.CBFontRenderer;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import com.cheatbreaker.client.ui.module.ModulePlacementGui;
import com.cheatbreaker.client.ui.util.GuiThemeColors;

public class ModulePreviewElement extends AbstractModulesGuiElement {
   public AbstractScrollableElement recoveredField3795;
   public ModulesGuiButtonElement recoveredField3796;
   public boolean recoveredField3797 = CheatBreaker.getInstance().getGlobalSettings().recoveredField504.getValue().equals("Compact");
   public static ModulePreviewElement recoveredField3798;
   public ModulesGuiButtonElement recoveredField3799;
   public AbstractModule recoveredField3800;
   public ModulesGuiButtonElement recoveredField3801;

   @Override
   public void handleDrawElement(int var1, int var2, float var3) {
      if (this.recoveredField3800.isEnabled()) {
         int var6 = GlobalSettings.recoveredField529.method_08908() ? GuiThemeColors.recoveredField1666 : GuiThemeColors.recoveredField1660;
         if (this.recoveredField3800.method_28796()) {
            var6 = -5859328;
         }

         RenderUtil.method_22054(this.x, this.y, this.x + this.width, this.recoveredField3797 ? this.y + 16 : this.y + this.height, 3.0, var6);
      } else {
         RenderUtil.method_22054(
            this.x,
            this.y,
            this.x + this.width,
            this.recoveredField3797 ? this.y + 16 : this.y + this.height,
            3.0,
            GlobalSettings.recoveredField529.method_08908() ? GuiThemeColors.recoveredField1646 : GuiThemeColors.recoveredField1680
         );
      }

      CBFontRenderer var20 = CheatBreaker.getInstance().recoveredField1595;
      GL11.glPushMatrix();
      byte var7 = 0;
      byte var8 = 0;
      if (!this.recoveredField3797) {
         if (this.recoveredField3800 == CheatBreaker.getInstance().getModuleManager().armourStatus) {
            var7 = -10;
            String var5 = "329/329";
            float var4 = Minecraft.getMinecraft().fontRendererObj.getStringWidth(var5);
            float var10002 = (int)(this.x + 1 + this.width / 2 - var4 / 2.0F);
            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(var5, var10002, this.y + this.height / 2 - 18, -1);
         } else if (this.recoveredField3800 == CheatBreaker.getInstance().getModuleManager().potionStatus) {
            var8 = -30;
            float var35 = this.x + 8 + this.width / 2 - 20;
            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("Speed II", var35, this.y + this.height / 2 - 36, -1);
            var35 = this.x + 8 + this.width / 2 - 20;
            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("0:42", var35, this.y + this.height / 2 - 26, -1);
         } else if (this.recoveredField3800 == CheatBreaker.getInstance().getModuleManager().scoreboard) {
            Gui.a(this.x + 20, this.y + this.height / 2 - 44, this.x + this.width - 20, this.y + this.height / 2 - 6, 1862270976);
            int var37 = this.x + this.width / 2;
            Minecraft.getMinecraft().fontRendererObj.method_08776("Score", var37, this.y + this.height / 2 - 40, -1);
            float var38 = this.x + 24;
            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("Steve", var38, this.y + this.height / 2 - 28, -1);
            float var39 = this.x + 24;
            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("Alex", var39, this.y + this.height / 2 - 18, -1);
            Minecraft.getMinecraft().fontRendererObj.method_08776(EnumChatFormatting.RED + "0", this.x + this.width - 26, this.y + this.height / 2 - 18, -1);
            Minecraft.getMinecraft().fontRendererObj.method_08776(EnumChatFormatting.RED + "1", this.x + this.width - 26, this.y + this.height / 2 - 28, -1);
         } else if (this.recoveredField3800 == CheatBreaker.getInstance().getModuleManager().recoveredField1708) {
            var7 = 3;
            String var18 = "Wither";
            float var14 = Minecraft.getMinecraft().fontRendererObj.getStringWidth(var18);
            float var40 = (int)(this.x + 1 + this.width / 2 - var14 / 2.0F);
            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(var18, var40, this.y + this.height / 2 - 36, -1);
         } else if (this.recoveredField3800 == CheatBreaker.getInstance().getModuleManager().chatModule) {
            Gui.a(this.x + 10, this.y + this.height / 2 - 35, this.x + this.width - 10, this.y + this.height / 2 - 17, 1862270976);
            Minecraft.getMinecraft()
               .fontRendererObj
               .drawStringWithShadow(EnumChatFormatting.GREEN + "Steve" + EnumChatFormatting.WHITE + ": Hey.", this.x + 10, this.y + this.height / 2 - 34, -1);
            Minecraft.getMinecraft()
               .fontRendererObj
               .drawStringWithShadow(EnumChatFormatting.GREEN + "Alex" + EnumChatFormatting.WHITE + ": Hi!", this.x + 10, this.y + this.height / 2 - 25, -1);
         } else if (this.recoveredField3800 == CheatBreaker.getInstance().getModuleManager().recoveredField1713) {
            int var9 = Minecraft.getMinecraft().fontRendererObj.getStringWidth(Minecraft.getMinecraft().getSession().getUsername());
            String var10 = var9 > 94 ? "Steve" : Minecraft.getMinecraft().getSession().getUsername();
            var9 = Minecraft.getMinecraft().fontRendererObj.getStringWidth(var10);
            Gui.drawRect(
               this.x + this.width / 2 - var9 / 2 - 8,
               this.y + this.height / 2 - 33,
               this.x + this.width / 2 + var9 / 2 + 8,
               this.y + this.height / 2 - 24,
               1862270976
            );
            Minecraft.getMinecraft()
               .fontRendererObj
               .drawString(
                  var10,
                  this.x + this.width / 2 - Minecraft.getMinecraft().fontRendererObj.getStringWidth(var10) / 2 + 7,
                  this.y + this.height / 2 - 32,
                  -1,
                  false
               );
            RenderUtil.method_22064(
               new ResourceLocation("client/logo_white.png"), this.x + this.width / 2 - var9 / 2 - 7, this.y + this.height / 2 - 32, 13.0F, 7.0F
            );
         } else if (this.recoveredField3800 == CheatBreaker.getInstance().getModuleManager().recoveredField1721) {
            GL11.glScalef(2.0F, 2.0F, 0.0F);
            float var22 = 6.0F;
            float var29 = 4.0F;
            float var11 = 2.0F;
            float var12 = (this.x + this.width / 2) / 2.0F;
            float var13 = (this.y + this.height / 2 - 27) / 2.0F;
            Gui.drawBoxWithOutLine(var12 - var29 - var22, var13 - var11 / 2.0F, var12 - var29, var13 + var11 / 2.0F, 0.5F, -1358954496, -1);
            Gui.drawBoxWithOutLine(var12 + var29, var13 - var11 / 2.0F, var12 + var29 + var22, var13 + var11 / 2.0F, 0.5F, -1358954496, -1);
            Gui.drawBoxWithOutLine(var12 - var11 / 2.0F, var13 - var29 - var22, var12 + var11 / 2.0F, var13 - var29, 0.5F, -1358954496, -1);
            Gui.drawBoxWithOutLine(var12 - var11 / 2.0F, var13 + var29, var12 + var11 / 2.0F, var13 + var29 + var22, 0.5F, -1358954496, -1);
            GL11.glScalef(1.0F, 1.0F, 0.0F);
         } else if (this.recoveredField3800 == CheatBreaker.getInstance().getModuleManager().recoveredField1705) {
            float var23 = this.recoveredField3800.getPreviewIconWidth();
            float var15 = this.recoveredField3800.getPreviewIconHeight();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderUtil.method_22064(
               CheatBreaker.getInstance().method_19810(Minecraft.getMinecraft().getSession().getUsername()),
               this.x + this.width / 2 - var23 / 2.0F + var8,
               this.y + var7 + this.height / 2 - 26 - var15 / 2.0F,
               var23,
               var15
            );
         }

         if (this.recoveredField3800 == CheatBreaker.getInstance().getModuleManager().tabList) {
            this.drawTabListPreview();
         } else if (this.recoveredField3800 == CheatBreaker.getInstance().getModuleManager().cooldowns) {
            CooldownRenderer var24 = new CooldownRenderer("EnderPearl", 368, 9000L);
            float var41 = this.x + this.width / 2 - 18;
            var24.method_06971(CheatBreaker.getInstance().getModuleManager().cooldowns.recoveredField652, var41, this.y + this.height / 2 - 26 - 18, -1);
         } else if ((this.recoveredField3800.getPreviewType() == null || this.recoveredField3800.getPreviewType() == AbstractModule.PreviewType.LABEL)
            && this.recoveredField3800 != CheatBreaker.getInstance().getModuleManager().scoreboard
            && this.recoveredField3800 != CheatBreaker.getInstance().getModuleManager().chatModule
            && this.recoveredField3800 != CheatBreaker.getInstance().getModuleManager().recoveredField1713) {
            String var19 = "";
            float var17;
            if (this.recoveredField3800.getPreviewType() == null) {
               var17 = 2.0F;

               for (String var33 : this.recoveredField3800.getName().split(" ")) {
                  String var34 = var33.substring(0, 1);
                  var19 = var19 + (Objects.equals(var19, "") ? var34 : var34.toLowerCase());
               }
            } else {
               var17 = this.recoveredField3800.getPreviewLabelSize();
               var19 = this.recoveredField3800.getPreviewLabel();
            }

            GL11.glScalef(var17, var17, var17);
            String[] previewLines = var19.split("\n");
            float lineHeight = Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 1.0F;
            float firstLineY = (this.y + this.height / 2 - 32) / var17 - (previewLines.length - 1) * lineHeight / 2.0F;
            for (int line = 0; line < previewLines.length; line++) {
               float lineWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(previewLines[line]) * var17;
               int lineX = (int)((this.x + 1 + this.width / 2 - lineWidth / 2.0F) / var17);
               int lineY = (int)(firstLineY + line * lineHeight);
               if (this.recoveredField3800.getPreviewType() == null && !this.recoveredField3797) {
                  Minecraft.getMinecraft().fontRendererObj.drawString(previewLines[line], lineX, lineY, -13750738);
               } else {
                  Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(previewLines[line], lineX, lineY, -1);
               }
            }
         } else if (this.recoveredField3800.getPreviewType() == AbstractModule.PreviewType.ICON) {
            float var25 = this.recoveredField3800.getPreviewIconWidth();
            float var16 = this.recoveredField3800.getPreviewIconHeight();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            if (this.recoveredField3800 == CheatBreaker.getInstance().getModuleManager().recoveredField1718) {
               // Vanilla server-list full signal: the first 10x8 sprite at (0, 176).
               Minecraft.getMinecraft().getTextureManager().bindTexture(Gui.icons);
               Gui.drawScaledCustomSizeModalRect(
                  (int)(this.x + this.width / 2 - var25 / 2.0F + var8),
                  (int)(this.y + var7 + this.height / 2 - 26 - var16 / 2.0F),
                  0.0F, 176.0F, 10, 8, (int)var25, (int)var16, 256.0F, 256.0F
               );
            } else {
               RenderUtil.method_22064(
                  this.recoveredField3800.getPreviewIcon(),
                  this.x + this.width / 2 - var25 / 2.0F + var8,
                  this.y + var7 + this.height / 2 - 26 - var16 / 2.0F,
                  var25,
                  var16
               );
            }
         }
      }

      GL11.glPopMatrix();
      String var28 = this.recoveredField3800.getName();
      byte var31 = 14;
      if (this.recoveredField3797 && var28.replaceAll(" ", "").length() >= var31) {
         var28 = var28.substring(0, var31).trim() + "..";
      }

      if (this.recoveredField3797) {
         var20.drawString(var28, this.x + 4.0F, this.y + 4, 1593835520);
         var20.drawString(var28, this.x + 3.0F, this.y + 3, -1);
      } else {
         var20.drawCenteredString(var28, this.x + this.width / 2 - 0.5F, this.y + this.height / 2 - 4 + 1, 1593835520);
         var20.drawCenteredString(var28, this.x + this.width / 2 - 1.5F, this.y + this.height / 2 - 4, -1);
      }

      if (!this.recoveredField3797) {
         this.recoveredField3801.displayString = this.recoveredField3800.isEnabled() ? "Disable" : "Enable";
         this.recoveredField3801.yOffset = this.yOffset;
         this.recoveredField3801.method_07930(false);
         if (!this.recoveredField3800.recoveredField3912) {
            this.recoveredField3801
               .setDimensions(
                  this.x + this.width / 2 + 8, this.y + this.height - 38, this.width / 2 - 12, this.y + this.height - 24 - (this.y + this.height - 38)
               );
            this.recoveredField3801.handleDrawElement(var1, var2, var3);
         }
      }

      this.recoveredField3796.displayString = this.recoveredField3797
         ? (this.recoveredField3800.isEnabled() ? "delete-64.png" : "plus-64.png")
         : (
            this.recoveredField3800.getGuiAnchor() == null
               ? (this.recoveredField3800.method_28866() && this.recoveredField3800.isEnabled() ? "Disable" : "Enable")
               : (
                  this.recoveredField3800.method_28866() && this.recoveredField3800.isEnabled()
                     ? "Hide from HUD"
                     : (this.recoveredField3800.method_28790() && !Keyboard.isKeyDown(42) ? "Return to HUD" : "Add to HUD")
               )
         );
      this.recoveredField3796.recoveredField2847 = this.recoveredField3800.method_28866() && this.recoveredField3800.isEnabled() ? -5756117 : -13916106;
      this.recoveredField3796
         .setDimensions(
            this.x + (this.recoveredField3797 ? 87 : 4),
            this.y + (this.recoveredField3797 ? 3 : this.height - 38),
            this.recoveredField3797 ? 10 : (this.recoveredField3800.recoveredField3912 ? this.width - 8 : this.width / 2 + 2),
            this.recoveredField3797 ? 10 : this.y + this.height - 24 - (this.y + this.height - 38)
         );
      this.recoveredField3796.yOffset = this.yOffset;
      this.recoveredField3796.method_07930(this.recoveredField3797);
      this.recoveredField3796.handleDrawElement(var1, var2, var3);
      this.recoveredField3799
         .setDimensions(
            this.x + (this.recoveredField3797 ? 102 : 4),
            this.y + (this.recoveredField3797 ? 3 : this.height - 20),
            this.recoveredField3797 ? 10 : this.width - 8,
            this.recoveredField3797 ? 10 : 16
         );
      this.recoveredField3799.method_07930(this.recoveredField3797);
      this.recoveredField3799.yOffset = this.yOffset;
      this.recoveredField3799.handleDrawElement(var1, var2, var3);
   }

   @Override
   public void handleMouseClick(int var1, int var2, int var3) {
      if (this.recoveredField3799.isMouseInside(var1, var2)) {
         Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
         ((ModuleListElement)CBModulesGui.instance.recoveredField793).recoveredField1358 = false;
         ((ModuleListElement)CBModulesGui.instance.recoveredField793).scrollable = this.recoveredField3795;
         ((ModuleListElement)CBModulesGui.instance.recoveredField793).module = this.recoveredField3800;
         CBModulesGui.instance.currentScrollableElement = CBModulesGui.instance.recoveredField793;
      } else if (!this.recoveredField3800.recoveredField3912 && this.recoveredField3801.isMouseInside(var1, var2)) {
         Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
         this.recoveredField3800.method_28786(true);
         this.recoveredField3800.setState(!this.recoveredField3800.isEnabled());
         this.recoveredField3801.displayString = this.recoveredField3800.isEnabled() ? "Disable" : "Enable";
         this.recoveredField3801.recoveredField2847 = this.recoveredField3800.isEnabled() ? -5756117 : -13916106;
         if (this.recoveredField3800.isEnabled()) {
            this.method_27154();
            this.recoveredField3800.setState(true);
         }
      } else if (this.recoveredField3796.recoveredField2853 && this.recoveredField3796.isMouseInside(var1, var2)) {
         Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
         if (!this.recoveredField3800.isEnabled()) {
            this.recoveredField3800.method_28847(true);
            this.method_27154();
            if (this.recoveredField3800.getGuiAnchor() == null || !Keyboard.isKeyDown(42) && this.recoveredField3800.method_28790()) {
               this.recoveredField3800.setState(true);
            } else {
               Minecraft.getMinecraft().displayGuiScreen(new ModulePlacementGui(CBModulesGui.instance, this.recoveredField3800));
            }
         } else {
            this.recoveredField3800.method_28847(!this.recoveredField3800.method_28866());
            if (!this.recoveredField3800.method_28866()) {
               if (this.recoveredField3800.recoveredField3912 && this.recoveredField3800.isEnabled()) {
                  this.recoveredField3800.setState(false);
               }
            } else {
               this.method_27154();
               if (this.recoveredField3800.getGuiAnchor() == null || !Keyboard.isKeyDown(42) && this.recoveredField3800.method_28790()) {
                  this.recoveredField3800.setState(true);
               } else {
                  Minecraft.getMinecraft().displayGuiScreen(new ModulePlacementGui(CBModulesGui.instance, this.recoveredField3800));
               }
            }
         }

         this.recoveredField3800.method_28786(true);
         this.recoveredField3796.displayString = this.recoveredField3800.getGuiAnchor() == null
            ? (this.recoveredField3800.method_28866() && this.recoveredField3800.isEnabled() ? "Disable" : "Enable")
            : (
               this.recoveredField3800.method_28866() && this.recoveredField3800.isEnabled()
                  ? "Hide from HUD"
                  : (this.recoveredField3800.method_28790() && !Keyboard.isKeyDown(42) ? "Return to HUD" : "Add to HUD")
            );
         this.recoveredField3796.recoveredField2847 = this.recoveredField3800.method_28866() && this.recoveredField3800.isEnabled() ? -5756117 : -13916106;
      }

      if (this.recoveredField3797
         && this.isMouseInside(var1, var2)
         && !this.recoveredField3796.isMouseInside(var1, var2)
         && !this.recoveredField3801.isMouseInside(var1, var2)
         && !this.recoveredField3799.isMouseInside(var1, var2)) {
         Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
         this.recoveredField3800.method_28786(true);
         this.recoveredField3800.setState(!this.recoveredField3800.isEnabled());
         if (this.recoveredField3800.isEnabled()) {
            this.method_27154();
            this.recoveredField3800.setState(true);
         }
      }
   }

   public void method_27153(AbstractModule var1) {
      ((ModuleListElement)CBModulesGui.instance.recoveredField793).recoveredField1358 = false;
      ((ModuleListElement)CBModulesGui.instance.recoveredField793).scrollable = this.recoveredField3795;
      ((ModuleListElement)CBModulesGui.instance.recoveredField793).module = var1;
      CBModulesGui.instance.recoveredField793.recoveredField3012 = CheatBreaker.getInstance().getModuleManager().recoveredField1724;
      CBModulesGui.instance.currentScrollableElement = CBModulesGui.instance.recoveredField793;
   }

   public ModulePreviewElement(AbstractScrollableElement var1, AbstractModule var2, float var3) {
      super(var3);
      recoveredField3798 = this;
      this.recoveredField3800 = var2;
      this.recoveredField3795 = var1;
      boolean var4 = var2 != CheatBreaker.getInstance().getModuleManager().minmap && var2 != CheatBreaker.getInstance().getModuleManager().notifications;
      String var5 = var2.getGuiAnchor() == null ? (var2.method_28866() ? "Disable" : "Enable") : (var2.method_28866() ? "Hide from HUD" : "Add to HUD");
      String var6 = var2.isEnabled() ? "Disable" : "Enable";
      String var7 = this.recoveredField3797 ? "cog-64.png" : "Options";
      CBFontRenderer var8 = CheatBreaker.getInstance().recoveredField1595;
      CBFontRenderer var9 = CheatBreaker.getInstance().playRegular14px;
      this.recoveredField3801 = new ModulesGuiButtonElement(
         var9,
         null,
         var6,
         this.x + this.width / 2 + 2,
         this.y + this.height - 38,
         this.x + this.width - 4,
         this.y + this.height - 24,
         var2.isEnabled() ? -5756117 : -13916106,
         var3
      );
      this.recoveredField3799 = new ModulesGuiButtonElement(
         var8, null, var7, this.x + 4, this.y + this.height - 20, this.x + this.width - 4, this.y + this.height - 6, -12418828, var3
      );
      this.recoveredField3796 = new ModulesGuiButtonElement(
         var9,
         null,
         var5,
         this.x + 4,
         this.y + this.height - 38,
         this.x + this.width / 2 - 2,
         this.y + this.height - 24,
         var2.method_28866() ? -5756117 : -13916106,
         var3
      );
      this.recoveredField3796.method_07928(var4);
   }

   private void drawTabListPreview() {
      Minecraft client = Minecraft.getMinecraft();
      client.ingameGUI.overlayPlayerList.renderPlayerlistPreview(
         this.x + this.width / 2.0F, this.y + this.height / 2.0F - 26,
         Math.max(1, this.width - 12), 37
      );
   }

   public void method_27154() {
      if (this.recoveredField3800 != CheatBreaker.getInstance().getModuleManager().directionHud
         && this.recoveredField3800 != CheatBreaker.getInstance().getModuleManager().recoveredField1714
         && this.recoveredField3800 != CheatBreaker.getInstance().getModuleManager().recoveredField1727
         && this.recoveredField3800 != CheatBreaker.getInstance().getModuleManager().recoveredField1696
         && this.recoveredField3800 != CheatBreaker.getInstance().getModuleManager().recoveredField1731
         && this.recoveredField3800 != CheatBreaker.getInstance().getModuleManager().recoveredField1721
         && this.recoveredField3800 != CheatBreaker.getInstance().getModuleManager().scoreboard
         && (Boolean)CheatBreaker.getInstance().getGlobalSettings().recoveredField483.getValue()) {
         for (Setting var2 : this.recoveredField3800.getSettingsList()) {
            if (var2.getType() == Setting.Type.INTEGER
               && var2.method_08911().toLowerCase().contains("color")
               && !var2.method_08911().toLowerCase().contains("background")
               && !var2.method_08911().toLowerCase().contains("border")
               && !var2.method_08911().toLowerCase().contains("pressed")
               && !var2.method_08911().toLowerCase().contains("amount")
               && !var2.method_08911().toLowerCase().contains("line")) {
               Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
               var2.setValue(CheatBreaker.getInstance().getGlobalSettings().recoveredField536.method_08901());
            }
         }
      }
   }
}
