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

package io.github.zerthick.commandKits.utils.string;

import io.github.zerthick.commandKits.utils.string.dropin.DropinEngine;
import io.github.zerthick.commandKits.utils.string.expression.ExpressionParser;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

public class StringParser {

    public static String parseCommand(Player player, String command, String[] args){
        return DropinEngine.replaceDropins(command, player, args);
    }

    public static Boolean parseRequirement(Player player, String requirementRule){
        ExpressionParser.parseExpression(DropinEngine.replaceDropins(requirementRule, player, new String[] {"useTypeFlag"}));
        return true;
        //return ExpressionParser.parseExpression(DropinEngine.replaceDropins(requirementRule, player, new String[0]));
    }

}
