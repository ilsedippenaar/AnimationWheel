package shapes;

import base.Triangle;

import java.util.ArrayList;

/**
 * Created by Jaco on 4/15/16
 */
public class TestTriangleShape extends AbstractShape {

    public TestTriangleShape(float[][] points) {
        super(new ArrayList<>(1), 0x660000);
        mesh.add(new Triangle(points, color));
    }
}
