package belven.classes.timedevents;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import belven.classes.Abilities.ChainLightning;

public class ChainLightningTimer extends BukkitRunnable {
	private Player currentPlayer;
	private int maxDuration;
	private Location locationToStrike;
	private ChainLightning currentChainLightning;

	public ChainLightningTimer(ChainLightning CurrentChainLightning) {
		this.currentChainLightning = CurrentChainLightning;
		currentPlayer = CurrentChainLightning.currentClass.classOwner;
		locationToStrike = currentPlayer.getLocation();
		maxDuration = 24;
	}

	@Override
	public void run() {
		maxDuration--;
		if (maxDuration > 0) {
			currentChainLightning.PerformAbility(locationToStrike);
		} else {
			currentPlayer.sendMessage("Chain Lightning Ended");
			this.cancel();
		}
	}
}
