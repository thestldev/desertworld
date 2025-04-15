package com.idlenonsense.desertworld.block.traps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class TrapJawBlock extends Block {

    public TrapJawBlock(Settings settings) { super(settings); }

    // Не нашёл метода, который напрямую может вызываться, если игрок над блоком
    // есть только метод, когда игрок заходит внутрь блока
//    @Override
//    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
//        if (!world.isClient && entity instanceof PlayerEntity) {
//            PlayerEntity player = (PlayerEntity) entity;
//            if (player.getY() > pos.getY() && player.getY() < pos.getY() + 2.0) {
//                MinecraftServer server = world.getServer();
//                if (server != null) {
//                    String command = String.format("summon minecraft:evoker_fangs %d %d %d", pos.getX(), pos.getY() + 1, pos.getZ());
//                    Vec3d position = new Vec3d(pos.getX(), pos.getY() + 1, pos.getZ());
//                    ServerCommandSource source = server.getCommandSource().withPosition(position);
//                    try {
//                        server.getCommandManager().getDispatcher().execute(command, source);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//    }

    public static void spawnFangsAt(World world, BlockPos pos) {
        if (!world.isClient) {
            MinecraftServer server = world.getServer();
            if (server != null) {
                double x = pos.getX() + 0.5;
                double y = pos.getY() + 1;
                double z = pos.getZ() + 0.5;

                // библиотеки для использования сущности evoker_fangs напрямую тоже не нашёл
                // поэтому вызов команды
                String command = String.format(java.util.Locale.ROOT, "summon minecraft:evoker_fangs %.1f %.1f %.1f", x, y, z);
                ServerCommandSource source = server.getCommandSource().withPosition(new Vec3d(x, y, z)).withSilent();
                try {
                    server.getCommandManager().getDispatcher().execute(command, source);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}