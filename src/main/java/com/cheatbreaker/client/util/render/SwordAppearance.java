package com.cheatbreaker.client.util.render;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.module.type.TextureOptionsModule;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

/** Render-only substitution. Never pass the returned copy to gameplay or inventory code. */
public final class SwordAppearance {
   private SwordAppearance() {}

   public static ItemStack forRender(ItemStack stack, boolean localSword) {
      CheatBreaker client = CheatBreaker.getInstance();
      if (client == null || client.getModuleManager() == null) return stack;
      TextureOptionsModule module = client.getModuleManager().recoveredField1727;
      if (module == null || !module.isEnabled() || module.alwaysDiamondSword == null) return stack;
      return forRender(stack, (String)module.alwaysDiamondSword.getValue(), localSword);
   }

   public static ItemStack forRender(ItemStack stack, String mode, boolean localSword) {
      if (stack == null || !(stack.getItem() instanceof ItemSword) || stack.getItem() == Items.diamond_sword
            || !("All".equals(mode) || ("Self".equals(mode) && localSword))) return stack;
      ItemStack appearance = stack.copy();
      appearance.setItem(Items.diamond_sword);
      return appearance;
   }
}
