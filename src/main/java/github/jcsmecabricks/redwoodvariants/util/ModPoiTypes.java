package github.jcsmecabricks.redwoodvariants.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import github.jcsmecabricks.redwoodvariants.block.ModBlocks;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.neoforged.neoforge.registries.GameData;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class ModPoiTypes {

    public static final ResourceKey<PoiType> REDWOOD_PORTAL = createKey("redwood_portal");
    private static final Map<BlockState, Holder<PoiType>> TYPE_BY_STATE;

    private static Set<BlockState> getBlockStates(Block block) {
        return ImmutableSet.copyOf(block.getStateDefinition().getPossibleStates());
    }

    private static ResourceKey<PoiType> createKey(String name) {
        return ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, ResourceLocation.withDefaultNamespace(name));
    }

    private static PoiType register(Registry<PoiType> key, ResourceKey<PoiType> value, Set<BlockState> matchingStates, int maxTickets, int validRange) {
        PoiType poitype = new PoiType(matchingStates, maxTickets, validRange);
        Registry.register(key, value, poitype);
        registerBlockStates(key.getOrThrow(value), matchingStates);
        return poitype;
    }

    private static void registerBlockStates(Holder<PoiType> poi, Set<BlockState> states) {
        states.forEach((p_218081_) -> {
            Holder holder = TYPE_BY_STATE.put(p_218081_, poi);
            if (holder != null) {
                throw Util.pauseInIde(new IllegalStateException(String.format(Locale.ROOT, "%s is defined in more than one PoI type", p_218081_)));
            }
        });
    }

    public static Optional<Holder<PoiType>> forState(BlockState state) {
        return Optional.ofNullable(TYPE_BY_STATE.get(state));
    }

    public static boolean hasPoi(BlockState state) {
        return TYPE_BY_STATE.containsKey(state);
    }

    public static PoiType bootstrap(Registry<PoiType> registry) {
        return register(registry, REDWOOD_PORTAL, getBlockStates(ModBlocks.REDWOOD_PORTAL.get()), 0, 1);
    }

    static {
        TYPE_BY_STATE = GameData.getBlockStatePointOfInterestTypeMap();
    }
}
