package io.github.zerthick.commandKits.utils.string;

import io.github.zerthick.commandKits.utils.string.data.DataConverter;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringParser {

    public static String parseCommand(Player player, String command){
        String parsedCommand = command;
        Pattern regex = Pattern.compile("\\{(.*?)\\}");
        Matcher matcher =  regex.matcher(command);
        while (matcher.find()){
            String key = matcher.group(1);
            if (key.equalsIgnoreCase("PLAYER_NAME")){
                parsedCommand = parsedCommand.replace('{' + key + '}', player.getName());
            } else if (key.equalsIgnoreCase("PLAYER_UUID")) {
                parsedCommand = parsedCommand.replace('{' + key + '}', player.getUniqueId().toString());
            } else if (key.equalsIgnoreCase("WORLD_NAME")) {
                parsedCommand = parsedCommand.replace('{' + key  + '}', player.getWorld().getName());
            } else if (key.equalsIgnoreCase("WORLD_UUID")) {
                parsedCommand = parsedCommand.replace('{' + key + '}', player.getWorld().getUniqueId().toString());
            } else if (key.startsWith("KEYS_")){
                String cleanedKey = key.replace("KEYS_", "");
                Optional<?> optionalValue = DataConverter.convertStringToDataValue(player, cleanedKey);
                if(optionalValue.isPresent()){
                    parsedCommand = parsedCommand.replace('{' + key + '}', optionalValue.get().toString());
                }
            }
            parsedCommand += " Key: " + key;
        }
        return parsedCommand;
    }

    public static Boolean parseRequirement(Player player, String key, String comparison){
        if (key.startsWith("KEYS_")){
            String cleanedKey = key.replace("KEYS_", "");
            return parseDataRequirement(player, cleanedKey, comparison.replaceAll("\\s",""));
        } else if (key.equals("permission")){
            return player.hasPermission(comparison);
        } else if (key.equals("world")){
            return player.getWorld().getName().equalsIgnoreCase(comparison);
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
