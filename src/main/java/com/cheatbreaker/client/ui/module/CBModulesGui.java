package com.cheatbreaker.client.ui.module;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.ui.ClientUiScale;
import com.cheatbreaker.client.module.AbstractModule;
import com.cheatbreaker.client.ui.element.AbstractModulesGuiElement;
import com.cheatbreaker.client.ui.element.AbstractScrollableElement;
import com.cheatbreaker.client.ui.element.module.ModuleListElement;
import com.cheatbreaker.client.ui.element.module.ModulePreviewContainer;
import com.cheatbreaker.client.ui.element.module.ModulesGuiButtonElement;
import com.cheatbreaker.client.ui.element.profile.ProfilesListElement;
import com.cheatbreaker.client.ui.util.RenderUtil;
import com.cheatbreaker.client.ui.util.font.CBFontRenderer;
import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import com.cheatbreaker.client.ui.module.ModuleGroupPositionSnapshot;
import com.cheatbreaker.client.ui.module.ModuleResizeSnapshot;

public class CBModulesGui extends GuiScreen {
   private float clientUiRenderScale;
   public float recoveredField778;
   public ResourceLocation recoveredField779 = new ResourceLocation("client/icons/cog-64.png");
   public List<CBModulePosition> positions;
   public static CBModulesGui instance;
   public List<ModuleGroupPositionSnapshot> recoveredField780;
   public AbstractScrollableElement currentScrollableElement;
   public float recoveredField781;
   public float recoveredField782;
   public static boolean recoveredField784 = false;
   public static AbstractModule draggingModule;
   public int recoveredField786;
   public boolean recoveredField787;
   public ModulesGuiButtonElement recoveredField788;
   public List<AbstractScrollableElement> recoveredField789;
   public ModuleResizeSnapshot recoveredField790;
   public boolean recoveredField791;
   public ModulesGuiButtonElement recoveredField792;
   public AbstractScrollableElement recoveredField793;
   public int recoveredField794;
   public AbstractScrollableElement recoveredField795;
   public AbstractScrollableElement recoveredField796;
   public int recoveredField797;
   public List<ModuleGroupPositionSnapshot> recoveredField798;
   public ResourceLocation recoveredField799 = new ResourceLocation("client/icons/delete-64.png");
   public AbstractScrollableElement recoveredField801;
   public AbstractScrollableElement recoveredField802;
   public List<ModulesGuiButtonElement> buttons;
   private ModulesGuiButtonElement cosmeticsButton;
   public List<AbstractModule> recoveredField803;

   public void snapVertically(float var1) {
      for (CBModulePosition var3 : this.positions) {
         var3.module.setTranslations(var3.module.getXTranslation(), var3.module.getYTranslation() + var1);
      }
   }

   @Override
   public void mouseReleased(int var1, int var2, int var3) {
      ScaledResolution var4 = new ScaledResolution(this.j);
      if (this.recoveredField790 != null && var3 == 0) {
         this.method_27009(this.recoveredField790.recoveredField3333, this.recoveredField790.recoveredField3337, var1, var2, var4);
         this.recoveredField790 = null;
      }

      if (draggingModule != null && var3 == 0) {
         if (this.recoveredField791) {
            for (CBModulePosition var6 : this.positions) {
               CBGuiAnchor var7 = CBAnchorHelper.getAnchor(var1, var2, var4);
               if (var7 != CBGuiAnchor.MIDDLE_MIDDLE && var7 != var6.module.getGuiAnchor() && this.recoveredField791) {
                  this.method_27009(var6.module, var7, var1, var2, var4);
                  var6.x = var1 - var6.module.getXTranslation();
                  var6.y = var2 - var6.module.getYTranslation();
               }
            }

            if (this.getModulePosition(draggingModule) == null) {
               float[] var8 = draggingModule.getScaledPoints(var4, true);
               float var9 = var1 - draggingModule.getXTranslation();
               float var10 = var2 - draggingModule.getYTranslation();
               this.positions.add(new CBModulePosition(draggingModule, var9, var10));
            }

            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
         }

         draggingModule = null;
      }
   }

   public void method_27003(AbstractScrollableElement var1, boolean var2, int var3) {
      if (var2) {
         var1.x = var1.recoveredField3011;
         recoveredField784 = false;
         this.recoveredField795 = null;
      } else {
         var1.x = var3 / 2 - 185;
         this.currentScrollableElement = null;
         this.recoveredField795 = var1;
      }
   }

   @Override
   public void a_() {
      Keyboard.enableRepeatEvents(false);
      this.j.entityRenderer.stopUseShader();
   }

   public void method_26999(ScaledResolution var1) {
      if (!Mouse.isButtonDown(1) && draggingModule != null) {
         for (CBModulePosition var3 : this.positions) {
            if (var3.module == draggingModule && (Boolean)CheatBreaker.getInstance().getGlobalSettings().recoveredField540.getValue()) {
               Object var4 = null;

               for (AbstractModule var6 : this.recoveredField803) {
                  float[] var7 = var3.module.getScaledPoints(var1, true);
                  float var8 = this.l / 2 - (var7[0] + var3.module.recoveredField3889 / 2.0F) * var3.module.method_28770();
                  float var9 = this.m / 2 - (var7[1] + var3.module.recoveredField3894 / 2.0F) * var3.module.method_28770();
                  float var10 = (Float)CheatBreaker.getInstance().getGlobalSettings().recoveredField569.getValue();
                  if (var9 >= -var10 && var9 <= var10) {
                     RenderUtil.method_22054(0.0, this.m / 2 - 0.25F, this.l, this.m / 2 + 0.25F, 0.0, -3596854);
                  }

                  if (var8 >= -var10 && var8 <= var10) {
                     RenderUtil.method_22054(this.l / 2 - 0.25F, 0.0, this.l / 2 + 0.25F, this.m, 0.0, -3596854);
                  }

                  if (this.getModulePosition(var6) == null
                     && var6.getGuiAnchor() != null
                     && var6.isEnabled()
                     && var6 != CheatBreaker.getInstance().getModuleManager().minmap
                     && (var6.recoveredField3912 || var6.method_28866())
                     && (var4 == null || var4 == var6)) {
                     float[] var11 = var6.getScaledPoints(var1, true);
                     boolean var12 = false;
                     float var13 = var11[0] * var6.method_28770() - var7[0] * var3.module.method_28770();
                     float var14 = (var11[0] + var6.recoveredField3889) * var6.method_28770()
                        - (var7[0] + var3.module.recoveredField3889) * var3.module.method_28770();
                     float var15 = (var11[0] + var6.recoveredField3889) * var6.method_28770() - var7[0] * var3.module.method_28770();
                     float var16 = var11[0] * var6.method_28770() - (var7[0] + var3.module.recoveredField3889) * var3.module.method_28770();
                     float var17 = var11[1] * var6.method_28770() - var7[1] * var3.module.method_28770();
                     float var18 = (var11[1] + var6.recoveredField3894) * var6.method_28770()
                        - (var7[1] + var3.module.recoveredField3894) * var3.module.method_28770();
                     float var19 = (var11[1] + var6.recoveredField3894) * var6.method_28770() - var7[1] * var3.module.method_28770();
                     float var20 = var11[1] * var6.method_28770() - (var7[1] + var3.module.recoveredField3894) * var3.module.method_28770();
                     if (var13 >= -var10 && var13 <= var10) {
                        var12 = true;
                        RenderUtil.method_22054(var11[0] * var6.method_28770() - 0.5F, 0.0, var11[0] * var6.method_28770(), this.m, 0.0, -3596854);
                     }

                     if (var14 >= -var10 && var14 <= var10) {
                        var12 = true;
                        RenderUtil.method_22054(
                           (var11[0] + var6.recoveredField3889) * var6.method_28770(),
                           0.0,
                           (var11[0] + var6.recoveredField3889) * var6.method_28770() + 0.5F,
                           this.m,
                           0.0,
                           -3596854
                        );
                     }

                     if (var16 >= -var10 && var16 <= var10) {
                        var12 = true;
                        RenderUtil.method_22054(var11[0] * var6.method_28770(), 0.0, var11[0] * var6.method_28770() + 0.5F, this.m, 0.0, -3596854);
                     }

                     if (var15 >= -var10 && var15 <= var10) {
                        var12 = true;
                        RenderUtil.method_22054(
                           (var11[0] + var6.recoveredField3889) * var6.method_28770(),
                           0.0,
                           (var11[0] + var6.recoveredField3889) * var6.method_28770() + 0.5F,
                           this.m,
                           0.0,
                           -3596854
                        );
                     }

                     if (var17 >= -var10 && var17 <= var10) {
                        var12 = true;
                        RenderUtil.method_22054(0.0, var11[1] * var6.method_28770(), this.l, var11[1] * var6.method_28770() + 0.5F, 0.0, -3596854);
                     }

                     if (var18 >= -var10 && var18 <= var10) {
                        var12 = true;
                        RenderUtil.method_22054(
                           0.0,
                           (var11[1] + var6.recoveredField3894) * var6.method_28770(),
                           this.l,
                           (var11[1] + var6.recoveredField3894) * var6.method_28770() + 0.5F,
                           0.0,
                           -3596854
                        );
                     }

                     if (var20 >= -var10 && var20 <= var10) {
                        var12 = true;
                        RenderUtil.method_22054(0.0, var11[1] * var6.method_28770(), this.l, var11[1] * var6.method_28770() + 0.5F, 0.0, -3596854);
                     }

                     if (var19 >= -var10 && var19 <= var10) {
                        var12 = true;
                        RenderUtil.method_22054(
                           0.0,
                           (var11[1] + var6.recoveredField3894) * var6.method_28770() - 0.5F,
                           this.l,
                           (var11[1] + var6.recoveredField3894) * var6.method_28770(),
                           0.0,
                           -3596854
                        );
                     }

                     if (var12) {
                        GL11.glPushMatrix();
                        var6.scaleAndTranslate(var1);
                        Gui.method_00887(0.0F, 0.0F, var6.recoveredField3889, var6.recoveredField3894, 0.5F, 0, 449387978);
                        GL11.glPopMatrix();
                     }
                  }
               }
            }
         }
      }
   }

