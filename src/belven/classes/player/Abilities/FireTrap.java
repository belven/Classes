package belven.classes.player.Abilities;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.material.Dye;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import belven.classes.Abilities.Ability;
import belven.classes.player.RPGClass;
import belven.classes.timedevents.FireTrapTimer;
import belven.resources.Functions;

public class FireTrap extends Ability {
	public FireTrap(RPGClass cc, int priority, int amp) {
		super(cc, priority, amp);
		

		Dye dye = new Dye();
		dye.setColor(DyeColor.RED);
		requirements.add(dye.toItemStack(1));

		inHandRequirements.add(Material.INK_SACK);
		abilitiyName = "Fire Trap";
	}

	@Override
	public boolean PerformAbility() {
		Location targetLocation = currentClass.getPlayer().getLocation();
		if (targetLocation.getBlock().getType() != Material.WOOL) {
			new FireTrapTimer(targetLocation.getBlock(), Functions.SecondsToTicks(Amplifier()), 4).runTaskTimer(
					currentClass.plugin, Functions.SecondsToTicks(5), Functions.SecondsToTicks(2));

			targetLocation.getBlock().setType(Material.WOOL);
			currentClass.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Functions
					.SecondsToTicks(2), 3));

			RemoveItems();
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int Amplifier() {
		return currentClass.getPlayer().getLevel() / 3 + 2;
	}
}
