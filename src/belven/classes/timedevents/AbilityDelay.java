package belven.classes.timedevents;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import belven.classes.ClassManager;

public class AbilityDelay extends BukkitRunnable {
	private Player currentPlayer;
	private ClassManager plugin;

	public AbilityDelay(Player CurrentPlayer, ClassManager Plugin) {
		currentPlayer = CurrentPlayer;
		plugin = Plugin;
		plugin.GetClass(currentPlayer).CanCast = false;
	}

	@Override
	public void run() {
		plugin.GetClass(currentPlayer).CanCast = true;
		this.cancel();
	}
}