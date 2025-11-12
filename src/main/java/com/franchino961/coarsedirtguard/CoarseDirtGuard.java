package com.franchino961.coarsedirtguard;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.logging.Logger;

public class CoarseDirtGuard extends JavaPlugin implements Listener {

    private Logger logger;
    private List<String> protectedBlocks;
    private boolean logBlockedActions;
    private boolean blockShovel;
    private boolean blockHoe;

    @Override
    public void onEnable() {
        this.logger = getLogger();
        
        // Salva la configurazione predefinita se non esiste
        saveDefaultConfig();
        
        // Carica la configurazione
        loadConfig();
        
        // Registra gli eventi
        getServer().getPluginManager().registerEvents(this, this);
        
        logger.info("CoarseDirtGuard v" + getDescription().getVersion() + " abilitato!");
        logger.info("Blocchi protetti: " + protectedBlocks.toString());
    }

    @Override
    public void onDisable() {
        logger.info("CoarseDirtGuard disabilitato!");
    }

    private void loadConfig() {
        reloadConfig();
        protectedBlocks = getConfig().getStringList("protected-blocks");
        logBlockedActions = getConfig().getBoolean("settings.log-blocked-actions", false);
        blockShovel = getConfig().getBoolean("settings.block-shovel", true);
        blockHoe = getConfig().getBoolean("settings.block-hoe", true);
    }

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

        // Controlla se il blocco è nella lista dei protetti
        String blockType = clickedBlock.getType().name();
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

        // Log dell'azione bloccata se abilitato (opzionale)
        if (logBlockedActions) {
            logger.info("Bloccata trasformazione di " + blockType + " da parte di " + event.getPlayer().getName() + 
                       " alle coordinate " + clickedBlock.getX() + ", " + clickedBlock.getY() + ", " + clickedBlock.getZ());
        }
    }

    private boolean isShovel(Material material) {
        return material == Material.WOODEN_SHOVEL ||
               material == Material.STONE_SHOVEL ||
               material == Material.IRON_SHOVEL ||
               material == Material.GOLDEN_SHOVEL ||
               material == Material.DIAMOND_SHOVEL ||
               material == Material.NETHERITE_SHOVEL;
    }

    private boolean isHoe(Material material) {
        return material == Material.WOODEN_HOE ||
               material == Material.STONE_HOE ||
               material == Material.IRON_HOE ||
               material == Material.GOLDEN_HOE ||
               material == Material.DIAMOND_HOE ||
               material == Material.NETHERITE_HOE;
    }
}
