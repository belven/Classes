package belven.classes.Abilities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import belven.timedevents.FireTrapTimer;

public class FireTrap extends Ability
{
    public FireTrap(belven.classes.Class CurrentClass)
    {
        currentClass = CurrentClass;
        requirements.add(new ItemStack(Material.WOOL, 1));
        abilitiyName = "FireTrap";
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
        targetLocation.getBlock().setType(Material.WOOL);
    }

    @Override
    public int Amplifier()
    {
        return 0;
    }  
}
