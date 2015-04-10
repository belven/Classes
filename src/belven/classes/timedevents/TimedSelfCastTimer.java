package belven.classes.timedevents;

import org.bukkit.scheduler.BukkitRunnable;

import belven.classes.ClassManager;
import belven.classes.mob.MobClass;

public class TimedSelfCastTimer extends BukkitRunnable {
	private MobClass currentClass;

	public TimedSelfCastTimer(MobClass currentClass, ClassManager Plugin) {
		this.currentClass = currentClass;
	}

	@Override
	public void run() {
		if (currentClass != null && currentClass.getOwner() != null && !currentClass.getOwner().isDead()) {
			currentClass.TimedSelfCast();
		} else {
			this.cancel();
		}
	}
}