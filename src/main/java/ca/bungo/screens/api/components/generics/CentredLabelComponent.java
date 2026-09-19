package ca.bungo.screens.api.components.generics;

import net.kyori.adventure.text.Component;
import org.joml.Vector3f;

import java.util.UUID;

public class CentredLabelComponent extends LabelComponent {

    public CentredLabelComponent(String id, float x, float y, float widthPercent, float heightPercent, Component text) {
        super(id, x, y, widthPercent, heightPercent, text);
    }

    public CentredLabelComponent(float x, float y, float widthPercent, float heightPercent, Component text) {
        this(UUID.randomUUID().toString(), x, y, widthPercent, heightPercent, text);
    }

    public CentredLabelComponent(float x, float y, Component text) {
        this(UUID.randomUUID().toString(), x, y, 1, 1, text);
    }

    @Override
    protected Vector3f alignmentOffset(Vector3f right, Vector3f down, float halfWidthWorld, float heightWorld) {
        return new Vector3f(down).mul(heightWorld / 2f);
    }

}
