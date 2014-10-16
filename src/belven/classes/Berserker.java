package belven.classes;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import resources.EntityFunctions;
import resources.Functions;
import resources.MaterialFunctions;
import belven.classes.Abilities.Ability;
import belven.classes.Abilities.Grapple;
import resources.ClassDrop;

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
		classDrops.add(new ClassDrop(string, true, 2, 1));
		classDrops.add(new ClassDrop(sword, true, 1, 1));
	}

	@Override
	public void SelfCast(Player currentPlayer) {
		if (MaterialFunctions.isFood(classOwner.getItemInHand().getType())) {
			return;
		}

		for (Ability a : Abilities) {
			if (!a.onCooldown && a.HasRequirements(classOwner)) {
				if (!a.PerformAbility()) {
					continue;
				} else {
					break;
				}
			}
		}
	}

	@Override
	public void RightClickEntity(Entity currentEntity) {
		if (MaterialFunctions.isFood(classOwner.getItemInHand().getType())) {
			return;
		}

		for (Ability a : Abilities) {
			if (!a.onCooldown && a.HasRequirements(classOwner)) {
				if (!a.PerformAbility()) {
					continue;
				} else {
					break;
				}
			}
		}
	}

	@Override
	public void SelfTakenDamage(EntityDamageByEntityEvent event) {
		double healthPercent = plugin.GetPlayerE(classOwner).GetMissingHealthPercent();

		this.classOwner.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Functions.SecondsToTicks(2),
				(int) (2 * healthPercent)));
	}

	@Override
	public void SelfDamageOther(EntityDamageByEntityEvent event) {
		if (plugin.GetPlayerE(classOwner).MeleeWeaponInHand()) {
			event.setDamage(event.getDamage() + 2);
		}

		int mobCount = 0;
		for (Entity e : EntityFunctions.getNearbyEntities(event.getEntity().getLocation(), 4)) {
			double damageToDo = event.getDamage() / 3.0D;

			if (e instanceof LivingEntity) {
				mobCount++;

				if (mobCount >= 3) {
					break;
				}

				LivingEntity le = (LivingEntity) e;

				if (le == this.classOwner && !EntityFunctions.IsAMob(event.getEntityType())) {
					if (plugin.GetPlayerE(classOwner).GetHealthPercent() > 0.2D) {
						le.damage(damageToDo);
					}
				} else if (le.getType() != EntityType.PLAYER) {
					// HashMap<DamageModifier, Double> damageModifiers = new
					// HashMap<DamageModifier, Double>();
					// damageModifiers.put(DamageModifier.BASE, damageToDo);
					// EntityDamageByEntityEvent ede = new
					// EntityDamageByEntityEvent(
					// classOwner, le, DamageCause.ENTITY_ATTACK,
					// damageModifiers, null);
					// le.setLastDamageCause(ede);

					le.damage(damageToDo);

				}
			}
		}

	}

	@Override
	public void SetAbilities() {
		this.classGrapple = new Grapple(this, 1, 0);
		Abilities.add(classGrapple);
		SortAbilities();

	}
}
