package com.cheatbreaker.client.ui.element.module;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.config.GlobalSettings;
import com.cheatbreaker.client.module.AbstractModule;
import com.cheatbreaker.client.ui.element.AbstractScrollableElement;
import com.cheatbreaker.client.ui.overlay.element.InputFieldElement;
import com.cheatbreaker.client.ui.util.RenderUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.lwjgl.input.Keyboard;
import com.cheatbreaker.client.ui.util.GuiThemeColors;

public class ModulePreviewContainer extends AbstractScrollableElement {
   public List<ModulePreviewElement> elements = new ArrayList<>();
   private final InputFieldElement searchField;
   private final List<String> searchNames = new ArrayList<>();
   private final List<ModulePreviewElement> searchResults = new ArrayList<>();
   private String searchQuery = "";
   private int clipLeft, clipTop, clipRight, clipBottom, clipHeight;
   private float clipScale;

   private void layoutSearch() {
      searchField.setElementSize(this.x + 4, this.y + 4, this.width - 14, 16);
   }

   public void setSearchViewport(int left, int top, int right, int bottom, float pixelScale, int screenHeight) {
      this.clipLeft = left;
      this.clipTop = top;
      this.clipRight = right;
      this.clipBottom = bottom;
      this.clipScale = pixelScale;
      this.clipHeight = screenHeight;
   }

   private void updateSearch() {
      String query = searchField.getText().trim().toLowerCase(Locale.ROOT);
      if (!query.equals(searchQuery)) {
         searchQuery = query;
         searchResults.clear();
         for (int index = 0; index < elements.size(); index++) {
            if (searchNames.get(index).contains(query)) searchResults.add(elements.get(index));
         }
         this.recoveredField3012 = 0;
         this.recoveredField3010 = 0.0;
         this.recoveredField3014 = false;
      }
   }

   public void blurSearch() {
      searchField.method_06028(false);
   }

   public boolean isSearchFocused() {
      return searchField.method_06013();
   }

   public void updateSearchCursor() {
      searchField.handleElementUpdate();
   }

   public boolean handleSearchKey(char character, int key) {
      if (!searchField.method_06013()) return false;
      if (key == Keyboard.KEY_ESCAPE || key == Keyboard.KEY_RETURN || key == Keyboard.KEY_NUMPADENTER) {
         blurSearch();
      } else {
         searchField.handleElementKeyTyped(character, key);
         updateSearch();
      }
      return true;
   }

   public boolean handleSearchClick(int mouseX, int mouseY, int button) {
      layoutSearch();
      searchField.handleElementMouseClicked(mouseX / this.scale, mouseY / this.scale, button, true);
      updateSearch();
      return searchField.a_(mouseX / this.scale, mouseY / this.scale);
   }

   public void restoreSearch(ModulePreviewContainer previous) {
      layoutSearch();
      searchField.setText(previous.searchField.getText());
      updateSearch();
      boolean focused = previous.isSearchFocused();
      previous.blurSearch();
      searchField.method_06028(focused);
   }

   @Override
   public void handleDrawElement(int var1, int var2, float var3) {
      super.handleDrawElement(var1, var2, var3);
      boolean var4 = CheatBreaker.getInstance().getGlobalSettings().recoveredField504.getValue().equals("Compact");
      int var5 = var4 ? 20 : 112;
      byte var6 = 20;
      RenderUtil.method_22054(
         this.x,
         this.y,
         this.x + this.width,
         this.y + this.height + 2,
         8.0,
         GlobalSettings.recoveredField529.method_08908() ? GuiThemeColors.recoveredField1664 : GuiThemeColors.recoveredField1647
      );
      layoutSearch();
      boolean dark = GlobalSettings.recoveredField529.method_08908();
      searchField.recoveredField2633 = dark ? 0xFF202020 : 0xFFD8D8D8;
      searchField.method_06032(dark ? 0xFFE0E0E0 : 0xFF404040);
      searchField.drawElement(var1 / this.scale, var2 / this.scale, true);
      // Reuse the parent's viewport instead of querying OpenGL or allocating a resolution each frame.
      RenderUtil.method_22061(clipLeft, Math.min(clipBottom, Math.max(clipTop, this.y + 24)),
         clipRight, clipBottom, clipScale, clipHeight);
      this.preDraw(var1, var2);
      int var7 = 0;
      int var8 = 0;
      int var9 = 0;

      for (ModulePreviewElement var11 : this.searchResults) {
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
      RenderUtil.method_22061(clipLeft, clipTop, clipRight, clipBottom, clipScale, clipHeight);
      this.postDraw(var1, var2);
   }

   public ModulePreviewContainer(float var1, int var2, int var3, int var4, int var5) {
      super(var1, var2, var3, var4, var5);
      searchField = new InputFieldElement(CheatBreaker.getInstance().recoveredField1589, "Search Mods...", 0, -1);
      searchField.trimToLength(128);

      for (AbstractModule var7 : CheatBreaker.getInstance().getModuleManager().recoveredField1706) {
         if (var7 != CheatBreaker.getInstance().getModuleManager().notifications && var7 != CheatBreaker.getInstance().getModuleManager().minmap) {
            ModulePreviewElement var8 = new ModulePreviewElement(this, var7, var1);
            this.elements.add(var8);
            this.searchNames.add(var7.getName().toLowerCase(Locale.ROOT));
         }
      }
      this.searchResults.addAll(this.elements);
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
      if (var2 < (this.y + 24) * this.scale) return;
      super.handleMouseClick(var1, var2, var3);

      for (ModulePreviewElement var5 : this.searchResults) {
         if (var5.isMouseInside(var1, var2)) {
            var5.handleMouseClick(var1, var2, var3);
         }
      }
   }
}
