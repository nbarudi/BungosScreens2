package ca.bungo.screens;

import ca.bungo.screens.commands.TestingCommand;
import ca.bungo.screens.events.InteractionEvents;
import ca.bungo.screens.impl.Screen;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

public final class Screens extends JavaPlugin {

    public static Screen screen;

    public static Logger LOGGER;
    public static final String NAMESPACE = "bungoscreens";

    @Override
    public void onEnable() {
        LOGGER = getSLF4JLogger();

        registerEvents();
        registerCommands();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    private void registerEvents() {
        this.getServer().getPluginManager().registerEvents(new InteractionEvents(), this);
    }

    private void registerCommands() {
        this.getServer().getCommandMap().register(NAMESPACE, new TestingCommand());
    }
}
