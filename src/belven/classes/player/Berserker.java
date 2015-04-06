package belven.classes.player;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import belven.classes.ClassManager;
import belven.classes.RPGClass;
import belven.classes.abilities.Ability;
import belven.classes.player.abilities.Grapple;
import belven.resources.ClassDrop;
import belven.resources.EntityFunctions;
import belven.resources.Functions;
import belven.resources.MaterialFunctions;

import com.google.common.base.Function;

public class Berserker extends RPGClass {
	public Grapple classGrapple;

	public Berserker(Player currentPlayer, ClassManager instance) {
		super(30, currentPlayer, instance);
		this.className = "Berserker";
		SetClassDrops();
		SetAbilities();
	}

	@Override
	public void SetClassDrops() {
		ItemStack string = new ItemStack(Material.STRING);
		ItemStack sword = new ItemStack(Material.STONE_SWORD);
		getClassDrops().add(new ClassDrop(string, true, 2, 1));
		getClassDrops().add(new ClassDrop(sword, true, 1, 1));
	}

	@Override
	public void SelfCast(PlayerInteractEvent event, Player currentPlayer) {
		if (MaterialFunctions.isFood(getPlayer().getItemInHand().getType())) {
			return;
		}

		for (Ability a : getAbilities()) {
			if (!a.onCooldown() && a.HasRequirements()) {
				if (!a.PerformAbility(event)) {
					continue;
				} else {
					break;
				}
			}
		}
	}

	@Override
	public void SelfTakenDamage(EntityDamageByEntityEvent event) {
		double healthPercent = getPlugin().GetPlayerE(getPlayer()).GetMissingHealthPercent();

		this.getOwner().addPotionEffect(
				new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Functions.SecondsToTicks(2),
						(int) (2 * healthPercent)));
	}

	@SuppressWarnings("deprecation")
	@Override
	public synchronized void SelfDamageOther(EntityDamageByEntityEvent event) {
		int mobCount = 0;
		DamageCause dc = DamageCause.CUSTOM;

		if (event.getCause() != dc) {
			for (Entity e : EntityFunctions.getNearbyEntities(event.getEntity().getLocation(), 4)) {
				double damageToDo = event.getDamage();

				if (e instanceof LivingEntity && e != event.getEntity() && mobCount < 3 && e != this.getOwner()) {
					LivingEntity le = (LivingEntity) e;
					mobCount++;
					try {
						HashMap<DamageModifier, Double> damage = new HashMap<>();
						damage.put(DamageModifier.BASE, damageToDo);

						Map<DamageModifier, Function<? super Double, Double>> functions = new HashMap<>();

						functions.put(DamageModifier.BASE, com.google.common.base.Functions.constant(damageToDo));

						EntityDamageByEntityEvent ede = new EntityDamageByEntityEvent(getOwner(), le, dc, damage,
								functions);

						Bukkit.getPluginManager().callEvent(ede);
						le.damage(ede.getDamage());
					} catch (Throwable ex) {
						Bukkit.getPluginManager().callEvent(
								new EntityDamageByEntityEvent(getOwner(), le, dc, damageToDo));
					}

				}
			}
		}
	}

	@Override
	public void SetAbilities() {
		if (!AbilitiesSet()) {
			this.classGrapple = new Grapple(this, 1, 0);
			getAbilities().add(classGrapple);
			SortAbilities();
			setAbilitiesSet(true);
		}

	}

	@Override
	public void RightClickEntity(PlayerInteractEntityEvent event, Entity currentEntity) {
		if (MaterialFunctions.isFood(getPlayer().getItemInHand().getType())) {
			return;
		}

		for (Ability a : getAbilities()) {
			if (!a.onCooldown() && a.HasRequirements()) {
				if (!a.PerformAbility(event)) {
					continue;
				} else {
					break;
				}
			}
		}

	}
}
