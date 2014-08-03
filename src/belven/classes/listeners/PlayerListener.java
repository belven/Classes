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
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.metadata.MetadataValue;

import resources.EntityFunctions;
import resources.Functions;
import resources.MaterialFunctions;
import resources.PlayerExtended;
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
        plugin.PlayersE.put(event.getPlayer(),
                new PlayerExtended(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerLoginEvent(PlayerQuitEvent event)
    {
        plugin.PlayersE.remove(event.getPlayer());
    }

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event)
    {
<<<<<<< HEAD
        Class pClass = plugin.GetClass(event.getPlayer());
=======
        Class pClass = plugin.CurrentPlayerClasses.get(event.getPlayer());
>>>>>>> origin/master
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
<<<<<<< HEAD
        plugin.GetClass(event.GetPlayer()).AbilityUsed(event.GetAbility());
=======
        plugin.CurrentPlayerClasses.get(event.GetPlayer()).AbilityUsed(
                event.GetAbility());
>>>>>>> origin/master
    }

    @EventHandler
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event)
    {
        Player currentPlayer = event.getPlayer();
        Entity currentEntity = event.getRightClicked();

<<<<<<< HEAD
        if (plugin.GetClass(event.getPlayer()).CanCast)
=======
        if (plugin.CurrentPlayerClasses.get(event.getPlayer()).CanCast)
>>>>>>> origin/master
        {
            new AbilityDelay(event.getPlayer(), plugin).runTaskLater(plugin,
                    Functions.SecondsToTicks(1));

<<<<<<< HEAD
            plugin.GetClass(currentPlayer).RightClickEntity(currentEntity);
=======
            plugin.CurrentPlayerClasses.get(currentPlayer).RightClickEntity(
                    currentEntity);
>>>>>>> origin/master
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
                        && !MaterialFunctions.isNotInteractiveBlock(event
                                .getItem().getType()))
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

<<<<<<< HEAD
            if (plugin.GetClass(currentPlayer).CanCast)
=======
            if (plugin.CurrentPlayerClasses.get(currentPlayer).CanCast)
>>>>>>> origin/master
            {
                new AbilityDelay(currentPlayer, plugin).runTaskLater(plugin,
                        Functions.SecondsToTicks(1));

<<<<<<< HEAD
                plugin.GetClass(currentPlayer).SelfCast(currentPlayer);
=======
                plugin.CurrentPlayerClasses.get(currentPlayer).SelfCast(
                        currentPlayer);
>>>>>>> origin/master
            }
        }
    }

    @EventHandler
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event)
    {
<<<<<<< HEAD
        if (plugin.GetClass(event.getPlayer()).CanCast)
=======
        if (plugin.CurrentPlayerClasses.get(event.getPlayer()).CanCast)
>>>>>>> origin/master
        {
            new AbilityDelay(event.getPlayer(), plugin).runTaskLater(plugin,
                    Functions.SecondsToTicks(1));

<<<<<<< HEAD
            plugin.GetClass(event.getPlayer()).ToggleSneakEvent(event);
=======
            plugin.CurrentPlayerClasses.get(event.getPlayer())
                    .ToggleSneakEvent(event);
>>>>>>> origin/master
        }
    }

    @EventHandler
    public void onPlayerVelocityEvent(PlayerVelocityEvent event)
    {
<<<<<<< HEAD
        if (plugin.GetClass(event.getPlayer()) instanceof Assassin)
        {
            event.setCancelled(true);
        }
        else if (plugin.GetClass(event.getPlayer()) instanceof Archer
=======
        if (plugin.CurrentPlayerClasses.get(event.getPlayer()) instanceof Assassin)
        {
            event.setCancelled(true);
        }
        else if (plugin.CurrentPlayerClasses.get(event.getPlayer()) instanceof Archer
>>>>>>> origin/master
                && event.getPlayer().getItemInHand().getType() == Material.BOW)
        {
            event.setCancelled(true);
        }
<<<<<<< HEAD
        else if (plugin.GetClass(event.getPlayer()) instanceof Daemon
=======
        else if (plugin.CurrentPlayerClasses.get(event.getPlayer()) instanceof Daemon
>>>>>>> origin/master
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

<<<<<<< HEAD
            plugin.GetClass(damagedPlayer).SelfTakenDamage(event);
=======
            plugin.CurrentPlayerClasses.get(damagedPlayer).SelfTakenDamage(
                    event);
>>>>>>> origin/master
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

<<<<<<< HEAD
            plugin.GetClass(damagedPlayer).SelfTakenDamage(event);
=======
            plugin.CurrentPlayerClasses.get(damagedPlayer).SelfTakenDamage(
                    event);
>>>>>>> origin/master
        }
    }

    public void MobTakenDamage(EntityDamageByEntityEvent event)
    {
<<<<<<< HEAD
        LivingEntity le = Functions.GetDamager(event);
=======
        LivingEntity le = EntityFunctions.GetDamager(event);
>>>>>>> origin/master

        if (le != null && le.getType() == EntityType.PLAYER)
        {
            Player currentPlayer = (Player) le;

            addPlayerToArena(currentPlayer, le);

<<<<<<< HEAD
            plugin.GetClass(currentPlayer).SelfDamageOther(event);
=======
            if (plugin.CurrentPlayerClasses.containsKey(le))
            {
                plugin.CurrentPlayerClasses.get(currentPlayer).SelfDamageOther(
                        event);
            }
>>>>>>> origin/master

            // String damage = String.valueOf(event.getDamage());
            // currentPlayer.sendMessage(damage);
        }
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
<<<<<<< HEAD
            double heathToscaleTo = Functions.MobMaxHealth(mobToScale)
=======
            double heathToscaleTo = EntityFunctions.MobMaxHealth(mobToScale)
>>>>>>> origin/master
                    + (player.getLevel() * 1.2);

            if (heathToscaleTo > 380)
            {
                heathToscaleTo = 380;
            }

            Damageable dmobToScale = (Damageable) mobToScale;

            double CurrentHealthPercent = EntityFunctions
                    .entityCurrentHealthPercent(dmobToScale.getHealth(),
                            dmobToScale.getMaxHealth());

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