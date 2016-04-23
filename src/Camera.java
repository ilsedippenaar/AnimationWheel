import base.*;
import base.Mat;

/**
 * 1. Translate so that camera is at origin
 * 2. Transform to world view
 * 3. Cull
 * 4. Transform to NDC
 * 5. Clip
 * 6. Project
 */

public class Camera {
    // for perspective projection
    private float aspectRatio = 1; // x/y
    private float FOV = 45; // from 0 to 90
    private float zNear = 10, zFar = 1000;
    private float[][] projectMatrix = makeProjectionMatrix();


    // for world view transformation
    public float[] pos = {0,0,0,1};
    public float[] facing = {0,0,-1,0};
    private float[] up = {0,1,0,0};
    private float[][] worldView;
    private boolean isWorldViewDirty = true;


    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
        projectMatrix = makeProjectionMatrix();
    }

    public void setFOV(float FOV) {
        this.FOV = FOV;
        projectMatrix = makeProjectionMatrix();
    }

    public void faceTowards(float[] target){
        float[] diff=Mat.subtract(target, pos);
        //If target = pos
        if(Mat.magnitude(diff)==0)
            return;
        facing=Mat.normalize(diff);
        facing[3]=0;
        isWorldViewDirty=true;
    }

    public void faceTowardsOrigin(){
        faceTowards(new float[]{0,0,0,0});
    }

    public void setZNear(float zNear) {
        this.zNear = zNear;
        projectMatrix = makeProjectionMatrix();
    }

    public void setZFar(float zFar) {
        this.zFar = zFar;
        projectMatrix = makeProjectionMatrix();
    }

    public void setRotationX(float rotationX) {
        facing = Mat.matrixVecMult(
                Matrix.makeRotationMatrix(rotationX, Matrix.xAxis),
                new float[] {0,0,-1,0});
        isWorldViewDirty = true;
    }

    public void setRotationY(float rotationY) {
        facing = Mat.matrixVecMult(
                Matrix.makeRotationMatrix(rotationY, Matrix.yAxis),
                new float[] {0,0,-1,0});
        isWorldViewDirty = true;
    }

    public void rotateXBy(float dx) {
        facing = Mat.matrixVecMult(
                Matrix.makeRotationMatrix(dx, Matrix.xAxis),
                facing);
        isWorldViewDirty = true;
    }

    public void rotateYBy(float dy) {
        facing = Mat.matrixVecMult(
                Matrix.makeRotationMatrix(dy, Matrix.yAxis),
                facing);
        isWorldViewDirty = true;
    }

    public void setPos(float[] pos) {
        this.pos = pos;
        isWorldViewDirty = true;
    }

    public void setUpVec(float[] up) {
        this.up = up;
        isWorldViewDirty = true;
    }

    public void moveX(float dx) {
        pos[0] += dx;
        isWorldViewDirty = true;
    }

    public void moveY(float dy) {
        pos[1] += dy;
        isWorldViewDirty = true;
    }

    public void moveZ(float dz) {
        pos[2] += dz;
        isWorldViewDirty = true;
    }

    public float[][] look(float[][] points) {
        if (isWorldViewDirty) {
            worldView = makeWorldViewMatrix();
            isWorldViewDirty = false;
        }
        float[][] worldViewPoints = Mat.matrixPointMult(worldView, points); // puts into world view space
        worldViewPoints = Mat.matrixPointMult(projectMatrix, worldViewPoints); // puts in clip space
        worldViewPoints = perspectiveDivide(worldViewPoints); // puts into NDC
        //TODO: cull and clip anything outside [-1,1],[-1,1],[-1,1]
        return worldViewPoints;
    }

    /**
     * Translates points so that the camera is at the origin, then projects onto camera's axes
     *
     * @return A world view matrix
     */
    public float[][] makeWorldViewMatrix() {
        float[] n = Mat.normalize(facing);
        float[] x = Mat.normalize(Mat.cross(n, up));
        float[] y = Mat.normalize(Mat.cross(x, n));
        return new float[][] {
                {x[0],x[1],x[2], -Mat.dot(x, pos)},
                {y[0],y[1],y[2], -Mat.dot(y, pos)},
                {n[0], n[1], n[2], -Mat.dot(n, pos)},
                {0,0,0,1}
        };
    }

    /**
     * Maps points in the view frustum to normalized device coordinates so  that
     * x, y, and z are in the range (-1, 1). Based mostly on
     * <a href="http://www.ogldev.org/www/tutorial12/tutorial12.html"/>this site</a>.
     *
     * @return A perspective projection matrix
     */
    private float[][] makeProjectionMatrix() {
        return new float[][] {{1/(float) (aspectRatio*Math.tan(FOV)),0,0,0},
                {0,1/(float) Math.tan(FOV), 0,0},
                {0,0,(-zNear-zFar)/(zNear-zFar),2*zNear*zFar/(zNear-zFar)},
                {0,0,1,0}}; // save the z value for depth-checking and perspective divide
    }

    /**
     * Puts the vector into NDC coordinates by performing a perspective divide
     * which maps the z coordinate to [-1,1]. This should follow multiplication by {@link #makeWorldViewMatrix()}
     */
    private float[][] perspectiveDivide(float[][] worldViewPoints) {
        for (int i = 0; i < worldViewPoints.length; i++) {
            worldViewPoints[i] = Mat.mult(worldViewPoints[i], 1/worldViewPoints[i][3]);
        }
        return worldViewPoints;
    }
}
