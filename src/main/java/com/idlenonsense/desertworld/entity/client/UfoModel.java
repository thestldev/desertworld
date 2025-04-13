package com.idlenonsense.desertworld.entity.client;

import com.idlenonsense.desertworld.DesertWorld;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class UfoModel extends AnimatedGeoModel<UfoEntity> {

    @Override
    public Identifier getModelResource(UfoEntity ufoEntity) {
        return new Identifier(DesertWorld.MOD_ID, "geo/ufo.geo.json");
    }

    @Override
    public Identifier getTextureResource(UfoEntity ufoEntity) {
        return new Identifier(DesertWorld.MOD_ID, "textures/entity/ufo.png");
    }

    @Override
    public Identifier getAnimationResource(UfoEntity ufoEntity) {
        return new Identifier(DesertWorld.MOD_ID, "animations/ufo.animation.json");
    }
}
