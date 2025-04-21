package org.thesalutyt.utils;

import net.minecraft.util.math.BlockPos;
import org.thesalutyt.network.packets.UfoAttackPacket;

import java.util.UUID;

public abstract class UfoAttackContainer {
    protected final UUID ufo;

    protected UfoAttackContainer(UUID ufo) {
        this.ufo = ufo;
    }

    public UUID getUfo() {
        return ufo;
    }

    public abstract UfoAttackPacket.AttackType getAttackType();
    public abstract String toJson();

    public static String jsonRequestEncoder(UfoAttackPacket.AttackType type, Object typeArgument, UUID ufoId) {
        if (type == UfoAttackPacket.AttackType.BLOCK && typeArgument instanceof BlockPos) {
            return "{\"type\":\"block\",\"arg\":\"" + encodeBlockPos((BlockPos) typeArgument) + "\",\"ufo\":\"" + ufoId + "\"}";
        } else if (type == UfoAttackPacket.AttackType.ENTITY && typeArgument instanceof UUID) {
            return "{\"type\":\"entity\",\"arg\":\"" + typeArgument + "\",\"ufo\":\"" + ufoId + "\"}";
        }
        return null;
    }

    private static String encodeBlockPos(BlockPos pos) {
        return String.format("%s;%s;%s", pos.getX(), pos.getY(), pos.getZ());
    }

    public static class BlockAttackContainer extends UfoAttackContainer {
        private final BlockPos pos;

        public BlockAttackContainer(UUID ufo, BlockPos pos) {
            super(ufo);
            this.pos = pos;
        }

        public BlockPos getPos() {
            return pos;
        }

        @Override
        public UfoAttackPacket.AttackType getAttackType() {
            return UfoAttackPacket.AttackType.BLOCK;
        }

        @Override
        public String toJson() {
            return jsonRequestEncoder(getAttackType(), pos, ufo);
        }
    }

    public static class EntityAttackContainer extends UfoAttackContainer {
        private final UUID entity;

        public EntityAttackContainer(UUID ufo, UUID entity) {
            super(ufo);
            this.entity = entity;
        }

        public UUID getEntity() {
            return entity;
        }

        @Override
        public UfoAttackPacket.AttackType getAttackType() {
            return UfoAttackPacket.AttackType.ENTITY;
        }

        @Override
        public String toJson() {
            return jsonRequestEncoder(getAttackType(), entity, ufo);
        }
    }
}
