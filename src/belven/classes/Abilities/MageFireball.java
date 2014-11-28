package belven.classes.Abilities;

import org.bukkit.DyeColor;
import org.bukkit.entity.Fireball;
import org.bukkit.material.Dye;

public class MageFireball extends Ability {
	public MageFireball(belven.classes.RPGClass CurrentClass, int priority, int amp) {
		super(priority, amp);
		currentClass = CurrentClass;

		Dye dye = new Dye();
		dye.setColor(DyeColor.BLUE);
		requirements.add(dye.toItemStack(1));

		abilitiyName = "Mage Fireball";
		shouldBreak = false;
	}

	@Override
	public boolean PerformAbility() {
		currentClass.classOwner.launchProjectile(Fireball.class);
		RemoveItems();
		return true;
	}

	@Override
	public int Amplifier() {
		return Math.round(currentClass.classOwner.getLevel() / 7);
	}
}
