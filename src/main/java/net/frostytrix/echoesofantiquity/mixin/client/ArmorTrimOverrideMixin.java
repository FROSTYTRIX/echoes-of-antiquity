package net.frostytrix.echoesofantiquity.mixin.client;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.frostytrix.echoesofantiquity.item.ModTrimMaterials;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelOverride;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

/**
 * Adds the End Steel trim override to every armor model as it loads, instead of shipping edited copies of
 * the vanilla model files. Those copies would fight with any other mod adding a trim material, and would
 * freeze whatever the models looked like on the Minecraft version they were copied from.
 */
@Mixin(JsonUnbakedModel.Deserializer.class)
public class ArmorTrimOverrideMixin {
    @Unique
    private static final Identifier TRIM_TYPE = Identifier.ofVanilla("trim_type");

    /** Vanilla's highest trim index, so its override is the one we derive our model name from. */
    @Unique
    private static final String REFERENCE_SUFFIX = "_amethyst_trim";

    @Unique
    private static final String OUR_SUFFIX = "_end_steel_trim";

    @Inject(method = "overridesFromJson", at = @At("RETURN"), cancellable = true)
    private void echoes$addEndSteelTrim(JsonDeserializationContext context, JsonObject json,
                                        CallbackInfoReturnable<List<ModelOverride>> cir) {
        List<ModelOverride> overrides = cir.getReturnValue();
        if (overrides.isEmpty()) {
            return;
        }

        ModelOverride reference = null;
        for (ModelOverride override : overrides) {
            Float threshold = echoes$trimThreshold(override);
            if (threshold == null) {
                continue;
            }
            if (threshold == ModTrimMaterials.END_STEEL_MODEL_INDEX) {
                return; // already there, nothing to do
            }
            if (threshold == 1.0F && override.getModelId().getPath().endsWith(REFERENCE_SUFFIX)) {
                reference = override;
            }
        }

        if (reference == null) {
            return; // not a trimmable armor model
        }

        String path = reference.getModelId().getPath();
        Identifier ourModel = Identifier.of(reference.getModelId().getNamespace(),
                path.substring(0, path.length() - REFERENCE_SUFFIX.length()) + OUR_SUFFIX);

        ModelOverride ours = new ModelOverride(ourModel,
                List.of(new ModelOverride.Condition(TRIM_TYPE, ModTrimMaterials.END_STEEL_MODEL_INDEX)));

        // ModelOverrideList scans forward and keeps the first match, so the trim entries have to stay
        // ascending. Insert in place rather than sorting, to leave any non trim override where it was.
        List<ModelOverride> patched = new ArrayList<>(overrides);
        int insertAt = patched.size();
        for (int i = 0; i < patched.size(); i++) {
            Float threshold = echoes$trimThreshold(patched.get(i));
            if (threshold != null && threshold > ModTrimMaterials.END_STEEL_MODEL_INDEX) {
                insertAt = i;
                break;
            }
        }
        patched.add(insertAt, ours);

        cir.setReturnValue(List.copyOf(patched));
    }

    @Unique
    private static Float echoes$trimThreshold(ModelOverride override) {
        return override.streamConditions()
                .filter(condition -> condition.getType().equals(TRIM_TYPE))
                .map(ModelOverride.Condition::getThreshold)
                .findFirst()
                .orElse(null);
    }
}
