package belven.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import belven.classes.Archer;
import belven.classes.ClassManager;
import belven.classes.Healer;
import belven.classes.Mage;

public class BlockListener implements Listener
{

    public ClassManager plugin;

    public BlockListener(ClassManager instance)
    {
        plugin = instance;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event)
    {
        belven.classes.Class currentClass = plugin.CurrentPlayerClasses
                .get(event.getPlayer());
        Material mat = event.getBlock().getType();

        if (mat == Material.LAPIS_BLOCK
                && (currentClass instanceof Mage || currentClass instanceof Healer))
        {
            event.setCancelled(true);
        }
        else if (mat == Material.WOOL && currentClass instanceof Archer)
        {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockDispenseEvent(BlockDispenseEvent event)
    {
        Block tempblock = event.getBlock();

        if (tempblock.getType() == Material.DISPENSER)
        {
            // plugin.getServer().broadcastMessage("Dispenser Used");

            Dispenser tempDispenser = (Dispenser) tempblock.getState();
            // Inventory tempInventory = tempDispenser.getInventory();

            tempDispenser.getInventory().addItem(event.getItem().clone());
        }
        return;
    }

}