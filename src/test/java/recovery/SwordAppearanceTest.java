package recovery;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.config.ConfigManager;
import com.cheatbreaker.client.event.EventBus;
import com.cheatbreaker.client.module.type.HurtcamModule;
import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;
import junit.framework.TestCase;
import sun.misc.Unsafe;

public class SwordAppearanceTest extends TestCase {
    private CheatBreaker previous;
    private net.minecraft.client.Minecraft previousMinecraft;

    private static <T> T allocate(Class<T> type) throws Exception {
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        return type.cast(((Unsafe)field.get(null)).allocateInstance(type));
    }

    @Override public void setUp() {
        previous = CheatBreaker.instance;
        previousMinecraft = net.minecraft.client.Minecraft.theMinecraft;
        try {
            net.minecraft.init.Bootstrap.register();
            net.minecraft.client.Minecraft.theMinecraft = allocate(net.minecraft.client.Minecraft.class);
            CheatBreaker.instance = allocate(CheatBreaker.class);
            CheatBreaker.instance.configManager = allocate(ConfigManager.class);
            EventBus bus = allocate(EventBus.class);
            bus.recoveredField2205 = new ConcurrentHashMap<>();
            CheatBreaker.instance.recoveredField1568 = bus;
        } catch (Exception error) {
            CheatBreaker.instance = previous;
            net.minecraft.client.Minecraft.theMinecraft = previousMinecraft;
            throw new IllegalStateException(error);
        }
    }

    @Override public void tearDown() {
        CheatBreaker.instance = previous;
        net.minecraft.client.Minecraft.theMinecraft = previousMinecraft;
    }

    public void testDefaultAndScopeAndOriginalItemData() throws Exception {
        com.cheatbreaker.client.module.type.TextureOptionsModule module =
            new com.cheatbreaker.client.module.type.TextureOptionsModule();
        assertEquals("Off", module.alwaysDiamondSword.getValue());
        CheatBreaker.instance.moduleManager = allocate(com.cheatbreaker.client.module.ModuleManager.class);
        CheatBreaker.instance.moduleManager.recoveredField1727 = module;
        for (net.minecraft.item.Item sword : new net.minecraft.item.Item[]{
                net.minecraft.init.Items.wooden_sword, net.minecraft.init.Items.stone_sword,
                net.minecraft.init.Items.iron_sword, net.minecraft.init.Items.golden_sword,
                net.minecraft.init.Items.diamond_sword}) {
            net.minecraft.item.ItemStack original = new net.minecraft.item.ItemStack(sword, 1, 12);
            original.setStackDisplayName("Actual sword");
            original.addEnchantment(net.minecraft.enchantment.Enchantment.recoveredField640, 2);
            net.minecraft.nbt.NBTTagCompound before = (net.minecraft.nbt.NBTTagCompound)original.getTagCompound().copy();
            assertSame(original, com.cheatbreaker.client.util.render.SwordAppearance.forRender(original, true));
            for (String mode : new String[]{"Self", "All", "Off"}) {
                module.alwaysDiamondSword.setValue(mode, false);
                for (boolean own : new boolean[]{true, false}) {
                    net.minecraft.item.ItemStack appearance = com.cheatbreaker.client.util.render.SwordAppearance.forRender(original, own);
                    boolean diamond = "All".equals(mode) || ("Self".equals(mode) && own);
                    assertSame(diamond ? net.minecraft.init.Items.diamond_sword : sword, appearance.getItem());
                    assertSame(sword, original.getItem());
                    assertEquals(12, original.getItemDamage());
                    assertEquals(before, original.getTagCompound());
                    assertEquals(original.hasEffect(), appearance.hasEffect());
                    if (appearance != original) {
                        appearance.getTagCompound().setString("test", "copy");
                        assertEquals(before, original.getTagCompound());
                    }
                }
            }
            module.alwaysDiamondSword.setValue("Off", false);
        }
        module.alwaysDiamondSword.setValue("All", false);
        module.enabled = false;
        net.minecraft.item.ItemStack sword = new net.minecraft.item.ItemStack(net.minecraft.init.Items.iron_sword);
        assertSame(sword, com.cheatbreaker.client.util.render.SwordAppearance.forRender(sword, true));
        net.minecraft.item.ItemStack axe = new net.minecraft.item.ItemStack(net.minecraft.init.Items.iron_axe);
        assertSame(axe, com.cheatbreaker.client.util.render.SwordAppearance.forRender(axe, "All", true));
        assertNull(com.cheatbreaker.client.util.render.SwordAppearance.forRender(null, "All", true));
    }

