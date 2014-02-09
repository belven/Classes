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

    public void PerformClassAbility(PlayerInteractEvent event)
    {
        if (event.getAction() == Action.RIGHT_CLICK_AIR)
        {
            Player currentPlayer = event.getPlayer();

            StringToClass(plugin.CurrentClasses.get(currentPlayer),
                    currentPlayer);
        }
    }

    public void PerformClassAbility(PlayerInteractEntityEvent event)
    {
        Player currentPlayer = event.getPlayer();
        Entity currentEntity = event.getRightClicked();

        StringToClass(plugin.CurrentClasses.get(currentPlayer), currentPlayer,
                currentEntity);

    }

    public void StringToClass(String className, Player player,
            Entity currentEntity)
    {

        // plugin.CurrentPlayerClasses.get(player).PerformAbility(currentEntity);

        switch (className.toLowerCase())
        {
        case "healer":
            Healer currentHealer = new Healer(player, plugin);
            currentHealer.PerformAbility(currentEntity);
            break;
        case "mage":
            Mage currentMage = new Mage(player, plugin);
            currentMage.PerformAbility(currentEntity);
            break;
        case "warrior":
            Warrior currentWarrior = new Warrior(player, plugin);
            currentWarrior.PerformAbility(currentEntity);
            break;
        case "assassin":
            Assassin currentAssassin = new Assassin(player, plugin);
            currentAssassin.PerformAbility(player);
            break;
        case "archer":
            Archer currentArcher = new Archer(player, plugin);
            currentArcher.PerformAbility(currentEntity);
            break;
        }
    }

    public void StringToClass(String className, Player player)
    {
        // plugin.CurrentPlayerClasses.get(player).PerformAbility(player);

        switch (className.toLowerCase())
        {
        case "healer":
            Healer currentHealer = new Healer(player, plugin);
            currentHealer.PerformAbility(player);
            break;
        case "mage":
            Mage currentMage = new Mage(player, plugin);
            currentMage.PerformAbility(player);
            break;
        case "warrior":
            Warrior currentWarrior = new Warrior(player, plugin);
            currentWarrior.PerformAbility(player);
            break;
        case "assassin":
            Assassin currentAssassin = new Assassin(player, plugin);
            currentAssassin.PerformAbility(player);
            break;
        case "archer":
            Archer currentArcher = new Archer(player, plugin);
            currentArcher.PerformAbility(player);
            break;
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
        String playerClass = null;
        Player currentPlayer = null;

        if (damagerEntity.getType() == EntityType.PLAYER)
        {
            currentPlayer = (Player) event.getDamager();
            playerClass = plugin.CurrentClasses.get(currentPlayer)
                    .toLowerCase();
        }
        else if (damagerEntity.getType() == EntityType.ARROW)
        {
            Arrow currentArrow = (Arrow) damagerEntity;

            if (currentArrow.getShooter().getType() == EntityType.PLAYER)
            {
                currentPlayer = (Player) currentArrow.getShooter();
                playerClass = plugin.CurrentClasses.get(currentPlayer)
                        .toLowerCase();
            }
        }
        else if (damagerEntity.getType() == EntityType.FIREBALL)
        {
            Fireball currentFireball = (Fireball) damagerEntity;

            if (currentFireball.getShooter().getType() == EntityType.PLAYER)
            {
                currentPlayer = (Player) currentFireball.getShooter();
                playerClass = plugin.CurrentClasses.get(currentPlayer)
                        .toLowerCase();
            }
        }

        if (playerClass != null && currentPlayer != null)
        {
            switch (playerClass)
            {
            case "archer":
                Archer newArcher = new Archer(currentPlayer, plugin);
                newArcher.MobTakenDamage(event);
                break;
            case "assassin":
                Assassin newAssassin = new Assassin(currentPlayer, plugin);
                newAssassin.MobTakenDamage(event);
                break;
            case "mage":
                event.setDamage(7);
                break;
            }
        }
    }

    public void PlayerTakenDamage(EntityDamageByEntityEvent event)
    {
        Player damagedPlayer = (Player) event.getEntity();
        Warrior currentWarrior = new Warrior(damagedPlayer, plugin);
        Mage currentMage = new Mage(damagedPlayer, plugin);
        String playerClass = plugin.CurrentClasses.get(damagedPlayer)
                .toLowerCase();
        
        switch (playerClass)
        {
        case "warrior":
            currentWarrior.TakeDamage(event, damagedPlayer);
            break;
        case "assassin":
            break;
        case "mage":
            currentMage.TakeDamage(event, damagedPlayer);
            break;
        }
    }

    public int SecondsToTicks(int seconds)
    {
        return seconds * 20;
    }
}