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

    /**
     * Adds to the angle
     * @param delta Amount to add to angle
     */
    public void modifyAngle(float delta) {
        angle += delta;
    }

    private float angle = 0;

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

        // Fill background on imagebuffer, so it will cover the previous frame
        imageBuffer.setColor(Color.white);
        imageBuffer.fillRect(0,0,getWidth(),getHeight());

        // Draw all tris
        for(Triangle t:tris){
            imageBuffer.setColor(new Color(t.getColor()));
            imageBuffer.fill(t);
        }

        float[] facing={}; // TODO set to be a vector of the camera facing

        for (AbstractShape s : shapes) {
            for (base.Triangle t : s.mesh) {
                // TODO: optimize all the things
                // transform the points with world matrix
                float[][] points = new Matrix.Builder(Math.matrixPointMult(s.transform, t.points))
                        .translate(x, y, 0)
                        .project(new float[]{0, 0, 100}, new float[]{0, 0, -1})
                        .rotate(angle, Matrix.yAxis)
                        .build();
                // project the points onto view frame

                // Cull tris facing away (which solves depth-checking for simple shapes)
                float[] edge1=base.Math.subtract(points[0], points[1]);
                float[] edge2=base.Math.subtract(points[0], points[2]);
                float[] norm = base.Math.cross(edge1, edge2);

                if(base.Math.dot(norm, facing)<0) {
                    int[][] coords = coordinatesToJFrame(points);
                    imageBuffer.setColor(new Color(t.color));
                    imageBuffer.fillPolygon(coords[0], coords[1], 3);
                }
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
                y += 50;
                break;
            case KeyEvent.VK_S: //backward
                y -= 50;
                //Q = new Matrix.Builder(Q).rotate(-20, Matrix.xAxis).build();
                break;
            case KeyEvent.VK_D:
                x += 50;
                break;
            case KeyEvent.VK_A:
                x -= 50;
                break;
            case KeyEvent.VK_R:
                angle += 20;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


    /**
     * @param points Points of a triangle ready to be painted
     * @return A matrix with two rows, one for x values, the other for y values
     */
    private int[][] coordinatesToJFrame(float [][] points) {
        int midHeight = getHeight()/2;
        int midWidth = getWidth()/2;
        int[][] newPoints = new int[2][points.length];
        for (int i = 0; i < points.length; i++) {
            newPoints[0][i] = (int) points[i][0] + midWidth; // move and set x
            newPoints[1][i] = midHeight - (int) points[i][1]; // move and set y
        }
        return newPoints;
    }
}
