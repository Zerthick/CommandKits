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
import io.github.zerthick.commandKits.cmdKit.CommandKitRequirement;
import io.github.zerthick.commandKits.utils.string.Strings;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.pagination.PaginationBuilder;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.*;
import java.util.stream.Collectors;

public class KitInfoExecutor extends AbstractCmdExecutor implements CommandExecutor{

    public KitInfoExecutor(PluginContainer pluginContainer) {
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
                        List<Text> outputList = new LinkedList<>();
                        outputList.add(Text.of(kit.getDescription(), "\n"));
                        Set<CommandKitRequirement> requirements = kit.getRequirements();
                        if(!requirements.isEmpty()){
                            outputList.add(Text.of(Strings.getInstance().getStrings().get("requirementHeader")));
                            outputList.addAll(requirements.stream().filter(requirement ->
                                    requirement.hasRequirement(player)).map(requirement ->
                                        Text.of(TextColors.GREEN, requirement.getName(), ": ",
                                        TextColors.WHITE, requirement.getDescription())).collect(Collectors.toList()));
                        }
                        Text header;
                        if(kit.hasRequirements(player)){
                            header = Text.of(TextColors.GREEN, kit.getName());
                        } else {
                            header = Text.of(TextColors.RED, kit.getName());
                        }
                        listBuilder(outputList, header).sendTo(src);
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

    private PaginationBuilder listBuilder(List<Text> list, Text header){
        PaginationService pagServ = plugin.getGame().getServiceManager().provide(PaginationService.class).get();
        PaginationBuilder builder = pagServ.builder();
        builder.contents(list).title(header)
                .paddingString(Strings.getInstance().getStrings().get("listPadding"));
        return builder;
    }
}
