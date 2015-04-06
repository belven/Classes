package belven.classes;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import belven.classes.events.ClassChangeEvent;
import belven.classes.listeners.BlockListener;
import belven.classes.listeners.MobListener;
import belven.classes.listeners.PlayerListener;
import belven.classes.player.Archer;
import belven.classes.player.Assassin;
import belven.classes.player.Berserker;
import belven.classes.player.Daemon;
import belven.classes.player.Healer;
import belven.classes.player.Knight;
import belven.classes.player.Mage;
import belven.classes.player.Monk;
import belven.classes.player.Priest;
import belven.classes.player.Warrior;
import belven.resources.Group;
import belven.resources.PlayerExtended;

public class ClassManager extends JavaPlugin {
	private final PlayerListener newplayerListener = new PlayerListener(this);
	private final BlockListener blockListener = new BlockListener(this);
	private final MobListener mobListener = new MobListener(this);

	private HashMap<LivingEntity, RPGClass> currentPlayerClasses = new HashMap<LivingEntity, RPGClass>();
	public HashMap<Player, PlayerExtended> PlayersE = new HashMap<Player, PlayerExtended>();

	private static HashMap<String, Class<? extends RPGClass>> StrToRPGClass = new HashMap<String, Class<? extends RPGClass>>();

	static {
		StrToRPGClass.put("Healer", Healer.class);
		StrToRPGClass.put("Mage", Mage.class);
		StrToRPGClass.put("Assassin", Assassin.class);
		StrToRPGClass.put("Archer", Archer.class);
		StrToRPGClass.put("Monk", Monk.class);
		StrToRPGClass.put("Daemon", Daemon.class);
		StrToRPGClass.put("Priest", Priest.class);
		StrToRPGClass.put("Warrior", Warrior.class);
		StrToRPGClass.put("Knight", Knight.class);
		StrToRPGClass.put("Berserker", Berserker.class);
		StrToRPGClass.put("Default", DEFAULT.class);
	}

	@Override
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(newplayerListener, this);
		pm.registerEvents(blockListener, this);
		pm.registerEvents(mobListener, this);

