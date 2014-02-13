package belven.classes;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitTask;

import belven.classes.Abilities.CripplingArrow;
import belven.classes.Abilities.FireTrap;
import belven.timedevents.BlockRestorer;

public class Archer extends Class
{

    public CripplingArrow classCripplingArrow;
    public FireTrap classFireTrap;

    public Archer(Player currentPlayer, ClassManager instance)
    {
        plugin = instance;
        classOwner = currentPlayer;
        classCripplingArrow = new CripplingArrow();
        classFireTrap = new FireTrap(this);
        className = "Archer";
        SetAbilities();
    }

    @Override
    public void PerformAbility(Player currentPlayer)
    {
        Player playerSelected;
        playerSelected = classOwner;

        CheckAbilitiesToCast(playerSelected, currentPlayer);
    }

    public void PerformAbility(Entity currentEntity)
    {
        CheckAbilitiesToCast(currentEntity);
    }

    private void CheckAbilitiesToCast(Entity currentEntity)
    {
        Location trapLocation = classOwner.getLocation();
        trapLocation.setY(trapLocation.getY() - 1);

        if (classOwner.getItemInHand().getType() == Material.WOOL
                && trapLocation.getBlock().getType() != Material.WOOL
                && classFireTrap.HasRequirements(classOwner, 1))
        {
            classFireTrap.PerformAbility(trapLocation);
        }
    }

    public void CheckAbilitiesToCast(Player target, Player player)
    {
        Location trapLocation = classOwner.getLocation();
        trapLocation.setY(trapLocation.getY() - 1);

        if (classOwner.getItemInHand().getType() == Material.WOOL
                && trapLocation.getBlock().getType() != Material.WOOL
                && classFireTrap.HasRequirements(classOwner, 1))
        {
            classFireTrap.PerformAbility(trapLocation);
        }
    }

    public void SetAbilities()
    {
        if (classOwner != null)
        {
            // int currentLevel = classOwner.getLevel();
        }
    }

    @SuppressWarnings("unused")
    public void MobTakenDamage(EntityDamageByEntityEvent event)
    {
        Entity damagedEntity = event.getEntity();
        double damageDone = event.getDamage();
        double damageToDo;

        switch (damagedEntity.getType().toString().toLowerCase())
        {
        case "zombie":
            Zombie currentZombie = (Zombie) damagedEntity;
            damageToDo = damageToDo(damageDone, currentZombie.getHealth(),
                    currentZombie.getMaxHealth());

            event.setDamage(damageToDo);
            if (classCripplingArrow.HasRequirements(classOwner, 1))
            {
                Location zombieLocation = currentZombie.getLocation();
                zombieLocation.setY(zombieLocation.getY() + 1);

                if (zombieLocation.getBlock().getType() != Material.WEB)
                {
                    BukkitTask newTask = new BlockRestorer(
                            zombieLocation.getBlock()).runTaskLater(
                            this.plugin, SecondsToTicks(5));
                    zombieLocation.getBlock().setType(Material.WEB);
                }
            }
            break;

        case "skeleton":
            Skeleton currentSkeleton = (Skeleton) damagedEntity;
            damageToDo = damageToDo(damageDone, currentSkeleton.getHealth(),
                    currentSkeleton.getMaxHealth());

            event.setDamage(damageToDo);

            if (classCripplingArrow.HasRequirements(classOwner, 1))
            {
                Location skeletonLocation = currentSkeleton.getLocation();
                skeletonLocation.setY(skeletonLocation.getY() + 1);

                if (skeletonLocation.getBlock().getType() != Material.WEB)
                {
                    BukkitTask task = new BlockRestorer(
                            skeletonLocation.getBlock()).runTaskLater(
                            this.plugin, SecondsToTicks(5));
                    skeletonLocation.getBlock().setType(Material.WEB);
                }
            }
            break;
        }
    }

    @Override
    public ClassManager plugin()
    {
        return plugin;
    }

    public Player classOwner()
    {
        return classOwner;
    }

    public int SecondsToTicks(int seconds)
    {
        return (seconds * 20);
    }

    private double damageToDo(double damageDone, double currentHealth,
            double maxHealth)
    {
        return damageDone + (damageDone / ((currentHealth / maxHealth)));
    }

}
