package belven.classes.player.abilities;

import org.bukkit.DyeColor;
import org.bukkit.entity.Fireball;
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
		shouldBreak = false;
	}

	@Override
	public boolean PerformAbility() {
		currentClass.getPlayer().launchProjectile(Fireball.class);
		RemoveItems();
		return true;
	}

	@Override
	public int Amplifier() {
		return Math.round(currentClass.getPlayer().getLevel() / 7);
	}
}
