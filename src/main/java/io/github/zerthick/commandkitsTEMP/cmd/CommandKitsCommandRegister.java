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

package io.github.zerthick.commandkitsTEMP.cmd;

import io.github.zerthick.commandkitsTEMP.cmd.cmdexecutors.*;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

public class CommandKitsCommandRegister {


    public static void registerCmds(PluginContainer container) {
        // ck info <KitName>
        CommandSpec kitInfoCommand = CommandSpec.builder()
                .description(Text.of("/ck kitInfo <kitName>"))
                .permission("commandkitsTEMP.command.kitInfo")
                .arguments(GenericArguments.optional(GenericArguments.string(Text.of("kitName"))))
                .executor(new KitInfoExecutor(container))
                .build();

        //ck configReload
        CommandSpec configReloadCommand = CommandSpec.builder()
                .description(Text.of("/ck reload"))
                .permission("commandkitsTEMP.command.reloadConfig")
                .executor(new ConfigReloadExecutor(container))
                .build();

        //ck pluginInfo
        CommandSpec pluginInfoCommand = CommandSpec.builder()
                .description(Text.of("/ck pluginInfo"))
                .permission("commandkitsTEMP.command.pluginInfo")
                .executor(new PluginInfoExecutor(container))
                .build();

        //ck list
        CommandSpec kitListCommand = CommandSpec.builder()
                .description(Text.of("/ck list"))
                .permission("commandkitsTEMP.command.list")
                .executor(new KitListExecutor(container))
                .build();

        // ck <KitName> [args]
        CommandSpec kitSelectCommand = CommandSpec.builder()
                .description(Text.of("/ck <kitName>"))
                .permission("commandkitsTEMP.command.select")
                .arguments(GenericArguments.string(Text.of("kitName")),
                        GenericArguments.optional(GenericArguments.remainingJoinedStrings(Text.of("args"))))
                .executor(new KitSelectExecutor(container))
                .child(kitInfoCommand, "info")
                .child(kitListCommand,"list", "ls" )
                .child(pluginInfoCommand, "pluginInfo")
                .child(configReloadCommand, "reload", "reloadConfig")
                .build();

        Sponge.getGame().getCommandManager().register(container.getInstance().get(),
                kitSelectCommand, "commandkitsTEMP", "ck");
    }
}
