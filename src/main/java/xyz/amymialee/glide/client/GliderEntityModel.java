package xyz.amymialee.glide.client;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class GliderEntityModel<T extends LivingEntity> extends AnimalModel<T> {
    private final ModelPart group;
    private final ModelPart bone;

    public GliderEntityModel(@NotNull ModelPart root) {
        this.group = root.getChild("group");
        this.bone = root.getChild("bone");
    }

    public static @NotNull TexturedModelData getTexturedModelData() {
        var modelData = new ModelData();
        var modelPartData = modelData.getRoot();
        var group = modelPartData.addChild("group", ModelPartBuilder.create().uv(25, 19).cuboid(-4.5F, 0.0F, -18.0F, 11.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.0F, 13.0F, 12.0F));
        group.addChild("cube_r1", ModelPartBuilder.create().uv(25, 12).cuboid(-0.75F, -1.5F, -6.5F, 0.0F, 4.0F, 13.0F, new Dilation(0.0F)), ModelTransform.of(-4.0F, 2.5F, -11.5F, 0.0F, -0.0349F, 0.5236F));
        group.addChild("cube_r2", ModelPartBuilder.create().uv(25, 12).cuboid(0.75F, -1.5F, -6.5F, 0.0F, 4.0F, 13.0F, new Dilation(0.0F)), ModelTransform.of(6.0F, 2.5F, -11.5F, 0.0F, 0.0349F, -0.5236F));
        group.addChild("cube_r3", ModelPartBuilder.create().uv(0, 19).cuboid(-1.0F, -1.0F, -19.0F, 2.0F, 2.0F, 20.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, 2.0F, 0.0F, -0.2444F, -0.7703F, 0.1719F));
        group.addChild("cube_r4", ModelPartBuilder.create().uv(0, 19).mirrored().cuboid(-1.0F, -1.0F, -19.0F, 2.0F, 2.0F, 20.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(1.0F, 2.0F, 0.0F, -0.2444F, 0.7703F, -0.1719F));
        var bone = modelPartData.addChild("bone", ModelPartBuilder.create(), ModelTransform.pivot(8.0F, 24.0F, -8.0F));
        bone.addChild("cube_r5", ModelPartBuilder.create().uv(25, 22).cuboid(-5.5F, -1.1625F, -0.55F, 11.0F, 2.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-8.0F, -11.0F, 3.0F, -0.9599F, 0.0F, 0.0F));
        bone.addChild("cube_r6", ModelPartBuilder.create().uv(-18, 0).cuboid(-6.0F, 0.0F, -9.0F, 23.0F, 0.0F, 18.0F, new Dilation(0.0F)), ModelTransform.of(-13.5F, -11.0F, 10.0F, -0.1745F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(T livingEntity, float f, float g, float h, float i, float j) {}

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        this.group.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        this.bone.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    @Override
    protected Iterable<ModelPart> getHeadParts() {
        return ImmutableList.of();
    }

    @Override
    protected Iterable<ModelPart> getBodyParts() {
        return ImmutableList.of(this.group, this.bone);
    }
}