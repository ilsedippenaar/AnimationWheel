import base.*;
import base.Mat;

public class Camera {
    public float aspectRatio = 1; // x/y
    public float FOV = 45; // from 0 to 90
    public float zNear = 10, zFar = 1000;
    public float rotationX = 0, rotationY = 0;
    public float[] pos = {0,0,0};
    public float[][] transformMatrix = Matrix.identity();
    public float[][] projectMatrix = makeProjectionMatrix();
    private boolean isTransformDirty = true;

    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
        makeProjectionMatrix();
    }

    public void setFOV(float FOV) {
        this.FOV = FOV;
        makeProjectionMatrix();
    }

    public void setZNear(float zNear) {
        this.zNear = zNear;
        makeProjectionMatrix();
    }

    public void setZFar(float zFar) {
        this.zFar = zFar;
        makeProjectionMatrix();
    }

    public void setRotationX(float rotationX) {
        this.rotationX = rotationX;
        isTransformDirty = true;
    }

    public void setRotationY(float rotationY) {
        this.rotationY = rotationY;
        isTransformDirty = true;
    }

    public void rotateXBy(float dx) {
        rotationX += dx;
        isTransformDirty = true;
    }

    public void rotateYBy(float dy) {
        rotationX += dy;
        isTransformDirty = true;
    }

    public void setPos(float[] pos) {
        this.pos = pos;
        isTransformDirty = true;
    }

    public float[][] look(float[][] points) {
        if (isTransformDirty) {
            makeTransformationMatrix();
            isTransformDirty = false;
        }
        float[][] transformPoints = Mat.matrixPointMult(transformMatrix, points);
        // send points to renderer for mapping to NDC, culling, and clipping
        return Mat.matrixPointMult(projectMatrix, transformPoints);
    }

    public float[][] look(Triangle t) {
        return null;
    }

    public void moveX(float dx) {
        pos[0] += dx;
        isTransformDirty = true;
    }

    public void moveY(float dy) {
        pos[1] += dy;
        isTransformDirty = true;
    }

    public void moveZ(float dz) {
        pos[2] += dz;
        isTransformDirty = true;
    }

    /**
     * We want to do the transforms in reverse order, so
     * (TRS)^-1=(S^-1)(R^-1)(T^-1), but no scaling
     */
    private void makeTransformationMatrix() {
        transformMatrix = Mat.matrixMatrixMult(
                makeCompleteInverseRotationMatrix(),
                makeInverseTranslation()
        );
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
                {0,0,-1,0}};
    }

    private float[][] makeCompleteInverseRotationMatrix() {
        float[][] xRot = new float[][] {
                {1,0,0,0},
                {0, (float) Math.cos(rotationX), (float) -Math.sin(rotationX),0},
                {0, (float) Math.sin(rotationX), (float) Math.cos(rotationX),0},
                {0,0,0,1}
        };
        float [][] yRot = new float[][] {
                {(float) Math.cos(rotationY), 0, (float) Math.sin(rotationY),0},
                {0,1,0,0},
                {(float) -Math.sin(rotationY), 0, (float) Math.cos(rotationY),0},
                {0,0,0,1}
        };
        return Mat.matrixMatrixMult(xRot, yRot);
    }

    private float[][] makeInverseTranslation() {
        float[][] trans = Matrix.identity();
        trans[0][3] = -pos[0];
        trans[1][3] = -pos[1];
        trans[2][3] = -pos[2];
        return trans;
    }
}
