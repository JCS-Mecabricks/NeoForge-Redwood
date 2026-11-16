package github.jcsmecabricks.redwoodvariants;

import github.jcsmecabricks.redwoodvariants.block.ModBlocks;
import github.jcsmecabricks.redwoodvariants.block.entity.ModBlockEntities;
import github.jcsmecabricks.redwoodvariants.entity.ModEntities;
import github.jcsmecabricks.redwoodvariants.entity.client.GrizzlyBearRenderer;
import github.jcsmecabricks.redwoodvariants.entity.client.ModBoatRenderer;
import github.jcsmecabricks.redwoodvariants.entity.client.ModModelLayers;
import github.jcsmecabricks.redwoodvariants.item.ModCreativeModeTabs;
import github.jcsmecabricks.redwoodvariants.item.ModItems;
import github.jcsmecabricks.redwoodvariants.worldgen.biome.ModBiomes;
import github.jcsmecabricks.redwoodvariants.worldgen.biome.surface.ModMaterialRules;
import net.minecraft.client.renderer.entity.EntityRenderers;
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
import terrablender.api.MaterialRuleManager;

@Mod(RedwoodVariants.MOD_ID)
public class RedwoodVariants {
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
        modEventBus.addListener(this::addCreative);

        modContainer.registerConfig(ModConfig.Type.SYNCED, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModBiomes.registerBiomes();

            MaterialRuleManager.addRules(MaterialRuleManager.RuleCategory.OVERWORLD, MOD_ID, ModMaterialRules::makeRules);
        });
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    @EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ModEntities.GRIZZLY_BEAR.get(), GrizzlyBearRenderer::new);
            EntityRenderers.register(ModEntities.REDWOOD_BOAT.get(), context -> new ModBoatRenderer(context, ModModelLayers.REDWOOD_BOAT));
            EntityRenderers.register(ModEntities.REDWOOD_CHEST_BOAT.get(), context -> new ModBoatRenderer(context, ModModelLayers.REDWOOD_CHEST_BOAT));
        }
    }
}
