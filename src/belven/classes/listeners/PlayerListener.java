package belven.classes.listeners;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.metadata.MetadataValue;

import resources.EntityFunctions;
import resources.Functions;
import resources.MaterialFunctions;
import belven.classes.Archer;
import belven.classes.Assassin;
import belven.classes.Class;
import belven.classes.ClassManager;
import belven.classes.Daemon;
import belven.classes.events.AbilityUsed;
import belven.classes.timedevents.AbilityDelay;

public class PlayerListener implements Listener
{
    private final ClassManager plugin;

    public PlayerListener(ClassManager instance)
    {
        plugin = instance;
    }

    @EventHandler
    public void onPlayerInteractEventSigns(PlayerInteractEvent event)
    {
        Sign currentSign;
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            if (event.getClickedBlock().getType() == Material.SIGN)
            {
                currentSign = (Sign) event.getClickedBlock();

                if (currentSign.getLine(0) != null
                        && currentSign.getLine(0).contentEquals("[Class]"))
                {
                    plugin.SetClass(event.getPlayer(), currentSign.getLine(1));
                }
            }
            else if (event.getClickedBlock().getType() == Material.WALL_SIGN)
            {
                currentSign = (Sign) event.getClickedBlock().getState();

                if (currentSign.getLine(0) != null
                        && currentSign.getLine(0).contentEquals("[Class]"))
                {
                    plugin.SetClass(event.getPlayer(), currentSign.getLine(1));
                }
            }
        }
    }

    @EventHandler
    public void onPlayerLoginEvent(PlayerLoginEvent event)
    {
        plugin.AddClassToPlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event)
    {
        Class pClass = plugin.CurrentPlayerClasses.get(event.getPlayer());
        Block currentBlock = event.getTo().getBlock();
        org.bukkit.Location upLoc = event.getTo();
        upLoc.setY(upLoc.getY() + 1);

        event.getPlayer().setWalkSpeed(.2F);
        // Block blockBelow = currentBlock.getRelative(BlockFace.DOWN);

        if (pClass.getClassName() == "Archer")
        {
            if (currentBlock.getType() == Material.WEB)
            {
                event.getPlayer().setWalkSpeed(1F);
            }
        }
        // else if (pClass.getClassName() == "Daemon")
        // {
        // if (currentBlock.getType() == Material.LAVA)
        // {
        // event.setTo(upLoc);
        // }
        // }
        // else if (pClass.getClassName() == "Assassin")
        // {
        // if (currentBlock.getType() == Material.WATER)
        // {
        // event.setTo(upLoc);
        // }
        // }
    }

    @EventHandler
    public void onAbilityUsed(AbilityUsed event)
    {
        plugin.CurrentPlayerClasses.get(event.GetPlayer()).AbilityUsed(
                event.GetAbility());
    }

    @EventHandler
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event)
    {
        Player currentPlayer = event.getPlayer();
        Entity currentEntity = event.getRightClicked();

        if (plugin.CurrentPlayerClasses.get(event.getPlayer()).CanCast)
        {
            new AbilityDelay(event.getPlayer(), plugin).runTaskLater(plugin,
                    Functions.SecondsToTicks(1));

            plugin.CurrentPlayerClasses.get(currentPlayer).RightClickEntity(
                    currentEntity);
        }
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event)
    {
        if (event.getAction() == Action.RIGHT_CLICK_AIR
                || event.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            Player currentPlayer = event.getPlayer();

            if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
            {
                Material blockMaterial = event.getClickedBlock().getType();
                if (!MaterialFunctions.isNotInteractiveBlock(blockMaterial))
                {
                    return;
                }
                else if (event.getItem() != null
                        && !MaterialFunctions.isNotInteractiveBlock(event.getItem()
                                .getType()))
                {
                    return;
                }
            }
            else if (event.getItem() != null
                    && !MaterialFunctions.isNotInteractiveBlock(event.getItem()
                            .getType()))
            {
                return;
            }

            if (plugin.CurrentPlayerClasses.get(currentPlayer).CanCast)
            {
                new AbilityDelay(currentPlayer, plugin).runTaskLater(plugin,
                        Functions.SecondsToTicks(1));

                plugin.CurrentPlayerClasses.get(currentPlayer).SelfCast(
                        currentPlayer);
            }
        }
    }

    @EventHandler
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event)
    {
        if (plugin.CurrentPlayerClasses.get(event.getPlayer()).CanCast)
        {
            new AbilityDelay(event.getPlayer(), plugin).runTaskLater(plugin,
                    Functions.SecondsToTicks(1));

            plugin.CurrentPlayerClasses.get(event.getPlayer())
                    .ToggleSneakEvent(event);
        }
    }

    @EventHandler
    public void onPlayerVelocityEvent(PlayerVelocityEvent event)
    {
        if (plugin.CurrentPlayerClasses.get(event.getPlayer()) instanceof Assassin)
        {
            event.setCancelled(true);
        }
        else if (plugin.CurrentPlayerClasses.get(event.getPlayer()) instanceof Archer
                && event.getPlayer().getItemInHand().getType() == Material.BOW)
        {
            event.setCancelled(true);
        }
        else if (plugin.CurrentPlayerClasses.get(event.getPlayer()) instanceof Daemon
                && event.getPlayer().getFireTicks() > 0)
        {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
    {
        if (event.getEntityType() == EntityType.PLAYER)
        {
            Player damagedPlayer = (Player) event.getEntity();

            plugin.CurrentPlayerClasses.get(damagedPlayer).SelfTakenDamage(
                    event);
        }
        else
        {
            MobTakenDamage(event);
        }
    }

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent event)
    {
        if (event.getEntityType() == EntityType.PLAYER)
        {
            Player damagedPlayer = (Player) event.getEntity();

            plugin.CurrentPlayerClasses.get(damagedPlayer).SelfTakenDamage(
                    event);
        }
    }

    public void MobTakenDamage(EntityDamageByEntityEvent event)
    {
        LivingEntity le = Functions.GetDamager(event);

        if (le != null && le.getType() == EntityType.PLAYER)
        {
            Player currentPlayer = (Player) le;

            addPlayerToArena(currentPlayer, le);

            if (plugin.CurrentPlayerClasses.containsKey(le))
            {
                plugin.CurrentPlayerClasses.get(currentPlayer).SelfDamageOther(
                        event);
            }

            // String damage = String.valueOf(event.getDamage());
            // currentPlayer.sendMessage(damage);
        }

        // if (damagerEntity.getType() == EntityType.PLAYER)
        // {
        // currentPlayer = (Player) damagerEntity;
        // // addPlayerToArena(currentPlayer, event.getEntity());
        //
        // plugin.CurrentPlayerClasses.get(currentPlayer).SelfDamageOther(
        // event);
        // }
        // else if (damagerEntity.getType() == EntityType.ARROW)
        // {
        // Arrow currentArrow = (Arrow) damagerEntity;
        //
        // ProjectileSource ps = currentArrow.getShooter();
        //
        // if (ps instanceof Player)
        // {
        // currentPlayer = (Player) ps;
        // // addPlayerToArena(currentPlayer, event.getEntity());
        //
        // plugin.CurrentPlayerClasses.get(currentPlayer).SelfDamageOther(
        // event);
        // }
        //
        // }
        // else if (damagerEntity.getType() == EntityType.FIREBALL)
        // {
        // Projectile currentFireball = (Projectile) damagerEntity;
        // ProjectileSource ps = currentFireball.getShooter();
        //
        // if (currentFireball instanceof Fireball && ps instanceof Player)
        // {
        // currentPlayer = (Player) ps;
        //
        // if (plugin.CurrentPlayerClasses.get(currentPlayer) instanceof Mage)
        // {
        // event.setDamage(event.getDamage() + 10);
        // }
        // }
        // }

        // if (currentPlayer != null)
        // {
        // event.setDamage(Functions.ScaleDamage(currentPlayer.getLevel(),
        // event.getDamage(), 8));
        // }
        //
        // if (event.getEntity() instanceof LivingEntity)
        // {
        // if (ScaleMobHealth(currentPlayer, (LivingEntity) event.getEntity(),
        // event.getDamage()))
        // {
        // event.setDamage(0.0);
        // }
        // }
    }

    public void addPlayerToArena(Player p, Entity e)
    {
        if (e.hasMetadata("ArenaMob") && !plugin.arenas.IsPlayerInArena(p))
        {
            List<MetadataValue> currentMetaData = e.getMetadata("ArenaMob");

            if (currentMetaData.size() == 0)
            {
                return;
            }

            List<String> arena = Arrays.asList(currentMetaData.get(0)
                    .asString().split(" "));

            plugin.arenas.WarpToArena(p, arena.get(0));
        }
    }

    public boolean ScaleMobHealth(Player player, LivingEntity mobToScale,
            double DamageDone)
    {
        if (player != null && mobToScale != null)
        {
            double heathToscaleTo = Functions.MobMaxHealth(mobToScale)
                    + (player.getLevel() * 1.2);

            if (heathToscaleTo > 380)
            {
                heathToscaleTo = 380;
            }

            Damageable dmobToScale = (Damageable) mobToScale;

            double CurrentHealthPercent = EntityFunctions.entityCurrentHealthPercent(
                    dmobToScale.getHealth(), dmobToScale.getMaxHealth());

            double damageToDo = (heathToscaleTo * (CurrentHealthPercent))
                    - DamageDone;

            dmobToScale.setMaxHealth(heathToscaleTo);

            if (damageToDo < 0)
            {
                damageToDo = 0;
            }

            mobToScale.setHealth(damageToDo);
            return true;
        }
        return false;
    }

}