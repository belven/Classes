package belven.classes;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import resources.PlayerExtended;
import belven.arena.ArenaManager;
import belven.classes.events.ClassChangeEvent;
import belven.classes.listeners.BlockListener;
import belven.classes.listeners.MobListener;
import belven.classes.listeners.PlayerListener;
import belven.teams.TeamManager;

public class ClassManager extends JavaPlugin {
	private final PlayerListener newplayerListener = new PlayerListener(this);
	private final BlockListener blockListener = new BlockListener(this);
	private final MobListener mobListener = new MobListener(this);

	public ArenaManager arenas = (ArenaManager) Bukkit.getServer()
			.getPluginManager().getPlugin("BelvensArenas");
	public TeamManager teams = (TeamManager) Bukkit.getServer()
			.getPluginManager().getPlugin("BelvensTeams");

	private HashMap<Player, RPGClass> CurrentPlayerClasses = new HashMap<Player, RPGClass>();
	public HashMap<Player, PlayerExtended> PlayersE = new HashMap<Player, PlayerExtended>();

	private static HashMap<String, Class<? extends RPGClass>> StrToRPGClass = new HashMap<String, Class<? extends RPGClass>>();

	static {
		StrToRPGClass.put("Healer", Healer.class);
		StrToRPGClass.put("Mage", DEFAULT.class);
		StrToRPGClass.put("Assassin", Assassin.class);
		StrToRPGClass.put("Archer", Archer.class);
		StrToRPGClass.put("Monk", Monk.class);
		StrToRPGClass.put("Daemon", Daemon.class);
		StrToRPGClass.put("Priest", Priest.class);
		StrToRPGClass.put("Warrior", Warrior.class);
		StrToRPGClass.put("Berserker", Berserker.class);
	}

	@SuppressWarnings("deprecation")
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
			String classString = this.getConfig().getString(
					PlayerName + ".Class");

			if (classString != null) {
				if (CurrentPlayerClasses.get(playerToAdd) == null)

					CurrentPlayerClasses.put(playerToAdd,
							StringToClass(classString, playerToAdd));
			}

			this.getServer().broadcastMessage(
					PlayerName + " was given class " + classString);
		}
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		Player player = (Player) sender;
		String commandSent = cmd.getName();

		if (!(this.arenas.IsPlayerInArena(player) || this.arenas
				.getArenaInIsPlayer(player).isActive)) {

			// Class
			if (commandSent.startsWith("bc")) {
				StringBuilder sb = new StringBuilder(commandSent.toLowerCase());
				sb.delete(0, 2);
				sb.setCharAt(0, (char) (sb.charAt(0) - 32));
				String s = sb.toString();

				if (StrToRPGClass.containsKey(s)) {
					SetClass(player, s);
				}
				return true;
			}

			// Costume BC commands
			switch (commandSent) {

			case "bcdummy":
				Location playerLocation = player.getLocation();
				playerLocation.setX(playerLocation.getX() + 3);
				player.getWorld()
						.spawnEntity(playerLocation, EntityType.ZOMBIE);
				return true;

			case "setlevel":
				int level = Integer.valueOf(args[0]);
				player.setLevel(level);

				String temp1 = String.valueOf(Math.log(player.getLevel()));
				String temp2 = String.valueOf(Math.log10(player.getLevel()));
				String temp3 = String.valueOf(Math.log1p(player.getLevel()));

				player.sendMessage(temp1);
				player.sendMessage(temp2);
				player.sendMessage(temp3);
				return true;

			case "listclasses":
				Player[] currentPlayers = this.getServer().getOnlinePlayers();
				for (Player currentPlayer : currentPlayers) {
					if (currentPlayer != null) {
						RPGClass currentClass = CurrentPlayerClasses
								.get(currentPlayer);
						player.sendMessage(currentPlayer.getName() + " is a "
								+ currentClass.getClassName());
					}
				}
				return true;

			case "listabilities":
				CurrentPlayerClasses.get(player).ListAbilities();
				return true;

			}
		}

		return false;
	}

	private RPGClass StringToClass(String className, Player player) {
		try {
			return StrToRPGClass.get(className)
					.getDeclaredConstructor(Player.class, ClassManager.class)
					.newInstance(player, this);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new DEFAULT(player, this);
	}

	public RPGClass GetClass(Player p) {
		return CurrentPlayerClasses.get(p);
	}

	public void SetClass(Player playerToChange, String classString) {
		String PlayerName = playerToChange.getName();

		this.reloadConfig();
		getConfig().set(PlayerName + ".Class", classString);

		playerToChange.sendMessage(PlayerName + " is class " + classString);

		this.saveConfig();
		CurrentPlayerClasses.put(playerToChange,
				StringToClass(classString, playerToChange));

		Bukkit.getPluginManager().callEvent(
				new ClassChangeEvent(CurrentPlayerClasses.get(playerToChange),
						playerToChange));
	}

	@Override
	public void onDisable() {
		getLogger().info("Goodbye world!");
		this.saveConfig();
	}

}
