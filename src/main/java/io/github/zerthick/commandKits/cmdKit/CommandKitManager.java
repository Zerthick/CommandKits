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

    public boolean isCommand(String cmdName){
        return kits.containsKey(cmdName);
    }

    public boolean hasRequirements(Player player, String cmdName){
        if(isCommand(cmdName)){
            CommandKit kit = kits.get(cmdName);
            try {
                return !kit.getRequirementsMap(player).containsValue(false);
            } catch (IllegalAccessException e) {
            }
        }
        return false;
    }

    public Map<String, Boolean> getRequirementsMap(Player player, String cmdName){
        if(isCommand(cmdName)){
            try {
                return kits.get(cmdName).getRequirementsMap(player);
            } catch (IllegalAccessException e) {
            }
        }
        return new HashMap<>();
    }
}
