import base.*;
import shapes.AbstractShape;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * Created by Tara on 4/6/2016.
 */
public class TestFrame extends JFrame {

    ArrayList<Triangle> tris=new ArrayList<>();
    ArrayList<AbstractShape> shapes = new ArrayList<>();

    public TestFrame(){
        super();
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
                float[][] QTranspose = {{1,0,0,0}, base.Math.normalize(new float[] {0,1,-0.5f,0})};
                float[][] points = base.Math.matrixPointMult(QTranspose, t.points);
                int[] xPoints = {(int) points[0][0], (int) points[0][1], (int) points[0][2]};
                int[] yPoints = {(int) points[1][0], (int) points[1][1], (int) points[1][2]};
                //int[] xPoints = {(int) points[0][0], (int) points[1][0], (int) points[2][0]};
                //int[] yPoints = {(int) points[0][1], (int) points[1][1], (int) points[2][1]};
                imageBuffer.setColor(new Color(t.color));
                imageBuffer.fillPolygon(xPoints, yPoints, 3);
            }
        }

        // Draw the buffered frame
        Graphics2D g2D = (Graphics2D) g;
        g2D.drawImage(buffer, null, 0,0);


    }
}
