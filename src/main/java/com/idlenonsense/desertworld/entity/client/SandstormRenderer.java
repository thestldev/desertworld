package com.idlenonsense.desertworld.entity.client;

import com.idlenonsense.desertworld.entity.SandstormEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;

public class SandstormRenderer extends EntityRenderer<SandstormEntity> {
    private static final Identifier TEXTURE = new Identifier("desertworld", "textures/entity/tornado.png");

    public SandstormRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(SandstormEntity entity) {
        return TEXTURE;
    }

    private void renderCube(MatrixStack matrices, VertexConsumer vertexConsumer, int light) {
        // Пример рисования простого куба
        MatrixStack.Entry entry = matrices.peek();
        Matrix4f matrix = entry.getPositionMatrix();
        Matrix3f normalMatrix = entry.getNormalMatrix();

        // Вершины куба
        float[] vertices = new float[]{
                -0.5F, -0.5F, -0.5F,
                0.5F, -0.5F, -0.5F,
                0.5F, 0.5F, -0.5F,
                -0.5F, 0.5F, -0.5F,
                -0.5F, -0.5F, 0.5F,
                0.5F, -0.5F, 0.5F,
                0.5F, 0.5F, 0.5F,
                -0.5F, 0.5F, 0.5F
        };

        // Нормали куба
        float[] normals = new float[]{
                0.0F, 0.0F, -1.0F,
                0.0F, 0.0F, 1.0F,
                0.0F, -1.0F, 0.0F,
                0.0F, 1.0F, 0.0F,
                -1.0F, 0.0F, 0.0F,
                1.0F, 0.0F, 0.0F
        };

        // Индексы вершин для граней куба
        int[] indices = new int[]{
                0, 1, 2, 2, 3, 0,
                4, 5, 6, 6, 7, 4,
                0, 1, 5, 5, 4, 0,
                2, 3, 7, 7, 6, 2,
                1, 2, 6, 6, 5, 1,
                0, 3, 7, 7, 4, 0
        };

        for (int i = 0; i < indices.length; i += 3) {
            int v1 = indices[i];
            int v2 = indices[i + 1];
            int v3 = indices[i + 2];

            float x1 = vertices[v1 * 3];
            float y1 = vertices[v1 * 3 + 1];
            float z1 = vertices[v1 * 3 + 2];

            float x2 = vertices[v2 * 3];
            float y2 = vertices[v2 * 3 + 1];
            float z2 = vertices[v2 * 3 + 2];

            float x3 = vertices[v3 * 3];
            float y3 = vertices[v3 * 3 + 1];
            float z3 = vertices[v3 * 3 + 2];

            float nx = normals[i / 6 * 3];
            float ny = normals[i / 6 * 3 + 1];
            float nz = normals[i / 6 * 3 + 2];

            vertexConsumer.vertex(matrix, x1, y1, z1).color(255, 255, 255, 255).texture(0.0F, 0.0F).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normalMatrix, nx, ny, nz).next();
            vertexConsumer.vertex(matrix, x2, y2, z2).color(255, 255, 255, 255).texture(0.0F, 1.0F).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normalMatrix, nx, ny, nz).next();
            vertexConsumer.vertex(matrix, x3, y3, z3).color(255, 255, 255, 255).texture(1.0F, 1.0F).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normalMatrix, nx, ny, nz).next();
        }
    }

    @Override
    public void render(SandstormEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        matrices.translate(0.0D, 1.5D, 0.0D);
        matrices.multiply(this.dispatcher.getRotation());
        matrices.scale(-1.0F, -1.0F, 1.0F);
        matrices.translate(0.0D, -1.5D, 0.0D);

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntitySolid(TEXTURE));
        // Пример рисования простого куба
        renderCube(matrices, vertexConsumer, light);

        // Здесь вы можете напрямую рисовать вашу сущность
        // Например, используя VertexConsumerProvider и MatrixStack

        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }
}