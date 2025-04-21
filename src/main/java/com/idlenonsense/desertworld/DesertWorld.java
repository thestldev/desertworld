package com.idlenonsense.desertworld;

import com.idlenonsense.desertworld.block.ModBlocks;
import com.idlenonsense.desertworld.entity.ModEntities;
import com.idlenonsense.desertworld.entity.SandstormEntity;
import com.idlenonsense.desertworld.entity.client.UfoEntity;
import com.idlenonsense.desertworld.events.ModEventListener;
import com.idlenonsense.desertworld.events.ServerTickEvent;
import com.idlenonsense.desertworld.events.impl.AbilitiesEventImpl;
import com.idlenonsense.desertworld.events.listeners.BreakBlockListener;
import com.idlenonsense.desertworld.events.listeners.StepBlockListener;
import com.idlenonsense.desertworld.events.listeners.TrapsEventListener;
import com.idlenonsense.desertworld.gui.SetProgress;
import com.idlenonsense.desertworld.item.ModItems;
import com.idlenonsense.desertworld.scripts.PlayerAbilities;
import com.idlenonsense.desertworld.util.ModLootTablesModifiers;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.passive.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thesalutyt.network.handlers.UfoAttackHandler;

public class DesertWorld implements ModInitializer {
	public static final String MOD_ID = "desertworld";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		PlayerAbilities.registerModAbilities();
		ServerTickEvent.register();
		TrapsEventListener.register();
		StepBlockListener.register();
		BreakBlockListener.register();
		AbilitiesEventImpl.register();

		FabricDefaultAttributeRegistry.register(ModEntities.DESERT_SHEEP, SheepEntity.createSheepAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.DESERT_COW, CowEntity.createCowAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.DESERT_CHICKEN, ChickenEntity.createChickenAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.DESERT_GOLEM, SnowGolemEntity.createSnowGolemAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.DESERT_IRON_GOLEM, IronGolemEntity.createIronGolemAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.UFO_ENTITY, UfoEntity.createUfoAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.SANDSTORM_ENTITY, SandstormEntity.createSandstormAttributes());

		UfoAttackHandler.registerServerReceiver();

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> SetProgress.register(dispatcher));

		ModLootTablesModifiers.modifyLootTables();

		// спавн челюстей для trap_jaw
		ModEventListener.register();
	}
}
