import javafx.geometry.Point3D;

import java.awt.*;

/**
 *
 * Created by Tara on 4/7/2016.
 */
public class Triangle3D {

    public Point3D getPoint1() {
        return point1;
    }

    public Point3D getPoint2() {
        return point2;
    }

    public Point3D getPoint3() {
        return point3;
    }


    public Point3D point1, point2, point3;
    public int color;

    public Triangle3D(int c){color=c;
    }

    public Triangle3D(int c, Point3D p1, Point3D p2, Point3D p3){
        this(c);
        point1=p1;
        point2=p2;
        point3=p3;
    }


}
