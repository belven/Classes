package belven.classes;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import belven.classes.Abilities.Ability;
import belven.resources.Functions;

public class Knight extends Warrior {
	public Knight(Player currentPlayer, ClassManager instance) {
		super(currentPlayer, instance);
		className = "Knight";
		baseClassName = "Warrior";
		SortAbilities();
		SetClassDrops();
	}

	@Override
	public void SelfDamageOther(EntityDamageByEntityEvent event) {
		if (classOwner.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
			classOwner.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
		}
	}

	@Override
	public void SelfTakenDamage(EntityDamageByEntityEvent event) {
		super.SelfTakenDamage(event);
	}

	@Override
	public void AbilityUsed(Ability ability) {
		if (ability == currentRetaliation) {
			classOwner.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Functions.SecondsToTicks(10),
					Functions.abilityCap(2, classOwner.getLevel())), true);
		}
	}

	public int Amplifier() {
		return Math.round(classOwner.getLevel() / 5) + 1;
	}

	@Override
	public void SetClassDrops() {
	}
}
