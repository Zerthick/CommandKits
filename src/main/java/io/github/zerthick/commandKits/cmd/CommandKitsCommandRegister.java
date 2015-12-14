package io.github.zerthick.commandKits.cmd;

import io.github.zerthick.commandKits.CommandKitsMain;
import io.github.zerthick.commandKits.cmd.cmdExecutors.KitSelectExecutor;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Texts;

public class CommandKitsCommandRegister {

    private PluginContainer container;
    private Game game;

    public CommandKitsCommandRegister(PluginContainer pluginContainer) {

        //Unpack Container
        container = pluginContainer;
        CommandKitsMain plugin = (CommandKitsMain) (container.getInstance().get() instanceof CommandKitsMain ? container
                .getInstance().get() : null);
        game = plugin.getGame();
    }

    public void registerCmds() {
        // ck
        CommandSpec graveyardCommand = CommandSpec.builder()
                .description(Texts.of("/ck [kitName]"))
                .permission("commandKits.command")
                .arguments(GenericArguments.optional(GenericArguments.string(Texts.of("kitName"))))
                .executor(new KitSelectExecutor(container))
                .build();
        game.getCommandManager().register(container.getInstance().get(),
                graveyardCommand, "commandKits", "ck");
    }
}
