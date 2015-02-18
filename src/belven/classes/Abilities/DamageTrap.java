package belven.classes.Abilities;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.material.Dye;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import belven.classes.timedevents.DamageTrapTimer;
import belven.resources.Functions;

public class DamageTrap extends Ability {
	public DamageTrap(belven.classes.RPGClass CurrentClass, int priority, int amp) {
		super(priority, amp);
		currentClass = CurrentClass;
		Dye dye = new Dye();
		dye.setColor(DyeColor.GRAY);
		requirements.add(dye.toItemStack(1));

		inHandRequirements.add(Material.INK_SACK);

		abilitiyName = "Damage Trap";
	}

	@Override
	public boolean PerformAbility() {
		Location targetLocation = currentClass.classOwner.getLocation();
		if (targetLocation.getBlock().getType() != Material.WOOL) {
			new DamageTrapTimer(targetLocation.getBlock(), currentClass.classOwner.getLevel(), 6).runTaskTimer(
					currentClass.plugin, Functions.SecondsToTicks(5), Functions.SecondsToTicks(2));

			targetLocation.getBlock().setType(Material.WOOL);
			currentClass.classOwner.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Functions
					.SecondsToTicks(2), 3));
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
		return currentClass.classOwner.getLevel() / 3 + 2;
	}
}
