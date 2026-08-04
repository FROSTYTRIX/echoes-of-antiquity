package net.frostytrix.echoesofantiquity.item.custom;

import net.frostytrix.echoesofantiquity.lore.LorePage;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/** Reading a scrip deciphers one page the player has not seen yet, picked at random. */
public class AncientScripItem extends Item {
    public AncientScripItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (!(user instanceof ServerPlayerEntity player)) {
            return TypedActionResult.success(stack, world.isClient());
        }

        List<LorePage> remaining = new ArrayList<>();
        for (LorePage page : LorePage.values()) {
            AdvancementEntry entry = player.getServer().getAdvancementLoader().get(page.advancementId());
            if (entry != null && !player.getAdvancementTracker().getProgress(entry).isDone()) {
                remaining.add(page);
            }
        }

        if (remaining.isEmpty()) {
            player.sendMessage(Text.translatable("message.echoesofantiquity.scrip.nothing_left").formatted(Formatting.GRAY), true);
            return TypedActionResult.fail(stack);
        }

        LorePage page = remaining.get(world.random.nextInt(remaining.size()));
        AdvancementEntry entry = player.getServer().getAdvancementLoader().get(page.advancementId());
        player.getAdvancementTracker().grantCriterion(entry, "unlocked");

        world.playSound(null, player.getBlockPos(), SoundEvents.ITEM_BOOK_PAGE_TURN, SoundCategory.PLAYERS, 1.0f, 1.0f);
        player.sendMessage(Text.translatable("message.echoesofantiquity.scrip.deciphered",
                Text.translatable(page.translationKey()).formatted(Formatting.AQUA)), false);

        stack.decrement(1);
        return TypedActionResult.success(stack);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("tooltip.echoesofantiquity.ancient_scrip").formatted(Formatting.DARK_GRAY));
        super.appendTooltip(stack, context, tooltip, type);
    }
}
