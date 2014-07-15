package belvens.classes.resources;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.util.Vector;

public class functions
{
    public static final float DEGTORAD = 0.01745329F;

    public static LivingEntity findTargetEntity(Player origin, double radius)
    {
        Location originLocation = origin.getEyeLocation();
        Vector originDirection = originLocation.getDirection();
        Vector originVector = originLocation.toVector();

        LivingEntity target = null;
        double minDotProduct = 4.9E-324D;
        for (Entity entity : origin.getNearbyEntities(radius, radius, radius))
        {
            if ((!entity.equals(origin)) && ((entity instanceof LivingEntity)))
            {
                LivingEntity living = (LivingEntity) entity;
                Location newTargetLocation = living.getEyeLocation();

                Vector toTarget = newTargetLocation.toVector()
                        .subtract(originVector).normalize();
                double dotProduct = toTarget.dot(originDirection);
                if ((dotProduct > 0.3D) && (origin.hasLineOfSight(living))
                        && ((target == null) || (dotProduct > minDotProduct)))
                {
                    target = living;
                    minDotProduct = dotProduct;
                }
            }
        }
        return target;
    }

    @SuppressWarnings("deprecation")
    public static void AddToInventory(Player p, ItemStack is)
    {
        int maxAmount = is.getMaxStackSize() - is.getAmount();

        if (is.getMaxStackSize() == 1
                && !p.getInventory().contains(is.getType(), 1))
        {
            p.getInventory().addItem(is);
        }
        else if (!p.getInventory().contains(is.getType(), maxAmount))
        {
            p.getInventory().addItem(is);
        }

        p.updateInventory();
    }

    public static List<LivingEntity> findTargetEntityByType(Player origin,
            double radius, List<EntityType> types, int maxTargets)
    {
        int count = 0;
        Location originLocation = origin.getEyeLocation();
        Vector originDirection = originLocation.getDirection();
        Vector originVector = originLocation.toVector();

        List<LivingEntity> targets = new ArrayList<LivingEntity>();
        double minDotProduct = 4.9E-324D;
        for (Entity entity : origin.getNearbyEntities(radius, radius, radius))
        {
            if (count >= maxTargets)
            {
                break;
            }
            if ((!entity.equals(origin)) && ((entity instanceof LivingEntity)))
            {
                LivingEntity living = (LivingEntity) entity;

                Vector toTarget = living.getEyeLocation().toVector()
                        .subtract(originVector).normalize();

                double dotProduct = toTarget.dot(originDirection);
                if ((types.contains(living.getType())) && (dotProduct > 0.3D)
                        && (origin.hasLineOfSight(living))
                        && ((targets == null) || (dotProduct > minDotProduct)))
                {
                    count++;
                    targets.add(living);
                    minDotProduct = dotProduct;
                }
            }
        }
        return targets;
    }

    public static LivingEntity findTargetEntityByType(Player origin,
            double radius, List<EntityType> types)
    {
        Location originLocation = origin.getEyeLocation();
        Vector originDirection = originLocation.getDirection();
        Vector originVector = originLocation.toVector();

        LivingEntity target = null;
        double minDotProduct = 4.9E-324D;
        for (Entity entity : origin.getNearbyEntities(radius, radius, radius))
        {
            if ((!entity.equals(origin)) && ((entity instanceof LivingEntity)))
            {
                LivingEntity living = (LivingEntity) entity;
                Location newTargetLocation = living.getEyeLocation();

                Vector toTarget = newTargetLocation.toVector()
                        .subtract(originVector).normalize();
                double dotProduct = toTarget.dot(originDirection);
                if ((types.contains(living.getType())) && (dotProduct > 0.3D)
                        && (origin.hasLineOfSight(living))
                        && ((target == null) || (dotProduct > minDotProduct)))
                {
                    target = living;
                    minDotProduct = dotProduct;
                    break;
                }
            }
        }
        return target;
    }

