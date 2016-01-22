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

package io.github.zerthick.commandkits.cmdkit;

import io.github.zerthick.commandkits.utils.string.StringParser;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class CommandKit {

    private final String name;
    private final String description;
    private final String permission;
    private final String message;
    private final Set<CommandKitRequirement> requirements;
    private final List<String> commands;
    private final List<String> items;
    private final Long interval;

    public CommandKit(String name, String description, String permission, String message, Set<CommandKitRequirement> requirements, List<String> commands, List<String> items, Long interval) {
        this.name = name;
        this.description = description;
        this.permission = permission;
        this.message = message;
        this.requirements = requirements;
        this.commands = commands;
        this.items = items;
        this.interval = interval;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPermission() {
        return permission;
    }

    public String getMessage() {
        return message;
    }

    public Set<CommandKitRequirement> getRequirements() {
        return requirements;
    }

    public List<String> getCommands() {
        return commands;
    }

    public List<String> getItems() {
        return items;
    }

    public Long getInterval() {
        return interval;
    }

    public boolean hasPermission(Player player){
        return permission.isEmpty() || player.hasPermission(permission);
    }

    public boolean hasRequirements(Player player) {
        for(CommandKitRequirement requirement : requirements){
            if(!requirement.hasRequirement(player)){
                return false;
            }
        }
        return true;
    }

    public void execute(Player player, String[] args) {
        executeCommands(player, args);
        executeItems(player, args);
        executeMessage(player, args);
    }

    private void executeCommands(Player player, String[] args) {
        for(String command : commands){
            String parsedCommand = StringParser.parseDropins(player, command, args);
            if(parsedCommand.startsWith("$")){
                Sponge.getGame().getCommandManager().process(Sponge.getGame().getServer().getConsole(), parsedCommand.substring(1));
            } else {
                Sponge.getGame().getCommandManager().process(player, parsedCommand);
            }
        }
    }

    private void executeMessage(Player player, String[] args) {
        String parsedMessage = StringParser.parseDropins(player, message, args);
        player.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(parsedMessage));
    }

    private void executeItems(Player player, String[] args) {
        for (String item : items) {

            String parsedItem = StringParser.parseDropins(player, item, args);

            String itemName = parsedItem;
            int amount = 1;

            if(itemName.contains(" ")){
                int index = parsedItem.indexOf(" ");
                itemName = parsedItem.substring(0, index);
                amount = Integer.parseInt(parsedItem.substring(index + 1));
            }

            Optional<ItemType> optionalItemType = Sponge.getRegistry().getType(ItemType.class, itemName);
            if(optionalItemType.isPresent())
            {
                player.getInventory().offer(ItemStack.of(optionalItemType.get(), amount));
            }
        }
    }

    @Override
    public String toString() {
        return "CommandKit{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", permission='" + permission + '\'' +
                ", message='" + message + '\'' +
                ", requirements=" + requirements +
                ", commands=" + commands +
                ", items=" + items +
                '}';
    }
}
