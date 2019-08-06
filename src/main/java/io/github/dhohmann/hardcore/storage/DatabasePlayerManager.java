package io.github.dhohmann.hardcore.storage;

import org.bukkit.entity.Player;

import io.github.dhohmann.hardcore.HardcorePlayerStorage;

/**
 * Stores player information inside a database.
 * 
 * @author Daniel Hohmann
 * @since 0.0.1
 */
public class DatabasePlayerManager implements HardcorePlayerStorage {

	@Override
	public void addPlayer(Player player) {

	}

	@Override
	public void removePlayer(Player player) {
	}

	@Override
	public void savePlayers() {

	}

	@Override
	public void loadPlayers() {

	}

	@Override
	public boolean isBannedFromPlaying(Player player) {
		return false;
	}

}
