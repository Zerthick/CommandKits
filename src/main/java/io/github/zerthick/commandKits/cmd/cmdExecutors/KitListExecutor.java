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
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class KitListExecutor extends AbstractCmdExecutor implements CommandExecutor {

    public KitListExecutor(PluginContainer pluginContainer) {
        super(pluginContainer);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Map<String, CommandKit> kits = plugin.getKitManager().getKits();
        List<Text> outputList = new LinkedList<>();

        if(src instanceof Player) {
            Player player = (Player) src;

            kits.values().stream().filter(kit -> kit.hasPermission(player)).forEach(kit -> {
                if (kit.hasRequirements(player)) {
                    outputList.add(Texts.of(TextColors.GREEN, kit.getName()));
                } else {
                    outputList.add(Texts.of(TextColors.RED, kit.getName()));
                }
            });

            if (outputList.isEmpty()){
                outputList.add(Texts.of(Strings.getInstance().getStrings().get("emptyList")));
            }

            listBuilder(outputList).sendTo(src);
        }
        return CommandResult.success();
    }

    private PaginationBuilder listBuilder(List<Text> list){
        PaginationService pagServ = plugin.getGame().getServiceManager().provide(PaginationService.class).get();
        PaginationBuilder builder = pagServ.builder();
        builder.contents(list).title(Texts.of(Strings.getInstance().getStrings().get("listHeader")))
                .paddingString(Strings.getInstance().getStrings().get("listPadding"));
        return builder;
    }
}
