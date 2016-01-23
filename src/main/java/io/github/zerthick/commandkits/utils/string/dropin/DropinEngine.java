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

package io.github.zerthick.commandkits.utils.string.dropin;

import io.github.zerthick.commandkits.utils.economy.CommandKitsEconomyHandler;
import org.spongepowered.api.entity.living.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class DropinEngine {

    private static Map<String, DropinExecutor> dropinMap = new HashMap<String, DropinExecutor>(){
        {
            put("PLAYER_NAME", (player, args) -> {
                if(args.length > 0){
                    if(args[0].equals("useTypeFlag")){
                        return "'" + player.getName() + "'";
                    }
                }
                return player.getName();
            });
            put("PLAYER_UUID", (player, args) -> {
                if(args.length > 0){
                    if(args[0].equals("useTypeFlag")){
                        return "'" + player.getUniqueId().toString() + "'";
                    }
                }
                return player.getUniqueId().toString();
            });
            put("PLAYER_BALANCE", ((player, args1) -> String.valueOf(CommandKitsEconomyHandler.getInstance().getPlayerBalance(player))));
            put("WORLD_NAME", (player, args) -> {
                if(args.length > 0){
                    if(args[0].equals("useTypeFlag")){
                        return "'" + player.getWorld().getName()+ "'";
                    }
                }
                return player.getWorld().getName();
            });
            put("WORLD_UUID", (player, args) -> {
                if(args.length > 0){
                    if(args[0].equals("useTypeFlag")){
                        return "'" + player.getWorld().getUniqueId().toString() + "'";
                    }
                }
                return player.getWorld().getUniqueId().toString();
            });
            put("KEYS_", new DataKeyExecutor());
            put("ARGS_", new CommandArgExecutor());
            put("PERM_", (player, args) -> String.valueOf(player.hasPermission(args[0].replace("PERM_", ""))));
            put("INV_", new InventoryExecutor());
        }
    };

    public static String replaceDropins(String rawString, Player player, String[] args){

        String processedString = rawString;

        Pattern regex = Pattern.compile("\\{(.*?)\\}");
        Matcher matcher =  regex.matcher(rawString);
        while (matcher.find()) {
            String key = matcher.group(1);
            String result = key;
            if(key.startsWith("KEYS_")){ //Data key
                result = dropinMap.get("KEYS_").execute(player, Stream.of(new String[] {key}, args).flatMap(Stream::of)
                        .toArray(String[]::new));
            } else if(key.startsWith("ARGS_")) { //Command key
                result = dropinMap.get("ARGS_").execute(player, Stream.of(new String[] {key}, args).flatMap(Stream::of)
                        .toArray(String[]::new));
            } else if(key.startsWith("PERM_")) { //Permission key
                result = dropinMap.get("PERM_").execute(player, Stream.of(new String[]{key}, args).flatMap(Stream::of)
                        .toArray(String[]::new));
            } else if (key.startsWith("INV_")) { //Inventory key
                result = dropinMap.get("INV_").execute(player, Stream.of(new String[]{key}, args).flatMap(Stream::of)
                        .toArray(String[]::new));
            } else if(dropinMap.containsKey(key)){ //Vanilla Dropin
                result = dropinMap.get(key).execute(player, args);
            }
            processedString = processedString.replace('{' + key + '}', result);
        }
        return processedString;
    }
}
