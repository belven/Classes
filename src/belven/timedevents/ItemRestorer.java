package belven.timedevents;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

public class ItemRestorer extends BukkitRunnable
{
    public Player currentPlayer;

    public ItemRestorer(Player CurrentPlayer)
    {
        currentPlayer = CurrentPlayer;
    }

    @Override
    public void run()
    {
        int positionID;
        ItemStack tempStack = new ItemStack(Material.NETHER_STAR, 1);
        PlayerInventory playerInventory = currentPlayer.getInventory();
        
        if (!playerInventory.containsAtLeast(tempStack, 1))
        {
            positionID = currentPlayer.getInventory().firstEmpty();

            playerInventory.setItem(positionID, tempStack);
        }
        
        this.cancel();
    }
}
