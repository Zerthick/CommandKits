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

    public boolean hasRequirements(Player player, String kitName){
        if(isKit(kitName)){
            CommandKit kit = kits.get(kitName);
            try {
                return !kit.getRequirementsMap(player).containsValue(false);
            } catch (IllegalAccessException e) {
            }
        }
        return false;
    }

    public Map<String, Boolean> getRequirementsMap(Player player, String kitName){
        if(isKit(kitName)){
            try {
                return kits.get(kitName).getRequirementsMap(player);
            } catch (IllegalAccessException e) {
            }
        }
        return new HashMap<>();
    }
}
