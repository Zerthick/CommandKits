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

package io.github.zerthick.commandkitsTEMP.utils.string.dropin;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;

import java.util.Optional;

public class InventoryExecutor implements DropinExecutor {

    @Override
    public String execute(Player player, String[] args) {
        String key = args[0];
        String cleanedKey = key.replace("INV_", "");
        if (cleanedKey.startsWith("CONTAINS_")) {

            String itemName = cleanedKey.replace("CONTAINS_", "").trim();
            Optional<ItemType> optionalItemType = Sponge.getRegistry().getType(ItemType.class, itemName);
            if (optionalItemType.isPresent()) {
                return String.valueOf(player.getInventory().contains(optionalItemType.get()));
            }

        } else if (cleanedKey.startsWith("AMOUNT_")) {

            String itemName = cleanedKey.replace("AMOUNT_", "").trim();
            Optional<ItemType> optionalItemType = Sponge.getRegistry().getType(ItemType.class, itemName);
            if (optionalItemType.isPresent()) {
                return String.valueOf(player.getInventory().query(optionalItemType.get()).size());
            }
        }
        return key;
    }
}
