package ca.bungo.screens;

import ca.bungo.screens.api.registry.ScreenManager;
import ca.bungo.screens.commands.TestingCommand;
import ca.bungo.screens.events.InteractionEvents;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

public final class Screens extends JavaPlugin {
    public static Logger LOGGER;
    public static final String NAMESPACE = "bungoscreens";

    private static Screens instance;

    public ScreenManager screenManager;

    @Override
    public void onEnable() {
        LOGGER = getSLF4JLogger();
        instance = this;

        screenManager = new ScreenManager();

        registerEvents();
        registerCommands();
    }

    @Override
    public void onDisable() {
        screenManager.onDisable();
    }


    private void registerEvents() {
        this.getServer().getPluginManager().registerEvents(new InteractionEvents(), this);
    }

    private void registerCommands() {
        this.getServer().getCommandMap().register(NAMESPACE, new TestingCommand());
    }

    public static Screens getInstance() {
        return instance;
    }
}
