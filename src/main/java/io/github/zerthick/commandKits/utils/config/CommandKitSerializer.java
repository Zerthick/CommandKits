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

package io.github.zerthick.commandKits.utils.config;

import com.google.common.reflect.TypeToken;
import io.github.zerthick.commandKits.cmdKit.CommandKit;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CommandKitSerializer implements TypeSerializer<CommandKit>{
    @Override
    public CommandKit deserialize(TypeToken type, ConfigurationNode value) throws ObjectMappingException {

        final String name = value.getNode("name").getString();
        final String description = value.getNode("description").getString("No description available!");
        final Map<String, String> requirements = new HashMap<>();
        Set<Object> keySet = value.getNode("requirements").getChildrenMap().keySet();
        for(Object o : keySet){
            requirements.put(o.toString(), value.getNode("requirements", o).getString());
        }
        final List<String> commands = value.getNode("commands").getList(TypeToken.of(String.class));

        return new CommandKit(name, description, requirements, commands);
    }

    @Override
    public void serialize(TypeToken<?> type, CommandKit obj, ConfigurationNode value) throws ObjectMappingException {
        value.getNode("name").setValue(obj.getName());
        value.getNode("description").setValue(obj.getDescription());
        value.getNode("requirements").setValue(obj.getRequirements());
        value.getNode("commands").setValue(obj.getCommands());
    }
}
