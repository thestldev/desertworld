package org.thesalutyt.utils;

import net.minecraft.util.Identifier;

public class SkinManager {
    private static Identifier current_skin = null;

    /**
     * Returns the current skin identifier that is being used by the client.
     *
     * @return The current skin identifier.
     */
    public static Identifier getCurrentSkin() {
        return current_skin;
    }

    /**
     * Set the current skin that is being used by the client. This is called
     * by the client when the player's skin changes.
     *
     * @param skin The identifier of the new skin.
     */
    public static void setCurrentSkin(Identifier skin) {
        current_skin = skin;
    }

    /**
     * Reset the current skin to null, indicating that the client should
     * use the default skin for the player.
     */
    public static void resetSkin() {
        current_skin = null;
    }
}
