package ca.bungo.screens.events;

import ca.bungo.screens.Screens;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractionEvents implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        if(Screens.screen == null) return;

        Screens.screen.handleClick(player);
    }

}
