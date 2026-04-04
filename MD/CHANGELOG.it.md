# Changelog

Tutte le modifiche rilevanti a questo progetto saranno documentate in questo file.

Il formato è basato su [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
e questo progetto segue il [Versionamento Semantico](https://semver.org/spec/v2.0.0.html).

## [1.1.0] - Febbraio 2026 — Ottimizzazioni e Nuove Funzionalità

### ⚡ Ottimizzazioni Performance
- **EnumSet O(1) Lookup**: Sostituiti confronti multipli con `EnumSet` per lookup O(1)
  - Metodi `isShovel()` e `isHoe()` ora usano `EnumSet.contains()` invece di catene di OR
- **HashSet per Blocchi Protetti**: Usato `HashSet<Material>` per lookup più veloci e validazione al caricamento config

### 🆕 Nuove Funzionalità
- Comando dedicato `/coarsedirtguard reload` (alias: `/cdg`, `/cdguard`)
  - Ricarica configurazione senza `/reload confirm`
  - Richiede permesso `coarsedirtguard.reload`
- Comando `/coarsedirtguard info` — mostra stato del plugin e impostazioni attive
- Sistema permessi granulare:
  - `coarsedirtguard.bypass` — bypassa tutte le protezioni
  - `coarsedirtguard.bypass.shovel` — bypassa solo pale
  - `coarsedirtguard.bypass.hoe` — bypassa solo zappe
  - `coarsedirtguard.reload` — permette reload configurazione
- Modalità debug: nuova opzione `settings.debug` in `config.yml`

### 🔧 Miglioramenti
- JavaDoc completo per tutte le classi e metodi
- Gestione errori avanzata: validazione Material con fallback a `COARSE_DIRT`
- Il plugin si disabilita automaticamente su errori critici di configurazione
- Logging strutturato con `String.format()`

### 🐛 Bug Fix
- Il plugin non va più in crash con config malformata
- I Material non validi vengono ignorati con un warning

## [1.0.0] - Dicembre 2024 — Release Iniziale

### Aggiunto
- Prima release pubblica stabile
- Sistema di protezione blocchi: blocco silenzioso delle trasformazioni dirt path (pale) e dirt (zappe)
- Supporto per tutti i materiali pale e zappe vanilla (wooden → netherite)
- `config.yml` personalizzabile con toggle indipendenti per pale/zappe e logging opzionale
- Architettura a singola classe Java per footprint minimale
- Event listener su `PlayerInteractEvent`
- Zero dipendenze runtime (solo Paper API provided)
- Licenza MIT

## [Non Rilasciato]

### Pianificato
- Configurazione per-mondo
- Supporto regioni WorldGuard
- Materiali configurabili aggiuntivi

---

## Cronologia Versioni

### Come Leggere i Numeri di Versione
- **Major.Minor.Patch** (es. 1.1.0)
  - **Major**: Modifiche incompatibili o aggiunta di funzionalità principali
  - **Minor**: Nuove funzionalità, compatibili con versioni precedenti
  - **Patch**: Correzioni di bug e piccoli miglioramenti

[1.1.0]: https://github.com/Fagghino/CoarseDirtGuard/releases/tag/v1.1.0
[1.0.0]: https://github.com/Fagghino/CoarseDirtGuard/releases/tag/v1.0.0
