import base.Matrix;
import javafx.geometry.Point3D;


/**
 * Created by Tara on 4/9/2016.
 */
public abstract class Camera {
    public float aspectRatio = 1;
    public float FOV = 45;
    public float zNear = 10, zFar = 1000;
    public float rotationX = 0, rotationY = 0;
    //public float[][] matrix = Matrix.makeProjectionMatrix(aspectRatio, zNear, zFar);

    public abstract Point3D getPosition();
    public abstract Point3D getFacing();
    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public void setFOV(float FOV) {
        this.FOV = FOV;
    }

    public void setZNear(float zNear) {
        this.zNear = zNear;
    }

    public void setZFar(float zFar) {
        this.zFar = zFar;
    }

    public void setRotationX(float rotationX) {
        this.rotationX = rotationX;
    }

    public void setRotationY(float rotationY) {
        this.rotationY = rotationY;
    }
}
