package lights;

import base.Mat;
import shapes.AbstractShape;

/**
 * @author Jaco
 */
public class PointLight extends AbstractLight {
    float[] pos;
    int attenuationFactor = 500; // every 500, decrease by half

    public PointLight(float intensity, int color, float[] pos) {
        super(intensity, color);
        this.pos = pos;
    }

    public PointLight(float intensity, int color, float[] pos, AbstractShape shape, int attenuationFactor) {
        this(intensity, color, pos);
        this.shape = shape;
        this.attenuationFactor = attenuationFactor;
    }

    @Override
    public int light(float[][] points, float[] norm, int color) {
        float[] toTri = Mat.subtract(getCenter(points), pos);
        float amt = Mat.dot(Mat.normalize(toTri), norm);
        amt = amt < 0 ? 0 : amt * intensity * (float) Math.pow(2, -Mat.magnitude(toTri)/attenuationFactor);
        return Mat.multByChannel(color, amt);
    }

    private float[] getCenter(float[][] points) {
        float x = (points[0][0]+points[1][0]+points[2][0])/3;
        float y = (points[0][1]+points[1][1]+points[2][1])/3;
        float z = (points[0][2]+points[1][2]+points[2][2])/3;
        return new float[] {x, y, z, 1};
    }
}
