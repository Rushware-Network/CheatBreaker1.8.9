package net.minecraft.client.gui.inventory;

import com.cheatbreaker.client.CheatBreaker;
import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import com.cheatbreaker.client.event.type.MouseClickEvent;

public abstract class GuiContainer extends GuiScreen {
   public ItemStack recoveredField2723;
   public long recoveredField2724;
   public int recoveredField2725;
   public Container h;
   public Slot recoveredField2726;
   public boolean recoveredField2727;
   public long recoveredField2728;
   public Slot recoveredField2729;
   public int f = 176;
   public Slot theSlot;
   public ItemStack recoveredField2730;
   public long recoveredField2731;
   public int i;
   public boolean recoveredField2732;
   public Slot recoveredField2733;
   public Set<Slot> s;
   public int recoveredField2734;
   public int recoveredField2735;
   public Slot recoveredField2736;
   public ItemStack recoveredField2737;
   public boolean recoveredField2738;
   public int recoveredField2739;
   public int g = 166;
   public int recoveredField2740;
   public boolean ignoreMouseUp;
   public static ResourceLocation inventoryBackground = new ResourceLocation("textures/gui/container/inventory.png");
   public int r;
   public int recoveredField2741;

   public abstract void drawGuiContainerBackgroundLayer(float var1, int var2, int var3);

   public boolean c(int var1, int var2, int var3, int var4, int var5, int var6) {
      int var7 = this.i;
      int var8 = this.r;
      var5 -= var7;
      var6 -= var8;
      return var5 >= var1 - 1 && var5 < var1 + var3 + 1 && var6 >= var2 - 1 && var6 < var2 + var4 + 1;
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      int var4 = this.i;
      int var5 = this.r;
      this.drawGuiContainerBackgroundLayer(var3, var1, var2);
      GlStateManager.disableRescaleNormal();
      RenderHelper.disableStandardItemLighting();
      GlStateManager.disableLighting();
      GlStateManager.disableDepth();
      super.drawScreen(var1, var2, var3);
      RenderHelper.enableGUIStandardItemLighting();
      GlStateManager.pushMatrix();
      GlStateManager.translate((float)var4, (float)var5, 0.0F);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.enableRescaleNormal();
      this.theSlot = null;
      short var6 = 240;
      short var7 = 240;
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var6 / 1.0F, var7 / 1.0F);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

      for (int var8 = 0; var8 < this.h.c.size(); var8++) {
         Slot var9 = this.h.c.get(var8);
         this.drawSlot(var9);
         if (this.isMouseOverSlot(var9, var1, var2) && var9.canBeHovered()) {
            this.theSlot = var9;
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            int var10 = var9.xDisplayPosition;
            int var11 = var9.yDisplayPosition;
            GlStateManager.colorMask(true, true, true, false);
            this.drawGradientRect(var10, var11, var10 + 16, var11 + 16, -2130706433, -2130706433);
            GlStateManager.colorMask(true, true, true, true);
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
         }
      }

      RenderHelper.disableStandardItemLighting();
      this.drawGuiContainerForegroundLayer(var1, var2);
      RenderHelper.enableGUIStandardItemLighting();
      InventoryPlayer var15 = this.j.thePlayer.bi;
      ItemStack var16 = this.recoveredField2730 == null ? var15.getItemStack() : this.recoveredField2730;
      if (var16 != null) {
         byte var17 = 8;
         int var20 = this.recoveredField2730 == null ? 8 : 16;
         String var12 = null;
         if (this.recoveredField2730 != null && this.recoveredField2738) {
            var16 = var16.copy();
            var16.stackSize = MathHelper.ceiling_float_int(var16.stackSize / 2.0F);
         } else if (this.recoveredField2727 && this.s.size() > 1) {
            var16 = var16.copy();
            var16.stackSize = this.recoveredField2739;
            if (var16.stackSize == 0) {
               var12 = "" + EnumChatFormatting.YELLOW + "0";
            }
         }

         this.drawItemStack(var16, var1 - var4 - var17, var2 - var5 - var20, var12);
      }

