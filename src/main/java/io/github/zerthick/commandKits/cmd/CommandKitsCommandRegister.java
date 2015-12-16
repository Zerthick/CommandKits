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

package io.github.zerthick.commandKits.cmd;

import io.github.zerthick.commandKits.CommandKitsMain;
import io.github.zerthick.commandKits.cmd.cmdExecutors.KitSelectExecutor;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Texts;

public class CommandKitsCommandRegister {

    private PluginContainer container;
    private Game game;

    public CommandKitsCommandRegister(PluginContainer pluginContainer) {

        //Unpack Container
        container = pluginContainer;
        CommandKitsMain plugin = (CommandKitsMain) (container.getInstance().get() instanceof CommandKitsMain ? container
                .getInstance().get() : null);
        game = plugin.getGame();
    }

    public void registerCmds() {
        // ck <KitName>
        CommandSpec kitSelectCommand = CommandSpec.builder()
                .description(Texts.of("/ck <kitName>"))
                .permission("commandKits.command")
                .arguments(GenericArguments.optional(GenericArguments.string(Texts.of("kitName"))))
                .executor(new KitSelectExecutor(container))
                .build();

        game.getCommandManager().register(container.getInstance().get(),
                kitSelectCommand, "commandKits", "ck");
    }
}
