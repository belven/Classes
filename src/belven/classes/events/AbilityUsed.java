package belven.classes.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import belven.classes.Abilities.Ability;

public class AbilityUsed extends Event {
	private static final HandlerList handlers = new HandlerList();

	private Ability a;

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public AbilityUsed(Ability ability) {
		a = ability;
	}

	public Ability GetAbility() {
		return a;
	}

	public Player GetPlayer() {
		return a.currentClass.classOwner;
	}

	public belven.classes.RPGClass GetClass() {
		return a.currentClass;
	}
}
