package com.idlenonsense.desertworld.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.thesalutyt.utils.SkinManager;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class SkinChangeMixin {
    @Inject(method = "getSkinTexture", at = @At("HEAD"), cancellable = true)
    private void onGetSkinTextureLocation(CallbackInfoReturnable<Identifier> cir) {
        Identifier customSkin = SkinManager.getCurrentSkin();

        if (customSkin != null) {
            cir.setReturnValue(customSkin);
        }
    }
}
