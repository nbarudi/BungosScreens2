package ca.bungo.screens.api.impl.animations;

import ca.bungo.screens.api.animation.Animatable;
import ca.bungo.screens.api.animation.Animation;
import ca.bungo.screens.api.animation.AnimationChannel;
import ca.bungo.screens.api.animation.locks.RotatableZ;
import org.joml.Vector2f;

public class RotateZAnimation implements Animation<RotatableZ> {

    private float time;
    private float elapsedTime;

    private float startingRotation;
    private final float endingRotation;

    public RotateZAnimation(float radians, float time){
        this.endingRotation = radians;

        this.time = time;
    }

    @Override
    public AnimationChannel channel() {
        return AnimationChannel.ROTATION_Z;
    }

    @Override
    public Class<RotatableZ> requiredType() {
        return RotatableZ.class;
    }

    @Override
    public void start(RotatableZ target) {
        startingRotation = target.rotationZ();
        this.elapsedTime = 0;
    }

    @Override
    public void tick(RotatableZ target, float deltaSeconds) {
        elapsedTime += deltaSeconds;
        float progress = Math.min(elapsedTime / time, 1f);
        target.rotationZ(this.lerpRotation(startingRotation, endingRotation, progress));
    }

    private float lerpRotation(float from, float to, float t) {
        float diff = to - from;
        diff = ((diff + (float) Math.PI) % (2f * (float) Math.PI));
        if (diff < 0) diff += 2f * (float) Math.PI;
        diff -= (float) Math.PI;
        return from + diff * t;
    }

    @Override
    public boolean isFinished() {
        return elapsedTime >= time;
    }
}
