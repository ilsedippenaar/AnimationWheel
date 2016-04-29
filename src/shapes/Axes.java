package shapes;

import base.Triangle;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author Jaco
 */
public class Axes extends AbstractShape {
    public Axes() {
        super(new ArrayList<>(3), 0x0);
        float[] x1 = {0,0,-0.5f,1},
                x2 = {0,0,0.5f,1},
                x3 = {50,0,0,1},
                y1 = {-0.5f,0,0,1},
                y2 = {0.5f,0,0,1},
                y3 = {0,50,0,1},
                z1 = {-0.5f,0,0,1},
                z2 = {0.5f,0,0,1},
                z3 = {0,0,50,1};
        mesh.add(new Triangle(new float[][] {x1, x2, x3}, Color.RED.getRGB()));
        mesh.add(new Triangle(new float[][] {x2, x1, x3}, Color.RED.getRGB()));
        mesh.add(new Triangle(new float[][] {y1, y2, y3}, Color.GREEN.getRGB()));
        mesh.add(new Triangle(new float[][] {y2, y1, y3}, Color.GREEN.getRGB()));
        mesh.add(new Triangle(new float[][] {z1, z2, z3}, Color.BLUE.getRGB()));
        mesh.add(new Triangle(new float[][] {z2, z1, z3}, Color.BLUE.getRGB()));
    }
}
