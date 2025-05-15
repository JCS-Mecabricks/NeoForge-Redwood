package github.jcsmecabricks.redwoodvariants;

import github.jcsmecabricks.redwoodvariants.block.ModBlocks;
import github.jcsmecabricks.redwoodvariants.block.entity.ModBlockEntities;
import github.jcsmecabricks.redwoodvariants.entity.ModEntities;
import github.jcsmecabricks.redwoodvariants.entity.client.GrizzlyBearRenderer;
import github.jcsmecabricks.redwoodvariants.entity.client.ModBoatRenderer;
import github.jcsmecabricks.redwoodvariants.entity.client.ModModelLayers;
import github.jcsmecabricks.redwoodvariants.item.ModCreativeModeTabs;
import github.jcsmecabricks.redwoodvariants.item.ModItems;
import github.jcsmecabricks.redwoodvariants.util.ModWoodTypes;
import github.jcsmecabricks.redwoodvariants.worldgen.biome.ModTerrablender;
import github.jcsmecabricks.redwoodvariants.worldgen.biome.surface.ModSurfaceRules;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.EntityType;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import terrablender.api.SurfaceRuleManager;

@Mod(RedwoodVariants.MOD_ID)
public class RedwoodVariants
{
    public static final String MOD_ID = "redwoodvariants";
    private static final Logger LOGGER = LogUtils.getLogger();

    public RedwoodVariants(IEventBus modEventBus, ModContainer modContainer)
    {
        ModEntities.register(modEventBus);
        modEventBus.addListener(this::commonSetup);
        ModItems.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);
        NeoForge.EVENT_BUS.register(this);
        ModTerrablender.registerBiomes();
        modEventBus.addListener(this::addCreative);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, MOD_ID, ModSurfaceRules.makeRules());
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ModEntities.GRIZZLY_BEAR.get(), GrizzlyBearRenderer::new);
            EntityRenderers.register(ModEntities.REDWOOD_BOAT.get(), context -> new ModBoatRenderer(context, ModModelLayers.REDWOOD_BOAT));
            EntityRenderers.register(ModEntities.REDWOOD_CHEST_BOAT.get(), context -> new ModBoatRenderer(context, ModModelLayers.REDWOOD_CHEST_BOAT));
            Sheets.addWoodType(ModWoodTypes.REDWOOD);
        }
    }
}
