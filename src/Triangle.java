import java.awt.*;

/**
 *
 * Created by Tara on 4/6/2016.
 */
public class Triangle extends Polygon {


    private int color;

    public Triangle(int c, Point one, Point two, Point three){
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

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
