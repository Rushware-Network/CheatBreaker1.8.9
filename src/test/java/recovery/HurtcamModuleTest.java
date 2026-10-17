package recovery;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.config.ConfigManager;
import com.cheatbreaker.client.event.EventBus;
import com.cheatbreaker.client.module.type.HurtcamModule;
import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;
import junit.framework.TestCase;
import sun.misc.Unsafe;

public class HurtcamModuleTest extends TestCase {
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

    public void testCameraSettingsMovedFromPerspective() {
        HurtcamModule module = new HurtcamModule();
        com.cheatbreaker.client.module.type.PerspectiveModule perspective =
            new com.cheatbreaker.client.module.type.PerspectiveModule();
        com.cheatbreaker.client.module.type.AnimationsModule animations =
            new com.cheatbreaker.client.module.type.AnimationsModule();
        assertFalse(module.isEnabled());
        for (com.cheatbreaker.client.config.Setting setting : new com.cheatbreaker.client.config.Setting[]{
                module.damageAffectsCamera, module.hurtCameraIntensity}) {
            assertSame(module, setting.method_08920());
            assertTrue(module.getSettingsList().contains(setting));
            for (com.cheatbreaker.client.config.Setting remaining : perspective.getSettingsList()) {
                assertFalse(setting.method_08911().equals(remaining.method_08911()));
            }
        }
        for (com.cheatbreaker.client.config.Setting setting : module.getSettingsList()) {
            assertFalse("Intensity".equals(setting.method_08911()));
        }
        for (com.cheatbreaker.client.config.Setting setting : animations.getSettingsList()) {
            assertFalse("Minimal Bobbing".equals(setting.method_08911()));
            assertFalse("View Bobbing Settings".equals(setting.getValue()));
        }
        assertTrue(module.damageAffectsCamera.method_08908());
        assertEquals(14.0F, module.hurtCameraIntensity.method_08905(), 0.0F);
        assertEquals(5.0F, ((Number)module.hurtCameraIntensity.method_08904()).floatValue(), 0.0F);
        assertEquals(35.0F, ((Number)module.hurtCameraIntensity.method_08878()).floatValue(), 0.0F);
        assertTrue(module.hurtCameraIntensity.method_08921().getAsBoolean());
        module.damageAffectsCamera.setValue(false, false);
        assertFalse(module.hurtCameraIntensity.method_08921().getAsBoolean());
        assertFalse(perspective.recoveredField3178.method_08908());
        assertEquals(30, ((Number)perspective.recoveredField3168.getValue()).intValue());
        assertEquals(0.15F, perspective.recoveredField3173.method_08905(), 0.0F);
    }
}
