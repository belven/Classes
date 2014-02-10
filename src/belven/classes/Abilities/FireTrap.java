package belven.classes.Abilities;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;
import org.bukkit.scheduler.BukkitTask;

import belven.timedevents.FireTrapTimer;

public class FireTrap extends Ability
{
    public FireTrap(belven.classes.Class CurrentClass)
    {
        currentClass = CurrentClass;
        requirements.add(new ItemStack(Material.SNOW_BALL, 1));
    }

    @Override
    public void PerformAbility()
    {

    }

    @Override
    public void PerformAbility(Player targetPlayer)
    {

    }

    @SuppressWarnings("unused")
    public void PerformAbility(Location targetLocation)
    {
        BukkitTask currentTimer = new FireTrapTimer(targetLocation.getBlock(),
                4).runTaskTimer(currentClass.plugin(), SecondsToTicks(5),
                SecondsToTicks(2));
        targetLocation.getBlock().setType(
                (new Wool(DyeColor.RED).getItemType()));
    }

    @Override
    public int Amplifier()
    {
        return 0;
    }


    @Override
    public String GetAbilityName()
    {
        return "FireTrap";
    }
}
