package ca.bungo.screens.api;

import org.bukkit.entity.Player;

public interface InteractableComponent {
    void onClick(Player player, float localX, float localY);
}
