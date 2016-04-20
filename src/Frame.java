import base.*;
import base.Mat;
import shapes.AbstractShape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * Created by Tara on 4/6/2016.
 */
public class Frame extends JFrame implements KeyListener {
    Camera camera;
    ArrayList<AbstractShape> shapes = new ArrayList<>();
    Matrix.Builder builder = new Matrix.Builder();
    private float x = 0;
    private float y = 0;
    private float angle = 0;
    private float[] cameraPos = {0,0,1};

    public Frame() {
        super();
        addKeyListener(this);
        camera = new Camera();
    }

    public void setCameraPos(float[] newPos) {
        cameraPos = newPos;
    }

    public float[] getCameraPos() {
        return cameraPos;
    }

    /**
     * Adds to the angle
     * @param delta Amount to add to angle
     */
    public void modifyAngle(float delta) {
        angle = (angle + delta)%360;
        //TODO: remove this temp code
        shapes.get(0).setTransform(
                builder.scale(50)
                .translate(0,0,-30)
                .rotateZ(angle)
                .build()
        );
        System.out.println(angle);
    }

    public void addShape(AbstractShape shape) {shapes.add(shape);}

    // listen for resize events
    @Override
    public void validate() {
        super.validate();
        camera.setAspectRatio(getWidth() / getHeight());
    }

    @Override
    public void paint(Graphics g){
        // Make an image buffer so the frame updates all at once
        BufferedImage buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D imageBuffer = buffer.createGraphics();

        // Fill background on imagebuffer, so it will cover the previous frame
        imageBuffer.setColor(Color.white);
        imageBuffer.fillRect(0,0,getWidth(),getHeight());

        float[] facing= Mat.normalize(Mat.mult(cameraPos, -1));

        for (AbstractShape s : shapes) {
            for (base.Triangle t : s.mesh) {
                // transform the points with world transformMatrix
                /*float[][] points = builder.setPoints(Mat.matrixPointMult(s.transform, t.points))
                        .translate(x, y, 0)
                        .project(cameraPos, Mat.normalize(Mat.mult(cameraPos, -1)))
                        .rotate(angle, Matrix.yAxis)
                        .build();*/
                float[][] points = camera.look(Mat.matrixPointMult(s.transform, t.points));

                // Cull tris facing away (which solves depth-checking for simple shapes)
                float[] edge1= Mat.subtract(points[0], points[1]);
                float[] edge2= Mat.subtract(points[0], points[2]);
                float[] norm = Mat.cross(edge1, edge2);

                if(Mat.dot(norm, facing)<0) {
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
            case KeyEvent.VK_W:
                y += 50;
                camera.moveZ(50);
                break;
            case KeyEvent.VK_S:
                y -= 50;
                camera.moveZ(-50);
                break;
            case KeyEvent.VK_D:
                x += 50;
                camera.moveY(50);
                break;
            case KeyEvent.VK_A:
                x -= 50;
                camera.moveY(-50);
                break;
            case KeyEvent.VK_R:
                angle += 20;
                camera.rotateXBy(20);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


    /**
     * @param points Points of a triangle ready to be painted
     * @return A transformMatrix with two rows, one for x values, the other for y values
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
