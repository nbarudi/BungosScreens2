package ca.bungo.screens.api.impl.components.generics;

import ca.bungo.screens.api.RenderContext;
import ca.bungo.screens.api.animation.Animatable;
import ca.bungo.screens.api.animation.Animation;
import ca.bungo.screens.api.animation.AnimationChannel;
import ca.bungo.screens.api.animation.locks.Positionable;
import ca.bungo.screens.api.animation.locks.RotatableZ;
import ca.bungo.screens.api.animation.locks.Scalable;
import ca.bungo.screens.api.impl.components.AbstractScreenComponent;
import ca.bungo.screens.utility.TextDisplayMetrics;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;


public class SimpleRectComponent extends AbstractScreenComponent implements Animatable, Positionable, RotatableZ, Scalable {

    private static final int GLYPH_PIXEL_SIZE = 16;

    private final Map<AnimationChannel, Animation<?>> animations = new HashMap<>();

    private final String id;
    private final Color color;

    private TextDisplay display;

    private float rotationZ = 0;
    private RenderContext lastContext;

    public SimpleRectComponent(String id, float x, float y, float width, float height, Color color) {
        super(id, x, y, width, height);
        this.id = id;
        this.color = color;
    }

    public SimpleRectComponent(float x, float y, float width, float height, Color color) {
        this(UUID.randomUUID().toString(), x, y, width, height, color);
    }

    @Override public String id() { return id; }

    @Override
    public void spawn(RenderContext screen) {
        lastContext = screen;
        if(display != null){
            despawn();
        }
        Vector3f right = screen.right();
        Vector3f down = screen.down();
        double unitScale = TextDisplayMetrics.UNIT_SCALE;

        Vector3f localOffset = new Vector3f(right).mul(x() * (float) unitScale)
                .add(new Vector3f(down).mul(y() * (float) unitScale));

        Location spawnLocation = screen.origin().clone().add(localOffset.x, localOffset.y, localOffset.z);

        Vector3f scale = TextDisplayMetrics.computeBackgroundScale(GLYPH_PIXEL_SIZE, width(), height(), unitScale);

        float heightWorld = scale.y * (GLYPH_PIXEL_SIZE + TextDisplayMetrics.GLYPH_PADDING_PX) * (float) TextDisplayMetrics.PIXELS_TO_WORLD;
        float widthWorld = scale.x * TextDisplayMetrics.X_ANCHOR_OFFSET_PX * (float) TextDisplayMetrics.PIXELS_TO_WORLD;

        Vector3f translation = new Vector3f(down).mul(heightWorld)
                .add(new Vector3f(right).mul(widthWorld));
        translation.sub(new Vector3f(screen.normal()).mul((screen.layer()) * TextDisplayMetrics.LAYER_STEP_WORLD));

        Quaternionf baseRotation = new Quaternionf(screen.orientation());
        baseRotation.rotateZ(rotationZ);

        Transformation transformation = new Transformation(
                translation,
                baseRotation,
                scale,
                new Quaternionf()
        );

        display = spawnLocation.getWorld().spawn(spawnLocation, TextDisplay.class, d -> {
            d.text(Component.text("\uF000").font(Key.key("bungoscreens", "screens")).color(TextColor.color(color.asRGB())));
            d.setAlignment(TextDisplay.TextAlignment.LEFT);
            d.setBillboard(Display.Billboard.FIXED);
            d.setShadowed(false);
            d.setSeeThrough(false);
            d.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
            d.setTransformation(transformation);
        });
    }

    @Override
    public void update(RenderContext context) {
        lastContext = context;
        if(display == null) return;
        Vector3f right = context.right();
        Vector3f down = context.down();
        double unitScale = TextDisplayMetrics.UNIT_SCALE;

        Vector3f scale = TextDisplayMetrics.computeBackgroundScale(GLYPH_PIXEL_SIZE, width(), height(), unitScale);

        float heightWorld = scale.y * (GLYPH_PIXEL_SIZE + TextDisplayMetrics.GLYPH_PADDING_PX) * (float) TextDisplayMetrics.PIXELS_TO_WORLD;
        float widthWorld = scale.x * TextDisplayMetrics.X_ANCHOR_OFFSET_PX * (float) TextDisplayMetrics.PIXELS_TO_WORLD;

        Vector3f translation = new Vector3f(down).mul(heightWorld)
                .add(new Vector3f(right).mul(widthWorld));
        translation.sub(new Vector3f(context.normal()).mul((context.layer()) * TextDisplayMetrics.LAYER_STEP_WORLD));

        Vector3f localOffset = new Vector3f(right).mul(x() * (float) unitScale)
                .add(new Vector3f(down).mul(y() * (float) unitScale));

        Location spawnLocation = context.origin().clone().add(localOffset.x, localOffset.y, localOffset.z);

        Quaternionf baseRotation = new Quaternionf(context.orientation());
        baseRotation.rotateZ(rotationZ);

        Transformation transformation = new Transformation(
                translation,
                baseRotation,
                scale,
                new Quaternionf()
        );

        display.setTransformation(transformation);
        display.teleport(spawnLocation);
    }

    public void offsetTranslation(Consumer<TextDisplay> displayConsumer){
        displayConsumer.accept(display);
    }

    public void despawn() {
        if (display != null) {
            display.remove();
            display = null;
        }
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
    public float rotationZ() {
        return rotationZ;
    }

    @Override
    public void rotationZ(float radians) {
        this.rotationZ = radians;
        update(lastContext);
    }

    @Override
    public Vector2f scale() {
        return new Vector2f(width(), height());
    }

    @Override
    public void scale(Vector2f newScale) {
        setWidth(newScale.x);
        setHeight(newScale.y);
        update(lastContext);
    }

    @Override
    public void scale(float x, float y) {
        setWidth(x);
        setHeight(y);
        update(lastContext);
    }
}
