package io.github.zerthick.commandKits.cmdKit;

import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.entity.living.player.Player;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        return player.hasPermission(requirements.get("permission"));
    }

    public Map<String,Boolean> getRequirementsMap(Player player) throws IllegalAccessException {
        Map<String, Boolean> requirementsMap = new HashMap<>();

        if(requirements.containsKey("permission")){
            requirementsMap.put("permission", player.hasPermission(requirements.get("permission")));
        }

        Field[] dataKeys = Keys.class.getDeclaredFields();
        for(String keyName : requirements.keySet()) {
            if(keyName.startsWith("KEYS.")) {
                String keyNameCleaned = keyName.replaceFirst("KEYS.", "").toUpperCase();
                for (Field field : dataKeys) {
                    if (field.getName().equals(keyNameCleaned)) {
                        Key<?> key = (Key<?>) field.get(null);
                        if (player.supports(key)) {
                            Object keyValue = player.get((Key<? extends BaseValue<Object>>) key).get();
                            String comparison = requirements.get(keyName);
                            switch (comparison){
                                case "true":
                                    requirementsMap.put(keyNameCleaned, ((Boolean) keyValue));
                                    break;
                                case "false":
                                    requirementsMap.put(keyNameCleaned, (!(Boolean) keyValue));
                                    break;
                                default:
                                    if(comparison.startsWith("=")){
                                        requirementsMap.put(keyNameCleaned, keyValue.toString()
                                                .equalsIgnoreCase(comparison.substring(1)));
                                    } else if (comparison.startsWith("<=")){
                                        requirementsMap.put(keyNameCleaned, keyValue.toString()
                                                .compareToIgnoreCase(comparison.substring(1)) <= 0);
                                    } else if (comparison.startsWith("<")) {
                                        requirementsMap.put(keyNameCleaned, keyValue.toString()
                                                .compareToIgnoreCase(comparison.substring(1)) < 0);
                                    } else if (comparison.startsWith(">=")) {
                                        requirementsMap.put(keyNameCleaned, keyValue.toString()
                                                .compareToIgnoreCase(comparison.substring(1)) >= 0);
                                    } else if (comparison.startsWith(">")) {
                                        requirementsMap.put(keyNameCleaned, keyValue.toString()
                                                .compareToIgnoreCase(comparison.substring(1)) > 0);
                                    }
                            }
                        }
                        break;
                    }
                }
            }
        }

        return requirementsMap;
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
