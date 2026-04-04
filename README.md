# 🛡️ CoarseDirtGuard

[![Version](https://img.shields.io/badge/version-1.1.0-blue.svg)]()
[![Minecraft](https://img.shields.io/badge/minecraft-1.18+-green.svg)](https://www.minecraft.net/)
[![License](https://img.shields.io/badge/license-MIT-yellow.svg)](LICENSE)
[![Spigot](https://img.shields.io/badge/Spigot-1.18+-orange.svg)](https://www.spigotmc.org/)

[![en](https://img.shields.io/badge/lang-en-red.svg)](README.md)
[![it](https://img.shields.io/badge/lang-it-green.svg)](MD/README.it.md)

> 📝 [Changelog](MD/CHANGELOG.en.md)

Lightweight and performant plugin for Spigot/Paper that **silently** prevents the transformation of specific blocks (like coarse dirt) into dirt paths or dirt when using shovels or hoes. Perfect for protecting decorative areas or special terrain without impacting gameplay experience.

## 📋 Features

- 🛡️ **Invisible Protection** - Blocks transformations without chat messages
- 🔧 **Highly Configurable** - Choose which tools (shovels/hoes) to block
- 📦 **Multi-Block** - Protect coarse dirt, podzol, mycelium and other blocks
- ⚡ **Optimal Performance** - EnumSet O(1) lookup, zero TPS impact
- 🔐 **Permission System** - Granular bypasses for admins and builders
- 💻 **Dedicated Commands** - Reload and info without server restart
- 📊 **Advanced Logging** - Track blocked actions with debug mode
- 🔄 **Safe Reload** - Dedicated command instead of `/reload confirm`
- 🎯 **Extended Compatibility** - Minecraft 1.18+ (Spigot/Paper)
- 🪶 **Lightweight** - Single Java class, minimal footprint
- 📖 **Complete JavaDoc** - Documentation for developers

## 📦 Requirements

- **Java:** 17 or higher
- **Server:** Spigot 1.18+ or Paper 1.18+ (Paper recommended for performance)
- **Operating System:** Windows, Linux, macOS

## 🚀 Installation

1. Download the latest release from [GitHub Releases](../../releases) or from [SpigotMC](https://www.spigotmc.org/)
2. Copy the JAR to your server's `plugins/` folder
3. Restart the server
4. The `config.yml` file will be generated automatically
5. Edit `plugins/CoarseDirtGuard/config.yml` if needed and use `/cdg reload`

## 🎮 Commands

| Command | Description | Permission | Default |
|---------|-------------|------------|---------|
| `/coarsedirtguard reload` | Reload configuration | `coarsedirtguard.reload` | OP |
| `/coarsedirtguard info` | Show plugin information | None | Everyone |

**Aliases**: `/cdg`, `/cdguard`

### Examples

```bash
# Reload configuration after changes
/coarsedirtguard reload
/cdg reload

# View plugin status
/coarsedirtguard info
/cdg info
```

## 🔑 Permissions

| Permission | Description | Default |
|------------|-------------|---------|
| `coarsedirtguard.bypass` | Bypasses all plugin protections | OP |
| `coarsedirtguard.bypass.shovel` | Bypasses shovel protection only | OP |
| `coarsedirtguard.bypass.hoe` | Bypasses hoe protection only | OP |
| `coarsedirtguard.reload` | Allows configuration reload | OP |

### Permission Examples

```yaml
# LuckPerms - Give full bypass to builders
lp group builder permission set coarsedirtguard.bypass true

# LuckPerms - Allow shovels only for landscapers
lp group landscaper permission set coarsedirtguard.bypass.shovel true

# LuckPerms - Admin can reload
lp group admin permission set coarsedirtguard.reload true
```

## ⚙️ Configuration

### config.yml

The `config.yml` file is automatically generated on first startup:

```yaml
# CoarseDirtGuard Configuration
# Plugin that prevents the transformation of specific blocks into dirt path/dirt

# Blocks to protect from transformation
# Possible values: GRASS_BLOCK, DIRT, COARSE_DIRT, PODZOL, MYCELIUM, ROOTED_DIRT
protected-blocks:
  - COARSE_DIRT
  # Add other blocks as needed:
  # - PODZOL
  # - MYCELIUM
  # - GRASS_BLOCK

# General settings
settings:
  # Block transformation to dirt path with shovels
  block-shovel: true
  
  # Block transformation to dirt with hoes
  block-hoe: true
  
  # Log blocked actions in console (for debugging/statistics)
  log-blocked-actions: false
  
  # Debug mode - detailed logging for troubleshooting
  debug: false
```

### Detailed Configuration Options

| Option | Type | Default | Description |
|--------|------|---------|-------------|
| `protected-blocks` | List | `[COARSE_DIRT]` | Blocks to protect (Bukkit Material names) |
| `settings.block-shovel` | Boolean | `true` | Block shovels (dirt path creation) |
| `settings.block-hoe` | Boolean | `true` | Block hoes (dirt creation) |
| `settings.log-blocked-actions` | Boolean | `false` | Enable console logging of blocked actions |
| `settings.debug` | Boolean | `false` | Enable debug mode with detailed logging |

## 🔬 Technical Details

### Architecture

```
CoarseDirtGuard.java
│
├── onEnable()
│   ├── saveDefaultConfig()
│   ├── loadConfig()
│   └── registerEvents(this)
│
├── onPlayerInteract(PlayerInteractEvent)
│   ├── Check: RIGHT_CLICK_BLOCK?
│   ├── Check: Protected block?
│   ├── Check: Shovel/hoe tool?
│   ├── Check: Tool enabled in config?
│   ├── event.setCancelled(true)  [SILENT BLOCK]
│   └── logger.info() [IF log-blocked-actions=true]
│
├── isShovel(Material) → Boolean
└── isHoe(Material) → Boolean
```

### Event Flow

#### 1. Player Right-Clicks with Shovel on Coarse Dirt
```
PlayerInteractEvent triggered
→ Action = RIGHT_CLICK_BLOCK ✓
→ Block = COARSE_DIRT ✓ (in protected-blocks)
→ Item = IRON_SHOVEL ✓ (isShovel returns true)
→ block-shovel = true ✓
→ event.setCancelled(true) [SILENT BLOCK]
→ Player sees no message, coarse dirt remains unchanged
```

#### 2. Player Right-Clicks with Hoe on Coarse Dirt
```
PlayerInteractEvent triggered
→ Action = RIGHT_CLICK_BLOCK ✓
→ Block = COARSE_DIRT ✓ (in protected-blocks)
→ Item = DIAMOND_HOE ✓ (isHoe returns true)
→ block-hoe = true ✓
→ event.setCancelled(true) [SILENT BLOCK]
→ Player sees no message, coarse dirt doesn't become dirt
```

#### 3. Player Right-Clicks with Other Tool
```
PlayerInteractEvent triggered
→ Item != Shovel/Hoe
→ No intervention, event proceeds normally
```

### Data Persistence

The plugin **does not save persistent data**. Uses only static configuration:
- File: `plugins/CoarseDirtGuard/config.yml`
- Loading: At startup and on reload
- Format: Standard YAML

### Supported Tools

**Shovels:**
- `WOODEN_SHOVEL`
- `STONE_SHOVEL`
- `IRON_SHOVEL`
- `GOLDEN_SHOVEL`
- `DIAMOND_SHOVEL`
- `NETHERITE_SHOVEL`

**Hoes:**
- `WOODEN_HOE`
- `STONE_HOE`
- `IRON_HOE`
- `GOLDEN_HOE`
- `DIAMOND_HOE`
- `NETHERITE_HOE`

## 📖 How It Works

### Practical Use Case Scenario

1. **Admin configures the plugin:**
   - Install CoarseDirtGuard
   - Open `config.yml` and add `PODZOL` to `protected-blocks` list
   - Save and restart the server

2. **Player tries to create a path:**
   - Takes an iron shovel
   - Right-clicks a coarse dirt block
   - The block **does not transform** into a dirt path
   - **No message** appears in chat
   - Player thinks the click wasn't registered (vanilla-like behaviour)

3. **Admin monitors actions (optional):**
   - Set `log-blocked-actions: true` in config
   - Reload: `/reload confirm`
   - Console shows: `[CoarseDirtGuard] Blocked COARSE_DIRT transformation by Steve at coordinates 100, 64, -200`

4. **Selective Administration:**
   - Disable `block-hoe: false` to allow coarse dirt → dirt transformation with hoes
   - Keep `block-shovel: true` to block dirt path
   - Reload without server restart

## 🎯 Project Structure

```
CoarseDirtGuard/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── franchino961/
│       │           └── coarsedirtguard/
│       │               └── CoarseDirtGuard.java    [Main Plugin Class]
│       └── resources/
│           ├── config.yml                          [Default Configuration]
│           └── plugin.yml                          [Plugin Manifest]
├── target/
│   ├── CoarseDirtGuard-1.0.0.jar                   [Compiled Plugin]
│   └── classes/                                    [Compiled Classes]
├── pom.xml                                         [Maven Configuration]
├── LICENSE                                         [MIT License]
└── README.md                                       [This File]
```

## 🐛 Troubleshooting

### Plugin doesn't block transformations

**Solutions:**
1. Verify the block is in `protected-blocks` list with correct Material name
2. Check that `block-shovel` or `block-hoe` are `true` in config.yml
3. Restart server or use `/reload confirm` after config changes
4. Verify compatible server version (1.18+)
5. Check console for loading errors

### How to find correct Material name for a block?

**Solutions:**
1. Complete list: [Bukkit Material Enum](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html)
2. In-game: Use debug plugin like WorldEdit (`//wand` then click block)
3. Common examples:
   - Coarse Dirt: `COARSE_DIRT`
   - Podzol: `PODZOL`
   - Mycelium: `MYCELIUM`
   - Grass Block: `GRASS_BLOCK`
   - Rooted Dirt: `ROOTED_DIRT` (1.17+)

### Plugin doesn't load on server 1.17 or earlier

**Solutions:**
1. This plugin requires **Minecraft 1.18+** (api-version: 1.18 in plugin.yml)
2. For earlier versions, modify:
   - `api-version: 1.17` in [plugin.yml](src/main/resources/plugin.yml)
   - Paper API version in [pom.xml](pom.xml) to `1.17.1-R0.1-SNAPSHOT`
3. Recompile: `mvn clean package`

### Logging doesn't work

**Solutions:**
1. Verify `log-blocked-actions: true` in config.yml
2. Restart server or reload
3. Check server logging level (bukkit.yml or spigot.yml)
4. Logs appear in server console, not in chat

### Conflict with other protection plugins

**Solutions:**
1. Check event priority (CoarseDirtGuard uses EventPriority.NORMAL)
2. Land protection plugins (WorldGuard, GriefPrevention) may take precedence
3. If block is already protected by other plugins, CoarseDirtGuard is redundant
4. Use `log-blocked-actions: true` to verify if plugin intervenes

## 💡 Practical Examples

### Example 1: Spawn Decorative Area Protection

**Goal:** Protect spawn decorative area with coarse dirt and podzol.

**Configuration:**
```yaml
protected-blocks:
  - COARSE_DIRT
  - PODZOL

settings:
  block-shovel: true
  block-hoe: true
  log-blocked-actions: true  # Monitor attempts
```

**Result:** Players cannot ruin decoration by transforming blocks into dirt path or normal dirt.

---

### Example 2: Selective Protection for Mycelium Farm

**Goal:** Prevent mycelium transformation into dirt path (shovels) but allow farming with hoes.

**Configuration:**
```yaml
protected-blocks:
  - MYCELIUM

settings:
  block-shovel: true   # Block shovels
  block-hoe: false     # Allow hoes
  log-blocked-actions: false
```

**Result:** Mushroom farm protected from accidental path creation, but hoes work normally.

---

### Example 3: Multi-Block Protection for Vanilla+ Server

**Goal:** Maintain terrain variety in survival world without transformations.

**Configuration:**
```yaml
protected-blocks:
  - COARSE_DIRT
  - PODZOL
  - MYCELIUM
  - ROOTED_DIRT
  - GRASS_BLOCK

settings:
  block-shovel: true
  block-hoe: true
  log-blocked-actions: false
```

**Result:** All special terrain types protected, vanilla experience preserved.

---

### Example 4: Debug and Analysis with Logging

**Goal:** Identify areas where players frequently attempt to create paths.

**Configuration:**
```yaml
protected-blocks:
  - COARSE_DIRT

settings:
  block-shovel: true
  block-hoe: false
  log-blocked-actions: true  # Enable tracking
```

**Console Output:**
```
[CoarseDirtGuard] Blocked COARSE_DIRT transformation by Steve at coordinates 120, 65, -340
[CoarseDirtGuard] Blocked COARSE_DIRT transformation by Alex at coordinates 121, 65, -339
```

**Usage:** Admin identifies coordinates 120,65,-340 as high-traffic area and decides whether to create permanent path or add signage.

---

### Example 5: Minigame Server with Periodic Reset

**Goal:** Protect terrain in minigame arena until reset.

**Configuration:**
```yaml
protected-blocks:
  - COARSE_DIRT
  - DIRT
  - GRASS_BLOCK

settings:
  block-shovel: true
  block-hoe: true
  log-blocked-actions: false
```

**Workflow:**
1. Set up arena with protected blocks
2. Minigame runs without terrain modifications
3. Reset arena with world management plugin
4. Cycle repeats

## 📄 License

This project is released under the **MIT** license — see the [LICENSE](LICENSE) file for details.

## 👤 Author

**Franchino961** — [GitHub](https://github.com/Franchino961-Plugins)

## 🤝 Contributing

Contributions are welcome!
- 🐛 Report bugs in [issues](../../issues)
- 💡 Propose new features
- 🔧 Submit Pull Requests

## 💬 Support

For bug reports, feature requests, or questions:
- Open an [issue](../../issues) on GitHub
- Contact the developer

## 🔗 Useful Links

- [Spigot API Documentation](https://hub.spigotmc.org/javadocs/spigot/)
- [Paper API Documentation](https://jd.papermc.io/paper/1.20/)
- [Bukkit Material Enum](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html)
- [Maven Plugin Development](https://maven.apache.org/guides/introduction/introduction-to-plugins.html)
- [Paper Discussion Forum](https://forums.papermc.io/)

---

## 📝 Changelog

See [CHANGELOG.en.md](MD/CHANGELOG.en.md) for complete version history.