    public static LivingEntity findTargetEntityByType(Player origin,
            double radius, EntityType type)
    {
        Location originLocation = origin.getEyeLocation();
        Vector originDirection = originLocation.getDirection();
        Vector originVector = originLocation.toVector();

        LivingEntity target = null;
        double minDotProduct = 4.9E-324D;
        for (Entity entity : origin.getNearbyEntities(radius, radius, radius))
        {
            if ((!entity.equals(origin)) && ((entity instanceof LivingEntity)))
            {
                LivingEntity living = (LivingEntity) entity;
                Location newTargetLocation = living.getEyeLocation();

                Vector toTarget = newTargetLocation.toVector()
                        .subtract(originVector).normalize();
                double dotProduct = toTarget.dot(originDirection);
                if ((living.getType() == type) && (dotProduct > 0.3D)
                        && (origin.hasLineOfSight(living))
                        && ((target == null) || (dotProduct > minDotProduct)))
                {
                    target = living;
                    minDotProduct = dotProduct;
                    break;
                }
            }
        }
        return target;
    }

    public static LivingEntity findTargetPlayer(Player origin, double radius)
    {
        Location originLocation = origin.getEyeLocation();
        Vector originDirection = originLocation.getDirection();
        Vector originVector = originLocation.toVector();

        LivingEntity target = null;
        double minDotProduct = 4.9E-324D;
        for (Entity entity : origin.getNearbyEntities(radius, radius, radius))
        {
            if (((entity instanceof Player)) && (!entity.equals(origin)))
            {
                LivingEntity living = (LivingEntity) entity;
                Location newTargetLocation = living.getEyeLocation();

                Vector toTarget = newTargetLocation.toVector()
                        .subtract(originVector).normalize();
                double dotProduct = toTarget.dot(originDirection);
                if ((dotProduct > 0.3D) && (origin.hasLineOfSight(living))
                        && ((target == null) || (dotProduct > minDotProduct)))
                {
                    target = living;
                    minDotProduct = dotProduct;
                }
            }
        }
        return target;
    }

    public static Location move(Location loc, Vector offset)
    {
        return move(loc, offset.getX(), offset.getY(), offset.getZ());
    }

    public static boolean isNumberBetween(int numberToCheck, int low, int high)
    {
        return (numberToCheck >= low && numberToCheck < high);
    }

    public static Location move(Location loc, double dx, double dy, double dz)
    {
        Vector off = rotate(loc.getYaw(), loc.getPitch(), dx, dy, dz);
        double x = loc.getX() + off.getX();
        double y = loc.getY() + off.getY();
        double z = loc.getZ() + off.getZ();
        return new Location(loc.getWorld(), x, y, z, loc.getYaw(),
                loc.getPitch());
    }

    public static Vector rotate(float yaw, float pitch, double x, double y,
            double z)
    {
        float angle = yaw * 0.01745329F;
        double sinyaw = Math.sin(angle);
        double cosyaw = Math.cos(angle);

        angle = pitch * 0.01745329F;
        double sinpitch = Math.sin(angle);
        double cospitch = Math.cos(angle);

        double newx = 0.0D;
        double newy = 0.0D;
        double newz = 0.0D;
        newz -= x * cosyaw;
        newz -= y * sinyaw * sinpitch;
        newz -= z * sinyaw * cospitch;
        newx += x * sinyaw;
        newx -= y * cosyaw * sinpitch;
        newx -= z * cosyaw * cospitch;
        newy += y * cospitch;
        newy -= z * sinpitch;

        return new Vector(newx, newy, newz);
    }

    public static Location lookAt(Location loc, Location lookat)
    {
        loc = loc.clone();

        double dx = lookat.getX() - loc.getX();
        double dy = lookat.getY() - loc.getY();
        double dz = lookat.getZ() - loc.getZ();
        if (dx != 0.0D)
        {
            if (dx < 0.0D)
            {
                loc.setYaw(4.712389F);
            }
            else
            {
                loc.setYaw(1.570796F);
            }
            loc.setYaw(loc.getYaw() - (float) Math.atan(dz / dx));
        }
        else if (dz < 0.0D)
        {
            loc.setYaw(3.141593F);
        }
        double dxz = Math.sqrt(Math.pow(dx, 2.0D) + Math.pow(dz, 2.0D));

        loc.setPitch((float) -Math.atan(dy / dxz));

        loc.setYaw(-loc.getYaw() * 180.0F / 3.141593F);
        loc.setPitch(loc.getPitch() * 180.0F / 3.141593F);

        return loc;
    }

