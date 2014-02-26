package belven.classes;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import belven.classes.Abilities.WebArrow;
import belven.classes.Abilities.FireTrap;
import belven.classes.timedevents.BlockRestorer;

public class Archer extends Class
{

    public WebArrow classCripplingArrow;
    public FireTrap classFireTrap;

    public Archer(Player currentPlayer, ClassManager instance)
    {
        plugin = instance;
        classOwner = currentPlayer;
        classCripplingArrow = new WebArrow(this);
        classFireTrap = new FireTrap(this);
        className = "Archer";
        SetAbilities();
        currentPlayer.setMaxHealth(24);
        currentPlayer.setHealth(currentPlayer.getMaxHealth());
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

        if (trapLocation.getBlock().getType() != Material.WOOL
                && classFireTrap.HasRequirements(classOwner))
        {
            classFireTrap.PerformAbility(trapLocation);
        }
    }

    public void CheckAbilitiesToCast(Player target, Player player)
    {
        Location trapLocation = classOwner.getLocation();
        trapLocation.setY(trapLocation.getY() - 1);

        if (trapLocation.getBlock().getType() != Material.WOOL
                && classFireTrap.HasRequirements(classOwner))
        {
            classFireTrap.PerformAbility(trapLocation);
        }
    }

    public void SetAbilities()
    {
        if (classOwner != null)
        {
        }
    }

    public void MobTakenDamage(EntityDamageByEntityEvent event)
    {
        Entity damagedEntity = event.getEntity();
        double damageDone = event.getDamage();
        double damageToDo = 0;
        Location damagedEntityLocation = damagedEntity.getLocation();
        damagedEntityLocation.setY(damagedEntityLocation.getY() + 1);

        if (classCripplingArrow.HasRequirements(classOwner)
                && damagedEntityLocation.getBlock().getType() != Material.WEB)
        {
            new BlockRestorer(damagedEntityLocation.getBlock(), Material.WEB)
                    .runTaskLater(plugin, SecondsToTicks(5));
        }

        if (damagedEntity instanceof LivingEntity)
        {
            LivingEntity currentLivingEntity = (LivingEntity) damagedEntity;
            damageToDo = damageToDo(damageDone,
                    currentLivingEntity.getHealth(),
                    currentLivingEntity.getMaxHealth());
            event.setDamage(damageToDo);
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
