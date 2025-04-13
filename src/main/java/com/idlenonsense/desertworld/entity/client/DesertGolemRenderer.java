package com.idlenonsense.desertworld.entity.client;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.SnowmanPumpkinFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.SnowGolemEntityModel;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.util.Identifier;

public class DesertGolemRenderer extends MobEntityRenderer<SnowGolemEntity, SnowGolemEntityModel<SnowGolemEntity>> {
    private static final Identifier TEXTURE = new Identifier("textures/entity/desert_golem.png");

    public DesertGolemRenderer(EntityRendererFactory.Context context) {
        super(context, new SnowGolemEntityModel<>(context.getPart(EntityModelLayers.SNOW_GOLEM)), 0.5F);
        this.addFeature(new SnowmanPumpkinFeatureRenderer(this, context.getBlockRenderManager(), context.getItemRenderer()));
    }

    public Identifier getTexture(SnowGolemEntity snowGolemEntity) {
        return TEXTURE;
    }
}
