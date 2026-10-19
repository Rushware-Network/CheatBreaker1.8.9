package com.cheatbreaker.client.ui.element.module;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.config.GlobalSettings;
import com.cheatbreaker.client.module.AbstractModule;
import com.cheatbreaker.client.ui.element.AbstractScrollableElement;
import com.cheatbreaker.client.ui.util.RenderUtil;
import java.util.ArrayList;
import java.util.List;
import com.cheatbreaker.client.ui.util.GuiThemeColors;

public class ModulePreviewContainer extends AbstractScrollableElement {
   public List<ModulePreviewElement> elements = new ArrayList<>();

   @Override
   public void handleDrawElement(int var1, int var2, float var3) {
      super.handleDrawElement(var1, var2, var3);
      boolean var4 = CheatBreaker.getInstance().getGlobalSettings().recoveredField504.getValue().equals("Compact");
      int var5 = var4 ? 20 : 112;
      byte var6 = 0;
      RenderUtil.method_22054(
         this.x,
         this.y,
         this.x + this.width,
         this.y + this.height + 2,
         8.0,
         GlobalSettings.recoveredField529.method_08908() ? GuiThemeColors.recoveredField1664 : GuiThemeColors.recoveredField1647
      );
      this.preDraw(var1, var2);
      int var7 = 0;
      int var8 = 0;
      int var9 = 0;

      for (ModulePreviewElement var11 : this.elements) {
         var11.yOffset = this.recoveredField3012;
         var11.setDimensions(this.x + 4 + var7 * 120, this.y + 4 + var8 * var5 + var6, 116, var4 ? 16 : 108);
         var11.handleDrawElement(var1, var2, var3);
         var9 = var4 ? 20 : 112;
         if (++var7 == 3) {
            var8++;
            var9 = 0;
            var7 = 0;
         }
      }

      this.recoveredField3009 = 4 + var8 * var5 + var6 + var9;
      this.postDraw(var1, var2);
   }

   public ModulePreviewContainer(float var1, int var2, int var3, int var4, int var5) {
      super(var1, var2, var3, var4, var5);

      for (AbstractModule var7 : CheatBreaker.getInstance().getModuleManager().recoveredField1706) {
         if (var7 != CheatBreaker.getInstance().getModuleManager().notifications && var7 != CheatBreaker.getInstance().getModuleManager().minmap) {
            ModulePreviewElement var8 = new ModulePreviewElement(this, var7, var1);
            this.elements.add(var8);
         }
      }
   }

   @Override
   public boolean method_03198(AbstractModule var1) {
      return false;
   }

   @Override
   public void method_03200(AbstractModule var1) {
   }

   @Override
   public void handleMouseClick(int var1, int var2, int var3) {
      super.handleMouseClick(var1, var2, var3);

      for (ModulePreviewElement var5 : this.elements) {
         if (var5.isMouseInside(var1, var2)) {
            var5.handleMouseClick(var1, var2, var3);
         }
      }
   }
}
