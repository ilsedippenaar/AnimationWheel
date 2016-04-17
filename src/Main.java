import base.*;
import shapes.TestTriangleShape;
import shapes.Tetrahedron;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        // Setup the frame
        TestFrame frame = new TestFrame();
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

        // Random triangle for testing
        Triangle test = new Triangle(Color.black.getRGB(), new Point(400,0), new Point(100,590), new Point(700,590));
        //frame.addTri(test);

        float[][] points = new float[][] {{0,100,-100,1}, {-100,-50,-100,1}, {100,-50,-100,1}}; // column-major order
        //frame.addShape(new TestTriangleShape(points));

        points = new float[][] {{0,200,-1000,1}, {-100,50,-1000,1}, {100,50,-1000,1}};
        //frame.addShape(new TestTriangleShape(points));
        Tetrahedron t = new Tetrahedron();
        t.setTransform(new Matrix.Builder().rotate(15, Matrix.yAxis).scale(50).build());
        frame.addShape(t);

        boolean persist = true;
        while(persist){
            // Do stuff

            //frame.paint(image);
            // TODO: clear so animation is possible
            frame.repaint();
            Thread.sleep(1000); // fewer frames works better for some reason???
        }

    }

}
