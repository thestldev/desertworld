package com.idlenonsense.desertworld.entity.client.modified;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.CowEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.WitchEntityModel;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.util.Identifier;

public class AlienWitchRenderer extends MobEntityRenderer<WitchEntity, WitchEntityModel<WitchEntity>> {
    private static final Identifier TEXTURE = new Identifier("textures/entity/desert_cow.png");

    public AlienWitchRenderer(EntityRendererFactory.Context context) {
        super(context, new WitchEntityModel<>(context.getPart(EntityModelLayers.COW)), 0.7F);
    }

    @Override
    public Identifier getTexture(WitchEntity cowEntity) {
        return TEXTURE;
    }
}