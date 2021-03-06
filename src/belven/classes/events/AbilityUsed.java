package belven.classes.events;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import belven.classes.RPGClass;
import belven.classes.abilities.Ability;

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

	public LivingEntity GetOwner() {
		return a.getRPGClass().getOwner();
	}

	public RPGClass GetClass() {
		return a.getRPGClass();
	}
}
