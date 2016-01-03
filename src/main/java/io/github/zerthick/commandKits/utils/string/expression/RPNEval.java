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

import io.github.zerthick.commandKits.utils.Debug;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class RPNEval {

    private enum Operator {

        NOT(1, args -> {
            if(args[0].equals("true")){
                return "false";
            }
            return "true";
        }),
        MULTIPLY(2, args -> String.valueOf(Double.parseDouble(args[1]) * Double.parseDouble(args[0]))),
        DIVIDE(2, args -> String.valueOf(Double.parseDouble(args[1]) / Double.parseDouble(args[0]))),
        MOD(2, args -> String.valueOf(Double.parseDouble(args[1]) % Double.parseDouble(args[0]))),
        ADD(2, args -> {
            //if numeric
            if(args[0].matches("-?\\d+(\\.\\d+)?") && args[1].matches("-?\\d+(\\.\\d+)?")){
                return String.valueOf(Double.parseDouble(args[1]) + Double.parseDouble(args[0]));
            }
            // String concat
            return args[1].substring(0, args[1].length()-1) +  args[0].substring(1);
        }),
        SUBTRACT(2, args -> String.valueOf(Double.parseDouble(args[1]) - Double.parseDouble(args[0]))),
        LESS(2, args -> String.valueOf(Double.parseDouble(args[1]) < Double.parseDouble(args[0]))),
        LESS_EQUAL(2, args -> String.valueOf(Double.parseDouble(args[1]) <= Double.parseDouble(args[0]))),
        GREATER(2, args -> String.valueOf(Double.parseDouble(args[1]) > Double.parseDouble(args[0]))),
        GREATER_EQUAL(2, args -> String.valueOf(Double.parseDouble(args[1]) >= Double.parseDouble(args[0]))),
        EQUAL(2, args -> String.valueOf(args[1].equals(args[0]))),
        NOT_EQUAL(2, args -> String.valueOf(!args[1].equals(args[0]))),
        AND(2, args -> String.valueOf(Boolean.parseBoolean(args[1]) && Boolean.parseBoolean(args[0]))),
        OR(2, args -> String.valueOf(Boolean.parseBoolean(args[1]) || Boolean.parseBoolean(args[0])));

        final int numArgs;
        final Operation operation;
        Operator(int n, Operation o) { numArgs = n; operation = o;}
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
            put(">=", Operator.GREATER_EQUAL);
            put("==", Operator.EQUAL);
            put("!=", Operator.NOT_EQUAL);
            put("&&", Operator.AND);
            put("||", Operator.OR);
        }
    };

    public static String eval(String rpn){
        Deque<String> stack  = new LinkedList<>();

        for (String token : rpn.split("\\s")) {

            // operator
            if (ops.containsKey(token)) {
                Operator op = ops.get(token);
                if(stack.size() < op.numArgs){
                    return "Parsing_Error";
                } else {
                    String[] args = new String[op.numArgs];
                    for(int i = 0; i < op.numArgs; i++){
                        args[i] = stack.pop();
                    }
                    stack.push(op.operation.execute(args));
                }
            }
            // value
            else {
                stack.push(token);
            }
        }

        if(stack.size() != 1){
            return "Parsing_Error";
        }
        Debug.out(stack.peek());
        return stack.pop();
    }

    private interface Operation {
        String execute(String[] args);
    }
}
