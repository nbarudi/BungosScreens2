package ca.bungo.screens.api.impl.animations;

import ca.bungo.screens.api.animation.Animation;
import ca.bungo.screens.api.animation.AnimationChannel;
import ca.bungo.screens.api.animation.locks.Positionable;
import org.joml.Vector2f;

public class MoveToAnimation implements Animation<Positionable> {

    private float time;
    private float elapsedTime;

    private Vector2f startingPosition;
    private final Vector2f endingPosition;

    public MoveToAnimation(float newX, float newY, float time){
        endingPosition = new Vector2f(newX, newY);

        this.time = time;
    }

    @Override
    public AnimationChannel channel() {
        return AnimationChannel.POSITON;
    }

    @Override
    public Class<Positionable> requiredType() {
        return Positionable.class;
    }

    @Override
    public void start(Positionable target) {
        startingPosition = target.position();
        this.elapsedTime = 0;
    }

    @Override
    public void tick(Positionable target, float deltaSeconds) {
        elapsedTime += deltaSeconds;
        float progress = Math.min(elapsedTime / time, 1f);
        target.position(this.lerpPosition(startingPosition, endingPosition, progress));

    }

    private Vector2f lerpPosition(Vector2f from, Vector2f to, float progress) {
        return new Vector2f(from).lerp(to, progress);
    }

    @Override
    public boolean isFinished() {
        return elapsedTime >= time;
    }
}
