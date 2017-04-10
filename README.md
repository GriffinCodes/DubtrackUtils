# DubtrackUtils
Hook Dubtrack into your minecraft server!

*Credit to [@sponges](https://github.com/sponges) / [@zombachu](https://github.com/zombachu) for the [Dubtrack4J](https://github.com/zombachu/Dubtrack4J) library*

## Features
- Song update announcements
- Send dubtrack chat to minecraft
- [*Fully* customizable](https://github.com/PugaBear/DubtrackUtils/blob/master/src/main/resources/config.yml)- every message and every feature.

### Planned features
- IRC/Discord hooks
- Send minecraft chat back to dubtrack
- Ability to turn off dubtrack announcements/chat per person.
- Ability to grab other misc. information about the dubtrack room
- (maybe) Ability to ban/kick dubtrack users from ingame

## Commands
- `/dubtrack` - View information about dubtrack, a link, and what is currently playing. 
- `/dubtrack reload` - Reload the configuration file 
- `/dubtrack reconnect` - Reconnect to dubtrack
- `/dubtrack reset` - Reload the configuration file and reconnect to dubtrack

## Permissions
`dubtrack.admin` - Reload/reconnect/reset the plugin

## Download
[DubtrackUtils.jar](http://dl.bn-mc.net/?q=dubtrackutils)

## Installing
- Drag and drop into plugins folder
- Stop / start server
- Edit the [config.yml](https://github.com/PugaBear/DubtrackUtils/blob/master/src/main/resources/config.yml)
- `/dubtrack reset`

## Building
You will first need to download & build the [Dubtrack4J](https://github.com/zombachu/Dubtrack4J) library and place the jar inside `libs/repo/dubtrack/dubtrack4j/1.06-SNAPSHOT/`
```
git clone https://github.com/zombachu/Dubtrack4J.git
cd Dubtrack4J
rm src/test/ -R
mvn clean package
cd ..
git clone https://github.com/PugaBear/DubtrackUtils.git
cd DubtrackUtils
mkdir -p libs/repo/dubtrack/dubtrack4j/1.06-SNAPSHOT
cp ../Dubtrack4J/target/dubtrack4j-1.06-SNAPSHOT.jar libs/repo/dubtrack/dubtrack4j/1.06-SNAPSHOT/
mvn clean package
```
The jar will be inside the `target` folder.

## Contact
### Discord
[<img src="https://discordapp.com/api/guilds/132680070480396288/widget.png?style=shield">](https://discord.gg/0jwsKTH4ATkkN8iB)

or Griffin#2583
### IRC
[#Bear-Nation on irc.spi.gt](http://irc.bn-mc.net)
