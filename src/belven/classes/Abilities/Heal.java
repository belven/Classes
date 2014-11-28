package belven.classes.Abilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import resources.Functions;
import belven.classes.timedevents.HealTimer;

public class Heal extends Ability {
	public Heal(belven.classes.RPGClass CurrentClass, int priority, int amp) {
		super(priority, amp);
		currentClass = CurrentClass;
		requirements.add(new ItemStack(Material.LAPIS_BLOCK, 1));
		abilitiyName = "Heal";
		Cooldown = 10;
	}

	@Override
	public boolean PerformAbility() {
		Player playerToHeal = currentClass.getTargetPlayer(30, currentClass.classOwner);

		if (playerToHeal == null) {
			return false;
		}

		if (currentClass.plugin.GetPlayerE(playerToHeal).GetHealthPercent() <= 0.3) {
			// playerToHeal.addPotionEffect(new
			// PotionEffect(PotionEffectType.HEAL, 1, Amplifier()));

			new HealTimer(currentClass.plugin, 0.4, playerToHeal);

			currentClass.classOwner.sendMessage("You healed " + playerToHeal.getName());

			currentClass.setAbilityOnCoolDown(this, true);
			RemoveItems();
			return true;
		}
		return false;
	}

	@Override
	public int Amplifier() {
		return Functions.abilityCap(Amplifier, currentClass.classOwner.getLevel()) + 1;
	}
}