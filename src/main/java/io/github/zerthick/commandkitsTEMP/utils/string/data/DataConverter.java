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

package io.github.zerthick.commandkitsTEMP.utils.string.data;

import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DataConverter {

    private static final Map<String, Key> dataKeyMap = new HashMap<String, Key>(){
        {
            put("CAN_FLY", Keys.CAN_FLY);
            put("DISARMED", Keys.DISARMED);
            put("DISPLAY_NAME", Keys.DISPLAY_NAME);
            put("EXPERIENCE_FROM_START_OF_LEVEL", Keys.EXPERIENCE_FROM_START_OF_LEVEL);
            put("EXPERIENCE_LEVEL", Keys.EXPERIENCE_LEVEL);
            put("EXPERIENCE_SINCE_LEVEL", Keys.EXPERIENCE_SINCE_LEVEL);
            put("FIRST_DATE_PLAYED", Keys.FIRST_DATE_PLAYED);
            put("FLYING_SPEED", Keys.FLYING_SPEED);
            put("FOOD_LEVEL", Keys.FOOD_LEVEL);
            put("GAME_MODE", Keys.GAME_MODE);
            put("HEALTH", Keys.HEALTH);
            put("HEALTH_SCALE", Keys.HEALTH_SCALE);
            put("HELD_EXPERIENCE", Keys.HELD_EXPERIENCE);
            put("INVISIBLE", Keys.INVISIBLE);
            put("IS_AFLAME", Keys.IS_AFLAME);
            put("IS_FLYING", Keys.IS_FLYING);
            put("IS_SLEEPING", Keys.IS_SLEEPING);
            put("IS_SNEAKING", Keys.IS_SNEAKING);
            put("IS_SPRINTING", Keys.IS_SPRINTING);
            put("IS_WET", Keys.IS_WET);
            put("LAST_DAMAGE", Keys.LAST_DAMAGE);
            put("MAX_HEALTH", Keys.MAX_HEALTH);
            put("REMAINING_AIR", Keys.REMAINING_AIR);
            put("SHOWS_DISPLAY_NAME", Keys.SHOWS_DISPLAY_NAME);
            put("WALKING_SPEED", Keys.WALKING_SPEED);
        }
    };

    public static Optional<?> convertStringToDataValue(Player player, String key){
        if(dataKeyMap.containsKey(key)){
            Key dataKey =  dataKeyMap.get(key);
            if(player.supports(dataKey)){
                return player.get(dataKey);
            }
        }
        return Optional.empty();
    }
}
