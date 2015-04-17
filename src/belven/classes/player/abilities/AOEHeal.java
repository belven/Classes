package belven.classes.player.abilities;

import org.bukkit.DyeColor;
import org.bukkit.Location;
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
		setShouldBreak(true);
	}

	@Override
	public boolean PerformAbility(Event e) {
		getRPGClass().setAbilityOnCoolDown(this, true);
		Location location = getRPGClass().getPlayer().getLocation();

		for (Player p : EntityFunctions.getNearbyPlayersNew(location, getAmplifier() + 8)) {
			if (getRPGClass().getPlugin().isAlly(p, getRPGClass().getPlayer())) {
				getRPGClass().getPlayer().sendMessage("You healed " + p.getName());
				new HealTimer(getRPGClass().getPlugin(), getAmplifier() / 100.0, p, 5, 1);
			}
		}

		RemoveItems();
		return true;
	}

	@Override
	public int getAmplifier() {
		return Functions.abilityCap((double) amplifier + 1, getRPGClass().getPlayer().getLevel());
	}
}