import base.*;
import base.Mat;
import lights.AbstractLight;

import java.util.ArrayList;

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
        faceTowards(new float[]{0, 0, 0, 0});
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

    public void moveRight(float dist) {
        float[] right = Mat.normalize(Mat.cross(facing, up));
        pos = Mat.add(pos, Mat.mult(right, dist));
        isWorldViewDirty = true;
    }

    public void moveUp(float dist) {
        pos = Mat.add(pos, Mat.mult(up, dist));
        isWorldViewDirty = true;
    }

    public void moveForward(float dist) {
        pos = Mat.add(pos, Mat.mult(facing, dist));
        isWorldViewDirty = true;
    }

    public PointsAndColor look(PointsAndColor pointsAndColor, ArrayList<AbstractLight> lights, boolean emissive) {
        if (isWorldViewDirty) {
            worldView = makeWorldViewMatrix();
            isWorldViewDirty = false;
        }
        float[][] points = Mat.matrixPointMult(worldView, pointsAndColor.points); // puts into world view space
        pointsAndColor.color = light(pointsAndColor, lights, emissive);
        points = Mat.matrixPointMult(projectMatrix, points); // puts in clip space
        points = perspectiveDivide(points); // puts into NDC
        // TODO: better clipping algorithm
        pointsAndColor.points = clip(points);
        return pointsAndColor;
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

    private int light(PointsAndColor pointsAndColor, ArrayList<AbstractLight> lights, boolean emissive) {
        int color = pointsAndColor.color;
        float[][] points = pointsAndColor.points;
        float[] norm = Mat.normalize(Mat.cross(Mat.subtract(points[1], points[0]), Mat.subtract(points[2], points[0])));
        norm = emissive ? Mat.mult(norm, -1) : norm;
        int[] colors = new int[lights.size()];
        for (int i = 0; i < lights.size(); i++) {
            colors[i] = lights.get(i).light(points, norm, color);
        }
        return Mat.addColorsWithThreshold(colors);
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

    private float[][] clip(float[][] points) {
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < 3; j++) {
                if (Math.abs(points[i][j]) >= 1) {
                    return null;
                }
            }
        }
        return points;
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

    // Because Java is silly, we need a holder to return a float[][] and an int for the triangle's color
    public static class PointsAndColor {
        public float[][] points;
        public int color;

        PointsAndColor(float[][] points, int color) {
            this.points = points;
            this.color = color;
        }
    }
}
