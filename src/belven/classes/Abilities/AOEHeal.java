package belven.classes.Abilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import belven.arena.resources.functions;

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
        for (Player p : functions.getNearbyPlayersNew(
                playerToHeal.getLocation(), Amplifier() + 8))
        {
            p.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1,
                    Amplifier()));
            playerToHeal.addPotionEffect(new PotionEffect(
                    PotionEffectType.REGENERATION, functions.SecondsToTicks(5),
                    Amplifier(), true));
        }

        currentClass.setAbilityOnCoolDown(this, true);

        RemoveItems();
        return true;
    }

    public int Amplifier()
    {
        return Math.round(currentClass.classOwner.getLevel() / Amplifier);
    }
}