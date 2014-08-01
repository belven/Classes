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

import resources.Functions;
import belven.classes.Abilities.Ability;
import belven.classes.Abilities.DamageTrap;
import belven.classes.Abilities.FireTrap;
import belven.classes.Abilities.WebArrow;
import belven.classes.resources.ClassDrop;
import belven.classes.timedevents.BlockRestorer;

public class Archer extends Class
{
    public WebArrow classCripplingArrow;
    public FireTrap classFireTrap;
    public DamageTrap classDamageTrap;

    public Archer(Player currentPlayer, ClassManager instance)
    {
        super(12, currentPlayer, instance);
        className = "Archer";
        SetClassDrops();
        SetAbilities();
    }

    @Override
    public void SelfCast(Player currentPlayer)
    {
        CheckAbilitiesToCast(currentPlayer);
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

    @Override
    public void SetClassDrops()
    {
        ItemStack redwool = new Wool(DyeColor.RED).toItemStack(2);
        ItemStack graywool = new Wool(DyeColor.GRAY).toItemStack(2);
        ItemStack arrow = new ItemStack(Material.ARROW, 10);
        ItemStack bow = new ItemStack(Material.BOW);
        ItemStack snowBall = new ItemStack(Material.SNOW_BALL, 8);

        classDrops.add(new ClassDrop(arrow, true));
        classDrops.add(new ClassDrop(bow, true));

        classDrops.add(new ClassDrop(redwool, 0, 30));
        classDrops.add(new ClassDrop(graywool, 0, 30));
        classDrops.add(new ClassDrop(snowBall, 30, 50));

        classDrops.add(new ClassDrop(l_Boots(), 50, 100));
        classDrops.add(new ClassDrop(l_ChestPlate(), 50, 100));
        classDrops.add(new ClassDrop(l_Leggings(), 50, 100));
        classDrops.add(new ClassDrop(l_Helmet(), 50, 100));

    }

    @Override
    public void RightClickEntity(Entity currentEntity)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void SelfTakenDamage(EntityDamageByEntityEvent event)
    {

    }

    @Override
    public void SelfDamageOther(EntityDamageByEntityEvent event)
    {
        LivingEntity currentLivingEntity = Functions.GetDamager(event);
        Location damagedEntityLocation = event.getEntity().getLocation();
        damagedEntityLocation.setY(damagedEntityLocation.getY());

        if (classOwner.getItemInHand().getType() == Material.BOW)
        {
            if (classCripplingArrow.HasRequirements(classOwner)
                    && damagedEntityLocation.getBlock().getType() != Material.WEB)
            {
                new BlockRestorer(damagedEntityLocation.getBlock(),
                        Material.WEB).runTaskLater(plugin,
                        Functions.SecondsToTicks(5));
            }

            event.setDamage(event.getDamage() + 10);

            Damageable dcurrentLivingEntity = (Damageable) currentLivingEntity;

            event.setDamage(Functions.damageToDo(event.getDamage(),
                    dcurrentLivingEntity.getHealth(),
                    dcurrentLivingEntity.getMaxHealth()));
        }
    }

    @Override
    public void SetAbilities()
    {
        classCripplingArrow = new WebArrow(this, 0, 0);
        classFireTrap = new FireTrap(this, 1, 0);
        classDamageTrap = new DamageTrap(this, 1, 0);
        Abilities.add(classFireTrap);
        Abilities.add(classDamageTrap);
        SortAbilities();
    }

}
