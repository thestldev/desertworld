package com.idlenonsense.desertworld.scripts;

import com.idlenonsense.desertworld.item.ModItems;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class KeyInputs {
    public static String KEY_KATEGORY_DESERTWORLD = "key.category.desertworld.desert_world";
    public static String KEY_STOP_DESERTPROGRESS = "key.desertworld.stop_desert_progress";
    public static KeyBinding desertProgressKey;
    public static boolean desertProgress = true;
    public static BlockPos targetPos = new BlockPos(-4303,68,10168);
    private static int tickCounter = 0;

    public static void createParticleArrow(ClientPlayerEntity player, ClientWorld world) {
        Vec3d playerPos = player.getPos();
        Vec3d targetVec = Vec3d.ofCenter(targetPos);
        Vec3d direction = targetVec.subtract(playerPos).normalize();
        double arrowLength = 5.0;
        double arrowWidth = 1.0;
        double arrowHeadLength = 2.0;
        double arrowHeadWidth = 3.5;
        double heightOffset = 4.0;
        Vec3d up = new Vec3d(0, 1, 0);
        Vec3d right = direction.crossProduct(up).normalize();
        double tailLength = arrowLength - arrowHeadLength;
        for (double i = 0; i <= tailLength; i += 0.3) {
            Vec3d basePos = playerPos.add(direction.multiply(i));
            Vec3d leftPos = basePos.add(right.multiply(-arrowWidth/2));
            Vec3d rightPos = basePos.add(right.multiply(arrowWidth/2));
            world.addParticle(ParticleTypes.FLAME, leftPos.x, leftPos.y + heightOffset, leftPos.z, 0, 0, 0);
            world.addParticle(ParticleTypes.FLAME, rightPos.x, rightPos.y + heightOffset, rightPos.z, 0, 0, 0);
        }
        Vec3d tailEnd = playerPos.add(direction.multiply(tailLength));
        for (double w = -arrowWidth/2; w <= arrowWidth/2; w += 0.2) {
            Vec3d closingPos = playerPos.add(right.multiply(w)).add(0, 0, 0);
            world.addParticle(ParticleTypes.FLAME, closingPos.x, closingPos.y + heightOffset, closingPos.z, 0, 0, 0);
            Vec3d closingPos1 = tailEnd.add(right.multiply(w));
            world.addParticle(ParticleTypes.FLAME, closingPos1.x, closingPos1.y + heightOffset, closingPos1.z, 0, 0, 0);
        }
        Vec3d arrowTip = playerPos.add(direction.multiply(arrowLength));
        Vec3d leftBase = tailEnd.add(right.multiply(-arrowHeadWidth/2));
        Vec3d rightBase = tailEnd.add(right.multiply(arrowHeadWidth/2));
        for (double t = 0; t <= 1; t += 0.1) {
            Vec3d point = new Vec3d(
                    leftBase.x + (arrowTip.x - leftBase.x) * t,
                    leftBase.y + (arrowTip.y - leftBase.y) * t,
                    leftBase.z + (arrowTip.z - leftBase.z) * t
            );
            world.addParticle(ParticleTypes.FLAME, point.x, point.y + heightOffset, point.z, 0, 0, 0);
            Vec3d pointRight = new Vec3d(
                    rightBase.x + (arrowTip.x - rightBase.x) * t,
                    rightBase.y + (arrowTip.y - rightBase.y) * t,
                    rightBase.z + (arrowTip.z - rightBase.z) * t
            );
            world.addParticle(ParticleTypes.FLAME, pointRight.x, pointRight.y + heightOffset, pointRight.z, 0, 0, 0);
        }
        for (double t = 0; t <= 1; t += 0.1) {
            Vec3d point = new Vec3d(
                    leftBase.x + (rightBase.x - leftBase.x) * t,
                    leftBase.y + (rightBase.y - leftBase.y) * t,
                    leftBase.z + (rightBase.z - leftBase.z) * t
            );
            world.addParticle(ParticleTypes.FLAME, point.x, point.y + heightOffset, point.z, 0, 0, 0);
        }
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
                    tickCounter++;
                    if (tickCounter >= 10) {
                        createParticleArrow(mc.player, (ClientWorld) mc.world);
                        tickCounter = 0;
                    }
                } else {
                    tickCounter = 0;
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
