package io.github.dhohmann.hardcore;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;

/**
 * Handles all player events.
 * 
 * @author Daniel Hohmann
 * @since 0.1.0
 */
public class PlayerHandler implements Listener {

	private static final String PUNISHMENT_LEVEL = "punishment-level";
	private static final String GAME_MODE = "game-mode";

	private HardcoreMode mode;
	private Map<String, PlayerData> players;
	private boolean onlineMode = Bukkit.getOnlineMode();

	/**
	 * Creates a new listener for player events for the provided plugin.
	 * 
	 * @param plugin Plugin instance. If the parameter is an instance of the class
	 *               {@link UltraHardcorePlugin}, the mode is used as the game mode
	 *               of the handler, otherwise the mode will be set to
	 *               {@link HardcoreMode#NORMAL}.
	 */
	public PlayerHandler(Plugin plugin) {
		players = new HashMap<>();

		if (plugin instanceof UltraHardcorePlugin) {
			mode = ((UltraHardcorePlugin) plugin).getMode();
		} else {
			mode = HardcoreMode.NORMAL;
		}
	}

	/**
	 * Creates or loads the player data of the joining player
	 * 
	 * @param event Player event fired when joining
	 */
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (player != null) {
			this.addPlayer(player);
		}

	}

	/**
	 * Handles the player death. Depends on the mode the handler is running in.
	 * 
	 * @param event Player event fired when dying
	 */
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		if (mode == HardcoreMode.LIGHT) {
			PlayerData data = this.players.get(player.getName());
			Object previousLevel = data.getProperty(PUNISHMENT_LEVEL);
			if (!(previousLevel instanceof Integer)) {
				previousLevel = 0;
			}
			data.setProperty(PUNISHMENT_LEVEL, (Integer) previousLevel + 1);
		} else {
			PlayerData data = this.players.get(player.getName());
			data.setProperty(GAME_MODE, GameMode.SPECTATOR);
		}
	}

	/**
	 * Handles the player re-spawn.
	 * 
	 * @param event Player event when the player re-spawns.
	 */
	@EventHandler
	public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		PlayerData data = this.players.get(player.getName());
		if (mode == HardcoreMode.LIGHT) {
			Integer punishmentLevel = (Integer) data.getProperty(PUNISHMENT_LEVEL);
			List<PotionEffect> effects = Punishments.getPunishments(punishmentLevel);
			for (PotionEffect effect : effects) {
				player.addPotionEffect(effect);
			}
		} else {
			GameMode gameMode = GameMode.valueOf((String) data.getProperty(GAME_MODE));
			player.setGameMode(gameMode);
		}
	}

	/**
	 * Cancels the milk bucket drink effect, when the handler is running in
	 * {@link HardcoreMode#LIGHT} mode
	 * 
	 * @param event Player event when the player consumes an item
	 */
	@EventHandler
	public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
		if (mode == HardcoreMode.LIGHT) {
			if (event.getItem().getType().equals(Material.MILK_BUCKET)) {
				event.setCancelled(true);
			}
		}
	}

	/**
	 * Loads the player data from the provided file
	 * 
	 * @param file File that holds the player data. Will be created, if it does not
	 *             exist.
	 * @throws IOException when the file could not be accessed
	 */
	public void loadPlayers(File file) throws IOException {
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			file.createNewFile();
		}
		if (file.exists()) {
			YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
			for (String playerId : configuration.getKeys(false)) {
				ConfigurationSection section = configuration.getConfigurationSection(playerId);
				PlayerData data = new PlayerData(playerId);
				for (String key : section.getKeys(false)) {
					data.setProperty(key, section.get(key));
				}
				players.put(playerId, data);
			}
		}
	}

	/**
	 * Saves the player data to the provided file.
	 * 
	 * @param file Destination file for the save operation.
	 * @throws IOException when the data could not be written
	 */
	public void savePlayers(File file) throws IOException {
		YamlConfiguration configuration;
		if (!file.exists()) {
			configuration = new YamlConfiguration();
		} else {
			configuration = YamlConfiguration.loadConfiguration(file);
		}

		for (String playerName : players.keySet()) {
			String playerId;
			if (onlineMode) {
				playerId = players.get(playerName).getId().toString();
			} else {
				playerId = playerName;
			}
			Map<String, Object> playerProperties = players.get(playerName).getProperties();
			for (String key : playerProperties.keySet()) {
				configuration.set(playerId + "." + key, playerProperties.get(key));
			}
		}
		configuration.save(file);
	}

	private void addPlayer(Player player) {
		if (!players.containsKey(player.getName())) {
			if (onlineMode) {
				players.put(player.getName(), new PlayerData(player.getUniqueId()));
			} else {
				players.put(player.getName(), new PlayerData(player.getName()));
			}

		}
	}

	private void removePlayer(Player player) {
		if (players.containsKey(player.getName())) {
			players.remove(player.getName());
		}
	}

}
