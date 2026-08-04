package net.frostytrix.echoesofantiquity.lore;

import net.frostytrix.echoesofantiquity.EchoesOfAntiquity;
import net.minecraft.util.Identifier;

/** One unlockable piece of the guide book. The advancement is what Patchouli checks to reveal it. */
public enum LorePage {
    FOUNDATIONS("foundations"),
    THE_MEASURE("the_measure"),
    THE_CROSSING("the_crossing"),
    WHAT_CAME_BACK("what_came_back"),
    THE_ARCHIVE("the_archive"),
    THE_COPIES("the_copies");

    private final String name;

    LorePage(String name) {
        this.name = name;
    }

    public String pageName() {
        return this.name;
    }

    public Identifier advancementId() {
        return Identifier.of(EchoesOfAntiquity.MOD_ID, "lore/" + this.name);
    }

    public String translationKey() {
        return "lore." + EchoesOfAntiquity.MOD_ID + "." + this.name;
    }
}
