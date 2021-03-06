package belven.classes.player;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;

import belven.classes.ClassManager;
import belven.classes.RPGClass;
import belven.classes.abilities.Ability;
import belven.classes.player.abilities.DamageTrap;
import belven.classes.player.abilities.FireTrap;
import belven.classes.player.abilities.WebArrow;
import belven.resources.ClassDrop;

public class Archer extends RPGClass {
	public WebArrow classCripplingArrow;
	public FireTrap classFireTrap;
	public DamageTrap classDamageTrap;

	public Archer(Player currentPlayer, ClassManager instance) {
		super(12, currentPlayer, instance);
		className = "Archer";
		SetClassDrops();
		SetAbilities();
	}

	@Override
	public void SelfCast(PlayerInteractEvent event, Player currentPlayer) {
		CheckAbilitiesToCast(currentPlayer, event);
	}

	private void CheckAbilitiesToCast(Entity currentEntity, Event event) {
		Location trapLocation = getOwner().getLocation();
		trapLocation.setY(trapLocation.getY() - 1);
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
	public void SetClassDrops() {
		Dye dye = new Dye();
		dye.setColor(DyeColor.RED);
		ItemStack redwool = dye.toItemStack(2);

		dye = new Dye();
		dye.setColor(DyeColor.GRAY);
		ItemStack graywool = dye.toItemStack(2);

		ItemStack arrow = new ItemStack(Material.ARROW, 20);
		ItemStack bow = new ItemStack(Material.BOW);
		ItemStack snowBall = new ItemStack(Material.SNOW_BALL, 2);

		getClassDrops().add(new ClassDrop(arrow, 30, 7));
		getClassDrops().add(new ClassDrop(bow, 1, 1));

		AddChanceToDrop(new ClassDrop(redwool, 5), 1);
		AddChanceToDrop(new ClassDrop(graywool, 5), 1);
		AddChanceToDrop(new ClassDrop(snowBall, 2), 1);

		AddChanceToDrop(new ClassDrop(l_Boots(), 1), 1);
		AddChanceToDrop(new ClassDrop(l_ChestPlate(), 1), 1);
		AddChanceToDrop(new ClassDrop(l_Leggings(), 1), 1);
		AddChanceToDrop(new ClassDrop(l_Helmet(), 1), 1);

	}

	@Override
	public void SelfTakenDamage(EntityDamageByEntityEvent event) {
	}

	@Override
	public void SelfDamageOther(EntityDamageByEntityEvent event) {
		setTarget((LivingEntity) event.getEntity());
		if (getPlayer().getItemInHand().getType() == Material.BOW) {
			if (!classCripplingArrow.onCooldown() && classCripplingArrow.HasRequirements()) {
				classCripplingArrow.PerformAbility(event);
			}
		}
	}

	@Override
	public void SetAbilities() {
		if (!AbilitiesSet()) {
			classCripplingArrow = new WebArrow(this, 0, 0);
			classFireTrap = new FireTrap(this, 1, 0);
			classDamageTrap = new DamageTrap(this, 1, 0);
			getAbilities().add(classFireTrap);
			getAbilities().add(classDamageTrap);
			SortAbilities();

			setAbilitiesSet(true);
		}
	}

	@Override
	public void RightClickEntity(PlayerInteractEntityEvent event, Entity currentEntity) {
		

	}

}