   public void snapHorizontally(float var1) {
      for (CBModulePosition var3 : this.positions) {
         var3.module.setTranslations(var3.module.getXTranslation() + var1, var3.module.getYTranslation());
      }
   }

   public void method_27006(CBModulePosition var1, int var2, int var3, ScaledResolution var4) {
      if (var1.module.getGuiAnchor() != null
         && var1.module.isEnabled()
         && var1.module != CheatBreaker.getInstance().getModuleManager().minmap
         && (var1.module.recoveredField3912 || var1.module.method_28866())) {
         float var5 = var2 - var1.x;
         float var6 = var3 - var1.y;
         if (!this.recoveredField791 && var1.module == draggingModule && (var2 != this.recoveredField782 || var3 != this.recoveredField781)) {
            if (this.recoveredField780.size() > 50) {
               this.recoveredField780.remove(0);
            }

            this.recoveredField780.add(new ModuleGroupPositionSnapshot(this, this.positions));
            CheatBreaker.getInstance().getConfigManager().method_25097();
            this.recoveredField791 = true;
         }

         float[] var7 = var1.module.getScaledPoints(var4, false);
         if (!Mouse.isButtonDown(1) && this.recoveredField791 && var1.module == draggingModule) {
            float var8 = var5;
            float var9 = var6;
            var5 = this.method_27008(var1.module, var5, var7, (int)(var1.module.recoveredField3889 * var1.module.method_28770()));
            var6 = this.method_27015(var1.module, var6, var7, (int)(var1.module.recoveredField3894 * var1.module.method_28770()));
            float var10 = var8 - var5;
            float var11 = var9 - var6;

            for (CBModulePosition var13 : this.positions) {
               if (var13 != var1) {
                  var7 = var13.module.getScaledPoints(var4, false);
                  float var14 = this.method_27008(
                     var13.module, var13.module.getXTranslation() - var10, var7, (int)(var13.module.recoveredField3889 * var13.module.method_28770())
                  );
                  float var15 = this.method_27015(
                     var13.module, var13.module.getYTranslation() - var11, var7, (int)(var13.module.recoveredField3894 * var13.module.method_28770())
                  );
                  var13.module.setTranslations(var14, var15);
               }
            }
         }

         if (this.recoveredField791) {
            var1.module.setTranslations(var5, var6);
         }
      }
   }

   public void method_27001(ScaledResolution var1, int var2, int var3, int var4) {
      for (AbstractModule var6 : this.recoveredField803) {
         float[] var7;
         if (var6.getGuiAnchor() != null
            && var6.isEnabled()
            && !var6.getName().contains("Zans")
            && var2 > (var7 = var6.getScaledPoints(var1, true))[0] * var6.method_28770()
            && var2 < (var7[0] + var6.recoveredField3889) * var6.method_28770()
            && var3 > var7[1] * var6.method_28770()
            && var3 < (var7[1] + var6.recoveredField3894) * var6.method_28770()) {
            boolean var8 = !var6.getSettingsList().isEmpty()
               && var2 >= var7[0] * var6.method_28770()
               && var2 <= (var7[0] + 10.0F) * var6.method_28770()
               && var3 >= (var7[1] + var6.recoveredField3894 - 10.0F) * var6.method_28770()
               && var3 <= (var7[1] + var6.recoveredField3894 + 2.0F) * var6.method_28770();
            boolean var9 = var2 > (var7[0] + var6.recoveredField3889 - 10.0F) * var6.method_28770()
               && var2 < (var7[0] + var6.recoveredField3889 + 2.0F) * var6.method_28770()
               && var3 > (var7[1] + var6.recoveredField3894 - 10.0F) * var6.method_28770()
               && var3 < (var7[1] + var6.recoveredField3894 + 2.0F) * var6.method_28770();
            boolean var10 = !var6.getSettingsList().isEmpty()
               && var2 >= (var7[0] + var6.recoveredField3889 / 2.0F - 10.0F) * var6.method_28770()
               && var2 <= (var7[0] + var6.recoveredField3889 / 2.0F - 2.0F) * var6.method_28770()
               && var3 >= (var7[1] + var6.recoveredField3894 + 2.0F) * var6.method_28770()
               && var3 <= (var7[1] + var6.recoveredField3894 + 12.0F) * var6.method_28770();
            boolean var11 = var2 > (var7[0] + var6.recoveredField3889 / 2.0F + 0.0F) * var6.method_28770()
               && var2 < (var7[0] + var6.recoveredField3889 / 2.0F + 8.0F) * var6.method_28770()
               && var3 > (var7[1] + var6.recoveredField3894 + 2.0F) * var6.method_28770()
               && var3 < (var7[1] + var6.recoveredField3894 + 12.0F) * var6.method_28770();
            boolean var12 = var6.recoveredField3889 < 22.0F || var6.recoveredField3894 < 8.0F;
            boolean var13 = var12 ? !var10 && !var11 : !var8 && !var9;
            if (var4 == 0 && var13 && var6 != CheatBreaker.getInstance().getModuleManager().minmap) {
               boolean var14 = true;
               if (this.getModulePosition(var6) != null) {
                  this.method_27007(var6);
                  var14 = false;
               }

               float var15 = var2 - var6.getXTranslation() * var6.method_28770();
               float var16 = var3 - var6.getYTranslation() * var6.method_28770();
               this.recoveredField782 = var2;
               this.recoveredField781 = var3;
               this.recoveredField791 = false;
               draggingModule = var6;
               if (this.getModulePosition(var6) == null) {
                  if (!isCtrlKeyDown() && var14) {
                     this.positions.clear();
                  }

                  if (var14 || !isCtrlKeyDown()) {
                     this.positions.add(new CBModulePosition(var6, var15, var16));
                  }
               }

               this.method_27013(var1, var2, var3);
            }

            if (var4 == 0 && (this.recoveredField795 == null || !this.recoveredField795.isMouseInside(var2, var3))) {
               if (var12 ? !var10 : !var8) {
                  if (var12 ? var11 : var9) {
                     Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
                     var6.setState(false);
                  }
               } else {
                  Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
                  ((ModuleListElement)this.recoveredField793).recoveredField1358 = false;
                  ((ModuleListElement)this.recoveredField793).module = var6;
                  this.currentScrollableElement = this.recoveredField793;
               }
            } else if (var4 == 1) {
               Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
               this.recoveredField780.add(new ModuleGroupPositionSnapshot(this, this.positions));
               float[] var17 = CBAnchorHelper.getPositions(var6.getGuiAnchor());
               if (var6 == CheatBreaker.getInstance().getModuleManager().chatModule && var6.getGuiAnchor() == var6.method_28830()) {
                  var6.setTranslations(var6.defaultXTranslation, var6.defaultYTranslation);
               } else {
                  var6.setTranslations(var17[0], var17[1]);
               }
            }

            if (var6 != CheatBreaker.getInstance().getModuleManager().minmap) {
               break;
            }
         }
      }
   }

