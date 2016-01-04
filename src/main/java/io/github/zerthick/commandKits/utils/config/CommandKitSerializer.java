/*
 * Copyright (C) 2016  Zerthick
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

package io.github.zerthick.commandKits.utils.config;

import com.google.common.reflect.TypeToken;
import io.github.zerthick.commandKits.cmdKit.CommandKit;
import io.github.zerthick.commandKits.cmdKit.CommandKitRequirement;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class CommandKitSerializer implements TypeSerializer<CommandKit>{

    @Override
    public CommandKit deserialize(TypeToken type, ConfigurationNode value) throws ObjectMappingException {

        final String name = value.getNode("name").getString();
        final String description = value.getNode("description").getString("No description available!");
        final String permission = value.getNode("requirements", "permission").getString("");
        final String message = value.getNode("message").getString("");
        final Long interval = value.getNode("interval").getLong(0);
        final Set<CommandKitRequirement> requirements = new HashSet<>();
        Set<Object> keySet = value.getNode("requirements").getChildrenMap().keySet();
        for(Object o : keySet){
            if(!o.toString().equals("permission")) {
                CommandKitRequirement requirement = value.getNode("requirements", o)
                        .getValue(TypeToken.of(CommandKitRequirement.class));
                requirements.add(requirement);
            }
        }
        final List<String> commands = value.getNode("commands").getList(TypeToken.of(String.class), new LinkedList<>());
        final List<String> items = value.getNode("items").getList(TypeToken.of(String.class), new LinkedList<>());
        return new CommandKit(name, description, permission, message, requirements, commands, items, interval);
    }

    @Override
    public void serialize(TypeToken<?> type, CommandKit obj, ConfigurationNode value) throws ObjectMappingException {
        value.getNode("name").setValue(obj.getName());
        value.getNode("description").setValue(obj.getDescription());
        value.getNode("requirements", "permission").setValue(obj.getPermission());
        value.getNode("message", "message").setValue(obj.getMessage());
        value.getNode("requirements").setValue(obj.getRequirements());
        value.getNode("commands").setValue(obj.getCommands());
        value.getNode("items").setValue(obj.getItems());
        value.getNode("interval").setValue(obj.getInterval());
    }
}
