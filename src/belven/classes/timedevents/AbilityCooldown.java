package belven.classes.timedevents;

import org.bukkit.scheduler.BukkitRunnable;

import belven.classes.abilities.Ability;

public class AbilityCooldown extends BukkitRunnable {
	private Ability currentAbility;
	private boolean sendMessage;

	public AbilityCooldown(Ability CurrentAbility, boolean SendMessage) {
		currentAbility = CurrentAbility;
		sendMessage = SendMessage;
	}

	public AbilityCooldown(Ability CurrentAbility) {
		this(CurrentAbility, false);
	}

	@Override
	public void run() {
		if (sendMessage) {
			currentAbility.getRPGClass().getOwner().sendMessage(currentAbility.GetAbilityName() + " is now availble.");
		}
		currentAbility.setOnCooldown(false);
		this.cancel();
	}
}
