import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        // Setup the frame
        TestFrame frame = new TestFrame();
        // Set size
        frame.setSize(800,600);
        // Set position (to the middle of a 1080p screen)
        frame.setLocation(960-frame.getWidth()/2,540-frame.getHeight()/2);
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
        Triangle test = new Triangle(Color.black.getRGB(), new Point(100,120), new Point(150,120), new Point(110,180));
        frame.addTri(test);

        boolean persist = true;
        while(persist){
            // Do stuff

            frame.paint(image);
            frame.repaint();
            Thread.sleep(15);
        }

    }

}
