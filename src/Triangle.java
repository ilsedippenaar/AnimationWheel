import java.awt.*;

/**
 *
 * Created by Tara on 4/6/2016.
 */
public class Triangle extends Polygon{


    private Color color;

    public Triangle(Color c, Point one, Point two, Point three){
        color = c;
        addPoint(one);
        addPoint(two);
        addPoint(three);
    }


    public void setPoints(Point one, Point two, Point three){
        reset();
        addPoint(one);
        addPoint(two);
        addPoint(three);
    }

    private void addPoint(Point p){
        addPoint(p.x, p.y);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
