package io.github.dhohmann.hardcore;

import org.bukkit.entity.Player;

import io.github.dhohmann.hardcore.storage.PlayerStorage;

/**
 * Player storage containing players that may be banned from playing.
 * 
 * @author Daniel Hohmann
 * @since 0.0.1
 */
public interface HardcorePlayerStorage extends PlayerStorage {

	/**
	 * Checks if a player is banned from playing.
	 * 
	 * @param player Player to be checked.
	 * @return <code>true</code>, if the player is banned, <code>false</code>
	 *         otherwise
	 */
	boolean isBannedFromPlaying(Player player);

}
