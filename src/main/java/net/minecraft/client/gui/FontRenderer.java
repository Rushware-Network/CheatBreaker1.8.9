package net.minecraft.client.gui;

import com.cheatbreaker.client.CheatBreaker;
import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;
import com.ibm.icu.text.Bidi;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.src.Config;
import net.minecraft.util.ResourceLocation;
import net.optifine.CustomColors;
import net.optifine.render.GlBlendState;
import net.optifine.util.FontUtils;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.GL11;

public class FontRenderer implements IResourceManagerReloadListener {
   public int[] colorCode;
   public int textColor;
   public boolean randomStyle;
   public float recoveredField2546;
   public static ResourceLocation[] unicodePageLocations = new ResourceLocation[256];
   public ResourceLocation locationFontTextureBase;
   public boolean unicodeFlag;
   public float posY;
   public ResourceLocation locationFontTexture;
   public Random fontRandom;
   public int FONT_HEIGHT;
   public float recoveredField2547;
   public TextureManager renderEngine;
   public boolean underlineStyle;
   public boolean strikethroughStyle;
   public float recoveredField2548;
   public boolean blend;
   public float recoveredField2549;
   public float offsetBold;
   public boolean boldStyle;
   public boolean bidiFlag;
   public GameSettings gameSettings;
   public int[] charWidth = new int[256];
   public float[] charWidthFloat;
   public byte[] glyphWidth;
   public boolean italicStyle;
   public float posX;
   private float posZ;
   public GlBlendState oldBlendState;

   public List<String> listFormattedStringToWidth(String var1, int var2) {
      return Arrays.asList(this.wrapFormattedStringToWidth(var1, var2).split("\n"));
   }

   public String bidiReorder(String var1) {
      try {
         Bidi var2 = new Bidi(new ArabicShaping(8).shape(var1), 127);
         var2.setReorderingMode(0);
         return var2.writeReordered(2);
      } catch (ArabicShapingException var3) {
         return var1;
      }
   }

   public void readGlyphSizes() {
      InputStream var1 = null;

      try {
         var1 = this.getResourceInputStream(new ResourceLocation("font/glyph_sizes.bin"));
         var1.read(this.glyphWidth);
      } catch (IOException var6) {
         throw new RuntimeException(var6);
      } finally {
         IOUtils.closeQuietly(var1);
      }
   }

   public float getCharWidthFloat(char var1) {
      if (var1 == 167) {
         return -1.0F;
      } else if (var1 != ' ' && var1 != 160) {
         int var2 = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000"
            .indexOf(var1);
         if (var1 > 0 && var2 != -1 && !this.unicodeFlag) {
            return this.charWidthFloat[var2];
         } else if (this.glyphWidth[var1] != 0) {
            int var3 = this.glyphWidth[var1] >>> 4;
            int var4 = this.glyphWidth[var1] & 15;
            if (var4 > 7) {
               var4 = 15;
               var3 = 0;
            }

            var4++;
            return (var4 - var3) / 2 + 1;
         } else {
            return 0.0F;
         }
      } else {
         return this.charWidthFloat[32];
      }
   }

   public static String getFormatFromString(String var0) {
      String var1 = "";
      int var2 = -1;
      int var3 = var0.length();

      while ((var2 = var0.indexOf(167, var2 + 1)) != -1) {
         if (var2 < var3 - 1) {
            char var4 = var0.charAt(var2 + 1);
            if (isFormatColor(var4)) {
               var1 = "§" + var4;
            } else if (isFormatSpecial(var4)) {
               var1 = var1 + "§" + var4;
            }
         }
      }

      return var1;
   }

   public void setUnicodeFlag(boolean var1) {
      this.unicodeFlag = var1;
   }

   public int drawString(String var1, int var2, int var3, int var4) {
      return this.drawString(var1, var2, var3, var4, false);
   }

   public int drawString(String var1, float var2, float var3, int var4, boolean var5) {
      return this.drawString(var1, var2, var3, var4, var5, 0.0F);
   }

