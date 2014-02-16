package belven.timedevents;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import belven.classes.ClassManager;

public class AbilityDelay extends BukkitRunnable
{
    private Player currentPlayer;
    private ClassManager currentPlugin;
    
    public AbilityDelay(Player CurrentPlayer, ClassManager Plugin)
    {
        currentPlayer = CurrentPlayer;
        currentPlugin = Plugin;
        currentPlugin.CurrentPlayerClasses.get(currentPlayer).CanCast = false;
    }

    @Override
    public void run()
    {
        currentPlugin.CurrentPlayerClasses.get(currentPlayer).CanCast = true;
        this.cancel();
    }

}