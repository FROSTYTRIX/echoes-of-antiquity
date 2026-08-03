# Echoes of Antiquity — 1.0.0

The Foundation. The first stable release. **Patchouli is no longer required**, a
**config file** lands, **26 advancements** chart the progression, End Steel becomes a
real **armor trim**, and the Sieve gains **weighted pools** and an **EMI page** — on
top of a long list of bugs that had been quietly breaking half the mod.

---

## 📖 Patchouli is optional

- **The mod runs without it.** Patchouli moved from a hard dependency to a suggestion,
  so a pack no longer has to ship it just to load Echoes of Antiquity.
- **The lore book has a recipe** — a Book plus an Ancient Scrip. It sits behind a
  resource condition, so it only appears when Patchouli is actually installed, and
  quietly does not exist otherwise. This also finally gives the Ancient Scrip a use. **BUT** this lore book is still **\[WIP\]**

## ⚙️ A config file

- **`config/echoesofantiquity.json`**, plain JSON, no config library pulled in. It
  covers the Void Pedestal and Gravity Anchor radiuses, the Magnet Ring range and
  strength, the Waystone's Soul Fragment cost and how much it heals, the durability
  the void rescue eats, the Static Pearl's uses, both machine speeds, and the Soul
  Siphon cooldown.
- Values are **clamped on load**, so a typo in the file cannot produce a broken world,
  and the file is rewritten afterwards — new options appear on their own after an
  update.

## 🏆 26 advancements

- **A single tree** rooted on the End Steel Ingot, branching into the Fallen Humans,
  the Failed Clones and the Architect's Tools. (May change)
- Fully translated in English and French.

## 🌑 End Steel armor trim

- **End Steel is a trim material.** Any vanilla armour can be trimmed with it at the
  smithing table, in a turquoise palette drawn from the ingot's own colours.
- **It works on every armour, not just this mod's.** The extra trim entry is injected
  into each armour model as it loads, rather than shipping edited copies of the
  vanilla files — so it will not fight another trim mod, and it will not freeze an old
  copy of a vanilla model.

## 🧺 The Sieve

- **Weighted pools.** A pool is now `{chance, rolls, entries}` with a weight on each
  entry, so a recipe can say "roughly two things, mostly flint, rarely gold" instead of
  rolling every result on its own. The three sifting recipes are rebalanced onto it.
  Older `results` lists still work.
- **EMI support.** A Sifting category shows the input, the Soul Fragment as fuel, and
  every possible output with its real odds, worked out from the weights and the roll
  count rather than written by hand.
- ⚠️ **Sand now yields at most 2 items per sift** instead of up to 6. The averages are
  tuned to match the old ones, but the swingy jackpots are gone.

## 🧲 Quality of life

- **The Magnet Ring has an Off mode.** It cycles Off → Attracting → Repulsing, with a
  click to confirm, and does no work at all while off. It used to run permanently from
  your inventory with no way to stop it.
- **Ender gear and Void Chainmail can be repaired again** — Void Treated Leather for
  the Ender tier, End Steel Ingots for the Void Chainmail. They had no repair material
  at all, so the anvil refused them.

## 🎯 Bug fixes

- **The Void Pedestal blocks Phasing again.** The tag it used was renamed a while back
  and the effect was still looking for the old name, so walking through walls ignored
  the pedestal entirely. The whole tag mechanism is gone; teleports now ask a registry
  directly, which also stops the mod leaving dead tags in player data.
- **Gravity Anchors survive a restart.** Nothing re-registered them when the server
  came back up, so an anchor stayed lit and did nothing until you toggled it. They
  register themselves now, and also recover from a chunk reload.
- **Void Chainmail no longer drops you back at the lip of the void.** Walking off a
  ledge keeps you "on ground" for a tick while your centre is already over nothing, so
  the armour was saving a column of air as your last safe spot and returning you
  straight to it. It now checks there is really something to stand on, and looks for a
  nearby safe spot if the ground was mined while you were away.
- **Phasing no longer triggers while standing still.** The old check passed on any
  movement at all, and gravity always leaves some — so facing a wall was enough to go
  through it. You have to actually walk into the wall now.
- **The Architect's Level fills holes instead of levelling everything.** It was
  destroying every block in the 3×3, ores included. It now only shaves natural bumps
  and fills genuine gaps.
- **The Sieve and Uncrafter screens close properly.** Both stayed usable from any
  distance and even after the block was broken.
- **The Soul Siphon actually drops fragments.** The death check ran a moment too early
  and almost never passed. The right click also gained a cooldown; it could be spammed
  for permanent Strength and Speed.
- **The Sieve's progress arrow is no longer stretched.**
- **The Relic Blade is in the creative menu** — it was in no tab at all.
- **Fixed a crash** when wearing a mix of armour and non-armour pieces, and several
  cases where a dispenser or a fake player could crash the Static Pearl, the Level or
  the Measuring Tape.
- Added the missing French translations and the Sieve's sound subtitle.

## 🧹 Under the hood

- **Cheaper anchor and pedestal lookups.** Both were flat lists, so every falling block
  checked every anchor in the dimension, every tick. They share a chunk-bucketed index
  now and only look where the radius reaches. The Void Pedestal also stopped scanning
  for nearby entities on every single tick.
- **Typed data components** replace the raw NBT the Magnet Ring, Measuring Tape and
  Void Chainmail were storing their state in.
  ⚠️ Rings, tapes and chestplates from an older world lose their saved state.
- **Client-only mixins are separated**, so dedicated servers stop logging failures
  trying to load them.
- **Credits.** [CREDITS.md](./CREDITS.md) lists the code borrowed from
  ImplementedInventory, structurized-reborn, MedievalWeapons and StructureTutorialMod,
  with their licences. The LICENSE file also had its placeholders filled in.
- The placeholder block and the leftover example mixin are gone.
