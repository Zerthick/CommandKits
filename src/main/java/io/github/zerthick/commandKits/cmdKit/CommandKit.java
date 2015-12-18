/*
 * Copyright (C) 2015  Zerthick
 *
 * This file is part of CommandKits.
 *
 * CommandKits is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * CommandKits is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CommandKits.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.zerthick.commandKits.cmdKit;

import io.github.zerthick.commandKits.utils.string.StringParser;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Texts;

import java.util.*;

public class CommandKit {

    private final String name;
    private final String description;
    private final String permission;
    private final Set<CommandKitRequirement> requirements;
    private final List<String> commands;

    public CommandKit(String name, String description, String permission, Set<CommandKitRequirement> requirements, List<String> commands) {
        this.name = name;
        this.description = description;
        this.permission = permission;
        this.requirements = requirements;
        this.commands = commands;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPermission() { return  permission; }

    public Set<CommandKitRequirement> getRequirements() {
        return requirements;
    }

    public List<String> getCommands() {
        return commands;
    }

    public boolean hasPermission(Player player){
        return permission.isEmpty() || player.hasPermission(permission);
    }

    public boolean hasRequirements(Player player) {
        for(CommandKitRequirement requirement : requirements){
            if(!requirement.hasRequirement(player)){
                return false;
            }
        }
        return true;
    }

    public void executeCommands(Player player, String[] args){
        for(String command : commands){
            String parsedCommand = StringParser.parseCommand(player, command, args);
            if(parsedCommand.startsWith("$")){
                Sponge.getGame().getCommandManager().process(Sponge.getGame().getServer().getConsole(), parsedCommand.substring(1));
            } else {
                Sponge.getGame().getCommandManager().process(player, parsedCommand);
            }
        }
    }

    @Override
    public String toString() {
        return "CommandKit{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", permission='" + permission + '\'' +
                ", requirements=" + requirements +
                ", commands=" + commands +
                '}';
    }
}
