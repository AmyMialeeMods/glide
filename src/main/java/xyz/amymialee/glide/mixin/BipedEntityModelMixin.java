package xyz.amymialee.glide.mixin;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.amymialee.glide.Glide;

@Mixin(BipedEntityModel.class)
public class BipedEntityModelMixin<T extends LivingEntity> {
    @Shadow @Final public ModelPart rightArm;
    @Shadow @Final public ModelPart leftArm;
    @Shadow public float leaningPitch;

    @Inject(method = "positionRightArm", at = @At("HEAD"), cancellable = true)
    private void glide$holdRight(T entity, CallbackInfo ci) {
        var component = Glide.GLIDING_COMPONENT.getNullable(entity);
        if (component != null && component.isGliding()) {
            this.rightArm.pitch = (float) (Math.PI);
            this.rightArm.yaw = 0.0F;
            this.rightArm.roll = (float) (-Math.PI / 16);
            ci.cancel();
        }
    }

    @Inject(method = "positionLeftArm", at = @At("HEAD"), cancellable = true)
    private void glide$holdLeft(T entity, CallbackInfo ci) {
        var component = Glide.GLIDING_COMPONENT.getNullable(entity);
        if (component != null && component.isGliding()) {
            this.leftArm.pitch = (float) (Math.PI);
            this.leftArm.yaw = 0.0F;
            this.leftArm.roll = (float) (Math.PI / 16);
            ci.cancel();
        }
    }

    @Inject(method = "animateModel(Lnet/minecraft/entity/LivingEntity;FFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/AnimalModel;animateModel(Lnet/minecraft/entity/Entity;FFF)V", shift = At.Shift.BEFORE))
    private void glide$stillArms(T livingEntity, float f, float g, float h, CallbackInfo ci) {
        var component = Glide.GLIDING_COMPONENT.getNullable(livingEntity);
        if (component != null && component.isGliding()) {
            this.leaningPitch = 0;
        }
    }

    @Inject(method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
    private void glide$cease(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
//        this.leftArm.pitch = 0F;
//        this.rightArm.pitch = 0F;
    }
}