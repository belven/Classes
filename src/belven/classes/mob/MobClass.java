package belven.classes.mob;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import belven.classes.ClassManager;
import belven.classes.Abilities.Ability;
import belven.classes.player.RPGClass;
import belven.classes.timedevents.AbilityCooldown;
import belven.resources.EntityFunctions;
import belven.resources.Functions;

public abstract class MobClass extends RPGClass {
	public LivingEntity targetLE;

	public MobClass(double health, LivingEntity classOwner, ClassManager instance) {
		super(health, classOwner, instance);
	}

	@Override
	public LivingEntity getTarget(int radius, Player p) {
		return EntityFunctions.findTargetEntity(p, radius);
	}

	@Override
	public Player getTargetPlayer(int radius, Player p) {
		return EntityFunctions.findTargetPlayer(p, radius);

	}

	@Override
	public void SortAbilities() {
		SortAbilities(abilities);
	}

	public void AddAbility(Ability ability, int cooldown) {
		abilities.add(ability);
		ability.cooldown = cooldown;
	}

	@Override
	public void SortAbilities(List<Ability> tempAbilities) {
		Collections.sort(tempAbilities, new Comparator<Ability>() {
			@Override
			public int compare(Ability a1, Ability a2) {
				return Math.min(1, Math.max(-1, a1.priority - a2.priority));
			}
		});
	}

	@Override
	public abstract void SelfCast(Player currentPlayer);

	@Override
	public abstract void SetAbilities();

	@Override
	public abstract void RightClickEntity(Entity currentEntity);

	@Override
	public abstract void SelfTakenDamage(EntityDamageByEntityEvent event);

	@Override
	public abstract void SelfDamageOther(EntityDamageByEntityEvent event);

	@Override
	public void ToggleSneakEvent(PlayerToggleSneakEvent event) {
	}

	@Override
	public void UltAbilityUsed(Ability currentAbility) {
		setAbilityOnCoolDown(currentAbility);
	}

	@Override
	public void setAbilityOnCoolDown(Ability currentAbility, boolean sendMessage) {
		new AbilityCooldown(currentAbility, sendMessage).runTaskLater(plugin,
				Functions.SecondsToTicks(currentAbility.cooldown));
		currentAbility.onCooldown = true;
	}

	@Override
	public void setAbilityOnCoolDown(Ability currentAbility) {
		setAbilityOnCoolDown(currentAbility, false);
	}

	@Override
	public void AbilityUsed(Ability ability) {

	}

}
