package net.frostytrix.echoesofantiquity.mixin;

import net.frostytrix.echoesofantiquity.util.GravityAnchorManager;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// On étend "Entity" pour avoir accès aux méthodes comme getWorld(), getBlockPos() et discard()
@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityMixin extends Entity {

    public FallingBlockEntityMixin(EntityType<?> type, World world) {
        super(type, world);

    }

    // On récupère la méthode native qui contient le bloc en train de tomber
    @Shadow public abstract BlockState getBlockState();

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void freezeInAnchorZone(CallbackInfo ci) {
        World world = this.getWorld();

        // On ne gère ça que côté serveur pour éviter les désynchronisations
        if (world.isClient) return;

        BlockPos currentPos = this.getBlockPos();

        // Si l'entité entre dans la zone de l'ancre
        if (GravityAnchorManager.isProtected(world, currentPos)) {
            BlockState stateToPlace = this.getBlockState();

            // Sécurité : on vérifie que l'entité ne remplace pas un bloc solide 
            // (on vérifie que l'espace est de l'air, de l'eau, etc.)
            if (world.getBlockState(currentPos).isReplaceable()) {
                // 1. On place le bloc fige
                world.setBlockState(currentPos, stateToPlace, 3);

                // 2. On supprime l'entité qui tombait
                this.discard();

                // 3. On annule le reste du tick (la gravité, les dégâts de chute, etc.)
                ci.cancel();
            }
        }
    }
}