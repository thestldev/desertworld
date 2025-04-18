package com.idlenonsense.desertworld.abilities;

import net.minecraft.nbt.NbtCompound;

import java.util.LinkedList;

public class Abilities {
    private final LinkedList<Ability> abilities = new LinkedList<>();
    private static final Abilities instance = new Abilities();

    public static Abilities getInstance() {
        return instance;
    }

    public void addAbility(Ability ability) {
        abilities.add(ability);
    }

    public void removeAbility(Ability ability) {
        abilities.remove(ability);
    }

    public boolean get(String name) {
        for (Ability ability : abilities) {
            if (ability.getName().equals(name)) {
                return ability.isEnabled();
            }
        }
        return false;
    }

    public float getPercent(String name) {
        for (Ability ability : abilities) {
            if (ability instanceof SpecialAbility specialAbility) {
                if (specialAbility.getName().equals(name)) {
                    return specialAbility.getPercent();
                }
            }
        }
        return 0;
    }

    public void set(String name, boolean enabled) {
        for (Ability ability : abilities) {
            if (ability.getName().equals(name)) {
                ability.setEnabled(enabled);
            }
        }
    }

    public void set(String name, float percent, boolean set) {
        for (Ability ability : abilities) {
            if (ability.getName().equals(name)) {
                ability.setEnabled(set);
                if (ability instanceof SpecialAbility specialAbility) {
                    specialAbility.setPercent((int) percent);
                }
            }
        }
    }

    public void enable(float percent) {
        int s = (int) percent;

        for (Ability ability : abilities) {
            if (ability instanceof SpecialAbility specialAbility) {
                if (specialAbility.getPercent() == s) {
                    specialAbility.setEnabled(true);
                }
            }
        }
    }

    public void write(NbtCompound tag) {
        abilities.forEach(
                ability -> {
                    if (ability instanceof SpecialAbility specialAbility) {
                        tag.putInt(specialAbility.getName(), specialAbility.getPercent());
                    }
                    tag.putBoolean(ability.getName(), ability.isEnabled());
                }
        );
    }

    public void read(NbtCompound tag) {
        abilities.forEach(
                ability -> {
                    if (ability instanceof SpecialAbility specialAbility) {
                        specialAbility.setPercent(tag.getInt(specialAbility.getName()));
                    }
                    ability.setEnabled(tag.getBoolean(ability.getName()));
                }
        );
    }

    public void clear() {
        abilities.clear();
    }

    public static class Ability {
        private boolean enabled = false;
        private final String name;

        public Ability(String name) {
            this.name = name;
        }

        public Ability() {
            this("Ability-" + System.currentTimeMillis());
        }

        public void onEnable() {}

        public void toggle() {
            enabled = !enabled;
        }

        public String getName() {
            return name;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public boolean isEnabled() {
            return enabled;
        }
    }

    public static class SpecialAbility extends Ability {
        private int percent;

        public SpecialAbility(String name, int percent) {
            super(name);
            this.percent = percent;
        }

        public int getPercent() {
            return percent;
        }

        protected void setPercent(int aFloat) {
            this.percent = aFloat;
        }
    }
}