    public void testRenderOwnershipAndGuiContextRestoration() throws Exception {
        com.cheatbreaker.client.module.type.TextureOptionsModule module = new com.cheatbreaker.client.module.type.TextureOptionsModule();
        module.alwaysDiamondSword.setValue("Self", false);
        CheatBreaker.instance.moduleManager = allocate(com.cheatbreaker.client.module.ModuleManager.class);
        CheatBreaker.instance.moduleManager.recoveredField1727 = module;
        net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getMinecraft();
        mc.thePlayer = allocate(net.minecraft.client.entity.EntityPlayerSP.class);
        mc.thePlayer.bi = new net.minecraft.entity.player.InventoryPlayer(mc.thePlayer);
        net.minecraft.item.ItemStack sword = new net.minecraft.item.ItemStack(net.minecraft.init.Items.iron_sword);
        RecordingRenderer renderer = allocate(RecordingRenderer.class);
        RecordingMesher mesher = allocate(RecordingMesher.class);
        renderer.itemModelMesher = mesher;
        renderer.recoveredField1396 = mc.thePlayer;
        renderer.getAppearanceModel(sword, "held");
        assertSame(net.minecraft.init.Items.diamond_sword, mesher.last.getItem());
        renderer.recoveredField1396 = allocate(net.minecraft.entity.monster.EntityZombie.class);
        renderer.getAppearanceModel(sword, "held");
        assertSame(sword, mesher.last);
        renderer.recoveredField1396 = allocate(net.minecraft.client.entity.EntityOtherPlayerMP.class);
        renderer.getAppearanceModel(sword, "held");
        assertSame(sword, mesher.last);
        renderer.recoveredField1396 = mc.thePlayer;
        renderer.getAppearanceModel(sword, "dropped");
        assertSame(sword, mesher.last);
        mc.thePlayer.bi.mainInventory[0] = sword;
        renderer.getAppearanceModel(sword, "inventory");
        assertSame(net.minecraft.init.Items.diamond_sword, mesher.last.getItem());
        renderer.renderItemAndEffectIntoGUI(sword.copy(), 0, 0, true);
        assertSame(net.minecraft.init.Items.diamond_sword, mesher.last.getItem());
        renderer.renderItemAndEffectIntoGUI(sword, 0, 0, false);
        assertSame(sword, mesher.last);
        renderer.getAppearanceModel(sword.copy(), "inventory");
        assertSame(net.minecraft.init.Items.iron_sword, mesher.last.getItem());
        module.alwaysDiamondSword.setValue("All", false);
        renderer.getAppearanceModel(sword, "dropped");
        assertSame(net.minecraft.init.Items.diamond_sword, mesher.last.getItem());
    }

    private static class RecordingMesher extends net.minecraft.client.renderer.ItemModelMesher {
        net.minecraft.item.ItemStack last;
        RecordingMesher() { super(null); }
        @Override public net.minecraft.client.resources.model.IBakedModel getItemModel(net.minecraft.item.ItemStack stack) {
            last = stack;
            return null;
        }
    }
    private static class RecordingRenderer extends net.minecraft.client.renderer.entity.RenderItem {
        RecordingRenderer() { super(null, null); }
        @Override public void renderItemAndEffectIntoGUI(net.minecraft.item.ItemStack stack, int x, int y) {
            getAppearanceModel(stack, "inventory");
        }
    }
}
