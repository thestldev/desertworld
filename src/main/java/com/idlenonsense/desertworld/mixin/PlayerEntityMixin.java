package com.idlenonsense.desertworld.mixin;

import com.idlenonsense.desertworld.abilities.IAbilitiesProvider;
import com.idlenonsense.desertworld.currency.DesertCurrency;
import com.idlenonsense.desertworld.currency.ICurrencyProvider;
import com.idlenonsense.desertworld.abilities.Abilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.thesalutyt.utils.SkinManager;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements ICurrencyProvider, IAbilitiesProvider {
    @Inject(at = @At("HEAD"), method = "writeCustomDataToNbt")
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        DesertCurrency.getInstance().write(nbt);
        Abilities.getInstance().write(nbt);
        SkinManager.writeNbt(nbt);
    }

    @Inject(at = @At("RETURN"), method = "readCustomDataFromNbt")
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        DesertCurrency.getInstance().read(nbt);
        Abilities.getInstance().read(nbt);
        SkinManager.readNbt(nbt);
    }

    @Unique
    @Override
    public DesertCurrency desertworld$getCurrency() {
        return DesertCurrency.getInstance();
    }

    @Unique
    @Override
    public Abilities desertworld$getAbilities() {
        return Abilities.getInstance();
    }
}
