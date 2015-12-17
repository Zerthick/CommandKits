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
    private final Map<String, String> requirements;
    private final List<String> commands;

    public CommandKit(String name, String description, Map<String, String> requirements, List<String> commands) {
        this.name = name;
        this.description = description;
        this.requirements = requirements;
        this.commands = commands;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, String> getRequirements() {
        return requirements;
    }

    public List<String> getCommands() {
        return commands;
    }

    public boolean hasPermission(Player player){
        return !requirements.containsKey("permission") || player.hasPermission(requirements.get("permission"));
    }

    public Map<String,Boolean> getRequirementsMap(Player player) {
        Map<String, Boolean> requirementsMap = new HashMap<>();

        for(String key : requirements.keySet()){
            requirementsMap.put(key, StringParser.parseRequirement(player, key, requirements.get(key)));
        }
        return requirementsMap;
    }

    public boolean hasRequirements(Player player) {
        for(String key : requirements.keySet()){
            if(!StringParser.parseRequirement(player, key, requirements.get(key))){
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
                ", requirements=" + requirements +
                ", commands=" + commands +
                '}';
    }
}
