package com.idlenonsense.desertworld.mixin;

import com.idlenonsense.desertworld.entity.ModEntities;
import com.idlenonsense.desertworld.gui.ProgressBar;
import com.idlenonsense.desertworld.scripts.KeyInputs;
import com.idlenonsense.desertworld.scripts.PlayerAbilities;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.client.render.entity.model.SnowGolemEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerMixin {

   private int tickCounter = 0;
   private final List<Block> sandList = List.of(Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.PODZOL);
   private final List<Block> sandstoneList = List.of(Blocks.STONE, Blocks.GRANITE, Blocks.ANDESITE, Blocks.COBBLESTONE);
   private final List<Block> cactusList = List.of(Blocks.OAK_LOG, Blocks.ACACIA_LOG, Blocks.SPRUCE_LOG, Blocks.BIRCH_LOG,
           Blocks.DARK_OAK_LOG, Blocks.JUNGLE_LOG, Blocks.MANGROVE_LOG);

   @Inject(method = "tick", at = @At("HEAD"))
   private void onTick(CallbackInfo ci) {
      ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
//      World world
//              = player.getWorld();
//      BlockPos playerPos = player.getBlockPos();
//      int radius = 3;
//
//      if (KeyInputs.desertProgress) {
//         for (int x = -radius; x <= radius; x++) {
//            for (int y = -2; y <= 5; y++) {
//               for (int z = -radius; z <= radius; z++) {
//                  Box box = new Box(playerPos.getX() - radius, playerPos.getY() - 2, playerPos.getZ() - radius,
//                          playerPos.getX() + radius, playerPos.getY() + 5, playerPos.getZ() + radius);
//
//                  for (Entity entity : world.getEntitiesByClass(Entity.class, box, e -> e != player)) {
//                     if (entity.getType() == EntityType.SHEEP) {
//                        SheepEntity sheep = new SheepEntity(ModEntities.DESERT_SHEEP, world);
//                        sheep.setPos(entity.getX(), entity.getY(), entity.getZ());
//                        world.spawnEntity(sheep);
//                        entity.remove(Entity.RemovalReason.DISCARDED);
//                     } else if (entity.getType() == EntityType.CHICKEN) {
//                        ChickenEntity chicken = new ChickenEntity(ModEntities.DESERT_CHICKEN, world);
//                        chicken.setPos(entity.getX(), entity.getY(), entity.getZ());
//                        world.spawnEntity(chicken);
//                        entity.remove(Entity.RemovalReason.DISCARDED);
//                     } else if (entity.getType() == EntityType.IRON_GOLEM) {
//                        IronGolemEntity ironGolem = new IronGolemEntity(ModEntities.DESERT_IRON_GOLEM, world);
//                        ironGolem.setPos(entity.getX(), entity.getY(), entity.getZ());
//                        world.spawnEntity(ironGolem);
//                        entity.remove(Entity.RemovalReason.DISCARDED);
//                     } else if (entity.getType() == EntityType.COW) {
//                        CowEntity cow = new CowEntity(ModEntities.DESERT_COW, world);
//                        cow.setPos(entity.getX(), entity.getY(), entity.getZ());
//                        world.spawnEntity(cow);
//                        entity.remove(Entity.RemovalReason.DISCARDED);
//                     }
//                  }
//
//
//                  BlockPos pos = playerPos.add(x, y, z);
//                  if (world.getBlockState(pos).getBlock() == Blocks.WATER) {
//                     world.setBlockState(pos, Blocks.AIR.getDefaultState());
//                  } else if (sandList.contains(world.getBlockState(pos).getBlock())) {
//                     world.setBlockState(pos, Blocks.SAND.getDefaultState());
//                     ProgressBar.updateProgressBar(ProgressBar.progressBar, 0.00001f, player);
//                  } else if (cactusList.contains(world.getBlockState(pos).getBlock())) {
//                     world.setBlockState(pos, Blocks.CACTUS.getDefaultState());
//                  } else if (sandstoneList.contains(world.getBlockState(pos).getBlock())) {
//                     world.setBlockState(pos, Blocks.SANDSTONE.getDefaultState());
//                     ProgressBar.updateProgressBar(ProgressBar.progressBar, 0.00001f, player);
//                  } else if (world.getBlockState(pos).getMaterial() == Material.LEAVES) {
//                     world.setBlockState(pos, Blocks.AIR.getDefaultState());
//                  }
//               }
//            }
//         }
//      }
//
//      if (world.getBlockState(playerPos.down()).getBlock() == Blocks.SANDSTONE && PlayerAbilities.desertSkin) {
//         tickCounter++;
//         if (tickCounter >= 20) { // 20 тиков = 1 секунда
//            tickCounter = 0;
//            EntityAttributeInstance armorAttribute = player.getAttributeInstance(EntityAttributes.GENERIC_ARMOR);
//            if (armorAttribute != null) {
//               armorAttribute.setBaseValue(armorAttribute.getBaseValue() + 0.5);
//            }
//         }
//      } else {
//         tickCounter++;
//         if (tickCounter >= 60) {
//            tickCounter = 0;
//            EntityAttributeInstance armorAttribute = player.getAttributeInstance(EntityAttributes.GENERIC_ARMOR);
//            if (armorAttribute != null && armorAttribute.getBaseValue() > 0) {
//               armorAttribute.setBaseValue(armorAttribute.getBaseValue() - 0.5);
//            }
//         }
//      }
   }
}
