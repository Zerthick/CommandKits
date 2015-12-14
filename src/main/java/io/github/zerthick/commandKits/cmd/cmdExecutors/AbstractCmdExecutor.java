package io.github.zerthick.commandKits.cmd.cmdExecutors;

import io.github.zerthick.commandKits.CommandKitsMain;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.plugin.PluginContainer;

public abstract class AbstractCmdExecutor implements CommandExecutor{

    protected PluginContainer container;
    protected CommandKitsMain plugin;

    public AbstractCmdExecutor(PluginContainer pluginContainer) {
        super();
        container = pluginContainer;
        plugin = (container.getInstance().get() instanceof CommandKitsMain ? (CommandKitsMain) container.getInstance().get() : null);
    }

}