   public int drawString(String var1, float var2, float var3, int var4, boolean var5, float shadowDepth) {
      this.enableAlpha();
      if (this.blend) {
         GlStateManager.getBlendState(this.oldBlendState);
         GlStateManager.enableBlend();
         GlStateManager.blendFunc(770, 771);
      }

      this.resetStyles();
      if (CheatBreaker.recoveredField1597) {
         var4 = -12058369;
      }

      int var7;
      if (var5) {
         float previousZ = this.posZ;
         this.posZ = shadowDepth;
         try {
            var7 = this.renderString(var1, var2 + 1.0F, var3 + 1.0F, var4, true);
         } finally {
            this.posZ = previousZ;
         }
         var7 = Math.max(var7, this.renderString(var1, var2, var3, var4, false));
      } else {
         var7 = this.renderString(var1, var2, var3, var4, false);
      }

      if (this.blend) {
         GlStateManager.setBlendState(this.oldBlendState);
      }

      return var7;
   }

   public void setColor(float var1, float var2, float var3, float var4) {
      GlStateManager.color(var1, var2, var3, var4);
   }

   public int getCharWidth(char var1) {
      return Math.round(this.getCharWidthFloat(var1));
   }

   public void bindTexture(ResourceLocation var1) {
      this.renderEngine.bindTexture(var1);
   }

   @Override
   public void onResourceManagerReload(IResourceManager var1) {
      this.locationFontTexture = FontUtils.getHdFontLocation(this.locationFontTextureBase);

      for (int var2 = 0; var2 < unicodePageLocations.length; var2++) {
         unicodePageLocations[var2] = null;
      }

      this.readFontTexture();
      this.readGlyphSizes();
   }

   public void drawSplitString(String var1, int var2, int var3, int var4, int var5) {
      if (this.blend) {
         GlStateManager.getBlendState(this.oldBlendState);
         GlStateManager.enableBlend();
         GlStateManager.blendFunc(770, 771);
      }

      this.resetStyles();
      this.textColor = var5;
      var1 = this.trimStringNewline(var1);
      this.renderSplitString(var1, var2, var3, var4, false);
      if (this.blend) {
         GlStateManager.setBlendState(this.oldBlendState);
      }
   }

   public String trimStringToWidth(String var1, int var2) {
      return this.trimStringToWidth(var1, var2, false);
   }

   public String trimStringToWidth(String var1, int var2, boolean var3) {
      StringBuilder var4 = new StringBuilder();
      float var5 = 0.0F;
      int var6 = var3 ? var1.length() - 1 : 0;
      int var7 = var3 ? -1 : 1;
      boolean var8 = false;
      boolean var9 = false;

      for (int var10 = var6; var10 >= 0 && var10 < var1.length() && var5 < var2; var10 += var7) {
         char var11 = var1.charAt(var10);
         float var12 = this.getCharWidthFloat(var11);
         if (var8) {
            var8 = false;
            if (var11 == 'l' || var11 == 'L') {
               var9 = true;
            } else if (var11 == 'r' || var11 == 'R') {
               var9 = false;
            }
         } else if (var12 < 0.0F) {
            var8 = true;
         } else {
            var5 += var12;
            if (var9) {
               var5++;
            }
         }

         if (var5 > var2) {
            break;
         }

         if (var3) {
            var4.insert(0, var11);
         } else {
            var4.append(var11);
         }
      }

      return var4.toString();
   }

   public void doDraw(float var1) {
      if (this.strikethroughStyle) {
         Tessellator var2 = Tessellator.getInstance();
         WorldRenderer var3 = var2.getWorldRenderer();
         GlStateManager.disableTexture2D();
         var3.begin(7, DefaultVertexFormats.POSITION);
         var3.pos(this.posX, this.posY + this.FONT_HEIGHT / 2, this.posZ).endVertex();
         var3.pos(this.posX + var1, this.posY + this.FONT_HEIGHT / 2, this.posZ).endVertex();
         var3.pos(this.posX + var1, this.posY + this.FONT_HEIGHT / 2 - 1.0F, this.posZ).endVertex();
         var3.pos(this.posX, this.posY + this.FONT_HEIGHT / 2 - 1.0F, this.posZ).endVertex();
         var2.draw();
         GlStateManager.enableTexture2D();
      }

      if (this.underlineStyle) {
         Tessellator var5 = Tessellator.getInstance();
         WorldRenderer var6 = var5.getWorldRenderer();
         GlStateManager.disableTexture2D();
         var6.begin(7, DefaultVertexFormats.POSITION);
         int var4 = this.underlineStyle ? -1 : 0;
         var6.pos(this.posX + var4, this.posY + this.FONT_HEIGHT, this.posZ).endVertex();
         var6.pos(this.posX + var1, this.posY + this.FONT_HEIGHT, this.posZ).endVertex();
         var6.pos(this.posX + var1, this.posY + this.FONT_HEIGHT - 1.0F, this.posZ).endVertex();
         var6.pos(this.posX + var4, this.posY + this.FONT_HEIGHT - 1.0F, this.posZ).endVertex();
         var5.draw();
         GlStateManager.enableTexture2D();
      }

      this.posX += var1;
   }

