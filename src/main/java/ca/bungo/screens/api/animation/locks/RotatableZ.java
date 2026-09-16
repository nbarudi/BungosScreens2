package ca.bungo.screens.api.animation.locks;

import ca.bungo.screens.api.animation.AnimationLock;

public interface RotatableZ extends AnimationLock {
    float rotationZ();
    void rotationZ(float radians);
}
