package ca.bungo.screens.api.animation.locks;

import ca.bungo.screens.api.animation.AnimationLock;

public interface UniformlyScalable extends AnimationLock {
    float scale();
    void scale(float newScale);
}
