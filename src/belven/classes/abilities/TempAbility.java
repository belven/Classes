package belven.classes.abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.Event;

import belven.classes.RPGClass;

public class TempAbility {

	public List<Event> events = new ArrayList<>();

	public RPGClass currentClass;
	protected String abilitiyName = "";

	public boolean onCooldown = false;
	private boolean shouldBreak = false;

	public int priority = 0;
	public int amplifier = 5;
	public int cooldown = 1;

	public TempAbility(RPGClass cc, int Priority, int amplifier, List<Event> events) {
		this.priority = Priority;
		this.amplifier = amplifier;
		this.currentClass = cc;
		this.events = events;
	}

	public boolean PerformAbility(Event e) {
		List<TempAbility> abilities = new ArrayList<>();

		for (TempAbility a : abilities) {
			if (!a.onCooldown && a.contains(e)) {
				if (!a.PerformAbility(e)) {
					continue;
				} else if (a.shouldBreak) {
					break;
				}
			}
		}
		return false;
	}

	private boolean contains(Event e) {
		return events.contains(e);
	}

	public String GetAbilityName() {
		return abilitiyName;
	}
}
