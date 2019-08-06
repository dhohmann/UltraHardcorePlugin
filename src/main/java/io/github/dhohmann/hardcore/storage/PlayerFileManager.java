package io.github.dhohmann.hardcore.storage;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.dhohmann.hardcore.HardcorePlayerStorage;

/**
 * Stores player information in the file system.
 * 
 * @author Daniel Hohmann
 * @since 0.0.1
 */
public class PlayerFileManager implements HardcorePlayerStorage {

	private JavaPlugin plugin;
	private final File playerFile;
	private FileConfiguration config;

	/**
	 * Constructs a new player manager.
	 * 
	 * @param plugin Plugin that owns the player manager
	 */
	public PlayerFileManager(JavaPlugin plugin) {
		this.playerFile = new File(plugin.getDataFolder(), "player.yml");
		this.plugin = plugin;
		this.config = new YamlConfiguration();
	}

	/**
	 * Retrieves the file the manager stores the data in.
	 * 
	 * @return Player file
	 */
	public File getPlayerFile() {
		return playerFile;
	}

	@Override
	public boolean isBannedFromPlaying(Player player) {
		String key = player.getDisplayName();
		if (Bukkit.getServer().getOnlineMode()) {
			key = player.getUniqueId().toString();
		}
		GameMode mode = null;
		if (config.contains(key)) {
			mode = GameMode.valueOf(config.getString(key));
		}
		return GameMode.SPECTATOR.equals(mode);
	}

	@Override
	public void addPlayer(Player player) {
		String key = player.getDisplayName();
		if (Bukkit.getServer().getOnlineMode()) {
			key = player.getUniqueId().toString();
		}
		config.set(key, GameMode.SPECTATOR.toString());
	}

	@Override
	public void removePlayer(Player player) {
		String key = player.getDisplayName();
		if (Bukkit.getServer().getOnlineMode()) {
			key = player.getUniqueId().toString();
		}
		config.set(key, null);
	}

	@Override
	public void loadPlayers() {
		try {
			if (!playerFile.exists()) {
				playerFile.createNewFile();
			}
			config.load(playerFile);
		} catch (IOException | InvalidConfigurationException e) {
			plugin.getLogger().log(Level.SEVERE, "PlayerManager.loadingError", e);
		}
	}

	@Override
	public void savePlayers() {
		try {
			config.save(playerFile);
		} catch (IOException e) {
			plugin.getLogger().log(Level.SEVERE, "PlayerManager.saveError", e);
		}

	}

}