   public static boolean isFormatSpecial(char var0) {
      return var0 >= 'k' && var0 <= 'o' || var0 >= 'K' && var0 <= 'O' || var0 == 'r' || var0 == 'R';
   }

   public void readFontTexture() {
      BufferedImage var1;
      try {
         var1 = TextureUtil.readBufferedImage(this.getResourceInputStream(this.locationFontTexture));
      } catch (IOException var21) {
         throw new RuntimeException(var21);
      }

      Properties var2 = FontUtils.readFontProperties(this.locationFontTexture);
      this.blend = FontUtils.readBoolean(var2, "blend", false);
      int var3 = var1.getWidth();
      int var4 = var1.getHeight();
      int var5 = var3 / 16;
      int var6 = var4 / 16;
      float var7 = var3 / 128.0F;
      float var8 = Config.limit(var7, 1.0F, 2.0F);
      this.offsetBold = 1.0F / var8;
      float var9 = FontUtils.readFloat(var2, "offsetBold", -1.0F);
      if (var9 >= 0.0F) {
         this.offsetBold = var9;
      }

      int[] var10 = new int[var3 * var4];
      var1.getRGB(0, 0, var3, var4, var10, 0, var3);

      for (int var11 = 0; var11 < 256; var11++) {
         int var12 = var11 % 16;
         int var13 = var11 / 16;
         int var14 = 0;

         for (var14 = var5 - 1; var14 >= 0; var14--) {
            int var15 = var12 * var5 + var14;
            boolean var16 = true;

            for (int var17 = 0; var17 < var6 && var16; var17++) {
               int var18 = (var13 * var6 + var17) * var3;
               int var19 = var10[var15 + var18];
               int var20 = var19 >> 24 & 0xFF;
               if (var20 > 16) {
                  var16 = false;
               }
            }

            if (!var16) {
               break;
            }
         }

         if (var11 == 65) {
            var11 = var11;
         }

         if (var11 == 32) {
            if (var5 <= 8) {
               var14 = (int)(2.0F * var7);
            } else {
               var14 = (int)(1.5F * var7);
            }
         }

         this.charWidthFloat[var11] = (var14 + 1) / var7 + 1.0F;
      }

      FontUtils.readCustomCharWidths(var2, this.charWidthFloat);

      for (int var22 = 0; var22 < this.charWidth.length; var22++) {
         this.charWidth[var22] = Math.round(this.charWidthFloat[var22]);
      }
   }

   public boolean getBidiFlag() {
      return this.bidiFlag;
   }

   public String wrapFormattedStringToWidth(String var1, int var2) {
      if (var1.length() <= 1) {
         return var1;
      } else {
         int var3 = this.sizeStringToWidth(var1, var2);
         if (var1.length() <= var3) {
            return var1;
         } else {
            String var4 = var1.substring(0, var3);
            char var5 = var1.charAt(var3);
            boolean var6 = var5 == ' ' || var5 == '\n';
            String var7 = getFormatFromString(var4) + var1.substring(var3 + (var6 ? 1 : 0));
            return var4 + "\n" + this.wrapFormattedStringToWidth(var7, var2);
         }
      }
   }

   public FontRenderer(GameSettings var1, ResourceLocation var2, TextureManager var3, boolean var4) {
      this.FONT_HEIGHT = 9;
      this.fontRandom = new Random();
      this.glyphWidth = new byte[65536];
      this.colorCode = new int[32];
      this.offsetBold = 1.0F;
      this.charWidthFloat = new float[256];
      this.blend = false;
      this.oldBlendState = new GlBlendState();
      this.gameSettings = var1;
      this.locationFontTextureBase = var2;
      this.locationFontTexture = var2;
      this.renderEngine = var3;
      this.unicodeFlag = var4;
      this.locationFontTexture = FontUtils.getHdFontLocation(this.locationFontTextureBase);
      this.bindTexture(this.locationFontTexture);

      for (int var5 = 0; var5 < 32; var5++) {
         Minecraft.getMinecraft().recoveredField3820.method_25825();
         int var6 = (var5 >> 3 & 1) * 85;
         int var7 = (var5 >> 2 & 1) * 170 + var6;
         int var8 = (var5 >> 1 & 1) * 170 + var6;
         int var9 = (var5 >> 0 & 1) * 170 + var6;
         if (var5 == 6) {
            var7 += 85;
         }

         if (var1.anaglyph) {
            int var10 = (var7 * 30 + var8 * 59 + var9 * 11) / 100;
            int var11 = (var7 * 30 + var8 * 70) / 100;
            int var12 = (var7 * 30 + var9 * 70) / 100;
            var7 = var10;
            var8 = var11;
            var9 = var12;
         }

         if (var5 >= 16) {
            var7 /= 4;
            var8 /= 4;
            var9 /= 4;
         }

         this.colorCode[var5] = (var7 & 0xFF) << 16 | (var8 & 0xFF) << 8 | var9 & 0xFF;
      }

      this.readGlyphSizes();
   }