    public static double ScaleDamage(int Level, double damageDone,
            int levelDivider)
    {
        return damageDone + Level / levelDivider;
    }

    public static double damageToDo(double damageDone, double currentHealth,
            double maxHealth)
    {
        return damageDone + damageDone / (currentHealth / maxHealth);
    }

    public static boolean deosPlayersInventoryContainAtLeast(Player p,
            Material m, int amountInI)
    {
        return p.getInventory().containsAtLeast(new ItemStack(m), amountInI);
    }

    public static boolean deosPlayersInventoryContainAtLeast(Player p,
            PotionType pt, int amountInI, int level)
    {
        return p.getInventory().containsAtLeast(
                new ItemStack(new Potion(pt, level).toItemStack(1)), amountInI);
    }

    public static boolean numberBetween(double number, double start, double end)
    {
        return (number >= start) && (number < end);
    }

    public static double entityCurrentHealthPercent(double currentHealth,
            double maxHealth)
    {
        return currentHealth / maxHealth;
    }

    public static Player[] getNearbyPlayers(Location l, int radius)
    {
        int chunkRadius = radius < 16 ? 1 : (radius - radius % 16) / 16;
        HashSet<Entity> radiusEntities = new HashSet<Entity>();
        for (int chX = 0 - chunkRadius; chX <= chunkRadius; chX++)
        {
            for (int chZ = 0 - chunkRadius; chZ <= chunkRadius; chZ++)
            {
                int x = (int) l.getX();
                int y = (int) l.getY();
                int z = (int) l.getZ();
                Entity[] arrayOfEntity;
                int j = (arrayOfEntity = new Location(l.getWorld(), x + chX
                        * 16, y, z + chZ * 16).getChunk().getEntities()).length;
                for (int i = 0; i < j; i++)
                {
                    Entity e = arrayOfEntity[i];
                    if ((e.getLocation().distance(l) <= radius)
                            && ((e instanceof Player))
                            && (e.getLocation().getBlock() != l.getBlock()))
                    {
                        radiusEntities.add((Player) e);
                    }
                }
            }
        }
        return (Player[]) radiusEntities.toArray(new Player[radiusEntities
                .size()]);
    }

    public static Entity[] getNearbyEntities(Location l, int radius)
    {
        int chunkRadius = radius < 16 ? 1 : (radius - radius % 16) / 16;
        HashSet<Entity> radiusEntities = new HashSet<Entity>();
        for (int chX = 0 - chunkRadius; chX <= chunkRadius; chX++)
        {
            for (int chZ = 0 - chunkRadius; chZ <= chunkRadius; chZ++)
            {
                int x = (int) l.getX();
                int y = (int) l.getY();
                int z = (int) l.getZ();

                Entity[] arrayOfEntity;

                int j = (arrayOfEntity = new Location(l.getWorld(), x + chX
                        * 16, y, z + chZ * 16).getChunk().getEntities()).length;

                for (int i = 0; i < j; i++)
                {
                    Entity e = arrayOfEntity[i];
                    if ((e.getLocation().distance(l) <= radius))
                    {
                        radiusEntities.add(e);
                    }
                }
            }
        }
        return (Entity[]) radiusEntities.toArray(new Entity[radiusEntities
                .size()]);
    }

