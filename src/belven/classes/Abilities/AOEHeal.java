package belven.classes.Abilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import belven.classes.resources.functions;

public class AOEHeal extends Ability
{
    public AOEHeal(belven.classes.Class CurrentClass, int priority, int amp)
    {
        super(priority, amp);
        currentClass = CurrentClass;
        requirements.add(new ItemStack(Material.LAPIS_BLOCK, 4));
        abilitiyName = "AOE Heal";
    }

    @Override
    public boolean PerformAbility(Player playerToHeal)
    {
        for (Player p : belven.arena.resources.functions.getNearbyPlayersNew(
                playerToHeal.getLocation(), Amplifier() + 8))
        {
            // p.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1,
            // Amplifier()));
            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,
                    functions.SecondsToTicks(5), Amplifier(), true));
        }

        currentClass.setAbilityOnCoolDown(this, true);

        RemoveItems();
        return true;
    }

    public int Amplifier()
    {
        return functions.abilityCap((double) Amplifier + 1,
                (double) currentClass.classOwner.getLevel());
    }
}