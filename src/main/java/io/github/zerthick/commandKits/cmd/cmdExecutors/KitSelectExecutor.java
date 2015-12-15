package io.github.zerthick.commandKits.cmd.cmdExecutors;

import io.github.zerthick.commandKits.cmdKit.CommandKitManager;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;

import java.util.Optional;

public class KitSelectExecutor extends AbstractCmdExecutor implements CommandExecutor{

    public KitSelectExecutor(PluginContainer pluginContainer) {
        super(pluginContainer);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if(src instanceof Player){
            Player player = (Player)src;
            Optional<String> optionalKitName = args.getOne("kitName");
            if(optionalKitName.isPresent()){
                String kitName = optionalKitName.get().toUpperCase();
                CommandKitManager kitManager = plugin.getKitManager();
                if(kitManager.isKit(kitName)){
                    plugin.getLogger().info(kitManager.getKit(kitName).getRequirementsMap(player).toString());
                    kitManager.getKit(kitName).executeCommands(player);
                }
            } else {
                src.sendMessage(Texts.of(TextColors.DARK_GREEN, container.getName(),
                        TextColors.GREEN, " version: ", TextColors.DARK_GREEN,
                        container.getVersion(), TextColors.GREEN, " by ",
                        TextColors.DARK_GREEN, "Zerthick"));
            }
        }
        return CommandResult.success();
    }
}
