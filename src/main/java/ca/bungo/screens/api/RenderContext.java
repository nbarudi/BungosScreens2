package ca.bungo.screens.api;

import ca.bungo.screens.api.components.Screen;
import org.bukkit.Location;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public interface RenderContext {
    Location origin();
    Quaternionf orientation();
    Vector3f right();
    Vector3f down();
    Vector3f normal();
    float width();
    float height();

    Screen screen();

    default int layer() {
        return 0; // most components sit flush with their parent's plane
    }
}