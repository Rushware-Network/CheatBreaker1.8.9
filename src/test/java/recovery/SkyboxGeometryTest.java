package recovery;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import junit.framework.TestCase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.src.Config;
import sun.misc.Unsafe;

public class SkyboxGeometryTest extends TestCase {
    private GameSettings previousSettings;
    private Minecraft previousMinecraft;
    private RenderGlobal renderer;

    @Override public void setUp() {
        previousSettings = Config.gameSettings;
        previousMinecraft = Minecraft.theMinecraft;
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            Unsafe unsafe = (Unsafe)field.get(null);
            Config.gameSettings = (GameSettings)unsafe.allocateInstance(GameSettings.class);
            Minecraft.theMinecraft = (Minecraft)unsafe.allocateInstance(Minecraft.class);
            Minecraft.theMinecraft.gameSettings = Config.gameSettings;
            renderer = (RenderGlobal)unsafe.allocateInstance(RenderGlobal.class);
        } catch (Exception failure) {
            Config.gameSettings = previousSettings;
            Minecraft.theMinecraft = previousMinecraft;
            throw new IllegalStateException(failure);
        }
    }

    @Override public void tearDown() {
        Config.gameSettings = previousSettings;
        Minecraft.theMinecraft = previousMinecraft;
    }

    private WorldRenderer mesh(int chunks, boolean underside) {
        renderer.renderDistance = chunks * 16;
        WorldRenderer vertices = new WorldRenderer(4096);
        renderer.renderSky(vertices, underside ? -16 : 16, underside);
        vertices.finishDrawing();
        return vertices;
    }

    public void testFullCoverageAndNoAdditionalVerticesAtAnySupportedDistance() {
        // Includes initialization before a distance is selected, and OptiFine's 2-64 chunks.
        for (int chunks = 0; chunks <= 64; chunks++) {
            for (boolean underside : new boolean[]{false, true}) {
                WorldRenderer vertices = mesh(chunks, underside);
                int oldRadius = (chunks * 16 / 64 + 2) * 64;
                int oldCells = oldRadius / 64 * 2 + 1;
                assertTrue("Vertex budget at " + chunks, vertices.vertexCount <= oldCells * oldCells * 4);
                assertTrue(vertices.vertexCount <= 676);
                ByteBuffer data = vertices.getByteBuffer();
                float minX = Float.POSITIVE_INFINITY, maxX = Float.NEGATIVE_INFINITY;
                float minZ = Float.POSITIVE_INFINITY, maxZ = Float.NEGATIVE_INFINITY;
                double area = 0;
                for (int v = 0; v < vertices.vertexCount; v += 4) {
                    int offset = v * 12;
                    float x0 = data.getFloat(offset), z0 = data.getFloat(offset + 8);
                    float x1 = data.getFloat(offset + 12), z1 = data.getFloat(offset + 32);
                    double signedArea = (double)(x1 - x0) * (z1 - z0);
                    assertTrue("Winding at " + chunks, underside ? signedArea < 0 : signedArea > 0);
                    area += Math.abs(signedArea);
                    for (int corner = 0; corner < 4; corner++) {
                        int p = offset + corner * 12;
                        float x = data.getFloat(p), z = data.getFloat(p + 8);
                        assertEquals(underside ? -16f : 16f, data.getFloat(p + 4), 0f);
                        minX = Math.min(minX, x); maxX = Math.max(maxX, x);
                        minZ = Math.min(minZ, z); maxZ = Math.max(maxZ, z);
                    }
                }
                assertEquals(-384f, minX, 0.001f); assertEquals(448f, maxX, 0.001f);
                assertEquals(-384f, minZ, 0.001f); assertEquals(448f, maxZ, 0.001f);
                assertEquals("Complete plane area", 832.0 * 832.0, area, 0.25);
            }
        }
    }

    public void testAdjacentQuadsShareEdgesWithoutCracks() {
        for (int chunks : new int[]{2, 4, 8, 12, 16, 32, 64}) {
            WorldRenderer vertices = mesh(chunks, false);
            ByteBuffer data = vertices.getByteBuffer();
            int side = (int)Math.sqrt(vertices.vertexCount / 4);
            for (int x = 0; x < side; x++) for (int z = 0; z < side; z++) {
                int p = (x * side + z) * 48;
                if (x + 1 < side) assertEquals(data.getFloat(p + 12), data.getFloat(p + side * 48), 0f);
                if (z + 1 < side) assertEquals(data.getFloat(p + 32), data.getFloat(p + 48 + 8), 0f);
            }
        }
    }

    public void testNormalDistanceMatchesPatcherMeshExactly() {
        WorldRenderer vertices = mesh(16, false);
        assertEquals(676, vertices.vertexCount);
        ByteBuffer data = vertices.getByteBuffer();
        int p = 0;
        for (int x = -384; x <= 384; x += 64) for (int z = -384; z <= 384; z += 64) {
            float[][] quad = {{x, 16, z}, {x + 64, 16, z}, {x + 64, 16, z + 64}, {x, 16, z + 64}};
            for (float[] point : quad) for (float coordinate : point) {
                assertEquals(coordinate, data.getFloat(p), 0f);
                p += 4;
            }
        }
    }
}
