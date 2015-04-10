package belven.classes.player.abilities;

import org.bukkit.DyeColor;
import org.bukkit.entity.Fireball;
import org.bukkit.event.Event;
import org.bukkit.material.Dye;

import belven.classes.RPGClass;
import belven.classes.abilities.Ability;

public class MageFireball extends Ability {
	public MageFireball(RPGClass cc, int priority, int amp) {
		super(cc, priority, amp);

		Dye dye = new Dye();
		dye.setColor(DyeColor.BLUE);
		requirements.add(dye.toItemStack(1));

		abilitiyName = "Mage Fireball";
		setShouldBreak(false);
	}

	@Override
	public boolean PerformAbility(Event e) {
		getRPGClass().getPlayer().launchProjectile(Fireball.class);
		RemoveItems();
		return true;
	}

	@Override
	public int getAmplifier() {
		return Math.round(getRPGClass().getPlayer().getLevel() / 7);
	}
}
