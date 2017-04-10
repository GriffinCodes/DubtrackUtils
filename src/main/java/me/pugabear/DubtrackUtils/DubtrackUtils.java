package me.pugabear.DubtrackUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.sponges.dubtrack4j.DubtrackAPI;
import io.sponges.dubtrack4j.DubtrackBuilder;
import io.sponges.dubtrack4j.event.room.SongChangeEvent;
import io.sponges.dubtrack4j.event.user.UserChatEvent;
import io.sponges.dubtrack4j.framework.Room;

public class DubtrackUtils extends JavaPlugin {
	private final static String NAME = "DubtrackUtils";
	public static DubtrackUtils instance;
	protected static FileConfiguration config; 
	public static Logger log;

	private final static String SEP = System.getProperty("file.separator");
	private static File fileFolder = new File("plugins" + SEP + NAME);
	private static File fileConfig = new File("plugins" + SEP + NAME + SEP + "config.yml");

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
		this.getCommand("dubtrack").setExecutor(new DubtrackCommand());
		getRoomId();
		loadConfig();
		init();
		register();
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
		clickEvent = new ClickEvent(ClickEvent.Action.OPEN_URL, "https://dubtrack.fm/join/" + config.getString("settings.room"));
		hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(config.getString("lang.tooltip").replaceAll("&", "§")));
	}

	protected void register() {
		if (config.getBoolean("settings.announcements") || 
				config.getBoolean("hooks.irc.announcements.enabled") || 
				config.getBoolean("hooks.discord.announcements.enabled")) {

			dubtrack.getEventBus().register(SongChangeEvent.class, event -> {
				if (config.getBoolean("settings.announcements")) {
					String url = "https://dubtrack.fm/join/" + config.getString("settings.room");
					String display = config.getString("lang.display")
							.replaceAll("&", "§")
							.replaceAll("%song%", event.getNewSong().getSongInfo().getName())
							.replaceAll("%dj%", event.getNewSong().getUser().getUsername());
					String am = config.getString("lang.announcement")
							.replaceAll("&", "§")
							.replaceAll("%url%", url)
							.replaceAll("%display%", display)
							.replaceAll("%song%", event.getNewSong().getSongInfo().getName())
							.replaceAll("%dj%", event.getNewSong().getUser().getUsername());
					String[] amSplit = am.split("%new%");
					for (String _am : amSplit) {
						TextComponent tc = new TextComponent(TextComponent.fromLegacyText(_am));
						tc.setClickEvent(clickEvent);
						tc.setHoverEvent(hoverEvent);
						Bukkit.spigot().broadcast(tc);
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
				if (config.getBoolean("settings.chat")) {
					String format = config.getString("lang.chat")
							.replaceAll("&", "§")
							.replaceAll("%user%", event.getMessage().getUser().getUsername())
							.replaceAll("%message%", event.getMessage().getContent());
					Bukkit.broadcastMessage(format);
				}

				// if (config.getBoolean("hooks.irc.chat.enabled")) {

				// if (config.getBoolean("hooks.discord.chat.enabled")) {

			});
		}
	}

	protected void getRoomId() {
		try {
			URL roomAPI = new URL("https://api.dubtrack.fm/room/" + config.getString("settings.room"));
			HttpURLConnection connection = (HttpURLConnection) roomAPI.openConnection();
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB;     rv:1.9.2.13) Gecko/20101203 Firefox/3.6.13 (.NET CLR 3.5.30729)");
			BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String response = rd.readLine();
			connection.disconnect();

			JsonParser jsonParser = new JsonParser();
			JsonObject resultCount = jsonParser
					.parse(response).getAsJsonObject()
					.get("data").getAsJsonObject();

			id = resultCount.get("_id").getAsString();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void loadConfig() {
		if (!fileFolder.exists()) {
			fileFolder.mkdir();
		}

		if (!fileConfig.exists()) {
			instance.saveDefaultConfig();
		}

		try {
			config.load(fileConfig);
		} catch (Exception e) {
			log.info("Could not load config file; you may need to regenerate it. Error: " + e.getMessage());
			e.printStackTrace();
		}
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

	protected Room getRoom() {
		return room;
	}
}