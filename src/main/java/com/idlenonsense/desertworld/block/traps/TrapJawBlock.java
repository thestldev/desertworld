package com.idlenonsense.desertworld.block.traps;
import com.idlenonsense.desertworld.block.traps.resource.ITrapBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.EvokerFangsEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class TrapJawBlock extends Block implements ITrapBlock {
    private int lastUsageTick = 0;

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
        if (world.isClient) return;
        MinecraftServer server = world.getServer();
        if (server == null) return;
        spawnFangsEntity((ServerWorld) world, pos);
    }

    private static void spawnFangsEntity(ServerWorld serverWorld, BlockPos pos) {
        EvokerFangsEntity fangs = new EvokerFangsEntity(EntityType.EVOKER_FANGS, serverWorld);
        fangs.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
        serverWorld.spawnEntity(fangs);
    }

    @Deprecated
    private static void spawnFangsCommand(ServerWorld serverWorld, BlockPos pos) {
        String command = String.format("summon minecraft:evoker_fangs %d %d %d", pos.getX(), pos.getY() + 1, pos.getZ());
        Vec3d position = new Vec3d(pos.getX(), pos.getY() + 1, pos.getZ());
        ServerCommandSource source = serverWorld.getServer().getCommandSource().withPosition(position);
        try {
            serverWorld.getServer().getCommandManager().getDispatcher().execute(command, source);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stepOn(ServerWorld world, PlayerEntity player, BlockPos pos) {
        int currentTick = world.getServer().getTicks();

        if (currentTick - lastUsageTick < getDelay()) return;
        lastUsageTick = currentTick;
        spawnFangsAt(world, pos);
    }

    @Override
    public void trapActiveTick(ServerWorld world, PlayerEntity player, BlockPos pos) {

    }

    @Override
    public int getDelay() {
        return 15;
    }
}