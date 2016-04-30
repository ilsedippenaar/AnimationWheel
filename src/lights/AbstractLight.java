package lights;

import base.Triangle;
import shapes.AbstractShape;

/**
 * @author Jaco
 */
public abstract class AbstractLight {
    public AbstractShape shape;
    float intensity;
    int color;

    public AbstractLight(float intensity, int color) {
        this.intensity = intensity;
        this.color = color;
    }

    public AbstractLight(float intensity, int color, AbstractShape shape) {
        this(intensity, color);
        this.shape = shape;
    }

    public abstract int light(float[][] points, float[] norm, int color);
}
