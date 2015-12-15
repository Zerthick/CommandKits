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

package io.github.zerthick.commandKits.utils.config;

import com.google.common.reflect.TypeToken;
import io.github.zerthick.commandKits.cmdKit.CommandKit;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.*;

public class CommandKitsConfigManager {

    private static ZConfigManager configManager;
    private static CommentedConfigurationNode config;
    private  Logger logger;
    private static CommandKitsConfigManager instance = null;
    protected CommandKitsConfigManager(){
        //Singleton Design Pattern
    }

    public static CommandKitsConfigManager getInstance(){
        if(instance == null){
            instance = new CommandKitsConfigManager();
        }
        return instance;
    }

    public void setUp(Path configFilePath, ConfigurationLoader<CommentedConfigurationNode> configLoader, Logger logger){
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(CommandKit.class), new CommandKitSerializer());

        configManager = ZConfigManager.getInstance();
        configManager.setup(configFilePath, configLoader, logger, config -> {
            CommentedConfigurationNode exampleKitNode = config.getNode("kits", "Example");
            exampleKitNode.getNode("name").setComment("This is the name of the kit that will be used in the /kc command")
                    .setValue("Example");
            exampleKitNode.getNode("description").setComment("This is the description that will be returned" +
                    " by the /kc info command").setValue("This is an example kit");
            exampleKitNode.getNode("requirements", "permission").setComment("This is the permission required to run" +
                    " /kc on this kit").setValue("commandKits.example");
            exampleKitNode.getNode("requirements", "KEYS_EXPERIENCE_LEVEL").setComment("This requirement specifies that the player" +
                    " requires 5 enchanting levels to get this kit").setValue(">=5");
            List<String> commandList = new LinkedList<>();
            commandList.add("minecraft:me I used the example kit!");
            commandList.add("$minecraft:tell {PLAYER_NAME} You used the example kit!");
            exampleKitNode.getNode("commands").setComment("List containing commands that will be executed by this kit" +
                    " commands starting with \'$\' will be executed by the console, otherwise they will be executed by" +
                    " the player, \'%p\' will be replaced a runtime with the executing player's name")
                    .setValue(commandList);
        });
        config = configManager.getConfig();
        this.logger = logger;
    }

    public Map<String, CommandKit> loadKits() {
        Set<Object> keySet = config.getNode("kits").getChildrenMap().keySet();
        Map<String, CommandKit> kits = new HashMap<>();
        try {
            for (Object key : keySet) {
                CommandKit kit = config.getNode("kits", key).getValue(TypeToken.of(CommandKit.class));
                kits.put(kit.getName().toUpperCase(), kit);
            }
        } catch (ObjectMappingException e) {
            logger.error("Error loading kits from config! Error: " + e);
        }
        return kits;
    }
}
