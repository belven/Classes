package belven.classes.timedevents;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import belven.classes.player.abilities.Barrier;

public class BarrierTimer extends BukkitRunnable {
	private Player currentPlayer;
	private int maxDuration;
	private Barrier currentBarrier;

	public BarrierTimer(Barrier currentBarrier) {
		this.currentBarrier = currentBarrier;
		currentPlayer = currentBarrier.currentClass.getPlayer();
		maxDuration = 30;
	}

	@Override
	public void run() {
		maxDuration--;
		if (maxDuration > 0) {
			currentBarrier.PerformAbility(currentPlayer.getLocation());
		} else {
			currentPlayer.sendMessage("Barrier Ended");
			this.cancel();
		}
	}
}