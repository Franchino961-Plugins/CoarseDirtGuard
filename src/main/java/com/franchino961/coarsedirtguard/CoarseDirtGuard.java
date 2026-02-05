package com.franchino961.coarsedirtguard;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * CoarseDirtGuard - Plugin che impedisce la trasformazione di blocchi specifici
 * in dirt path (con pale) o dirt (con zappe).
 * 
 * @author Franchino961
 * @version 1.1.0
 * @since 1.0.0
 */
public class CoarseDirtGuard extends JavaPlugin implements Listener {

    private Logger logger;
    private Set<Material> protectedBlocks;
    private boolean logBlockedActions;
    private boolean blockShovel;
    private boolean blockHoe;
    private boolean debugMode;

    /**
     * EnumSet contenente tutti i tipi di pale per lookup O(1)
     */
    private static final EnumSet<Material> SHOVELS = EnumSet.of(
            Material.WOODEN_SHOVEL,
            Material.STONE_SHOVEL,
            Material.IRON_SHOVEL,
            Material.GOLDEN_SHOVEL,
            Material.DIAMOND_SHOVEL,
            Material.NETHERITE_SHOVEL);

    /**
     * EnumSet contenente tutti i tipi di zappe per lookup O(1)
     */
    private static final EnumSet<Material> HOES = EnumSet.of(
            Material.WOODEN_HOE,
            Material.STONE_HOE,
            Material.IRON_HOE,
            Material.GOLDEN_HOE,
            Material.DIAMOND_HOE,
            Material.NETHERITE_HOE);

