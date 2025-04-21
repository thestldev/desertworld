package com.idlenonsense.desertworld.entity.client.modified;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.SheepWoolFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.SheepEntityModel;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.Identifier;

public class DesertSheepRenderer extends MobEntityRenderer<SheepEntity, SheepEntityModel<SheepEntity>> {
    private static final Identifier TEXTURE = new Identifier("textures/entity/desert_sheep.png");

    public DesertSheepRenderer(EntityRendererFactory.Context context) {
        super(context, new SheepEntityModel<>(context.getPart(EntityModelLayers.SHEEP)), 0.7F);
        this.addFeature(new SheepWoolFeatureRenderer(this, context.getModelLoader()));
    }

    public Identifier getTexture(SheepEntity sheepEntity) {
        return TEXTURE;
    }
}
