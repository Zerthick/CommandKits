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

package io.github.zerthick.commandKits.utils.string;

import io.github.zerthick.commandKits.utils.string.data.DataConverter;
import io.github.zerthick.commandKits.utils.string.dropin.DropinEngine;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Optional;

public class StringParser {

    public static String parseCommand(Player player, String command, String[] args){
        return DropinEngine.replaceDropins(command, player, args);
    }

    public static Boolean parseRequirement(Player player, String key, String comparison){
        if (key.equalsIgnoreCase("permission")){
            return player.hasPermission(comparison);
        } else if (key.equalsIgnoreCase("PLAYER_NAME")){
            return player.getName().equalsIgnoreCase(comparison);
        } else if (key.equalsIgnoreCase("PLAYER_UUID")) {
            return player.getUniqueId().toString().equals(comparison);
        } else if (key.equalsIgnoreCase("WORLD_NAME")){
            return player.getWorld().getName().equalsIgnoreCase(comparison);
        } else if (key.equalsIgnoreCase("WORLD_UUID")) {
            return player.getWorld().getUniqueId().toString().equals(comparison);
        } else if (key.startsWith("KEYS_")) {
            String cleanedKey = key.replace("KEYS_", "");
            return parseDataRequirement(player, cleanedKey, comparison.replaceAll("\\s",""));
        }
        return false;
    }

    private static Boolean parseDataRequirement(Player player, String key, String comparison){

        Optional<?> optionalValue = DataConverter.convertStringToDataValue(player, key);
        boolean result = false;

        if (optionalValue.isPresent()) {

            Object value = optionalValue.get();

            if (value instanceof Boolean) {
                if(comparison.startsWith("=")){
                    result = value.equals(Boolean.parseBoolean(comparison.substring(1)));
                } else if(comparison.startsWith("!=")){
                    result = !value.equals(Boolean.parseBoolean(comparison.substring(2)));
                }
            } else if (value instanceof Integer) {
                if(comparison.startsWith("=")){
                    result = value.equals(Integer.parseInt(comparison.substring(1)));
                } else if(comparison.startsWith("!=")){
                    result =  !value.equals(Integer.parseInt(comparison.substring(2)));
                } else if(comparison.startsWith("<=")){
                    result = (Integer)value <= Integer.parseInt(comparison.substring(2));
                } else if(comparison.startsWith(">=")){
                    result = (Integer)value >= Integer.parseInt(comparison.substring(2));
                } else if(comparison.startsWith("<")){
                    result = (Integer)value < Integer.parseInt(comparison.substring(1));
                } else if(comparison.startsWith(">")){
                    result = (Integer)value > Integer.parseInt(comparison.substring(1));
                }
            } else if (value instanceof Double) {
                if(comparison.startsWith("=")){
                    result = value.equals(Double.parseDouble(comparison.substring(1)));
                } else if(comparison.startsWith("!=")){
                    result =  !value.equals(Double.parseDouble(comparison.substring(2)));
                } else if(comparison.startsWith("<=")){
                    result = (Double)value <= Double.parseDouble(comparison.substring(2));
                } else if(comparison.startsWith(">=")){
                    result = (Double)value >= Double.parseDouble(comparison.substring(2));
                } else if(comparison.startsWith("<")){
                    result = (Double)value < Double.parseDouble(comparison.substring(1));
                } else if(comparison.startsWith(">")){
                    result = (Double)value > Double.parseDouble(comparison.substring(1));
                }
            } else {
                if(comparison.startsWith("=")){
                    result = value.toString().equals(comparison.substring(1));
                } else if(comparison.startsWith("!=")){
                    result = !value.toString().equals(comparison.substring(2));
                }
            }
        }
        return result;
    }
}
