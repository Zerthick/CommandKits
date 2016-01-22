# Command Kits
Command Kits is a simple yet flexible command kit plugin. Groups of commands called "kits" can be defined in the plugin config file. Players can then execute these kits all at once with one command. Requirements can also be set on the kits to only allow players to execute them under certain circumstances.

You can check out the Command Kits Sponge Forum post [here](https://forums.spongepowered.org/t/command-kits-v1-0-0-kits-of-commands-and-items/10270 "Command Kits Forum Post")!

#Commands:
    /ck <kitName> - Attempts to execute the Command Kit with the specified kitName. (Aliases: ck, commandKits)
    /ck pluginInfo - Displays version information about the Command Kits Plugin.
    /ck list - Lists available command kits, kits for which the player does not have permission will not be displayed.  Kits which the player meets the requirements for will be displayed in GREEN, kits which the player does not meet the requirments for will be displayed in RED.
    /ck info <kitName> - Displays the description of the kit with the name kitName, as well of any requirements it may have.  Requirements which the player meets will be displayed in GREEN, requirements which the player does not meet will be displayed in RED.  If the player meets all requirments the border of the display will be GREEN, otherwise it will be RED.
    /ck reload - Reloads the configuration file for Command Kits
#Permissions
    commandKits.command.select
    commandKits.command.pluginInfo
    commandKits.command.list
    commandKits.command.kitInfo
    commandKits.command.reloadConfig

#Defining a Simple Kit:
So you may be wondering what the point of this plugin is or how it works.  At the core of Command Kits is the concept of a "kit", while this is similar to kits from other kit plugins instead of holding items, these kits hold commands.  In order to define a "kit" simply create an entry in the kits section of the config like so:

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

As of Command Kits v0.2.1 you can also access arguments passed in by the player!

Arguments can be passed in after the kit name. Example: ```/ck <kitName> [arg1] [arg2] ... ```
You can access these arguments in your commands via the ```ARGS_``` drop-in type
For example to access the first argument in  a command simply append the index 1 to the drop-in ```{ARGS_1}```
If you simply want to insert **all** arguments in a drop-in (ex: for a message input) you can use ```{ARGS_*}```
You can also specify ranges of args to insert, for example 1 to 3 ```{ARGS_1-3}```
If you want to specify a starting index, but include all args after it we can also use ```*```, for example all arguments from 2 forward ```{ARGS_2-*}```
Similarly if we wanted all arguments up to and including the 2nd one we could use ```{ARGS_*-2}```

As of Command Kits v0.3.1 you can now also check to see if a player has a particular permission with the ```PERM_``` drop-in type.  For example to check if the player has the permission ```some.permission.example``` we can simply append the permission name to the drop-in like so ```{PERM_some.permission.example}```, this will evaluate to ```true``` if the player has the permission or ```false``` if the player doesn't have the permission.

##The Potatoes - Requirements
"Zerthick, didn't you say something about requirements earlier, where are those?"
Now we get to the other half of Command Kits, **requirements**.

In addition to the three sections mentioned earlier, kits can have an optional **requirements** section, where each condition in the section must be true in order for the player to execute the command.  The most basic requirement is a **permission**, for example:

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

This kit will only be able to be executed by a player with the permission ```commandKits.example```, in addition, if a player does not have permission to use a kit, they will not be able to call ```/ck info``` on that kit, nor will it be visible when they call ```/ck list```.

"What about drop-ins, do they have a role to play here to?" 
Yes!  Any piece of data accessed via a drop-in can also be used as a requirement for a kit. As of command kits v0.3.1, requirements other than the basic permission requirement are specified as an **advanced requirement** section. 
For example:

    requirements {
        Test {
            # Description of requirement
            description="This requirement specifies that the player must have at least 5 enchanting levels to use this kit."
            # Human-friendly name for requirement
            name="Example Requirement"
            rule="{KEYS_EXPERIENCE_LEVEL} >= 5"
        }
        # This is the permission required to run /kc on this kit
        permission="commandKits.example"
    }

Every advanced requirement consists of a **key** for the section (Test), a **name** (Example Requirement), a **description** (This requirement specifies that the player must have at least 5 enchanting levels to use this kit.), and a **rule** ({KEYS_EXPERIENCE_LEVEL} >= 5). 

- **key** - used solely for your organizational purposes, it can be named what you wish
- **name** - the name of the requirement, will be displayed in the ```/ck info``` command
- **description** - human-friendly description of the requirement, will be displayed in the ```/ck info``` command
- **rule** - this rule, expressed as a **boolean expression** (i.e. either ```true``` or ```false```), will be used to evaluate if the requirement is met by the player

''Hold up a minute Zerthick, you're saying we can express requirements as a boolean expression?"
YES!  This is the big feature of Command Kits v0.3.1, for values representing numbers the following operations are supported ```+, -, *, /, %, <, >, <=, >=, ==, !=```.  All numbers are evaluated as **doubles** when performing addition, subtraction, multiplication, division, and modulo division!  Strings are denoted encased in single quotes ex: ``` 'This is a String' ``` and support the following operations ```==, !=, +(concatenation)```.  The following boolean operations are also available ```&&(and), ||(or), !(not)```

There are two requirements when writing rule expressions

**1.** Rules must evaluate to either **true** or **false**
**2.** All tokens in rules must be separated by spaces

The first requirement is so that Command Kits can determine whether or not the player has a requirement, the second is because I need to improve my regex skills :wink:.

Examples:
```rule="3 + 4"``` fails Requirement 1: ```3 + 4 = 5``` not ```true``` or ```false```
```rule="3+4<5"``` fails Requirement 2: tokens not separated by spaces
```rule="3 + 4 < 5"``` valid, tokens are separated by spaces and evaluates to either ```true``` or ```false```, in this case ```false```.  

So for example, say we have are requirement that in order to use a kit a player must have a permission "mykit.use", be in a world named "MyWorld", and either be flying or have at least 30 levels of exp. Then we could have the following requirements section:

    requirements {
        MyWorldFlyingExp {
            description="You must be in MyWorld and have either 30 levels of xp or be flying"
            name="MyWorld: Flying or 30 Exp Levles"
            rule="{WORLD_NAME} == 'MyWorld' && ( {KEYS_IS_FLYING} == true || {KEYS_EXPERIENCE_LEVEL} >= 30 )"
        }
        permission="mykit.use"
    }


You can specify as many different advanced requirements as you like, but they must all be true in order for a player to use a kit!
##Vanilla Minecraft and Beyond!
As you have seen, all of my examples use vanilla Minecraft commands, but the beauty of Command Kits is that it will work with **any** plugin that supports player commands, so long as you can define appropriate arguments for them.   Have a spell plugin that uses commands, but only want you players to be able to cast spells in a certain world or with a certain level of exp?  Bind the spell to a Command Kit!  Have an item kit plugin, but want more control over when players can spawn a kit?  Bind the kit command to a Command Kit!  Or say you have a whole bunch of plugins and whenever you have to accomplish a task you type in the same series of commands over and over, bind the sequence to a command kit and knock it out all with one command!
