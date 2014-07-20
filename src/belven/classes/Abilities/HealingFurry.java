package belven.classes.Abilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import belven.classes.resources.functions;

public class HealingFurry extends Ability
{
    public HealingFurry(belven.classes.Class CurrentClass, int priority, int amp)
    {
        super(priority, amp);
        currentClass = CurrentClass;
        requirements.add(new ItemStack(Material.LAPIS_BLOCK, 4));
        abilitiyName = "Healing Furry";
    }

    @Override
    public boolean PerformAbility(Player playerToHeal)
    {
        PotionEffect pe = new PotionEffect(PotionEffectType.HEALTH_BOOST,
                functions.SecondsToTicks(Amplifier() + 30), Amplifier(), true);

        if (!onCooldown
                && (!playerToHeal
                        .hasPotionEffect(PotionEffectType.HEALTH_BOOST)))
        {
            playerToHeal.addPotionEffect(pe, false);

            currentClass.classOwner.sendMessage("You boosted  "
                    + playerToHeal.getName() + "s max health");
            currentClass.setAbilityOnCoolDown(this, true);
            RemoveItems();
            return true;
        }
        return false;
    }

    public boolean containsEffect(PotionEffectType pet, Player playerToHeal)
    {
        for (PotionEffect pe : playerToHeal.getActivePotionEffects())
        {
            if (pe.getType().equals(pet))
            {
                return true;
            }
        }
        return false;
    }

    public int Amplifier()
    {
        return Math.round(currentClass.classOwner.getLevel() / Amplifier);
    }

}
