package ca.bungo.screens.api.animation.locks;

import ca.bungo.screens.api.animation.AnimationLock;

public interface RotatableX extends AnimationLock {
    float rotationX();
    void rotationX(float radians);
}
