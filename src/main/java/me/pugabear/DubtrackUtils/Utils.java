package me.pugabear.DubtrackUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.bukkit.configuration.file.FileConfiguration;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Utils {
	private static DubtrackUtils dta = DubtrackUtils.getInstance();
	private static FileConfiguration config = DubtrackUtils.getInstance().getConfig();
	private final static String SEP = System.getProperty("file.separator");
	private static File fileFolder = new File("plugins" + SEP + DubtrackUtils.NAME);
	private static File fileConfig = new File("plugins" + SEP + DubtrackUtils.NAME + SEP + "config.yml");
	
	public static String color(String s) {
		return config.getString(s).replaceAll("&", "§");
	}
	
	public static void loadConfig() {
		if (!fileFolder.exists()) {
			fileFolder.mkdir();
		}

		if (!fileConfig.exists()) {
			DubtrackUtils.getInstance().saveDefaultConfig();
		}

		try {
			config.load(fileConfig);
		} catch (Exception e) {
			DubtrackUtils.log.info("Could not load config file; you may need to regenerate it. Error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	protected static void getRoomId() {
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

			dta.setId(resultCount.get("_id").getAsString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
