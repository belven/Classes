package belven.classes.player;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import belven.classes.ClassManager;
import belven.classes.abilities.Ability;
import belven.resources.Functions;

public class Knight extends Warrior {
	public Knight(Player currentPlayer, ClassManager instance) {
		super(currentPlayer, instance);
		className = "Knight";
		baseClassName = "Warrior";
	}

	@Override
	public void SelfDamageOther(EntityDamageByEntityEvent event) {
		if (getOwner().hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
			getOwner().removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
		}
	}

	@Override
	public void SelfTakenDamage(EntityDamageByEntityEvent event) {
		super.SelfTakenDamage(event);
	}

	@Override
	public void AbilityUsed(Ability ability) {
		if (ability == currentRetaliation) {
			getOwner().addPotionEffect(
					new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Functions.SecondsToTicks(10),
							Functions.abilityCap(2, getPlayer().getLevel())), true);
		}
	}

	public int Amplifier() {
		return Math.round(getPlayer().getLevel() / 5) + 1;
	}

	@Override
	public void SetClassDrops() {
	}
}