   @Override
   public void keyTyped(char var1, int var2) throws java.io.IOException {
      if (var2 == 1) {
         CheatBreaker.getInstance().configManager.method_25109();
      }

      super.keyTyped(var1, var2);
      if (var2 == 30 && isCtrlKeyDown()) {
         for (AbstractModule var4 : this.recoveredField803) {
            if (var4.isEnabled() && var4.getGuiAnchor() != null && this.getModulePosition(var4) == null) {
               float var5 = this.recoveredField786 - var4.getXTranslation() * var4.method_28770();
               float var6 = this.recoveredField797 - var4.getYTranslation() * var4.method_28770();
               this.positions.add(new CBModulePosition(var4, var5, var6));
            }
         }
      }

      if (var2 == 45 && isCtrlKeyDown()) {
         for (AbstractModule var16 : this.recoveredField803) {
            if (this.getModulePosition(var16) != null) {
               var16.setState(false);
            }
         }
      }

      if (var2 == 19 && isCtrlKeyDown()) {
         for (AbstractModule var17 : this.recoveredField803) {
            if (this.getModulePosition(var17) != null) {
               var17.recoveredField3895.setValue(1.0F);
            }
         }
      }

      if (var2 == 44 && isCtrlKeyDown()) {
         if (!this.recoveredField780.isEmpty()) {
            int var15 = this.recoveredField780.size() - 1;
            ModuleGroupPositionSnapshot var20 = this.recoveredField780.get(this.recoveredField780.size() - 1);

            for (int var23 = 0; var23 < var20.recoveredField1007.size(); var23++) {
               AbstractModule var26 = var20.recoveredField1007.get(var23);
               float var27 = var20.recoveredField1006.get(var23);
               float var28 = var20.recoveredField1003.get(var23);
               CBGuiAnchor var29 = var20.recoveredField1008.get(var23);
               Float var30 = (Float)var20.recoveredField1004.get(var23);
               var26.method_28819(var29);
               var26.setTranslations(var27, var28);
               var26.recoveredField3895.setValue(var30);
            }

            if (this.recoveredField798.size() > 50) {
               this.recoveredField798.remove(0);
            }

            this.recoveredField798.add(var20);
            this.recoveredField780.remove(var15);
         }
      } else if (var2 == 21 && isCtrlKeyDown()) {
         if (!this.recoveredField798.isEmpty()) {
            int var14 = this.recoveredField798.size() - 1;
            ModuleGroupPositionSnapshot var19 = this.recoveredField798.get(this.recoveredField798.size() - 1);

            for (int var22 = 0; var22 < var19.recoveredField1007.size(); var22++) {
               AbstractModule var25 = var19.recoveredField1007.get(var22);
               float var7 = var19.recoveredField1006.get(var22);
               float var8 = var19.recoveredField1003.get(var22);
               CBGuiAnchor var9 = var19.recoveredField1008.get(var22);
               Float var10 = (Float)var19.recoveredField1004.get(var22);
               var25.method_28819(var9);
               var25.setTranslations(var7, var8);
               var25.recoveredField3895.setValue(var10);
            }

            if (this.recoveredField798.size() > 50) {
               this.recoveredField798.remove(0);
            }

            this.recoveredField780.add(var19);
            this.recoveredField798.remove(var14);
         }
      } else {
         this.recoveredField794 = 0;

         for (CBModulePosition var18 : this.positions) {
            AbstractModule var21 = var18.module;
            if (var21 != null) {
               float var24 = 1.0F;
               if (isCtrlKeyDown() && isShiftKeyDown()) {
                  var24 = 1.0F / CheatBreaker.method_19763() / 4.0F;
               } else if (isCtrlKeyDown()) {
                  var24 = 4.0F;
               } else if (isShiftKeyDown()) {
                  var24 = 1.0F / CheatBreaker.method_19763() / 2.0F;
               }

               switch (var2) {
                  case 200:
                     var21.setTranslations(var21.getXTranslation(), var21.getYTranslation() - var24);
                  case 201:
                  case 202:
                  case 204:
                  case 206:
                  case 207:
                  default:
                     break;
                  case 203:
                     var21.setTranslations(var21.getXTranslation() - var24, var21.getYTranslation());
                     break;
                  case 205:
                     var21.setTranslations(var21.getXTranslation() + var24, var21.getYTranslation());
                     break;
                  case 208:
                     var21.setTranslations(var21.getXTranslation(), var21.getYTranslation() + var24);
               }
            }
         }
      }
   }

   public float method_27008(AbstractModule var1, float var2, float[] var3, int var4) {
      float var5 = var2;
      float var6 = 2.0F;
      if (var2 + var3[0] * var1.method_28770() < var6) {
         var5 = -var3[0] * var1.method_28770() + var6;
      } else if (var2 + var3[0] * var1.method_28770() + var4 > this.l - var6) {
         var5 = this.l - var3[0] * var1.method_28770() - var4 - var6;
      }

      return var5;
   }

   public static float getSmoothFloat(float var0) {
      float var1 = var0 / (Minecraft.debugFPS + 1);
      return Math.max(var1, 1.0F);
   }

   public void renderRoundButton(String var1, int var2, int var3) {
      CBFontRenderer var4 = CheatBreaker.getInstance().playRegular14px;
      float var5 = var4.getStringWidth(var1);
      RenderUtil.method_22054(var2, var3, var2 + var5 + 4.0F, var3 + 10, 2.0, -1073741825);
      var4.drawString(var1, var2 + 2, var3, -16777216);
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      super.drawScreen(var1, var2, var3);
      this.method_11292();
      ScaledResolution var7 = new ScaledResolution(this.j);
      float var8 = this.clientUiRenderScale;
      if (draggingModule != null) {
         if (!Mouse.isButtonDown(1)) {
            RenderUtil.method_22054(2.0, 2.0, 2.5, this.m - 2, 0.0, -15599126);
            RenderUtil.method_22054(this.l - 2.5F, 2.0, this.l - 2, this.m - 2, 0.0, -15599126);
            RenderUtil.method_22054(2.0, 2.0, this.l - 2, 2.5, 0.0, -15599126);
            RenderUtil.method_22054(2.0, this.m - 2.5F, this.l - 2, this.m - 2, 0.0, -15599126);
         }

         try {
            this.recoveredField803
               .sort(
                  (var2x, var3x) -> {
                     if (var2x != draggingModule && var3x != draggingModule && var2x.getGuiAnchor() != null && var3x.getGuiAnchor() != null) {
                        float[] var4x = var2x.getScaledPoints(var7, true);
                        float[] var5x = var3x.getScaledPoints(var7, true);
                        float[] var6x = draggingModule.getScaledPoints(var7, true);
                        Rectangle var7x = new Rectangle(
                           (int)(var4x[0] * var2x.method_28770()),
                           (int)(var4x[1] * var2x.method_28770()),
                           (int)(var2x.recoveredField3894 * var2x.method_28770()),
                           (int)(var2x.recoveredField3889 * var2x.method_28770())
                        );
                        Rectangle var8x = new Rectangle(
                           (int)(var5x[0] * var3x.method_28770()),
                           (int)(var5x[1] * var3x.method_28770()),
                           (int)(var3x.recoveredField3894 * var3x.method_28770()),
                           (int)(var3x.recoveredField3889 * var3x.method_28770())
                        );
                        Rectangle var9x = new Rectangle(
                           (int)(var6x[0] * draggingModule.method_28770()),
                           (int)(var6x[1] * draggingModule.method_28770()),
                           (int)(draggingModule.recoveredField3889 * draggingModule.method_28770()),
                           (int)(draggingModule.recoveredField3894 * draggingModule.method_28770())
                        );

                        try {
                           return this.getIntersectionFloat(var7x, var9x) > this.getIntersectionFloat(var8x, var9x) ? -1 : 1;
                        } catch (Exception var11x) {
                           return 0;
                        }
                     } else {
                        return 0;
                     }
                  }
               );
         } catch (Exception var27) {
            var27.printStackTrace();
         }

         CBModulePosition var9 = this.getModulePosition(draggingModule);
         if (var9 != null) {
            this.positions.remove(var9);
            this.positions.add(var9);
         }

         for (CBModulePosition var11 : this.positions) {
            this.method_27006(var11, var1, var2, var7);
            if ((Boolean)CheatBreaker.getInstance().getGlobalSettings().recoveredField540.getValue()
               && this.recoveredField791
               && !Mouse.isButtonDown(1)
               && var11.module == draggingModule) {
               for (AbstractModule var13 : this.recoveredField803) {
                  float[] var14 = var11.module.getScaledPoints(var7, true);
                  float var15 = this.m / 2 - (var14[1] + var11.module.recoveredField3894 / 2.0F) * var11.module.method_28770();
                  float var16 = this.l / 2 - (var14[0] + var11.module.recoveredField3889 / 2.0F) * var11.module.method_28770();
                  float var17 = (Float)CheatBreaker.getInstance().getGlobalSettings().recoveredField569.getValue();
                  if (var16 >= -var17 && var16 <= var17) {
                     this.snapHorizontally(var16);
                  }

                  if (var15 >= -var17 && var15 <= var17) {
                     this.snapVertically(var15);
                  }

                  if (this.getModulePosition(var13) == null
                     && var13.getGuiAnchor() != null
                     && var13.method_28866()
                     && var13.isEnabled()
                     && !var13.getName().contains("Zans")) {
                     float[] var18 = var13.getScaledPoints(var7, true);
                     boolean var19 = true;
                     boolean var20 = true;
                     float var21 = var18[0] * var13.method_28770() - var14[0] * var11.module.method_28770();
                     float var22 = (var18[0] + var13.recoveredField3889) * var13.method_28770()
                        - (var14[0] + var11.module.recoveredField3889) * var11.module.method_28770();
                     float var23 = (var18[0] + var13.recoveredField3889) * var13.method_28770() - var14[0] * var11.module.method_28770();
                     float var24 = var18[0] * var13.method_28770() - (var14[0] + var11.module.recoveredField3889) * var11.module.method_28770();
                     float var25 = var18[1] * var13.method_28770() - var14[1] * var11.module.method_28770();
                     float var5 = (var18[1] + var13.recoveredField3894) * var13.method_28770()
                        - (var14[1] + var11.module.recoveredField3894) * var11.module.method_28770();
                     float var4 = (var18[1] + var13.recoveredField3894) * var13.method_28770() - var14[1] * var11.module.method_28770();
                     float var26 = var18[1] * var13.method_28770() - (var14[1] + var11.module.recoveredField3894) * var11.module.method_28770();
                     if (var21 >= -var17 && var21 <= var17) {
                        var19 = false;
                        this.snapHorizontally(var21);
                     }

                     if (var22 >= -var17 && var22 <= var17 && var19) {
                        var19 = false;
                        this.snapHorizontally(var22);
                     }

                     if (var24 >= -var17 && var24 <= var17 && var19) {
                        var19 = false;
                        this.snapHorizontally(var24);
                     }

                     if (var23 >= -var17 && var23 <= var17 && var19) {
                        this.snapHorizontally(var23);
                     }

                     if (var25 >= -var17 && var25 <= var17) {
                        var20 = false;
                        this.snapVertically(var25);
                     }

                     if (var5 >= -var17 && var5 <= var17 && var20) {
                        var20 = false;
                        this.snapVertically(var5);
                     }

                     if (var26 >= -var17 && var26 <= var17 && var20) {
                        var20 = false;
                        this.snapVertically(var26);
                     }

                     if (var4 >= -var17 && var4 <= var17 && var20) {
                        this.snapVertically(var4);
                     }
                  }
               }
            }
         }
      } else if (this.recoveredField790 != null) {
         float var30 = 1.0F;
         switch (this.recoveredField790.recoveredField3340) {
            case RIGHT_BOTTOM:
               int var35 = var2 - this.recoveredField790.recoveredField3332 + (var1 - this.recoveredField790.recoveredField3335);
               var30 = this.recoveredField790.recoveredField3330 - var35 / 115.0F;
               break;
            case LEFT_TOP:
               int var34 = var2 - this.recoveredField790.recoveredField3332 + (var1 - this.recoveredField790.recoveredField3335);
               var30 = this.recoveredField790.recoveredField3330 + var34 / 115.0F;
               break;
            case RIGHT_TOP:
               int var33 = var1 - this.recoveredField790.recoveredField3335 - (var2 - this.recoveredField790.recoveredField3332);
               var30 = this.recoveredField790.recoveredField3330 - var33 / 115.0F;
               break;
            case LEFT_BOTTOM:
               int var32 = var1 - this.recoveredField790.recoveredField3335 - (var2 - this.recoveredField790.recoveredField3332);
               var30 = this.recoveredField790.recoveredField3330 + var32 / 115.0F;
         }

         if (var30 >= 0.5F && var30 <= 1.5F) {
            this.recoveredField790.recoveredField3333.recoveredField3895.setValue((float)(Math.round(var30 * 100.0) / 100.0));
         }
      }

      this.method_26999(var7);
      boolean var31 = true;

      for (AbstractModule var38 : this.recoveredField803) {
         boolean var40 = this.method_26997(var8, var38, var7, var1, var2, var31);
         if (!var40) {
            var31 = false;
         }
      }

      GL11.glPushMatrix();
      GL11.glScalef(var8, var8, var8);
      int var37 = (int)(this.l / var8);
      int var39 = (int)(this.m / var8);
      this.recoveredField792.handleDrawElement(var1, var2, var3);
      this.recoveredField788.handleDrawElement(var1, var2, var3);
      this.cosmeticsButton.handleDrawElement(var1, var2, var3);
      float var41 = this.recoveredField778 * 8.0F / 255.0F;
      GL11.glPushMatrix();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, var41);
      int var42 = 16777215;
      if (var41 / 4.0F > 0.0F && var41 / 4.0F < 1.0F) {
         var42 = new Color(1.0F, 1.0F, 1.0F, var41 / 4.0F).getRGB();
      }

