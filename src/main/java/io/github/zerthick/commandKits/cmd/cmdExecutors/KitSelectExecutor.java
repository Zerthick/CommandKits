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

package io.github.zerthick.commandKits.cmd.cmdExecutors;

import io.github.zerthick.commandKits.cmdKit.CommandKit;
import io.github.zerthick.commandKits.cmdKit.CommandKitManager;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;

import java.util.Map;
import java.util.Optional;

public class KitSelectExecutor extends AbstractCmdExecutor implements CommandExecutor{

    public KitSelectExecutor(PluginContainer pluginContainer) {
        super(pluginContainer);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if(src instanceof Player){
            Player player = (Player)src;
            Optional<String> optionalKitName = args.getOne("kitName");
            if(optionalKitName.isPresent()){
                String kitName = optionalKitName.get().toUpperCase();
                CommandKitManager kitManager = plugin.getKitManager();
                if(kitManager.isKit(kitName)){
                    CommandKit kit = kitManager.getKit(kitName);
                    if(kit.hasPermission(player)){
                        Map<String, Boolean> requirementsMap = kit.getRequirementsMap(player);
                        if(!requirementsMap.containsValue(false)){
                            kit.executeCommands(player);
                        }
                    }
                }
            } else {
                src.sendMessage(Texts.of(TextColors.DARK_GREEN, container.getName(),
                        TextColors.GREEN, " version: ", TextColors.DARK_GREEN,
                        container.getVersion(), TextColors.GREEN, " by ",
                        TextColors.DARK_GREEN, "Zerthick"));
            }
        }
        return CommandResult.success();
    }
}
