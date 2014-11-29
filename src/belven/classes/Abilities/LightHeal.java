package belven.classes.Abilities;

import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.material.Dye;

import resources.EntityFunctions;
import resources.Functions;
import belven.classes.timedevents.HealTimer;

public class LightHeal extends Ability {
	public LightHeal(belven.classes.RPGClass CurrentClass, int priority, int amp) {
		super(priority, amp);
		currentClass = CurrentClass;

		Dye dye = new Dye();
		dye.setColor(DyeColor.BLUE);
		requirements.add(dye.toItemStack(1));

		abilitiyName = "Light Heal";
		shouldBreak = false;
	}

	@Override
	public synchronized boolean PerformAbility() {
		Player playerToHeal = currentClass.targetPlayer;

		if (playerToHeal == null || onCooldown) {
			return false;
		}

		if (EntityFunctions.isHealthLessThanOther(currentClass.classOwner, playerToHeal)) {
			playerToHeal = currentClass.classOwner;
		}

		new HealTimer(currentClass.plugin, Amplifier() / 100.0, playerToHeal, 5, 1);

		currentClass.classOwner.sendMessage(abilitiyName + ": You healed " + playerToHeal.getName());

		currentClass.setAbilityOnCoolDown(this);
		RemoveItems();
		return true;
	}

	@Override
	public int Amplifier() {
		return Functions.abilityCap(Amplifier + 1, currentClass.classOwner.getLevel());
	}
}
