package com.idlenonsense.desertworld.item;

import com.idlenonsense.desertworld.DesertWorld;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ModItemGroup {
    public static final ItemGroup DESERTWORLD = FabricItemGroupBuilder.build(
            new Identifier(DesertWorld.MOD_ID, "desertworld"), () -> new ItemStack(ModItems.DESERT_HEART));
}
