package com.cheatbreaker.client.ui.element;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.config.Setting;
import com.cheatbreaker.client.module.ModuleManager;
import com.cheatbreaker.client.ui.module.CBModulesGui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class SettingTextEditGui extends GuiScreen {
   public GuiTextField recoveredField2771 = null;
   public GuiScreen recoveredField2772;
   public boolean recoveredField2773;
   public Setting recoveredField2774;
   public float recoveredField2775;

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.recoveredField2772.drawScreen(var1, var2, var3);
      this.drawDefaultBackground();
      a(this.l / 2 - 73, this.m / 2 - 19, this.l / 2 + 73, this.m / 2 + 8, -11250604);
      a(this.l / 2 - 72, this.m / 2 - 18, this.l / 2 + 72, this.m / 2 + 7, -3881788);
      GL11.glPushMatrix();
      GL11.glScalef(this.recoveredField2775, this.recoveredField2775, this.recoveredField2775);
      int var4 = (int)(this.l / this.recoveredField2775);
      int var5 = (int)(this.m / this.recoveredField2775);
      CheatBreaker.getInstance()
         .recoveredField1589
         .drawString(this.recoveredField2774.method_08911(), var4 / 2 - 70.0F / this.recoveredField2775, var5 / 2 - 17.0F / this.recoveredField2775, 1862270976);
      GL11.glPopMatrix();
      this.recoveredField2771.drawTextBox();
   }

   public SettingTextEditGui(GuiScreen var1, float var2) {
      this.recoveredField2772 = var1;
      this.recoveredField2775 = var2;
      this.recoveredField2773 = true;
   }

   @Override
   public void initGui() {
      Keyboard.enableRepeatEvents(true);
      if (!this.recoveredField2773) {
         this.j.displayGuiScreen(this.recoveredField2772);
      } else {
         this.recoveredField2773 = false;
         this.recoveredField2771 = new GuiTextField(299, this.j.fontRendererObj, this.l / 2 - 70, this.m / 2 - 6, 140, 10);
         if (this.recoveredField2774 != null) {
            this.recoveredField2771.setMaxStringLength(this.recoveredField2774.getMaxStringLength());
            this.recoveredField2771.setText(this.recoveredField2774.method_08874());
         }

         this.recoveredField2771.setFocused(true);
      }
   }

   @Override
   public void mouseReleased(int var1, int var2, int var3) {
      super.mouseReleased(var1, var2, var3);
   }

   @Override
   public void a_() {
      Keyboard.enableRepeatEvents(false);
   }

   @Override
   public void mouseClicked(int var1, int var2, int var3) throws java.io.IOException {
      super.mouseClicked(var1, var2, var3);
      this.recoveredField2771.mouseClicked(var1, var2, var3);
   }

   public SettingTextEditGui(Setting var1, GuiScreen var2, float var3) {
      this(var2, var3);
      this.recoveredField2774 = var1;
   }

   @Override
   public void keyTyped(char var1, int var2) throws java.io.IOException {
      ModuleManager var3 = CheatBreaker.getInstance().getModuleManager();
      switch (var2) {
         case 1:
            this.j.displayGuiScreen(this.recoveredField2772);
            ((CBModulesGui)this.recoveredField2772).currentScrollableElement = ((CBModulesGui)this.recoveredField2772).recoveredField801;
            CheatBreaker.getInstance().method_19818().method_04443(var3.recoveredField1695);
            break;
         case 28:
            this.recoveredField2774.setValue(this.recoveredField2771.getText());
            this.j.displayGuiScreen(this.recoveredField2772);
            CheatBreaker.getInstance()
               .getModuleManager()
               .notifications
               .queueNotification("info", EnumChatFormatting.GREEN + "Updated custom string value successfully.", 5000L);
            ((CBModulesGui)this.recoveredField2772).currentScrollableElement = ((CBModulesGui)this.recoveredField2772).recoveredField801;
            CheatBreaker.getInstance().method_19818().method_04443(var3.recoveredField1695);
            break;
         default:
            this.recoveredField2771.textboxKeyTyped(var1, var2);
      }
   }

   @Override
   public void updateScreen() {
      this.recoveredField2771.updateCursorCounter();
   }
}
