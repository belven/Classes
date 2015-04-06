package belven.classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

import belven.classes.abilities.Ability;
import belven.classes.timedevents.AbilityCooldown;
import belven.resources.ClassDrop;
import belven.resources.EntityFunctions;
import belven.resources.Functions;

public abstract class RPGClass {
	public ArrayList<Ability> abilities = new ArrayList<Ability>();
	public LivingEntity classOwner = null;
	public ClassManager plugin;
	protected String className = "";
	protected String baseClassName = "";
	public LivingEntity targetLE;
	public Player targetPlayer;

	public List<ClassDrop> classDrops = new ArrayList<ClassDrop>();

	public boolean CanCast = true;
	public boolean abilitiesSet = false;

	public RPGClass(double health, LivingEntity classOwner, ClassManager instance) {
		plugin = instance;
		this.classOwner = classOwner;

		Damageable dcurrentPlayer = classOwner;
		dcurrentPlayer.setMaxHealth(health * 2);
		dcurrentPlayer.setHealth(dcurrentPlayer.getMaxHealth());
	}

	public LivingEntity getTarget(int radius, Player p) {
		return EntityFunctions.findTargetEntity(p, radius);
	}

	public Player getTargetPlayer(int radius, Player p) {
		return EntityFunctions.findTargetPlayer(p, radius);
	}

	public Player getPlayer() {
		return (Player) classOwner;
	}

	public final String getClassName() {
		return className;
	}

	public void SetClassDrops() {

	}

	public synchronized void RemoveClassDrop(Material m) {
		Iterator<ClassDrop> tempDrops = classDrops.iterator();

		while (tempDrops.hasNext()) {
			ClassDrop cd = tempDrops.next();
			if (cd.is.getType() == m) {
				tempDrops.remove();
			}
		}
	}

	public void ListAbilities() {
		for (Ability a : abilities) {
			classOwner.sendMessage(a.GetAbilityName());
		}
	}

	public void AddAbility(Ability ability, int cooldown) {
		abilities.add(ability);
		ability.cooldown = cooldown;
	}

	public void SortAbilities() {
		SortAbilities(abilities);
	}

	public void SortAbilities(List<Ability> tempAbilities) {
		Collections.sort(tempAbilities, new Comparator<Ability>() {
			@Override
			public int compare(Ability a1, Ability a2) {
				return Math.min(1, Math.max(-1, a1.priority - a2.priority));
			}
		});
	}

	public final String getBaseClassName() {
		return baseClassName;
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

	public abstract void SelfCast(Player currentPlayer);

	public abstract void SetAbilities();

	public abstract void RightClickEntity(Entity currentEntity);

	public abstract void SelfTakenDamage(EntityDamageByEntityEvent event);

	public abstract void SelfDamageOther(EntityDamageByEntityEvent event);

	public void ToggleSneakEvent(PlayerToggleSneakEvent event) {
	}

	public void UltAbilityUsed(Ability currentAbility) {
		setAbilityOnCoolDown(currentAbility);
	}

	public void setAbilityOnCoolDown(Ability currentAbility, boolean sendMessage) {
		new AbilityCooldown(currentAbility, sendMessage).runTaskLater(plugin,
				Functions.SecondsToTicks(currentAbility.cooldown));
		currentAbility.onCooldown = true;
	}

	public void setAbilityOnCoolDown(Ability currentAbility) {
		setAbilityOnCoolDown(currentAbility, false);
	}

	public ItemStack l_ChestPlate() {
		return new ItemStack(Material.LEATHER_CHESTPLATE);
	}

	public ItemStack l_Leggings() {
		return new ItemStack(Material.LEATHER_LEGGINGS);
	}

	public ItemStack l_Helmet() {
		return new ItemStack(Material.LEATHER_HELMET);
	}

	public ItemStack l_Boots() {
		return new ItemStack(Material.LEATHER_BOOTS);
	}

	public ItemStack i_ChestPlate() {
		return new ItemStack(Material.IRON_CHESTPLATE);
	}

	public ItemStack i_Leggings() {
		return new ItemStack(Material.IRON_LEGGINGS);
	}

	public ItemStack i_Helmet() {
		return new ItemStack(Material.IRON_HELMET);
	}

	public ItemStack i_Boots() {
		return new ItemStack(Material.IRON_BOOTS);
	}

	public void SelfTakenDamage(EntityDamageEvent event) {

	}

	public void AbilityUsed(Ability ability) {

	}

	public void OtherTakenDamage(EntityDamageByEntityEvent event) {
	}

}
