package ca.bungo.screens.api.animation;

import java.util.Iterator;
import java.util.Map;

public interface Animatable {

    Map<AnimationChannel, Animation<?>> activeAnimations();

    default <T extends AnimationLock> void addAnimation(Animation<T> animation) {
        if (!animation.requiredType().isInstance(this)) {
            throw new IllegalArgumentException(
                    "Cannot add " + animation.getClass().getSimpleName() +
                            " to " + this.getClass().getSimpleName() +
                            ": requires " + animation.requiredType().getSimpleName()
            );
        }
        @SuppressWarnings("unchecked")
        T target = (T) this;
        animation.start(target);
        activeAnimations().put(animation.channel(), animation);
    }

    default void tickAnimations(float deltaSeconds) {
        Iterator<Animation<?>> it = activeAnimations().values().iterator();
        while (it.hasNext()) {
            Animation<?> animation = it.next();
            tickOne(animation, deltaSeconds);
            if (animation.isFinished()) {
                it.remove();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends AnimationLock> void tickOne(Animation<T> animation, float deltaSeconds) {
        animation.tick((T) this, deltaSeconds);
    }

    default void cancelAnimation(AnimationChannel channel) {
        activeAnimations().remove(channel);
    }

    default boolean isAnimating(AnimationChannel channel) {
        return activeAnimations().containsKey(channel);
    }
}
