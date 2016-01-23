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

package io.github.zerthick.commandkitsTEMP.cmdkit;

import io.github.zerthick.commandkitsTEMP.utils.string.StringParser;
import org.spongepowered.api.entity.living.player.Player;

public class CommandKitRequirement {

    private final String name;
    private final String description;
    private final String rule;

    public CommandKitRequirement(String name, String description, String rule) {
        this.name = name;
        this.description = description;
        this.rule = rule;
    }

    public boolean hasRequirement(Player player){
        return StringParser.parseRequirement(player, rule);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getRule() {
        return rule;
    }

    @Override
    public String toString() {
        return "CommandKitRequirement{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", rule='" + rule + '\'' +
                '}';
    }
}
