package net.minecraft.client.renderer;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.block.BlockBarrier;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.src.Config;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ISmartItemModel;
import net.optifine.CustomItems;
import net.optifine.reflect.Reflector;

public class ItemModelMesher {
   public ModelManager modelManager;
   public Map<Integer, ModelResourceLocation> simpleShapes = Maps.newHashMap();
   public Map<Item, ItemMeshDefinition> shapers;
   public Map<Integer, IBakedModel> simpleShapesCache = Maps.newHashMap();

   public IBakedModel getItemModel(ItemStack var1) {
      Item var2 = var1.getItem();
      IBakedModel var3 = this.getItemModel(var2, this.getMetadata(var1));
      if (var3 == null) {
         ItemMeshDefinition var4 = this.shapers.get(var2);
         if (var4 != null) {
            var3 = this.modelManager.getModel(var4.getModelLocation(var1));
         }
      }

      if (Reflector.ForgeHooksClient.exists() && var3 instanceof ISmartItemModel) {
         var3 = ((ISmartItemModel)var3).handleItemState(var1);
      }

      if (var3 == null) {
         var3 = this.modelManager.getMissingModel();
      }

      if (Config.isCustomItems()) {
         var3 = CustomItems.getCustomItemModel(var1, var3, (ResourceLocation)null, true);
      }

      return var3;
   }

   public void rebuildCache() {
      this.simpleShapesCache.clear();

      for (Entry var2 : this.simpleShapes.entrySet()) {
         this.simpleShapesCache.put((Integer)var2.getKey(), this.modelManager.getModel((ModelResourceLocation)var2.getValue()));
      }
      this.updateBarrierModel();
   }

   public void updateBarrierModel() {
      int index = this.getIndex(Item.getItemFromBlock(Blocks.barrier), 0);
      IBlockState state = ((BlockBarrier)Blocks.barrier).getRenderState();
      if (state == null) {
         ModelResourceLocation original = this.simpleShapes.get(index);
         if (original == null) {
            this.simpleShapesCache.remove(index);
         } else {
            this.simpleShapesCache.put(index, this.modelManager.getModel(original));
         }
      } else {
         Item item = Item.getItemFromBlock(state.getBlock());
         this.simpleShapesCache.put(index, this.getItemModel(item, state.getBlock().getMetaFromState(state)));
      }
   }

   public int getMetadata(ItemStack var1) {
      return var1.isItemStackDamageable() ? 0 : var1.getMetadata();
   }

   public ModelManager getModelManager() {
      return this.modelManager;
   }

   public void register(Item var1, ItemMeshDefinition var2) {
      this.shapers.put(var1, var2);
   }

   public TextureAtlasSprite getParticleIcon(Item var1) {
      return this.getParticleIcon(var1, 0);
   }

   public ItemModelMesher(ModelManager var1) {
      this.shapers = Maps.newHashMap();
      this.modelManager = var1;
   }

   public int getIndex(Item var1, int var2) {
      return Item.getIdFromItem(var1) << 16 | var2;
   }

   public IBakedModel getItemModel(Item var1, int var2) {
      return this.simpleShapesCache.get(this.getIndex(var1, var2));
   }

   public TextureAtlasSprite getParticleIcon(Item var1, int var2) {
      return this.getItemModel(new ItemStack(var1, 1, var2)).getParticleTexture();
   }

   public void register(Item var1, int var2, ModelResourceLocation var3) {
      this.simpleShapes.put(this.getIndex(var1, var2), var3);
      this.simpleShapesCache.put(this.getIndex(var1, var2), this.modelManager.getModel(var3));
   }
}
