package com.idlenonsense.desertworld.entity.client;

import com.idlenonsense.desertworld.item.ModItems;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.random.Random;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;

@Deprecated
public class OldUfoRenderer extends EntityRenderer<UfoEntity> {
    private final ItemRenderer itemRenderer;
    private final Random random = Random.create();

    public OldUfoRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.shadowRadius = 0.5F;
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(UfoEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        ItemStack itemStack = ModItems.UFO.getDefaultStack();
        int seed = 187;
        this.random.setSeed((long) seed);
        BakedModel bakedModel = this.itemRenderer.getModel(itemStack, entity.world, (LivingEntity) null, entity.getId());
        float scale = bakedModel.getTransformation().getTransformation(ModelTransformation.Mode.GROUND).scale.getY();
        matrices.translate(0.0D, (double) (0.25F * scale), 0.0D);
        float rotation = entity.getPitch(tickDelta);
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(rotation));
        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-90.0F));
        matrices.push();
        this.itemRenderer.renderItem(itemStack, ModelTransformation.Mode.GROUND, false, matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV, bakedModel);
        matrices.pop();
        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(UfoEntity entity) {
        return new Identifier("textures/entity/ufo.png");
    }
}
