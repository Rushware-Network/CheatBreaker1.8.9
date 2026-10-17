package net.minecraft.client.gui;

import java.lang.reflect.Field;
import junit.framework.TestCase;
import sun.misc.Unsafe;

public class NametagTextDepthTest extends TestCase {
    private static final Field DEPTH;
    static {
        try {
            DEPTH = FontRenderer.class.getDeclaredField("posZ");
            DEPTH.setAccessible(true);
        } catch (Exception error) { throw new ExceptionInInitializerError(error); }
    }

    public static class RecordingFont extends FontRenderer {
        float shadowZ, faceZ;
        int shadowCalls, faceCalls;
        boolean failShadow;
        public RecordingFont() { super(null, null, null, false); }
        @Override public void enableAlpha() {}
        @Override public int renderString(String text, float x, float y, int color, boolean shadow) {
            try {
                if (shadow) {
                    shadowZ = DEPTH.getFloat(this);
                    shadowCalls++;
                    if (failShadow) throw new IllegalStateException("shadow failed");
                } else {
                    faceZ = DEPTH.getFloat(this);
                    faceCalls++;
                }
                return (int)x + 10;
            } catch (IllegalAccessException error) { throw new AssertionError(error); }
        }
    }

    private RecordingFont font() throws Exception {
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        return (RecordingFont)((Unsafe)field.get(null)).allocateInstance(RecordingFont.class);
    }

    public void testNametagSeparatesOnlyShadowAndPreservesTwoPassesAndWidth() throws Exception {
        RecordingFont font = font();
        assertEquals(15, font.drawString("Name", 4, 3, -1, true, 1));
        assertEquals(1f, font.shadowZ, 0f);
        assertEquals(0f, font.faceZ, 0f);
        assertEquals(1, font.shadowCalls);
        assertEquals(1, font.faceCalls);
        assertEquals(0f, DEPTH.getFloat(font), 0f);
    }

    public void testGuiShadowRemainsCoplanarAfterNametag() throws Exception {
        RecordingFont font = font();
        font.drawString("Name", 4, 3, -1, true, 1);
        font.drawString("GUI", 4, 3, -1, true);
        assertEquals(0f, font.shadowZ, 0f);
        assertEquals(0f, font.faceZ, 0f);
    }

    public void testDisabledShadowAddsNoPassAndLeavesFaceAtZero() throws Exception {
        RecordingFont font = font();
        font.drawString("Name", 4, 3, -1, false, 1);
        assertEquals(0, font.shadowCalls);
        assertEquals(1, font.faceCalls);
        assertEquals(0f, font.faceZ, 0f);
    }

    public void testShadowFailureRestoresDepthBeforeNextGuiDraw() throws Exception {
        RecordingFont font = font();
        font.failShadow = true;
        try {
            font.drawString("Name", 4, 3, -1, true, 1);
            fail("Expected shadow failure");
        } catch (IllegalStateException expected) {
            assertEquals("shadow failed", expected.getMessage());
        }
        assertEquals(0f, DEPTH.getFloat(font), 0f);
        font.failShadow = false;
        font.drawString("GUI", 4, 3, -1, true);
        assertEquals(0f, font.shadowZ, 0f);
        assertEquals(0f, font.faceZ, 0f);
    }
}
