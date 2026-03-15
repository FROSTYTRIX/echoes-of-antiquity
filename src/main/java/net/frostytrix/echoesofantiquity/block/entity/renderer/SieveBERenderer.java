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

        // --- NEW CHECK ---
        // If there's no block, OR if the block doesn't have a valid recipe, don't render anything!
        if (inputStack.isEmpty() || !entity.hasValidRecipeForRender()) {
            return;
        }
        // -----------------

        int progress = entity.getProgress();
        int maxProgress = entity.getMaxProgress();

        matrices.push();

        // 1. Calculate the height offset
        // Let's assume 0.8f is the top of the sieve, and 0.5f is the mesh at the bottom.
        float startY = 0.8f;
        float endY = 0.3f;
        float currentY = startY;

        if (progress > 0 && maxProgress > 0) {
            // We use tickDelta to smooth out the animation between ticks so it doesn't look jittery
            float smoothedProgress = progress + tickDelta;
            float progressRatio = Math.min(smoothedProgress / maxProgress, 1.0f);

            // Interpolate from startY down to endY
            currentY = startY - ((startY - endY) * progressRatio);
        }

        // 2. Move the item to the center of the block and set its height
        matrices.translate(0.5f, currentY, 0.5f);

        // 3. Scale the block down slightly so it visually fits inside the wooden walls of your sieve
        matrices.scale(1.5f, 1.5f, 1.5f);

        // 4. Render the item
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