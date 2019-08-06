package io.github.dhohmann.hardcore;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

/**
 * Listens to various player events and sets the game mode of the involved
 * player according to the banned from playing state.
 * 
 * @since 0.0.1
 * @author Daniel Hohmann
 *
 */
public class PlayerListener implements Listener {

	private final HardcorePlayerStorage manager;

	/**
	 * Constructs a new player listener.
	 * 
	 * @param manager Player manager that holds the information about players that
	 *                are banned from playing
	 */
	public PlayerListener(HardcorePlayerStorage manager) {
		this.manager = manager;
	}

	/**
	 * Sets a joining player into spectator mode, if he was killed previously.
	 * 
	 * @param event Event triggered by a player join on the server
	 */
	@EventHandler(ignoreCancelled = true)
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (manager.isBannedFromPlaying(player)) {
			player.setGameMode(GameMode.SPECTATOR);
		}
	}

	/**
	 * Sets the killed player to spectator mode.
	 * 
	 * @param event Kill event
	 */
	@EventHandler(ignoreCancelled = true)
	public void onPlayerDeathEvent(PlayerDeathEvent event) {
		Player player = event.getEntity();
		manager.addPlayer(player);
	}

	/**
	 * Prevents a player from changing the own game mode.
	 * 
	 * @param event Event occurred when the player changes the game mode
	 */
	@EventHandler(ignoreCancelled = true)
	public void onPlayerGameModeChangeEvent(PlayerGameModeChangeEvent event) {
		if (event.getNewGameMode() == GameMode.SPECTATOR) {
			return;
		}
		Player player = event.getPlayer();
		if (manager.isBannedFromPlaying(player)) {
			player.sendMessage(
					"You are not allowed as a player. Please contact the administrator to change the gamemode.");
			player.setGameMode(GameMode.SPECTATOR);
			event.setCancelled(true);
		}
	}

	/**
	 * Prevents a player from respawning when he or she should be a spectator.
	 * 
	 * @param event Respawning event
	 */
	@EventHandler(ignoreCancelled = true)
	public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		if (manager.isBannedFromPlaying(player)) {
			player.setGameMode(GameMode.SPECTATOR);
			player.setCollidable(false);
			player.sendMessage("You are dead. You can only spectate!");
		}
	}
}
