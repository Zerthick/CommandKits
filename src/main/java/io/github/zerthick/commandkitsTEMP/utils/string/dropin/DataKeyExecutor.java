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

import io.github.zerthick.commandkitsTEMP.utils.string.data.DataConverter;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Optional;

public class DataKeyExecutor implements DropinExecutor{
    @Override
    public String execute(Player player, String[] args) {
        String key = args[0];
        String cleanedKey = key.replace("KEYS_", "");
        Optional<?> optionalValue = DataConverter.convertStringToDataValue(player, cleanedKey);
        if(optionalValue.isPresent()) {
            if (args.length > 1) {
                if (args[1].equals("useTypeFlag")) {
                    if (optionalValue.get() instanceof String){
                        return "'" + optionalValue.get().toString() + "'";
                    }
                }
            }
            return optionalValue.get().toString();
        }
        return key;
    }
}
