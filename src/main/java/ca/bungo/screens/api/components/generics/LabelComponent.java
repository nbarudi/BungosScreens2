package ca.bungo.screens.api.components.generics;

import ca.bungo.screens.api.RenderContext;
import ca.bungo.screens.api.animation.Animatable;
import ca.bungo.screens.api.animation.Animation;
import ca.bungo.screens.api.animation.AnimationChannel;
import ca.bungo.screens.api.animation.locks.Positionable;
import ca.bungo.screens.api.animation.locks.RotatableZ;
import ca.bungo.screens.api.animation.locks.Scalable;
import ca.bungo.screens.api.components.AbstractScreenComponent;
import ca.bungo.screens.utility.ComponentUtility;
import ca.bungo.screens.utility.FontHelper;
import ca.bungo.screens.utility.TextDisplayMetrics;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
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

public class LabelComponent extends AbstractScreenComponent implements Animatable, Positionable, RotatableZ, Scalable {

    private final Map<AnimationChannel, Animation<?>> animations = new HashMap<>();

    private float widthPercent;   // 0.0–1.0, fraction of screen width
    private float heightPercent;  // 0.0–1.0, fraction of screen height

    private TextDisplay textDisplay;
    private Component component;
    private RenderContext lastContext;

    private float rotationZ = 0f;

    public LabelComponent(String id, float x, float y, float widthPercent, float heightPercent, Component text) {
        super(id, x, y, 0, 0);
        this.widthPercent = widthPercent;
        this.heightPercent = heightPercent;
        this.component = text.font(Key.key("bungoscreens", "screens"));
    }

    public LabelComponent(float x, float y, float widthPercent, float heightPercent, Component text) {
        this(UUID.randomUUID().toString(), x, y, widthPercent, heightPercent, text);
    }

    public LabelComponent(float x, float y, Component text) {
        this(UUID.randomUUID().toString(), x, y, 1, 1, text);
    }

    public void setText(String message) {
        this.component = ComponentUtility.convertToComponent(message).font(Key.key("bungoscreens", "screens"));
    }

    public void setText(Component message) {
        this.component = message.font(Key.key("bungoscreens", "screens"));
    }

    @Override
    public float width() {
        float pixelWidth = FontHelper.measureWidth(plainText());
        float worldWidth = pixelWidth * (float) TextDisplayMetrics.PIXELS_TO_WORLD;
        return worldWidth / (float) TextDisplayMetrics.UNIT_SCALE;
    }

    @Override
    public float height() {
        float worldHeight = TextDisplayMetrics.TEXT_LINE_HEIGHT_PX * (float) TextDisplayMetrics.PIXELS_TO_WORLD;
        return worldHeight / (float) TextDisplayMetrics.UNIT_SCALE / 2;
    }

    private String plainText() {
        return PlainTextComponentSerializer.plainText().serialize(component);
    }

    protected Vector3f alignmentOffset(Vector3f right, Vector3f down, float halfWidthWorld, float heightWorld) {
        return new Vector3f(right).mul(halfWidthWorld)
                .add(new Vector3f(down).mul(heightWorld));
    }

