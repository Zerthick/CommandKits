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

package io.github.zerthick.commandkitsTEMP.utils.string;

import java.util.Collections;
import java.util.Map;

public class Strings {

    private static Map<String, String> strings;

    private static Strings instance = null;

    protected Strings(){
        //Singleton Design Pattern
    }

    public static Strings getInstance(){
        if(instance == null){
            instance = new Strings();
        }
        return instance;
    }

    public void setUp(Map<String, String> strings){
        Strings.strings = strings;
    }

    public Map<String, String> getStrings(){
        return Collections.unmodifiableMap(strings);
    }
}
