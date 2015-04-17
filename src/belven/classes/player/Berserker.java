package belven.classes.player;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import belven.classes.ClassManager;
import belven.classes.RPGClass;
import belven.classes.abilities.Ability;
import belven.classes.player.abilities.Cleave;
import belven.classes.player.abilities.Grapple;
import belven.resources.ClassDrop;
import belven.resources.Functions;
import belven.resources.MaterialFunctions;

public class Berserker extends RPGClass {
	public Grapple classGrapple;
	private Cleave cleave;

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
		getClassDrops().add(new ClassDrop(sword, 1, 1));

		AddChanceToDrop(new ClassDrop(string, 2, 1), 1);
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
						(int) (3 * healthPercent)));
	}

	@Override
	public synchronized void SelfDamageOther(EntityDamageByEntityEvent event) {
		setTarget((LivingEntity) event.getEntity());
		if (!cleave.onCooldown()) {
			cleave.PerformAbility(event);
		}
	}

	@Override
	public void SetAbilities() {
		AddAbility(classGrapple = new Grapple(this, 1, 20), 4);
		AddAbility(cleave = new Cleave(this, 1, 5), 1);
		SortAbilities();
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
