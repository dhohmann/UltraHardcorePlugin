package io.github.dhohmann.hardcore;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;

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
	@EventHandler
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

	/**
	 * Cancels regeneration of players if configured inside configuration.
	 * 
	 * @param e Regeneration event
	 * @since 0.0.2
	 */
	@EventHandler
	public void cancelRegenerationEvent(EntityRegainHealthEvent e) {
		if (!(e.getEntity() instanceof Player)) {
			return;
		}
		FileConfiguration config;
		try {
			Plugin plugin = Bukkit.getPluginManager().getPlugin("UltraHardcorePlugin");
			config = plugin.getConfig();
		} catch (Throwable ex) {
			config = new YamlConfiguration();
			Bukkit.getLogger().log(Level.WARNING, "An error occured in the player listener", ex);
		}

		if (e.getRegainReason() == RegainReason.EATING) {
			e.setCancelled(config.getBoolean("regeneration.food", false));
		}

		if (e.getRegainReason() == RegainReason.MAGIC || e.getRegainReason() == RegainReason.MAGIC_REGEN) {
			e.setCancelled(config.getBoolean("regeneration.potion", false));
		}
	}
}