      GL11.glColor4f(1.0F, 1.0F, 1.0F, var41);
      if (var41 > 1.0F) {
         GL11.glTranslatef(-(this.recoveredField778 * 2.0F - 32.0F) / 12.0F - 1.0F, 0.0F, 0.0F);
      }

      RenderUtil.method_22064(
         new ResourceLocation("client/logo_white.png"),
         var37 / 2 - 14,
         var39 / 2 - 47 - (CheatBreaker.getInstance().getConfigManager().method_25096() ? 22 : 0),
         28.0F,
         15.0F
      );
      if (var41 > 1.0F) {
         CheatBreaker.getInstance()
            .recoveredField1595
            .drawString("| CHEAT", var37 / 2.0F + 18.0F, var39 / 2 - 45 - (CheatBreaker.getInstance().getConfigManager().method_25096() ? 22 : 0), var42);
         CheatBreaker.getInstance()
            .recoveredField1557
            .drawString("BREAKER", var37 / 2.0F + 53.0F, var39 / 2 - 45 - (CheatBreaker.getInstance().getConfigManager().method_25096() ? 22 : 0), var42);
      }

      GL11.glPopMatrix();

      for (ModulesGuiButtonElement var46 : this.buttons) {
         var46.handleDrawElement(var1, var2, var3);
      }

      if (draggingModule == null) {
         GL11.glPushMatrix();
         GL11.glEnable(3089);
         RenderUtil.method_22061(var37 / 2 - 185, var39 / 2 + 15, var37 / 2 + 185, var39 - 20, var7.getScaleFactor() * var8, var39);

         for (AbstractScrollableElement var47 : this.recoveredField789) {
            if (var47 == this.recoveredField795 || var47 == this.currentScrollableElement) {
               var47.handleDrawElement(var1, var2, var3);
            }
         }

         GL11.glDisable(3089);
         GL11.glPopMatrix();
      }

      GL11.glPopMatrix();
      if (this.recoveredField786 != -1) {
         if (Mouse.isButtonDown(0)) {
            if (this.recoveredField786 != var1 && this.recoveredField797 != var2) {
               method_00887(this.recoveredField786, this.recoveredField797, var1, var2, 0.49F, -1358888961, 520159231);
            }
         } else {
            this.positions.clear();

            for (AbstractModule var48 : this.recoveredField803) {
               if (var48.getGuiAnchor() != null && var48.isEnabled() && !var48.getName().contains("Zans")) {
                  float[] var51 = var48.getScaledPoints(var7, true);
                  float var52 = var8 / var48.method_28770();
                  Rectangle var6 = new Rectangle(
                     (int)(var51[0] * var48.method_28770() - 2.0F),
                     (int)(var51[1] * var48.method_28770() - 2.0F),
                     (int)(var48.recoveredField3889 * var48.method_28770() + 4.0F),
                     (int)(var48.recoveredField3894 * var48.method_28770() + 4.0F)
                  );
                  int var49;
                  int var50;
                  if (var6.intersects(
                     new Rectangle(
                        var50 = Math.min(this.recoveredField786, var1),
                        var49 = Math.min(this.recoveredField797, var2),
                        Math.max(this.recoveredField786, var1) - var50,
                        Math.max(this.recoveredField797, var2) - var49
                     )
                  )) {
                     float var29 = var1 - var48.getXTranslation();
                     float var28 = var2 - var48.getYTranslation();
                     this.positions.add(new CBModulePosition(var48, var29, var28));
                  }
               }
            }

            this.recoveredField786 = -1;
            this.recoveredField797 = -1;
         }
      }

