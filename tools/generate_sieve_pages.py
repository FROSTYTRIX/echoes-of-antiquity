#!/usr/bin/env python3
"""Rebuilds the guide book sifting pages from the actual sieve recipes.

The sieving template takes its items as plain strings, so the odds shown in the book cannot be read
from the recipe at runtime. Run this after touching a sieve recipe, or the book will drift.
"""
import json, pathlib, re

ROOT = pathlib.Path(__file__).resolve().parent.parent
RECIPES = ROOT / "src/main/resources/data/echoesofantiquity/recipe/sieve"
BOOK = ROOT / "src/main/resources/assets/echoesofantiquity/patchouli_books/lore_book"
SLOTS = 6

def at_least_once(chance, rolls, weight, total):
    return chance * (1.0 - (1.0 - weight / total) ** rolls)

def slot(item_id, percent):
    label = ("%.1f" % (percent * 100)).rstrip("0").rstrip(".") + "%"
    lore = json.dumps({"text": label, "color": "gold", "italic": False}, separators=(",", ":"))
    return "%s[minecraft:lore=['%s']]" % (item_id, lore.replace("'", "\\'"))

def page(title_key, input_id, entries, description_key):
    p = {"type": "echoesofantiquity:sieving", "title": title_key, "input": input_id}
    for i in range(SLOTS):
        p["out%d" % (i + 1)] = entries[i] if i < len(entries) else "minecraft:air"
    p["description"] = description_key
    return p

written = 0
for recipe_file in sorted(RECIPES.glob("*.json")):
    data = json.loads(recipe_file.read_text())
    input_id = data["ingredient"]["item"]
    name = input_id.split(":")[1]

    common, rare = [], []
    for pool in data.get("pools", []):
        chance, rolls = pool.get("chance", 1.0), pool.get("rolls", 1)
        total = sum(max(0, e.get("weight", 1)) for e in pool["entries"])
        if total <= 0:
            continue
        bucket = common if chance >= 0.5 else rare
        for e in pool["entries"]:
            w = max(0, e.get("weight", 1))
            if w > 0:
                bucket.append(slot(e["item"]["id"], at_least_once(chance, rolls, w, total)))

    pages = [{"type": "patchouli:text",
              "text": "entry.echoesofantiquity.sifting.%s.intro" % name,
              "title": "entry.echoesofantiquity.sifting.%s" % name}]
    for i in range(0, len(common), SLOTS):
        pages.append(page("entry.echoesofantiquity.sifting.common", input_id,
                          common[i:i + SLOTS], "entry.echoesofantiquity.sifting.common.desc"))
    for i in range(0, len(rare), SLOTS):
        pages.append(page("entry.echoesofantiquity.sifting.rare", input_id,
                          rare[i:i + SLOTS], "entry.echoesofantiquity.sifting.rare.desc"))

    for lang in ("en_us", "fr_fr"):
        out = BOOK / lang / "entries" / "manual" / ("sifting_%s.json" % name)
        out.parent.mkdir(parents=True, exist_ok=True)
        out.write_text(json.dumps({
            "name": "entry.echoesofantiquity.sifting.%s" % name,
            "icon": input_id,
            "category": "echoesofantiquity:manual",
            "pages": pages,
        }, indent=2) + "\n")
        written += 1
    print("%-12s %d common, %d rare -> %d pages" % (name, len(common), len(rare), len(pages)))

print("%d files written" % written)
