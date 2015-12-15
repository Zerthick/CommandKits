package io.github.zerthick.commandKits.cmdKit;

import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.Property;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.lang.reflect.Field;
import java.util.*;

public class CommandKit {

    private final String name;
    private final String description;
    private final Map<String, String> requirements;
    private final List<String> commands;

    private static final Map<String, Key> dataKeyMap = new HashMap<String, Key>(){
        {
            put("CAN_FLY", Keys.CAN_FLY);
            put("DISARMED", Keys.DISARMED);
            put("DISPLAY_NAME", Keys.DISPLAY_NAME);
            put("EXPERIENCE_FROM_START_OF_LEVEL", Keys.EXPERIENCE_FROM_START_OF_LEVEL);
            put("EXPERIENCE_LEVEL", Keys.EXPERIENCE_LEVEL);
            put("EXPERIENCE_SINCE_LEVEL", Keys.EXPERIENCE_SINCE_LEVEL);
            put("FIRST_DATE_PLAYED", Keys.FIRST_DATE_PLAYED);
            put("FLYING_SPEED", Keys.FLYING_SPEED);
            put("FOOD_LEVEL", Keys.FOOD_LEVEL);
            put("GAME_MODE", Keys.GAME_MODE);
            put("HEALTH", Keys.HEALTH);
            put("HEALTH_SCALE", Keys.HEALTH_SCALE);
            put("HELD_EXPERIENCE", Keys.HELD_EXPERIENCE);
            put("INVISIBLE", Keys.INVISIBLE);
            put("IS_AFLAME", Keys.IS_AFLAME);
            put("IS_FLYING", Keys.IS_FLYING);
            put("IS_SLEEPING", Keys.IS_SLEEPING);
            put("IS_SNEAKING", Keys.IS_SNEAKING);
            put("IS_SPRINTING", Keys.IS_SPRINTING);
            put("IS_WET", Keys.IS_WET);
            put("IS_WHITELISTED", Keys.IS_WHITELISTED);
            put("LAST_DAMAGE", Keys.LAST_DAMAGE);
            put("MAX_HEALTH", Keys.MAX_HEALTH);
            put("REMAINING_AIR", Keys.REMAINING_AIR);
            put("SHOWS_DISPLAY_NAME", Keys.SHOWS_DISPLAY_NAME);
            put("WALKING_SPEED", Keys.WALKING_SPEED);
        }
    };

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
        return requirements.containsKey("permission") ? player.hasPermission(requirements.get("permission")) : true;
    }

    public Map<String,Boolean> getRequirementsMap(Player player) {
        Map<String, Boolean> requirementsMap = new HashMap<>();

        if(requirements.containsKey("permission")){
            requirementsMap.put("permission", player.hasPermission(requirements.get("permission")));
        }

        requirements.keySet().stream().filter(keyName -> keyName.startsWith("KEYS_")).forEach(keyName -> {
            String cleanedKeyName = keyName.replace("KEYS_", "");
            requirementsMap.put(cleanedKeyName, true);
            if (dataKeyMap.containsKey(cleanedKeyName)) {
                Optional<?> optionalValue = player.get(dataKeyMap.get(cleanedKeyName));
                if (optionalValue.isPresent()) {
                    Object value = optionalValue.get();
                    requirementsMap.put(cleanedKeyName + " : " + value, true);
                    if (value instanceof Boolean) {

                    } else if (value instanceof Text) {

                    }
                }
            }
        });
        requirementsMap.put("Map: " +  requirements.keySet().toString(), true);
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
