package ca.bungo.screens.commands;

import ca.bungo.screens.Screens;
import ca.bungo.screens.api.components.buttons.SimpleButtonComponent;
import ca.bungo.screens.api.components.generics.LabelComponent;
import ca.bungo.screens.api.components.Screen;
import ca.bungo.screens.utility.FontHelper;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

import java.util.UUID;

public class TestingCommand extends Command {

    private Screen screen;

    public TestingCommand() {
        super("test");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String @NotNull [] args) {
        if(!(sender instanceof Player player)) return false;

        Location location = new Location(player.getWorld(), 0, 75, 0);

        if (args.length == 0) {
            player.sendMessage(Component.text("hello there!"));
            player.sendMessage(Component.text("hello there!").font(Key.key("bungoscreens", "screens")));
            player.sendMessage(Component.text("Font Width: " + FontHelper.measureWidth("hello there!")));

            TextDisplay dis = player.getLocation().getWorld().spawn(player.getLocation(), TextDisplay.class);
            dis.text(Component.text("Hello World!", NamedTextColor.RED));
        } else if (args.length == 1) {
            String cmd = args[0];

            if(cmd.equalsIgnoreCase("spawn")){
                if(screen != null) {
                    Screens.getInstance().screenManager.unregister(screen.id());
                    screen = null;
                }

                screen = new Screen(
                        location,
                        new Quaternionf(0, 0, 0, 1),
                        1080,
                        720
                );
                screen.setColor(Color.BLACK);
                screen.addComponent(new LabelComponent(0, 0, Component.text("difforwant world!", NamedTextColor.GOLD)));
                screen.addComponent(new SimpleButtonComponent(
                        UUID.randomUUID().toString(),
                        0, 0, 0.25f, 0.125f,
                        Color.GREEN, Component.text("Submit"),
                        (clicker, localX, localY, clicked) -> {
                            Screens.LOGGER.info("{} has clicked the screen!", clicker.getName());
                            Bukkit.getScheduler().runTaskLater(JavaPlugin.getProvidingPlugin(TestingCommand.class), clicked::despawn, 10);
                        }
                ));
                Screens.getInstance().screenManager.register(screen);
            } else if (cmd.equalsIgnoreCase("reload")) {
                if(screen != null) {

                }
            }
        }

        return false;
    }
}
