package org.thesalutyt.utils;

import com.mojang.authlib.GameProfile;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class SkinManager {
    private static Identifier current_skin = null;

    public static Identifier getCurrentSkin() {
        return current_skin;
    }

    public static void setCurrentSkin(Identifier skin) {
        current_skin = skin;
    }

}
