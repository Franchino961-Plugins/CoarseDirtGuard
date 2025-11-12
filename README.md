# CoarseDirtGuard

Plugin per Spigot/Paper che impedisce **silenziosamente** la trasformazione di blocchi specifici (come la coarse dirt) in dirt path o dirt quando si utilizzano pale o zappe.

## 🔧 Caratteristiche

- ✅ Blocca la trasformazione in dirt path con la pala
- ✅ Blocca la trasformazione in dirt con la zappa
- ✅ Configurazione flessibile - puoi scegliere quali strumenti bloccare
- ✅ Protezione personalizzabile per diversi tipi di blocchi
- ✅ **Nessun messaggio in chat** - funzionamento invisibile
- ✅ **Nessun comando necessario** - funziona automaticamente
- ✅ **Nessun permesso richiesto** - attivo per tutti i player
- ✅ Logging opzionale delle azioni bloccate (disabilitato di default)
- ✅ Compatibile con Minecraft 1.18+

## 📦 Installazione

1. Compila il plugin con Maven: `mvn clean package`
2. Copia il file JAR dalla cartella `target/` nella cartella `plugins/` del tuo server
3. Riavvia il server o usa `/reload` (sconsigliato)

## ⚙️ Configurazione

Il file `config.yml` permette di personalizzare:

```yaml
# Blocchi da proteggere dalla trasformazione
protected-blocks:
  - COARSE_DIRT
  # Puoi aggiungere altri blocchi come:
  # - GRASS_BLOCK
  # - DIRT
  # - PODZOL
  # - MYCELIUM

# Impostazioni generali
settings:
  # Blocca la trasformazione in dirt path con la pala
  block-shovel: true
  
  # Blocca la trasformazione in dirt con la zappa
  block-hoe: true
  
  # Log delle azioni bloccate nella console (opzionale)
  log-blocked-actions: false
```

## 🎮 Comandi

**Nessun comando disponibile** - il plugin funziona automaticamente senza intervento dell'utente.

## 🔐 Permessi

**Nessun permesso necessario** - il plugin funziona per tutti i player automaticamente.

## 📋 Requisiti

- Java 17+
- Spigot/Paper 1.18+
- Maven (per la compilazione)

## 🔨 Compilazione

```bash
mvn clean package
```

Il file JAR sarà generato in `target/coarse-dirt-guard-1.0.0.jar`

## 🐛 Come funziona

Il plugin intercetta l'evento `PlayerInteractEvent` e:
1. Controlla se è un click destro su un blocco
2. Verifica se il blocco è nella lista dei protetti
3. Controlla se il player ha una pala in mano
4. Se tutte le condizioni sono soddisfatte, **annulla silenziosamente l'evento**

**Funzionamento invisibile:** Il player non riceve nessun messaggio, l'azione viene semplicemente bloccata come se nulla fosse successo.
