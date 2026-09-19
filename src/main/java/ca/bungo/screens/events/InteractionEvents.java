package ca.bungo.screens.events;

import ca.bungo.screens.Screens;
import ca.bungo.screens.api.impl.components.Screen;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;

public class InteractionEvents implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        List<Screen> screens = Screens.getInstance().screenManager.getScreensInWorld(player.getWorld());
        for (Screen screen : screens) {
            screen.handleClick(player);
        }
    }

}
