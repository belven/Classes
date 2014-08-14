package belven.classes.Abilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import resources.Functions;

public class Heal extends Ability {
	public Heal(belven.classes.RPGClass CurrentClass, int priority, int amp) {
		super(priority, amp);
		currentClass = CurrentClass;
		requirements.add(new ItemStack(Material.LAPIS_BLOCK, 1));
		abilitiyName = "Heal";
		Cooldown = 10;
	}

	@Override
	public boolean PerformAbility(Player playerToHeal) {
		if (currentClass.plugin.GetPlayerE(playerToHeal).GetHealth() <= 10) {
			playerToHeal.addPotionEffect(new PotionEffect(
					PotionEffectType.HEAL, 1, Amplifier()));

			currentClass.classOwner.sendMessage("You healed "
					+ playerToHeal.getName());

			currentClass.setAbilityOnCoolDown(this, true);
			RemoveItems();
			return true;
		}
		return false;
	}

	public int Amplifier() {
		return Functions.abilityCap((double) Amplifier,
				(double) currentClass.classOwner.getLevel()) + 1;
	}
}