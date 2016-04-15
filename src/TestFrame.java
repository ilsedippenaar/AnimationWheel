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

    public TestFrame(){
        super();
    }

    public void addTri(Triangle t){
        tris.add(t);
    }

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

        // Draw the buffered frame
        Graphics2D g2D = (Graphics2D) g;
        g2D.drawImage(buffer, null, 0,0);


    }
}
