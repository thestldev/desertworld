package com.idlenonsense.desertworld.entity.client;

import com.idlenonsense.desertworld.DesertWorld;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class ModModelLayers {
    public static final EntityModelLayer DESERT_SHEEP =
            new EntityModelLayer(new Identifier(DesertWorld.MOD_ID, "desert_sheep"), "main");

    public static final EntityModelLayer UFO_ENTITY =
            new EntityModelLayer(new Identifier(DesertWorld.MOD_ID, "ufo_entity"), "main");

    public static final EntityModelLayer DESERT_COW =
            new EntityModelLayer(new Identifier(DesertWorld.MOD_ID, "desert_cow"), "main");

    public static final EntityModelLayer DESERT_CHICKEN =
            new EntityModelLayer(new Identifier(DesertWorld.MOD_ID, "desert_chicken"), "main");

    public static final EntityModelLayer DESERT_GOLEM =
            new EntityModelLayer(new Identifier(DesertWorld.MOD_ID, "desert_golem"), "main");
//
//    public static final EntityModelLayer ALIEN_WITCH =
//            new EntityModelLayer(new Identifier(DesertWorld.MOD_ID, "alien_witch"), "main");


    public static final EntityModelLayer DESERT_IRON_GOLEM =
            new EntityModelLayer(new Identifier(DesertWorld.MOD_ID, "desert_iron_golem"), "main");
}
