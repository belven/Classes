package belven.classes.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.scheduler.BukkitTask;

import resources.functions;
import belven.classes.Archer;
import belven.classes.Assassin;
import belven.classes.ClassManager;
import belven.classes.Healer;
import belven.classes.Mage;
import belven.classes.Warrior;
import belven.classes.timedevents.AbilityDelay;

//import belven.classes.timedevents.MobOutOfCombatTimer;

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
                    plugin).runTaskLater(plugin, functions.SecondsToTicks(1));

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
                if (!functions.isNotInteractiveBlock(blockMaterial))
                {
                    return;
                }
                else if (event.getItem() != null
                        && !functions.isNotInteractiveBlock(event.getItem()
                                .getType()))
                {
                    return;
                }
            }
            else if (event.getItem() != null
                    && !functions.isNotInteractiveBlock(event.getItem()
                            .getType()))
            {
                return;
            }

            if (plugin.CurrentPlayerClasses.get(currentPlayer.getName()).CanCast)
            {
                BukkitTask currentTimer = new AbilityDelay(currentPlayer,
                        plugin).runTaskLater(plugin,
                        functions.SecondsToTicks(1));

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

    @SuppressWarnings("unused")
    public void PerformClassAbility(PlayerInteractEntityEvent event)
    {
        Player currentPlayer = event.getPlayer();
        Entity currentEntity = event.getRightClicked();

        if (plugin.CurrentPlayerClasses.get(event.getPlayer().getName()).CanCast)
        {
            BukkitTask currentTimer = new AbilityDelay(event.getPlayer(),
                    plugin).runTaskLater(plugin, functions.SecondsToTicks(1));

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
        else if (functions.IsAMob(event.getEntityType()))
        {
            MobTakenDamage(event);
        }
    }

    @SuppressWarnings("deprecation")
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

            }
        }
        else if (damagerEntity.getType() == EntityType.FIREBALL)
        {
            Projectile currentFireball = (Projectile) damagerEntity;

            if (currentFireball instanceof Fireball
                    && currentFireball.getShooter().getType() == EntityType.PLAYER)
            {
                currentPlayer = (Player) currentFireball.getShooter();

                if (plugin.CurrentPlayerClasses.get(currentPlayer.getName()) instanceof Mage)
                {
                    event.setDamage(event.getDamage() + 10);
                }
            }
        }

        if (currentPlayer != null)
        {
            event.setDamage(functions.ScaleDamage(currentPlayer.getLevel(),
                    event.getDamage(), 8));
        }

        if (event.getEntity() instanceof LivingEntity)
        {
            if (ScaleMobHealth(currentPlayer, (LivingEntity) event.getEntity(),
                    event.getDamage()))
            {
                event.setDamage(0);
            }
        }
    }

    @SuppressWarnings("deprecation")
    public boolean ScaleMobHealth(Player player, LivingEntity mobToScale,
            double DamageDone)
    {
        if (player != null && mobToScale != null)
        {
            int heathToscaleTo = (int) (functions.MobMaxHealth(mobToScale) + (player
                    .getLevel() * 1.2));

            if (heathToscaleTo > 380)
            {
                heathToscaleTo = 380;
            }

            // if (mobToScale.getMaxHealth() < heathToscaleTo)
            // {

            Damageable dmobToScale = (Damageable) mobToScale;

            double CurrentHealthPercent = functions.entityCurrentHealthPercent(
                    dmobToScale.getHealth(), dmobToScale.getMaxHealth());

            double damageToDo = (heathToscaleTo * (CurrentHealthPercent))
                    - DamageDone;

            dmobToScale.setMaxHealth(heathToscaleTo);

            if (damageToDo < 0)
            {
                damageToDo = 0;
            }

            mobToScale.setHealth(damageToDo);
            // mobToScale.damage(damageToDo);
            // new MobOutOfCombatTimer(mobToScale).runTaskTimer(plugin,
            // functions.SecondsToTicks(15), 0);
            return true;
            // }
        }
        return false;
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

}