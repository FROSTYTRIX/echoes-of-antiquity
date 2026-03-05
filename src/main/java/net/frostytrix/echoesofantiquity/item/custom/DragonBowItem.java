package net.frostytrix.echoesofantiquity.item.custom;

import net.minecraft.item.BowItem;

public class DragonBowItem extends BowItem {
    public DragonBowItem(Settings settings) {
        super(settings);
    }

    @Override
    public int getRange() {
        return 30;
    }
}
