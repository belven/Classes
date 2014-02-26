package belven.classes.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClassChangeEvent extends Event
{
    private static final HandlerList handlers = new HandlerList();

    private belven.classes.Class classChagedTo;
    private Player playerThatChanged;

    @Override
    public HandlerList getHandlers()
    {
        return handlers;
    }

    public ClassChangeEvent(belven.classes.Class classChagedTo,
            Player playerThatChanged)
    {
        this.classChagedTo = classChagedTo;
        this.playerThatChanged = playerThatChanged;
    }

    public Player GetPlayerInvolved()
    {
        return playerThatChanged;
    }

    public belven.classes.Class GetNewClass()
    {
        return classChagedTo;
    }
}
