package github.jcsmecabricks.redwoodvariants.entity.client;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class ModBoatRenderer extends BoatRenderer {
    public ModBoatRenderer(EntityRendererProvider.Context context, ModelLayerLocation modelLayer) {
        super(context, modelLayer);
    }
}
