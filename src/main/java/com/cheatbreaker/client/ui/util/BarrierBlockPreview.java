package com.cheatbreaker.client.ui.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

/** Composed once on load, then rendered through the standard single-texture icon path. */
public final class BarrierBlockPreview extends AbstractTexture {
   public static final ResourceLocation ICON = new ResourceLocation("client/icons/mods/barrier_block_preview.png");
   private static final ResourceLocation BARRIER = new ResourceLocation("textures/items/barrier.png");
   private static final ResourceLocation GLASS = new ResourceLocation("textures/blocks/glass.png");
   private static final int SIZE = 32;

   private BarrierBlockPreview() {}

   public static void prepare() {
      TextureManager textures = Minecraft.getMinecraft().getTextureManager();
      if (textures.getTexture(ICON) == null) {
         // TextureManager also reloads this object when the resource pack changes.
         textures.loadTexture(ICON, new BarrierBlockPreview());
      }
   }

   @Override
   public void loadTexture(IResourceManager resources) throws IOException {
      BufferedImage barrier = readTexture(resources, BARRIER);
      BufferedImage glass = readTexture(resources, GLASS);
      BufferedImage icon = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
      for (int y = 0; y < SIZE; y++) {
         for (int x = 0; x < SIZE; x++) {
            BufferedImage source = x < SIZE / 2 ? barrier : glass;
            int color = source.getRGB(x * source.getWidth() / SIZE, y * source.getHeight() / SIZE);
            // Match the original center line's raster position, immediately left of the midpoint.
            icon.setRGB(x, y, x == SIZE / 2 - 1 ? 0xFFB8C2CC : color);
         }
      }
      TextureUtil.uploadTextureImage(this.getGlTextureId(), icon);
   }

   private static BufferedImage readTexture(IResourceManager resources, ResourceLocation location) throws IOException {
      try (InputStream input = resources.getResource(location).getInputStream()) {
         BufferedImage image = ImageIO.read(input);
         if (image == null) {
            throw new IOException("Invalid preview texture: " + location);
         }
         return image;
      }
   }
}
