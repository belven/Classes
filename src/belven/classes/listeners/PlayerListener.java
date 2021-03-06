package belven.classes.listeners;

import java.util.Iterator;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerVelocityEvent;

import belven.classes.ClassManager;
import belven.classes.events.AbilityUsed;
import belven.classes.player.Archer;
import belven.classes.player.Assassin;
import belven.classes.player.Daemon;
import belven.classes.timedevents.AbilityDelay;
import belven.resources.EntityFunctions;
import belven.resources.Functions;
import belven.resources.Group;
import belven.resources.MaterialFunctions;
import belven.resources.PlayerExtended;

public class PlayerListener implements Listener {
	private final ClassManager plugin;

	public PlayerListener(ClassManager instance) {
		plugin = instance;
	}

	@EventHandler
	public void onPlayerInteractEventSigns(PlayerInteractEvent event) {
		Sign currentSign;
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (event.getClickedBlock().getType() == Material.SIGN) {
				currentSign = (Sign) event.getClickedBlock();

				if (currentSign.getLine(0) != null && currentSign.getLine(0).contentEquals("[Class]")) {
					plugin.SetClass(event.getPlayer(), currentSign.getLine(1));
				}
			} else if (event.getClickedBlock().getType() == Material.WALL_SIGN) {
				currentSign = (Sign) event.getClickedBlock().getState();

				if (currentSign.getLine(0) != null && currentSign.getLine(0).contentEquals("[Class]")) {
					plugin.SetClass(event.getPlayer(), currentSign.getLine(1));
				}
			}
		}
	}

	@EventHandler
	public void onPlayerLoginEvent(PlayerLoginEvent event) {
		plugin.AddClassToPlayer(event.getPlayer());
		plugin.PlayersE.put(event.getPlayer(), new PlayerExtended(event.getPlayer()));
	}

	@EventHandler
	public void onPlayerLoginEvent(PlayerQuitEvent event) {
		plugin.PlayersE.remove(event.getPlayer());
	}

	// @EventHandler
	// public void onPlayerMoveEvent(PlayerMoveEvent event) {
	// RPGClass pClass = plugin.GetClass(event.getPlayer());
	// Block currentBlock = event.getTo().getBlock();
	// org.bukkit.Location upLoc = event.getTo();
	// upLoc.setY(upLoc.getY() + 1);
	//
	// event.getPlayer().setWalkSpeed(.2F);
	//
	// if (pClass.getClassName() == "Archer") {
	// if (currentBlock.getType() == Material.WEB) {
	// event.getPlayer().setWalkSpeed(1F);
	// }
	// }
	// }

	@EventHandler
	public void onAbilityUsed(AbilityUsed event) {
		plugin.GetClass(event.GetOwner()).AbilityUsed(event.GetAbility());
	}

	@EventHandler
	public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
		Player currentPlayer = event.getPlayer();
		Entity currentEntity = event.getRightClicked();
		if (plugin.GetClass(currentPlayer).CanCast()) {
			new AbilityDelay(currentPlayer, plugin).runTaskLater(plugin, Functions.SecondsToTicks(1));
			plugin.GetClass(currentPlayer).RightClickEntity(event, currentEntity);
		}
	}

	@EventHandler
	public synchronized void onPlayerInteractEvent(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Player currentPlayer = event.getPlayer();

			if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				Material blockMaterial = event.getClickedBlock().getType();
				if (MaterialFunctions.isInteractiveBlock(blockMaterial)) {
					return;
				} else if (event.getItem() != null && MaterialFunctions.isInteractiveBlock(event.getItem().getType())) {
					return;
				}
			} else if (event.getItem() != null && MaterialFunctions.isInteractiveBlock(event.getItem().getType())) {
				return;
			}

			if (plugin.GetClass(currentPlayer).CanCast()) {
				new AbilityDelay(currentPlayer, plugin).runTaskLater(plugin, Functions.SecondsToTicks(1));
				plugin.GetClass(currentPlayer).SelfCast(event, currentPlayer);
			}
		}
	}

	@EventHandler
	public synchronized void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event) {
		if (plugin.GetClass(event.getPlayer()).CanCast()) {
			new AbilityDelay(event.getPlayer(), plugin).runTaskLater(plugin, Functions.SecondsToTicks(1));
			plugin.GetClass(event.getPlayer()).ToggleSneakEvent(event);
		}
	}

	@EventHandler
	public synchronized void onPlayerVelocityEvent(PlayerVelocityEvent event) {
		if (plugin.GetClass(event.getPlayer()) instanceof Assassin) {
			event.setCancelled(true);
		} else if (plugin.GetClass(event.getPlayer()) instanceof Archer
				&& event.getPlayer().getItemInHand().getType() == Material.BOW) {
			event.setCancelled(true);
		} else if (plugin.GetClass(event.getPlayer()) instanceof Daemon && event.getPlayer().getFireTicks() > 0) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public synchronized void onEntityDamage(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof LivingEntity)) {
			return;
		}
		// The entity that took the damage
		LivingEntity damagee = (LivingEntity) event.getEntity();

		if (damagee != null) {
			plugin.GetClass(damagee).SelfTakenDamage(event);
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public synchronized void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof LivingEntity)) {
			return;
		}
		// The entity that took the damage
		LivingEntity damagee = (LivingEntity) event.getEntity();

		// The entity that dealt the damage
		LivingEntity damager = EntityFunctions.GetDamager(event);

		if (event.getEntityType() == EntityType.PLAYER) {
			Player dp = (Player) damagee;
			Group playerGroup = plugin.getAllyGroup(dp);

			if (playerGroup != null) {
				Iterator<Player> iter = playerGroup.getPlayers().iterator();

				while (iter.hasNext()) {
					Player p = iter.next();
					if (dp != p && p != null) {
						plugin.GetClass(p).OtherTakenDamage(event);
					}
				}
			}
		}

		if (damagee != null) {
			plugin.GetClass(damagee).SelfTakenDamage(event);
		}

		if (damager != null) {
			plugin.GetClass(damager).SelfDamageOther(event);
		}
	}

	public synchronized boolean ScaleMobHealth(Player player, LivingEntity mobToScale, double DamageDone) {
		if (player != null && mobToScale != null) {
			double heathToscaleTo = EntityFunctions.MobMaxHealth(mobToScale) + player.getLevel() * 1.2;

			if (heathToscaleTo > 380) {
				heathToscaleTo = 380;
			}

			Damageable dmobToScale = mobToScale;

			double CurrentHealthPercent = EntityFunctions.entityCurrentHealthPercent(dmobToScale.getHealth(),
					dmobToScale.getMaxHealth());

			double damageToDo = heathToscaleTo * CurrentHealthPercent - DamageDone;

			dmobToScale.setMaxHealth(heathToscaleTo);

			if (damageToDo < 0) {
				damageToDo = 0;
			}

			mobToScale.setHealth(damageToDo);
			return true;
		}
		return false;
	}
}