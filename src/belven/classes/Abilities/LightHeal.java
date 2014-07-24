package belven.classes.Abilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import belven.classes.resources.functions;

public class LightHeal extends Ability
{
    public LightHeal(belven.classes.Class CurrentClass, int priority, int amp)
    {
        super(priority, amp);
        currentClass = CurrentClass;
        requirements.add(new ItemStack(Material.LAPIS_BLOCK, 1));
        abilitiyName = "Light Heal";
        shouldBreak = false;
    }

    @Override
    public boolean PerformAbility(Player playerToHeal)
    {
        if (functions.isHealthLessThanOther(currentClass.classOwner,
                playerToHeal))
        {
            playerToHeal = currentClass.classOwner;
        }

        playerToHeal.addPotionEffect(new PotionEffect(
                PotionEffectType.REGENERATION, functions.SecondsToTicks(5),
                Amplifier()), true);

        currentClass.classOwner.sendMessage("You healed "
                + playerToHeal.getName());
        RemoveItems();

        return true;
    }

    public int Amplifier()
    {
        return functions.abilityCap((double) Amplifier + 1,
                (double) currentClass.classOwner.getLevel());
    }
}
