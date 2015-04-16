package belven.classes.player.abilities;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;

import belven.classes.RPGClass;
import belven.classes.abilities.Ability;
import belven.resources.EntityFunctions;

public class Retaliation extends Ability {
	public Retaliation(RPGClass cc, int priority, int amp) {
		super(cc, priority, amp);
		abilitiyName = "Retaliation";
	}

	@Override
	public boolean PerformAbility(Event e) {
		Player p = getRPGClass().getPlayer();
		EntityDamageEvent ldc = p.getLastDamageCause();
		LivingEntity entityDamaged = EntityFunctions.GetDamager(p);

		if (entityDamaged != null) {
			entityDamaged.damage(ldc.getDamage() * getAmplifier(), getRPGClass().getPlayer());
		}

		getRPGClass().setAbilityOnCoolDown(this);
		return true;
	}

	@Override
	public int getAmplifier() {
		return amplifier;
	}
}