    @Override
    public void spawn(RenderContext screen) {
        lastContext = screen;
        if(textDisplay != null) {
            despawn();
        }

        Vector3f right = screen.right();
        Vector3f down = screen.down();

        float intrinsicWidthPx  = FontHelper.measureWidth(plainText());
        float intrinsicHeightPx = TextDisplayMetrics.TEXT_LINE_HEIGHT_PX;

        float boxWorldWidth  = widthPercent  * screen.width()  * (float) TextDisplayMetrics.UNIT_SCALE;
        float boxWorldHeight = heightPercent * screen.height() * (float) TextDisplayMetrics.UNIT_SCALE;

        float intrinsicWorldWidth  = intrinsicWidthPx  * (float) TextDisplayMetrics.PIXELS_TO_WORLD;
        float intrinsicWorldHeight = intrinsicHeightPx * (float) TextDisplayMetrics.PIXELS_TO_WORLD;

        float scaleToFitX = boxWorldWidth  / intrinsicWorldWidth;
        float scaleToFitY = boxWorldHeight / intrinsicWorldHeight;

        float uniformScale = Math.min(scaleToFitX, scaleToFitY);

        Vector3f scale = new Vector3f(uniformScale, uniformScale, 1f);

        Vector3f localOffset = new Vector3f(right).mul(x()*(float) TextDisplayMetrics.UNIT_SCALE)
                .add(new Vector3f(down).mul(y()*(float) TextDisplayMetrics.UNIT_SCALE));

        Location spawnLocation = screen.origin().clone().add(
                localOffset.x, localOffset.y, localOffset.z
        );

        float heightWorld = uniformScale * TextDisplayMetrics.TEXT_LINE_HEIGHT_PX * (float) TextDisplayMetrics.PIXELS_TO_WORLD;

        float totalAdvancePx = FontHelper.measureWidth(plainText());
        float halfWidthWorld = uniformScale * (totalAdvancePx - TextDisplayMetrics.TEXT_X_RESIDUAL_PX) * (float) TextDisplayMetrics.PIXELS_TO_WORLD / 2f;
        Vector3f translation = alignmentOffset(right, down, halfWidthWorld, heightWorld);

        translation.sub(new Vector3f(screen.normal()).mul((screen.layer()+1) * TextDisplayMetrics.LAYER_STEP_WORLD));

        Quaternionf baseRotation = new Quaternionf(screen.orientation());
        baseRotation = baseRotation.rotateZ(this.rotationZ());

        Transformation transformation = new Transformation(
                translation,
                baseRotation,
                scale,
                new Quaternionf()
        );

        textDisplay = spawnLocation.getWorld().spawn(spawnLocation, TextDisplay.class);
        textDisplay.text(component);
        textDisplay.setAlignment(TextDisplay.TextAlignment.LEFT);
        textDisplay.setBillboard(Display.Billboard.FIXED);
        textDisplay.setShadowed(false);
        textDisplay.setSeeThrough(false);
        textDisplay.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
        textDisplay.setTransformation(transformation);
    }

    @Override
    public void update(RenderContext context) {
        lastContext = context;
        if(textDisplay == null) return;
        Vector3f right = context.right();
        Vector3f down = context.down();

        float intrinsicWidthPx  = FontHelper.measureWidth(plainText());
        float intrinsicHeightPx = TextDisplayMetrics.TEXT_LINE_HEIGHT_PX;

        float boxWorldWidth  = widthPercent  * context.width()  * (float) TextDisplayMetrics.UNIT_SCALE;
        float boxWorldHeight = heightPercent * context.height() * (float) TextDisplayMetrics.UNIT_SCALE;

        float intrinsicWorldWidth  = intrinsicWidthPx  * (float) TextDisplayMetrics.PIXELS_TO_WORLD;
        float intrinsicWorldHeight = intrinsicHeightPx * (float) TextDisplayMetrics.PIXELS_TO_WORLD;

        float scaleToFitX = boxWorldWidth  / intrinsicWorldWidth;
        float scaleToFitY = boxWorldHeight / intrinsicWorldHeight;

        float uniformScale = Math.min(scaleToFitX, scaleToFitY);

        Vector3f scale = new Vector3f(uniformScale, uniformScale, 1f);

        float heightWorld = uniformScale * TextDisplayMetrics.TEXT_LINE_HEIGHT_PX * (float) TextDisplayMetrics.PIXELS_TO_WORLD;

        float totalAdvancePx = FontHelper.measureWidth(plainText());
        float halfWidthWorld = uniformScale * (totalAdvancePx - TextDisplayMetrics.TEXT_X_RESIDUAL_PX) * (float) TextDisplayMetrics.PIXELS_TO_WORLD / 2f;
        Vector3f translation = alignmentOffset(right, down, halfWidthWorld, heightWorld);

        translation.sub(new Vector3f(context.normal()).mul((context.layer()+1) * TextDisplayMetrics.LAYER_STEP_WORLD));

        Vector3f localOffset = new Vector3f(right).mul(x()*(float) TextDisplayMetrics.UNIT_SCALE)
                .add(new Vector3f(down).mul(y()*(float) TextDisplayMetrics.UNIT_SCALE));
        Location spawnLocation = context.origin().clone().add(
                localOffset.x, localOffset.y, localOffset.z
        );

        Quaternionf baseRotation = new Quaternionf(context.orientation());
        baseRotation = baseRotation.rotateZ(this.rotationZ());

        Transformation transformation = new Transformation(
                translation,
                baseRotation,
                scale,
                new Quaternionf()
        );

        textDisplay.setTransformation(transformation);
        textDisplay.teleport(spawnLocation);
    }

    public void offsetTranslation(Consumer<TextDisplay> displayConsumer){
        displayConsumer.accept(textDisplay);
    }

    @Override
    public void despawn() {
        if(textDisplay != null) {
            textDisplay.remove();
            textDisplay = null;
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
