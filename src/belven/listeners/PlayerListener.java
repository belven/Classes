package belven.listeners;

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

import belven.classes.Archer;
import belven.classes.Assassin;
import belven.classes.ClassManager;
import belven.classes.Healer;
import belven.classes.Mage;
import belven.classes.Warrior;

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

    private void PerformClassAbility(PlayerToggleSneakEvent event)
    {
        if (plugin.CurrentPlayerClasses.get(event.getPlayer()) instanceof Assassin)
        {
            ((Assassin)plugin.CurrentPlayerClasses.get(event.getPlayer())).ToggleSneakEvent(event);
        }        
    }

    public void PerformClassAbility(PlayerInteractEvent event)
    {
        if (event.getAction() == Action.RIGHT_CLICK_AIR)
        {
            Player currentPlayer = event.getPlayer();

            if (plugin.CurrentPlayerClasses.get(currentPlayer) instanceof Healer)
            {
                ((Healer)plugin.CurrentPlayerClasses.get(currentPlayer)).PerformAbility(currentPlayer);
            }
            else if (plugin.CurrentPlayerClasses.get(currentPlayer) instanceof Mage)
            {
                ((Mage)plugin.CurrentPlayerClasses.get(currentPlayer)).PerformAbility(currentPlayer);
            }
            else if (plugin.CurrentPlayerClasses.get(currentPlayer) instanceof Warrior)
            {
                ((Warrior)plugin.CurrentPlayerClasses.get(currentPlayer)).PerformAbility(currentPlayer);
            }
            else if (plugin.CurrentPlayerClasses.get(currentPlayer) instanceof Assassin)
            {
                ((Assassin)plugin.CurrentPlayerClasses.get(currentPlayer)).PerformAbility(currentPlayer);
            }
            else if (plugin.CurrentPlayerClasses.get(currentPlayer) instanceof Archer)
            {
                ((Archer)plugin.CurrentPlayerClasses.get(currentPlayer)).PerformAbility(currentPlayer);
            }
        }
    }

    public void PerformClassAbility(PlayerInteractEntityEvent event)
    {
        Player currentPlayer = event.getPlayer();
        Entity currentEntity = event.getRightClicked();
        belven.classes.Class currentClass = plugin.CurrentPlayerClasses.get(currentPlayer);

        if (currentClass instanceof Healer)
        {
            ((Healer)currentClass).PerformAbility(currentEntity);
        }
        else if (currentClass instanceof Mage)
        {
            ((Mage)currentClass).PerformAbility(currentEntity);
        }
        else if (currentClass instanceof Warrior)
        {
            ((Warrior)currentClass).PerformAbility(currentEntity);
        }
        else if (currentClass instanceof Assassin)
        {
            ((Assassin)currentClass).PerformAbility(currentEntity);
        }
        else if (currentClass instanceof Archer)
        {
            ((Archer)currentClass).PerformAbility(currentEntity);
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
        belven.classes.Class currentClass = plugin.CurrentPlayerClasses.get(currentPlayer);

        if (damagerEntity.getType() == EntityType.PLAYER)
        {
            currentPlayer = (Player) event.getDamager();
        }
        else if (damagerEntity.getType() == EntityType.ARROW)
        {
            Arrow currentArrow = (Arrow) damagerEntity;

            if (currentArrow.getShooter().getType() == EntityType.PLAYER)
            {
                currentPlayer = (Player) currentArrow.getShooter();
            }
        }
        else if (damagerEntity.getType() == EntityType.FIREBALL)
        {
            Fireball currentFireball = (Fireball) damagerEntity;

            if (currentFireball.getShooter().getType() == EntityType.PLAYER)
            {
                currentPlayer = (Player) currentFireball.getShooter();
            }
        }
        
        if (currentClass instanceof Mage)
        {
            event.setDamage(7);
        }
        else if (currentClass instanceof Assassin)
        {
            ((Assassin)currentClass).MobTakenDamage(event);;
        }
        else if (currentClass instanceof Archer)
        {
            ((Archer)currentClass).MobTakenDamage(event);
        }       
    }

    public void PlayerTakenDamage(EntityDamageByEntityEvent event)
    {
        Player damagedPlayer = (Player) event.getEntity();
        belven.classes.Class currentClass = plugin.CurrentPlayerClasses.get(damagedPlayer);
        
        if (currentClass instanceof Mage)
        {
            ((Mage)currentClass).TakeDamage(event, damagedPlayer);
        }
        else if (currentClass instanceof Warrior)
        {
            ((Warrior)currentClass).TakeDamage(event, damagedPlayer);
        }
    }

    public int SecondsToTicks(int seconds)
    {
        return seconds * 20;
    }
}