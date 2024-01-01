package xyz.amymialee.glide.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.amymialee.glide.Glide;
import xyz.amymialee.glide.client.GliderFeatureRenderer;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void glider$feature(EntityRendererFactory.@NotNull Context ctx, boolean slim, CallbackInfo ci) {
        this.addFeature(new GliderFeatureRenderer<>(this, ctx.getModelLoader()));
    }

    @Inject(method = "setupTransforms(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/util/math/MatrixStack;FFF)V", at = @At("HEAD"), cancellable = true)
    protected void glider$clip(AbstractClientPlayerEntity player, MatrixStack matrixStack, float f, float g, float h, CallbackInfo ci) {
        var component = Glide.GLIDING_COMPONENT.get(player);
        if (component.isGliding()) {
            player.bodyYaw = player.headYaw;
            player.prevBodyYaw = player.prevHeadYaw;
            super.setupTransforms(player, matrixStack, f, g, h);
            ci.cancel();
        }
    }
}