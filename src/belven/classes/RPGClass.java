package belven.classes;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

import belven.classes.abilities.Ability;
import belven.classes.events.AbilityUsed;
import belven.classes.timedevents.AbilityCooldown;
import belven.resources.ClassDrop;
import belven.resources.EntityFunctions;
import belven.resources.Functions;

public abstract class RPGClass extends RPGClassData {

	public RPGClass(double health, LivingEntity classOwner, ClassManager instance) {
		this(classOwner, instance);

		Damageable dcurrentPlayer = classOwner;
		dcurrentPlayer.setMaxHealth(health * 2);
		dcurrentPlayer.setHealth(dcurrentPlayer.getMaxHealth());
	}

	public RPGClass(LivingEntity classOwner, ClassManager instance) {
		setPlugin(instance);
		this.setOwner(classOwner);
	}

	public void AbilityUsed(Ability ability) {

	}

	public void AddAbility(Ability ability, int cooldown) {
		if (!getAbilities().contains(ability)) {
			getAbilities().add(ability);
			ability.cooldown = cooldown;
		}
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		} else if (other == this) {
			return true;
		} else if (!(other instanceof RPGClass)) {
			return false;
		} else {
			return false;
		}
	}

	public Player getPlayer() {
		return (Player) getOwner();
	}

	public LivingEntity getTarget(int radius, Player p) {
		return EntityFunctions.findTargetEntity(p, radius);
	}

	public Player getTargetPlayer(int radius, Player p) {
		return EntityFunctions.findTargetPlayer(p, radius);
	}

	public ItemStack i_Boots() {
		return new ItemStack(Material.IRON_BOOTS);
	}

	public ItemStack i_ChestPlate() {
		return new ItemStack(Material.IRON_CHESTPLATE);
	}

	public ItemStack i_Helmet() {
		return new ItemStack(Material.IRON_HELMET);
	}

	public ItemStack i_Leggings() {
		return new ItemStack(Material.IRON_LEGGINGS);
	}

	public ItemStack l_Boots() {
		return new ItemStack(Material.LEATHER_BOOTS);
	}

	public ItemStack l_ChestPlate() {
		return new ItemStack(Material.LEATHER_CHESTPLATE);
	}

	public ItemStack l_Helmet() {
		return new ItemStack(Material.LEATHER_HELMET);
	}

	public ItemStack l_Leggings() {
		return new ItemStack(Material.LEATHER_LEGGINGS);
	}

	public void ListAbilities() {
		for (Ability a : getAbilities()) {
			getOwner().sendMessage(a.GetAbilityName());
		}
	}

	public synchronized void RemoveClassDrop(Material m) {
		Iterator<ClassDrop> tempDrops = getClassDrops().iterator();

		while (tempDrops.hasNext()) {
			ClassDrop cd = tempDrops.next();
			if (cd.is.getType() == m) {
				tempDrops.remove();
			}
		}
	}

	public abstract void RightClickEntity(PlayerInteractEntityEvent event, Entity currentEntity);

	public abstract void SelfCast(PlayerInteractEvent event, Player currentPlayer);

	public abstract void SelfDamageOther(EntityDamageByEntityEvent event);

	public abstract void SelfTakenDamage(EntityDamageByEntityEvent event);

	public void SelfTakenDamage(EntityDamageEvent event) {

	}

	public void OtherTakenDamage(EntityDamageByEntityEvent event) {

	}

	public abstract void SetAbilities();

	public void setAbilityOnCoolDown(Ability currentAbility) {
		setAbilityOnCoolDown(currentAbility, false);
	}

	public void setAbilityOnCoolDown(Ability currentAbility, boolean sendMessage) {
		new AbilityCooldown(currentAbility, sendMessage).runTaskLater(getPlugin(),
				Functions.SecondsToTicks(currentAbility.cooldown));
		currentAbility.setOnCooldown(true);

		Bukkit.getPluginManager().callEvent(new AbilityUsed(currentAbility));
		getPlugin().writeToLog(
				getClassName() + " (" + getActualName(getOwner()) + ") used " + currentAbility.GetAbilityName());
	}

	public String getActualName(LivingEntity le) {
		if (getOwner().isDead() || !getOwner().isValid()) {
			return "N/A";
		}

		String name = getOwner().getName();
		return name.contains("?") ? getOwner().getType().toString() : name;
	}

	public void SetClassDrops() {

	}

	public void SortAbilities() {
		SortAbilities(getAbilities());
	}

	public void SortAbilities(List<Ability> tempAbilities) {
		Collections.sort(tempAbilities, new Comparator<Ability>() {

			@Override
			public int compare(Ability a1, Ability a2) {
				return Math.min(1, Math.max(-1, a1.priority - a2.priority));
			}
		});
	}

	public void ToggleSneakEvent(PlayerToggleSneakEvent event) {
	}

}
