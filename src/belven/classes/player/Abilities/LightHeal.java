package belven.classes.player.Abilities;

import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.material.Dye;

import belven.classes.Abilities.Ability;
import belven.classes.player.RPGClass;
import belven.classes.timedevents.HealTimer;
import belven.resources.EntityFunctions;
import belven.resources.Functions;

public class LightHeal extends Ability {
	public LightHeal(RPGClass cc, int priority, int amp) {
		super(cc, priority, amp);
		

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

		if (EntityFunctions.isHealthLessThanOther(currentClass.getPlayer(), playerToHeal)) {
			playerToHeal = currentClass.getPlayer();
		}

		new HealTimer(currentClass.plugin, Amplifier() / 100.0, playerToHeal, 5, 1);

		currentClass.getPlayer().sendMessage("You healed " + playerToHeal.getName());

		currentClass.setAbilityOnCoolDown(this);
		RemoveItems();
		return true;
	}

	@Override
	public int Amplifier() {
		return Functions.abilityCap(amplifier + 1, currentClass.getPlayer().getLevel());
	}
}
