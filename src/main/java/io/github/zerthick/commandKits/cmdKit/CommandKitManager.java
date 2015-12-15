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

import org.spongepowered.api.entity.living.player.Player;

import java.util.HashMap;
import java.util.Map;

public class CommandKitManager {

    private final Map<String, CommandKit> kits;

    public CommandKitManager(Map<String, CommandKit> kits) {
        this.kits = kits;
    }

    public Map<String, CommandKit> getKits() {
        return kits;
    }

    public boolean isKit(String kitName){
        return kits.containsKey(kitName);
    }

    public CommandKit getKit(String kitName){
        if(isKit(kitName)){
            return kits.get(kitName);
        }
        return null;
    }
}
