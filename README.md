# Echoes of Antiquity

**Author:** FROSTYTRIX  
**Version:** 1.0.0  
**Platform:** Fabric 1.21

---
[🇫🇷 Voir le README en Français](./README_FR.md)

---

## Overview
**Echoes of Antiquity** is a Minecraft mod inspired by Game Theory's exploration of Minecraft lore. It introduces ancient artifacts, specialized gear, and mechanics centered around the Void and the End.

---

## Items & Gear

### 📜 Ancient Scrip
* A basic item used in the mod's progression.

### 🖤 Void Treated Leather
* A crafting material infused with End energies.
* **Recipe:** 1 Leather and 4 Chorus Fruits arranged in a **cross pattern** in a crafting table.

### 👣 Ender Boots
* Specialized footwear that grants the wearer stealth properties.
* **Recipe:** Created at a **Smithing Table** by combining Leather Boots with Void Treated Leather.
* **Abilities:** While worn, the boots occlude vibration signals, preventing detection by vibration-sensitive mechanics like Sculk.

---

## Blocks

### ⚓ Void Anchor
* A pedestal-like block capable of holding items.
* **Mechanics:** When an Eye of Ender is placed on the anchor, it becomes **ACTIVE**.
* **Suppression Field:** An active Void Anchor creates a **20-block radius** suppression field. Within this area, teleportation is disabled for:
    * Endermen
    * Shulkers
    * Ender Pearls
    * Players with the Phasing Effect
* **Visuals:** The Eye of Ender on the anchor will rotate and track the nearest player.

---

## Potions & Effects

### ✨ Phasing
* **Effect:** Allows the entity to walk through solid walls (approximately 2 blocks thick) when moving.
* **Brewing:** Created by brewing a **Popped Chorus Fruit** into an **Awkward Potion**.
* **Limitation:** Phasing is suppressed and cannot be used near an active Void Anchor.

### 📏 Reach
* **Effect:** Increases both player interaction range and block placement range by **+2.0 units**.
* **Brewing:** Created by brewing a **Popped Chorus Fruit** into a **Potion of Phasing**.
> **Note:** Make sure your code in `ModPotions.java` matches this recipe, as it currently uses a Potion of Weakness as a base!

---

## Creative Tab
All mod content can be found in **"The Fallen Humans"** creative inventory tab, except for potions, which are located in the **Consumables** tab.
