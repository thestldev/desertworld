package com.idlenonsense.desertworld.converter;

import com.idlenonsense.desertworld.entity.ModEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class EntitiesConverter {
    public static void convertEntities(BlockPos pos, int radius, World world) {
        Box box = new Box(
                pos.getX() - radius,
                pos.getY() - radius,
                pos.getZ() - radius,
                pos.getX() + radius,
                pos.getY() + radius,
                pos.getZ() + radius
        );
        convertEntities(box, world);
    }

    public static void convertEntities(Box box, World world) {
        for (Entity entity :
                world.getEntitiesByClass(Entity.class, box, e -> !(e.getType().equals(EntityType.PLAYER)))
        ) {
            processEntity(entity, world);
        }
    }

    private static void processEntity(Entity entity, World world) {
        EntityType<?> type = entity.getType();

        Entity newEntity = null;
        if (type.equals(EntityType.SHEEP)) {
            newEntity = new SheepEntity(ModEntities.DESERT_SHEEP, world);
        }
        if (type.equals(EntityType.CHICKEN)) {
            newEntity = new ChickenEntity(ModEntities.DESERT_CHICKEN, world);
        }
        if (type.equals(EntityType.COW)) {
            newEntity = new CowEntity(ModEntities.DESERT_COW, world);
        }
        if (type.equals(EntityType.IRON_GOLEM)) {
            newEntity = new IronGolemEntity(ModEntities.DESERT_IRON_GOLEM, world);
        }
        if (type.equals(EntityType.SNOW_GOLEM)) {
            newEntity = new SnowGolemEntity(ModEntities.DESERT_GOLEM, world);
        }

        if (newEntity != null) {
            newEntity.setPosition(
                    entity.getX(),
                    entity.getY() + 0.512,
                    entity.getZ()
            );
            world.spawnEntity(newEntity);
            entity.remove(Entity.RemovalReason.DISCARDED);
        }
    }
}
