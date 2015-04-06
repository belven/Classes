package belven.classes.player;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
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
		double healthPercent = plugin.GetPlayerE(getPlayer()).GetMissingHealthPercent();

		this.getOwner().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Functions.SecondsToTicks(2),
				(int) (2 * healthPercent)));
	}

	@SuppressWarnings("deprecation")
	@Override
	public void SelfDamageOther(EntityDamageByEntityEvent event) {
		int mobCount = 0;
		for (Entity e : EntityFunctions.getNearbyEntities(event.getEntity().getLocation(), 4)) {
			double damageToDo = event.getDamage() / 3.0D;

			if (e instanceof LivingEntity) {

				if (mobCount >= 3) {
					break;
				}

				LivingEntity le = (LivingEntity) e;

				if (le != this.getOwner()) {
					le.damage(damageToDo);
					mobCount++;
				}

				// HashMap<DamageModifier, Double> damageModifiers = new HashMap<DamageModifier, Double>();
				// damageModifiers.put(DamageModifier.BASE, damageToDo);

				// Map<DamageModifier, ? extends Function<? super Double, Double>> test = null;

				EntityDamageByEntityEvent ede = new EntityDamageByEntityEvent(getOwner(), le,
						DamageCause.ENTITY_ATTACK, damageToDo);

				le.setLastDamageCause(ede);
			}
		}

	}

	@Override
	public void SetAbilities() {
		if (!abilitiesSet) {
			this.classGrapple = new Grapple(this, 1, 0);
			getAbilities().add(classGrapple);
			SortAbilities();
			abilitiesSet = true;
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