   public float renderDefaultChar(int var1, boolean var2) {
      int var3 = var1 % 16 * 8;
      int var4 = var1 / 16 * 8;
      int var5 = var2 ? 1 : 0;
      this.bindTexture(this.locationFontTexture);
      float var6 = this.charWidthFloat[var1];
      float var7 = 7.99F;
      GL11.glBegin(5);
      GL11.glTexCoord2f(var3 / 128.0F, var4 / 128.0F);
      GL11.glVertex3f(this.posX + var5, this.posY, this.posZ);
      GL11.glTexCoord2f(var3 / 128.0F, (var4 + 7.99F) / 128.0F);
      GL11.glVertex3f(this.posX - var5, this.posY + 7.99F, this.posZ);
      GL11.glTexCoord2f((var3 + var7 - 1.0F) / 128.0F, var4 / 128.0F);
      GL11.glVertex3f(this.posX + var7 - 1.0F + var5, this.posY, this.posZ);
      GL11.glTexCoord2f((var3 + var7 - 1.0F) / 128.0F, (var4 + 7.99F) / 128.0F);
      GL11.glVertex3f(this.posX + var7 - 1.0F - var5, this.posY + 7.99F, this.posZ);
      GL11.glEnd();
      return var6;
   }

   public InputStream getResourceInputStream(ResourceLocation var1) throws java.io.IOException {
      return Minecraft.getMinecraft().getResourceManager().getResource(var1).getInputStream();
   }

   public int getStringWidth(String var1) {
      if (var1 == null) {
         return 0;
      } else {
         float var2 = 0.0F;
         boolean var3 = false;

         for (int var4 = 0; var4 < var1.length(); var4++) {
            char var5 = var1.charAt(var4);
            float var6 = this.getCharWidthFloat(var5);
            if (var6 < 0.0F && var4 < var1.length() - 1) {
               var5 = var1.charAt(++var4);
               if (var5 == 'l' || var5 == 'L') {
                  var3 = true;
               } else if (var5 == 'r' || var5 == 'R') {
                  var3 = false;
               }

               var6 = 0.0F;
            }

            var2 += var6;
            if (var3 && var6 > 0.0F) {
               var2 += this.unicodeFlag ? 1.0F : this.offsetBold;
            }
         }

         return Math.round(var2);
      }
   }

   public int renderStringAligned(String var1, int var2, int var3, int var4, int var5, boolean var6) {
      if (this.bidiFlag) {
         int var7 = this.getStringWidth(this.bidiReorder(var1));
         var2 = var2 + var4 - var7;
      }

      return this.renderString(var1, var2, var3, var5, var6);
   }

   public int method_08776(String var1, int var2, int var3, int var4) {
      int var5 = this.getStringWidth(var1);
      return this.drawString(var1, var2 - var5 / 2.0F, var3, var4, true);
   }

   public int method_08753(String var1, float var2, float var3, int var4) {
      int var5 = this.getStringWidth(var1);
      return this.drawString(var1, var2 - var5 / 2.0F, var3, var4, false);
   }

   public void resetStyles() {
      this.randomStyle = false;
      this.boldStyle = false;
      this.italicStyle = false;
      this.underlineStyle = false;
      this.strikethroughStyle = false;
   }

   public int splitStringWidth(String var1, int var2) {
      return this.FONT_HEIGHT * this.listFormattedStringToWidth(var1, var2).size();
   }

