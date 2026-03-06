package net.frostytrix.echoesofantiquity.item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.BowItem;
import org.jetbrains.annotations.Nullable;

public class DragonBowItem extends BowItem {
    public DragonBowItem(Settings settings) {
        super(settings);
    }

    @Override
    protected void shoot(LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, @Nullable LivingEntity target) {
        projectile.setVelocity(shooter, shooter.getPitch(), shooter.getYaw() + yaw, 0.0F, speed * 2, divergence);
    }

    @Override
    public int getRange() {
        return 30;
    }
}
