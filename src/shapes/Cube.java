package shapes;

import base.Triangle;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Tara on 4/22/2016.
 */
public class Cube extends AbstractShape {


    public Cube() {
        super(new ArrayList<>(12), 0x880000);
        float[] upRightFront = {1,1,1,1},
                upRightBack = {1,1,-1,1},
                upLeftFront = {1,-1,1,1},
                upLeftBack = {1,-1,-1,1},
                downRightFront = {-1,1,1,1},
                downRightBack = {-1,1,-1,1},
                downLeftFront = {-1,-1,1,1},
                downLeftBack = {-1,-1,-1,1};

        //Right
        mesh.add(new Triangle(new float[][]{upRightFront, downRightFront, downRightBack}, Color.RED.getRGB()));
        mesh.add(new Triangle(new float[][]{upRightFront, downRightBack, upRightBack}, Color.RED.getRGB()));
        //Up
        mesh.add(new Triangle(new float[][]{upLeftFront, upRightFront, upRightBack}, Color.GREEN.getRGB()));
        mesh.add(new Triangle(new float[][]{upLeftFront, upRightBack, upLeftBack}, Color.GREEN.getRGB()));
        //Front
        mesh.add(new Triangle(new float[][]{upLeftFront, downLeftFront, upRightFront}, Color.BLUE.getRGB()));
        mesh.add(new Triangle(new float[][]{downLeftFront, downRightFront, upRightFront}, Color.BLUE.getRGB()));
        //Left
        mesh.add(new Triangle(new float[][]{upLeftFront, upLeftBack, downLeftBack}, Color.PINK.getRGB()));
        mesh.add(new Triangle(new float[][]{downLeftBack, downLeftFront, upLeftFront}, Color.PINK.getRGB()));
        //Down
        mesh.add(new Triangle(new float[][]{downLeftFront, downLeftBack, downRightBack}, Color.ORANGE.getRGB()));
        mesh.add(new Triangle(new float[][]{downRightBack, downRightFront, downLeftFront}, Color.ORANGE.getRGB()));
        //Back
        mesh.add(new Triangle(new float[][]{upLeftBack, upRightBack, downRightBack}, Color.CYAN.getRGB()));
        mesh.add(new Triangle(new float[][]{downRightBack, downLeftBack, upLeftBack}, Color.CYAN.getRGB()));
    }
}