   public void renderStringAtPos(String var1, boolean var2) {
      for (int var3 = 0; var3 < var1.length(); var3++) {
         char var4 = var1.charAt(var3);
         if (var4 == 167 && var3 + 1 < var1.length()) {
            int var9 = "0123456789abcdefklmnor".indexOf(var1.toLowerCase(Locale.ENGLISH).charAt(var3 + 1));
            if (var9 < 16) {
               this.randomStyle = false;
               this.boldStyle = false;
               this.strikethroughStyle = false;
               this.underlineStyle = false;
               this.italicStyle = false;
               if (var9 < 0 || var9 > 15) {
                  var9 = 15;
               }

               if (var2) {
                  var9 += 16;
               }

               int var11 = this.colorCode[var9];
               if (Config.isCustomColors()) {
                  var11 = CustomColors.method_29860(var9, var11);
               }

               this.textColor = var11;
               this.setColor((var11 >> 16) / 255.0F, (var11 >> 8 & 0xFF) / 255.0F, (var11 & 0xFF) / 255.0F, this.recoveredField2547);
            } else if (var9 == 16) {
               this.randomStyle = true;
            } else if (var9 == 17) {
               this.boldStyle = true;
            } else if (var9 == 18) {
               this.strikethroughStyle = true;
            } else if (var9 == 19) {
               this.underlineStyle = true;
            } else if (var9 == 20) {
               this.italicStyle = true;
            } else if (var9 == 21) {
               this.randomStyle = false;
               this.boldStyle = false;
               this.strikethroughStyle = false;
               this.underlineStyle = false;
               this.italicStyle = false;
               this.setColor(this.recoveredField2548, this.recoveredField2549, this.recoveredField2546, this.recoveredField2547);
            }

            var3++;
         } else {
            int var5 = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000"
               .indexOf(var4);
            if (this.randomStyle && var5 != -1) {
               int var6 = this.getCharWidth(var4);

               char var7;
               do {
                  var5 = this.fontRandom
                     .nextInt(
                        "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000"
                           .length()
                     );
                  var7 = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000"
                     .charAt(var5);
               } while (var6 != this.getCharWidth(var7));

               var4 = var7;
            }

            float var10 = var5 != -1 && !this.unicodeFlag ? this.offsetBold : 0.5F;
            boolean var12 = (var4 == 0 || var5 == -1 || this.unicodeFlag) && var2;
            if (var12) {
               this.posX -= var10;
               this.posY -= var10;
            }

            float var8 = this.renderChar(var4, this.italicStyle);
            if (var12) {
               this.posX += var10;
               this.posY += var10;
            }

            if (this.boldStyle) {
               this.posX += var10;
               if (var12) {
                  this.posX -= var10;
                  this.posY -= var10;
               }

               this.renderChar(var4, this.italicStyle);
               this.posX -= var10;
               if (var12) {
                  this.posX += var10;
                  this.posY += var10;
               }

               var8 += var10;
            }

            this.doDraw(var8);
         }
      }
   }

   public void enableAlpha() {
      GlStateManager.enableAlpha();
   }

   public int drawStringWithShadow(String var1, float var2, float var3, int var4) {
      return this.drawString(var1, var2, var3, var4, true);
   }

   public int renderString(String var1, float var2, float var3, int var4, boolean var5) {
      if (var1 == null) {
         return 0;
      } else {
         if (this.bidiFlag) {
            var1 = this.bidiReorder(var1);
         }

         if ((var4 & -67108864) == 0) {
            var4 |= -16777216;
         }

         if (var5) {
            var4 = (var4 & 16579836) >> 2 | var4 & 0xFF000000;
         }

         if (CheatBreaker.recoveredField1597) {
            var4 = -12058369;
         }

         this.recoveredField2548 = (var4 >> 16 & 0xFF) / 255.0F;
         this.recoveredField2549 = (var4 >> 8 & 0xFF) / 255.0F;
         this.recoveredField2546 = (var4 & 0xFF) / 255.0F;
         this.recoveredField2547 = (var4 >> 24 & 0xFF) / 255.0F;
         this.setColor(this.recoveredField2548, this.recoveredField2549, this.recoveredField2546, this.recoveredField2547);
         this.posX = var2;
         this.posY = var3;
         this.renderStringAtPos(var1, var5);
         return (int)this.posX;
      }
   }

   public String trimStringNewline(String var1) {
      while (var1 != null && var1.endsWith("\n")) {
         var1 = var1.substring(0, var1.length() - 1);
      }

      return var1;
   }

   public void setBidiFlag(boolean var1) {
      this.bidiFlag = var1;
   }

