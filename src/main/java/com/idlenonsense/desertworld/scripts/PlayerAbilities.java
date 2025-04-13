package com.idlenonsense.desertworld.scripts;

import com.idlenonsense.desertworld.DesertWorld;
import com.idlenonsense.desertworld.abilities.IAbilitiesProvider;
import com.idlenonsense.desertworld.item.ModItems;
import com.idlenonsense.desertworld.abilities.Abilities;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class PlayerAbilities {
    @Deprecated
    public static boolean desertSkin = false;
    @Deprecated
    public static boolean sandstoneTools = false;
    @Deprecated
    public static boolean desertHeart = false;
    @Deprecated
    public static boolean structureZero = false;
    @Deprecated
    public static boolean structureOne = false;
    @Deprecated
    public static boolean structureTwo = false;
    @Deprecated
    public static boolean sandClock = false;
    @Deprecated
    public static boolean cursedGold = false;
    @Deprecated
    public static boolean vultureWings = false;
    @Deprecated
    public static boolean structureThree = false;
    @Deprecated
    public static boolean desertSun = false;
    @Deprecated
    public static boolean structureFour = false;

    public static void registerModAbilities() {
        DesertWorld.LOGGER.info("Регистрация возможностей для " + DesertWorld.MOD_ID);

        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            ItemStack heldItem = player.getMainHandStack();
            boolean increaseDrop = heldItem.getItem() == ModItems.CURSED_PICKAXE || heldItem.getItem() == ModItems.CURSED_AXE || heldItem.getItem() == ModItems.CURSED_SHOVEL;
            if (increaseDrop) {
                List<ItemStack> drops = Block.getDroppedStacks(state, (ServerWorld) world, pos, blockEntity, player, heldItem);
                for (ItemStack drop : drops) {
                    drop.setCount(drop.getCount() * 3);
                    Block.dropStack(world, pos, drop);
                }
            }
        });
    }

    private static void addAbilities() {
        clear();
        addAbility(new Abilities.SpecialAbility("desertSkin", 2));
        addAbility(new Abilities.SpecialAbility("sandstoneTools", 5));
        addAbility(new Abilities.SpecialAbility("desertHeart", 10));
        addAbility(new Abilities.SpecialAbility("structureZero", 15));
        addAbility(new Abilities.SpecialAbility("structureOne", 25) {
            @Override
            public void onEnable() {
                KeyInputs.targetPos = new BlockPos(5342, 65, -157);
            }
        });
        addAbility(new Abilities.SpecialAbility("structureTwo", 35));
        addAbility(new Abilities.SpecialAbility("sandClock", 50) {
            @Override
            public void onEnable() {
                KeyInputs.targetPos = new BlockPos(4320, 74, -288);
            }
        });
        addAbility(new Abilities.SpecialAbility("cursedGold", 55));
        addAbility(new Abilities.SpecialAbility("vultureWings", 70));
        addAbility(new Abilities.SpecialAbility("structureThree", 75) {
            @Override
            public void onEnable() {
                KeyInputs.targetPos = new BlockPos(4553, 66, 116);
            }
        });
        addAbility(new Abilities.SpecialAbility("desertSun", 85));
        addAbility(new Abilities.SpecialAbility("structureFour", 100) {
            @Override
            public void onEnable() {
                KeyInputs.targetPos = new BlockPos(4832, 70, 524);
            }
        });
    }

    private static void addAbility(Abilities.Ability specialAbility) {
        Abilities.getInstance()
                .addAbility(specialAbility);
    }

    private static void clear() {
        Abilities.getInstance()
                .clear();
    }
}
