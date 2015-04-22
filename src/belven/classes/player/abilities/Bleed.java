package belven.classes.player.abilities;

import org.bukkit.DyeColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.material.Dye;

import belven.classes.RPGClass;
import belven.classes.abilities.Ability;
import belven.classes.timedevents.DamageTimer;

public class Bleed extends Ability {
	public Bleed(RPGClass cc, int priority, int amp) {
		super(cc, priority, amp);

		Dye dye = new Dye();
		dye.setColor(DyeColor.RED);
		requirements.add(dye.toItemStack(1));

		abilitiyName = "Bleed";
	}

	@Override
	public boolean PerformAbility(Event e) {
		LivingEntity targetEntity = getRPGClass().getTarget();

		if (targetEntity != null) {
			new DamageTimer(getRPGClass().getPlugin(), 0.5, targetEntity, getRPGClass().getOwner(), getAmplifier(), 1);
			getRPGClass().setAbilityOnCoolDown(this);
			RemoveItems();
		}

		return false;
	}
}
