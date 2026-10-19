package recovery;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.config.ConfigManager;
import com.cheatbreaker.client.config.Profile;
import com.cheatbreaker.client.event.EventBus;
import com.cheatbreaker.client.module.type.BarrierBlockModule;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import junit.framework.TestCase;
import net.minecraft.block.BlockBarrier;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.init.Blocks;
import net.minecraft.init.Bootstrap;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.RegistrySimple;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import sun.misc.Unsafe;

public class BarrierBlockRenderingTest extends TestCase {
    private CheatBreaker previousClient;
    private Minecraft previousMinecraft;
    private IBlockState previousAppearance;
    private BarrierBlockModule module;
    private BlockBarrier barrier;
    private BlockModelShapes shapes;
    private CountingRenderGlobal renderer;
    private BlockRendererDispatcher dispatcher;

    private static <T> T allocate(Class<T> type) throws Exception {
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        return type.cast(((Unsafe)field.get(null)).allocateInstance(type));
    }

    @Override public void setUp() {
        previousClient = CheatBreaker.instance;
        previousMinecraft = Minecraft.theMinecraft;
        try {
            initialize();
        } catch (Exception failure) {
            tearDown();
            throw new IllegalStateException(failure);
        }
    }

    private void initialize() throws Exception {
        Bootstrap.register();
        barrier = (BlockBarrier)Blocks.barrier;
        previousAppearance = barrier.getRenderState();
        barrier.setRenderState(null);
        Minecraft game = allocate(Minecraft.class);
        Minecraft.theMinecraft = game;
        CheatBreaker.instance = allocate(CheatBreaker.class);
        CheatBreaker.instance.configManager = allocate(ConfigManager.class);
        CheatBreaker.instance.configManager.recoveredField3785 = new ArrayList<>();
        CheatBreaker.instance.configManager.recoveredField3785.add(new Profile("default", true));
        CheatBreaker.instance.configManager.recoveredField3784 = new Profile("Test", false);
        EventBus bus = allocate(EventBus.class);
        bus.recoveredField2205 = new ConcurrentHashMap<>();
        CheatBreaker.instance.recoveredField1568 = bus;
        ModelManager models = new ModelManager(null);
        models.defaultModel = allocate(SimpleBakedModel.class);
        models.modelRegistry = new RegistrySimple<ModelResourceLocation, IBakedModel>();
        shapes = models.getBlockModelShapes();
        shapes.bakedModelStore.put(Blocks.glass.getDefaultState(), allocate(SimpleBakedModel.class));
        shapes.bakedModelStore.put(Blocks.stone.getDefaultState(), allocate(SimpleBakedModel.class));
        for (EnumDyeColor color : EnumDyeColor.values()) {
            shapes.bakedModelStore.put(Blocks.stained_glass.getDefaultState().withProperty(BlockStainedGlass.COLOR, color),
                allocate(SimpleBakedModel.class));
        }
        dispatcher = allocate(BlockRendererDispatcher.class);
        dispatcher.blockModelShapes = shapes;
        dispatcher.gameSettings = allocate(GameSettings.class);
        dispatcher.blockModelRenderer = allocate(CapturingModelRenderer.class);
        game.blockRenderDispatcher = dispatcher;
        renderer = allocate(CountingRenderGlobal.class);
        game.renderGlobal = renderer;
        game.theWorld = allocate(WorldClient.class);
        module = new BarrierBlockModule();
    }

    @Override public void tearDown() {
        if (barrier != null) {
            barrier.setRenderState(previousAppearance);
        }
        CheatBreaker.instance = previousClient;
        Minecraft.theMinecraft = previousMinecraft;
    }

