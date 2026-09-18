package ca.bungo.screens.utility;

import org.joml.Vector3f;

public final class TextDisplayMetrics {

    public static final double PIXELS_TO_WORLD = 0.025;
    public static final double UNIT_SCALE = 0.01;
    public static final int GLYPH_PADDING_PX = 2;
    public static final float X_ANCHOR_OFFSET_PX = 7.5f;
    public static final float TEXT_LINE_HEIGHT_PX = 9f;
    public static final float TEXT_X_RESIDUAL_PX = 2f;
    public static final float LAYER_STEP_WORLD = 0.0005f;

    private TextDisplayMetrics() {}


    public static Vector3f computeBackgroundScale(int glyphPixelSize, float desiredWidthLocal, float desiredHeightLocal, double unitScale) {
        return computeBackgroundScale(glyphPixelSize, glyphPixelSize, desiredWidthLocal, desiredHeightLocal, unitScale);
    }

    public static Vector3f computeBackgroundScale(float glyphPixelWidth, float glyphPixelHeight, float desiredWidthLocal, float desiredHeightLocal, double unitScale) {
        double baseWorldWidth = glyphPixelWidth * TextDisplayMetrics.PIXELS_TO_WORLD;
        double baseWorldHeight = glyphPixelHeight * TextDisplayMetrics.PIXELS_TO_WORLD;

        float scaleX = (float) ((desiredWidthLocal * unitScale) / baseWorldWidth);
        float scaleY = (float) ((desiredHeightLocal * unitScale) / baseWorldHeight);

        return new Vector3f(scaleX, scaleY, 1f);
    }
}
