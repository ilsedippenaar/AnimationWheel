import base.*;
import base.Math;
import shapes.AbstractShape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * Created by Tara on 4/6/2016.
 */
public class TestFrame extends JFrame implements KeyListener {

    ArrayList<Triangle> tris=new ArrayList<>();
    ArrayList<AbstractShape> shapes = new ArrayList<>();
    float[][] Q ={
            {1,0,0,0},
            {0,1,0,0},
            {0,0,0,0},
            {0,0,0,0}
    };
    private float x = 0;
    private float y = 0;

    public TestFrame(){
        super();
        addKeyListener(this);
    }

    public void addTri(Triangle t){
        tris.add(t);
    }

    public void addShape(AbstractShape shape) {shapes.add(shape);}

    public void addTris(Collection<Triangle> ts){
        tris.addAll(ts);
    }

    public void setTris(ArrayList<Triangle> newTris){
        tris=newTris;
    }

    @Override
    public void paint(Graphics g){
        // Not sure if this is needed/wanted
        //super.paint(g);

        // Make an image buffer so the frame updates all at once
        BufferedImage buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D imageBuffer = buffer.createGraphics();

        // Draw all tris
        for(Triangle t:tris){
            imageBuffer.setColor(new Color(t.getColor()));
            imageBuffer.fill(t);
        }

        for (AbstractShape s : shapes) {
            for (base.Triangle t : s.mesh) {
                // transform the points
                float[][] points = new Matrix.Builder(t.points).translate(x, y, 0).build();
                System.out.println(points[0][0]);
                // project
                points = base.Math.matrixPointMult(Math.transpose(Q), points);
                int[] xPoints = {(int) points[0][0], (int) points[0][1], (int) points[0][2]};
                int[] yPoints = {(int) points[1][0], (int) points[1][1], (int) points[1][2]};
                imageBuffer.setColor(new Color(t.color));
                imageBuffer.fillPolygon(xPoints, yPoints, 3);
            }
        }

        // Draw the buffered frame
        Graphics2D g2D = (Graphics2D) g;
        g2D.drawImage(buffer, null, 0,0);


    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getExtendedKeyCode()) {
            case KeyEvent.VK_W: // forward
                //Q = new Matrix.Builder(Q).rotate(20, Matrix.xAxis).build();
                x += 50;
                break;
            case KeyEvent.VK_S: //backward
                x -= 50;
                //Q = new Matrix.Builder(Q).rotate(-20, Matrix.xAxis).build();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
