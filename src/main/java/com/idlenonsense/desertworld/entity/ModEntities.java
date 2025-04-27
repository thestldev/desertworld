package com.idlenonsense.desertworld.entity;

import com.idlenonsense.desertworld.DesertWorld;
import com.idlenonsense.desertworld.entity.client.UfoEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class ModEntities {
    public static final EntityType<SheepEntity> DESERT_SHEEP = Registry.register(Registry.ENTITY_TYPE,
            new Identifier(DesertWorld.MOD_ID, "desert_sheep"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, SheepEntity::new)
                    .dimensions(EntityDimensions.fixed(0.9F, 1.3F)).build());

    public static final EntityType<ChickenEntity> DESERT_CHICKEN = Registry.register(Registry.ENTITY_TYPE,
            new Identifier(DesertWorld.MOD_ID, "desert_chicken"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, ChickenEntity::new)
                    .dimensions(EntityDimensions.fixed(0.4F, 0.7F)).build());

    public static final EntityType<CowEntity> DESERT_COW = Registry.register(Registry.ENTITY_TYPE,
            new Identifier(DesertWorld.MOD_ID, "desert_cow"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, CowEntity::new)
                    .dimensions(EntityDimensions.fixed(0.9F, 1.4F)).build());

    public static final EntityType<SnowGolemEntity> DESERT_GOLEM = Registry.register(Registry.ENTITY_TYPE,
            new Identifier(DesertWorld.MOD_ID, "desert_golem"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, SnowGolemEntity::new)
                    .dimensions(EntityDimensions.fixed(0.7F, 1.9F)).build());

    public static final EntityType<IronGolemEntity> DESERT_IRON_GOLEM = Registry.register(Registry.ENTITY_TYPE,
            new Identifier(DesertWorld.MOD_ID, "desert_iron_golem"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, IronGolemEntity::new)
                    .dimensions(EntityDimensions.fixed(1.4F, 2.7F)).build());
//
//    public static final EntityType<WitchEntity> ALIEN_WITCH = Registry.register(Registry.ENTITY_TYPE,
//            new Identifier(DesertWorld.MOD_ID, "alien_witch"),
//            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, WitchEntity::new)
//                    .dimensions(EntityDimensions.fixed(0.6F, 1.95F)).build());

    public static final EntityType<UfoEntity> UFO_ENTITY = Registry.register(Registry.ENTITY_TYPE,
            new Identifier(DesertWorld.MOD_ID, "ufo_entity"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, UfoEntity::new)
                    .dimensions(EntityDimensions.fixed(1.0F, 1.0F)).build());

//    public static final EntityType<SandstormEntity> SANDSTORM_ENTITY = Registry.register(Registry.ENTITY_TYPE,
//            new Identifier(DesertWorld.MOD_ID, "sandstorm_entity"),
//            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, SandstormEntity::new)
//                    .dimensions(EntityDimensions.fixed(1.0F, 1.0F)).build());

}
