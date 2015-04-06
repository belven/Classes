package belven.classes.player.abilities;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import belven.classes.RPGClass;
import belven.classes.abilities.Ability;

public class SoulDrain extends Ability {
	public SoulDrain(RPGClass cc, int priority, int amp) {
		super(cc, priority, amp);

		abilitiyName = "Soul Drain";
	}

	@Override
	public boolean PerformAbility(Event e) {
		LivingEntity targetEntity = getRPGClass().getTarget(30, getRPGClass().getPlayer());

		if (targetEntity == null) {
			return false;
		}

		if (getRPGClass().getPlayer().getItemInHand().getType() == Material.NETHER_STAR) {
			targetEntity.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, Amplifier()));
			return true;
		}
		return false;
	}

	@Override
	public int Amplifier() {
		return getRPGClass().getPlayer().getLevel() / 4;
	}
}
