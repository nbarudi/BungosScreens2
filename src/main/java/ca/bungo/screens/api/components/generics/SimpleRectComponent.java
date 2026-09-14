package ca.bungo.screens.api.components.generics;

import ca.bungo.screens.Screens;
import ca.bungo.screens.api.RenderContext;
import ca.bungo.screens.api.components.AbstractScreenComponent;
import ca.bungo.screens.impl.Screen;
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
import org.joml.Vector3f;

import java.util.UUID;
import java.util.function.Consumer;


public class SimpleRectComponent extends AbstractScreenComponent {

    private static final int GLYPH_PIXEL_SIZE = 16;

    private final String id;
    private final float x, y, width, height;
    private final Color color;

    private TextDisplay display;

    public SimpleRectComponent(String id, float x, float y, float width, float height, Color color) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;

        super(id, x, y, width, height);
    }

    public SimpleRectComponent(float x, float y, float width, float height, Color color) {
        this(UUID.randomUUID().toString(), x, y, width, height, color);
        Screens.LOGGER.info("{} {}", width, height);
    }

    @Override public String id() { return id; }
    @Override public float x() { return x; }
    @Override public float y() { return y; }
    @Override public float width() { return width; }
    @Override public float height() { return height; }

    @Override
    public void render(RenderContext screen) {
        Vector3f right = screen.right();
        Vector3f down = screen.down();
        double unitScale = TextDisplayMetrics.UNIT_SCALE;

        Vector3f localOffset = new Vector3f(right).mul(x * (float) unitScale)
                .add(new Vector3f(down).mul(y * (float) unitScale));

        Location spawnLocation = screen.origin().clone().add(localOffset.x, localOffset.y, localOffset.z);

        Vector3f scale = TextDisplayMetrics.computeBackgroundScale(GLYPH_PIXEL_SIZE, width, height, unitScale);

        float heightWorld = scale.y * (GLYPH_PIXEL_SIZE + TextDisplayMetrics.GLYPH_PADDING_PX) * (float) TextDisplayMetrics.PIXELS_TO_WORLD;
        float widthWorld = scale.x * TextDisplayMetrics.X_ANCHOR_OFFSET_PX * (float) TextDisplayMetrics.PIXELS_TO_WORLD;

        Vector3f translation = new Vector3f(down).mul(heightWorld)
                .add(new Vector3f(right).mul(widthWorld));
        translation.sub(new Vector3f(screen.normal()).mul((screen.layer()) * TextDisplayMetrics.LAYER_STEP_WORLD));

        Transformation transformation = new Transformation(
                translation,
                new Quaternionf(screen.orientation()),
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

    public void offsetTranslation(Consumer<TextDisplay> displayConsumer){
        displayConsumer.accept(display);
    }

    public void despawn() {
        if (display != null) display.remove();
    }
}
