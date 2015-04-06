package belven.classes.player.Abilities;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import belven.classes.Abilities.Ability;
import belven.classes.player.RPGClass;

public class SoulDrain extends Ability {
	public SoulDrain(RPGClass cc, int priority, int amp) {
		super(cc, priority, amp);

		abilitiyName = "Soul Drain";
	}

	@Override
	public boolean PerformAbility() {
		LivingEntity targetEntity = currentClass.getTarget(30, currentClass.getPlayer());

		if (targetEntity == null) {
			return false;
		}

		if (currentClass.getPlayer().getItemInHand().getType() == Material.NETHER_STAR) {
			targetEntity.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, Amplifier()));
			return true;
		}
		return false;
	}

	@Override
	public int Amplifier() {
		return currentClass.getPlayer().getLevel() / 4;
	}
}
