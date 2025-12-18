package github.jcsmecabricks.redwoodvariants.entity.client;

import github.jcsmecabricks.redwoodvariants.RedwoodVariants;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.Identifier;

public class ModModelLayers {

    public static final ModelLayerLocation REDWOOD_BOAT = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath(RedwoodVariants.MOD_ID, "boat/redwood"), "main");
    public static final ModelLayerLocation REDWOOD_CHEST_BOAT = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath(RedwoodVariants.MOD_ID, "chest_boat/redwood"), "main");
}
