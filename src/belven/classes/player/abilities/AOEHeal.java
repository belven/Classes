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

public class AOEHeal extends Ability {
	public AOEHeal(RPGClass cc, int priority, int amp) {
		super(cc, priority, amp);

		Dye dye = new Dye();
		dye.setColor(DyeColor.BLUE);
		requirements.add(dye.toItemStack(1));

		abilitiyName = "AOE Heal";
	}

	@Override
	public boolean PerformAbility(Event e) {
		for (Player p : EntityFunctions.getNearbyPlayersNew(getRPGClass().getPlayer().getLocation(), Amplifier() + 8)) {
			if (getRPGClass().plugin.isAlly(p, getRPGClass().getPlayer())) {
				new HealTimer(getRPGClass().plugin, Amplifier() / 100.0, p, 5, 1);
			}
		}

		getRPGClass().setAbilityOnCoolDown(this, true);

		RemoveItems();
		return true;
	}

	@Override
	public int Amplifier() {
		return Functions.abilityCap((double) amplifier + 1, getRPGClass().getPlayer().getLevel());
	}
}