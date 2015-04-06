package belven.classes.player;

import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import belven.classes.ClassManager;
import belven.classes.RPGClass;
import belven.classes.player.abilities.LastResort;
import belven.classes.player.abilities.Retaliation;
import belven.resources.ClassDrop;
import belven.resources.Functions;

public class Warrior extends RPGClass {
	public LastResort currentLastResort;
	public Retaliation currentRetaliation;

	public Warrior(Player currentPlayer, ClassManager instance) {
		super(40, currentPlayer, instance);
		className = "Warrior";

		SetClassDrops();
		SetAbilities();
	}

	@Override
	public void SetClassDrops() {
		ItemStack bread = new ItemStack(Material.BREAD);
		ItemStack sword = new ItemStack(Material.STONE_SWORD);
		ItemStack strength = new ItemStack(new Potion(PotionType.STRENGTH, 2).toItemStack(1));
		classDrops.add(new ClassDrop(bread, true, 5));
		classDrops.add(new ClassDrop(sword, true, 1, 1));
		classDrops.add(new ClassDrop(strength, 0, 10, 1));
		classDrops.add(new ClassDrop(i_Boots(), true, 1));
		classDrops.add(new ClassDrop(i_ChestPlate(), true, 1));
		classDrops.add(new ClassDrop(i_Leggings(), true, 1));
		classDrops.add(new ClassDrop(i_Helmet(), true, 1));
	}

	@Override
	public void SelfCast(Player currentPlayer) {
	}

	@Override
	public void RightClickEntity(Entity currentEntity) {
	}

	@Override
	public void OtherTakenDamage(EntityDamageByEntityEvent event) {
		Player p = (Player) event.getEntity();
		double healthPercent = plugin.GetPlayerE(getPlayer()).GetMissingHealthPercent();

		if (event.getDamage() > 0 && healthPercent > 0.1 && p.getLocation().distance(classOwner.getLocation()) < 30) {
			classOwner.damage(event.getDamage());
			event.setDamage(0.0);
			this.classOwner.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Functions
					.SecondsToTicks(3), (int) (4 * healthPercent)));
		}
	}

	@Override
	public void SelfTakenDamage(EntityDamageByEntityEvent event) {
		Damageable dcurrentPlayer = classOwner;

		if (!currentLastResort.onCooldown && dcurrentPlayer.getHealth() <= 5 && currentLastResort.HasRequirements()) {
			UltAbilityUsed(currentLastResort);
			currentLastResort.PerformAbility();
		} else if (!currentRetaliation.onCooldown && getPlayer().isBlocking()) {
			currentRetaliation.PerformAbility();
		}
	}

	@Override
	public void SelfTakenDamage(EntityDamageEvent event) {
		Damageable dcurrentPlayer = classOwner;

		if (!currentLastResort.onCooldown && dcurrentPlayer.getHealth() <= 5 && currentLastResort.HasRequirements()) {
			UltAbilityUsed(currentLastResort);
			currentLastResort.PerformAbility();
		}
	}

	@Override
	public void SetAbilities() {
		if (!abilitiesSet) {
			currentLastResort = new LastResort(this, 1, 5);
			currentRetaliation = new Retaliation(this, 2, 1);
			currentRetaliation.cooldown = 3;
			SortAbilities();
			abilitiesSet = true;
		}
	}

	@Override
	public void SelfDamageOther(EntityDamageByEntityEvent event) {
		// TODO Auto-generated method stub

	}
}