package xyz.amymialee.glide;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import xyz.amymialee.glide.client.GliderEntityModel;

public class GlideClient implements ClientModInitializer {
    public static final EntityModelLayer GLIDER = new EntityModelLayer(Glide.id("glider"), "main");

    @Override
    public void onInitializeClient() {
        EntityModelLayerRegistry.registerModelLayer(GLIDER, GliderEntityModel::getTexturedModelData);
    }
}