    public static List<Block> getBlocksInRadius(Location l, int radius)
    {
        World w = l.getWorld();
        int xCoord = (int) l.getX();
        int zCoord = (int) l.getZ();
        int YCoord = (int) l.getY();

        List<Block> tempList = new ArrayList<Block>();
        for (int x = 0; x <= 2 * radius; x++)
        {
            for (int z = 0; z <= 2 * radius; z++)
            {
                for (int y = 0; y <= 2 * radius; y++)
                {
                    tempList.add(w.getBlockAt(xCoord + x, YCoord + y, zCoord
                            + z));
                }
            }
        }
        return tempList;
    }

    public static void Heal(LivingEntity entityToHeal, int amountToHeal)
    {
        Damageable dEntityToHeal = entityToHeal;
        double max = dEntityToHeal.getMaxHealth();
        double current = dEntityToHeal.getHealth();
        if (entityToHeal != null)
        {
            for (int i = amountToHeal; i != 0; i--)
            {
                if (current + i < max)
                {
                    entityToHeal.setHealth(current + i);
                }
            }
        }
    }

    public static void RestoreHunger(Player entityToRestore, int amountToRestore)
    {
        if (entityToRestore != null)
        {
            for (int i = amountToRestore; i != 0; i--)
            {
                if (entityToRestore.getFoodLevel() + i < 10)
                {
                    entityToRestore.setFoodLevel(entityToRestore.getFoodLevel()
                            + i);
                }
            }
        }
    }

    public static void RestoreSaturation(Player entityToRestore,
            int amountToRestore)
    {
        if (entityToRestore != null)
        {
            for (int i = amountToRestore; i != 0; i--)
            {
                if (entityToRestore.getSaturation() + i < 10.0F)
                {
                    entityToRestore.setSaturation(entityToRestore
                            .getSaturation() + i);
                }
            }
        }
    }

