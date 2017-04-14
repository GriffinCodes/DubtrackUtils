# DubtrackUtils
Hook Dubtrack into your minecraft server!

*Credit to [@sponges](https://github.com/sponges) / [@zombachu](https://github.com/zombachu) for the [Dubtrack4J](https://github.com/PugaBear/Dubtrack4J) library*

## Features
- Song update announcements
- Display dubtrack chat in minecraft and vice-versa
- Hide announcements/chat per person
- Kick/ban dubtrack users from ingame
- Role management from ingame
- Skip the current song from ingame
- Send announcements and chat to IRC via [PurpleIRC](https://www.spigotmc.org/resources/purpleirc.2836/)
- [*Fully* customizable](https://github.com/PugaBear/DubtrackUtils/blob/master/src/main/resources/config.yml) - every message and every feature.

### Planned features
- Discord hook
- Grab other misc. information about the dubtrack room

## Commands
### Basic
**Permission: dubtrackutils.use**
- `/dubtrack` - View information about dubtrack, a link, and what is currently playing. 
- `/dubtrack hide [chat]` - Hide announcements or chat (by adding 'chat') 
### Mod
**Permission: dubtrackutils.mod**
- `/dubtrack kick <user>` - Kick a user from the dubtrack room
- `/dubtrack ban <user>` - Ban a user from the dubtrack room <!-- - `/dubtrack unban <user>` - Unban a user from the dubtrack room -->
- `/dubtrack skip` - Skip the current song
### Roles
**Permission: dubtrackutils.roles AND dubtrackutils.roles.set.<role>**
- `/dubtrack roles set <user> <role>` - Set a user role. Valid roles are `DJ`, `Resident_DJ`, `VIP`, `Mod`, `Manager`, and `Co_Owner`. The Co-Owner role cannot be set unless DubtrackUtils logs in with the room creator's account.
- `/dubtrack roles clear <user>` - Clear a user's roles.
### Admin
**Permission: dubtrackutils.admin**
- `/dubtrack reload` - Reload the configuration file 
- `/dubtrack reconnect` - Reconnect to dubtrack 
- `/dubtrack reset` - Reload the configuration file and reconnect to dubtrack 

NOTE: The `reconnect` and `reset` commands will pause your server for ~2-5 seconds. It shouldn't cause any problems.

## Download
[DubtrackUtils.jar](http://dl.bn-mc.net/?q=dubtrackutils)

## Installing
- Drag and drop into plugins folder
- Stop / start server
- Edit the [config.yml](https://github.com/PugaBear/DubtrackUtils/blob/master/src/main/resources/config.yml)
- `/dubtrack reset`

## Building
You will first need to clone the [Dubtrack4J](https://github.com/PugaBear/Dubtrack4J) library and the [PurpleIRC plugin](https://github.com/PugaBear/PurpleIRC) and install them to your local Maven cache.
```
git clone https://github.com/PugaBear/Dubtrack4J.git
cd Dubtrack4J
mvn clean install
cd ..
git clone https://github.com/PugaBear/PurpleIRC.git
cd PurpleIRC
mvn clean install
cd ..
git clone https://github.com/PugaBear/DubtrackUtils.git
cd DubtrackUtils
mvn clean package
```
The jar will be inside the `target` folder.

## Contact
### Discord
[<img src="https://discordapp.com/api/guilds/132680070480396288/widget.png?style=shield">](https://discord.gg/0jwsKTH4ATkkN8iB)

or Griffin#2583
### IRC
[#Bear-Nation on irc.spi.gt](http://irc.bn-mc.net)
