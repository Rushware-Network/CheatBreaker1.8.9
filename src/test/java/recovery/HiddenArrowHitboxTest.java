package recovery;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.config.GlobalSettings;
import com.cheatbreaker.client.config.Setting;
import java.lang.reflect.Field;
import java.util.HashMap;
import junit.framework.TestCase;
import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.projectile.EntityArrow;
import sun.misc.Unsafe;

public class HiddenArrowHitboxTest extends TestCase {
    private CheatBreaker previous;
    private GlobalSettings settings;
    private RenderManager manager;
    private EntityArrow arrow;
    private FailOnRenderArrow renderer;

    private static <T> T allocate(Class<T> type) throws Exception {
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        return type.cast(((Unsafe)field.get(null)).allocateInstance(type));
    }

    private Setting toggle(boolean enabled) throws Exception {
        Setting setting = allocate(Setting.class);
        setting.recoveredField3086 = enabled;
        return setting;
    }

    public void setUp() {
        try {
            initialize();
        } catch (Exception failure) {
            throw new RuntimeException(failure);
        }
    }

    private void initialize() throws Exception {
        previous = CheatBreaker.instance;
        CheatBreaker client = allocate(CheatBreaker.class);
        settings = allocate(GlobalSettings.class);
        settings.recoveredField518 = toggle(false);
        settings.recoveredField557 = toggle(false);
        client.globalSettings = settings;
        CheatBreaker.instance = client;
        manager = allocate(RenderManager.class);
        manager.entityRenderMap = new HashMap();
        manager.renderEngine = allocate(TextureManager.class);
        renderer = allocate(FailOnRenderArrow.class);
        manager.entityRenderMap.put(EntityArrow.class, renderer);
        arrow = allocate(EntityArrow.class);
    }

    public void tearDown() {
        CheatBreaker.instance = previous;
    }

    private void assertHiddenWithoutRendering() {
        assertTrue(renderer.method_01981(arrow));
        assertTrue(manager.doRenderEntity(arrow, 0, 0, 0, 0, 0, false));
        // No module manager or GL context: this must return before reading any Hitboxes settings.
        manager.renderDebugBoundingBox(arrow, 0, 0, 0, 0, 0);
    }

    public void testGroundedArrowSkipsEntityAndAllHitboxRendering() {
        arrow.inGround = true;
        settings.recoveredField557.recoveredField3086 = true;
        assertHiddenWithoutRendering();
        settings.recoveredField557.recoveredField3086 = false;
        assertFalse(renderer.method_01981(arrow));
    }

    public void testMovingArrowSkipsEntityAndAllHitboxRendering() {
        settings.recoveredField518.recoveredField3086 = true;
        double[][] velocities = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        for (double[] velocity : velocities) {
            arrow.v = velocity[0];
            arrow.w = velocity[1];
            arrow.x = velocity[2];
            assertHiddenWithoutRendering();
        }
        settings.recoveredField518.recoveredField3086 = false;
        assertFalse(renderer.method_01981(arrow));
    }

    public void testMovingToggleDoesNotHideGroundedOrStationaryArrows() {
        settings.recoveredField518.recoveredField3086 = true;
        assertFalse(renderer.method_01981(arrow));
        arrow.v = 1;
        arrow.inGround = true;
        assertFalse(renderer.method_01981(arrow));
    }

    public static class FailOnRenderArrow extends RenderArrow {
        public FailOnRenderArrow() {
            super(null);
        }

        @Override
        public void doRender(EntityArrow entity, double x, double y, double z, float yaw, float partialTicks) {
            throw new AssertionError("Hidden arrow reached entity rendering");
        }

        @Override
        public void method_08026(net.minecraft.entity.Entity entity, double x, double y, double z, float yaw, float partialTicks) {
            throw new AssertionError("Hidden arrow reached post-render effects");
        }
    }
}
