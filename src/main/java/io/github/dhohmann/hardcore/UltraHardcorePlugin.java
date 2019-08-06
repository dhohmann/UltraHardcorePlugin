package io.github.dhohmann.hardcore;

import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Criterias;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import io.github.dhohmann.hardcore.server.ServerListener;
import io.github.dhohmann.hardcore.storage.DatabasePlayerManager;
import io.github.dhohmann.hardcore.storage.PlayerFileManager;

/**
 * Plugin class.
 * 
 * @author Daniel Hohmann
 * @since 0.0.1
 */
public class UltraHardcorePlugin extends JavaPlugin {

	private static final String REGENERATION_POTION = "regeneration.potion";
	private static final String REGENERATION_FOOD = "regeneration.food";
	private static final String PLUGIN_UPDATECHECK = "plugin.updateCheck";
	private static final String DEFAULT_DIFFICULTY = "defaultDifficulty";
	private static final String DATABASE_ENABLED = "database.enabled";
	private static final String SETTINGS_SHOWHEARTS = "settings.showHearts";

	private FileConfiguration config = getConfig();
	private HardcorePlayerStorage playerManager;
	private Logger logger = getLogger();

	@Override
	public void onEnable() {
		logger.setResourceBundle(ResourceBundle.getBundle("resourceBundle"));

		saveDefaultConfig();

		config.addDefault(REGENERATION_POTION, false);
		config.addDefault(REGENERATION_FOOD, false);
		config.addDefault(PLUGIN_UPDATECHECK, false);

		config.options().copyDefaults();
		saveConfig();

		if (config.getBoolean(PLUGIN_UPDATECHECK, false)) {
			logger.log(Level.INFO, "Todo.Implement", PLUGIN_UPDATECHECK);
		}

		configureServerSettings();
		configureWorldSettings();
		preparePlayerManager();
		prepareHeartsInTabList();
		prepareListeners();
	}

	private void configureServerSettings() {
		Server server = Bukkit.getServer();
		server.setDefaultGameMode(GameMode.SURVIVAL);
	}

	private void configureWorldSettings() {
		List<World> worlds = Bukkit.getServer().getWorlds();
		Difficulty defaultDifficulty;
		try {
			defaultDifficulty = Difficulty.valueOf(config.getString(DEFAULT_DIFFICULTY, "HARD").toUpperCase());
		} catch (IllegalArgumentException ex) {
			logInvalidConfiguration(DEFAULT_DIFFICULTY, config.getString(DEFAULT_DIFFICULTY));
			defaultDifficulty = Difficulty.HARD;
		}

		for (World w : worlds) {
			w.setDifficulty(defaultDifficulty);
			w.setGameRule(GameRule.NATURAL_REGENERATION, false);
			w.setGameRule(GameRule.DO_LIMITED_CRAFTING, false);
			w.setGameRule(GameRule.KEEP_INVENTORY, false);
		}
	}

	private void preparePlayerManager() {
		if (config.getBoolean(DATABASE_ENABLED, false)) {
			playerManager = new DatabasePlayerManager();
		} else {
			playerManager = new PlayerFileManager(this);
		}
		playerManager.loadPlayers();
	}

	private void prepareHeartsInTabList() {
		if (config.getBoolean(SETTINGS_SHOWHEARTS, true)) {
			Scoreboard mainBoard = Bukkit.getScoreboardManager().getMainScoreboard();
			Objective obj = mainBoard.getObjective(DisplaySlot.PLAYER_LIST);
			if (mainBoard.getObjectivesByCriteria(Criterias.HEALTH).isEmpty()) {
				if (obj != null) {
					obj.unregister();
				}
				obj = Bukkit.getScoreboardManager().getMainScoreboard().registerNewObjective(Criterias.HEALTH,
						Criterias.HEALTH, Criterias.HEALTH);
				obj.setDisplaySlot(DisplaySlot.PLAYER_LIST);
			}
		}

	}
	
	private void prepareListeners() {
		Bukkit.getPluginManager().registerEvents(new PlayerListener(playerManager), this);
		Bukkit.getPluginManager().registerEvents(new ServerListener(this), this);
	}

	private void logInvalidConfiguration(String configuration, String value) {
		logger.log(Level.WARNING, "Configuration.Invalid", new Object[] { configuration, value });
	}

	@Override
	public void onDisable() {
		saveConfig();

		playerManager.savePlayers();
		playerManager = null;
		
	}

}
