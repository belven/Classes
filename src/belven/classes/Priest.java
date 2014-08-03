package belven.classes;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import belven.classes.Abilities.AOEHeal;
import belven.classes.Abilities.Cleanse;
import belven.classes.resources.ClassDrop;

public class Priest extends Healer
{
    public AOEHeal classAOEHeal;
    public Cleanse classCleanse;

    public Priest(Player currentPlayer, ClassManager instance)
    {
        super(8, currentPlayer, instance);
        classAOEHeal = new AOEHeal(this, 0, 3);
        classCleanse = new Cleanse(this, 3, 3);
        className = "Priest";
        baseClassName = "Healer";
        classAOEHeal.Cooldown = 8;
        Abilities.add(classAOEHeal);
        Abilities.add(classCleanse);
        Abilities.remove(classBandage);
        SortAbilities();
        SetClassDrops();
    }

    @Override
    public void SetClassDrops()
    {
        super.SetClassDrops();
        ItemStack glow = new ItemStack(Material.GLOWSTONE_DUST, 1);
        classDrops.add(new ClassDrop(glow, true));
    }

}
