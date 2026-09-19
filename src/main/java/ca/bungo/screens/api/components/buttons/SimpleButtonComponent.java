package ca.bungo.screens.api.components.buttons;

import ca.bungo.screens.api.OnScreenClickConsumer;
import ca.bungo.screens.api.RenderContext;
import ca.bungo.screens.api.animation.Animatable;
import ca.bungo.screens.api.animation.Animation;
import ca.bungo.screens.api.animation.AnimationChannel;
import ca.bungo.screens.api.animation.locks.Positionable;
import ca.bungo.screens.api.animation.locks.RotatableZ;
import ca.bungo.screens.api.animation.locks.Scalable;
import ca.bungo.screens.api.components.AbstractScreenComponent;
import ca.bungo.screens.api.components.InteractableComponent;
import ca.bungo.screens.api.components.generics.CentredLabelComponent;
import ca.bungo.screens.api.components.generics.LabelComponent;
import ca.bungo.screens.api.components.generics.SimpleRectComponent;
import ca.bungo.screens.api.components.Screen;
import ca.bungo.screens.utility.TextDisplayMetrics;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;


public class SimpleButtonComponent extends AbstractScreenComponent implements InteractableComponent, RenderContext, Animatable, Positionable, Scalable {

    private final OnScreenClickConsumer onClick;

    private final Map<AnimationChannel, Animation<?>> animations = new HashMap<>();

    private Color backgroundColor;
    private CentredLabelComponent label;
    private SimpleRectComponent background;

    private Component labelText;

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

    private float centreX() {
        return x() - TextDisplayMetrics.TEXT_X_RESIDUAL_PX/2f + width() / 2f;
    }

    private float centreY() {
        return y() + height() / 2f;
    }

    @Override
    public void spawn(RenderContext screen) {
        if(background != null) {
            background.despawn();
        }
        if(label != null) {
            label.despawn();
        }
        this.lastContext = screen;
        if(labelText != null) {
            label = new CentredLabelComponent(centreX(), centreY(), labelText);
            label.spawn(this);
        }
        background = new SimpleRectComponent(x(), y(), width(), height(), backgroundColor);
        background.spawn(this);
    }

    @Override
    public void update(RenderContext context) {
        lastContext = context;
        if(background != null) {
            background.position(x(), y());
            //background.scale(width(), height());
            background.update(this);
        }
        if(label != null) {
            label.position(centreX(), centreY());
            //label.scale(width(), height());
            label.update(this);
        }
    }

    @Override
    public void despawn() {
        if(label != null){
            label.despawn();
            label = null;
        }
        if(background != null) {
            background.despawn();
            background = null;
        }
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
        if (lastContext == null) {
            return 0;
        }
        return lastContext.layer()+1;
    }

    @Override
    public Screen screen() {
        return lastContext.screen();
    }

    @Override
    public Map<AnimationChannel, Animation<?>> activeAnimations() {
        return animations;
    }

    @Override
    public Vector2f position() {
        return new Vector2f(x(), y());
    }

    @Override
    public void position(Vector2f newPosition) {
        setX(newPosition.x());
        setY(newPosition.y());
        update(lastContext);
    }

    @Override
    public void position(float x, float y) {
        setX(x);
        setY(y);
        update(lastContext);
    }

    @Override
    public Vector2f scale() {
        return new Vector2f(widthPercent, heightPercent);
    }

    @Override
    public void scale(Vector2f newScale) {
        widthPercent = newScale.x;
        heightPercent = newScale.y;
        update(lastContext);
    }

    @Override
    public void scale(float x, float y) {
        widthPercent = x;
        heightPercent = y;
        update(lastContext);
    }
}
