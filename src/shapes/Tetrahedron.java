package shapes;

import base.*;

import java.lang.Math;
import java.util.ArrayList;

/**
 * Created by Jaco on 4/16/16.
 *
 * Test class to render 3D objects
 */
public class Tetrahedron extends AbstractShape {

    public Tetrahedron() {
        super(new ArrayList<>(4), 0x880000);
        float[][] p = {
                {1, 0, -1/ (float) Math.sqrt(2),1},
                {-1, 0, -1/ (float) Math.sqrt(2),1},
                {0, 1, 1/ (float) Math.sqrt(2),1},
                {0, -1, 1/ (float) Math.sqrt(2),1}
        };
        p = base.Math.transpose(p);
        // TODO: check for correct order of points for calculating normals
        mesh.add(new Triangle(new float[][]{p[0], p[1], p[2]}, color));
        mesh.add(new Triangle(new float[][]{p[0], p[1], p[3]}, color));
        mesh.add(new Triangle(new float[][]{p[0], p[2], p[3]}, color));
        mesh.add(new Triangle(new float[][]{p[1], p[2], p[3]}, color));
    }
}
