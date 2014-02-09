package belven.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDispenseEvent;

import belven.classes.ClassManager;

public class BlockListener implements Listener {

	public ClassManager plugin;

	public BlockListener(ClassManager instance)
	{
		plugin = instance;
	}

	// @EventHandler
	// public void onBlockPlace(BlockPlaceEvent event) {
	// Player player = event.getPlayer();
	// Block block = event.getBlock();
	// Material mat = block.getType();
	//
	// player.sendMessage("You placed a block with ID : " + mat);// Display a
	// // message
	// }

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