package io.github.zerthick.commandKits.cmd.cmdExecuters;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;

public class KitExecuter implements CommandExecutor{

    private PluginContainer container;

    public KitExecuter(PluginContainer pluginContainer) {
        super();
        container = pluginContainer;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        src.sendMessage(Texts.of(TextColors.DARK_GREEN, container.getName(),
                TextColors.GREEN, " version: ", TextColors.DARK_GREEN,
                container.getVersion(), TextColors.GREEN, " by ",
                TextColors.DARK_GREEN, "Zerthick"));

        return CommandResult.success();
    }
}
