package com.idlenonsense.desertworld.util;

import com.idlenonsense.desertworld.block.ModBlocks;
import com.idlenonsense.desertworld.item.ModItems;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.util.Identifier;

public class ModLootTablesModifiers {
    private static final Identifier WHITE_SHEEP_ID
            = new Identifier("minecraft", "entities/sheep/white");
    private static final Identifier ARCTIC_BEAR
            = new Identifier("minecraft", "entities/polar_bear");

    public static void modifyLootTables() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, identifier, builder, lootTableSource) -> {
            if(WHITE_SHEEP_ID.equals(identifier)) {
                LootPool.Builder poolProvider = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(1f))
                        .with(ItemEntry.builder(ModBlocks.MAGIC_CARPET));
                builder.pool(poolProvider.build());
            }

            if(ARCTIC_BEAR.equals(identifier)) {
                LootPool.Builder poolProvider = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(1f))
                        .with(ItemEntry.builder(ModItems.ARCTIC_TEAR));
                builder.pool(poolProvider.build());
            }
        });
    }
}
