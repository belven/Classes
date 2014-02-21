package belven.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import belven.classes.ClassManager;
import belven.events.ArenaBlockActivatedEvent;

public class ArenaListener implements Listener
{
    public ClassManager plugin;

    public ArenaListener(ClassManager instance)
    {
        plugin = instance;
    }

    @EventHandler
    public void onArenaBlockActivatedEvent(ArenaBlockActivatedEvent event)
    {
        if (plugin.currentArenaBlocks.size() > 0)
        {
            for (int i = 0; i < plugin.currentArenaBlocks.size(); i++)
            {
                
                if (event.GetBlockLocation().equals(
                        plugin.currentArenaBlocks.get(i).blockToActivate
                                .getLocation())
                        && !plugin.currentArenaBlocks.get(i).isActive)
                {
                    plugin.currentArenaBlocks.get(i).Activate();
                }
            }
        }
    }
}
