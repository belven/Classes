package belven.classes.Abilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import resources.EntityFunctions;
import resources.Functions;

public class Bandage extends Ability
{
    public Bandage(belven.classes.Class CurrentClass, int priority, int amp)
    {
        super(priority, amp);
        currentClass = CurrentClass;
        requirements.add(new ItemStack(Material.PAPER, 1));
        requirements.add(new ItemStack(Material.STICK, 1));
        inHandRequirements.add(Material.PAPER);
        inHandRequirements.add(Material.STICK);
        abilitiyName = "Bandage";
        shouldBreak = false;
        Cooldown = 10;
    }

    @Override
    public boolean PerformAbility(Player targetPlayer)
    {
        if (EntityFunctions.isHealthLessThanOther(currentClass.classOwner,
                targetPlayer))
        {
            targetPlayer = currentClass.classOwner;
        }

        targetPlayer.addPotionEffect(new PotionEffect(
                PotionEffectType.ABSORPTION, Functions.SecondsToTicks(20),
                Amplifier()), true);

        currentClass.setAbilityOnCoolDown(this, true);
        RemoveItems();
        return true;
    }

    @Override
    public int Amplifier()
    {
        return Functions.abilityCap((double) Amplifier + 1,
                (double) currentClass.classOwner.getLevel());
    }

}
