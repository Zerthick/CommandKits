package io.github.zerthick.commandKits.utils.string.expression;

import org.spongepowered.api.Sponge;

public class ExpressionParser {

    public static boolean parseExpression(String expression){
        Sponge.getGame().getCommandManager().process(Sponge.getGame().getServer().getConsole(), "minecraft:say " + expression);
        return true;
    }
}
