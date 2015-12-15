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
