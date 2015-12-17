# Command Kits
Command Kits is a simple yet flexible command kit plugin. Groups of commands called "kits" can be defined in the plugin config file. Players can then execute these kits all at once with one command. Requirements can also be set on the kits to only allow players to execute them under certain circumstances.

You can check out the Command Kits Sponge Forum post [here](https://forums.spongepowered.org/t/wip-graveyards-v0-1-0-pre-defined-spawnpoints-for-players/9575 "Command Kits Forum Post")!

#Commands:
    /ck <kitName> - Attempts to execute the Command Kit with the specified kitName. (Aliases: ck, commandKits)
    /ck pluginInfo - Displays version information about the Command Kits Plugin.
    /ck list - Lists available command kits, kits for which the player does not have permission will not be displayed.  Kits which the player meets the requirements for will be displayed in GREEN, kits which the player does not meet the requirments for will be displayed in RED.
    /ck info <kitName> - Displays the description of the kit with the name kitName, as well of any requirements it may have.  Requirements which the player meets will be displayed in GREEN, requirements which the player does not meet will be displayed in RED.  If the player meets all requirments the border of the display will be GREEN, otherwise it will be RED.

#Permissions
    commandKits.command.select
    commandKits.command.pluginInfo
    commandKits.command.list
    commandKits.command.kitInfo

#Defining a Simple Kit:
So you may be wondering what the point of this plugin is or how it works.  At the core of Command Kits is the concept of a "kit", while this is similar to kits from other kit plugins instead of holding items, these kits hold commands.  In order to define a "kit" simply create an entry in the kits section of the config like so:
```
    kits {
        Test {
            commands=[
                "minecraft:me I used the example kit!",
                "minecraft:me It was really fun"
            ]
            description="This is an example kit"
            name=Example
        }
    }
 ```
Every kit consists of a **key** for the section (Test), a **name** (Example), a **description** (This is an example kit), and a list of **commands** (["minecraft:me I used the example kit!","minecraft:me It was really fun"]). 

  - **key** - used solely for your organizational purposes, it can be named what you wish
  - **name** - this is what players will used to access the kit within commands
  - **description** - this is what will be returned to players when they call ```/ck info``` on this kit
  - **commands** - these commands will be executed when this kit is run with the ```/ck``` command, by defualt they will be executed by the player

##The Meat - Commands
"Zerthick, this is all well and good, but what happens if the commands I want to execute need to be executed from the console, or take arguments?"  
Well, I'm glad you asked!  In order to execute a command from the console instead of the player simply prefix a command with a '$'.  So for example in order to execute the ```time set day``` command from the console I could add the following command to the ```commands``` section of a kit:

`"$minecraft:time set day"`

"So what about arguments then?"
Now we get into some of the fun stuff, the **drop-in system**.  The way the drop-in system works is by defining several "drop-ins" that will be inserted into the command at run-time, for example one such drop-in is ```PLAYER_NAME```.  In order to use drop-ins simply enclose the requested drop-in in curly-braces like so:

```"$minecraft:tell {PLAYER_NAME} Hello!"```

This will make the console tell whatever player executed this kit "Hello!" Some available drop-ins include:

- ```PLAYER_NAME```
- ```PLAYER_UUID```
- ```WORLD_NAME```
- ```WORLD_UUID```

But that's not all!  In addition to those predefined drop-ins, you can also access most of the player-supported data keys found in the [Keys Enum](https://jd.spongepowered.org/org/spongepowered/api/data/key/Keys.html).  (I plan to support them all once ```Player.getKeys()``` doesn't throw an ```AbstractMethodError``` :stuck_out_tongue_winking_eye:).  These are accessed like normal dropn-ins, except whatever key you wish to access must be prefixed by ```KEYS_```.  So for example let's say we wanted to double the amount of xp levels a player has, we could do so with the following command

```"$minecraft:xp {KEYS_EXPERIENCE_LEVEL}L {PLAYER_NAME}"```

This does however pose the limitation that all arguments to a command must be determined beforehand or through drop-ins, in the future I may add additional sources for command arguments.

##The Potatoes - Requirements
"Zerthick, didn't you say something about requirements earlier, where are those?"
Now we get to the other half of Command Kits, **requirements**.

In addition to the three sections mentioned earlier, kits can have an optional **requirements** section, where each condition in the section must be true in order for the player to execute the command.  The most basic requirement is a **permission**, for example:
```
    kits {
        Example {
            commands=[
                "minecraft:me I used the example kit!",
                "$minecraft:tell {PLAYER_NAME} You used the example kit!"
            ]
            description="This is an example kit"
            name=Example
            requirements {
                permission="commandKits.example"
            }
        }
    }
```
This kit will only be able to be executed by a player with the permission ```commandKits.example```, in addition, if a player does not have permission to use a kit, they will not be able to call ```/ck info``` on that kit, nor will it be visible when they call ```/ck list```.

"What about drop-ins, do they have a role to play here to?" 
Yes!  Any piece of data accessed via a drop-in can also be used as a requirement for a kit, for example, let's say we have a kit that we only want me to be able to execute, but we want others to view its info, then to our requirements we could add:

```PLAYER_NAME="Zerthick" ```

Now only players with the name "Zerthick" will be able to use this kit.  The four basic dropins currently only support equality checks, though that may change in a future update.

"What about drop-ins representing data keys, can we use those too?"
For data keys we set the requirement key just as you'd expect, but we can get more detailed in the requirement itself.

 - Data keys returning a type of ```Boolean``` support the operations ``=`` and ```!=``` and can either be ```true``` or ```false```
 - Data keys returning a type of ```Integer``` or ```Double``` support the operations ```=```, ```!=```, ```<```, ```>```, ```<=```, and ```>=``` 
 - All other supported keys will support the operations ```=``` and ```!=``` and to be evaluated by a ```.toString()``` comparison. 

So for example, say we have are requirement that in order to use a kit a player must have a permission "mykit.use", be in a world named "MyWorld", be flying, and have at least 30 levels of exp. Then we could have the following requirements section:

```
    requirements{
        permission="mykit.use"
        WORLD_NAME="MyWorld"
        KEYS_IS_FLYING="=true"
        KEYS_EXPERIENCE_LEVEL=">=30"
    }
```

Currently requirements do not support ```AND/OR``` logic, nor can you have more than one requirement of the same key, but that may be coming in a future release :smile:.

##Vanilla Minecraft and Beyond!
As you have seen, all of my examples use vanilla Minecraft commands, but the beauty of Command Kits is that it will work with **any** plugin that supports player commands, so long as you can define appropriate arguments for them.   Have a spell plugin that uses commands, but only want you players to be able to cast spells in a certain world or with a certain level of exp?  Bind the spell to a Command Kit!  Have an item kit plugin, but want more control over when players can spawn a kit?  Bind the kit command to a Command Kit!  Or say you have a whole bunch of plugins and whenever you have to accomplish a task you type in the same series of commands over and over, bind the sequence to a command kit and knock it out all with one command!
