package me.pugabear.DubtrackUtils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class DubtrackCommand implements CommandExecutor {
	private DubtrackUtils dta = DubtrackUtils.getInstance();
	private FileConfiguration config = DubtrackUtils.getInstance().getConfig();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length != 0) {
			if (!sender.hasPermission("dubtrackutils.admin")) {
				sender.sendMessage(config.getString("lang.noperm")
						.replaceAll("&", "§"));
				return true;
			} else { 
				boolean b = true;
				switch (args[0]) {
					case "reload": 
						DubtrackUtils.loadConfig();
						break;
					case "reconnect":
						dta.getAPI().logout();
						dta.getRoomId();
						dta.init();
						dta.register();
						break;
					case "reset":
						dta.getAPI().logout();
						DubtrackUtils.loadConfig();
						dta.getRoomId();
						dta.init();
						dta.register();
						break;
					default:
						b = false;
				}
				if (b) {
					sender.sendMessage(config.getString("lang.admin.prefix").replaceAll("&", "§") +
							config.getString("lang.admin." + args[0]).replaceAll("&", "§"));
					return true;
				}
			}
		}

		String url = "https://dubtrack.fm/join/" + config.getString("settings.room");
		String info = config.getString("lang.info")
				.replaceAll("&", "§");

		String join = config.getString("lang.join")
				.replaceAll("&", "§")
				.replaceAll("%url%", url);

		String display = config.getString("lang.display")
				.replaceAll("&", "§")
				.replaceAll("%song%", dta.getRoom().getCurrentSong().getSongInfo().getName())
				.replaceAll("%dj%", dta.getRoom().getCurrentSong().getUser().getUsername());

		String cp = config.getString("lang.currentlyplaying")
				.replaceAll("&", "§")
				.replaceAll("%display%", display)
				.replaceAll("%song%", dta.getRoom().getCurrentSong().getSongInfo().getName())
				.replaceAll("%dj%", dta.getRoom().getCurrentSong().getUser().getUsername());

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

		return true;
	}
}