    public void testAllSeventeenChoicesResolveToExistingGlassModels() {
        assertFalse(module.isEnabled());
        assertEquals(0, module.method_28860().method_08912());
        assertEquals("Regular", module.glassType.getValue());
        EnumDyeColor[] colors = {null, EnumDyeColor.CYAN, EnumDyeColor.PINK, EnumDyeColor.WHITE,
            EnumDyeColor.ORANGE, EnumDyeColor.MAGENTA, EnumDyeColor.LIGHT_BLUE, EnumDyeColor.YELLOW,
            EnumDyeColor.LIME, EnumDyeColor.GRAY, EnumDyeColor.SILVER, EnumDyeColor.PURPLE,
            EnumDyeColor.BLUE, EnumDyeColor.BROWN, EnumDyeColor.GREEN, EnumDyeColor.RED, EnumDyeColor.BLACK};
        String[] choices = module.glassType.getAcceptedValues();
        assertEquals(colors.length, choices.length);
        module.setState(true);
        for (int i = 0; i < choices.length; i++) {
            module.glassType.setValue(choices[i], false); // Same callback used when loading a saved profile.
            IBlockState expected = colors[i] == null ? Blocks.glass.getDefaultState()
                : Blocks.stained_glass.getDefaultState().withProperty(BlockStainedGlass.COLOR, colors[i]);
            assertSame(expected, barrier.getRenderState());
            assertSame(shapes.getModelForState(expected), shapes.getModelForState(barrier.getDefaultState()));
            assertEquals(colors[i] == null ? EnumWorldBlockLayer.CUTOUT : EnumWorldBlockLayer.TRANSLUCENT,
                barrier.getBlockLayer());
        }
    }

    public void testToggleAndTextureChangesRebuildOnlyWhenNeeded() {
        IBakedModel ordinaryGlass = shapes.getModelForState(Blocks.glass.getDefaultState());
        assertEquals(-1, barrier.getRenderType());
        module.glassType.setValue("Cyan", false);
        assertEquals(0, renderer.reloads);
        module.setState(true);
        assertEquals(3, barrier.getRenderType());
        assertEquals(1, renderer.reloads);
        assertEquals(renderer.reloads, renderer.stops);
        module.setState(true);
        assertEquals(1, renderer.reloads);
        module.glassType.setValue("Pink", false);
        assertEquals(2, renderer.reloads);
        module.glassType.setValue("Pink", false);
        assertEquals(2, renderer.reloads);
        module.setState(false);
        assertEquals(3, renderer.reloads);
        assertEquals(renderer.reloads, renderer.stops);
        assertEquals(-1, barrier.getRenderType());
        assertNull(barrier.getRenderState());
        assertSame(shapes.modelManager.getMissingModel(), shapes.getModelForState(barrier.getDefaultState()));
        assertSame(ordinaryGlass, shapes.getModelForState(Blocks.glass.getDefaultState()));
        module.glassType.setValue("Regular", false);
        assertEquals(3, renderer.reloads);
    }

    public void testResourceReloadRebindsToFreshBakedModel() throws Exception {
        module.glassType.setValue("Cyan", false);
        module.setState(true);
        IBlockState selected = module.getSelectedGlassState();
        IBakedModel before = shapes.getModelForState(barrier.getDefaultState());
        IBakedModel replacement = allocate(SimpleBakedModel.class);
        ModelResourceLocation location = shapes.getBlockStateMapper().putAllStateModelLocations().get(selected);
        assertNotNull(location);
        shapes.modelManager.modelRegistry.putObject(location, replacement);
        shapes.reloadModels();
        assertNotSame(before, shapes.getModelForState(barrier.getDefaultState()));
        assertSame(replacement, shapes.getModelForState(barrier.getDefaultState()));
        assertSame(replacement, shapes.getModelForState(selected));
        assertEquals(1, renderer.reloads);
    }

