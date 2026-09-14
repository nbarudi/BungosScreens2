package ca.bungo.screens.api.components.buttons;

import ca.bungo.screens.Screens;
import ca.bungo.screens.api.OnScreenClickConsumer;
import ca.bungo.screens.api.RenderContext;
import ca.bungo.screens.api.components.AbstractScreenComponent;
import ca.bungo.screens.api.components.InteractableComponent;
import ca.bungo.screens.api.components.generics.LabelComponent;
import ca.bungo.screens.api.components.generics.SimpleRectComponent;
import ca.bungo.screens.impl.Screen;
import ca.bungo.screens.utility.TextDisplayMetrics;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.joml.Quaternionf;
import org.joml.Vector3f;


public class SimpleButtonComponent extends AbstractScreenComponent implements InteractableComponent, RenderContext {

    private final OnScreenClickConsumer onClick;

    private Color backgroundColor;
    private LabelComponent label;
    private SimpleRectComponent background;

    private Component labelText;
    private int layer = 0;

    private RenderContext lastContext;

    private float widthPercent;
    private float heightPercent;


    public SimpleButtonComponent(String id, float x, float y, float widthPercent, float heightPercent, Color backgroundColor, OnScreenClickConsumer onClick) {
        super(id, x, y, 0, 0);
        this.backgroundColor = backgroundColor;
        this.onClick = onClick;
        this.widthPercent = widthPercent;
        this.heightPercent = heightPercent;
    }

    public SimpleButtonComponent(String id, float x, float y, float widthPercent, float heightPercent, Color backgroundColor, Component labelText, OnScreenClickConsumer onClick) {
        this(id, x, y, widthPercent, heightPercent, backgroundColor, onClick);
        this.labelText = labelText;
    }

    @Override
    public float width() {
        if(lastContext == null) {
            return 0;
        }
        return widthPercent  * lastContext.width();


    }

    @Override
    public float height() {
        if(lastContext == null) {
            return 0;
        }
        return heightPercent * lastContext.height();
    }

    @Override
    public void render(RenderContext screen) {
        this.lastContext = screen;
        this.layer = screen.layer()+1;
        if(labelText != null) {
            label = new LabelComponent(x(), y(), labelText);
            label.render(this);
        }
        background = new SimpleRectComponent(x(), y(), width(), height(), backgroundColor);
        background.render(this);
    }

    @Override
    public void despawn() {
        if(label != null)  label.despawn();
        if(background != null) background.despawn();
    }

    @Override
    public void onClick(Player player, float localX, float localY) {
        onClick.onClick(player, localX, localY, lastContext.screen());
    }

    @Override
    public Location origin() {
        return lastContext.origin();
    }

    @Override
    public Quaternionf orientation() {
        return lastContext.orientation();
    }

    @Override
    public Vector3f right() {
        return lastContext.right();
    }

    @Override
    public Vector3f down() {
        return lastContext.down();
    }

    @Override
    public Vector3f normal() {
        return lastContext.normal();
    }

    @Override
    public int layer() {
        return layer;
    }

    @Override
    public Screen screen() {
        return lastContext.screen();
    }
}
