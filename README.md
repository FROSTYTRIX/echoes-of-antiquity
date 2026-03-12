# Echoes of Antiquity

[🇫🇷 Voir le README en Français](./README_FR.md)

---

**Author:** FROSTYTRIX  
**Platform:** Fabric 1.21

---

## 🛠 Materials & Ingredients
<details> 
  <summary> Spoiler </summary>


### 🌑 End Steel Ingot 🟣
* A core material for advanced crafting.
* **Recipe:** Shapeless crafting with 1 Iron Ingot, 1 Popped Chorus Fruit, and 1 Ender Pearl.

### 🖤 Void Treated Leather 🟣
* Leather infused with End energies.
* **Recipe:** 1 Leather and 4 Chorus Fruits in a cross pattern.

### 📜 Ancient Scrip ⚪
* An ancient material used in crafting.
</details>
---

## ⚔️ Weaponry
<details> 
  <summary> Spoiler </summary>
### 🏹 Dragon Bow 🟣
* A powerful long-range weapon.
* Shoots with **twice the velocity** of the vanilla bow and is **more precise**

### 🗡 Relic Blade 🟣
* A sharp blade forged from pure End Steel.
* **Recipe:** 3 End Steel Ingots arranged in a vertical column.

### 🗡️ Relic Greatsword 🟣
* A massive sword with custom animations for holding and attacking.
* **Recipe:** Place a **Relic Blade** (top), a **Stick** (middle), and **Void Treated Leather** (bottom) in a vertical column.
* Has **8.5** Attack damage, but with a **0.6** Attack Speed (lower = slower)

### 🗡️ Soul Siphon 🔵
* Has the **same** Attack Damage as a **Stone Sword** but with an **2.6** Attack Speed
* Entities killed by the **Soul Siphon** will drop a **Soul Fragment**
  </details>
---

## ⬛ Blocks
<details> 
  <summary> Spoiler </summary>
   
### 👣 Ender Boots 🟣
* Grants stealth by stopping vibration signals (Sculk sensors won't detect you).
* **Recipe:** Combine Leather Boots with Void Treated Leather at a Smithing Table.

### 🔮 Void Pedestal 🟣
* Allows the player to showcase one item.
* Prevents teleportation within a **20-block radius** when active.
* **Activation:** Place an **Ender Eye** inside.
* **Visuals:** The Eye rotates and tracks nearby players.
* **Effect:** Applies the `void_pedestal_suppressed` tag to entities in range.

### ⚓ Void Anchor 🟣
* Used as an "anchor" for the **Static Pearl**
* Right-click the Void Anchor with a **magenta dye** to change its texture. You can **wash** the dye with a water bucket.

### 🪚 Uncrafter ⚪
* Takes items and blocks as inputs, outputs a **random** ingredient from its crafting **and** smelting recipes.
* Will **not** output blocks/items that would allow for too big of a **duplication** (ex iron block from iron ingot)

### 🪨 Waystone ⚪
* A teleportation marker.
* **Activation:** Right-click with **10 Soul Fragments** to activate it.
* Remembers the player who placed/activated it and will tp them back if health gets under 2.5 hearts

### 🧲 Gravity Anchor ⚪
* **Activation:** Powered by Redstone.
* **Effect:** When active, it projects an energy field in a **10-block radius** that freezes falling blocks (like Sand or Gravel) in mid-air.
* **Sound Effect:** Humming sound when activated
  </details>
---

## Items
<details> 
  <summary> Spoiler </summary>
   
### 🔘 Static Pearl 🟣
* **Mechanics:** Use once on a Void Anchor to link it to **this** Static Pearl, use again to instantly teleport back to it.
* **Shift + Right-Click** on a new Void Anchor to change which the Pearl is linked to
* Can be used **20 times**

### 🧿 Soul Fragments 🔵
* Used to activate **Waystones** (Costs 10 fragments).

### 🕷️ Climbing Spider Leg ⚪
* Allows any player holding this item (main hand or offhand) to climb walls just like a spider
* Has a 0.5% drop chance from cave spiders

### 📏 Measuring Tape ⚪
* Right-Click a block to set its position has the starting point for the measure.
* Shift + Right-Click a block to set its position has the ending point for the measure.
* Shift + Right-Click in the air to change the distance calculation method.
* Has 2 distance calculations method :
    * Vector distance : Calculates the norm of the vector linking both blocks.
    * Manhattan distance : Calculates the distance between both blocks while staying on the block grid.

### 🪣 Infinite Water Bucket ⚪
* Places water just like a normal bucket.
* **Mechanics:** **Shift + Right-Click** directly on a fluid block to drain/absorb it.

### 📐 Level (Architect's Level) ⚪
* **Mechanics:** Right-click a block to instantly flatten a **3x3 area** around it.
* It will automatically shave off up to 2 blocks above the target level.
* It will automatically fill in holes using **Dirt** from your inventory.

### 🥽 Obsidian Goggles 🟣
* Crafted with Obsidian, Glass Panes, and a Static Pearl.
* Allows the player **to look at Endermen** without angering them. Unlike **Carved Pumpkin**, it **won't** partially cover your view.
</details>
---

## ✨ Potions & Effects 🥛

### 👻 Phasing 🥛
* **Effect:** Allows walking through walls that are **maximum 1 block thick**.
* **Brewing:** Popped Chorus Fruit into an Awkward Potion.
* **Note:** This effect is disabled near an active Void Anchor.

### 📏 Long Arms (Reach) 🥛
* **Effect:** Increases interaction range by **+2.0 blocks**.
* **Brewing:** Popped Chorus Fruit into a Potion of Phasing.

---

## 📦 Creative Tab

* Find all items and blocks annotated with "🟣" in **"The Fallen Humans"** tab.
* Find all items and blocks annotated with "🔵" in **"The Failed Clones"** tab.
* Find all items and blocks annotated with "⚪" in **"The Architect's Tools"** tab.
* Find all items annotated with "🥛" in the vanilla **"Food & Drinks"** tab.
