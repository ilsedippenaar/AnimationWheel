package lights;

import base.Mat;

/**
 * @author Jaco
 */
public class AmbientLight extends AbstractLight {
    public AmbientLight(float intensity, int color) {
        super(intensity, color);
    }

    @Override
    public int light(float[][] points, float[] norm, int color) {
        return Mat.multByChannel(Mat.averageColors(new int[]{color, this.color}), intensity);
    }
}
