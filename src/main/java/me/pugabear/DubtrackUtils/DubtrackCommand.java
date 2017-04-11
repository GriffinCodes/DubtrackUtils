package me.pugabear.DubtrackUtils;

import java.io.IOException;
import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class DubtrackCommand implements CommandExecutor {
	private DubtrackUtils dta = DubtrackUtils.getInstance();
	private FileConfiguration config = DubtrackUtils.getInstance().getConfig();
	private String prefix = config.getString("lang.prefix").replaceAll("&", "§");

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		HashMap<String, String> cmds = dta.getCmds();
		String first = args.length != 0 ? args[0] : "default";
		String level;
		try {
			level = cmds.get(first);
		} catch (NullPointerException e) {
			sender.sendMessage(prefix + Utils.color("lang.usage"));
			return true;
		}
		if (!sender.hasPermission("dubtrackutils." + level)) {
			sender.sendMessage(Utils.color("lang.noperm"));
			return true;
		}
		try {
			switch (level) {
			case "admin":
				switch (first) {
				case "reload": 
					Utils.loadConfig();
					break;
				case "reconnect":
					dta.getAPI().logout();
					Utils.getRoomId();
					dta.init();
					dta.register();
					break;
				case "reset":
					dta.getAPI().logout();
					Utils.loadConfig();
					Utils.getRoomId();
					dta.init();
					dta.register();
					break;
				default:
					sender.sendMessage(prefix + Utils.color("lang.admin.usage"));
					return true;
				}
				sender.sendMessage(prefix + Utils.color("lang.admin." + args[0]));
				return true;
			case "mod":
				switch (first) {
				case "ban":
					dta.getRoom().banUser(dta.getRoom().getUserByUsername(args[1]));
					break;
				case "unban":
					dta.getRoom().unbanUser(dta.getRoom().getUserByUsername(args[1]).getId());
					break;
				case "kick":
					dta.getRoom().kickUser(dta.getRoom().getUserByUsername(args[1]));
					break;
				case "skip":
					dta.getRoom().skipSong();
					sender.sendMessage(prefix + Utils.color("lang.mod.skip"));
					return true;
				default:
					sender.sendMessage(prefix + Utils.color("lang.mod.usage"));
					return true;
				}
				sender.sendMessage(prefix + Utils.color("lang.mod." + first).replaceAll("%user%", args[1]));
				return true;
			case "use":
				switch (first) {
				case "mute":
				case "hide":
					// Hide dubtrack messages from player
				default:
					String url = "https://dubtrack.fm/join/" + config.getString("settings.room");
					String info = config.getString("lang.info")
							.replaceAll("&", "§");

					String join = config.getString("lang.join")
							.replaceAll("&", "§")
							.replaceAll("%url%", url);

					String song = dta.getRoom().getCurrentSong().getSongInfo().getName();
					String dj = dta.getRoom().getCurrentSong().getUser().getUsername();
					
					String display = config.getString("lang.display")
							.replaceAll("&", "§")
							.replaceAll("%song%", song)
							.replaceAll("%dj%", dj);

					String cp = config.getString("lang.currentlyplaying")
							.replaceAll("&", "§")
							.replaceAll("%display%", display)
							.replaceAll("%song%", song)
							.replaceAll("%dj%", dj);

					if (!info.isEmpty()) {
						String[] infoSplit = info.split("%new%");
						for (String _info : infoSplit) {
							sender.sendMessage(_info);
						}
					}

					if (!join.isEmpty()) {
						String[] joinSplit = join.split("%new%");
						for (String _join : joinSplit) {
							sender.sendMessage(_join);
						}
					}

					if (!cp.isEmpty()) {
						String[] cpSplit = cp.split("%new%");
						for (String _cp : cpSplit) {
							sender.sendMessage(_cp);
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException ex) {
			sender.sendMessage(prefix + Utils.color("lang.mod.usage"));
		} // catch (InvalidUserException ex) {
//			sender.sendMessage(prefix + Utils.color("lang.mod.invaliduser").replaceAll("%user%", args[1]));
//		}
		return true;
	}
}