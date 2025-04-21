package org.thesalutyt.network.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.idlenonsense.desertworld.DesertWorld;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import org.thesalutyt.utils.ServerUfoController;
import org.thesalutyt.utils.UfoAttackContainer;

import java.util.UUID;

public class UfoAttackHandler {
    public static final Identifier ID = new Identifier(DesertWorld.MOD_ID, "ufo_attack");

    public static void registerServerReceiver() {
        ServerPlayNetworking.registerGlobalReceiver(ID, (mcServer, serverPlayer, spn, buf, pSender) -> {
            System.out.println("!! received packet");
            String json = buf.readString();

            UfoAttackContainer container = fromJson(json);

            assert container != null;
            ServerUfoController controller = ServerUfoController.getControllerByUUID(container.getUfo());

            if (container instanceof UfoAttackContainer.BlockAttackContainer) {
                blockAttack(((UfoAttackContainer.BlockAttackContainer) container).getPos(), controller);
            } else if (container instanceof UfoAttackContainer.EntityAttackContainer) {
                entityAttack(((UfoAttackContainer.EntityAttackContainer) container).getEntity(), controller);
            }
        });
    }

    private static void blockAttack(BlockPos pos, ServerUfoController controller) {
        controller.attackBlock(pos);
    }

    private static void entityAttack(UUID entity, ServerUfoController controller) {
        controller.attackEntity(entity);
    }

    @SuppressWarnings("deprecation")
    private static UfoAttackContainer fromJson(String json) {
        JsonParser parser = new JsonParser();
        JsonElement el = parser.parse(json);

        String type = el.getAsJsonObject().get("type").getAsString();
        String arg = el.getAsJsonObject().get("arg").getAsString();
        UUID ufoId = UUID.fromString(el.getAsJsonObject().get("ufo").getAsString());

        return switch (type) {
            case "block" -> new UfoAttackContainer.BlockAttackContainer(ufoId, blockPosFromString(arg));
            case "entity" -> new UfoAttackContainer.EntityAttackContainer(ufoId, UUID.fromString(arg));
            default -> null;
        };
    }

    private static BlockPos blockPosFromString(String pos) {
        String[] split = pos.split(";");
        return new BlockPos(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
    }
}
