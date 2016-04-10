import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tara on 4/9/2016.
 */
public class SimpleRenderer extends Renderer {


    private OriginCamera camera;

    @Override
    public List<Triangle> render() {
        // 1. Setup transformation matrix

        // Rotate scene to match camera facing
        // By doing a coordinage rotation
        double faceX=camera.getFacing().getX();
        double faceY=camera.getFacing().getY();
        double faceZ=camera.getFacing().getZ();
        double[][] rotate = {
                // This sets the new X axis to be along the axis of the camera facing
                {faceX, faceY, faceZ, 0},
                // This sets the new Y axis to be perpendicular to that and the original Z axis
                //      (so there isn't any roll)
                {faceY, -1 * faceX, 0, 0},
                // This sets the new Z axis to be the axis perpendicular to both of those
                {faceX * faceZ, faceY * faceZ, -1 *Math.sqrt((faceX*faceX) + (faceY*faceY))},
                {0,0,0,1}};
        // By this transformation, "up" for the camera is -Z and "right" is Y

        // Move scene so the camera is at <0,0,0>
        double[][] translate = {
                {1,0,0,-1*camera.getPosition().getX()},
                {0,1,0,-1*camera.getPosition().getY()},
                {0,0,1,-1*camera.getPosition().getZ()},
                {0,0,0,1}};


        // Create the final transformation
        double[][] transform = matrixCombine(rotate, translate);

        ArrayList<Triangle> out = new ArrayList<>(tris.size());
        // This will be bigger than needed if some tris don't get rendered



        // 2. Transform tris

        for(Triangle3D tri : tris){
            // TODO filter out some tris that don't need rendering (such as facing away from camera)
            double[] vals1, vals2, vals3;
            vals1=matrixMult(transform, tri.getPoint1());
            vals2=matrixMult(transform, tri.getPoint2());
            vals3=matrixMult(transform, tri.getPoint3());
            Point point1, point2, point3;
            point1=new Point((int)(-1*vals1[2]),(int)vals1[1]);
            point2=new Point((int)(-1*vals2[2]),(int)vals2[1]);
            point3=new Point((int)(-1*vals3[2]),(int)vals3[1]);
            out.add(new Triangle(tri.color, point1, point2, point3));
        }

        return out;
    }

    @Override
    public void setCamera(Camera cam) {
        if (cam instanceof OriginCamera){
            camera=(OriginCamera)cam;
        } else {
            camera = new OriginCamera(cam.getPosition());
        }

    }
}
