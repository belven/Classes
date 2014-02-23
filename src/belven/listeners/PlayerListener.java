package belven.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
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
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

import belven.classes.Archer;
import belven.classes.Assassin;
import belven.classes.ClassManager;
import belven.classes.Healer;
import belven.classes.Mage;
import belven.classes.Warrior;
import belven.timedevents.AbilityDelay;
import belven.timedevents.MobOutOfCombatTimer;

public class PlayerListener implements Listener
{
    private final ClassManager plugin;

    Location arenaWarp;
    ItemStack[] currentPlayerInventory;

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
    public void onPlayerDeathEvent(PlayerDeathEvent event)
    {
        Player currentPlayer = (Player) event.getEntity();

        if (plugin.currentArenaBlocks.size() > 0)
        {
            for (int i = 0; i < plugin.currentArenaBlocks.size(); i++)
            {
                if (plugin.currentArenaBlocks.get(i).isActive
                        && plugin.currentArenaBlocks.get(i).playersString
                                .contains(currentPlayer.getName()))
                {
                    event.setNewLevel(currentPlayer.getLevel() / 2);

                    currentPlayerInventory = currentPlayer.getInventory()
                            .getContents();

                    arenaWarp = plugin.currentArenaBlocks.get(i).arenaWarp
                            .getLocation();
                }
            }
        }
    }

    @EventHandler
    public void onPlayerRespawnEvent(PlayerRespawnEvent event)
    {
        Player currentPlayer = event.getPlayer();

        if (arenaWarp != null)
        {
            event.setRespawnLocation(arenaWarp);
            currentPlayer.getInventory().setContents(currentPlayerInventory);
            arenaWarp = null;
            currentPlayerInventory = null;
        }
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

    @SuppressWarnings(
    { "unused" })
    @EventHandler
    public void OnPlayerExpChangeEvent(PlayerExpChangeEvent event)
    {
        Inventory currentInventory = event.getPlayer().getInventory();
        List<String> lore = new ArrayList<String>();
        belven.classes.Class currentClass = plugin.CurrentPlayerClasses
                .get(event.getPlayer());

        for (int i = 0; i < currentInventory.getSize(); i++)
        {
            ItemStack currentItemStack = currentInventory.getItem(i);

            if (currentItemStack != null
                    && currentItemStack.getMaxStackSize() == 1)
            {
                ItemMeta currentItemMeta = currentItemStack.getItemMeta();
                if (currentItemMeta != null)
                {
                    // lore.add("Actual damage: " +
                    // ScaleDamage(event.getPlayer().getLevel(), );
                    // currentItemMeta.setLore(lore);
                    // currentItemStack.setItemMeta(currentItemMeta);
                    // event.getPlayer().updateInventory();
                }

            }
        }
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

                if (plugin.CurrentPlayerClasses.get(currentPlayer) instanceof Mage)
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