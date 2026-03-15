package net.frostytrix.echoesofantiquity.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import net.frostytrix.echoesofantiquity.EchoesOfAntiquity;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SieveScreen extends HandledScreen<SieveScreenHandler> {
    public static final Identifier GUI_TEXTURE =
            Identifier.of(EchoesOfAntiquity.MOD_ID, "textures/gui/sieve/sieve_gui.png");
    public static final Identifier ARROW_TEXTURE =
            Identifier.of(EchoesOfAntiquity.MOD_ID, "textures/gui/sieve/arrow_progress.png");

    public SieveScreen(SieveScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);

        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        context.drawTexture(GUI_TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);

        renderProgressArrow(context,x,y);
    }

    private void renderProgressArrow(DrawContext context, int x, int y) {
        if (handler.isSifting()) {
            context.drawTexture(ARROW_TEXTURE, x + 58, y+35, 0, 0,
                    handler.getScaledArrowProgress(), 16, 24,16);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }
}
