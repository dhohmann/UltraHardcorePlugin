package io.github.dhohmann.hardcore.server;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ServerListener implements Listener {

	private final JavaPlugin plugin;

	public ServerListener(JavaPlugin plugin) {
		this.plugin = plugin;
	}

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
		suffix.append("}");
		suffix.append(ChatColor.RESET);
		event.setMotd(event.getMotd() + suffix.toString());
	}

}
