package base;

/**
 * Created by Jaco on 4/14/16.
 *
 * Smaller class for triangles
 */
public class Triangle {
    public float[][] points; // in column-major order
    public int color;

    /**
     * @param points The points for the triangle in <b>counterclockwise</b> order and column-major
     * */
    public Triangle(float[][] points, int color) {
        this.points = points;
        this.color = color;
    }
}
