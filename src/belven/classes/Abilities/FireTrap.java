package belven.classes.Abilities;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import belven.classes.resources.functions;
import belven.classes.timedevents.FireTrapTimer;

public class FireTrap extends Ability
{
    public FireTrap(belven.classes.Class CurrentClass, int priority)
    {
        super(priority);
        currentClass = CurrentClass;
        ItemStack redwool = new Wool(DyeColor.RED).toItemStack(1);
        requirements.add(redwool);
        inHandRequirements.add(Material.WOOL);
        abilitiyName = "Fire Trap";
    }

    public boolean PerformAbility(Location targetLocation)
    {
        if (targetLocation.getBlock().getType() != Material.WOOL)
        {
            new FireTrapTimer(targetLocation.getBlock(),
                    functions.SecondsToTicks(Amplifier()), 4).runTaskTimer(
                    currentClass.plugin, functions.SecondsToTicks(5),
                    functions.SecondsToTicks(2));

            targetLocation.getBlock().setType(Material.WOOL);
            currentClass.classOwner.addPotionEffect(new PotionEffect(
                    PotionEffectType.SPEED, functions.SecondsToTicks(2), 3));
            return true;
        }
        else
        {
            return false;
        }
        // Wool tempWool = (Wool) targetLocation.getBlock().getState();
        // tempWool.setColor(DyeColor.RED);
    }

    @Override
    public int Amplifier()
    {
        return (int) ((currentClass.classOwner.getLevel() / 3) + 2);
    }
}
