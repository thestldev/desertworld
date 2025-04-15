package com.idlenonsense.desertworld.block;

import com.idlenonsense.desertworld.DesertWorld;
import com.idlenonsense.desertworld.item.ModItemGroup;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlocks {
    public static final Block CACTUS_TABLE = registerBlock("cactus_table",
            new CactusTable(FabricBlockSettings.of(Material.CACTUS).strength(0.4f).sounds(BlockSoundGroup.WOOL)), ModItemGroup.DESERTWORLD);
    public static final Block HOURGLASS = registerBlock("hourglass",
            new HourglassBlock(FabricBlockSettings.of(Material.GLASS).strength(0.1f).sounds(BlockSoundGroup.GLASS)), ModItemGroup.DESERTWORLD);
    public static final Block MAGIC_CARPET = registerBlock("magic_carpet",
            new Block(FabricBlockSettings.of(Material.GLASS).strength(0.1f).sounds(BlockSoundGroup.GLASS)), ModItemGroup.DESERTWORLD);

    public static final Block TRAP_JAW = registerBlock("trap_jaw",
            new TrapJawBlock(FabricBlockSettings.of(Material.STONE).strength(2.0f).sounds(BlockSoundGroup.STONE)), ModItemGroup.DESERTWORLD);
    public static final Block ARROW_TRAP = registerBlock("arrow_trap",
            new ArrowTrapBlock(FabricBlockSettings.of(Material.METAL).strength(2.0f).sounds(BlockSoundGroup.STONE)), ModItemGroup.DESERTWORLD);
    public static final Block LASER_TRAP = registerBlock("laser_trap",
            new LaserTrapBlock(FabricBlockSettings.of(Material.METAL).strength(2.0f).sounds(BlockSoundGroup.STONE)), ModItemGroup.DESERTWORLD);
    
    private static Block registerBlock(String name, Block block, ItemGroup tab) {
        registerBlockItem(name, block, tab);
        return Registry.register(Registry.BLOCK, new Identifier(DesertWorld.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block, ItemGroup tab) {
        return Registry.register(Registry.ITEM, new Identifier(DesertWorld.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings().group(tab)));
    }

    public static void registerModBlocks() {
        DesertWorld.LOGGER.debug("Регистрация блоков для " + DesertWorld.MOD_ID);
    }

}
