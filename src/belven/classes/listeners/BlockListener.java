package belven.classes.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;

import belven.classes.ClassManager;

public class BlockListener implements Listener {
	public ClassManager plugin;

	public BlockListener(ClassManager instance) {
		plugin = instance;
	}

	@EventHandler
	public void onBlockDispenseEvent(BlockDispenseEvent event) {
		Block tempblock = event.getBlock();

		if (tempblock.getType() == Material.DISPENSER) {
			Dispenser tempDispenser = (Dispenser) tempblock.getState();
			tempDispenser.getInventory().addItem(event.getItem().clone());
		}
		return;
	}
}