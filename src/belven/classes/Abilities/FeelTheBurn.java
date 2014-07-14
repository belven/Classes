package belven.classes.Abilities;

import org.bukkit.entity.Player;
import belven.arena.resources.functions;

public class FeelTheBurn extends Ability
{
    public FeelTheBurn(belven.classes.Class CurrentClass)
    {
        currentClass = CurrentClass;
        abilitiyName = "Feel The Burn";
    }

    @Override
    public void PerformAbility(Player playerToHeal)
    {
        functions.Heal(playerToHeal, 1);
    }

}
