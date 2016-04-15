package base;

/**
 * Created by Jaco on 4/14/16.
 *
 * Smaller class for triangles
 */
public class Triangle {
    float[][] points;
    int color;
    float[] norm;

    /**
     * @param points The points for the triangle in <b>counterclockwise</b> order
     * */
    public Triangle(float[][] points, int color) {
        this.points = points;
        this.color = color;
        norm = Math.normalize(Math.cross(Math.subtract(points[1],points[0]), Math.subtract(points[2], points[0])));
    }
}