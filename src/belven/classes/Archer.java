package belven.classes;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import resources.functions;
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

        Damageable dcurrentPlayer = (Damageable) currentPlayer;
        dcurrentPlayer.setMaxHealth(24.0);
        dcurrentPlayer.setHealth(dcurrentPlayer.getMaxHealth());
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
        Location damagedEntityLocation = damagedEntity.getLocation();
        damagedEntityLocation.setY(damagedEntityLocation.getY() + 1);

        if (classCripplingArrow.HasRequirements(classOwner)
                && damagedEntityLocation.getBlock().getType() != Material.WEB)
        {
            new BlockRestorer(damagedEntityLocation.getBlock(), Material.WEB)
                    .runTaskLater(plugin, functions.SecondsToTicks(5));
        }

        if (damagedEntity instanceof LivingEntity)
        {
            LivingEntity currentLivingEntity = (LivingEntity) damagedEntity;

            Damageable dcurrentLivingEntity = (Damageable) currentLivingEntity;

            event.setDamage(functions.damageToDo(event.getDamage(),
                    dcurrentLivingEntity.getHealth(),
                    dcurrentLivingEntity.getMaxHealth()));
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

}
