package ca.bungo.screens.commands;

import ca.bungo.screens.Screens;
import ca.bungo.screens.api.ScreenComponent;
import ca.bungo.screens.api.animation.Animatable;
import ca.bungo.screens.api.components.buttons.SimpleButtonComponent;
import ca.bungo.screens.api.components.generics.CentredLabelComponent;
import ca.bungo.screens.api.components.generics.LabelComponent;
import ca.bungo.screens.api.components.Screen;
import ca.bungo.screens.api.components.generics.SimpleGlyphComponent;
import ca.bungo.screens.api.impl.animations.MoveToAnimation;
import ca.bungo.screens.utility.FontHelper;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

public class TestingCommand extends Command {

    private Screen screen;

    public TestingCommand() {
        super("test");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String @NotNull [] args) {
        if(!(sender instanceof Player player)) return false;

        Location location = new Location(player.getWorld(), 0, 85, 0);

        if (args.length == 0) {
            player.sendMessage(Component.text("\uE200").font(Key.key("bungoscreens", "screens")));
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
                        400,
                        400
                );
                screen.setColor(Color.BLACK);
                screen.addComponent(new LabelComponent(0, 0, Component.text("New Screen!", NamedTextColor.GOLD)));
                screen.addComponent(new SimpleButtonComponent(
                        "submit-button",
                        0, 50, 0.25f, 0.125f,
                        Color.GREEN, Component.text("Submit"),
                        (clicker, localX, localY, clicked) -> {
                            MoveToAnimation moveAnim = new MoveToAnimation(localX + 10, localY + 10, 1);
                            ScreenComponent component = clicked.getComponent("submit-button");

                            if(!(component instanceof Animatable animatable)){
                                return;
                            }
                            animatable.addAnimation(moveAnim);
                        }
                ));
                screen.addComponent(new SimpleGlyphComponent(0, 100, 32, 8, '\uE200', 32, 8));
                screen.addComponent(new CentredLabelComponent(0, 150, Component.text("Centred Text!", NamedTextColor.GOLD)));
                Screens.getInstance().screenManager.register(screen);
            } else if (cmd.equalsIgnoreCase("reload")) {
                if(screen != null) {

                }
            }
        }

        return false;
    }
}
