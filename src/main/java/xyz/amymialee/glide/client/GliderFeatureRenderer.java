package xyz.amymialee.glide.client;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.NotNull;
import xyz.amymialee.glide.Glide;
import xyz.amymialee.glide.GlideClient;

public class GliderFeatureRenderer<T extends PlayerEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
    private static final Identifier SKIN = Glide.id("textures/entity/glider.png");
    private final GliderEntityModel<T> glider;

    public GliderFeatureRenderer(FeatureRendererContext<T, M> context, @NotNull EntityModelLoader loader) {
        super(context);
        this.glider = new GliderEntityModel<>(loader.getModelPart(GlideClient.GLIDER));
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T playerEntity, float f, float g, float h, float j, float k, float l) {
        var component = Glide.GLIDING_COMPONENT.get(playerEntity);
        if (component.isGliding()) {
            matrixStack.push();
            matrixStack.translate(0.0, -1.55, 0.0);
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0f));
            this.getContextModel().copyStateTo(this.glider);
            this.glider.setAngles(playerEntity, f, g, j, k, l);
            var vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumerProvider, RenderLayer.getArmorCutoutNoCull(SKIN), false, false);
            this.glider.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
            matrixStack.pop();
        }
    }
}