package ca.bungo.screens.api.animation;

public interface Animation<T extends AnimationLock> {
    AnimationChannel channel();
    Class<T> requiredType();

    void start(T target);
    void tick(T target, float deltaSeconds);
    boolean isFinished();
}
