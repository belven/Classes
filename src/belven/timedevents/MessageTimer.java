package belven.timedevents;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class MessageTimer extends BukkitRunnable
{
    private List<Player> players = new ArrayList<Player>();
    private String messageToSend;

    public MessageTimer(List<Player> Players, String MessageToSend)
    {
        players = Players;
        messageToSend = MessageToSend;
    }

    @Override
    public void run()
    {
        for (int i = 0; i < players.size(); i++)
        {
            if (players.get(i) != null)
            {
                players.get(i).sendMessage(messageToSend);
            }
        }
    }
}
