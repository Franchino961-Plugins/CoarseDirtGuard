# Changelog - CoarseDirtGuard

Tutte le modifiche rilevanti al plugin **CoarseDirtGuard** saranno documentate in questo file.

Il formato è basato su [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
e questo progetto segue il [Versionamento Semantico](https://semver.org/spec/v2.0.0.html).

---

## [1.1.0] - 2026-02-01

### Aggiunto
- **Comando Reload**: Comando dedicato `/coarsedirtguard reload` (alias: `/cdg`, `/cdguard`) — ricarica la configurazione senza `/reload confirm`. Richiede il permesso `coarsedirtguard.reload`.
- **Comando Info**: Comando `/coarsedirtguard info` — mostra lo stato del plugin e le impostazioni attive.
- **Permessi Granulari**: Nuovi nodi di permesso: `coarsedirtguard.bypass`, `coarsedirtguard.bypass.shovel`, `coarsedirtguard.bypass.hoe`, `coarsedirtguard.reload`.
- **Modalità Debug**: Nuova opzione `settings.debug` in `config.yml`.

### Modificato
- **Performance**: Sostituiti confronti multipli con `EnumSet` per lookup O(1) in `isShovel()` e `isHoe()`.
- **Performance**: Usato `HashSet<Material>` per i blocchi protetti — lookup più veloci e validazione Material al caricamento della config.
- **Documentazione**: JavaDoc completo per tutte le classi e i metodi.
- **Gestione Errori**: Gestione errori avanzata — validazione Material con fallback a `COARSE_DIRT`. Il plugin si disabilita automaticamente su errori critici di configurazione.
- **Logging**: Logging strutturato con `String.format()`.

### Risolto
- **Stabilità**: Il plugin non va più in crash con config malformata.
- **Validazione**: I Material non validi vengono ignorati silenziosamente con un warning.

---

## [1.0.0] - 2024-12-01

### Aggiunto
- **Prima Release**: Prima release pubblica stabile.
- **Protezione Blocchi**: Blocco silenzioso delle trasformazioni dirt path (pale) e dirt (zappe).
- **Supporto Strumenti**: Supporto per tutti i materiali pale e zappe vanilla (wooden → netherite).
- **Configurazione**: `config.yml` personalizzabile con toggle indipendenti per pale/zappe e logging opzionale.
- **Architettura**: Architettura a singola classe Java per footprint minimale.
- **Eventos**: Event listener su `PlayerInteractEvent`.
- **Dipendenze**: Zero dipendenze runtime (solo Paper API provided).

---

## Roadmap di Sviluppo

### Fase 1 - Prima Release ✅
- Sistema di protezione blocchi per pale e zappe.
- Toggle configurabili per strumento.

### Fase 2 - Comandi e Permessi ✅
- Comando reload dedicato.
- Permessi di bypass granulari.

### Fase 3 - Funzionalità Avanzate 📋
- Configurazione per-mondo.
- Supporto regioni WorldGuard.
- Materiali configurabili aggiuntivi.

---

*Formato: [Versione] - Data*
*Categorie: Aggiunto, Modificato, Risolto, Rimosso*
