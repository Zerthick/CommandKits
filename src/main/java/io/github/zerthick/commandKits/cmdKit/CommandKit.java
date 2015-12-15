package io.github.zerthick.commandKits.cmdKit;

import io.github.zerthick.commandKits.utils.string.StringParser;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Texts;

import java.util.*;

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
        return !requirements.containsKey("permission") || player.hasPermission(requirements.get("permission"));
    }

    public Map<String,Boolean> getRequirementsMap(Player player) {
        Map<String, Boolean> requirementsMap = new HashMap<>();

        for(String key : requirements.keySet()){
            requirementsMap.put(key, StringParser.parseRequirement(player, key, requirements.get(key)));
        }
        return requirementsMap;
    }

    public void executeCommands(Player player){
        for(String command : commands){
            String parsedCommand = StringParser.parseCommand(player, command);
            if(parsedCommand.startsWith("$")){
                Sponge.getGame().getCommandManager().process(Sponge.getGame().getServer().getConsole(), parsedCommand.substring(1));
            } else {
                Sponge.getGame().getCommandManager().process(player, parsedCommand);
            }
        }
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
