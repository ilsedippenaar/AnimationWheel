package shapes;

import base.Triangle;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A cube, but with a tunnel in from every face
 * Created by Tara on 5/3/2016.
 */
public class HollowCube extends AbstractShape {
    public HollowCube() {
        super(new ArrayList<>(96), 0x880000);
        float[] upRightFront = {1,1,1,1},
                upRightBack = {1,1,-1,1},
                upLeftFront = {1,-1,1,1},
                upLeftBack = {1,-1,-1,1},
                downRightFront = {-1,1,1,1},
                downRightBack = {-1,1,-1,1},
                downLeftFront = {-1,-1,1,1},
                downLeftBack = {-1,-1,-1,1};

        //Right
        addSurface(new float[][]{upRightFront, downRightFront, downRightBack,{}}, Color.RED.getRGB());
        //Up
        addSurface(new float[][]{upLeftFront, upRightFront, upRightBack,{}}, Color.GREEN.getRGB());
        //Front
        addSurface(new float[][]{upLeftFront, downLeftFront, downRightFront,{}}, Color.BLUE.getRGB());
        //Left
        addSurface(new float[][]{upLeftFront, upLeftBack, downLeftBack,{}}, Color.PINK.getRGB());
        //Down
        addSurface(new float[][]{downLeftFront, downLeftBack, downRightBack,{}}, Color.ORANGE.getRGB());
        //Back
        addSurface(new float[][]{upLeftBack, upRightBack, downRightBack,{}}, Color.CYAN.getRGB());
    }
    
    // Points go in clockwise order
    private void addSurface(float[][] points, int color){


        //Determine the index of the axis on which the face protrudes (x,y,z)
        int coord=0;
        if(points[0][coord]!=points[1][coord] || points[0][coord]!=points[2][coord])
            coord++;
        if(points[0][coord]!=points[1][coord] || points[0][coord]!=points[2][coord])
            coord++;

        //Copy and rotate 2nd point to create the fourth of the face
        points[3] = Arrays.copyOf(points[1],4);
        for(int i=0; i<3; i++){
            if (i!=coord){
                points[3][i]*=-1;
            }
        }

        for(int i=0; i<4; i++){
            // index of the next point clockwise from the current point
            int next= (i+1)%4;

            float[] currentMid= Arrays.copyOf(points[i], 4),
                    nextMid=Arrays.copyOf(points[next], 4),
                    currentOuter= Arrays.copyOf(points[i], 4),
                    nextOuter=Arrays.copyOf(points[next], 4);

            currentMid[coord]*=3;
            nextMid[coord]*=3;

            currentOuter[0]*=3;
            nextOuter[0]*=3;
            currentOuter[1]*=3;
            nextOuter[1]*=3;
            currentOuter[2]*=3;
            nextOuter[2]*=3;


            //Outer face
            mesh.add(new Triangle(new float[][]{currentOuter, nextOuter, currentMid}, color));
            mesh.add(new Triangle(new float[][]{currentMid, nextOuter, nextMid}, color));
            //Tunnel
            mesh.add(new Triangle(new float[][]{currentMid, nextMid, points[i]}, color));
            mesh.add(new Triangle(new float[][]{points[i], nextMid, points[next]}, color));

        }
    }


    
    
}
