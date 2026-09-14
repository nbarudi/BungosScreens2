package ca.bungo.screens.api.components.generics;

import ca.bungo.screens.Screens;
import ca.bungo.screens.api.RenderContext;
import ca.bungo.screens.api.components.AbstractScreenComponent;
import ca.bungo.screens.impl.Screen;
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
import org.joml.Vector3f;

import java.util.UUID;
import java.util.function.Consumer;

public class LabelComponent extends AbstractScreenComponent {

    private final float widthPercent;   // 0.0–1.0, fraction of screen width
    private final float heightPercent;  // 0.0–1.0, fraction of screen height

    private TextDisplay textDisplay;
    private Component component;

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

    @Override
    public void render(RenderContext screen) {
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

        Screens.LOGGER.info("Uniform Scale: {}", uniformScale);

        float totalAdvancePx = FontHelper.measureWidth(plainText());
        float halfWidthWorld = uniformScale * (totalAdvancePx - TextDisplayMetrics.TEXT_X_RESIDUAL_PX) * (float) TextDisplayMetrics.PIXELS_TO_WORLD / 2f;
        Vector3f translation = new Vector3f(right).mul(halfWidthWorld)
                .add(new Vector3f(down).mul(heightWorld))
                .add(0, 0,  0);

        translation.sub(new Vector3f(screen.normal()).mul((screen.layer()+1) * TextDisplayMetrics.LAYER_STEP_WORLD));

        Transformation transformation = new Transformation(
                translation,
                new Quaternionf(screen.orientation()),
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

    public void offsetTranslation(Consumer<TextDisplay> displayConsumer){
        displayConsumer.accept(textDisplay);
    }

    @Override
    public void despawn() {
        if(textDisplay != null) {
            textDisplay.remove();

        }
    }

}
