package belven.classes.player.abilities;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;

import belven.classes.RPGClass;
import belven.classes.abilities.Ability;
import belven.classes.timedevents.DamageTimer;
import belven.classes.timedevents.HealTimer;

public class SoulDrain extends Ability {
	public SoulDrain(RPGClass cc, int priority, int amp) {
		super(cc, priority, amp);

		abilitiyName = "Soul Drain";
	}

	@Override
	public boolean PerformAbility(Event e) {
		LivingEntity targetEntity = getRPGClass().getTarget();

		if (targetEntity != null) {
			new HealTimer(getRPGClass().getPlugin(), 0.20, getRPGClass().getOwner(), 5, 1);
			new DamageTimer(getRPGClass().getPlugin(), 0.10, targetEntity, getRPGClass().getOwner(), 5, 1);
			getRPGClass().setAbilityOnCoolDown(this, true);
			return true;
		}

		return false;
	}

	@Override
	public int getAmplifier() {
		return getRPGClass().getPlayer().getLevel() / 4;
	}
}