      if (this.recoveredField788.isMouseInside(var1, var2) && (this.recoveredField795 == null || !this.recoveredField795.isMouseInside(var1, var2))) {
         this.drawHelpMenu(var8);
      }
   }

   public void method_27007(AbstractModule var1) {
      this.positions.removeIf(var1x -> var1x.module == var1);
   }

   public AbstractModule method_27000(ScaledResolution var1, int var2, int var3) {
      for (AbstractModule var5 : this.recoveredField803) {
         if (var5.getGuiAnchor() != null) {
            float[] var6 = var5.getScaledPoints(var1, true);
            boolean var7 = !var5.getSettingsList().isEmpty()
               && var2 >= var6[0] * var5.method_28770()
               && var2 <= (var6[0] + 10.0F) * var5.method_28770()
               && var3 >= (var6[1] + var5.recoveredField3894 - 10.0F) * var5.method_28770()
               && var3 <= (var6[1] + var5.recoveredField3894 + 2.0F) * var5.method_28770();
            boolean var8 = var2 > (var6[0] + var5.recoveredField3889 - 10.0F) * var5.method_28770()
               && var2 < (var6[0] + var5.recoveredField3889 + 2.0F) * var5.method_28770()
               && var3 > (var6[1] + var5.recoveredField3894 - 10.0F) * var5.method_28770()
               && var3 < (var6[1] + var5.recoveredField3894 + 2.0F) * var5.method_28770();
            boolean var9 = !var5.getSettingsList().isEmpty()
               && var2 >= (var6[0] + var5.recoveredField3889 / 2.0F - 10.0F) * var5.method_28770()
               && var2 <= (var6[0] + var5.recoveredField3889 / 2.0F - 2.0F) * var5.method_28770()
               && var3 >= (var6[1] + var5.recoveredField3894 + 2.0F) * var5.method_28770()
               && var3 <= (var6[1] + var5.recoveredField3894 + 12.0F) * var5.method_28770();
            boolean var10 = var2 > (var6[0] + var5.recoveredField3889 / 2.0F + 0.0F) * var5.method_28770()
               && var2 < (var6[0] + var5.recoveredField3889 / 2.0F + 8.0F) * var5.method_28770()
               && var3 > (var6[1] + var5.recoveredField3894 + 2.0F) * var5.method_28770()
               && var3 < (var6[1] + var5.recoveredField3894 + 12.0F) * var5.method_28770();
            if (var8 || var7 || var9 || var10) {
               return var5;
            }
         }
      }

      return null;
   }

   @Override
   public void updateScreen() {
      this.refreshClientUiScale();
      float var1 = this.clientUiRenderScale;
      int var2 = (int)(this.l / var1);
      int var3 = (int)(this.m / var1);
      this.method_26998(var2);
      if (!this.positions.isEmpty()) {
         boolean var4 = Keyboard.isKeyDown(203);
         boolean var5 = Keyboard.isKeyDown(205);
         boolean var6 = Keyboard.isKeyDown(200);
         boolean var7 = Keyboard.isKeyDown(208);
         if (var4 || var5 || var6 || var7) {
            this.recoveredField794++;
            if (this.recoveredField794 > 10) {
               for (CBModulePosition var9 : this.positions) {
                  AbstractModule var10 = var9.module;
                  if (var10 != null) {
                     if (var4) {
                        var10.setTranslations((int)var10.getXTranslation() - 1, (int)var10.getYTranslation());
                     } else if (var5) {
                        var10.setTranslations((int)var10.getXTranslation() + 1, (int)var10.getYTranslation());
                     } else if (var6) {
                        var10.setTranslations((int)var10.getXTranslation(), (int)var10.getYTranslation() - 1);
                     } else if (var7) {
                        var10.setTranslations((int)var10.getXTranslation(), (int)var10.getYTranslation() + 1);
                     }
                  }
               }
            }
         }
      }

      float var11 = this.recoveredField778 > 30.0F ? 2.0F + this.recoveredField778 / 2.0F : 4.0F;
      this.recoveredField778 = this.recoveredField778 + var11 >= 255.0F ? 255.0F : (int)(this.recoveredField778 + var11);
   }

   public CBModulesGui() {
      this.positions = new ArrayList<>();
      this.recoveredField789 = new ArrayList<>();
      this.buttons = new ArrayList<>();
      this.recoveredField795 = null;
      this.currentScrollableElement = null;
      this.recoveredField791 = false;
      this.recoveredField787 = false;
      this.recoveredField778 = 0.0F;
   }

   public void drawHelpMenu(float var1) {
      GL11.glPushMatrix();
      GL11.glTranslatef(4.0F, this.m - 245.0F * var1, 0.0F);
      GL11.glScalef(var1, var1, var1);
      Gui.drawRect(0.0F, 0.0F, 240.0F, 200.0F, -1895825408);
      CheatBreaker.getInstance().recoveredField1589.drawString("Shortcuts & Movement", 4.0F, 2.0F, -1);
      Gui.drawRect(4.0F, 12.0F, 234.0F, 12.5F, 1342177279);
      int var2 = 16;
      String var3 = Minecraft.isRunningOnMac ? "CMD" : "CTRL";
      int var4 = Minecraft.isRunningOnMac ? 2 : 0;
      this.renderRoundButton("Mouse1", 6, var2);
      CheatBreaker.getInstance()
         .playRegular14px
         .drawString("| " + EnumChatFormatting.AQUA + "HOLD" + EnumChatFormatting.RESET + " Add mods to selected region", 80.0F, var2, -1);
      var2 += 12;
      this.renderRoundButton("Mouse1", 6, var2);
      CheatBreaker.getInstance()
         .playRegular14px
         .drawString("| " + EnumChatFormatting.AQUA + "HOLD" + EnumChatFormatting.RESET + " Select & drag mods", 80.0F, var2, -1);
      var2 += 12;
      this.renderRoundButton("Mouse2", 6, var2);
      CheatBreaker.getInstance()
         .playRegular14px
         .drawString("| " + EnumChatFormatting.AQUA + "CLICK" + EnumChatFormatting.RESET + " Reset mod to closest position", 80.0F, var2, -1);
      var2 += 12;
      this.renderRoundButton("Mouse2", 6, var2);
      CheatBreaker.getInstance()
         .playRegular14px
         .drawString("| " + EnumChatFormatting.AQUA + "HOLD" + EnumChatFormatting.RESET + " Don't lock mods while dragging", 80.0F, var2, -1);
      var2 += 12;
      this.renderRoundButton(var3, 6, var2);
      CheatBreaker.getInstance().playRegular14px.drawString("+", 30 - var4, var2, -1);
      this.renderRoundButton("Mouse1", 36 - var4, var2);
      CheatBreaker.getInstance().playRegular14px.drawString("| Toggle (multiple) mod selection", 80.0F, var2, -1);
      var2 += 12;
      this.renderRoundButton(var3, 6, var2);
      CheatBreaker.getInstance().playRegular14px.drawString("+", 30 - var4, var2, -1);
      this.renderRoundButton("A", 36 - var4, var2);
      CheatBreaker.getInstance().playRegular14px.drawString("| Select all mods", 80.0F, var2, -1);
      var2 += 12;
      this.renderRoundButton(var3, 6, var2);
      CheatBreaker.getInstance().playRegular14px.drawString("+", 30 - var4, var2, -1);
      this.renderRoundButton("X", 36 - var4, var2);
      CheatBreaker.getInstance().playRegular14px.drawString("| Remove selected mod(s) from HUD", 80.0F, var2, -1);
      var2 += 12;
      this.renderRoundButton(var3, 6, var2);
      CheatBreaker.getInstance().playRegular14px.drawString("+", 30 - var4, var2, -1);
      this.renderRoundButton("R", 36 - var4, var2);
      CheatBreaker.getInstance().playRegular14px.drawString("| Reset selected mod(s) scale", 80.0F, var2, -1);
      var2 += 12;
      this.renderRoundButton(var3, 6, var2);
      CheatBreaker.getInstance().playRegular14px.drawString("+", 30 - var4, var2, -1);
      this.renderRoundButton("Z", 36 - var4, var2);
      CheatBreaker.getInstance().playRegular14px.drawString("| Undo mod movements", 80.0F, var2, -1);
      var2 += 12;
      this.renderRoundButton(var3, 6, var2);
      CheatBreaker.getInstance().playRegular14px.drawString("+", 30 - var4, var2, -1);
      this.renderRoundButton("Y", 36 - var4, var2);
      CheatBreaker.getInstance().playRegular14px.drawString("| Redo mod movements", 80.0F, var2, -1);
      var2 += 12;
      this.renderRoundButton("SHIFT", 6, var2);
      CheatBreaker.getInstance().playRegular14px.drawString("+", 32.0F, var2, -1);
      this.renderRoundButton("Arrows", 38, var2);
      CheatBreaker.getInstance().playRegular14px.drawString("| Smaller mod movements", 80.0F, var2, -1);
      var2 += 12;
      this.renderRoundButton(var3, 6, var2);
      CheatBreaker.getInstance().playRegular14px.drawString("+", 30 - var4, var2, -1);
      this.renderRoundButton("Arrows", 36 - var4, var2);
      CheatBreaker.getInstance().playRegular14px.drawString("| Larger mod movements", 80.0F, var2, -1);
      int var16 = 172;
      this.renderRoundButton("Up", 31, var16);
      var16 += 12;
      this.renderRoundButton("Left", 6, var16);
      this.renderRoundButton("Down", 26, var16);
      this.renderRoundButton("Right", 51, var16);
      CheatBreaker.getInstance().playRegular14px.drawString("| Move selected mod(s) with precision", 80.0F, var16, -1);
      GL11.glPopMatrix();
   }

   public float getIntersectionFloat(Rectangle var1, Rectangle var2) {
      float var3 = Math.max(Math.abs(var1.x - var2.x) - var2.width / 2, 0);
      float var4 = Math.max(Math.abs(var1.y - var2.y) - var2.height / 2, 0);
      return var3 * var3 + var4 * var4;
   }

   public void method_27009(AbstractModule var1, CBGuiAnchor var2, int var3, int var4, ScaledResolution var5) {
      if (var2 != var1.getGuiAnchor()) {
         float[] var6 = var1.getScaledPoints(var5, true);
         var1.method_28819(var2);
         float[] var7 = var1.getScaledPoints(var5, false);
         var1.setTranslations(var6[0] * var1.method_28770() - var7[0] * var1.method_28770(), var6[1] * var1.method_28770() - var7[1] * var1.method_28770());
      }
   }

   public void method_27013(ScaledResolution var1, int var2, int var3) {
      for (CBModulePosition var5 : this.positions) {
         if (var5.module != null && var5.module.getGuiAnchor() != null) {
            var5.x = var2 - var5.module.getXTranslation();
            var5.y = var3 - var5.module.getYTranslation();
         }
      }
   }

   @Override
   public void initGui() {
      this.method_11296();
      Keyboard.enableRepeatEvents(true);
      this.recoveredField803 = new ArrayList<>();
      this.recoveredField803.addAll(CheatBreaker.getInstance().getModuleManager().recoveredField1706);
      this.recoveredField780 = new ArrayList<>();
      this.recoveredField798 = new ArrayList<>();
      this.recoveredField786 = -1;
      this.recoveredField797 = -1;
      this.recoveredField794 = 0;
      instance = this;
      draggingModule = null;
      this.recoveredField795 = null;
      this.currentScrollableElement = null;
      this.recoveredField790 = null;
      recoveredField784 = false;
      this.clientUiRenderScale = ClientUiScale.getRenderScale(this.j, new ScaledResolution(this.j));
      float var1 = this.clientUiRenderScale;
      int var2 = (int)(this.l / var1);
      int var3 = (int)(this.m / var1);
      this.recoveredField789.clear();
      this.buttons.clear();
      List var4 = CheatBreaker.getInstance().getModuleManager().recoveredField1706;
      List var5 = CheatBreaker.getInstance().getModuleManager().recoveredField1725;
      this.recoveredField801 = new ModulePreviewContainer(var1, var2 / 2 - 565, var3 / 2 + 14, 370, var3 / 2 - 35);
      this.recoveredField789.add(this.recoveredField801);
      this.recoveredField802 = new ModuleListElement(var5, var1, var2 / 2 + 195, var3 / 2 + 14, 370, var3 / 2 - 35);
      this.recoveredField789.add(this.recoveredField802);
      this.recoveredField793 = new ModuleListElement(var4, var1, var2 / 2 + 195, var3 / 2 + 14, 370, var3 / 2 - 35);
      this.recoveredField789.add(this.recoveredField793);
      this.recoveredField796 = new ProfilesListElement(var1, var2 / 2 - 565, var3 / 2 + 14, 370, var3 / 2 - 35);
      this.recoveredField789.add(this.recoveredField796);
      this.recoveredField792 = new ModulesGuiButtonElement(null, "eye-64.png", 4, var3 - 32, 28, 28, -12418828, var1);
      this.recoveredField788 = new ModulesGuiButtonElement(null, "?", 36, var3 - 32, 28, 28, -12418828, var1);
      this.cosmeticsButton = new ModulesGuiButtonElement(null, "cosmetics-64.png", 68, var3 - 32, 28, 28, -12418828, var1);
      if (CheatBreaker.getInstance().getConfigManager().method_25096()) {
         this.buttons.add(new ModulesGuiButtonElement(this.recoveredField802, "Staff Mods", var2 / 2 - 50, var3 / 2 - 44, 100, 20, -9442858, var1));
      }

      this.buttons.add(new ModulesGuiButtonElement(this.recoveredField801, "Mods", var2 / 2 - 50, var3 / 2 - 19, 100, 28, -13916106, var1));
      this.buttons.add(new ModulesGuiButtonElement(this.recoveredField793, "cog-64.png", var2 / 2 + 54, var3 / 2 - 19, 28, 28, -12418828, var1));
      this.buttons.add(new ModulesGuiButtonElement(this.recoveredField796, "profiles-64.png", var2 / 2 - 82, var3 / 2 - 19, 28, 28, -12418828, var1));
      recoveredField784 = false;
      this.recoveredField795 = null;
      this.recoveredField778 = 5.0F;
   }

   @Override
   public void mouseClicked(int var1, int var2, int var3) throws java.io.IOException {
      if (var3 == 0 && draggingModule == null && this.cosmeticsButton.isMouseInside(var1, var2)
         && (this.recoveredField795 == null || !this.recoveredField795.isMouseInside(var1, var2))) {
         this.j.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
         this.j.displayGuiScreen(new com.cheatbreaker.client.ui.mainmenu.CosmeticsMenu(this));
         return;
      }
      ScaledResolution var4 = new ScaledResolution(this.j);
      if (this.recoveredField795 != null && this.recoveredField795.isMouseInside(var1, var2)) {
         this.recoveredField795.handleMouseClick(var1, var2, var3);
      } else {
         AbstractModule var5 = this.method_27000(var4, var1, var2);
         if ((draggingModule == null || !this.recoveredField791) && var5 != null) {
            float[] var6 = var5.getScaledPoints(var4, true);
            boolean var7 = var5.recoveredField3889 < 22.0F || var5.recoveredField3894 < 8.0F;
            boolean var8 = !var7
               && var5.isEnabled()
               && !var5.getSettingsList().isEmpty()
               && var1 >= var6[0] * var5.method_28770()
               && var1 <= (var6[0] + 10.0F) * var5.method_28770()
               && var2 >= (var6[1] + var5.recoveredField3894 - 10.0F) * var5.method_28770()
               && var2 <= (var6[1] + var5.recoveredField3894 + 2.0F) * var5.method_28770();
            boolean var9 = !var7
               && var5.isEnabled()
               && var1 > (var6[0] + var5.recoveredField3889 - 10.0F) * var5.method_28770()
               && var1 < (var6[0] + var5.recoveredField3889 + 2.0F) * var5.method_28770()
               && var2 > (var6[1] + var5.recoveredField3894 - 10.0F) * var5.method_28770()
               && var2 < (var6[1] + var5.recoveredField3894 + 2.0F) * var5.method_28770();
            boolean var10 = var7
               && var5.isEnabled()
               && !var5.getSettingsList().isEmpty()
               && var1 >= (var6[0] + var5.recoveredField3889 / 2.0F - 10.0F) * var5.method_28770()
               && var1 <= (var6[0] + var5.recoveredField3889 / 2.0F - 2.0F) * var5.method_28770()
               && var2 >= (var6[1] + var5.recoveredField3894 + 2.0F) * var5.method_28770()
               && var2 <= (var6[1] + var5.recoveredField3894 + 12.0F) * var5.method_28770();
            boolean var11 = var7
               && var5.isEnabled()
               && var1 > (var6[0] + var5.recoveredField3889 / 2.0F + 0.0F) * var5.method_28770()
               && var1 < (var6[0] + var5.recoveredField3889 / 2.0F + 8.0F) * var5.method_28770()
               && var2 > (var6[1] + var5.recoveredField3894 + 2.0F) * var5.method_28770()
               && var2 < (var6[1] + var5.recoveredField3894 + 12.0F) * var5.method_28770();
            if (var8 || var10) {
               Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
               ((ModuleListElement)this.recoveredField793).recoveredField1358 = false;
               ((ModuleListElement)this.recoveredField793).module = var5;
               this.currentScrollableElement = this.recoveredField793;
            } else if (var9 || var11) {
               Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
               var5.setState(false);
            }

            if (!var7) {
               return;
            }
         }

         for (AbstractModule var20 : this.recoveredField803) {
            if (var20.getGuiAnchor() != null && var20.isEnabled() && var20 != CheatBreaker.getInstance().getModuleManager().minmap) {
               float[] var24 = var20.getScaledPoints(var4, true);
               boolean var25 = var1 > var24[0] * var20.method_28770()
                  && var1 < (var24[0] + var20.recoveredField3889) * var20.method_28770()
                  && var2 > var24[1] * var20.method_28770()
                  && var2 < (var24[1] + var20.recoveredField3894) * var20.method_28770();
               boolean var12 = this.recoveredField790 != null
                     && this.recoveredField790.recoveredField3333 == var20
                     && this.recoveredField790.recoveredField3340 == SomeRandomAssEnum.LEFT_BOTTOM
                  || !var25
                     && var1 >= (var24[0] + var20.recoveredField3889 - 5.0F) * var20.method_28770()
                     && var1 <= (var24[0] + var20.recoveredField3889 + 5.0F) * var20.method_28770()
                     && var2 >= (var24[1] - 5.0F) * var20.method_28770()
                     && var2 <= (var24[1] + 5.0F) * var20.method_28770();
               boolean var13 = this.recoveredField790 != null
                     && this.recoveredField790.recoveredField3333 == var20
                     && this.recoveredField790.recoveredField3340 == SomeRandomAssEnum.RIGHT_TOP
                  || !var25
                     && var1 >= (var24[0] - 5.0F) * var20.method_28770()
                     && var1 <= (var24[0] + 5.0F) * var20.method_28770()
                     && var2 >= (var24[1] + var20.recoveredField3894 - 5.0F) * var20.method_28770()
                     && var2 <= (var24[1] + var20.recoveredField3894 + 5.0F) * var20.method_28770();
               boolean var14 = this.recoveredField790 != null
                     && this.recoveredField790.recoveredField3333 == var20
                     && this.recoveredField790.recoveredField3340 == SomeRandomAssEnum.RIGHT_BOTTOM
                  || !var25
                     && var1 >= (var24[0] - 5.0F) * var20.method_28770()
                     && var1 <= (var24[0] + 5.0F) * var20.method_28770()
                     && var2 >= (var24[1] - 5.0F) * var20.method_28770()
                     && var2 <= (var24[1] + 5.0F) * var20.method_28770();
               boolean var15 = this.recoveredField790 != null
                     && this.recoveredField790.recoveredField3333 == var20
                     && this.recoveredField790.recoveredField3340 == SomeRandomAssEnum.LEFT_TOP
                  || !var25
                     && var1 >= (var24[0] + var20.recoveredField3889 - 5.0F) * var20.method_28770()
                     && var1 <= (var24[0] + var20.recoveredField3889 + 5.0F) * var20.method_28770()
                     && var2 >= (var24[1] + var20.recoveredField3894 - 5.0F) * var20.method_28770()
                     && var2 <= (var24[1] + var20.recoveredField3894 + 5.0F) * var20.method_28770();
               if (this.recoveredField786 == -1 && (var12 || var13 || var14 || var15)) {
                  CBGuiAnchor var22;
                  SomeRandomAssEnum var23;
                  if (var12) {
                     var23 = SomeRandomAssEnum.LEFT_BOTTOM;
                     var22 = CBGuiAnchor.LEFT_BOTTOM;
                  } else if (var13) {
                     var23 = SomeRandomAssEnum.RIGHT_TOP;
                     var22 = CBGuiAnchor.RIGHT_TOP;
                  } else if (var14) {
                     var23 = SomeRandomAssEnum.RIGHT_BOTTOM;
                     var22 = CBGuiAnchor.RIGHT_BOTTOM;
                  } else {
                     var23 = SomeRandomAssEnum.LEFT_TOP;
                     var22 = CBGuiAnchor.LEFT_TOP;
                  }

                  if (!this.method_26995(var4, var1, var2)) {
                     if (var3 == 0) {
                        this.recoveredField780.add(new ModuleGroupPositionSnapshot(this, this.positions));
                        this.recoveredField790 = new ModuleResizeSnapshot(this, var20, var23, var1, var2);
                        this.method_27009(var20, var22, var1, var2, var4);
                     } else if (var3 == 1) {
                        CBGuiAnchor var16 = var20.getGuiAnchor();
                        this.method_27009(var20, var22, var1, var2, var4);
                        var20.recoveredField3895.setValue(1.0F);
                        this.method_27009(var20, var16, var1, var2, var4);
                     }

                     return;
                  }
               }
            }
         }

         if (draggingModule == null) {
            if (this.recoveredField792.isMouseInside(var1, var2)) {
               Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
               this.recoveredField787 = !this.recoveredField787;
            }

            this.method_26994(var1, var2, var3);
            this.method_27001(var4, var1, var2, var3);
         }

         for (AbstractModulesGuiElement var21 : this.buttons) {
            if (var21.isMouseInside(var1, var2)) {
               return;
            }
         }

         boolean var19 = this.method_26995(var4, var1, var2);
         if (var19) {
            return;
         }

         if (!this.positions.isEmpty()) {
            this.positions.clear();
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
         }

         this.recoveredField786 = var1;
         this.recoveredField797 = var2;
      }

      if (!this.positions.isEmpty()) {
         this.recoveredField794 = 0;
      }
   }

   public boolean method_26997(float var1, AbstractModule var2, ScaledResolution var3, int var4, int var5, boolean var6) {
      int var10 = 0;
      if (var2.getGuiAnchor() != null
         && var2.isEnabled()
         && var2 != CheatBreaker.getInstance().getModuleManager().minmap
         && (var2.recoveredField3912 || var2.method_28866())) {
         boolean var12 = false;
         GL11.glPushMatrix();
         float[] var13 = var2.getScaledPoints(var3, true);
         var2.scaleAndTranslate(var3);
         boolean var14 = this.recoveredField786 != -1;
         if (var14) {
            Rectangle var15 = new Rectangle(
               (int)(var13[0] * var2.method_28770() - 2.0F),
               (int)(var13[1] * var2.method_28770() - 2.0F),
               (int)(var2.recoveredField3889 * var2.method_28770() + 4.0F),
               (int)(var2.recoveredField3894 * var2.method_28770() + 4.0F)
            );
            var10 = Math.min(this.recoveredField786, var4);
            int var9 = Math.min(this.recoveredField797, var5);
            int var8 = Math.max(this.recoveredField786, var4) - var10;
            int var7 = Math.max(this.recoveredField797, var5) - var9;
            Rectangle var16 = new Rectangle(var10, var9, var8, var7);
            var14 = var15.intersects(var16);
         }

         float[] var11 = var2.getScaledPoints(var3, true);
         boolean var29 = var2.recoveredField3889 < 22.0F || var2.recoveredField3894 < 8.0F;
         boolean var30 = var4 > var11[0] * var2.method_28770()
            && var4 < (var11[0] + var2.recoveredField3889) * var2.method_28770()
            && var5 > var11[1] * var2.method_28770()
            && var5 < (var11[1] + var2.recoveredField3894) * var2.method_28770();
         boolean var17 = var4 > var11[0] * var2.method_28770()
            && var4 < (var11[0] + var2.recoveredField3889) * var2.method_28770()
            && var5 > var11[1] * var2.method_28770()
            && var5 < (var11[1] + var2.recoveredField3894 + 10.0F) * var2.method_28770();
         if (!this.recoveredField787) {
            if (this.getModulePosition(var2) == null && !var14) {
               Gui.method_00887(
                  0.0F, 0.0F, var2.recoveredField3889, var2.recoveredField3894, 0.4F, var30 ? -2130706433 : 1879048191, var30 ? 989855743 : 452984831
               );
            } else {
               Gui.method_00887(0.0F, 0.0F, var2.recoveredField3889, var2.recoveredField3894, 0.4F, -1627324417, var30 ? 721420287 : 452984831);
            }
         }

         if (!this.recoveredField787 && (var30 || var29 && var17)) {
            boolean var28 = !var2.getSettingsList().isEmpty()
               && var4 >= (var11[0] + 2.0F) * var2.method_28770()
               && var4 <= (var11[0] + 10.0F) * var2.method_28770()
               && var5 >= (var11[1] + var2.recoveredField3894 - 8.0F) * var2.method_28770()
               && var5 <= (var11[1] + var2.recoveredField3894 - 2.0F) * var2.method_28770();
            boolean var25 = var4 > (var11[0] + var2.recoveredField3889 - 10.0F) * var2.method_28770()
               && var4 < (var11[0] + var2.recoveredField3889 - 2.0F) * var2.method_28770()
               && var5 > (var11[1] + var2.recoveredField3894 - 8.0F) * var2.method_28770()
               && var5 < (var11[1] + var2.recoveredField3894 - 2.0F) * var2.method_28770();
            boolean var18 = !var2.getSettingsList().isEmpty()
               && var4 >= (var11[0] + var2.recoveredField3889 / 2.0F - 10.0F) * var2.method_28770()
               && var4 <= (var11[0] + var2.recoveredField3889 / 2.0F - 2.0F) * var2.method_28770()
               && var5 >= (var11[1] + var2.recoveredField3894 + 2.0F) * var2.method_28770()
               && var5 <= (var11[1] + var2.recoveredField3894 + 10.0F) * var2.method_28770();
            boolean var19 = var4 > (var11[0] + var2.recoveredField3889 / 2.0F + 0.0F) * var2.method_28770()
               && var4 < (var11[0] + var2.recoveredField3889 / 2.0F + 8.0F) * var2.method_28770()
               && var5 > (var11[1] + var2.recoveredField3894 + 2.0F) * var2.method_28770()
               && var5 < (var11[1] + var2.recoveredField3894 + 10.0F) * var2.method_28770();
            float var20 = var29 ? var2.recoveredField3889 / 2.0F - 7.0F : 2.0F;
            float var21 = var29 ? var2.recoveredField3889 / 2.0F + 1.0F : var2.recoveredField3889 - 8.0F;
            float var22 = var29 ? var2.recoveredField3894 + 2.0F : var2.recoveredField3894 - 7.5F;
            if (!var2.getSettingsList().isEmpty()) {
               GL11.glColor4f(1.0F, 1.0F, 1.0F, (var29 ? !var18 : !var28) ? 0.6F : 1.0F);
               RenderUtil.drawIcon(this.recoveredField779, 3.0F, var20, var22);
            }

            GL11.glColor4f(0.8F, 0.2F, 0.2F, (var29 ? !var19 : !var25) ? 0.6F : 1.0F);
            RenderUtil.drawIcon(this.recoveredField799, 3.0F, var21, var22);
         }

         GL11.glPushMatrix();
         float var31 = var1 / var2.method_28770();
         GL11.glScalef(var31, var31, var31);
         if (var6) {
            boolean var26 = this.recoveredField790 != null
                  && this.recoveredField790.recoveredField3333 == var2
                  && this.recoveredField790.recoveredField3340 == SomeRandomAssEnum.LEFT_BOTTOM
               || var10 == 0
                  && var4 >= (var11[0] + var2.recoveredField3889 - 5.0F) * var2.method_28770()
                  && var4 <= (var11[0] + var2.recoveredField3889 + 5.0F) * var2.method_28770()
                  && var5 >= (var11[1] - 5.0F) * var2.method_28770()
                  && var5 <= (var11[1] + 5.0F) * var2.method_28770();
            boolean var24 = this.recoveredField790 != null
                  && this.recoveredField790.recoveredField3333 == var2
                  && this.recoveredField790.recoveredField3340 == SomeRandomAssEnum.RIGHT_TOP
               || var10 == 0
                  && var4 >= (var11[0] - 5.0F) * var2.method_28770()
                  && var4 <= (var11[0] + 5.0F) * var2.method_28770()
                  && var5 >= (var11[1] + var2.recoveredField3894 - 5.0F) * var2.method_28770()
                  && var5 <= (var11[1] + var2.recoveredField3894 + 5.0F) * var2.method_28770();
            boolean var32 = this.recoveredField790 != null
                  && this.recoveredField790.recoveredField3333 == var2
                  && this.recoveredField790.recoveredField3340 == SomeRandomAssEnum.RIGHT_BOTTOM
               || var10 == 0
                  && var4 >= (var11[0] - 5.0F) * var2.method_28770()
                  && var4 <= (var11[0] + 5.0F) * var2.method_28770()
                  && var5 >= (var11[1] - 5.0F) * var2.method_28770()
                  && var5 <= (var11[1] + 5.0F) * var2.method_28770();
            boolean var34 = this.recoveredField790 != null
                  && this.recoveredField790.recoveredField3333 == var2
                  && this.recoveredField790.recoveredField3340 == SomeRandomAssEnum.LEFT_TOP
               || var10 == 0
                  && var4 >= (var11[0] + var2.recoveredField3889 - 5.0F) * var2.method_28770()
                  && var4 <= (var11[0] + var2.recoveredField3889 + 5.0F) * var2.method_28770()
                  && var5 >= (var11[1] + var2.recoveredField3894 - 5.0F) * var2.method_28770()
                  && var5 <= (var11[1] + var2.recoveredField3894 + 5.0F) * var2.method_28770();
            GL11.glPushMatrix();
            float var36 = 4.0F;
            if (this.recoveredField786 == -1 && var32) {
               GL11.glTranslatef(0.0F, 0.0F, 0.0F);
               Gui.drawRect(-var36 / 2.0F, -var36 / 2.0F, var36 / 2.0F, var36 / 2.0F, -16711936);
            } else if (this.recoveredField786 == -1 && var26) {
               GL11.glTranslatef(var2.recoveredField3889 / var31, 0.0F, 0.0F);
               Gui.drawRect(-var36 / 2.0F, -var36 / 2.0F, var36 / 2.0F, var36 / 2.0F, -16711936);
            } else if (this.recoveredField786 == -1 && var34) {
               GL11.glTranslatef(var2.recoveredField3889 / var31, var2.recoveredField3894 / var31, 0.0F);
               Gui.drawRect(-var36 / 2.0F, -var36 / 2.0F, var36 / 2.0F, var36 / 2.0F, -16711936);
            } else if (this.recoveredField786 == -1 && var24) {
               GL11.glTranslatef(0.0F, var2.recoveredField3894 / var31, 0.0F);
               Gui.drawRect(-var36 / 2.0F, -var36 / 2.0F, var36 / 2.0F, var36 / 2.0F, -16711936);
            }

            GL11.glPopMatrix();
            var12 = this.recoveredField786 == -1 && (var32 || var26 || var24 || var34);
         }

         boolean var27 = var13[1] - CheatBreaker.getInstance().recoveredField1589.getHeight() - 6.0F < 0.0F;
         float var33 = var27 ? var2.recoveredField3894 * var2.method_28770() / var1 : -CheatBreaker.getInstance().recoveredField1589.getHeight() - 4;
         if ((Boolean)CheatBreaker.getInstance().getGlobalSettings().recoveredField559.getValue()) {
            String var35 = var2.getName() + (var2.recoveredField3905 ? EnumChatFormatting.GRAY + " (Hidden)" : "");
            switch (var2.getPosition()) {
               case LEFT:
                  float var37 = 0.0F;
                  CheatBreaker.getInstance().recoveredField1589.drawStringWithShadow(var2.getName(), var37, var33, -1);
                  break;
               case CENTER:
                  float var38 = var2.recoveredField3889 * var2.method_28770() / var1 / 2.0F;
                  CheatBreaker.getInstance().recoveredField1589.method_03191(var2.getName(), var38, var33, -1);
                  break;
               case RIGHT:
                  float var23 = var2.recoveredField3889 * var2.method_28770() / var1
                     - CheatBreaker.getInstance().recoveredField1589.getStringWidth(var2.getName());
                  CheatBreaker.getInstance().recoveredField1589.drawStringWithShadow(var2.getName(), var23, var33, -1);
            }
         }

         GL11.glPopMatrix();
         GL11.glPopMatrix();
         return !var12;
      } else {
         return true;
      }
   }

   @Override
   public void handleMouseInput() throws java.io.IOException {
      super.handleMouseInput();
      int var1 = Mouse.getEventDWheel();
      if (this.recoveredField795 != null) {
         this.recoveredField795.onScroll(var1);
      }
   }

   public float method_27015(AbstractModule var1, float var2, float[] var3, int var4) {
      float var5 = var2;
      float var6 = 2.0F;
      if (var2 + var3[1] * var1.method_28770() < var6) {
         var5 = -var3[1] * var1.method_28770() + var6;
      } else if (var2 + var3[1] * var1.method_28770() + var4 > this.m - var6) {
         var5 = this.m - var3[1] * var1.method_28770() - var4 - var6;
      }

      return var5;
   }

   public boolean method_26995(ScaledResolution var1, int var2, int var3) {
      boolean var4 = false;

      for (AbstractModule var6 : this.recoveredField803) {
         if (var6.getGuiAnchor() != null) {
            float[] var7 = var6.getScaledPoints(var1, true);
            boolean var8 = var2 > var7[0] * var6.method_28770()
               && var2 < (var7[0] + var6.recoveredField3889) * var6.method_28770()
               && var3 > var7[1] * var6.method_28770()
               && var3 < (var7[1] + var6.recoveredField3894) * var6.method_28770();
            var4 = var4 || var8;
         }
      }

      return var4;
   }

   public void method_26994(int var1, int var2, int var3) {
      for (ModulesGuiButtonElement var5 : this.buttons) {
         if (var3 == 0 && var5.isMouseInside(var1, var2) && !recoveredField784) {
            if (var5.recoveredField2846 != null && this.recoveredField795 != var5.recoveredField2846 && this.currentScrollableElement == null) {
               Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
               this.currentScrollableElement = var5.recoveredField2846;
            } else if (var5.recoveredField2846 != null && this.currentScrollableElement == null) {
               Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
               recoveredField784 = true;
            }
         }
      }
   }

   public CBModulePosition getModulePosition(AbstractModule var1) {
      for (CBModulePosition var3 : this.positions) {
         if (var1 == var3.module) {
            return var3;
         }
      }

      return null;
   }

   public void method_26998(int var1) {
      if (recoveredField784) {
         if (this.recoveredField795 != null) {
            this.method_27003(this.recoveredField795, true, var1);
         }
      } else if (this.currentScrollableElement != null) {
         if (this.recoveredField795 != null) {
            this.method_27003(this.recoveredField795, true, var1);
         }

         this.method_27003(this.currentScrollableElement, false, var1);
      }
   }

   private void refreshClientUiScale() {
      float scale = ClientUiScale.getRenderScale(this.j, new ScaledResolution(this.j));
      if (scale == this.clientUiRenderScale) {
         return;
      }
      int current = this.recoveredField789.indexOf(this.currentScrollableElement);
      int open = this.recoveredField789.indexOf(this.recoveredField795);
      List<AbstractScrollableElement> oldPanels = new ArrayList<>(this.recoveredField789);
      boolean closing = recoveredField784;
      float fade = this.recoveredField778;
      this.initGui();
      for (int index = 0; index < oldPanels.size(); index++) {
         AbstractScrollableElement oldPanel = oldPanels.get(index);
         AbstractScrollableElement newPanel = this.recoveredField789.get(index);
         newPanel.recoveredField3012 = oldPanel.recoveredField3012;
         newPanel.recoveredField3010 = oldPanel.recoveredField3010;
         if (oldPanel instanceof ModuleListElement && newPanel instanceof ModuleListElement) {
            ((ModuleListElement)newPanel).module = ((ModuleListElement)oldPanel).module;
            ((ModuleListElement)newPanel).recoveredField1358 = ((ModuleListElement)oldPanel).recoveredField1358;
         }
      }
      if (current >= 0) this.currentScrollableElement = this.recoveredField789.get(current);
      if (open >= 0) {
         this.recoveredField795 = this.recoveredField789.get(open);
         this.recoveredField795.x = (int)(this.l / scale) / 2 - 185;
      }
      recoveredField784 = closing;
      this.recoveredField778 = fade;
   }
}
