package ca.bungo.screens.api.animation.locks;

import org.joml.Vector2f;

public interface Scalable {
    Vector2f scale();
    void scale(Vector2f newScale);
    void scale(float x, float y);
}
