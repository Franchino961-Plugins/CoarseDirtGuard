# 🛡️ CoarseDirtGuard

[![Version](https://img.shields.io/badge/version-1.1.0-blue.svg)]()
[![Minecraft](https://img.shields.io/badge/minecraft-1.18+-green.svg)](https://www.minecraft.net/)
[![License](https://img.shields.io/badge/license-MIT-yellow.svg)](../LICENSE)
[![Spigot](https://img.shields.io/badge/Spigot-1.18+-orange.svg)](https://www.spigotmc.org/)

[![en](https://img.shields.io/badge/lang-en-red.svg)](../README.md)
[![it](https://img.shields.io/badge/lang-it-green.svg)](README.it.md)

> 📝 **Changelog**: Consulta [CHANGELOG.it.md](CHANGELOG.it.md) per la cronologia delle versioni.

---

## 📖 Panoramica

Plugin leggero e performante per Spigot/Paper che impedisce **silenziosamente** la trasformazione di blocchi specifici (come la coarse dirt) in dirt path o dirt quando si utilizzano pale o zappe. Perfetto per proteggere aree decorative o terreni speciali senza impattare l'esperienza di gioco.

## 📋 Caratteristiche

- 🛡️ **Protezione invisibile** - Blocca le trasformazioni senza messaggi in chat
- 🔧 **Altamente configurabile** - Scegli quali strumenti (pale/zappe) bloccare
- 📦 **Multi-blocco** - Proteggi coarse dirt, podzol, mycelium e altri blocchi
- ⚡ **Prestazioni ottimali** - EnumSet O(1) lookup, zero impatto su TPS
- 🔐 **Sistema permessi** - Bypass granulari per admin e builder
- 💻 **Comandi dedicati** - Reload e info senza restart server
- 📊 **Logging avanzato** - Traccia azioni bloccate con modalità debug
- 🔄 **Reload sicuro** - Comando dedicato invece di `/reload confirm`
- 🎯 **Compatibilità estesa** - Minecraft 1.18+ (Spigot/Paper)
- 🪶 **Leggerissimo** - Singola classe Java, footprint minimale
- 📖 **JavaDoc completo** - Documentazione per sviluppatori

## 📦 Requisiti

- **Java:** 17 o superiore
- **Server:** Spigot 1.18+ o Paper 1.18+ (consigliato Paper per performance)
- **Sistema Operativo:** Windows, Linux, macOS

## 🚀 Installazione

1. Scarica l'ultima release da [GitHub Releases](../../releases) o da [SpigotMC](https://www.spigotmc.org/)
2. Copia il JAR nella cartella `plugins/` del tuo server
3. Riavvia il server
4. Il file `config.yml` verrà generato automaticamente
5. Modifica `plugins/CoarseDirtGuard/config.yml` se necessario e usa `/cdg reload`

## 🎮 Comandi

| Comando | Descrizione | Permesso | Default |
|---------|-------------|----------|---------|
| `/coarsedirtguard reload` | Ricarica la configurazione | `coarsedirtguard.reload` | OP |
| `/coarsedirtguard info` | Mostra informazioni sul plugin | Nessuno | Tutti |

**Alias**: `/cdg`, `/cdguard`

### Esempi

```bash
# Ricarica configurazione dopo modifiche
/coarsedirtguard reload
/cdg reload

# Visualizza stato plugin
/coarsedirtguard info
/cdg info
```

## 🔑 Permessi

| Permesso | Descrizione | Default |
|----------|-------------|---------|
| `coarsedirtguard.bypass` | Bypassa tutte le protezioni del plugin | OP |
| `coarsedirtguard.bypass.shovel` | Bypassa solo la protezione delle pale | OP |
| `coarsedirtguard.bypass.hoe` | Bypassa solo la protezione delle zappe | OP |
| `coarsedirtguard.reload` | Permette di ricaricare la configurazione | OP |

### Esempi Permessi

```yaml
# LuckPerms - Dare bypass completo a builder
lp group builder permission set coarsedirtguard.bypass true

# LuckPerms - Permettere solo pale a landscaper
lp group landscaper permission set coarsedirtguard.bypass.shovel true

# LuckPerms - Admin può ricaricare
lp group admin permission set coarsedirtguard.reload true
```

## ⚙️ Configurazione

### config.yml

Il file `config.yml` viene generato automaticamente al primo avvio:

```yaml
# Configurazione CoarseDirtGuard
# Plugin che impedisce la trasformazione di blocchi specifici in dirt path/dirt

# Blocchi da proteggere dalla trasformazione
# Valori possibili: GRASS_BLOCK, DIRT, COARSE_DIRT, PODZOL, MYCELIUM, ROOTED_DIRT
protected-blocks:
  - COARSE_DIRT
  # Aggiungi altri blocchi secondo necessità:
  # - PODZOL
  # - MYCELIUM
  # - GRASS_BLOCK

# Impostazioni generali
settings:
  # Blocca la trasformazione in dirt path con pale
  block-shovel: true
  
  # Blocca la trasformazione in dirt con zappe
  block-hoe: true
  
  # Log delle azioni bloccate nella console (per debug/statistiche)
  log-blocked-actions: false
  
  # Modalità debug - logging dettagliato per troubleshooting
  debug: false
```

### Opzioni di Configurazione Dettagliate

| Opzione | Tipo | Default | Descrizione |
|---------|------|---------|-------------|
| `protected-blocks` | Lista | `[COARSE_DIRT]` | Blocchi da proteggere (nomi Material Bukkit) |
| `settings.block-shovel` | Boolean | `true` | Blocca pale (dirt path creation) |
| `settings.block-hoe` | Boolean | `true` | Blocca zappe (dirt creation) |
| `settings.log-blocked-actions` | Boolean | `false` | Abilita logging console delle azioni bloccate |
| `settings.debug` | Boolean | `false` | Abilita modalità debug con logging dettagliato |

## 🔬 Dettagli Tecnici

### Architettura

```
CoarseDirtGuard.java
│
├── onEnable()
│   ├── saveDefaultConfig()
│   ├── loadConfig()
│   └── registerEvents(this)
│
├── onPlayerInteract(PlayerInteractEvent)
│   ├── Verifica: RIGHT_CLICK_BLOCK?
│   ├── Verifica: Blocco protetto?
│   ├── Verifica: Strumento vale/zappa?
│   ├── Verifica: Strumento abilitato in config?
│   ├── event.setCancelled(true)  [SILENT BLOCK]
│   └── logger.info() [SE log-blocked-actions=true]
│
├── isShovel(Material) → Boolean
└── isHoe(Material) → Boolean
```

### Flow degli Eventi

#### 1. Player Click Destro con Pala su Coarse Dirt
```
PlayerInteractEvent trigger
→ Action = RIGHT_CLICK_BLOCK ✓
→ Block = COARSE_DIRT ✓ (in protected-blocks)
→ Item = IRON_SHOVEL ✓ (isShovel returns true)
→ block-shovel = true ✓
→ event.setCancelled(true) [BLOCCO SILENZIOSO]
→ Player non vede messaggio, coarse dirt rimane invariata
```

#### 2. Player Click Destro con Zappa su Coarse Dirt
```
PlayerInteractEvent trigger
→ Action = RIGHT_CLICK_BLOCK ✓
→ Block = COARSE_DIRT ✓ (in protected-blocks)
→ Item = DIAMOND_HOE ✓ (isHoe returns true)
→ block-hoe = true ✓
→ event.setCancelled(true) [BLOCCO SILENZIOSO]
→ Player non vede messaggio, coarse dirt non diventa dirt
```

#### 3. Player Click Destro con Altro Strumento
```
PlayerInteractEvent trigger
→ Item != Shovel/Hoe
→ Nessun intervento, evento procede normalmente
```

### Persistenza Dati

Il plugin **non salva dati persistenti**. Utilizza solo configurazione statica:
- File: `plugins/CoarseDirtGuard/config.yml`
- Caricamento: All'avvio e su reload
- Formato: YAML standard

### Strumenti Supportati

**Pale (Shovel):**
- `WOODEN_SHOVEL`
- `STONE_SHOVEL`
- `IRON_SHOVEL`
- `GOLDEN_SHOVEL`
- `DIAMOND_SHOVEL`
- `NETHERITE_SHOVEL`

**Zappe (Hoe):**
- `WOODEN_HOE`
- `STONE_HOE`
- `IRON_HOE`
- `GOLDEN_HOE`
- `DIAMOND_HOE`
- `NETHERITE_HOE`

## 📖 Come Funziona

### Scenario d'Uso Pratico

1. **Admin configura il plugin:**
   - Installa CoarseDirtGuard
   - Apre `config.yml` e aggiunge `PODZOL` alla lista `protected-blocks`
   - Salva e riavvia il server

2. **Player prova a creare un sentiero:**
   - Prende una pala di ferro
   - Click destro su un blocco di coarse dirt
   - Il blocco **non si trasforma** in dirt path
   - **Nessun messaggio** appare in chat
   - Player pensa che il click non sia stato registrato (comportamento vanilla-like)

3. **Admin monitora le azioni (opzionale):**
   - Imposta `log-blocked-actions: true` in config
   - Reload: `/reload confirm`
   - Console mostra: `[CoarseDirtGuard] Bloccata trasformazione di COARSE_DIRT da parte di Steve alle coordinate 100, 64, -200`

4. **Amministrazione selettiva:**
   - Disabilita `block-hoe: false` per permettere trasformazione coarse dirt → dirt con zappe
   - Mantiene `block-shovel: true` per bloccare dirt path
   - Reload senza restart del server

## 🎯 Struttura del Progetto

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

### Plugin non blocca le trasformazioni

**Soluzioni:**
1. Verifica che il blocco sia nella lista `protected-blocks` con il nome Material corretto
2. Controlla che `block-shovel` o `block-hoe` siano `true` nel config.yml
3. Riavvia il server o usa `/reload confirm` dopo modifiche alla config
4. Verifica versione server compatibile (1.18+)
5. Controlla console per errori di caricamento

### Come trovare il nome Material corretto di un blocco?

**Soluzioni:**
1. Lista completa: [Bukkit Material Enum](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html)
2. In-game: Usa un plugin di debug come WorldEdit (`//wand` poi click sul blocco)
3. Esempi comuni:
   - Coarse Dirt: `COARSE_DIRT`
   - Podzol: `PODZOL`
   - Mycelium: `MYCELIUM`
   - Grass Block: `GRASS_BLOCK`
   - Rooted Dirt: `ROOTED_DIRT` (1.17+)

### Plugin non si carica su server 1.17 o precedenti

**Soluzioni:**
1. Questo plugin richiede **Minecraft 1.18+** (api-version: 1.18 in plugin.yml)
2. Per versioni precedenti, modifica:
   - `api-version: 1.17` in [plugin.yml](src/main/resources/plugin.yml)
   - Paper API version in [pom.xml](pom.xml) a `1.17.1-R0.1-SNAPSHOT`
3. Ricompila: `mvn clean package`

### Logging non funziona

**Soluzioni:**
1. Verifica `log-blocked-actions: true` nel config.yml
2. Riavvia il server o reload
3. Controlla il livello di logging del server (bukkit.yml o spigot.yml)
4. I log appaiono nella console del server, non in chat

### Conflitto con altri plugin di protezione

**Soluzioni:**
1. Verifica priorità eventi (CoarseDirtGuard usa EventPriority.NORMAL)
2. Plugin di protezione terra (WorldGuard, GriefPrevention) possono avere precedenza
3. Se il blocco è già protetto da altri plugin, CoarseDirtGuard è ridondante
4. Usa `log-blocked-actions: true` per verificare se il plugin interviene

## 💡 Esempi Pratici

### Esempio 1: Protezione Area Decorativa Spawn

**Obiettivo:** Proteggere un'area decorativa allo spawn con coarse dirt e podzol.

**Configurazione:**
```yaml
protected-blocks:
  - COARSE_DIRT
  - PODZOL

settings:
  block-shovel: true
  block-hoe: true
  log-blocked-actions: true  # Monitora tentativi
```

**Risultato:** Player non possono rovinare la decorazione trasformando i blocchi in dirt path o dirt normale.

---

### Esempio 2: Protezione Selettiva per Farm Mycelium

**Obiettivo:** Impedire trasformazione mycelium in dirt path (pale) ma permettere farming con zappe.

**Configurazione:**
```yaml
protected-blocks:
  - MYCELIUM

settings:
  block-shovel: true   # Blocca pale
  block-hoe: false     # Permetti zappe
  log-blocked-actions: false
```

**Risultato:** Farm di funghi protetta da creazione accidentale di sentieri, ma zappe funzionano normalmente.

---

### Esempio 3: Protezione Multi-Blocco per Server Vanilla+

**Obiettivo:** Mantenere varietà terreni in mondo survival senza trasformazioni.

**Configurazione:**
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

**Risultato:** Tutti i tipi di terreno speciali sono protetti, esperienza vanilla preservata.

---

### Esempio 4: Debug e Analisi con Logging

**Obiettivo:** Identificare aree dove player tentano frequentemente di creare sentieri.

**Configurazione:**
```yaml
protected-blocks:
  - COARSE_DIRT

settings:
  block-shovel: true
  block-hoe: false
  log-blocked-actions: true  # Abilita tracciamento
```

**Console Output:**
```
[CoarseDirtGuard] Bloccata trasformazione di COARSE_DIRT da parte di Steve alle coordinate 120, 65, -340
[CoarseDirtGuard] Bloccata trasformazione di COARSE_DIRT da parte di Alex alle coordinate 121, 65, -339
```

**Utilizzo:** Admin identifica coordinate 120,65,-340 come area ad alto traffico e decide se creare un sentiero permanente o mettere segnaletica.

---

### Esempio 5: Server Minigame con Reset Periodico

**Obiettivo:** Proteggere terreno in arena minigame fino al reset.

**Configurazione:**
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
1. Setup arena con blocchi protetti
2. Minigame esegue senza modifiche terreno
3. Reset arena con plugin di world management
4. Ciclo si ripete

## 📄 Licenza

Questo progetto è rilasciato sotto licenza **MIT** — vedi il file [LICENSE](../LICENSE) per i dettagli.

## 👤 Autore

**Franchino961** — [GitHub](https://github.com/Franchino961-Plugins)

## 🤝 Contributi

I contributi sono benvenuti!
- 🐛 Segnala bug nelle [issues](../../issues)
- 💡 Proponi nuove funzionalità
- 🔧 Invia Pull Request

## 💬 Supporto

Per bug report, richieste di funzionalità o domande:
- Apri una [issue](../../issues) su GitHub
- Contatta lo sviluppatore

## 🔗 Link Utili

- [Spigot API Documentation](https://hub.spigotmc.org/javadocs/spigot/)
- [Paper API Documentation](https://jd.papermc.io/paper/1.20/)
- [Bukkit Material Enum](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html)
- [Maven Plugin Development](https://maven.apache.org/guides/introduction/introduction-to-plugins.html)
- [Paper Discussion Forum](https://forums.papermc.io/)

---
