package belven.classes.listeners;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.MetadataValue;

import belven.arena.blocks.ArenaBlock;
import belven.classes.ClassManager;
import belvens.classes.resources.ClassDrop;
import belvens.classes.resources.functions;

public class MobListener implements Listener
{
    private final ClassManager plugin;

    public List<ClassDrop> classDrops = new ArrayList<ClassDrop>();

    Random randomGenerator = new Random();

    public MobListener(ClassManager instance)
    {
        plugin = instance;
    }

    @EventHandler
    public void onProjectileLaunchEvent(ProjectileLaunchEvent event)
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

        if (currentEntity.hasMetadata("ArenaMob"))
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

            String arena = currentMetaData.get(0).asString();
            ArenaBlock currentArena = this.plugin.arenas.getArenaBlock(arena);

            if (currentArena == null)
            {
                return;
            }

            for (Player p : currentArena.arenaPlayers)
            {
                GiveClassDrops(p);
            }
        }
    }

    private void GiveClassDrops(Player p)
    {
        int ran = randomGenerator.nextInt(99);
        PlayerInventory pInv = p.getInventory();

        if (plugin.CurrentPlayerClasses.containsKey(p))
        {
            belven.classes.Class playerClass = plugin.CurrentPlayerClasses
                    .get(p);

            for (ClassDrop cd : playerClass.classDrops)
            {
                if (cd.alwaysGive)
                {
                    if (!cd.isArmor)
                    {
                        functions.AddToInventory(p, cd.is);
                    }
                    else
                    {
                        if (AddArmor(pInv, cd.is))
                        {
                            break;
                        }
                    }
                }
                else if (functions.isNumberBetween(ran, cd.lowChance,
                        cd.highChance))
                {
                    if (!cd.isArmor)
                    {
                        functions.AddToInventory(p, cd.is);
                    }
                    else
                    {
                        if (AddArmor(pInv, cd.is))
                        {
                            break;
                        }
                    }
                }
            }
        }
    }

    public boolean AddArmor(PlayerInventory pInv, ItemStack is)
    {
        if (is.getType().name().contains("CHEST")
                && pInv.getChestplate() == null)
        {
            pInv.setChestplate(is);
            return true;
        }
        else if (is.getType().name().contains("LEGGINGS")
                && pInv.getLeggings() == null)
        {
            pInv.setLeggings(is);
            return true;
        }
        else if (is.getType().name().contains("HELMET")
                && pInv.getHelmet() == null)
        {
            pInv.setHelmet(is);
            return true;
        }
        else if (is.getType().name().contains("BOOTS")
                && pInv.getBoots() == null)
        {
            pInv.setBoots(is);
            return true;
        }
        return false;
    }
}