    public static List<Block> getBlocksBetweenPoints(Location min, Location max)
    {
        World w = min.getWorld();
        List<Block> tempList = new ArrayList<Block>();
        for (int x = min.getBlockX(); x <= max.getBlockX(); x++)
        {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++)
            {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++)
                {
                    tempList.add(w.getBlockAt(x, y, z));
                }
            }
        }
        return tempList;
    }

    public static boolean isArmor(Material material)
    {
        if (material.name().contains("CHESTPLATE"))
        {
            return true;
        }
        if (material.name().contains("LEGGINGS"))
        {
            return true;
        }
        if (material.name().contains("HELMET"))
        {
            return true;
        }
        if (material.name().contains("BOOTS"))
        {
            return true;
        }
        return false;
    }

    public static boolean isWeapon(Material material)
    {
        if (isAMeeleWeapon(material))
        {
            return true;
        }
        if (material.toString().contains("BOW"))
        {
            return true;
        }
        return false;
    }

    public static boolean debuffs(PotionEffectType pet)
    {
        switch (pet.toString())
        {
        case "HUNGER":
            return true;
        case "BLINDNESS":
            return true;
        case "CONFUSION":
            return true;
        case "POISON":
            return true;
        case "SLOW":
            return true;
        case "SLOW_DIGGING":
            return true;
        case "WEAKNESS":
            return true;
        case "WITHER":
            return true;
        default:
            return false;
        }
    }

    public static boolean isNotInteractiveBlock(Material material)
    {
        switch (material.toString())
        {
        case "ENDER_CHEST":
            return false;
        case "MINECART":
            return false;
        case "WORKBENCH":
            return false;
        case "BED":
            return false;
        case "SIGN":
            return false;
        case "ANVIL":
            return false;
        case "CHEST":
            return false;
        case "LEVER":
            return false;
        case "FURNACE":
            return false;
        case "ENCHANTMENT_TABLE":
            return false;
        case "BUTTON":
            return false;
        default:
            return true;
        }
    }

    public static ArrayList<ItemStack> getAllMeeleWeapons()
    {
        ArrayList<ItemStack> tempWeapons = new ArrayList<ItemStack>();
        tempWeapons.add(new ItemStack(Material.WOOD_SWORD));
        tempWeapons.add(new ItemStack(Material.STONE_SWORD));
        tempWeapons.add(new ItemStack(Material.IRON_SWORD));
        tempWeapons.add(new ItemStack(Material.GOLD_SWORD));
        tempWeapons.add(new ItemStack(Material.DIAMOND_SWORD));
        return tempWeapons;
    }

    public static boolean isAMeeleWeapon(Material material)
    {
        switch (material.toString())
        {
        case "WOOD_SWORD":
            return true;
        case "STONE_SWORD":
            return true;
        case "DIAMOND_SWORD":
            return true;
        case "IRON_SWORD":
            return true;
        case "GOLD_SWORD":
            return true;
        default:
            return false;
        }
    }

    public static boolean isFood(Material material)
    {
        switch (material.toString())
        {
        case "MUSHROOM_SOUP":
            return true;
        case "GOLDEN_APPLE":
            return true;
        case "COOKED_CHICKEN":
            return true;
        case "ROTTEN_FLESH":
            return true;
        case "RAW_CHICKEN":
            return true;
        case "PORK":
            return true;
        case "BREAD":
            return true;
        case "MELON":
            return true;
        case "COOKED_BEEF":
            return true;
        case "RAW_BEEF":
            return true;
        case "GOLDEN_CARROT":
            return true;
        case "CARROT":
            return true;
        case "COOKIE":
            return true;
        default:
            return false;
        }

    }

    public static boolean IsAMob(EntityType currentEntityType)
    {
        if ((currentEntityType == EntityType.BLAZE)
                || (currentEntityType == EntityType.CAVE_SPIDER)
                || (currentEntityType == EntityType.CREEPER)
                || (currentEntityType == EntityType.ENDER_DRAGON)
                || (currentEntityType == EntityType.ENDERMAN)
                || (currentEntityType == EntityType.GHAST)
                || (currentEntityType == EntityType.MAGMA_CUBE)
                || (currentEntityType == EntityType.PIG_ZOMBIE)
                || (currentEntityType == EntityType.SKELETON)
                || (currentEntityType == EntityType.SPIDER)
                || (currentEntityType == EntityType.SLIME)
                || (currentEntityType == EntityType.WITCH)
                || (currentEntityType == EntityType.WITHER)
                || (currentEntityType == EntityType.ZOMBIE))
        {
            return true;
        }
        return false;
    }

    public static int SecondsToTicks(int seconds)
    {
        return seconds * 20;
    }

    public static double MobMaxHealth(LivingEntity entity)
    {
        if (entity.getType() == EntityType.ZOMBIE)
        {
            return 20.0D;
        }
        if (entity.getType() == EntityType.SKELETON)
        {
            return 20.0D;
        }
        if (entity.getType() == EntityType.SPIDER)
        {
            return 16.0D;
        }
        if (entity.getType() == EntityType.CREEPER)
        {
            return 20.0D;
        }
        if (entity.getType() == EntityType.WITHER)
        {
            return 300.0D;
        }
        if (entity.getType() == EntityType.BLAZE)
        {
            return 20.0D;
        }
        if (entity.getType() == EntityType.ENDERMAN)
        {
            return 40.0D;
        }
        if (entity.getType() == EntityType.CAVE_SPIDER)
        {
            return 12.0D;
        }
        if (entity.getType() == EntityType.GHAST)
        {
            return 10.0D;
        }
        if (entity.getType() == EntityType.MAGMA_CUBE)
        {
            MagmaCube MagmaCube = (MagmaCube) entity;
            if (MagmaCube.getSize() == 4)
            {
                return 16.0D;
            }
            if (MagmaCube.getSize() == 2)
            {
                return 4.0D;
            }
            return 1.0D;
        }
        if (entity.getType() == EntityType.PIG_ZOMBIE)
        {
            return 20.0D;
        }
        if (entity.getType() == EntityType.SLIME)
        {
            Slime slime = (Slime) entity;
            if (slime.getSize() == 4)
            {
                return 16.0D;
            }
            if (slime.getSize() == 2)
            {
                return 4.0D;
            }
            return 1.0D;
        }
        return 20.0D;
    }
}
