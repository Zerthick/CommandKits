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

package io.github.zerthick.commandkits.cmd.cmdexecutors;

import io.github.zerthick.commandkits.cmdkit.CommandKit;
import io.github.zerthick.commandkits.cmdkit.CommandKitManager;
import io.github.zerthick.commandkits.utils.string.Strings;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

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
            Optional<String> optionalArgs = args.getOne("args");
            if(optionalKitName.isPresent()){
                String kitName = optionalKitName.get().toUpperCase();
                CommandKitManager kitManager = plugin.getKitManager();
                if(kitManager.isKit(kitName)){
                    CommandKit kit = kitManager.getKit(kitName);
                    if(kit.hasPermission(player)){
                        if(kit.hasRequirements(player)) {
                            if (kitManager.checkInterval(player, kitName) == 0) {
                                String[] argArray = new String[0];
                                if (optionalArgs.isPresent()) {
                                    argArray = optionalArgs.get().split(" ");
                                }
                                kit.execute(player, argArray);
                                kitManager.markInterval(player, kitName);
                            } else {
                                src.sendMessage(Text.of(TextColors.RED,
                                        Strings.getInstance().getStrings().get("intervalDenial")
                                                + kitManager.checkInterval(player, kitName)));
                            }
                        } else {
                            src.sendMessage(Text.of(TextColors.RED, Strings.getInstance().getStrings().get("requirementDenial")));
                        }
                    } else {
                        src.sendMessage(Text.of(TextColors.RED, Strings.getInstance().getStrings().get("permissionDenial")));
                    }
                } else {
                    src.sendMessage(Text.of(TextColors.RED, Strings.getInstance().getStrings().get("unknownKit")));
                }
                return CommandResult.success();
            }
            src.sendMessage(Text.of(TextColors.RED, Strings.getInstance().getStrings().get("unknownKit")));
        }
        return CommandResult.empty();
    }
}
