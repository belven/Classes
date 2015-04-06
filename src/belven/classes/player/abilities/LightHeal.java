package belven.classes.player.abilities;

import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.material.Dye;

import belven.classes.RPGClass;
import belven.classes.abilities.Ability;
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
		setShouldBreak(false);
	}

	@Override
	public synchronized boolean PerformAbility(Event e) {
		Player playerToHeal = getRPGClass().targetPlayer;

		if (playerToHeal == null || onCooldown()) {
			return false;
		}

		if (EntityFunctions.isHealthLessThanOther(getRPGClass().getPlayer(), playerToHeal)) {
			playerToHeal = getRPGClass().getPlayer();
		}

		new HealTimer(getRPGClass().plugin, Amplifier() / 100.0, playerToHeal, 5, 1);

		getRPGClass().getPlayer().sendMessage("You healed " + playerToHeal.getName());

		getRPGClass().setAbilityOnCoolDown(this);
		RemoveItems();
		return true;
	}

	@Override
	public int Amplifier() {
		return Functions.abilityCap(amplifier + 1, getRPGClass().getPlayer().getLevel());
	}
}
