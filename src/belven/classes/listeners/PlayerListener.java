package belven.classes.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
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
import belven.classes.timedevents.AbilityDelay;
import belven.classes.timedevents.MobOutOfCombatTimer;

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
        if (plugin.CurrentPlayerClasses.get(event.getPlayer().getName()).CanCast)
        {
            BukkitTask currentTimer = new AbilityDelay(event.getPlayer(),
                    plugin).runTaskLater(plugin, SecondsToTicks(1));

            if (plugin.CurrentPlayerClasses.get(event.getPlayer().getName()) instanceof Assassin)
            {
                ((Assassin) plugin.CurrentPlayerClasses.get(event.getPlayer()
                        .getName())).ToggleSneakEvent(event);
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

            if (plugin.CurrentPlayerClasses.get(currentPlayer.getName()).CanCast)
            {
                BukkitTask currentTimer = new AbilityDelay(currentPlayer,
                        plugin).runTaskLater(plugin, SecondsToTicks(1));

                if (plugin.CurrentPlayerClasses.get(currentPlayer.getName()) instanceof Healer)
                {
                    ((Healer) plugin.CurrentPlayerClasses.get(currentPlayer
                            .getName())).PerformAbility(currentPlayer);
                }
                else if (plugin.CurrentPlayerClasses.get(currentPlayer
                        .getName()) instanceof Mage)
                {
                    ((Mage) plugin.CurrentPlayerClasses.get(currentPlayer
                            .getName())).PerformAbility(currentPlayer);
                }
                else if (plugin.CurrentPlayerClasses.get(currentPlayer
                        .getName()) instanceof Warrior)
                {
                    ((Warrior) plugin.CurrentPlayerClasses.get(currentPlayer
                            .getName())).PerformAbility(currentPlayer);
                }
                else if (plugin.CurrentPlayerClasses.get(currentPlayer
                        .getName()) instanceof Assassin)
                {
                    ((Assassin) plugin.CurrentPlayerClasses.get(currentPlayer
                            .getName())).PerformAbility(currentPlayer);
                }
                else if (plugin.CurrentPlayerClasses.get(currentPlayer
                        .getName()) instanceof Archer)
                {
                    ((Archer) plugin.CurrentPlayerClasses.get(currentPlayer
                            .getName())).PerformAbility(currentPlayer);
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
        case "SIGN":
            return false;
        case "BUTTON":
            return false;
        case "LEVER":
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

        if (plugin.CurrentPlayerClasses.get(event.getPlayer().getName()).CanCast)
        {
            BukkitTask currentTimer = new AbilityDelay(event.getPlayer(),
                    plugin).runTaskLater(plugin, SecondsToTicks(1));

            if (plugin.CurrentPlayerClasses.get(currentPlayer.getName()) instanceof Healer)
            {
                ((Healer) plugin.CurrentPlayerClasses.get(currentPlayer
                        .getName())).PerformAbility(currentEntity);
            }
            else if (plugin.CurrentPlayerClasses.get(currentPlayer.getName()) instanceof Mage)
            {
                ((Mage) plugin.CurrentPlayerClasses
                        .get(currentPlayer.getName()))
                        .PerformAbility(currentEntity);
            }
            else if (plugin.CurrentPlayerClasses.get(currentPlayer.getName()) instanceof Warrior)
            {
                ((Warrior) plugin.CurrentPlayerClasses.get(currentPlayer
                        .getName())).PerformAbility(currentEntity);
            }
            else if (plugin.CurrentPlayerClasses.get(currentPlayer.getName()) instanceof Assassin)
            {
                ((Assassin) plugin.CurrentPlayerClasses.get(currentPlayer
                        .getName())).PerformAbility(currentEntity);
            }
            else if (plugin.CurrentPlayerClasses.get(currentPlayer.getName()) instanceof Archer)
            {
                ((Archer) plugin.CurrentPlayerClasses.get(currentPlayer
                        .getName())).PerformAbility(currentEntity);
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

            if (plugin.CurrentPlayerClasses.get(currentPlayer.getName()) instanceof Assassin)
            {
                ((Assassin) plugin.CurrentPlayerClasses.get(currentPlayer
                        .getName())).MobTakenDamage(event);
            }
        }
        else if (damagerEntity.getType() == EntityType.ARROW)
        {
            Arrow currentArrow = (Arrow) damagerEntity;

            if (currentArrow.getShooter().getType() == EntityType.PLAYER)
            {
                currentPlayer = (Player) currentArrow.getShooter();

                if (plugin.CurrentPlayerClasses.get(currentPlayer.getName()) instanceof Assassin)
                {
                    ((Assassin) plugin.CurrentPlayerClasses.get(currentPlayer
                            .getName())).MobTakenDamage(event);
                }
                else if (plugin.CurrentPlayerClasses.get(currentPlayer
                        .getName()) instanceof Archer)
                {
                    ((Archer) plugin.CurrentPlayerClasses.get(currentPlayer
                            .getName())).MobTakenDamage(event);
                }

                if (event.getEntity() instanceof LivingEntity
                        && currentPlayer != null)
                {
                    event.setDamage(ScaleDamage(currentPlayer.getLevel(),
                            event.getDamage()));
                }
            }
        }
        else if (damagerEntity.getType() == EntityType.FIREBALL)
        {
            Fireball currentFireball = (Fireball) damagerEntity;

            if (currentFireball.getShooter().getType() == EntityType.PLAYER)
            {
                currentPlayer = (Player) currentFireball.getShooter();

                if (plugin.CurrentPlayerClasses.get(currentPlayer.getName()) instanceof Mage)
                {
                    event.setDamage(ScaleDamage(currentPlayer.getLevel(),
                            event.getDamage() + 3));
                }
            }
        }

        if (event.getEntity() instanceof LivingEntity && currentPlayer != null)
        {
            ScaleMobHealth(currentPlayer, (LivingEntity) event.getEntity(),
                    event.getDamage());
        }
    }

    public void ScaleMobHealth(Player player, LivingEntity mobToScale,
            double DamageDone)
    {
        int heathToscaleTo = (int) (MobMaxHealth(mobToScale) + (player
                .getLevel() * 1.2));

        if (mobToScale.getMaxHealth() != heathToscaleTo)
        {
            mobToScale.setMaxHealth(heathToscaleTo);
            mobToScale.damage(heathToscaleTo - DamageDone);
            new MobOutOfCombatTimer(mobToScale).runTaskTimer(plugin, 0,
                    SecondsToTicks(15));
        }
    }

    public int MobMaxHealth(LivingEntity entity)
    {
        if (entity.getType() == EntityType.ZOMBIE)
        {
            return 20;
        }
        else if (entity.getType() == EntityType.SKELETON)
        {
            return 20;
        }
        else if (entity.getType() == EntityType.SPIDER)
        {
            return 16;
        }
        else if (entity.getType() == EntityType.CREEPER)
        {
            return 20;
        }
        else if (entity.getType() == EntityType.WITHER)
        {
            return 300;
        }
        else if (entity.getType() == EntityType.BLAZE)
        {
            return 20;
        }
        else if (entity.getType() == EntityType.ENDERMAN)
        {
            return 40;
        }
        else if (entity.getType() == EntityType.CAVE_SPIDER)
        {
            return 12;
        }
        else if (entity.getType() == EntityType.GHAST)
        {
            return 10;
        }
        else if (entity.getType() == EntityType.MAGMA_CUBE)
        {
            MagmaCube MagmaCube = (MagmaCube) entity;

            if (MagmaCube.getSize() == 4)

            {
                return 16;
            }
            else if (MagmaCube.getSize() == 2)
            {
                return 4;
            }
            else
            {
                return 1;
            }
        }
        else if (entity.getType() == EntityType.PIG_ZOMBIE)
        {
            return 20;
        }
        else if (entity.getType() == EntityType.SLIME)
        {
            Slime slime = (Slime) entity;

            if (slime.getSize() == 4)

            {
                return 16;
            }
            else if (slime.getSize() == 2)
            {
                return 4;
            }
            else
            {
                return 1;
            }
        }
        else
        {
            return 20;
        }
    }

    public int ScaleDamage(int Level, double damageDone)
    {
        return (int) damageDone + (Level / 5);
    }

    public void PlayerTakenDamage(EntityDamageByEntityEvent event)
    {
        Player damagedPlayer = (Player) event.getEntity();

        if (plugin.CurrentPlayerClasses.get(damagedPlayer.getName()) instanceof Mage)
        {
            ((Mage) plugin.CurrentPlayerClasses.get(damagedPlayer.getName()))
                    .TakeDamage(event, damagedPlayer);
        }
        else if (plugin.CurrentPlayerClasses.get(damagedPlayer.getName()) instanceof Warrior)
        {
            ((Warrior) plugin.CurrentPlayerClasses.get(damagedPlayer.getName()))
                    .TakeDamage(event, damagedPlayer);
        }
    }

    public int SecondsToTicks(int seconds)
    {
        return seconds * 20;
    }
}