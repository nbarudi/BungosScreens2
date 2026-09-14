package ca.bungo.screens.api;

import ca.bungo.screens.impl.Screen;

public interface ScreenComponent {

    String id();
    float x();
    float y();
    float width();
    float height();

    void render(RenderContext screen);
    void despawn();
}