    /**
     * Chiamato quando il plugin viene abilitato.
     * Inizializza configurazione, logger e registra eventi.
     */
    @Override
    public void onEnable() {
        this.logger = getLogger();

        // Salva la configurazione predefinita se non esiste
        saveDefaultConfig();

        // Carica la configurazione
        if (!loadConfig()) {
            logger.severe("Errore nel caricamento della configurazione! Plugin disabilitato.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Registra gli eventi
        getServer().getPluginManager().registerEvents(this, this);

        logger.info("CoarseDirtGuard v" + getDescription().getVersion() + " abilitato!");
        logger.info("Blocchi protetti: " + protectedBlocks.size() + " tipo/i");
        if (debugMode) {
            logger.info("Modalità debug abilitata");
        }
    }

    /**
     * Chiamato quando il plugin viene disabilitato.
     */
    @Override
    public void onDisable() {
        logger.info("CoarseDirtGuard disabilitato!");
    }

    /**
     * Carica (o ricarica) la configurazione dal file config.yml.
     * Valida i nomi dei materiali e gestisce errori.
     * 
     * @return true se il caricamento è riuscito, false altrimenti
     */
    private boolean loadConfig() {
        try {
            reloadConfig();

            // Carica lista blocchi protetti con validazione
            List<String> blockNames = getConfig().getStringList("protected-blocks");
            protectedBlocks = new HashSet<>();

            if (blockNames == null || blockNames.isEmpty()) {
                logger.warning("Lista 'protected-blocks' vuota o non trovata! Usando default: COARSE_DIRT");
                protectedBlocks.add(Material.COARSE_DIRT);
            } else {
                for (String blockName : blockNames) {
                    try {
                        Material material = Material.valueOf(blockName.toUpperCase());
                        protectedBlocks.add(material);
                    } catch (IllegalArgumentException e) {
                        logger.warning("Material non valido ignorato: " + blockName);
                    }
                }

                if (protectedBlocks.isEmpty()) {
                    logger.warning("Nessun blocco valido trovato! Usando default: COARSE_DIRT");
                    protectedBlocks.add(Material.COARSE_DIRT);
                }
            }

            // Carica impostazioni
            logBlockedActions = getConfig().getBoolean("settings.log-blocked-actions", false);
            blockShovel = getConfig().getBoolean("settings.block-shovel", true);
            blockHoe = getConfig().getBoolean("settings.block-hoe", true);
            debugMode = getConfig().getBoolean("settings.debug", false);

            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Errore nel caricamento della configurazione", e);
            return false;
        }
    }

    /**
     * Gestisce il comando /coarsedirtguard.
     * 
     * @param sender  Chi ha eseguito il comando
     * @param command Il comando eseguito
     * @param label   L'alias del comando usato
     * @param args    Gli argomenti del comando
     * @return true se il comando è stato gestito correttamente
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("coarsedirtguard")) {
            return false;
        }

        // Comando: /coarsedirtguard reload
        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("coarsedirtguard.reload")) {
                sender.sendMessage("§c✖ Non hai il permesso per ricaricare il plugin!");
                return true;
            }

            if (loadConfig()) {
                sender.sendMessage("§a✔ Configurazione ricaricata con successo!");
                sender.sendMessage("§7Blocchi protetti: " + protectedBlocks.size());
            } else {
                sender.sendMessage("§c✖ Errore nel ricaricamento della configurazione!");
            }
            return true;
        }

        // Comando: /coarsedirtguard info
        if (args.length > 0 && args[0].equalsIgnoreCase("info")) {
            sender.sendMessage("§6=== CoarseDirtGuard v" + getDescription().getVersion() + " ===");
            sender.sendMessage("§7Blocchi protetti: §f" + protectedBlocks.size());
            sender.sendMessage("§7Blocca pale: §f" + (blockShovel ? "✔" : "✖"));
            sender.sendMessage("§7Blocca zappe: §f" + (blockHoe ? "✔" : "✖"));
            sender.sendMessage("§7Logging: §f" + (logBlockedActions ? "✔" : "✖"));
            return true;
        }

        // Messaggio di aiuto
        sender.sendMessage("§6=== CoarseDirtGuard ===");
        sender.sendMessage("§7/coarsedirtguard reload §f- Ricarica configurazione");
        sender.sendMessage("§7/coarsedirtguard info §f- Mostra informazioni");
        return true;
    }

    /**
     * Gestisce l'evento di interazione del giocatore con i blocchi.
     * Blocca la trasformazione di blocchi protetti quando si usano pale o zappe.
     * 
     * @param event L'evento di interazione del giocatore
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // Controlla se è un click destro su un blocco
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) {
            return;
        }

        // Controlla se il giocatore ha permesso di bypass
        Player player = event.getPlayer();
        if (player.hasPermission("coarsedirtguard.bypass")) {
            if (debugMode) {
                logger.fine(player.getName() + " ha bypassato la protezione (permesso)");
            }
            return;
        }

        // Controlla se il blocco è nella lista dei protetti
        Material blockType = clickedBlock.getType();
        if (!protectedBlocks.contains(blockType)) {
            return;
        }

        // Controlla se il player ha una pala o una zappa in mano
        ItemStack itemInHand = event.getItem();
        if (itemInHand == null) {
            return;
        }

        Material toolType = itemInHand.getType();
        boolean isShovelTool = isShovel(toolType);
        boolean isHoeTool = isHoe(toolType);

        // Verifica permessi specifici per strumento
        if (isShovelTool && player.hasPermission("coarsedirtguard.bypass.shovel")) {
            if (debugMode) {
                logger.fine(player.getName() + " ha bypassato la protezione pale");
            }
            return;
        }

        if (isHoeTool && player.hasPermission("coarsedirtguard.bypass.hoe")) {
            if (debugMode) {
                logger.fine(player.getName() + " ha bypassato la protezione zappe");
            }
            return;
        }

        // Verifica se lo strumento deve essere bloccato in base alle impostazioni
        if ((isShovelTool && !blockShovel) || (isHoeTool && !blockHoe)) {
            return;
        }

        // Se non è né pala né zappa (o entrambi disabilitati), non fare nulla
        if (!isShovelTool && !isHoeTool) {
            return;
        }

        // Blocca l'interazione SILENZIOSAMENTE
        event.setCancelled(true);

        // Log dell'azione bloccata se abilitato
        if (logBlockedActions) {
            String tool = isShovelTool ? "pala" : "zappa";
            logger.info(String.format("Bloccata trasformazione di %s da parte di %s con %s alle coordinate %d, %d, %d",
                    blockType.name(),
                    player.getName(),
                    tool,
                    clickedBlock.getX(),
                    clickedBlock.getY(),
                    clickedBlock.getZ()));
        }

        if (debugMode) {
            logger.fine("Evento bloccato per " + player.getName() + " su " + blockType.name());
        }
    }

    /**
     * Verifica se un materiale è una pala.
     * Usa EnumSet per lookup O(1) invece di confronti multipli.
     * 
     * @param material Il materiale da verificare
     * @return true se è una pala, false altrimenti
     */
    private boolean isShovel(Material material) {
        return SHOVELS.contains(material);
    }

    /**
     * Verifica se un materiale è una zappa.
     * Usa EnumSet per lookup O(1) invece di confronti multipli.
     * 
     * @param material Il materiale da verificare
     * @return true se è una zappa, false altrimenti
     */
    private boolean isHoe(Material material) {
        return HOES.contains(material);
    }
}