		for (Player currentPlayer : getServer().getOnlinePlayers()) {
			AddClassToPlayer(currentPlayer);
		}
	}

	public PlayerExtended GetPlayerE(Player p) {
		return PlayersE.get(p);
	}

	public void AddClassToPlayer(Player playerToAdd) {
		String PlayerName = playerToAdd.getName();
		this.reloadConfig();

		if (!this.getConfig().contains(PlayerName)) {
			SetClass(playerToAdd, "DEFAULT");
		} else {
			String classString = this.getConfig().getString(PlayerName + ".Class");

			if (classString != null) {
				if (currentPlayerClasses.get(playerToAdd) == null) {
					currentPlayerClasses.put(playerToAdd, StringToClass(classString, playerToAdd));
				}
			}

			this.getServer().broadcastMessage(PlayerName + " was given class " + classString);
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		String commandSent = cmd.getName();

		// if (this.arenas.IsPlayerInArena(p) ?
		// !this.arenas.getArena(p).isActive() : true) {
		// Class
		if (commandSent.startsWith("bc")) {

			if (args.length == 0) {
				StringBuilder sb = new StringBuilder(commandSent.toLowerCase());
				sb.delete(0, 2);
				sb.setCharAt(0, (char) (sb.charAt(0) - 32));
				String s = sb.toString();
				if (StrToRPGClass.containsKey(s)) {
					SetClass(p, s);
				}
				return true;
			} else {
				switch (args[0]) {
				case "help":
					giveHelpBook(p);
					return true;
				case "lac":
					listAvailableClasses(p);
					return true;
				}
			}
		}

		// Costume BC commands
		switch (commandSent) {

		case "bcdummy":
			Location playerLocation = p.getLocation();
			playerLocation.setX(playerLocation.getX() + 3);
			p.getWorld().spawnEntity(playerLocation, EntityType.ZOMBIE);
			return true;
		case "setlevel":
			int level = Integer.valueOf(args[0]);
			p.setLevel(level);
			return true;
		case "listclasses":
			Collection<? extends Player> currentPlayers = this.getServer().getOnlinePlayers();
			for (Player currentPlayer : currentPlayers) {
				if (currentPlayer != null) {
					RPGClass newClass = currentPlayerClasses.get(currentPlayer);
					p.sendMessage(currentPlayer.getName() + " is a " + newClass.getClassName());
				}
			}
			return true;
		case "listabilities":
			currentPlayerClasses.get(p).ListAbilities();
			return true;
		}
		// }
		return false;
	}

	private void listAvailableClasses(Player p) {
		String classes = "Available classes: ";
		for (String s : StrToRPGClass.keySet()) {
			classes += s + ", ";
		}

		p.sendMessage(classes);
	}

	private void giveHelpBook(Player p) {
		RPGClass rpgClass = GetClass(p);

		if (rpgClass != null && rpgClass.getClass() != DEFAULT.class) {
			ItemStack is = new ItemStack(Material.WRITTEN_BOOK);
			is.setItemMeta(getBookAtPath(is, "Class Books." + rpgClass.getClassName()));
			p.getInventory().addItem(is);
		}

	}

	public boolean isAlly(Player p1, Player p2) {
		if (p1 == p2) {
			return true;
		}

		Group p1Arena = null;
		Group p2Arena = null;
		Group p1Team = null;
		Group p2Team = null;

		if (p1.hasMetadata("InArena")) {
			p1Arena = (Group) p1.getMetadata("InArena").get(0).value();
		}

		if (p2.hasMetadata("InArena")) {
			p2Arena = (Group) p2.getMetadata("InArena").get(0).value();
		}

		if (p1.hasMetadata("InTeam")) {
			p1Team = (Group) p1.getMetadata("InTeam").get(0).value();
		}

		if (p2.hasMetadata("InTeam")) {
			p2Team = (Group) p2.getMetadata("InTeam").get(0).value();
		}

		boolean selfInArena = p1Arena != null;
		boolean targetInArena = p2Arena != null;
		boolean isPvP = false;

		if (selfInArena) {
			isPvP = p1Arena.isPvP();
		}

		if (selfInArena && targetInArena && !isPvP) {
			return true;
		} else if (p1Team != null && p2Team != null && p1Team.getName().equals(p2Team.getName())) {
			return true;
		}

		return false;
	}

	public List<Player> getArenaAllies(Player p) {
		Group playerGroup = null;
		List<Player> players = new ArrayList<>();

		if (p.hasMetadata("InArena")) {

			playerGroup = (Group) p.getMetadata("InArena").get(0).value();

			if (playerGroup.isPvP()) {
				for (Player otherP : playerGroup.getPlayers()) {
					if (inSameTeam(p, otherP)) {
						players.add(otherP);
					}
				}
				return players;
			} else {
				return playerGroup.getPlayers();
			}
		}
		return new ArrayList<Player>();
	}

	public boolean inSameTeam(Player p1, Player p2) {
		Group p1Team = null;
		Group p2Team = null;

		if (p1.hasMetadata("InTeam")) {
			p1Team = (Group) p1.getMetadata("InTeam").get(0).value();
		}

		if (p2.hasMetadata("InTeam")) {
			p2Team = (Group) p2.getMetadata("InTeam").get(0).value();
		}

		if (p1Team != null && p2Team != null && p1Team.getName().equals(p2Team.getName())) {
			return true;
		}

		return false;
	}

	public Group getAllyGroup(Player p) {
		Group playerGroup = null;

		if (p.hasMetadata("InArena")) {

			playerGroup = (Group) p.getMetadata("InArena").get(0).value();

			if (playerGroup.isPvP()) {
				return null;
			}
		}

		if (playerGroup == null && p.hasMetadata("InTeam")) {
			playerGroup = (Group) p.getMetadata("InTeam").get(0).value();
		}

		return playerGroup;
	}

	public Group getGroup(Player p) {
		Group playerGroup = null;

		if (p.hasMetadata("InArena")) {
			playerGroup = (Group) p.getMetadata("InArena").get(0).value();
		} else if (p.hasMetadata("InTeam")) {
			playerGroup = (Group) p.getMetadata("InTeam").get(0).value();
		}

		return playerGroup != null ? playerGroup : new Group(new ArrayList<Player>(), UUID.randomUUID().toString());
	}

	public BookMeta getBookAtPath(ItemStack is, String path) {
		BookMeta meta = (BookMeta) is.getItemMeta();
		File ymlFile = new File(getDataFolder(), "Class Books.yml");
		YamlConfiguration loadedFile = YamlConfiguration.loadConfiguration(ymlFile);

		ConfigurationSection conf = loadedFile.getConfigurationSection(path);

		if (conf == null) {
			return meta;
		}

		meta.setTitle(conf.getString("Title"));

		if (conf.contains("Pages")) {
			for (Entry<String, Object> s : conf.getConfigurationSection("Pages").getValues(true).entrySet()) {
				meta.addPage((String) s.getValue());
			}
		}
		return meta;
	}

	private RPGClass StringToClass(String className, Player player) {
		try {
			return StrToRPGClass.get(className).getDeclaredConstructor(Player.class, ClassManager.class)
					.newInstance(player, this);
		} catch (Exception e) {
			e.printStackTrace();
			return new DEFAULT(player, this);
		}
	}

	public RPGClass GetClass(LivingEntity le) {
		return currentPlayerClasses.get(le) != null ? currentPlayerClasses.get(le) : new DEFAULT(le, this);
	}

	public void SetClass(LivingEntity le, RPGClass newClass) {
		currentPlayerClasses.put(le, newClass);
	}

	public void SetClass(Player playerToChange, String classString) {
		String PlayerName = playerToChange.getName();

		reloadConfig();
		getConfig().set(PlayerName + ".Class", classString);

		playerToChange.sendMessage(PlayerName + " is class " + classString);

		saveConfig();
		SetClass(playerToChange, StringToClass(classString, playerToChange));

		Bukkit.getPluginManager().callEvent(
				new ClassChangeEvent(currentPlayerClasses.get(playerToChange), playerToChange));
	}

	@Override
	public void onDisable() {
		this.saveConfig();
	}
}