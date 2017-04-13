package me.pugabear.DubtrackUtils;

import java.io.IOException;
import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.unbescape.html.HtmlEscape;

public class DubtrackCommand implements CommandExecutor {
	private DubtrackUtils dtu = DubtrackUtils.getInstance();
	private FileConfiguration config = DubtrackUtils.getInstance().getConfig();
	private String prefix = Utils.color("lang.prefix");

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		HashMap<String, String> cmds = dtu.getCmds();
		String first = args.length != 0 ? args[0].toLowerCase() : "default";
		String level;
		try {
			level = cmds.get(first);
		} catch (NullPointerException ex) {
			sender.sendMessage(prefix + Utils.color("lang.usage"));
			return true;
		}
		if (!sender.hasPermission("dubtrackutils." + level)) {
			sender.sendMessage(Utils.color("lang.noperm"));
			return true;
		}
		if (level == null) {
			dtu.getLogger().info("level is null");
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
					dtu.getAPI().logout();
					Utils.getRoomId();
					dtu.init();
					dtu.register();
					break;
				case "reset":
					try { dtu.getAPI().logout(); } catch (NullPointerException e) {}
					Utils.loadConfig();
					Utils.getRoomId();
					dtu.init();
					dtu.register();
					break;
				default:
					sender.sendMessage(Utils.color("lang.admin.usage"));
					return true;
				}
				sender.sendMessage(Utils.color("lang.admin." + args[0]));
				return true;
			case "mod":
				try {
					switch (first) {
					case "kick":
						dtu.getRoom().kickUser(dtu.getRoom().getUserByUsername(args[1]));
						break;
					case "ban":
						dtu.getRoom().banUser(dtu.getRoom().getUserByUsername(args[1]));
						break;
					case "unban":
						sender.sendMessage(prefix + "Currently not working, sorry!");
						return true;
//				        Bukkit.getScheduler().runTaskAsynchronously(DubtrackUtils.getInstance(), new Runnable() {
//				            @Override
//				            public void run() {
//				            	String id = Utils.getUserId(args[1]);
//				            	log.info(id);
//				            	dta.getRoom().unbanUser(id);;
//				            }
//				        });
//						break;
					case "skip":
						dtu.getRoom().skipSong();
						sender.sendMessage(Utils.color("lang.mod.skip"));
						return true;
					default:
						sender.sendMessage(Utils.color("lang.mod.usage"));
						return true;
					}
					sender.sendMessage(Utils.color("lang.mod." + first).replaceAll("%user%", args[1]));
				} catch (NullPointerException ex) {
					sender.sendMessage(Utils.color("lang.mod.invaliduser").replaceAll("%user%", args[1]));
				}
				return true;
			case "use":
				switch (first) {
				case "mute":
				case "hide":
					if (!(sender instanceof Player)) {
						sender.sendMessage("You must be ingame to use this command");
						return true;
					}
					if (args.length > 1) { 
						if (args[1].equalsIgnoreCase("chat")) {
							boolean b = !dtu.getChatMap().get((Player) sender);
							dtu.putChatMap((Player) sender, b);
							sender.sendMessage(Utils.color("lang.hide.chat." + b));
							return true;
						}
					}
					boolean b = !dtu.getAmMap().get((Player) sender);
					dtu.putAmMap((Player) sender, b);
					sender.sendMessage(Utils.color("lang.hide.announcements." + b));
					return true;
				default:
					String url = dtu.DUB_URL + config.getString("settings.room");
					String info = Utils.color("lang.info");

					String join = Utils.color("lang.join")
							.replaceAll("%url%", url);

					String song = HtmlEscape.unescapeHtml(dtu.getRoom().getCurrentSong().getSongInfo().getName());
					String dj = HtmlEscape.unescapeHtml(dtu.getRoom().getCurrentSong().getUser().getUsername());
					
					String display = Utils.color("lang.display")
							.replaceAll("%song%", song)
							.replaceAll("%dj%", dj);

					String cp = Utils.color("lang.currentlyplaying")
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
					return true;
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException ex) {
			sender.sendMessage(prefix + Utils.color("lang.mod.usage"));
		} 
		return true;
	}
}