package io.github.dhohmann.hardcore.server;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Listener class that handles server based events.
 * 
 * @author Daniel Hohmann
 * @since 0.0.1
 */
public class ServerListener implements Listener {

	private final JavaPlugin plugin;

	/**
	 * Constructs a new server listener.
	 * 
	 * @param plugin Plugin the listener is attached to
	 */
	public ServerListener(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	/**
	 * Appends the plugin name as a suffix to the message of the day in the server
	 * list.
	 * 
	 * @param event Ping event
	 */
	@EventHandler
	public void onServerListPingEvent(ServerListPingEvent event) {
		StringBuilder suffix = new StringBuilder();
		suffix.append(ChatColor.RED);
		suffix.append("[");
		suffix.append(plugin.getName());
		suffix.append(ChatColor.YELLOW);
		suffix.append(" v");
		suffix.append(plugin.getDescription().getVersion());
		suffix.append(ChatColor.RED);
		suffix.append("]");
		suffix.append(ChatColor.RESET);
		event.setMotd(event.getMotd() + " " + suffix.toString());
	}

}
