package belven.classes.timedevents;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

public class ItemRestorer extends BukkitRunnable
{
    public Player currentPlayer;
    public Material typeToRestore;

    public ItemRestorer(Player CurrentPlayer, Material TypeToRestore)
    {
        currentPlayer = CurrentPlayer;
        typeToRestore = TypeToRestore;
    }

    @Override
    public void run()
    {
        int positionID;
        ItemStack tempStack = new ItemStack(typeToRestore, 1);
        PlayerInventory playerInventory = currentPlayer.getInventory();
        
        if (!playerInventory.containsAtLeast(tempStack, 1))
        {
            positionID = currentPlayer.getInventory().firstEmpty();

            playerInventory.setItem(positionID, tempStack);
        }
        
        this.cancel();
    }
}
