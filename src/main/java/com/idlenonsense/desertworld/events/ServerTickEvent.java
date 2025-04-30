package com.idlenonsense.desertworld.events;

import com.idlenonsense.desertworld.abilities.Abilities;
import com.idlenonsense.desertworld.bar.DesertBar;
import com.idlenonsense.desertworld.currency.DesertCurrency;
import com.idlenonsense.desertworld.entity.client.UfoEntity;
import com.idlenonsense.desertworld.item.ModItems;
import com.idlenonsense.desertworld.util.WorldUpdater;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.thesalutyt.utils.ServerUfoController;
import java.util.List;
import java.util.ArrayList;
import static com.idlenonsense.desertworld.converter.BlocksConverter.convertBlocksWithAffectOnCurrency;

public class ServerTickEvent {
    private static BlockPos cachedPos = null;
    private static boolean sandstormActive = false;
    private static BlockPos sandstormPos = null;
    private static int ticksElapsed = 0;
    private static final int GRAVITY_DELAY = 20;
    private static int gravityDelayTicks = 0;
    private static List<Entity> sandstormEntities = new ArrayList<>();

    public static void register() {
        ServerTickEvents.START_SERVER_TICK
                .register(minecraftServer -> {
                    try {
                        if (minecraftServer.getPlayerManager().getPlayerList().isEmpty()) return;

                        for (ServerPlayerEntity player : minecraftServer.getPlayerManager().getPlayerList()) {
                            tickPlayer(player);
                            updateUfoMovement(player);
                            tickSandstorm(player);
                        }
                    } catch (Exception ignored) {}
                });
    }

    private static void moveEntitiesInSandstorm(ServerWorld world) {
        if (sandstormPos == null) return;
        sandstormEntities = world.getOtherEntities(null, new Box(sandstormPos.getX() - 10, sandstormPos.getY() - 5, sandstormPos.getZ() - 10, sandstormPos.getX() + 10, sandstormPos.getY() + 25, sandstormPos.getZ() + 10));
        for (Entity entity : sandstormEntities) {
            if (!(entity instanceof PlayerEntity)) {
                Vec3d towards = new Vec3d(sandstormPos.getX(), sandstormPos.getY() + 1, sandstormPos.getZ()).subtract(entity.getPos()).normalize();
                Vec3d move = towards.multiply(0.5D, 0.5D, 0.5D);
                entity.setVelocity(move.x, move.y + 0.05D, move.z);
                entity.setNoGravity(true);
            }
        }
    }

    private static void restoreGravityForEntities() {
        if (sandstormPos == null) return;
        for (Entity entity : sandstormEntities) {
            if (!(entity instanceof PlayerEntity)) {
                entity.setNoGravity(false);
                Vec3d currentVelocity = entity.getVelocity();
                double randomAngle = Math.random() * 2 * Math.PI;
                double randomSpeed = 0.5 + Math.random() * 0.5;
                double xOffset = Math.cos(randomAngle) * randomSpeed;
                double zOffset = Math.sin(randomAngle) * randomSpeed;
                double yOffset = 0.3 + Math.random() * 0.2;
                entity.setVelocity(currentVelocity.x + xOffset, currentVelocity.y + yOffset, currentVelocity.z + zOffset);
            }
        }
        sandstormEntities.clear();
    }

    private static void tickSandstorm(ServerPlayerEntity player) {
        if (sandstormActive) {
            if (ticksElapsed < 120) {
                if (ticksElapsed < 120 - 20) {
                    if (ticksElapsed % 5 == 0) {
                        spawnSandstormParticles(player.getWorld(), sandstormPos);
                        moveEntitiesInSandstorm(player.getWorld());
                        convertBlocksWithAffectOnCurrency(sandstormPos, player.getWorld(), 15);
                    }
                } else { restoreGravityForEntities(); }
                ticksElapsed++;
            } else {
                sandstormActive = false;
                gravityDelayTicks = GRAVITY_DELAY;
            }
        }
        if (gravityDelayTicks > 0) {
            gravityDelayTicks--;
            if (gravityDelayTicks == 0) {
                restoreGravityForEntities();
            }
        }
    }

    public static void startSandstorm(BlockPos pos) {
        if (!sandstormActive) {
            sandstormPos = pos;
            sandstormActive = true;
            ticksElapsed = 0;
        }
    }

    private static void spawnSandstormParticles(World world, BlockPos pos) {
        if (world instanceof ServerWorld serverWorld) {
            int radius = 5;
            int count = 100;
            int layers = 8;
            double step = radius / (double) layers;
            for (int j = 0; j < layers; j++) {
                double currentRadius = radius - step * j;
                for (int i = 0; i < count; ++i) {
                    float angle = (float) i / count * 6.2831855F;
                    double cos = Math.cos(angle) * currentRadius;
                    double sin = Math.sin(angle) * currentRadius;
                    double height = pos.getY() + 7.5D - (j * 1.0D);
                    serverWorld.spawnParticles(ParticleTypes.CLOUD,
                            pos.getX() + cos,
                            height,
                            pos.getZ() + sin,
                            1, 0.4D, 0.4D, 0.4D, 0.0D);
                }
            }
        }
    }

    public static void handleSandstormItemUse(PlayerEntity user, World world) {
        if (!world.isClient) {
            startSandstorm(user.getBlockPos());
            WorldUpdater.worldDeserted(sandstormPos, user.getWorld(), 15);
            syncCurrencyWithBar((ServerPlayerEntity) user);
        }
    }

    private static void updateUfoMovement(ServerPlayerEntity player) {
        ServerUfoController controller = ServerUfoController.getControllerByUUID(player.getUuid());
        //System.out.println("Controller: " + controller);
        if (controller != null) {
            //System.out.println("Test2");
            UfoEntity ufoEntity = controller.entity();
            if (ufoEntity != null) {
                //System.out.println("Test3");
                ufoEntity.moveToPlayer(player.getPos());
            }
        }
    }

    private static void tickPlayer(ServerPlayerEntity player) {
        pollPos(player);
        pollAbilities();
        pollCallbacks(player);
    }

    private static void pollCallbacks(ServerPlayerEntity player) {
        if (!isCursedTool(player.getMainHandStack())) return;
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 20, 1));
    }

    private static void pollPos(ServerPlayerEntity player) {
        if (cachedPos == null) cachedPos = player.getBlockPos();

        if (!player.getBlockPos().equals(cachedPos)) {
            cachedPos = player.getBlockPos();
            WorldUpdater.worldDeserted(cachedPos, player.getWorld(), 3);
            syncCurrencyWithBar(player);
        }
    }

    private static void pollAbilities() {
        Abilities.getInstance().enable(DesertCurrency.getInstance().get());
    }

    public static void syncCurrencyWithBar(ServerPlayerEntity player) {
        DesertCurrency currency = DesertCurrency.getInstance();
        float progress = currency.get();
        DesertBar.update(player, progress);
        //System.out.println(currency.get());
    }

    private static boolean isCursedTool(ItemStack stack) {
        return stack.getItem().equals(ModItems.CURSED_PICKAXE)
                || stack.getItem().equals(ModItems.CURSED_AXE)
                || stack.getItem().equals(ModItems.CURSED_SHOVEL)
                || stack.getItem().equals(ModItems.CURSED_SWORD);
    }
}
