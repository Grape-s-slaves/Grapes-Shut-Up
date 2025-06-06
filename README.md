# 🤫 Grape's Shut Up (GSU)

**GSU** is a Minecraft mod that completely removes unwanted items, blocks, entities, and structures from your game world. Simply add them to a config file and they'll disappear entirely—no more finding them in chests, no more spawning, no more world generation.

## 🔧 What It Does

GSU removes banned content at the source:
- **Items**: Removed from all loot tables (chests, mob drops, fishing, etc.) and cannot be used as fuel
- **Blocks**: Cannot be placed by players and are removed during world generation  
- **Entities**: Cannot spawn naturally or be summoned into the world
- **Structures**: Prevented from generating in new chunks

GSU doesn't just hide content—it **completely removes it** from the game as if it never existed.

---

## 📦 How to Use

1. **The mod creates a config file at:**
   ```
   config/banned.json
   ```

2. **Add items/blocks/entities/structures to ban:**
   ```json
   {
     "banned": [
       "minecraft:apple",
       "minecraft:diamond", 
       "tinkersconstruct:iron_rod",
       "farmersdelight:apple_pie_slice",
       "minecraft:zombie",
       "minecraft:village"
     ]
   }
   ```

3. **That's it!** The banned content will be completely removed from your world.

---

## ✅ Example Bans

**Items & Blocks:**
```json
{
  "banned": [
    "minecraft:apple",
    "minecraft:diamond_ore",
    "create:brass_ingot",
    "twilightforest:maze_stone"
  ]
}
```

**Entities:**
```json
{
  "banned": [
    "minecraft:creeper",
    "minecraft:zombie", 
    "alexsmobs:grizzly_bear"
  ]
}
```

**Mix Everything:**
```json
{
  "banned": [
    "minecraft:apple",
    "minecraft:creeper",
    "minecraft:diamond_ore",
    "minecraft:village"
  ]
}
```

---

## 🔍 Configuration Options

The mod also creates a Forge config file with these options:

| Setting | Default | Description |
|---------|---------|-------------|
| `enableDebugLogging` | `false` | Shows debug messages when content is removed |
| `removeFromLootTables` | `true` | Removes banned items from all loot generation |

---

## 💡 Notes

- Works with **any content from any mod** — just use the full resource ID
- **Immediate effect** — banned content is removed as soon as you add it to the config
- **World-safe** — doesn't break existing worlds, just prevents new generation
- **Server-friendly** — works on both client and server
- Changes take effect immediately, no restart required

---

## 🎯 Perfect For

- **Modpack creators** who want to disable specific items/blocks
- **Server admins** who need to remove problematic content  
- **Players** who want a cleaner, more curated gameplay experience
- **Challenge runs** where certain items are forbidden

Just add unwanted content to the ban list and enjoy a cleaner Minecraft experience!
