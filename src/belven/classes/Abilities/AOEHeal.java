package belven.classes.Abilities;

import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.material.Dye;

import resources.EntityFunctions;
import resources.Functions;
import belven.classes.timedevents.HealTimer;

public class AOEHeal extends Ability {
	public AOEHeal(belven.classes.RPGClass CurrentClass, int priority, int amp) {
		super(priority, amp);
		currentClass = CurrentClass;

		Dye dye = new Dye();
		dye.setColor(DyeColor.BLUE);
		requirements.add(dye.toItemStack(2));

		abilitiyName = "AOE Heal";
	}

	@Override
	public boolean PerformAbility() {
		for (Player p : EntityFunctions.getNearbyPlayersNew(currentClass.classOwner.getLocation(), Amplifier() + 8)) {
			if (currentClass.plugin.isAlly(p, currentClass.classOwner)) {
				new HealTimer(currentClass.plugin, Amplifier() / 100.0, p, 5, 1);
			}
		}

		currentClass.setAbilityOnCoolDown(this, true);

		RemoveItems();
		return true;
	}

	@Override
	public int Amplifier() {
		return Functions.abilityCap((double) Amplifier + 1, currentClass.classOwner.getLevel());
	}
}