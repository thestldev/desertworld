package com.idlenonsense.desertworld.scripts;

import com.idlenonsense.desertworld.item.ModItems;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class KeyInputs {
    public static String KEY_KATEGORY_DESERTWORLD = "key.category.desertworld.desert_world";
    public static String KEY_STOP_DESERTPROGRESS = "key.desertworld.stop_desert_progress";
    public static KeyBinding desertProgressKey;
    public static boolean desertProgress = true;
    public static BlockPos targetPos = new BlockPos(4303,68,168);

    public static void createParticleArrow(ClientPlayerEntity player, ClientWorld world) {
        Vec3d playerPos = player.getPos();
        Vec3d targetVec = Vec3d.ofCenter(targetPos);

        Vec3d direction = targetVec.subtract(playerPos).normalize();

        for (double i = 0; i < 5; i += 0.5) {
            Vec3d particlePos = playerPos.add(direction.multiply(i));
            world.addParticle(ParticleTypes.END_ROD, particlePos.x, particlePos.y + 5, particlePos.z, 0, 0, 0);
        }

        Vec3d arrowHeadPos = playerPos.add(direction.multiply(5));
        world.addParticle(ParticleTypes.FLAME, arrowHeadPos.x, arrowHeadPos.y + 5, arrowHeadPos.z, 0, 0, 0);
    }

    public static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(minecraftClient -> {
            if(desertProgressKey.wasPressed()) {
                desertProgress = !desertProgress;
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(server -> {
            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.player != null && mc.world != null) {
                ItemStack mainHandItem = mc.player.getMainHandStack();
                ItemStack offHandItem = mc.player.getOffHandStack();

                if (mainHandItem.getItem() == ModItems.DOWSING_ROD && offHandItem.getItem() == ModItems.DOWSING_ROD) {
                    createParticleArrow(mc.player, (ClientWorld) mc.world);
                }
            }
        });
    }


    public static void register() {
        desertProgressKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_STOP_DESERTPROGRESS,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_K,
                KEY_KATEGORY_DESERTWORLD
        ));
        registerKeyInputs();
    }
}
