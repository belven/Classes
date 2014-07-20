package belven.classes;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import belven.classes.Abilities.Ability;
import belven.classes.Abilities.FeelTheBurn;
import belven.classes.Abilities.SetAlight;
import belven.classes.resources.ClassDrop;

public class Knight extends Warrior
{
    public FeelTheBurn classFeelTheBurn;
    public SetAlight classSetAlight;

    public Knight(Player currentPlayer, ClassManager instance)
    {
        super(currentPlayer, instance);
        this.className = "Knight";
        baseClassName = "Warrior";
        SortAbilities();
        SetClassDrops();
    }

    @Override
    public void AbilityUsed(Ability ability)
    {
        if (ability == currentRetaliation)
        {
            
        }
    }

    public int Amplifier()
    {
        return Math.round(classOwner.getLevel() / 5) + 1;
    }

    @Override
    public void SetClassDrops()
    {
        super.SetClassDrops();
        ItemStack fire = new ItemStack(Material.FIREWORK_CHARGE, 2);
        classDrops.add(new ClassDrop(fire, true));
    }
}
