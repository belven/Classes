package belven.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.scheduler.BukkitTask;

import belven.classes.Archer;
import belven.classes.Assassin;
import belven.classes.ClassManager;
import belven.classes.Healer;
import belven.classes.Mage;
import belven.classes.Warrior;
import belven.timedevents.AbilityDelay;

public class PlayerListener implements Listener
{
    private final ClassManager plugin;

    public PlayerListener(ClassManager instance)
    {
        plugin = instance;
    }

    @EventHandler
    public void onPlayerLoginEvent(PlayerLoginEvent event)
    {
        plugin.AddClassToPlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event)
    {
        PerformClassAbility(event);
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event)
    {
        PerformClassAbility(event);
    }

    @EventHandler
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event)
    {
        PerformClassAbility(event);
    }

    @SuppressWarnings("unused")
    private void PerformClassAbility(PlayerToggleSneakEvent event)
    {
        if (plugin.CurrentPlayerClasses.get(event.getPlayer()).CanCast)
        {
            BukkitTask currentTimer = new AbilityDelay(event.getPlayer(),
                    plugin).runTaskLater(plugin, SecondsToTicks(1));

            if (plugin.CurrentPlayerClasses.get(event.getPlayer()) instanceof Assassin)
            {
                ((Assassin) plugin.CurrentPlayerClasses.get(event.getPlayer()))
                        .ToggleSneakEvent(event);
            }
        }
    }

    @SuppressWarnings("unused")
    public void PerformClassAbility(PlayerInteractEvent event)
    {
        if (event.getAction() == Action.RIGHT_CLICK_AIR
                || event.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            Player currentPlayer = event.getPlayer();

            if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
            {
                Material blockMaterial = event.getClickedBlock().getType();
                if (!isNotInteractiveBlock(blockMaterial))
                {
                    return;
                }
                else if (event.getItem() != null
                        && !isNotInteractiveBlock(event.getItem().getType()))
                {
                    return;
                }
            }

            if (plugin.CurrentPlayerClasses.get(currentPlayer).CanCast)
            {
                BukkitTask currentTimer = new AbilityDelay(currentPlayer,
                        plugin).runTaskLater(plugin, SecondsToTicks(1));

                if (plugin.CurrentPlayerClasses.get(currentPlayer) instanceof Healer)
                {
                    ((Healer) plugin.CurrentPlayerClasses.get(currentPlayer))
                            .PerformAbility(currentPlayer);
                }
                else if (plugin.CurrentPlayerClasses.get(currentPlayer) instanceof Mage)
                {
                    ((Mage) plugin.CurrentPlayerClasses.get(currentPlayer))
                            .PerformAbility(currentPlayer);
                }
                else if (plugin.CurrentPlayerClasses.get(currentPlayer) instanceof Warrior)
                {
                    ((Warrior) plugin.CurrentPlayerClasses.get(currentPlayer))
                            .PerformAbility(currentPlayer);
                }
                else if (plugin.CurrentPlayerClasses.get(currentPlayer) instanceof Assassin)
                {
                    ((Assassin) plugin.CurrentPlayerClasses.get(currentPlayer))
                            .PerformAbility(currentPlayer);
                }
                else if (plugin.CurrentPlayerClasses.get(currentPlayer) instanceof Archer)
                {
                    ((Archer) plugin.CurrentPlayerClasses.get(currentPlayer))
                            .PerformAbility(currentPlayer);
                }
            }
        }
    }

    public boolean isNotInteractiveBlock(Material material)
    {
        switch (material.toString())
        {
        case "CHEST":
            return false;
        case "WORKBENCH":
            return false;
        case "ANVIL":
            return false;
        case "FURNACE":
            return false;
        case "ENCHANTMENT_TABLE":
            return false;
        case "ENDER_CHEST":
            return false;
        case "BED":
            return false;
        case "MINECART":
            return false;
        default:
            return true;
        }
    }

    @SuppressWarnings("unused")
    public void PerformClassAbility(PlayerInteractEntityEvent event)
    {
        Player currentPlayer = event.getPlayer();
        Entity currentEntity = event.getRightClicked();

        if (plugin.CurrentPlayerClasses.get(event.getPlayer()).CanCast)
        {
            BukkitTask currentTimer = new AbilityDelay(event.getPlayer(),
                    plugin).runTaskLater(plugin, SecondsToTicks(1));

            if (plugin.CurrentPlayerClasses.get(currentPlayer) instanceof Healer)
            {
                ((Healer) plugin.CurrentPlayerClasses.get(currentPlayer))
                        .PerformAbility(currentEntity);
            }
            else if (plugin.CurrentPlayerClasses.get(currentPlayer) instanceof Mage)
            {
                ((Mage) plugin.CurrentPlayerClasses.get(currentPlayer))
                        .PerformAbility(currentEntity);
            }
            else if (plugin.CurrentPlayerClasses.get(currentPlayer) instanceof Warrior)
            {
                ((Warrior) plugin.CurrentPlayerClasses.get(currentPlayer))
                        .PerformAbility(currentEntity);
            }
            else if (plugin.CurrentPlayerClasses.get(currentPlayer) instanceof Assassin)
            {
                ((Assassin) plugin.CurrentPlayerClasses.get(currentPlayer))
                        .PerformAbility(currentEntity);
            }
            else if (plugin.CurrentPlayerClasses.get(currentPlayer) instanceof Archer)
            {
                ((Archer) plugin.CurrentPlayerClasses.get(currentPlayer))
                        .PerformAbility(currentEntity);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event)
    {
        if (event.getEntityType() == EntityType.PLAYER)
        {
            PlayerTakenDamage(event);
        }
        else if (IsAMob(event.getEntityType()))
        {
            MobTakenDamage(event);
        }
    }

    public boolean IsAMob(EntityType currentEntityType)
    {
        if (currentEntityType == EntityType.BLAZE
                || currentEntityType == EntityType.CAVE_SPIDER
                || currentEntityType == EntityType.CREEPER
                || currentEntityType == EntityType.ENDER_DRAGON
                || currentEntityType == EntityType.ENDERMAN
                || currentEntityType == EntityType.GHAST
                || currentEntityType == EntityType.MAGMA_CUBE
                || currentEntityType == EntityType.PIG_ZOMBIE
                || currentEntityType == EntityType.SKELETON
                || currentEntityType == EntityType.SPIDER
                || currentEntityType == EntityType.SLIME
                || currentEntityType == EntityType.WITCH
                || currentEntityType == EntityType.WITHER
                || currentEntityType == EntityType.ZOMBIE)
        {
            return true;
        }
        else
            return false;
    }

    public void MobTakenDamage(EntityDamageByEntityEvent event)
    {
        Entity damagerEntity = event.getDamager();
        Player currentPlayer = null;

        if (damagerEntity.getType() == EntityType.PLAYER)
        {
            currentPlayer = (Player) damagerEntity;

            if (plugin.CurrentPlayerClasses.get(currentPlayer) instanceof Assassin)
            {
                ((Assassin) plugin.CurrentPlayerClasses.get(currentPlayer))
                        .MobTakenDamage(event);
            }
        }
        else if (damagerEntity.getType() == EntityType.ARROW)
        {
            Arrow currentArrow = (Arrow) damagerEntity;

            if (currentArrow.getShooter().getType() == EntityType.PLAYER)
            {
                currentPlayer = (Player) currentArrow.getShooter();

                if (plugin.CurrentPlayerClasses.get(currentPlayer) instanceof Assassin)
                {
                    ((Assassin) plugin.CurrentPlayerClasses.get(currentPlayer))
                            .MobTakenDamage(event);
                }
                else if (plugin.CurrentPlayerClasses.get(currentPlayer) instanceof Archer)
                {
                    ((Archer) plugin.CurrentPlayerClasses.get(currentPlayer))
                            .MobTakenDamage(event);
                }
            }
        }
        else if (damagerEntity.getType() == EntityType.FIREBALL)
        {
            Fireball currentFireball = (Fireball) damagerEntity;

            if (currentFireball.getShooter().getType() == EntityType.PLAYER)
            {
                currentPlayer = (Player) currentFireball.getShooter();
                if (plugin.CurrentPlayerClasses.get(currentPlayer) instanceof Mage)
                {
                    event.setDamage(event.getDamage()
                            + (currentPlayer.getLevel() / 4));
                }
            }
        }

        // currentPlayer.sendMessage(String.valueOf(event.getDamage()));

    }

    public void PlayerTakenDamage(EntityDamageByEntityEvent event)
    {
        Player damagedPlayer = (Player) event.getEntity();

        if (plugin.CurrentPlayerClasses.get(damagedPlayer) instanceof Mage)
        {
            ((Mage) plugin.CurrentPlayerClasses.get(damagedPlayer)).TakeDamage(
                    event, damagedPlayer);
        }
        else if (plugin.CurrentPlayerClasses.get(damagedPlayer) instanceof Warrior)
        {
            ((Warrior) plugin.CurrentPlayerClasses.get(damagedPlayer))
                    .TakeDamage(event, damagedPlayer);
        }
    }

    public int SecondsToTicks(int seconds)
    {
        return seconds * 20;
    }
}