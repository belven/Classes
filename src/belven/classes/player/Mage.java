package belven.classes.player;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;
import org.bukkit.util.BlockIterator;

import belven.classes.ClassManager;
import belven.classes.RPGClass;
import belven.classes.abilities.Ability;
import belven.classes.player.abilities.ChainLightning;
import belven.classes.player.abilities.Levitate;
import belven.classes.player.abilities.LightningStrike;
import belven.classes.player.abilities.MageFireball;
import belven.classes.player.abilities.Pop;
import belven.resources.ClassDrop;
import belven.resources.EntityFunctions;

public class Mage extends RPGClass {
	public MageFireball classFireball;
	public ChainLightning classChainLightning;
	public LightningStrike classLightningStrike;
	public BlockIterator currentBlockIterator;
	public Pop classPop;
	private Levitate classLevitate;

	public Mage(Player currentPlayer, ClassManager instance) {
		super(8, currentPlayer, instance);
		className = "Mage";
		SetAbilities();
		SetClassDrops();
	}

	public void CheckAbilitiesToCast(Event event) {
		for (Ability a : getAbilities()) {
			if (!a.onCooldown() && a.HasRequirements()) {
				if (!a.PerformAbility(event)) {
					continue;
				} else if (a.shouldBreak()) {
					break;
				}
			}
		}
	}

	@Override
	public void SetClassDrops() {
		ItemStack firePot = new Potion(PotionType.FIRE_RESISTANCE, 2).toItemStack(1);

		Dye dye = new Dye();
		dye.setColor(DyeColor.BLUE);
		ItemStack lapisBlock = dye.toItemStack(10);

		ItemStack feather = new ItemStack(Material.FEATHER, 2);

		getClassDrops().add(new ClassDrop(lapisBlock, true, 20));
		getClassDrops().add(new ClassDrop(feather, 0, 20, 5));
		getClassDrops().add(new ClassDrop(firePot, 20, 40, 1));

		getClassDrops().add(new ClassDrop(l_Boots(), 40, 100, 1));
		getClassDrops().add(new ClassDrop(l_ChestPlate(), 40, 100, 1));
		getClassDrops().add(new ClassDrop(l_Leggings(), 40, 100, 1));
		getClassDrops().add(new ClassDrop(l_Helmet(), 40, 100, 1));
	}

	@Override
	public void SelfCast(PlayerInteractEvent event, Player currentPlayer) {
		CheckAbilitiesToCast(event);
	}

	@Override
	public void SelfTakenDamage(EntityDamageByEntityEvent event) {
		if (event.getDamager().getType() == EntityType.LIGHTNING) {
			event.setDamage(0.0);
		} else if (!classLightningStrike.onCooldown()) {
			LivingEntity entityToStrike = EntityFunctions.GetDamager(event);
			setTarget(entityToStrike);
			if (entityToStrike != null) {
				classLightningStrike.PerformAbility(event);
			}
		}
	}

	@Override
	public void SelfDamageOther(EntityDamageByEntityEvent event) {

	}

	@Override
	public void ToggleSneakEvent(PlayerToggleSneakEvent event) {
		if (!classLevitate.onCooldown() && classLevitate.HasRequirements()) {
			if (classLevitate.getTimer() != null) {
				classLevitate.getTimer().run();
				classLevitate.getTimer().cancel();
				classLevitate.setTimer(null);
			}
			// if (event.getPlayer().isSneaking()) {
			classLevitate.PerformAbility(event);
			// }
		}
	}

	@Override
	public void SetAbilities() {
		if (!AbilitiesSet()) {
			setAbilitiesSet(true);
			AddAbility(classFireball = new MageFireball(this, 2, 5), 1);
			AddAbility(classChainLightning = new ChainLightning(this, 1, 5), 60);
			AddAbility(classLevitate = new Levitate(this, 1, 5), 0);
			AddAbility(classLightningStrike = new LightningStrike(this, 3, 2), 2);
			AddAbility(classPop = new Pop(this, 1, 5), 1);

			SortAbilities();
		}
	}

	@Override
	public void RightClickEntity(PlayerInteractEntityEvent event, Entity currentEntity) {
		setTarget((LivingEntity) event.getRightClicked());
		CheckAbilitiesToCast(event);
	}
}
