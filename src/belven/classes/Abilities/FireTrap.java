package belven.classes.Abilities;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.material.Wool;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import resources.functions;
import belven.classes.timedevents.FireTrapTimer;

public class FireTrap extends Ability
{
    public FireTrap(belven.classes.Class CurrentClass)
    {
        currentClass = CurrentClass;
        ItemStack redwool = new Wool(DyeColor.RED).toItemStack(1);
        requirements.add(redwool);
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
                functions.SecondsToTicks(Amplifier()), 4).runTaskTimer(
                currentClass.plugin(), functions.SecondsToTicks(5),
                functions.SecondsToTicks(2));

        targetLocation.getBlock().setType(Material.WOOL);
        currentClass.classOwner.addPotionEffect(new PotionEffect(
                PotionEffectType.SPEED, functions.SecondsToTicks(2), 3));

        // Wool tempWool = (Wool) targetLocation.getBlock().getState();
        // tempWool.setColor(DyeColor.RED);
    }

    @Override
    public int Amplifier()
    {
        return (int) ((currentClass.classOwner.getLevel() / 3) + 2);
    }
}