   public ResourceLocation getUnicodePageLocation(int var1) {
      if (unicodePageLocations[var1] == null) {
         unicodePageLocations[var1] = new ResourceLocation(String.format("textures/font/unicode_page_%02x.png", var1));
         unicodePageLocations[var1] = FontUtils.getHdFontLocation(unicodePageLocations[var1]);
      }

      return unicodePageLocations[var1];
   }

   public float renderUnicodeChar(char var1, boolean var2) {
      if (this.glyphWidth[var1] == 0) {
         return 0.0F;
      } else {
         int var3 = var1 / 256;
         this.loadGlyphTexture(var3);
         int var4 = this.glyphWidth[var1] >>> 4;
         int var5 = this.glyphWidth[var1] & 15;
         float var6 = var4;
         float var7 = var5 + 1;
         float var8 = var1 % 16 * 16 + var6;
         float var9 = (var1 & 255) / 16 * 16;
         float var10 = var7 - var6 - 0.02F;
         float var11 = var2 ? 1.0F : 0.0F;
         GL11.glBegin(5);
         GL11.glTexCoord2f(var8 / 256.0F, var9 / 256.0F);
         GL11.glVertex3f(this.posX + var11, this.posY, this.posZ);
         GL11.glTexCoord2f(var8 / 256.0F, (var9 + 15.98F) / 256.0F);
         GL11.glVertex3f(this.posX - var11, this.posY + 7.99F, this.posZ);
         GL11.glTexCoord2f((var8 + var10) / 256.0F, var9 / 256.0F);
         GL11.glVertex3f(this.posX + var10 / 2.0F + var11, this.posY, this.posZ);
         GL11.glTexCoord2f((var8 + var10) / 256.0F, (var9 + 15.98F) / 256.0F);
         GL11.glVertex3f(this.posX + var10 / 2.0F - var11, this.posY + 7.99F, this.posZ);
         GL11.glEnd();
         return (var7 - var6) / 2.0F + 1.0F;
      }
   }

   public static boolean isFormatColor(char var0) {
      return var0 >= '0' && var0 <= '9' || var0 >= 'a' && var0 <= 'f' || var0 >= 'A' && var0 <= 'F';
   }

   public void loadGlyphTexture(int var1) {
      this.bindTexture(this.getUnicodePageLocation(var1));
   }

   public void renderSplitString(String var1, int var2, int var3, int var4, boolean var5) {
      for (String var7 : this.listFormattedStringToWidth(var1, var4)) {
         this.renderStringAligned(var7, var2, var3, var4, this.textColor, var5);
         var3 += this.FONT_HEIGHT;
      }
   }

   public int sizeStringToWidth(String var1, int var2) {
      int var3 = var1.length();
      float var4 = 0.0F;
      int var5 = 0;
      int var6 = -1;

      for (boolean var7 = false; var5 < var3; var5++) {
         char var8 = var1.charAt(var5);
         switch (var8) {
            case '\n':
               var5--;
               break;
            case ' ':
               var6 = var5;
            default:
               var4 += this.getCharWidth(var8);
               if (var7) {
                  var4++;
               }
               break;
            case '§':
               if (var5 < var3 - 1) {
                  char var9 = var1.charAt(++var5);
                  if (var9 == 'l' || var9 == 'L') {
                     var7 = true;
                  } else if (var9 == 'r' || var9 == 'R' || isFormatColor(var9)) {
                     var7 = false;
                  }
               }
         }

         if (var8 == '\n') {
            var6 = ++var5;
            break;
         }

         if (Math.round(var4) > var2) {
            break;
         }
      }

      return var5 != var3 && var6 != -1 && var6 < var5 ? var6 : var5;
   }

   public int getColorCode(char var1) {
      int var2 = "0123456789abcdef".indexOf(var1);
      if (var2 >= 0 && var2 < this.colorCode.length) {
         int var3 = this.colorCode[var2];
         if (Config.isCustomColors()) {
            var3 = CustomColors.method_29860(var2, var3);
         }

         return var3;
      } else {
         return 16777215;
      }
   }

   public boolean getUnicodeFlag() {
      return this.unicodeFlag;
   }

   public float renderChar(char var1, boolean var2) {
      if (var1 != ' ' && var1 != 160) {
         int var3 = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000"
            .indexOf(var1);
         return var3 != -1 && !this.unicodeFlag ? this.renderDefaultChar(var3, var2) : this.renderUnicodeChar(var1, var2);
      } else {
         return !this.unicodeFlag ? this.charWidthFloat[var1] : 4.0F;
      }
   }
}
