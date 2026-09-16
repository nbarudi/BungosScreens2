package ca.bungo.screens.api.animation.locks;

import ca.bungo.screens.api.animation.AnimationLock;

public interface RotatableY extends AnimationLock {
    float rotationY();
    void rotationY(float radians);
}
