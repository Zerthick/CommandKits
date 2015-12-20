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

package io.github.zerthick.commandKits.utils.string.expression;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ShuntingYard {

    private enum Operator {
        NOT(7), MULTIPLY(6), DIVIDE(6), MOD(6), ADD(5), SUBTRACT(5),
        LESS(4), LESS_EQUAL(4), GREATER(4), GREATER_EQUAL(4),
        EQUAL(3), NOT_EQUAL(3), AND(2), OR(1);

        final int precedence;
        Operator(int p) { precedence = p; }
    }

    private static Map<String, Operator> ops = new HashMap<String, Operator>() {
        {
            put("!", Operator.NOT);
            put("+", Operator.ADD);
            put("-", Operator.SUBTRACT);
            put("*", Operator.MULTIPLY);
            put("/", Operator.DIVIDE);
            put("%", Operator.MOD);
            put("<", Operator.LESS);
            put("<=", Operator.LESS_EQUAL);
            put(">", Operator.GREATER);
            put("<=", Operator.GREATER_EQUAL);
            put("==", Operator.EQUAL);
            put("!=", Operator.NOT_EQUAL);
            put("&&", Operator.AND);
            put("||", Operator.OR);
        }
    };

    private static boolean isLowerPrec(String op, String sub) {
        if(op.equals(Operator.NOT)){
            return (ops.containsKey(sub) && (ops.get(op).precedence < ops.get(sub).precedence));
        }
        return (ops.containsKey(sub) && (ops.get(op).precedence <= ops.get(sub).precedence));
    }

    public static String postfix(String infix){

        StringBuilder output = new StringBuilder();
        Deque<String> stack  = new LinkedList<>();

        for (String token : infix.split("\\s")) {

            // operator
            if (ops.containsKey(token)) {
                while ( ! stack.isEmpty() && isLowerPrec(token, stack.peek()))
                    output.append(stack.pop()).append(' ');
                stack.push(token);

            // left parenthesis
            } else if (token.equals("(")) {
                stack.push(token);

            // right parenthesis
            } else if (token.equals(")")) {
                while ( ! stack.peek().equals("("))
                    output.append(stack.pop()).append(' ');
                stack.pop();

            // value
            } else {
                output.append(token).append(' ');
            }
        }

        while ( ! stack.isEmpty())
            output.append(stack.pop()).append(' ');

        return output.toString();
    }
}