      if (this.recoveredField2723 != null) {
         float var18 = (float)(Minecraft.getSystemTime() - this.recoveredField2728) / 100.0F;
         if (var18 >= 1.0F) {
            var18 = 1.0F;
            this.recoveredField2723 = null;
         }

         int var21 = this.recoveredField2736.xDisplayPosition - this.recoveredField2734;
         int var22 = this.recoveredField2736.yDisplayPosition - this.recoveredField2741;
         int var13 = this.recoveredField2734 + (int)(var21 * var18);
         int var14 = this.recoveredField2741 + (int)(var22 * var18);
         this.drawItemStack(this.recoveredField2723, var13, var14, (String)null);
      }

      GlStateManager.popMatrix();
      if ((
            !CheatBreaker.getInstance().getModuleManager().recoveredField1727.isEnabled()
               || (Boolean)CheatBreaker.getInstance().getModuleManager().recoveredField1727.recoveredField1511.getValue()
         )
         && var15.getItemStack() == null
         && this.theSlot != null
         && this.theSlot.getHasStack()) {
         ItemStack var19 = this.theSlot.getStack();
         this.renderToolTip(var19, var1, var2);
      }

      GlStateManager.enableLighting();
      GlStateManager.enableDepth();
      RenderHelper.enableStandardItemLighting();
   }

   @Override
   public void initGui() {
      super.initGui();
      this.j.thePlayer.bk = this.h;
      this.i = (this.l - this.f) / 2;
      this.r = (this.m - this.g) / 2;
   }

   public boolean isMouseOverSlot(Slot var1, int var2, int var3) {
      return this.c(var1.xDisplayPosition, var1.yDisplayPosition, 16, 16, var2, var3);
   }

   @Override
   public void mouseReleased(int var1, int var2, int var3) {
      Slot var4 = this.getSlotAtPosition(var1, var2);
      int var5 = this.i;
      int var6 = this.r;
      boolean var7 = var1 < var5 || var2 < var6 || var1 >= var5 + this.f || var2 >= var6 + this.g;
      int var8 = -1;
      if (var4 != null) {
         var8 = var4.slotNumber;
      }

      if (var7) {
         var8 = -999;
      }

      if (this.recoveredField2732 && var4 != null && var3 == 0 && this.h.canMergeSlot((ItemStack)null, var4)) {
         if (isShiftKeyDown()) {
            if (var4 != null && var4.inventory != null && this.recoveredField2737 != null) {
               for (Slot var14 : this.h.c) {
                  if (var14 != null
                     && var14.canTakeStack(this.j.thePlayer)
                     && var14.getHasStack()
                     && var14.inventory == var4.inventory
                     && Container.canAddItemToSlot(var14, this.recoveredField2737, true)) {
                     this.handleMouseClick(var14, var14.slotNumber, var3, 1);
                  }
               }
            }
         } else {
            this.handleMouseClick(var4, var8, var3, 6);
         }

         this.recoveredField2732 = false;
         this.recoveredField2724 = 0L;
      } else {
         if (this.recoveredField2727 && this.recoveredField2725 != var3) {
            this.recoveredField2727 = false;
            this.s.clear();
            this.ignoreMouseUp = true;
            return;
         }

         if (this.ignoreMouseUp) {
            this.ignoreMouseUp = false;
            return;
         }

         if (this.recoveredField2733 != null && this.j.gameSettings.touchscreen) {
            if (var3 == 0 || var3 == 1) {
               if (this.recoveredField2730 == null && var4 != this.recoveredField2733) {
                  this.recoveredField2730 = this.recoveredField2733.getStack();
               }

               boolean var12 = Container.canAddItemToSlot(var4, this.recoveredField2730, false);
               if (var8 != -1 && this.recoveredField2730 != null && var12) {
                  this.handleMouseClick(this.recoveredField2733, this.recoveredField2733.slotNumber, var3, 0);
                  this.handleMouseClick(var4, var8, 0, 0);
                  if (this.j.thePlayer.bi.getItemStack() != null) {
                     this.handleMouseClick(this.recoveredField2733, this.recoveredField2733.slotNumber, var3, 0);
                     this.recoveredField2734 = var1 - var5;
                     this.recoveredField2741 = var2 - var6;
                     this.recoveredField2736 = this.recoveredField2733;
                     this.recoveredField2723 = this.recoveredField2730;
                     this.recoveredField2728 = Minecraft.getSystemTime();
                  } else {
                     this.recoveredField2723 = null;
                  }
               } else if (this.recoveredField2730 != null) {
                  this.recoveredField2734 = var1 - var5;
                  this.recoveredField2741 = var2 - var6;
                  this.recoveredField2736 = this.recoveredField2733;
                  this.recoveredField2723 = this.recoveredField2730;
                  this.recoveredField2728 = Minecraft.getSystemTime();
               }

               this.recoveredField2730 = null;
               this.recoveredField2733 = null;
            }
         } else if (this.recoveredField2727 && !this.s.isEmpty()) {
            this.handleMouseClick((Slot)null, -999, Container.func_94534_d(0, this.recoveredField2740), 5);

            for (Slot var10 : this.s) {
               this.handleMouseClick(var10, var10.slotNumber, Container.func_94534_d(1, this.recoveredField2740), 5);
            }

            this.handleMouseClick((Slot)null, -999, Container.func_94534_d(2, this.recoveredField2740), 5);
         } else if (this.j.thePlayer.bi.getItemStack() != null) {
            if (var3 == this.j.gameSettings.keyBindPickBlock.getKeyCode() + 100) {
               this.handleMouseClick(var4, var8, var3, 3);
            } else {
               boolean var9 = var8 != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
               if (var9) {
                  this.recoveredField2737 = var4 != null && var4.getHasStack() ? var4.getStack() : null;
               }

               this.handleMouseClick(var4, var8, var3, var9 ? 1 : 0);
            }
         }
      }

      if (this.j.thePlayer.bi.getItemStack() == null) {
         this.recoveredField2724 = 0L;
      }

      this.recoveredField2727 = false;
   }

   public void drawSlot(Slot var1) {
      int var2 = var1.xDisplayPosition;
      int var3 = var1.yDisplayPosition;
      ItemStack var4 = var1.getStack();
      boolean var5 = false;
      boolean var6 = var1 == this.recoveredField2733 && this.recoveredField2730 != null && !this.recoveredField2738;
      ItemStack var7 = this.j.thePlayer.bi.getItemStack();
      String var8 = null;
      if (var1 == this.recoveredField2733 && this.recoveredField2730 != null && this.recoveredField2738 && var4 != null) {
         var4 = var4.copy();
         var4.stackSize /= 2;
      } else if (this.recoveredField2727 && this.s.contains(var1) && var7 != null) {
         if (this.s.size() == 1) {
            return;
         }

         if (Container.canAddItemToSlot(var1, var7, true) && this.h.canDragIntoSlot(var1)) {
            var4 = var7.copy();
            var5 = true;
            Container.computeStackSize(this.s, this.recoveredField2740, var4, var1.getStack() == null ? 0 : var1.getStack().stackSize);
            if (var4.stackSize > var4.getMaxStackSize()) {
               var8 = EnumChatFormatting.YELLOW + "" + var4.getMaxStackSize();
               var4.stackSize = var4.getMaxStackSize();
            }

            if (var4.stackSize > var1.getItemStackLimit(var4)) {
               var8 = EnumChatFormatting.YELLOW + "" + var1.getItemStackLimit(var4);
               var4.stackSize = var1.getItemStackLimit(var4);
            }
         } else {
            this.s.remove(var1);
            this.method_21475();
         }
      }

      recoveredField2942 = 100.0F;
      this.k.zLevel = 100.0F;
      if (var4 == null) {
         String var9 = var1.getSlotTexture();
         if (var9 != null) {
            TextureAtlasSprite var10 = this.j.getTextureMapBlocks().getAtlasSprite(var9);
            GlStateManager.disableLighting();
            this.j.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
            this.drawTexturedModalRect(var2, var3, var10, 16, 16);
            GlStateManager.enableLighting();
            var6 = true;
         }
      }

      if (!var6) {
         if (var5) {
            a(var2, var3, var2 + 16, var3 + 16, -2130706433);
         }

         GlStateManager.enableDepth();
         this.k.renderItemAndEffectIntoGUI(var4, var2, var3, var1.inventory == this.j.thePlayer.bi);
         this.k.renderItemOverlayIntoGUI(this.q, var4, var2, var3, var8);
      }

      this.k.zLevel = 0.0F;
      recoveredField2942 = 0.0F;
   }

   @Override
   public void mouseClicked(int var1, int var2, int var3) throws java.io.IOException {
      CheatBreaker.getInstance().method_19817().method_21935(new MouseClickEvent(var3));
      this.checkHotbarKeys(var3 - 100);
      super.mouseClicked(var1, var2, var3);
      boolean var4 = var3 == this.j.gameSettings.keyBindPickBlock.getKeyCode() + 100;
      Slot var5 = this.getSlotAtPosition(var1, var2);
      long var6 = Minecraft.getSystemTime();
      this.recoveredField2732 = this.recoveredField2729 == var5 && var6 - this.recoveredField2724 < 250L && this.recoveredField2735 == var3;
      this.ignoreMouseUp = false;
      if (var3 == 0 || var3 == 1 || var4) {
         int var8 = this.i;
         int var9 = this.r;
         boolean var10 = var1 < var8 || var2 < var9 || var1 >= var8 + this.f || var2 >= var9 + this.g;
         int var11 = -1;
         if (var5 != null) {
            var11 = var5.slotNumber;
         }

         if (var10) {
            var11 = -999;
         }

         if (this.j.gameSettings.touchscreen && var10 && this.j.thePlayer.bi.getItemStack() == null) {
            this.j.displayGuiScreen((GuiScreen)null);
            return;
         }

         if (var11 != -1) {
            if (this.j.gameSettings.touchscreen) {
               if (var5 != null && var5.getHasStack()) {
                  this.recoveredField2733 = var5;
                  this.recoveredField2730 = null;
                  this.recoveredField2738 = var3 == 1;
               } else {
                  this.recoveredField2733 = null;
               }
            } else if (!this.recoveredField2727) {
               if (this.j.thePlayer.bi.getItemStack() == null) {
                  if (var3 == this.j.gameSettings.keyBindPickBlock.getKeyCode() + 100) {
                     this.handleMouseClick(var5, var11, var3, 3);
                  } else {
                     boolean var12 = var11 != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
                     byte var13 = 0;
                     if (var12) {
                        this.recoveredField2737 = var5 != null && var5.getHasStack() ? var5.getStack() : null;
                        var13 = 1;
                     } else if (var11 == -999) {
                        var13 = 4;
                     }

                     this.handleMouseClick(var5, var11, var3, var13);
                  }

                  this.ignoreMouseUp = true;
               } else {
                  this.recoveredField2727 = true;
                  this.recoveredField2725 = var3;
                  this.s.clear();
                  if (var3 == 0) {
                     this.recoveredField2740 = 0;
                  } else if (var3 == 1) {
                     this.recoveredField2740 = 1;
                  } else if (var3 == this.j.gameSettings.keyBindPickBlock.getKeyCode() + 100) {
                     this.recoveredField2740 = 2;
                  }
               }
            }
         }
      }

      this.recoveredField2729 = var5;
      this.recoveredField2724 = var6;
      this.recoveredField2735 = var3;
   }

   public boolean checkHotbarKeys(int var1) {
      if (this.j.thePlayer.bi.getItemStack() == null && this.theSlot != null) {
         for (int var2 = 0; var2 < 9; var2++) {
            if (var1 == this.j.gameSettings.keyBindsHotbar[var2].getKeyCode()) {
               this.handleMouseClick(this.theSlot, this.theSlot.slotNumber, var2, 2);
               return true;
            }
         }
      }

      return false;
   }

   @Override
   public void a_() {
      if (this.j.thePlayer != null) {
         this.h.onContainerClosed(this.j.thePlayer);
      }
   }

   public GuiContainer(Container var1) {
      this.s = Sets.newHashSet();
      this.h = var1;
      this.ignoreMouseUp = true;
   }

   @Override
   public void mouseClickMove(int var1, int var2, int var3, long var4) {
      Slot var6 = this.getSlotAtPosition(var1, var2);
      ItemStack var7 = this.j.thePlayer.bi.getItemStack();
      if (this.recoveredField2733 != null && this.j.gameSettings.touchscreen) {
         if (var3 == 0 || var3 == 1) {
            if (this.recoveredField2730 == null) {
               if (var6 != this.recoveredField2733 && this.recoveredField2733.getStack() != null) {
                  this.recoveredField2730 = this.recoveredField2733.getStack().copy();
               }
            } else if (this.recoveredField2730.stackSize > 1 && var6 != null && Container.canAddItemToSlot(var6, this.recoveredField2730, false)) {
               long var8 = Minecraft.getSystemTime();
               if (this.recoveredField2726 == var6) {
                  if (var8 - this.recoveredField2731 > 500L) {
                     this.handleMouseClick(this.recoveredField2733, this.recoveredField2733.slotNumber, 0, 0);
                     this.handleMouseClick(var6, var6.slotNumber, 1, 0);
                     this.handleMouseClick(this.recoveredField2733, this.recoveredField2733.slotNumber, 0, 0);
                     this.recoveredField2731 = var8 + 750L;
                     this.recoveredField2730.stackSize--;
                  }
               } else {
                  this.recoveredField2726 = var6;
                  this.recoveredField2731 = var8;
               }
            }
         }
      } else if (this.recoveredField2727
         && var6 != null
         && var7 != null
         && var7.stackSize > this.s.size()
         && Container.canAddItemToSlot(var6, var7, true)
         && var6.isItemValid(var7)
         && this.h.canDragIntoSlot(var6)) {
         this.s.add(var6);
         this.method_21475();
      }
   }

   @Override
   public void keyTyped(char var1, int var2) throws java.io.IOException {
      if (var2 == 1 || var2 == this.j.gameSettings.keyBindInventory.getKeyCode()) {
         this.j.thePlayer.closeScreen();
      }

      this.checkHotbarKeys(var2);
      if (this.theSlot != null && this.theSlot.getHasStack()) {
         if (var2 == this.j.gameSettings.keyBindPickBlock.getKeyCode()) {
            this.handleMouseClick(this.theSlot, this.theSlot.slotNumber, 0, 3);
         } else if (var2 == this.j.gameSettings.keyBindDrop.getKeyCode()) {
            this.handleMouseClick(this.theSlot, this.theSlot.slotNumber, isCtrlKeyDown() ? 1 : 0, 4);
         }
      }
   }

   public void drawItemStack(ItemStack var1, int var2, int var3, String var4) {
      GlStateManager.translate(0.0F, 0.0F, 32.0F);
      recoveredField2942 = 200.0F;
      this.k.zLevel = 200.0F;
      this.k.renderItemAndEffectIntoGUI(var1, var2, var3, true);
      this.k.renderItemOverlayIntoGUI(this.q, var1, var2, var3 - (this.recoveredField2730 == null ? 0 : 8), var4);
      recoveredField2942 = 0.0F;
      this.k.zLevel = 0.0F;
   }

   @Override
   public void updateScreen() {
      super.updateScreen();
      if (!this.j.thePlayer.isEntityAlive() || this.j.thePlayer.I) {
         this.j.thePlayer.closeScreen();
      }
   }

   public void drawGuiContainerForegroundLayer(int var1, int var2) {
   }

   @Override
   public boolean b_() {
      return false;
   }

   public Slot getSlotAtPosition(int var1, int var2) {
      for (int var3 = 0; var3 < this.h.c.size(); var3++) {
         Slot var4 = this.h.c.get(var3);
         if (this.isMouseOverSlot(var4, var1, var2)) {
            return var4;
         }
      }

      return null;
   }

   public void handleMouseClick(Slot var1, int var2, int var3, int var4) {
      if (var1 != null) {
         var2 = var1.slotNumber;
      }

      this.j.playerController.windowClick(this.h.d, var2, var3, var4, this.j.thePlayer);
   }

   public void method_21475() {
      ItemStack var1 = this.j.thePlayer.bi.getItemStack();
      if (var1 != null && this.recoveredField2727) {
         this.recoveredField2739 = var1.stackSize;

         for (Slot var3 : this.s) {
            ItemStack var4 = var1.copy();
            int var5 = var3.getStack() == null ? 0 : var3.getStack().stackSize;
            Container.computeStackSize(this.s, this.recoveredField2740, var4, var5);
            if (var4.stackSize > var4.getMaxStackSize()) {
               var4.stackSize = var4.getMaxStackSize();
            }

            if (var4.stackSize > var3.getItemStackLimit(var4)) {
               var4.stackSize = var3.getItemStackLimit(var4);
            }

            this.recoveredField2739 = this.recoveredField2739 - (var4.stackSize - var5);
         }
      }
   }
}