    public void testExistingDispatcherRendersSelectedModelWithOriginalBarrierState() {
        NeighborAccess access = new NeighborAccess(barrier.getDefaultState());
        CapturingModelRenderer modelRenderer = (CapturingModelRenderer)dispatcher.blockModelRenderer;
        for (String type : new String[]{"Regular", "Cyan", "Pink"}) {
            module.glassType.setValue(type, false);
            module.setState(true);
            assertTrue(dispatcher.renderBlock(barrier.getDefaultState(), BlockPos.ORIGIN, access, null));
            assertSame(barrier.getDefaultState(), modelRenderer.state);
            assertSame(shapes.getModelForState(module.getSelectedGlassState()), modelRenderer.model);
            assertSame(barrier.getDefaultState(), access.state);
        }
        assertEquals(3, modelRenderer.calls);
        module.setState(false);
        assertFalse(dispatcher.renderBlock(barrier.getDefaultState(), BlockPos.ORIGIN, access, null));
        assertEquals(3, modelRenderer.calls);
    }

    public void testInternalAndOpaqueFacesAreCulledWithoutChangingPhysics() {
        AxisAlignedBB bounds = barrier.getCollisionBoundingBox(null, BlockPos.ORIGIN, barrier.getDefaultState());
        float hardness = barrier.getBlockHardness(null, BlockPos.ORIGIN);
        module.setState(true);
        NeighborAccess access = new NeighborAccess(barrier.getDefaultState());
        for (EnumFacing side : EnumFacing.VALUES) {
            assertFalse(barrier.shouldSideBeRendered(access, BlockPos.ORIGIN, side));
            access.state = Blocks.stone.getDefaultState();
            assertFalse(barrier.shouldSideBeRendered(access, BlockPos.ORIGIN, side));
            access.state = Blocks.air.getDefaultState();
            assertTrue(barrier.shouldSideBeRendered(access, BlockPos.ORIGIN, side));
            access.state = barrier.getDefaultState();
        }
        assertEquals(Material.barrier, barrier.getMaterial());
        assertTrue(barrier.isCollidable());
        assertFalse(barrier.isOpaqueCube());
        assertEquals(hardness, barrier.getBlockHardness(null, BlockPos.ORIGIN), 0.0F);
        AxisAlignedBB after = barrier.getCollisionBoundingBox(null, BlockPos.ORIGIN, barrier.getDefaultState());
        assertEquals(bounds.a, after.a, 0.0);
        assertEquals(bounds.b, after.b, 0.0);
        assertEquals(bounds.c, after.c, 0.0);
        assertEquals(bounds.d, after.d, 0.0);
        assertEquals(bounds.e, after.e, 0.0);
        assertEquals(bounds.f, after.f, 0.0);
    }

    private static class CountingRenderGlobal extends RenderGlobal {
        int reloads;
        int stops;
        CountingRenderGlobal() { super(null); }
        @Override public void stopChunkUpdates() { stops++; }
        @Override public void loadRenderers() { reloads++; }
    }

    private static class CapturingModelRenderer extends BlockModelRenderer {
        IBakedModel model;
        IBlockState state;
        int calls;
        @Override public boolean renderModel(IBlockAccess access, IBakedModel model, IBlockState state,
                BlockPos pos, WorldRenderer renderer) {
            this.model = model;
            this.state = state;
            calls++;
            return true;
        }
    }

    private static class NeighborAccess implements IBlockAccess {
        IBlockState state;
        NeighborAccess(IBlockState state) { this.state = state; }
        public IBlockState getBlockState(BlockPos pos) { return state; }
        public int getStrongPower(BlockPos pos, EnumFacing side) { return 0; }
        public boolean isAirBlock(BlockPos pos) { return state.getBlock() == Blocks.air; }
        public TileEntity getTileEntity(BlockPos pos) { return null; }
        public BiomeGenBase getBiomeGenForCoords(BlockPos pos) { return null; }
        public int getCombinedLight(BlockPos pos, int light) { return light; }
        public boolean w_() { return false; }
        public WorldType x_() { return WorldType.DEFAULT; }
    }
}
