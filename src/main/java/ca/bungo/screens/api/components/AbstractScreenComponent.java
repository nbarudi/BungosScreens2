package ca.bungo.screens.api.components;

import ca.bungo.screens.api.ScreenComponent;

import java.util.UUID;

public abstract class AbstractScreenComponent implements ScreenComponent {

    private final String id;
    private final float x;
    private final float y;
    private final float width;
    private final float height;


    public AbstractScreenComponent(String id, float x, float y, float width, float height) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public AbstractScreenComponent(float x, float y, float width, float height) {
        this.id = UUID.randomUUID().toString();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public String id() { return id; }
    @Override
    public float x() { return x; }
    @Override
    public float y() { return y; }
    @Override
    public float width() { return width; }
    @Override
    public float height() { return height; }

}
