package io.github.dhohmann.hardcore.storage;

import org.bukkit.entity.Player;

/**
 * 
 * @author Daniel Hohmann
 * @since 0.0.1
 */
public interface PlayerStorage {
	/**
	 * Adds a player to the data storage.
	 * 
	 * @param player Player to be added
	 */
	void addPlayer(Player player);

	/**
	 * Removes a player from the data storage.
	 * 
	 * @param player Player to be removed
	 */
	void removePlayer(Player player);

	/**
	 * Loads all players from the underlying system.
	 */
	void loadPlayers();

	/**
	 * Saves all players contained inside the storage to the underlying system.
	 */
	void savePlayers();

}
