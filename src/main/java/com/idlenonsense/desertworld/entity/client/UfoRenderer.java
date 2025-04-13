package com.idlenonsense.desertworld.entity.client;

import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class UfoRenderer extends GeoEntityRenderer<UfoEntity> {
    public UfoRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new UfoModel());
    }
}
