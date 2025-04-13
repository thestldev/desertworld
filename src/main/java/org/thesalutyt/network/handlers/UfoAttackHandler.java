package org.thesalutyt.network.handlers;

import com.idlenonsense.desertworld.DesertWorld;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import org.thesalutyt.utils.ServerUfoController;

import java.util.UUID;

public class UfoAttackHandler {
    public static final Identifier ID = new Identifier(DesertWorld.MOD_ID, "ufo_attack");

    public static void registerServerReceiver() {
        ServerPlayNetworking.registerGlobalReceiver(ID, (mcServer, serverPlayer, spn, buf, pSender) -> {
            System.out.println("!! received packet");
            int type = buf.readInt();
            UUID ufoID = buf.readUuid();

            System.out.printf("!! type: %d, ufoID: %s%n", type, ufoID);

            ServerUfoController controller = ServerUfoController.getControllerByUUID(ufoID);

            System.out.println(controller);

            switch (type) {
                case 0:
                    System.out.println("!! block attack");
                    blockAttack(buf.readBlockPos(), controller);
                    break;
                case 1:
                    System.out.println("!! entity attack");
                    entityAttack(UUID.fromString(buf.readString()), controller);
                    break;
                default:
                    System.out.println("!! unknown type");
                    break;
            }
        });
    }

    private static void blockAttack(BlockPos pos, ServerUfoController controller) {
        controller.attackBlock(pos);
    }

    private static void entityAttack(UUID entity, ServerUfoController controller) {
        controller.attackEntity(entity);
    }
}
