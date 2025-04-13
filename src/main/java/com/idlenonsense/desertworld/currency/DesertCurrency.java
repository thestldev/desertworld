package com.idlenonsense.desertworld.currency;

import net.minecraft.nbt.NbtCompound;

public class DesertCurrency {
    private static final DesertCurrency instance = new DesertCurrency();
    private float worldDesertPercentage = 0;

    public static DesertCurrency getInstance() {
        return instance;
    }

    public float get() {
        return worldDesertPercentage;
    }

    public void set(float newValue) {
        if (!canSet(newValue)) return;
        worldDesertPercentage = newValue;
    }

    public void add(float v) {
        if (wrongAddValue(v)) return;
        worldDesertPercentage += v;
    }

    public void subtract(float v) {
        if (wrongAddValue(-v)) return;
        worldDesertPercentage -= v;
    }

    public void write(NbtCompound tag) {
        tag.putFloat("worldDesertPercentage", worldDesertPercentage);
    }

    public void read(NbtCompound tag) {
        worldDesertPercentage = tag.getInt("worldDesertPercentage");
    }

    private boolean wrongAddValue(float v) {
        return v + worldDesertPercentage > 100 || v + worldDesertPercentage < 0;
    }

    private boolean canSet(float v) {
        return !(v > 100) && !(v < 0);
    }

    @Override
    public String toString() {
        return "DesertCurrency{" +
                "worldDesertPercentage=" + worldDesertPercentage +
                '}';
    }
}
