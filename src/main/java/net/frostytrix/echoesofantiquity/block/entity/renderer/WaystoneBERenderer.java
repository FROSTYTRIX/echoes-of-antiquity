package net.frostytrix.echoesofantiquity.block.entity.renderer;

import net.frostytrix.echoesofantiquity.EchoesOfAntiquity;
import net.frostytrix.echoesofantiquity.block.custom.WaystoneBlock;
import net.frostytrix.echoesofantiquity.block.entity.custom.WaystoneBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;

public class WaystoneBERenderer implements BlockEntityRenderer<WaystoneBlockEntity> {
    // Target the texture file directly
    private static final Identifier GLOW_TEXTURE = Identifier.of(EchoesOfAntiquity.MOD_ID, "textures/block/waystone_glow_e.png");

    public WaystoneBERenderer(BlockEntityRendererFactory.Context context) {}

    @Override
    public void render(WaystoneBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        BlockState state = entity.getCachedState();

        // Check if the Waystone is ACTIVE
        if (state.get(WaystoneBlock.ACTIVE)) {
            matrices.push();

            // 1. Handle Rotation: Move to center, rotate, move back
            matrices.translate(0.5f, 0.5f, 0.5f);
            float angle = switch (state.get(WaystoneBlock.FACING)) {
                case NORTH -> 0f;
                case EAST -> -90f;
                case SOUTH -> 180f;
                case WEST -> 90f;
                default -> 0f;
            };
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(angle));
            matrices.translate(-0.5f, -0.5f, -0.5f);

            VertexConsumer consumer = vertexConsumers.getBuffer(RenderLayer.getEyes(GLOW_TEXTURE));
            Matrix4f matrix = matrices.peek().getPositionMatrix();

            // UV and Geometry (Corrected 14x14 mapping)
            float x1 = 1f / 16f;
            float x2 = 15f / 16f;
            float y1 = 0f;
            float y2 = 14f / 16f;
            float z = 4.99f / 16f;

            float u1 = 0f;
            float u2 = 14f / 16f;
            float v1 = 0f;
            float v2 = 14f / 16f;

            int maxLight = 15728880;

            // Draw the Quad (North face relative to the rotated matrix)
            consumer.vertex(matrix, x2, y1, z).color(255, 255, 255, 255).texture(u1, v2).overlay(overlay).light(maxLight).normal(0, 0, -1);
            consumer.vertex(matrix, x1, y1, z).color(255, 255, 255, 255).texture(u2, v2).overlay(overlay).light(maxLight).normal(0, 0, -1);
            consumer.vertex(matrix, x1, y2, z).color(255, 255, 255, 255).texture(u2, v1).overlay(overlay).light(maxLight).normal(0, 0, -1);
            consumer.vertex(matrix, x2, y2, z).color(255, 255, 255, 255).texture(u1, v1).overlay(overlay).light(maxLight).normal(0, 0, -1);

            matrices.pop();
        }
    }
}
