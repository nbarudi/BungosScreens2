package ca.bungo.screens.api.animation.locks;

import ca.bungo.screens.api.animation.AnimationLock;
import org.joml.Vector2f;

public interface Positionable extends AnimationLock {
    Vector2f position();
    void position(Vector2f newPosition);
    void position(float x, float y);
}
