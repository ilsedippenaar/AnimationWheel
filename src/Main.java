import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Main {

    public static void main(String[] args) {
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
        // Set visible
        frame.setVisible(true);

        BufferedImage buffer = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D image = buffer.createGraphics();
        frame.paint(image);

        boolean persist = true;

        while(persist){
            // Do stuff

            frame.paint(image);
            frame.repaint();
        }

    }

}
