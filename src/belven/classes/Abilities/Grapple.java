package belven.classes.Abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import resources.EntityFunctions;
import belven.classes.Class;

public class Grapple extends Ability
{
    public Grapple(Class CurrentClass, int priority, int amp)
    {
        super(priority, amp);
        this.currentClass = CurrentClass;
        this.requirements.add(new ItemStack(Material.STRING));
        this.abilitiyName = "Grapple";
    }

    public boolean PerformAbility()
    {
        List<EntityType> types = new ArrayList<EntityType>();
        types.add(EntityType.SKELETON);
        types.add(EntityType.BLAZE);
        types.add(EntityType.ZOMBIE);

        List<LivingEntity> tempTargets = EntityFunctions.findTargetEntityByType(
                currentClass.classOwner, 50.0D, types,
                currentClass.classOwner.getLevel() / 5 + 1);

        if (tempTargets != null && tempTargets.size() > 0)
        {
            Location l = this.currentClass.classOwner.getLocation();
            l.setY(l.getY() + 1.0D);

            for (LivingEntity le : tempTargets)
            {
                le.teleport(this.currentClass.classOwner);
            }
            currentClass.setAbilityOnCoolDown(this);
            RemoveItems();
            return true;
        }

        return false;
    }
}
