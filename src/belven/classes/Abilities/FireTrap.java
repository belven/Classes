package belven.classes.Abilities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import belven.classes.timedevents.FireTrapTimer;

public class FireTrap extends Ability
{
    public FireTrap(belven.classes.Class CurrentClass)
    {
        currentClass = CurrentClass;
        requirements.add(new ItemStack(Material.WOOL, 1));
        inHandRequirements.add(Material.WOOL);
        abilitiyName = "Fire Trap";
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
                SecondsToTicks(Amplifier()), 4).runTaskTimer(
                currentClass.plugin(), SecondsToTicks(5), SecondsToTicks(2));

        targetLocation.getBlock().setType(Material.WOOL);
    }

    @Override
    public int Amplifier()
    {
        return (int) ((currentClass.classOwner.getLevel() / 3) + 2);
    }
}
