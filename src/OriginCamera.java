import javafx.geometry.Point3D;

/**
 * A test camera that always faces the origin
 * Created by Tara on 4/9/2016.
 */
public class OriginCamera extends Camera {

    private Point3D position, facing;

    public OriginCamera(Point3D initialPos){
        setPos(initialPos);
    }

    public void setPos(Point3D pos){
        position = pos;
        facing = position.normalize();
        facing = facing.multiply(-1);
    }



    @Override
    public Point3D getPosition() {
        return position;
    }

    @Override
    public Point3D getFacing() {
        return facing;
    }
}
