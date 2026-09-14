package ca.bungo.screens.api;

public interface ScreenComponent {

    String id();
    float x();
    float y();
    float width();
    float height();

    void spawn(RenderContext context);
    void update(RenderContext context);
    void despawn();
}
