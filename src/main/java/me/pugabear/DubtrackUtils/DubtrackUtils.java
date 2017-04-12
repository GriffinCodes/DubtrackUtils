package me.pugabear.DubtrackUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import io.sponges.dubtrack4j.DubtrackAPI;
import io.sponges.dubtrack4j.DubtrackBuilder;
import io.sponges.dubtrack4j.event.room.SongChangeEvent;
import io.sponges.dubtrack4j.event.user.UserChatEvent;
import io.sponges.dubtrack4j.framework.Room;

public class DubtrackUtils extends JavaPlugin {
	public final static String NAME = "DubtrackUtils";
	public final String DUB_URL = "https://dubtrack.fm/join/";
	public static DubtrackUtils instance;
	public static Logger log;
	protected FileConfiguration config; 
	public HashMap<String, String> cmds = new HashMap<String, String>();
	public HashMap<Player, Boolean> amMap = new HashMap<Player, Boolean>();
	public HashMap<Player, Boolean> chatMap = new HashMap<Player, Boolean>();

	protected DubtrackBuilder builder;
	protected DubtrackAPI dubtrack;
	protected Room room;
	protected String id;

	private HoverEvent hoverEvent;
	private ClickEvent clickEvent;

	public void onEnable() {
		log = getLogger();
		instance = this;
		config = this.getConfig();
		Utils.loadConfig();
		this.getCommand("dubtrack").setExecutor(new DubtrackCommand());
		getServer().getPluginManager().registerEvents(new EventListener(), this);
		initCmds();
		if (!config.getString("settings.username").equalsIgnoreCase("username") || 
			!config.getString("settings.password").equalsIgnoreCase("password")) {
			Utils.getRoomId();
			init();
			register();
		} else {
			log.severe("Username and/or password not found!");
		}
	}

	protected void init() {
		try {
			builder = new DubtrackBuilder(
					config.getString("settings.username"), 
					config.getString("settings.password"));
			dubtrack = builder.buildAndLogin();
			room = dubtrack.joinRoom(id);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		setEvents();
	}

	private void setEvents() {
		clickEvent = new ClickEvent(ClickEvent.Action.OPEN_URL, DUB_URL + config.getString("settings.room"));
		hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(config.getString("lang.tooltip").replaceAll("&", "§")));
	}

	protected void register() {
		if (config.getBoolean("settings.announcements") || 
			config.getBoolean("hooks.irc.announcements.enabled") || 
			config.getBoolean("hooks.discord.announcements.enabled")) {

				dubtrack.getEventBus().register(SongChangeEvent.class, event -> {
					if (config.getBoolean("settings.announcements")) {
						if (config.getBoolean("settings.announcements")) {
							String display = config.getString("lang.display")
									.replaceAll("&", "§")
									.replaceAll("%song%", event.getNewSong().getSongInfo().getName())
									.replaceAll("%dj%", event.getNewSong().getUser().getUsername());
							String am = config.getString("lang.announcement")
									.replaceAll("&", "§")
									.replaceAll("%url%", DUB_URL + config.getString("settings.room"))
									.replaceAll("%display%", display)
									.replaceAll("%song%", event.getNewSong().getSongInfo().getName())
									.replaceAll("%dj%", event.getNewSong().getUser().getUsername());
							String[] amSplit = am.split("%new%");
							for (String _am : amSplit) {
								TextComponent tc = new TextComponent(TextComponent.fromLegacyText(_am));
								tc.setClickEvent(clickEvent);
								tc.setHoverEvent(hoverEvent);
								for (Player _p : Bukkit.getOnlinePlayers()) {
									if (!amMap.get(_p)) {
										_p.spigot().sendMessage(tc);
									}
								}
							}
						}
					}

					// if (config.getBoolean("hooks.irc.announcements.enabled")) {

					// if (config.getBoolean("hooks.discord.announcements.enabled")) {
					
				});
			}

		if (config.getBoolean("settings.chat") || 
			config.getBoolean("hooks.irc.chat.enabled") || 
			config.getBoolean("hooks.discord.chat.enabled")) {
		
			dubtrack.getEventBus().register(UserChatEvent.class, event -> {
				if (config.getBoolean("settings.chat.dub-to-game")) {
					String format = Utils.color("lang.chat.dub-to-game")
							.replaceAll("%user%", event.getMessage().getUser().getUsername())
							.replaceAll("%message%", event.getMessage().getContent());
					for (Player _p : Bukkit.getOnlinePlayers()) {
						if (!chatMap.get(_p)) {
							_p.sendMessage(format);
						}
					}
				}

				// if (config.getBoolean("hooks.irc.chat.enabled")) {

				// if (config.getBoolean("hooks.discord.chat.enabled")) {

			});
		}
	}
	
	public void initCmds() {
		cmds.put("reload", "admin");
		cmds.put("reconnect", "admin");
		cmds.put("reset", "admin");
		cmds.put("ban", "mod");
		cmds.put("unban", "mod");
		cmds.put("kick", "mod");
		cmds.put("skip", "mod");
		cmds.put("ban", "mod");
		cmds.put("mute", "use");
		cmds.put("hide", "use");
		cmds.put("default", "use");
	}
	
	public HashMap<String, String> getCmds() {
		return cmds;
	}

	protected static DubtrackUtils getInstance() {
		return instance;
	}

	protected DubtrackBuilder getBuilder() {
		return builder;
	}

	protected DubtrackAPI getAPI() {
		return dubtrack;
	}

	public Room getRoom() {
		return room;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void putAmMap(Player p, boolean b) {
		this.amMap.put(p, b);
	}
	
	public HashMap<Player, Boolean> getAmMap() {
		return amMap;
	}
	
	public void putChatMap(Player p, boolean b) {
		this.chatMap.put(p, b);
	}
	
	public HashMap<Player, Boolean> getChatMap() {
		return chatMap;
	}
}