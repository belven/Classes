package belven.classes;

import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Warrior extends Class
{
    public Warrior(Player currentPlayer, ClassManager instance)
    {
        plugin = instance;
        classOwner = currentPlayer;
    }

    public void TakeDamage(EntityDamageByEntityEvent event, Player damagedPlayer)
    {
        event.setDamage(event.getDamage() - 1);

        if (damagedPlayer.getHealth() <= 5)
        {
            damagedPlayer.addPotionEffect(new PotionEffect(
                    PotionEffectType.ABSORPTION, SecondsToTicks(90),
                    damagedPlayer.getLevel() + 5), false);
        }

    }

    public void PerformAbility(Player currentPlayer)
    {
        Entity[] entitiesToDamage = getNearbyEntities(
                currentPlayer.getLocation(), 15);

        if (classOwner.getItemInHand().getType() == Material.SKULL_ITEM)
        {
            for (int i = 0; i < entitiesToDamage.length; i++)
            {
                if (entitiesToDamage[i] != null
                        && entitiesToDamage[i].getType() != EntityType.PLAYER)
                {
                    Taunt(entitiesToDamage[i]);
                }
            }
        }
    }

    public void PerformAbility()
    {

    }

    public void Taunt(Entity targetEntity)
    {
        switch (targetEntity.getType().toString().toLowerCase())
        {
        case "zombie":
            Zombie currentZombie = (Zombie) targetEntity;
            currentZombie.setTarget(classOwner);
            break;
        case "skeleton":
            Skeleton currentSkeleton = (Skeleton) targetEntity;
            currentSkeleton.setTarget(classOwner);
            break;
        }
    }

    @SuppressWarnings("deprecation")
    public void PerformAbility(Entity currentEntity)
    {
        Entity[] entitiesToDamage = getNearbyEntities(
                currentEntity.getLocation(), 15);
        
        if (classOwner.getItemInHand().getType() == Material.SKULL_ITEM)
        {
            for (int i = 0; i < entitiesToDamage.length; i++)
            {
                if (entitiesToDamage[i] != null
                        && entitiesToDamage[i].getType() != EntityType.PLAYER)
                {
                    Taunt(entitiesToDamage[i]);
                }
            }
            classOwner.getItemInHand().setAmount(classOwner.getItemInHand().getAmount() - 1);
            classOwner.updateInventory();
        }
    }

    public static Entity[] getNearbyEntities(Location l, int radius)
    {
        int chunkRadius = radius < 16 ? 1 : (radius - (radius % 16)) / 16;
        HashSet<Entity> radiusEntities = new HashSet<Entity>();

        for (int chX = 0 - chunkRadius; chX <= chunkRadius; chX++)
        {
            for (int chZ = 0 - chunkRadius; chZ <= chunkRadius; chZ++)
            {
                int x = (int) l.getX(), y = (int) l.getY(), z = (int) l.getZ();

                for (Entity e : new Location(l.getWorld(), x + (chX * 16), y, z
                        + (chZ * 16)).getChunk().getEntities())
                {
                    if (e.getLocation().distance(l) <= radius
                            && e.getLocation().getBlock() != l.getBlock())
                        radiusEntities.add(e);
                }
            }
        }

        return radiusEntities.toArray(new Entity[radiusEntities.size()]);
    }

    public int SecondsToTicks(int seconds)
    {
        return (seconds * 20);

    }

}
