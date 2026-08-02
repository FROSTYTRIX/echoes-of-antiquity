package net.frostytrix.echoesofantiquity.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.frostytrix.echoesofantiquity.EchoesOfAntiquity;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Plain json config, no library. Loaded before anything registers, because a few values feed item
 * settings that are baked in at registration time.
 */
public class ModConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static ModConfig instance = new ModConfig();

    // Void Pedestal
    public int voidPedestalRadius = 20;

    // Gravity Anchor
    public int gravityAnchorRadius = 10;

    // Magnet Ring
    public int magnetRingRange = 4;
    public float magnetRingSpeed = 0.02f;

    // Waystone
    public int waystoneSoulFragmentCost = 10;
    public float waystoneRecallHealth = 5.0f;
    public float waystoneHealAmount = 10.0f;

    // Void Chainmail: void rescue costs maxDurability / this, on every piece
    public int voidRescueDurabilityDivisor = 3;

    // Static Pearl
    public int staticPearlUses = 20;

    // Machines, in ticks
    public int sieveDuration = 72;
    public int uncrafterDuration = 72;

    // Soul Siphon right click
    public int soulSiphonCooldown = 200;

    public static ModConfig get() {
        return instance;
    }

    public static void load() {
        Path path = FabricLoader.getInstance().getConfigDir().resolve(EchoesOfAntiquity.MOD_ID + ".json");

        if (Files.exists(path)) {
            try (Reader reader = Files.newBufferedReader(path)) {
                ModConfig loaded = GSON.fromJson(reader, ModConfig.class);
                if (loaded != null) {
                    instance = loaded;
                }
            } catch (IOException | RuntimeException e) {
                EchoesOfAntiquity.LOGGER.error("Couldn't read {}, falling back to defaults", path, e);
            }
        }

        instance.clamp();
        save(path);
    }

    private static void save(Path path) {
        try {
            Files.createDirectories(path.getParent());
            try (Writer writer = Files.newBufferedWriter(path)) {
                GSON.toJson(instance, writer);
            }
        } catch (IOException e) {
            EchoesOfAntiquity.LOGGER.error("Couldn't write {}", path, e);
        }
    }

    /** Keeps a hand edited file from producing a broken world. */
    private void clamp() {
        voidPedestalRadius = Math.clamp(voidPedestalRadius, 0, 128);
        gravityAnchorRadius = Math.clamp(gravityAnchorRadius, 0, 128);
        magnetRingRange = Math.clamp(magnetRingRange, 0, 32);
        magnetRingSpeed = Math.clamp(magnetRingSpeed, 0.0f, 1.0f);
        waystoneSoulFragmentCost = Math.clamp(waystoneSoulFragmentCost, 1, 64);
        waystoneRecallHealth = Math.clamp(waystoneRecallHealth, 1.0f, 20.0f);
        waystoneHealAmount = Math.clamp(waystoneHealAmount, 1.0f, 20.0f);
        voidRescueDurabilityDivisor = Math.clamp(voidRescueDurabilityDivisor, 1, 100);
        staticPearlUses = Math.clamp(staticPearlUses, 1, 10000);
        sieveDuration = Math.clamp(sieveDuration, 1, 12000);
        uncrafterDuration = Math.clamp(uncrafterDuration, 1, 12000);
        soulSiphonCooldown = Math.clamp(soulSiphonCooldown, 0, 12000);
    }
}
