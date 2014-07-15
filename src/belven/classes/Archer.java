package belven.classes;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;

import belven.classes.Abilities.Ability;
import belven.classes.Abilities.WebArrow;
import belven.classes.Abilities.FireTrap;
import belven.classes.timedevents.BlockRestorer;
import belvens.classes.resources.ClassDrop;
import belvens.classes.resources.functions;

public class Archer extends Class
{

    public WebArrow classCripplingArrow;
    public FireTrap classFireTrap;

    public Archer(Player currentPlayer, ClassManager instance)
    {
        plugin = instance;
        classOwner = currentPlayer;
        classCripplingArrow = new WebArrow(this, 0);
        classFireTrap = new FireTrap(this, 1);
        className = "Archer";

        Damageable dcurrentPlayer = (Damageable) currentPlayer;
        dcurrentPlayer.setMaxHealth(24.0);
        dcurrentPlayer.setHealth(dcurrentPlayer.getMaxHealth());

        Abilities.add(classFireTrap);
        SortAbilities();
        SetClassDrops();
    }

    @Override
    public void PerformAbility(Player currentPlayer)
    {
        CheckAbilitiesToCast(currentPlayer);
    }

    public void PerformAbility(Entity currentEntity)
    {
        CheckAbilitiesToCast(currentEntity);
    }

    private void CheckAbilitiesToCast(Entity currentEntity)
    {
        Location trapLocation = classOwner.getLocation();
        trapLocation.setY(trapLocation.getY() - 1);
        for (Ability a : Abilities)
        {
            if (!a.onCooldown && a.HasRequirements(classOwner))
            {
                if (!a.PerformAbility(trapLocation))
                {
                    continue;
                }
                else
                {
                    break;
                }
            }
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

    @Override
    public void SetClassDrops()
    {
        ItemStack redwool = new Wool(DyeColor.RED).toItemStack(2);
        ItemStack arrow = new ItemStack(Material.ARROW, 10);
        ItemStack bow = new ItemStack(Material.BOW);
        ItemStack snowBall = new ItemStack(Material.SNOW_BALL, 8);

        classDrops.add(new ClassDrop(arrow, true));
        classDrops.add(new ClassDrop(bow, true));

        classDrops.add(new ClassDrop(redwool, 0, 30));
        classDrops.add(new ClassDrop(snowBall, 30, 50));

        classDrops.add(new ClassDrop(l_Boots(), 50, 100));
        classDrops.add(new ClassDrop(l_ChestPlate(), 50, 100));
        classDrops.add(new ClassDrop(l_Leggings(), 50, 100));
        classDrops.add(new ClassDrop(l_Helmet(), 50, 100));

    }

}
