import base.*;
import shapes.Tetrahedron;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        // Setup the frame
        Frame frame = new Frame();
        // Set size
        frame.setSize(800,600);
        // Set position to middle of screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();
        frame.setLocation(width/2-frame.getWidth()/2,height/2-frame.getHeight()/2);
        // Exit on close
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // [OFF] Remove borders
        frame.setUndecorated(false);
        // Set to be resizeable
        frame.setResizable(true);
        // [OFF] Always on top
        frame.setAlwaysOnTop(false);
        // Set background color
        frame.setBackground(Color.white);
        // Set visible
        frame.setVisible(true);

        BufferedImage buffer = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D image = buffer.createGraphics();
        frame.paint(image);

        /* Old test triangles
        float[][] points = new float[][] {{0,100,-100,1}, {-100,-50,-100,1}, {100,-50,-100,1}}; // column-major order
        //frame.addShape(new TestTriangleShape(points));

        points = new float[][] {{0,200,-1000,1}, {-100,50,-1000,1}, {100,50,-1000,1}};
        //frame.addShape(new TestTriangleShape(points));
        //*/

        Tetrahedron t = new Tetrahedron();
        t.setTransform(new Matrix.Builder()
                .scale(20)
                .translate(0, 0, -100)
                .build());
        frame.addShape(t);

        frame.setCameraPos(new float[]{0,0,30});

        boolean persist = true;
        boolean up=true;

        //float cameraBob = 1.5F; // Camera y change per frame
        //float cameraMax = 15; // Camera y bounds (-max to max)
        while(persist){
            // Do stuff

            //frame.paint(image);

            frame.repaint();
            Thread.sleep(500); // fewer frames works better for some reason???

            // All this animation should probably go into a method in the frame itself
            // Some sort of step() method maybe?

            frame.modifyAngle(15f); // Rotate over time
            /*if(up){
                float[] newCam=frame.getCameraPos();
                newCam[1]+=cameraBob;
                if(newCam[1]>cameraMax)
                    up=false;
            } else {
                float[] newCam=frame.getCameraPos();
                newCam[1]-=cameraBob;
                if(newCam[1]<-cameraMax)
                    up=true;
            }*/


        }
    }

}
