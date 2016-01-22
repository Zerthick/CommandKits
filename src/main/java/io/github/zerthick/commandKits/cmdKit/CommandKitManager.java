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

import io.github.zerthick.commandkits.CommandKitsMain;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Task;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CommandKitManager {

    private final Map<String, CommandKit> kits;
    private final Map<String, Map<UUID, Long>> kitIntervalMap;
    protected PluginContainer container;
    protected CommandKitsMain plugin;
    private Task.Builder taskBuilder;

    public CommandKitManager(Map<String, CommandKit> kits, PluginContainer pluginContainer) {
        this.kits = kits;
        kitIntervalMap = new HashMap<>();
        taskBuilder = Sponge.getScheduler().createTaskBuilder();
        container = pluginContainer;
        plugin = (container.getInstance().get() instanceof CommandKitsMain ? (CommandKitsMain) container.getInstance().get() : null);
    }

    public Map<String, CommandKit> getKits() {
        return kits;
    }

    public boolean isKit(String kitName){
        return kits.containsKey(kitName);
    }

    public CommandKit getKit(String kitName){
        if(isKit(kitName)){
            return kits.get(kitName);
        }
        return null;
    }

    public void markInterval(Player player, String kitName) {
        if (getKit(kitName).getInterval() > 0) {
            Map<UUID, Long> intervalMap = kitIntervalMap.getOrDefault(kitName, new HashMap<>());
            intervalMap.put(player.getUniqueId(), getKit(kitName).getInterval());
            kitIntervalMap.put(kitName, intervalMap);

            //Updater Task
            taskBuilder.execute(task -> {
                Map<UUID, Long> playerIntervalMap = kitIntervalMap.get(kitName);
                Long oldInterval = playerIntervalMap.get(player.getUniqueId());
                if (oldInterval <= 1) {
                    playerIntervalMap.remove(player.getUniqueId());
                    kitIntervalMap.put(kitName, playerIntervalMap);
                    task.cancel();
                    return;
                }
                playerIntervalMap.put(player.getUniqueId(), oldInterval - 1);
                kitIntervalMap.put(kitName, playerIntervalMap);
            }).interval(1, TimeUnit.SECONDS).name("Command Kits " + kitName + " timer for " + player.getName()).submit(plugin);
        }
    }

    public long checkInterval(Player player, String kitName) {
        if (kitIntervalMap.containsKey(kitName) && kitIntervalMap.get(kitName).containsKey(player.getUniqueId())) {
            return kitIntervalMap.get(kitName).get(player.getUniqueId());
        }
        return 0;
    }
}
