package belven.classes.player.abilities;

import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.material.Dye;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import belven.classes.RPGClass;
import belven.classes.abilities.Ability;
import belven.resources.Functions;

public class BolsterHealth extends Ability {
	public BolsterHealth(RPGClass cc, int priority, int amp) {
		super(cc, priority, amp);

		Dye dye = new Dye();
		dye.setColor(DyeColor.BLUE);
		requirements.add(dye.toItemStack(2));

		abilitiyName = "Bolster Health";
	}

	@Override
	public boolean PerformAbility(Event e) {
		Player playerToHeal = getRPGClass().targetPlayer;

		if (playerToHeal == null) {
			return false;
		}

		PotionEffect pe = new PotionEffect(PotionEffectType.HEALTH_BOOST, Functions.SecondsToTicks(Amplifier() + 30),
				Amplifier(), true);

		if (!onCooldown() && !playerToHeal.hasPotionEffect(PotionEffectType.HEALTH_BOOST)) {
			playerToHeal.addPotionEffect(pe, false);

			getRPGClass().getPlayer().sendMessage("You boosted  " + playerToHeal.getName() + "s max health");
			getRPGClass().setAbilityOnCoolDown(this, true);
			RemoveItems();
			return true;
		}
		return false;
	}

	public boolean containsEffect(PotionEffectType pet, Player playerToHeal) {
		for (PotionEffect pe : playerToHeal.getActivePotionEffects()) {
			if (pe.getType().equals(pet)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int Amplifier() {
		return Functions.abilityCap((double) amplifier + 1, getRPGClass().getPlayer().getLevel());
	}
}