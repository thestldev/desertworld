package com.idlenonsense.desertworld.mixin;

import com.idlenonsense.desertworld.item.UfoControllerItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.thesalutyt.network.packets.UfoAttackPacket;
import org.thesalutyt.utils.ServerUfoController;

import java.util.UUID;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow public World world;

    @Shadow public abstract UUID getUuid();

    @Inject(at = @At("HEAD"), method = "interactAt")
    public void interactAt(PlayerEntity player, Vec3d hitPos, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        System.out.println("!! interaction");

        if (!this.world.isClient) return;

        System.out.println("!! not client");

        ItemStack stack = player.getStackInHand(hand);
        if (stack.isEmpty()) return;
        if (!(stack.getItem() instanceof UfoControllerItem)) return;

        System.out.println("!! sending packet");
        UUID ufoID = ((UfoControllerItem) stack.getItem()).getUfoID();
        UfoAttackPacket.sendEntityAttack(this.getUuid(), ufoID);

        /*Vec3d from = player.getPos().add(0, 1.5, 0);
        Vec3d to = this.getPos();
        ServerUfoController.getControllerByUUID(ufoID).sendLaserParticles(from, to);*/
    }
}
