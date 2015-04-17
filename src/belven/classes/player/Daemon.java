package belven.classes.player;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import belven.classes.ClassManager;
import belven.classes.player.abilities.FeelTheBurn;
import belven.classes.player.abilities.SetAlight;
import belven.resources.ClassDrop;
import belven.resources.EntityFunctions;
import belven.resources.Functions;

public class Daemon extends Berserker {
	public FeelTheBurn classFeelTheBurn;
	public SetAlight classSetAlight;

	public Daemon(Player currentPlayer, ClassManager instance) {
		super(currentPlayer, instance);
		this.className = "Daemon";
		baseClassName = "Berserker";
	}

	@Override
	public void SelfDamageOther(EntityDamageByEntityEvent event) {
		super.SelfDamageOther(event);

		for (Entity e : EntityFunctions.getNearbyEntities(event.getEntity().getLocation(), 4)) {
			e.setFireTicks(getOwner().getFireTicks());
		}
	}

	@Override
	public void SelfTakenDamage(EntityDamageByEntityEvent event) {
		super.SelfTakenDamage(event);
		if (!classSetAlight.onCooldown() && classSetAlight.HasRequirements()) {
			classSetAlight.PerformAbility(event);
		}
	}

	@Override
	public void SelfTakenDamage(EntityDamageEvent event) {
		double healthPercent = getPlugin().GetPlayerE(getPlayer()).GetHealthPercent();

		if (event.getCause() == DamageCause.FIRE_TICK || event.getCause() == DamageCause.FIRE) {

			if (getOwner().hasPotionEffect(PotionEffectType.HUNGER)) {
				getOwner().removePotionEffect(PotionEffectType.HUNGER);
			}

			if (healthPercent <= 0.25) {
				event.setDamage(0.0);
			} else if (!classFeelTheBurn.onCooldown() && classFeelTheBurn.HasRequirements()) {
				event.setDamage(0.0);
				classFeelTheBurn.PerformAbility(event);
			}

			getOwner().addPotionEffect(
					new PotionEffect(PotionEffectType.SPEED, Functions.SecondsToTicks(Amplifier()), 3));
		}
	}

	public int Amplifier() {
		return Functions.abilityCap(5, getPlayer().getLevel()) + 1;
	}

	@Override
	public void SetAbilities() {
		super.SetAbilities();
		AddAbility(classFeelTheBurn = new FeelTheBurn(this, 1, 0), 2);
		AddAbility(classSetAlight = new SetAlight(this, 2, 0), 1);
		SortAbilities();
	}

	@Override
	public void SetClassDrops() {
		super.SetClassDrops();
		ItemStack fire = new ItemStack(Material.FIREWORK_CHARGE, 2);
		AddChanceToDrop(new ClassDrop(fire, 10, 1), 1);
	}
}