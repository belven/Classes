package belven.classes;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerListener implements Listener
{
    private final ClassManager plugin;

    public PlayerListener(ClassManager instance)
    {
        plugin = instance;
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event)
    {
        PerformClassAbility(event);
    }

    @EventHandler
    public void onPlayerLoginEvent(PlayerLoginEvent event)
    {
        plugin.AddClassToPlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event)
    {

        PerformClassAbility(event);
    }

    public void PerformClassAbility(PlayerInteractEntityEvent event)
    {
        Player currentPlayer = event.getPlayer();
        Entity currentEntity = event.getRightClicked();

        StringToClass(plugin.CurrentClasses.get(currentPlayer), currentPlayer,
                currentEntity);

    }

    public void StringToClass(String className, Player player,
            Entity currentEntity)
    {

        plugin.CurrentPlayerClasses.get(player).PerformAbility(currentEntity);
        
        // Healer currentHealer = new Healer(player, plugin);
        // Mage currentMage = new Mage(player, plugin);
        //
        // switch (className.toLowerCase())
        // {
        // case "healer":
        // currentHealer.PerformAbility(currentEntity);
        // case "mage":
        // currentMage.PerformAbility(currentEntity);
        // default:
        // return;
        // }
    }

    public void StringToClass(String className, Player player)
    {
        // Healer currentHealer = new Healer(player, plugin);
        // Mage currentMage = new Mage(player, plugin);
        plugin.CurrentPlayerClasses.get(player).PerformAbility(player);

        // switch (className.toLowerCase())
        // {
        // case "healer":
        // currentHealer.PerformAbility(player);
        // case "mage":
        // currentMage.PerformAbility(player);
        // default:
        // return;
        // }
    }

    public void PerformClassAbility(PlayerInteractEvent event)
    {
        Player currentPlayer = event.getPlayer();

        // plugin.getServer().broadcastMessage("PerformClassAbility Air/Block");

        StringToClass(plugin.CurrentClasses.get(currentPlayer), currentPlayer);
    }

    public ClassManager getPlugin()
    {
        return plugin;
    }
}