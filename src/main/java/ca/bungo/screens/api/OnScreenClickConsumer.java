package ca.bungo.screens.api;

import ca.bungo.screens.impl.Screen;
import org.bukkit.entity.Player;

@FunctionalInterface
public interface OnScreenClickConsumer {
    void onClick(Player player, float localX, float localY, Screen screen);
}
