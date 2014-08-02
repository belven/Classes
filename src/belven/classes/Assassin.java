package belven.classes;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import resources.EntityFunctions;
import resources.Functions;
import belven.classes.Abilities.SoulDrain;
import belven.classes.resources.ClassDrop;

public class Assassin extends Class
{
    public Entity lastEntityConfused;
    private Block recallBlock;
    private SoulDrain classSoulDrain;

    public Assassin(Player currentPlayer, ClassManager instance)
    {
        super(12, currentPlayer, instance);
        className = "Assassin";
        SetAbilities();
        SetClassDrops();
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

        CanTeleportTo(locationToTeleportTo, mobLocation);
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

    public void CanTeleportTo(Location locationToTeleportTo,
            Location mobLocation)
    {
        for (int i = (int) locationToTeleportTo.getY(); i < locationToTeleportTo
                .getY() + 20; i++)
        {
            if (locationToTeleportTo.getBlock().getType() == Material.AIR)
            {
                Location temp = EntityFunctions.lookAt(locationToTeleportTo,
                        mobLocation);
                classOwner.teleport(temp);
            }
            else
            {
                locationToTeleportTo.setY(i);
            }
        }

        // if (locationToTeleportTo.getBlock().getType() == Material.AIR)
        // {
        // classOwner.teleport(locationToTeleportTo);
        // }
        // else
        // {
        // locationToTeleportTo.setY(locationToTeleportTo.getY() + 1);
        // CanTeleportTo(locationToTeleportTo);
        // }
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

    @Override
    public void SetClassDrops()
    {
        ItemStack arrow = new ItemStack(Material.ARROW, 3);
        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        ItemStack bow = new ItemStack(Material.BOW);

        ItemStack speed = new Potion(PotionType.SPEED, 2).toItemStack(1);

        classDrops.add(new ClassDrop(bow, true));
        classDrops.add(new ClassDrop(sword, true));
        classDrops.add(new ClassDrop(arrow, true));
        classDrops.add(new ClassDrop(speed, 0, 100));
    }

    @Override
    public void SelfCast(Player currentPlayer)
    {

    }

    @Override
    public void RightClickEntity(Entity currentEntity)
    {
        if (!classSoulDrain.onCooldown
                && classOwner.getItemInHand().getType() == Material.NETHER_STAR)
        {
            classSoulDrain.PerformAbility(currentEntity);
            UltAbilityUsed(classSoulDrain);
        }
    }

    @Override
    public void SelfTakenDamage(EntityDamageByEntityEvent event)
    {

    }

    @Override
    public void SelfDamageOther(EntityDamageByEntityEvent event)
    {
        Entity damagedEntity = event.getEntity();
        boolean arrowEntity = (event.getDamager().getType() == EntityType.ARROW);

        EntityFunctions.Heal(classOwner, 1);

        if (plugin.GetPlayerE(classOwner).MeleeWeaponInHand())
        {
            event.setDamage(event.getDamage() + 2);
        }

        if (arrowEntity)
        {
            TeleportToTarget(damagedEntity);
        }
    }

    @Override
    public void SetAbilities()
    {
        classSoulDrain = new SoulDrain(this, 1, 0);
        Abilities.add(classSoulDrain);
        SortAbilities();
    }
}
