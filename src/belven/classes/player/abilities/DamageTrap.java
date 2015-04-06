package belven.classes.player.abilities;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.material.Dye;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import belven.classes.RPGClass;
import belven.classes.abilities.Ability;
import belven.classes.timedevents.DamageTrapTimer;
import belven.resources.Functions;

public class DamageTrap extends Ability {
	public DamageTrap(RPGClass cc, int priority, int amp) {
		super(cc, priority, amp);

		Dye dye = new Dye();
		dye.setColor(DyeColor.GRAY);
		requirements.add(dye.toItemStack(1));

		inHandRequirements.add(Material.INK_SACK);

		abilitiyName = "Damage Trap";
	}

	@Override
	public boolean PerformAbility(Event e) {
		Location targetLocation = getRPGClass().getPlayer().getLocation();
		if (targetLocation.getBlock().getType() != Material.WOOL) {
			new DamageTrapTimer(targetLocation.getBlock(), getRPGClass().getPlayer().getLevel(), 6).runTaskTimer(
					getRPGClass().getPlugin(), Functions.SecondsToTicks(5), Functions.SecondsToTicks(2));

			targetLocation.getBlock().setType(Material.WOOL);
			getRPGClass().getPlayer().addPotionEffect(
					new PotionEffect(PotionEffectType.SPEED, Functions.SecondsToTicks(2), 3));
			RemoveItems();
			return true;
		} else {
			return false;
		}
		// Wool tempWool = (Wool) targetLocation.getBlock().getState();
		// tempWool.setColor(DyeColor.RED);
	}

	@Override
	public int Amplifier() {
		return getRPGClass().getPlayer().getLevel() / 3 + 2;
	}
}
