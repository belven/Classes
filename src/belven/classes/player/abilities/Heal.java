package belven.classes.player.abilities;

import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.material.Dye;

import belven.classes.RPGClass;
import belven.classes.abilities.Ability;
import belven.classes.timedevents.HealTimer;
import belven.resources.Functions;

public class Heal extends Ability {
	public Heal(RPGClass cc, int priority, int amp) {
		super(cc, priority, amp);

		Dye dye = new Dye();
		dye.setColor(DyeColor.BLUE);
		requirements.add(dye.toItemStack(1));

		abilitiyName = "Heal";
		cooldown = 10;
	}

	@Override
	public boolean PerformAbility(Event e) {
		Player playerToHeal = getRPGClass().targetPlayer;

		if (playerToHeal == null) {
			return false;
		}

		if (getRPGClass().plugin.GetPlayerE(playerToHeal).GetHealthPercent() <= 0.3) {
			new HealTimer(getRPGClass().plugin, 0.4, playerToHeal);

			getRPGClass().getPlayer().sendMessage("You healed " + playerToHeal.getName());

			getRPGClass().setAbilityOnCoolDown(this, true);
			RemoveItems();
			return true;
		}
		return false;
	}

	@Override
	public int Amplifier() {
		return Functions.abilityCap(amplifier, getRPGClass().getPlayer().getLevel()) + 1;
	}
}