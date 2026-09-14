package ca.bungo.screens.api.components;

import org.bukkit.entity.Player;

public interface InteractableComponent {
    void onClick(Player player, float localX, float localY);
}
