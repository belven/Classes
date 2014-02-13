package belven.classes;

import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.BlockIterator;

import belven.classes.Abilities.Ability;
import belven.classes.Abilities.ChainLightning;
import belven.classes.Abilities.MageFireball;
import belven.classes.Abilities.Pop;

public class Mage extends Class
{
    public MageFireball classFireball;
    public ChainLightning classChainLightning;
    public BlockIterator currentBlockIterator;
    public Pop classPop;

    public Mage(Player currentPlayer, ClassManager instance)
    {
        plugin = instance;
        this.classOwner = currentPlayer;
        this.classFireball = new MageFireball(this);
        classChainLightning = new ChainLightning(this);
        classPop = new Pop(this);
        this.SetAbilities();
        className = "Mage";
    }

    @Override
    public void PerformAbility(Player currentPlayer)
    {
        Player playerSelected;
        playerSelected = classOwner;

        this.CheckAbilitiesToCast(playerSelected, currentPlayer);
    }

    public void PerformAbility(Entity currentEntity)
    {
        Player playerSelected;

        if (currentEntity.getType() == EntityType.PLAYER)
        {
            playerSelected = (Player) currentEntity;
            this.CheckAbilitiesToCast(playerSelected, classOwner);
        }
        else
        {
            playerSelected = classOwner;
            this.CheckAbilitiesToCast(playerSelected, classOwner);
        }
    }

    public void CheckAbilitiesToCast(Player target, Player player)
    {
        if (classOwner.getItemInHand().getType() == Material.NETHER_STAR
                && classChainLightning.HasRequirements(classOwner, 30))
        {
            classChainLightning.PerformAbility(classOwner.getLocation());
        }
        else if (classOwner.getItemInHand().getType() == Material.FEATHER
                && classPop.HasRequirements(classOwner, 1))
        {
            currentBlockIterator = new BlockIterator(classOwner, 100);
            Block popBlock = GetTargetBlock();

            if (popBlock != null)
            {
                classPop.PerformAbility(popBlock.getLocation());
            }
        }
        else if (classFireball.HasRequirements(player, 1))
        {
            this.classFireball.PerformAbility(player);
        }
    }

    public Block GetTargetBlock()
    {
        Block lastBlock;

        if (currentBlockIterator != null)
        {
            do
            {
                lastBlock = currentBlockIterator.next();
            }
            while (currentBlockIterator.hasNext()
                    && lastBlock.getType() == Material.AIR
                    && getNearbyEntities(lastBlock.getLocation(), 1).length == 0);
            return lastBlock;
        }
        else
            return null;
    }

    // excluding players
    public static Entity[] getNearbyEntities(Location l, int radius)
    {
        int chunkRadius = radius < 16 ? 1 : (radius - (radius % 16)) / 16;
        HashSet<Entity> radiusEntities = new HashSet<Entity>();

        for (int chX = 0 - chunkRadius; chX <= chunkRadius; chX++)
        {
            for (int chZ = 0 - chunkRadius; chZ <= chunkRadius; chZ++)
            {
                int x = (int) l.getX(), y = (int) l.getY(), z = (int) l.getZ();

                for (Entity e : new Location(l.getWorld(), x + (chX * 16), y, z
                        + (chZ * 16)).getChunk().getEntities())
                {
                    if (e.getLocation().distance(l) <= radius
                            && e.getLocation().getBlock() != l.getBlock()
                            && e.getType() != EntityType.PLAYER)
                    {
                        radiusEntities.add(e);
                    }
                }
            }
        }
        
        return radiusEntities.toArray(new Entity[radiusEntities.size()]);
    }

    public void SetAbilities()
    {
        if (classOwner != null)
        {
            int currentLevel = classOwner.getLevel();

            if (currentLevel > 1)
            {
                AddToAbilities(new MageFireball(this));
            }
        }
    }

    public String ListAbilities()
    {
        String ListOfAbilities = "";

        if (Abilities != null)
        {
            for (int i = 0; i < Abilities.size(); i++)
            {
                ListOfAbilities = ListOfAbilities
                        + (Abilities.get(i) != null ? Abilities.get(i)
                                .GetAbilityName() + ", " : "");
            }
            return ListOfAbilities;
        }
        else
            return "";
    }

    public void AddToAbilities(Ability abilityToAdd)
    {
        if (Abilities != null)
        {
            Abilities.add(abilityToAdd);
        }
    }

    @Override
    public void PerformAbility()
    {

    }

    @Override
    public Player classOwner()
    {
        return classOwner;
    }

    public void TakeDamage(EntityDamageByEntityEvent event, Player damagedPlayer)
    {
        if (event.getDamager().getType() == EntityType.LIGHTNING)
        {
            event.setDamage(0);
        }

    }
}
