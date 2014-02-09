package belven.classes;

import java.util.HashSet;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Assassin extends Class
{
    public Entity lastEntityConfused;

    public Assassin(Player currentPlayer, ClassManager instance)
    {
        plugin = instance;
        classOwner = currentPlayer;
        this.SetAbilities();
    }

    @Override
    public void PerformAbility(Player currentPlayer)
    {
        Entity[] entitiesToDamage = getNearbyEntities(
                currentPlayer.getLocation(), 15);

        if (classOwner.getItemInHand().getType() == Material.NETHER_STAR)
        {
            for (int i = 0; i < entitiesToDamage.length; i++)
            {
                if (entitiesToDamage[i] != null
                        && entitiesToDamage[i].getType() != EntityType.PLAYER)
                {
                    Confusion(entitiesToDamage[i]);
                }
            }
        }
    }

    @Override
    public void PerformAbility(Entity currentEntity)
    {
        Entity[] entitiesToDamage = getNearbyEntities(
                currentEntity.getLocation(), 15);

        if (classOwner.getItemInHand().getType() == Material.NETHER_STAR)
        {
            for (int i = 0; i < entitiesToDamage.length; i++)
            {
                if (entitiesToDamage[i] != null
                        && entitiesToDamage[i].getType() != EntityType.PLAYER)
                {
                    Confusion(entitiesToDamage[i]);
                }
            }
        }
    }

    private void Confusion(Entity targetEntity)
    {
        switch (targetEntity.getType().toString().toLowerCase())
        {
        case "zombie":
            Zombie currentZombie = (Zombie) targetEntity;
            if (lastEntityConfused != null)
            {
                currentZombie.setTarget((LivingEntity) lastEntityConfused);
            }
            else
            {
                currentZombie.setTarget(null);
                lastEntityConfused = currentZombie;
            }
            break;
        case "skeleton":
            Skeleton currentSkeleton = (Skeleton) targetEntity;
            if (lastEntityConfused != null)
            {
                currentSkeleton.setTarget((LivingEntity) lastEntityConfused);
            }
            else
            {
                currentSkeleton.setTarget(null);
                lastEntityConfused = currentSkeleton;
            }
            break;
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

    public void MobTakenDamage(EntityDamageByEntityEvent event)
    {
        Entity damagedEntity = event.getEntity();
        boolean arrowEntity = (event.getDamager().getType() == EntityType.ARROW);

        if (classOwner.getHealth() < classOwner.getMaxHealth())
        {
            if (classOwner.getGameMode() != GameMode.CREATIVE)
            {
                classOwner.setHealth(classOwner.getHealth() + 1);
            }
        }

        if (damagedEntity.getLocation().getDirection() == classOwner
                .getLocation().getDirection())
        {
            // event.setDamage(event.getDamage() + 4);
        }

        if (arrowEntity)
        {
            switch (damagedEntity.getType().toString().toLowerCase())
            {
            case "zombie":
                Zombie currentZombie = (Zombie) damagedEntity;
                TeleportToTarget(currentZombie);
                break;
            case "skeleton":
                Skeleton currentSkeleton = (Skeleton) damagedEntity;
                TeleportToTarget(currentSkeleton);
                break;
            }
        }
    }

    public void TeleportToTarget(Zombie damagedEntity)
    {
        Location mobLocation = damagedEntity.getLocation();
        Location playerLocation = classOwner.getLocation();
        Location locationToTeleportTo = damagedEntity.getLocation();

        if (damagedEntity.hasLineOfSight(classOwner))
        {
            if (playerLocation.getZ() < mobLocation.getZ())
            {
                locationToTeleportTo.setZ(locationToTeleportTo.getZ() + 1);
            }
            else
            {
                locationToTeleportTo.setZ(locationToTeleportTo.getZ() - 1);
            }

            if (playerLocation.getX() < mobLocation.getX())
            {
                locationToTeleportTo.setX(locationToTeleportTo.getX() + 1);
            }
            else
            {
                locationToTeleportTo.setX(locationToTeleportTo.getX() - 1);
            }
        }
        else
        {
            locationToTeleportTo.setZ(locationToTeleportTo.getZ() - 1);
            locationToTeleportTo.setX(locationToTeleportTo.getX() - 1);
        }

        CanTeleportTo(locationToTeleportTo);
    }

    public void TeleportToTarget(Skeleton damagedEntity)
    {
        Location mobLocation = damagedEntity.getLocation();
        Location playerLocation = classOwner.getLocation();
        Location locationToTeleportTo = damagedEntity.getLocation();

        if (damagedEntity.hasLineOfSight(classOwner))
        {
            if (playerLocation.getZ() < mobLocation.getZ())
            {
                locationToTeleportTo.setZ(locationToTeleportTo.getZ() + 1);
            }
            else
            {
                locationToTeleportTo.setZ(locationToTeleportTo.getZ() - 1);
            }

            if (playerLocation.getX() < mobLocation.getX())
            {
                locationToTeleportTo.setX(locationToTeleportTo.getX() + 1);
            }
            else
            {
                locationToTeleportTo.setX(locationToTeleportTo.getX() - 1);
            }
        }
        else
        {
            locationToTeleportTo.setZ(locationToTeleportTo.getZ() - 1);
            locationToTeleportTo.setX(locationToTeleportTo.getX() - 1);
        }
        CanTeleportTo(locationToTeleportTo);
    }

    public void CanTeleportTo(Location locationToTeleportTo)
    {

        if (locationToTeleportTo.getBlock().getType() == Material.AIR)
        {
            classOwner.teleport(locationToTeleportTo);
        }
        else
        {
            locationToTeleportTo.setY(locationToTeleportTo.getY() + 1);
            CanTeleportTo(locationToTeleportTo);
        }
    }

    public void SetAbilities()
    {
        if (classOwner != null)
        {
            // int currentLevel = classOwner.getLevel();
        }
    }
}
