package io.github.dhohmann.hardcore;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Criterias;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Scoreboard;

import io.github.dhohmann.hardcore.server.ServerListener;

/**
 * Plugin class for the ultra hardcore plugin.
 * 
 * @author Daniel Hohmann
 * @since 0.0.1
 */
public class UltraHardcorePlugin extends JavaPlugin {

	private HardcoreMode mode;
	private FileConfiguration config = getConfig();
	private PlayerHandler handler;

	private static final String REGENERATION_POTION = "regeneration.potion";
	private static final String REGENERATION_FOOD = "regeneration.food";
	private static final String HARDCORE_MODE = "hardcoremode";

	public static final String PLAYER_FILE = "player.yml";

	@Override
	public void onEnable() {
		config.addDefault(REGENERATION_POTION, false);
		config.addDefault(REGENERATION_FOOD, false);
		config.addDefault(HARDCORE_MODE, "NORMAL");
		config.options().copyDefaults();

		if (!new File(getDataFolder(), "config.yml").exists()) {
			saveDefaultConfig();
		}
		saveConfig();

		this.mode = HardcoreMode.valueOf(config.getString(HARDCORE_MODE, "NORMAL"));

		getLogger().info("Using " + mode.toString() + " for Hardcore Plugin");

		handler = new PlayerHandler(this);
		try {
			handler.loadPlayers(new File(getDataFolder(), PLAYER_FILE));
		} catch (IOException ex) {
			getLogger().log(Level.SEVERE, "Could not load player file", ex);
		}
		Bukkit.getPluginManager().registerEvents(handler, this);
		Bukkit.getPluginManager().registerEvents(new ServerListener(this), this);

		Bukkit.getServer().setDefaultGameMode(GameMode.SURVIVAL);
		for (World world : Bukkit.getWorlds()) {
			world.setDifficulty(Difficulty.HARD);
			world.setGameRule(GameRule.NATURAL_REGENERATION, false);
			world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
			world.setPVP(true);
		}

		Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
		Objective lifeObjective = scoreboard.registerNewObjective("health", Criterias.DEATHS, "health");
		lifeObjective.setRenderType(RenderType.HEARTS);

	}

	@Override
	public void onDisable() {
		if (handler != null) {
			try {
				handler.savePlayers(new File(getDataFolder(), PLAYER_FILE));
			} catch (IOException ex) {
				getLogger().log(Level.SEVERE, "Could not save player file", ex);
			}
		}
	}

	/**
	 * Retrieves the current plugin hardcore mode.
	 * 
	 * @return current mode
	 * @since 0.1.0
	 */
	public HardcoreMode getMode() {
		return mode;
	}

}
