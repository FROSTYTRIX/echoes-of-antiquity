package net.frostytrix.echoesofantiquity.block.entity.renderer;

import net.frostytrix.echoesofantiquity.block.entity.custom.SieveBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

public class SieveBERenderer implements BlockEntityRenderer<SieveBlockEntity> {

    public SieveBERenderer(BlockEntityRendererFactory.Context context) {
    }

    @Override
    public void render(SieveBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        ItemStack inputStack = entity.getInputStack();

        // Nothing to render without a valid recipe.
        if (inputStack.isEmpty() || !entity.hasValidRecipeForRender()) {
            return;
        }

        int progress = entity.getProgress();
        int maxProgress = entity.getMaxProgress();

        matrices.push();

        float startY = 0.8f;
        float endY = 0.3f;
        float currentY = startY;

        if (progress > 0 && maxProgress > 0) {
            // tickDelta smooths the animation between ticks.
            float smoothedProgress = progress + tickDelta;
            float progressRatio = Math.min(smoothedProgress / maxProgress, 1.0f);

            currentY = startY - ((startY - endY) * progressRatio);
        }

        matrices.translate(0.5f, currentY, 0.5f);

        matrices.scale(1.5f, 1.5f, 1.5f);

        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        itemRenderer.renderItem(
                inputStack,
                ModelTransformationMode.FIXED, // FIXED makes it render like a 3D block rather than a flat GUI icon
                light,
                overlay,
                matrices,
                vertexConsumers,
                entity.getWorld(),
                (int) entity.getPos().asLong()
        );

        matrices.pop();
    }
}