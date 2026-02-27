package net.frostytrix.echoesofantiquity.effect;

import net.frostytrix.echoesofantiquity.EchoesOfAntiquity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class ReachEffect extends StatusEffect {
    public ReachEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        // We don't need logic here since attribute modifiers handle the "Reach"
        return super.applyUpdateEffect(entity, amplifier);
    }

    @Override
    public void onApplied(LivingEntity entity, int amplifier) {
        if (entity instanceof PlayerEntity player) {
            var reachAttribute = player.getAttributeInstance(EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE);
            var blockAttribute = player.getAttributeInstance(EntityAttributes.PLAYER_BLOCK_INTERACTION_RANGE);

            if (reachAttribute != null && blockAttribute != null) {
                // Always clear existing modifiers first using the same Identifier
                reachAttribute.removeModifier(Identifier.of(EchoesOfAntiquity.MOD_ID, "reach_extension"));
                blockAttribute.removeModifier(Identifier.of(EchoesOfAntiquity.MOD_ID, "reach_block_extension"));

                // Apply the "Long Leg" boost
                reachAttribute.addTemporaryModifier(new EntityAttributeModifier(
                        Identifier.of(EchoesOfAntiquity.MOD_ID, "reach_extension"), 2.0, EntityAttributeModifier.Operation.ADD_VALUE));
                blockAttribute.addTemporaryModifier(new EntityAttributeModifier(
                        Identifier.of(EchoesOfAntiquity.MOD_ID, "reach_block_extension"), 2.0, EntityAttributeModifier.Operation.ADD_VALUE));
            }
        }
        super.onApplied(entity, amplifier);
    }

    @Override
    public void onRemoved(AttributeContainer attributeContainer) {
        // We create the ID once so we are 100% sure we are removing the right thing
        Identifier reachId = Identifier.of(EchoesOfAntiquity.MOD_ID, "reach_extension");
        Identifier blockId = Identifier.of(EchoesOfAntiquity.MOD_ID, "reach_block_extension");

        // We pull the attributes directly from the container that is being "cleaned up"
        var reachAttr = attributeContainer.getCustomInstance(EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE);
        var blockAttr = attributeContainer.getCustomInstance(EntityAttributes.PLAYER_BLOCK_INTERACTION_RANGE);

        if (reachAttr != null) {
            reachAttr.removeModifier(reachId);
        }
        if (blockAttr != null) {
            blockAttr.removeModifier(blockId);
        }

        super.onRemoved(attributeContainer);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}