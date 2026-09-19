package ca.bungo.screens.api.impl.components;

import ca.bungo.screens.api.ScreenComponent;

import java.util.UUID;

public abstract class AbstractScreenComponent implements ScreenComponent {

    private final String id;
    private float x;
    private float y;
    private float width;
    private float height;


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
    protected void setX(float x){ this.x = x; }
    protected void setY(float y){ this.y = y; }
    protected void setWidth(float width){ this.width = width; }
    protected void setHeight(float height){ this.height = height; }
    @Override
    public float width() { return width; }
    @Override
    public float height() { return height; }

}
