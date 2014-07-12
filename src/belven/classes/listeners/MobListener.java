package belven.classes.listeners;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

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

            List<String> arena = Arrays.asList(currentMetaData.get(0)
                    .asString().split(" "));

            List<String> playerList = Arrays.asList(arena.get(1).split(","));

            if (playerList.get(0) != null)
            {
                for (String pn : playerList)
                {
                    if (plugin.CurrentPlayerClasses.containsKey(pn))
                    {
                        String className = plugin.CurrentPlayerClasses
                                .get(pn.trim()).getClassName().toLowerCase();

                        if (className != null)
                        {
                            switch (className)
                            {
                            case "healer":
                                SetHealerDrops(pn);
                                break;
                            case "warrior":
                                SetWarriorDrops(pn);
                                break;
                            case "mage":
                                SetMageDrops(pn);
                                break;
                            case "archer":
                                SetArcherDrops(pn);
                                break;
                            case "assassin":
                                SetAssassinDrops(pn);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    private void SetWarriorDrops(String pn)
    {
        int currentRand = randomGenerator.nextInt(99);

        @SuppressWarnings("deprecation")
        Player currentPlayer = plugin.getServer().getPlayer(pn);

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
        else if (currentRand > 50 && currentRand < 100)
        {
            if (!currentPlayer.getInventory().contains(Material.IRON_BOOTS)
                    && currentPlayer.getInventory().getBoots() == null)
            {
                currentPlayer.getInventory().setBoots(
                        new ItemStack(Material.IRON_BOOTS, 1));
                return;
            }
            else if (!currentPlayer.getInventory().contains(
                    Material.IRON_HELMET)
                    && currentPlayer.getInventory().getHelmet() == null)
            {
                currentPlayer.getInventory().setHelmet(
                        new ItemStack(Material.IRON_HELMET, 1));
                return;
            }
            else if (!currentPlayer.getInventory().contains(
                    Material.IRON_LEGGINGS)
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
    }

    @SuppressWarnings("deprecation")
    private void SetAssassinDrops(String pn)
    {
        int currentRand = randomGenerator.nextInt(99);
        Player currentPlayer = plugin.getServer().getPlayer(pn);

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
        else if (currentRand > 70 && currentRand < 100
                && !currentPlayer.getInventory().contains(Material.IRON_SWORD))
        {
            currentPlayer.getInventory().addItem(
                    new ItemStack(Material.IRON_SWORD, 1));
        }
    }

    @SuppressWarnings("deprecation")
    private void SetHealerDrops(String pn)
    {
        int currentRand = randomGenerator.nextInt(99);
        Player currentPlayer = plugin.getServer().getPlayer(pn);

        if (!currentPlayer.getInventory().containsAtLeast(
                new ItemStack(Material.LAPIS_BLOCK, 1), 62))
        {
            currentPlayer.getInventory().addItem(
                    new ItemStack(Material.LAPIS_BLOCK, 2));
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

    @SuppressWarnings("deprecation")
    private void SetMageDrops(String pn)
    {
        int currentRand = randomGenerator.nextInt(99);

        Player currentPlayer = plugin.getServer().getPlayer(pn);

        if (!currentPlayer.getInventory().containsAtLeast(
                new ItemStack(Material.LAPIS_BLOCK, 1), 61))
        {
            currentPlayer.getInventory().addItem(
                    new ItemStack(Material.LAPIS_BLOCK, 3));
        }

        if (currentRand > 10 && currentRand < 40)
        {
            if (!currentPlayer.getInventory().containsAtLeast(
                    new ItemStack(Material.LAPIS_BLOCK, 1), 62))
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

    @SuppressWarnings("deprecation")
    private void SetArcherDrops(String pn)
    {
        int currentRand = randomGenerator.nextInt(99);
        Player currentPlayer = plugin.getServer().getPlayer(pn);

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
        else if (currentRand > 30 && currentRand < 40)
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
                    new ItemStack(Material.SNOW_BALL), 10))
            {
                currentPlayer.getInventory().addItem(
                        new ItemStack(Material.SNOW_BALL, 6));
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