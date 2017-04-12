package me.pugabear.DubtrackUtils;

import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.EventPriority;

import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;

public class EventListener implements Listener {
	private DubtrackUtils dtu = DubtrackUtils.getInstance();
	private FileConfiguration config = DubtrackUtils.getInstance().getConfig();
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onChat(AsyncPlayerChatEvent event) {
		if (config.getBoolean("settings.chat.game-to-dub")) {
			try {
				dtu.getRoom().sendMessage(config.getString("lang.chat.game-to-dub")
						.replaceAll("%user%", event.getPlayer().getDisplayName())
						.replaceAll("%message%", event.getMessage()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onJoin(PlayerJoinEvent event) {
		dtu.putAmMap(event.getPlayer(), false);
		dtu.putChatMap(event.getPlayer(), false);
	}
}