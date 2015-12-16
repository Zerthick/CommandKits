# Command Kits
Command Kits is a simple yet flexible command kit plugin. Groups of commands called "kits" can be defined in the plugin config file.  Players can then execute these kits all at once with one command.  Requirments can also be set on the kits to only allow players to execute them under certain circumstances. 

You can check out the Command Kits Sponge Forum post [here](https://forums.spongepowered.org/t/wip-graveyards-v0-1-0-pre-defined-spawnpoints-for-players/9575 "Command Kits Forum Post")!
 
##Commands
```
/ck <kitName> - Attempts to execute the Command Kit with the specified kitName. (Aliases: ck, commandKits)
/ck pluginInfo - Displays version information about the Command Kits Plugin.
/ck list - Lists available command kits, kits for which the player does not have permission will not be displayed.  Kits which the player meets the requirements for will be displayed in GREEN, kits which the player does not meet the requirments for will be displayed in RED.
/ck info <kitName> - Displays the description of the kit with the name kitName, as well of any requirements it may have.  Requirements which the player meets will be displayed in GREEN, requirements which the player does not meet will be displayed in RED.  If the player meets all requirments the border of the display will be GREEN, otherwise it will be RED.
```

##Permissions
```
commandKits.command.select
commandKits.command.pluginInfo
commandKits.command.list
commandKits.command.kitInfo
```
