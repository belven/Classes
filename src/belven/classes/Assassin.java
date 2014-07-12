package belven.classes;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import resources.functions;
import belven.classes.Abilities.SoulDrain;

public class Assassin extends Class
{
    public Entity lastEntityConfused;
    private Block recallBlock;
    private SoulDrain classSoulDrain;

    public Assassin(Player currentPlayer, ClassManager instance)
    {
        plugin = instance;
        classOwner = currentPlayer;
        className = "Assassin";
        classSoulDrain = new SoulDrain(this);
        SetAbilities();
        Damageable dcurrentLivingEntity = (Damageable) currentPlayer;
        dcurrentLivingEntity.setMaxHealth(24.0);
        dcurrentLivingEntity.setHealth(dcurrentLivingEntity.getMaxHealth());
    }

    @Override
    public void PerformAbility(Player currentPlayer)
    {
        if (classOwner.getItemInHand().getType() == Material.NETHER_STAR)
        {

        }
    }

    @Override
    public void PerformAbility(Entity currentEntity)
    {
        if (!classSoulDrain.onCooldown
                && classOwner.getItemInHand().getType() == Material.NETHER_STAR)
        {
            classSoulDrain.PerformAbility(currentEntity);
            UltAbilityUsed(classSoulDrain);
        }
    }

    public void MobTakenDamage(EntityDamageByEntityEvent event)
    {
        Entity damagedEntity = event.getEntity();
        boolean arrowEntity = (event.getDamager().getType() == EntityType.ARROW);

        functions.Heal(classOwner, 1); 

        if (arrowEntity)
        {
            TeleportToTarget(damagedEntity);
        }
    }

    public void TeleportToTarget(Entity currentEntity)
    {
        Location mobLocation = currentEntity.getLocation();
        Location playerLocation = classOwner.getLocation();
        Location locationToTeleportTo = currentEntity.getLocation();
        Location recallLocation = classOwner.getLocation();
        recallLocation.setY(recallLocation.getY() - 1);
        recallBlock = recallLocation.getBlock();

        if (HasLineOfSight(currentEntity))
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

    public boolean HasLineOfSight(Entity damagedEntity)
    {
        if (damagedEntity instanceof LivingEntity)
        {
            LivingEntity currentLivingEntity = (LivingEntity) damagedEntity;
            return currentLivingEntity.hasLineOfSight(classOwner);
        }
        return false;
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

    public void ToggleSneakEvent(PlayerToggleSneakEvent event)
    {
        if (event.isSneaking() && recallBlock != null)
        {
            Location recallLocation = recallBlock.getLocation();
            recallLocation.setY(recallLocation.getY() + 1);
            classOwner.teleport(recallLocation);
            recallBlock = null;
        }
    }
}
