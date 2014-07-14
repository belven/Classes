package belven.classes.Abilities;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import belven.classes.Class;

public class Grapple extends Ability
{
    public Grapple(Class CurrentClass)
    {
        this.currentClass = CurrentClass;
        this.requirements.add(new ItemStack(Material.STRING));
        this.abilitiyName = "Grapple";
    }

    public void PerformAbility(List<LivingEntity> targetEntities)
    {
        super.PerformAbility();

        Location l = this.currentClass.classOwner.getLocation();
        l.setY(l.getY() + 1.0D);
        for (LivingEntity le : targetEntities)
        {
            le.teleport(this.currentClass.classOwner);
        }
    }

    public int Amplifier()
    {
        return 0;
    }
}
