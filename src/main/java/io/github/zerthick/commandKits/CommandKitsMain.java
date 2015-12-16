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

package io.github.zerthick.commandKits;

import com.google.inject.Inject;
import io.github.zerthick.commandKits.cmd.CommandKitsCommandRegister;
import io.github.zerthick.commandKits.cmdKit.CommandKitManager;
import io.github.zerthick.commandKits.utils.config.CommandKitsConfigManager;
import io.github.zerthick.commandKits.utils.string.Strings;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import java.nio.file.Path;

@Plugin(id = "CmdKits", name = "Command Kits", version = "0.1.1")
public class CommandKitsMain {

    @Inject
    private Game game;

    public Game getGame() {
        return game;
    }

    @Inject
    private Logger logger;

    public Logger getLogger() {
        return logger;
    }

    @Inject
    private PluginContainer instance;

    @Inject
    @DefaultConfig(sharedRoot = true)
    private Path defaultConfigPath;

    @Inject
    @DefaultConfig(sharedRoot = true)
    private ConfigurationLoader<CommentedConfigurationNode> configLoader;

    private CommandKitsConfigManager configManager;

    private CommandKitManager kitManager;

    public CommandKitManager getKitManager() {
        return kitManager;
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {

        //Setup ConfigManager
        configManager = CommandKitsConfigManager.getInstance();
        configManager.setUp(defaultConfigPath, configLoader, getLogger());

        //Load kits from config
        kitManager = new CommandKitManager(configManager.loadKits());

        //Load strings from config
        Strings.getInstance().setUp(configManager.loadStrings());

        // Register Commands
        CommandKitsCommandRegister commandRegister = new CommandKitsCommandRegister(
                instance);
        commandRegister.registerCmds();

        // Log Start Up to Console
        getLogger().info(
                instance.getName() + " version " + instance.getVersion()
                        + " enabled!");
    }
}
