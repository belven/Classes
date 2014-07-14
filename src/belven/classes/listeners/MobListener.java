package belven.classes.listeners;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import resources.functions;
import belven.arena.blocks.ArenaBlock;
import belven.classes.ClassManager;

public class MobListener implements Listener
{
    private final ClassManager plugin;

    public ItemStack l_ChestPlate = new ItemStack(Material.LEATHER_CHESTPLATE);
    public ItemStack l_Leggings = new ItemStack(Material.LEATHER_LEGGINGS);
    public ItemStack l_Helmet = new ItemStack(Material.LEATHER_HELMET);
    public ItemStack l_Boots = new ItemStack(Material.LEATHER_BOOTS);

    public HashMap<String, String> CurrentPlayerClasses = new HashMap<String, String>();

    Random randomGenerator = new Random();

    public MobListener(ClassManager instance)
    {
        plugin = instance;
    }

    @EventHandler
    public void onTemp(ProjectileLaunchEvent event)
    {
        if (event.getEntity().getType() == EntityType.FIREBALL)
        {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDeathEvent(EntityDeathEvent event)
    {
        Entity currentEntity = event.getEntity();

        int currentRand = randomGenerator.nextInt(2);

        if (currentEntity.hasMetadata("ArenaMob") && currentRand != 0)
        {
            List<MetadataValue> currentMetaData = currentEntity
                    .getMetadata("ArenaMob");

            if (currentMetaData.size() == 0)
            {
                return;
            }

            Iterator<ItemStack> drops = event.getDrops().iterator();

            while (drops.hasNext())
            {
                ItemStack is = drops.next();
                if (is.getMaxStackSize() == 1)
                {
                    drops.remove();
                }
            }

            List<String> arena = Arrays.asList(currentMetaData.get(0)
                    .asString().split(" "));

            ArenaBlock currentArena = this.plugin.arenas
                    .getArenaBlock((String) arena.get(0));

            if (currentArena == null)
            {
                return;
            }

            for (Player p : currentArena.arenaPlayers)
            {
                if (plugin.CurrentPlayerClasses.containsKey(p))
                {
                    String className = plugin.CurrentPlayerClasses.get(p)
                            .getClassName().toLowerCase();

                    if (className != null)
                    {
                        switch (className)
                        {
                        case "healer":
                            SetHealerDrops(p);
                            break;
                        case "warrior":
                            SetWarriorDrops(p);
                            break;
                        case "berserker":
                            SetBerserkerDrops(p);
                            break;
                        case "daemon":
                            SetDaemonDrops(p);
                            break;
                        case "priest":
                            SetPriestDrops(p);
                            break;
                        case "mage":
                            SetMageDrops(p);
                            break;
                        case "archer":
                            SetArcherDrops(p);
                            break;
                        case "assassin":
                            SetAssassinDrops(p);
                            break;
                        }
                    }
                }
            }
        }
    }

    private void SetPriestDrops(Player p)
    {
        if (!functions.deosPlayersInventoryContainAtLeast(p,
                Material.GLOWSTONE_DUST, 64))
        {
            p.getInventory().addItem(new ItemStack(Material.GLOWSTONE_DUST, 1));
        }

        SetHealerDrops(p);

    }

    private void SetDaemonDrops(Player p)
    {
        if (!functions.deosPlayersInventoryContainAtLeast(p,
                Material.FIREWORK_CHARGE, 64))
        {
            p.getInventory()
                    .addItem(new ItemStack(Material.FIREWORK_CHARGE, 1));
        }

        SetBerserkerDrops(p);
    }

    private void SetBerserkerDrops(Player currentPlayer)
    {
        if (!functions.deosPlayersInventoryContainAtLeast(currentPlayer,
                Material.STONE_SWORD, 1))
        {
            if (currentPlayer.getItemInHand() == null)
            {
                currentPlayer
                        .setItemInHand(new ItemStack(Material.STONE_SWORD));
            }
            else
            {
                currentPlayer.getInventory().addItem(
                        new ItemStack(Material.STONE_SWORD));
            }
        }

        if (!functions.deosPlayersInventoryContainAtLeast(currentPlayer,
                Material.STRING, 61))
        {
            currentPlayer.getInventory().addItem(
                    new ItemStack(Material.STRING, 4));
        }

    }

    private void SetWarriorDrops(Player currentPlayer)
    {
        int currentRand = randomGenerator.nextInt(99);

        if (!currentPlayer.getInventory().containsAtLeast(
                new ItemStack(Material.BREAD, 1), 64))
        {
            currentPlayer.getInventory().addItem(
                    new ItemStack(Material.BREAD, 1));
        }

        if (currentRand > 10 && currentRand < 40
                && !currentPlayer.getInventory().contains(Material.STONE_SWORD))
        {
            currentPlayer.getInventory().addItem(
                    new ItemStack(Material.STONE_SWORD, 1));
        }
        else if (currentRand > 40
                && currentRand < 50
                && !currentPlayer.getInventory().contains(
                        new ItemStack(new Potion(PotionType.STRENGTH, 2)
                                .toItemStack(1))))
        {
            currentPlayer.getInventory().addItem(
                    new ItemStack(new Potion(PotionType.STRENGTH, 2)
                            .toItemStack(1)));
        }

        if (!currentPlayer.getInventory().contains(Material.IRON_BOOTS)
                && currentPlayer.getInventory().getBoots() == null)
        {
            currentPlayer.getInventory().setBoots(
                    new ItemStack(Material.IRON_BOOTS, 1));
            return;
        }
        else if (!currentPlayer.getInventory().contains(Material.IRON_HELMET)
                && currentPlayer.getInventory().getHelmet() == null)
        {
            currentPlayer.getInventory().setHelmet(
                    new ItemStack(Material.IRON_HELMET, 1));
            return;
        }
        else if (!currentPlayer.getInventory().contains(Material.IRON_LEGGINGS)
                && currentPlayer.getInventory().getLeggings() == null)
        {
            currentPlayer.getInventory().setLeggings(
                    new ItemStack(Material.IRON_LEGGINGS, 1));
            return;
        }
        else if (!currentPlayer.getInventory().contains(
                Material.IRON_CHESTPLATE)
                && currentPlayer.getInventory().getChestplate() == null)
        {
            currentPlayer.getInventory().setChestplate(
                    new ItemStack(Material.IRON_CHESTPLATE, 1));
            return;
        }

    }

    private void SetAssassinDrops(Player currentPlayer)
    {
        int currentRand = randomGenerator.nextInt(99);

        if (!currentPlayer.getInventory().containsAtLeast(
                new ItemStack(Material.ARROW, 1), 61))
        {
            currentPlayer.getInventory().addItem(
                    new ItemStack(Material.ARROW, 3));
        }

        if (currentRand > 20 && currentRand < 30
                && !currentPlayer.getInventory().contains(Material.BOW))
        {
            currentPlayer.getInventory()
                    .addItem(new ItemStack(Material.BOW, 1));
        }
        else if (!currentPlayer.getInventory().contains(
                new Potion(PotionType.SPEED, 2).toItemStack(1))
                && currentRand > 50 && currentRand < 60)
        {
            currentPlayer.getInventory().addItem(
                    new ItemStack(new Potion(PotionType.SPEED, 2)
                            .toItemStack(1)));
        }
        else if (!currentPlayer.getInventory().contains(
                new ItemStack(new Potion(PotionType.NIGHT_VISION, 1)
                        .toItemStack(1)))
                && currentRand > 60 && currentRand < 70)
        {
            currentPlayer.getInventory().addItem(
                    new ItemStack(new Potion(PotionType.NIGHT_VISION, 1)
                            .toItemStack(1)));
        }

        if (!currentPlayer.getInventory().contains(Material.IRON_SWORD))
        {
            currentPlayer.getInventory().addItem(
                    new ItemStack(Material.IRON_SWORD));
        }
    }

    private void SetHealerDrops(Player currentPlayer)
    {
        int currentRand = randomGenerator.nextInt(99);

        if (!currentPlayer.getInventory().containsAtLeast(
                new ItemStack(Material.LAPIS_BLOCK, 1), 60))
        {
            currentPlayer.getInventory().addItem(
                    new ItemStack(Material.LAPIS_BLOCK, 4));
        }

        if (currentRand > 0 && currentRand < 20
                && !currentPlayer.getInventory().contains(Material.WOOD_SWORD))
        {
            currentPlayer.getInventory().addItem(
                    new ItemStack(Material.WOOD_SWORD, 1));
        }
        else if (currentRand > 20 && currentRand < 40)
        {
            if (!currentPlayer.getInventory().containsAtLeast(
                    new ItemStack(Material.PAPER, 1), 63))
            {
                currentPlayer.getInventory().addItem(
                        new ItemStack(Material.PAPER, 1));
            }

            if (!currentPlayer.getInventory().containsAtLeast(
                    new ItemStack(Material.STICK, 1), 63))
            {
                currentPlayer.getInventory().addItem(
                        new ItemStack(Material.STICK, 1));
            }
        }
        else if (!currentPlayer.getInventory().contains(
                new Potion(PotionType.REGEN, 2).toItemStack(1))
                && currentRand > 40 && currentRand < 50)
        {
            currentPlayer.getInventory().addItem(
                    new ItemStack(new Potion(PotionType.REGEN, 2)
                            .toItemStack(1)));
        }
        else if (currentRand > 50 && currentRand < 60)
        {
            if (!currentPlayer.getInventory().contains(Material.LEATHER_BOOTS)
                    && currentPlayer.getInventory().getBoots() == null)
            {
                currentPlayer.getInventory().setBoots(
                        new ItemStack(Material.LEATHER_BOOTS, 1));
            }
            else if (!currentPlayer.getInventory().contains(
                    Material.LEATHER_HELMET)
                    && currentPlayer.getInventory().getHelmet() == null)
            {
                currentPlayer.getInventory().setHelmet(
                        new ItemStack(Material.LEATHER_HELMET, 1));
            }
            else if (!currentPlayer.getInventory().contains(
                    Material.LEATHER_LEGGINGS)
                    && currentPlayer.getInventory().getLeggings() == null)
            {
                currentPlayer.getInventory().setLeggings(
                        new ItemStack(Material.LEATHER_LEGGINGS, 1));
            }
            else if (!currentPlayer.getInventory().contains(
                    Material.LEATHER_CHESTPLATE)
                    && currentPlayer.getInventory().getChestplate() == null)
            {
                currentPlayer.getInventory().setChestplate(
                        new ItemStack(Material.LEATHER_CHESTPLATE, 1));
            }
        }
    }

    private void SetMageDrops(Player currentPlayer)
    {
        int currentRand = randomGenerator.nextInt(99);

        if (!currentPlayer.getInventory().containsAtLeast(
                new ItemStack(Material.LAPIS_BLOCK), 54))
        {
            currentPlayer.getInventory().addItem(
                    new ItemStack(Material.LAPIS_BLOCK, 10));
        }

        if (currentRand > 10 && currentRand < 40)
        {
            if (!currentPlayer.getInventory().containsAtLeast(
                    new ItemStack(Material.FEATHER, 1), 62))
            {
                currentPlayer.getInventory().addItem(
                        new ItemStack(Material.FEATHER, 2));
            }
        }
        else if (!currentPlayer.getInventory().contains(
                (new Potion(PotionType.FIRE_RESISTANCE, 2).toItemStack(1)))
                && currentRand > 40 && currentRand < 50)
        {
            currentPlayer.getInventory().addItem(
                    new ItemStack(new Potion(PotionType.FIRE_RESISTANCE, 2)
                            .toItemStack(1)));
        }
        else if (currentRand > 50 && currentRand < 60)
        {
            if (!currentPlayer.getInventory().contains(Material.LEATHER_BOOTS)
                    && currentPlayer.getInventory().getBoots() == null)
            {
                currentPlayer.getInventory().setBoots(l_Boots);
            }
            else if (!currentPlayer.getInventory().contains(
                    Material.LEATHER_HELMET)
                    && currentPlayer.getInventory().getHelmet() == null)
            {
                currentPlayer.getInventory().setHelmet(l_Helmet);
            }
            else if (!currentPlayer.getInventory().contains(
                    Material.LEATHER_LEGGINGS)
                    && currentPlayer.getInventory().getLeggings() == null)
            {
                currentPlayer.getInventory().setLeggings(l_Leggings);
            }
            else if (!currentPlayer.getInventory().contains(
                    Material.LEATHER_CHESTPLATE)
                    && currentPlayer.getInventory().getChestplate() == null)
            {
                currentPlayer.getInventory().setChestplate(l_ChestPlate);
            }
        }
    }

    private void SetArcherDrops(Player currentPlayer)
    {
        int currentRand = randomGenerator.nextInt(99);

        if (!currentPlayer.getInventory().containsAtLeast(
                new ItemStack(Material.ARROW, 1), 64))
        {
            currentPlayer.getInventory().addItem(
                    new ItemStack(Material.ARROW, 10));
        }

        if (!currentPlayer.getInventory().contains(Material.BOW))
        {
            currentPlayer.getInventory()
                    .addItem(new ItemStack(Material.BOW, 1));
        }

        if (currentRand > 30 && currentRand < 40)
        {
            ItemStack redwool = new Wool(DyeColor.RED).toItemStack(2);

            if (!currentPlayer.getInventory().containsAtLeast(
                    new ItemStack(Material.WOOL), 62))
            {
                currentPlayer.getInventory().addItem(redwool);
            }
        }
        else if (currentRand > 50 && currentRand < 70)
        {
            if (!currentPlayer.getInventory().containsAtLeast(
                    new ItemStack(Material.SNOW_BALL), 9))
            {
                currentPlayer.getInventory().addItem(
                        new ItemStack(Material.SNOW_BALL, 8));
            }
        }
        else if (currentRand > 70 && currentRand < 100)
        {
            if (!currentPlayer.getInventory().contains(Material.LEATHER_BOOTS)
                    && currentPlayer.getInventory().getBoots() == null)
            {
                currentPlayer.getInventory().setBoots(l_Boots);
            }
            else if (!currentPlayer.getInventory().contains(
                    Material.LEATHER_HELMET)
                    && currentPlayer.getInventory().getHelmet() == null)
            {
                currentPlayer.getInventory().setHelmet(l_Helmet);
            }
            else if (!currentPlayer.getInventory().contains(
                    Material.LEATHER_LEGGINGS)
                    && currentPlayer.getInventory().getLeggings() == null)
            {
                currentPlayer.getInventory().setLeggings(l_Leggings);
            }
            else if (!currentPlayer.getInventory().contains(
                    Material.LEATHER_CHESTPLATE)
                    && currentPlayer.getInventory().getChestplate() == null)
            {
                currentPlayer.getInventory().setChestplate(l_ChestPlate);
            }
        }
    }
}