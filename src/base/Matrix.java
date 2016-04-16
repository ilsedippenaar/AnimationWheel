package base;

import com.sun.istack.internal.Nullable;

import java.lang.*;

/**
 * Created by Jaco on 4/16/16.
 *
 * Class for building transformation matrices
 */
public class Matrix {
    public static final float[][] I = {{1,0,0,0},{0,1,0,0},{0,0,1,0},{0,0,0,1}};
    public static final int xAxis = 0;
    public static final int yAxis = 1;
    public static final float DEFAULT_DISTANCE = 100;

    /**
     * @param transX Translation in the x direction
     * @param transY Translation in the y direction
     * @param transZ Translation in the z direction
     * @return A translation matrix
     */
    public static float[][] makeTranslationMatrix(float transX, float transY, float transZ) {
        float[][] matrix = I;
        matrix[0][3] = transX;
        matrix[1][3] = transY;
        matrix[2][3] = transZ;
        return matrix;
    }

    /**
     * @param angle The angle to rotate around in degrees
     * @param axis Either {@link #xAxis Matrix.xAxis} or {@link #yAxis Matrix.yAxis}
     * @return A rotation matrix
     */
    public static float[][] makeRotationMatrix(float angle, int axis) {
        angle = angle * (float) java.lang.Math.PI/180;
        switch (axis) {
            case xAxis:
                return new float[][] {
                        {1,0,0,0},
                        {0, (float) java.lang.Math.cos(angle), (float) java.lang.Math.sin(angle),0},
                        {0, (float) -java.lang.Math.sin(angle), (float) java.lang.Math.cos(angle),0},
                        {0,0,0,1}};
            case yAxis:
                return new float[][] {
                        {(float) java.lang.Math.cos(angle), 0, (float) -java.lang.Math.sin(angle),0},
                        {0,1,0,0},
                        {(float) java.lang.Math.sin(angle), 0, (float) java.lang.Math.cos(angle),0},
                        {0,0,0,1}
                };
            default:
                return null;
        }
    }

    /**
     * @param scaleX Multiple for x direction (1 for no effect)
     * @param scaleY Multiple for y direction (1 for no effect)
     * @param scaleZ Multiple for z direction (1 for no effect)
     * @return A scale matrix
     */
    public static float[][] makeScaleMatrix(float scaleX, float scaleY, float scaleZ) {
        float[][] matrix = I;
        matrix[0][0] = scaleX;
        matrix[1][1] = scaleY;
        matrix[2][2] = scaleZ;
        return matrix;
    }


    /**
     * ** Still needs work this does**
     * @param eye The camera position
     * @param up The up vector (should be {0,1,0,0} for no camera roll)
     * @param at The direction the eye should look at
     * @param distance The distance from the camera to the viewport (should be {@link #DEFAULT_DISTANCE})
     * @return A projection matrix
     */
    public static float[][] makeProjectionMatrix(float[] eye, float[] up, float[] at, float distance) {
        float[] n = Math.normalize(at);
        float[] y = Math.normalize(Math.subtract(up, Math.project(n, up)));
        float[] x = Math.normalize(Math.cross(n, y));
        return new float[][] {
                {x[0],x[1],x[2], -Math.dot(x, eye)},
                {y[0],y[1],y[2], -Math.dot(y, eye)},
                {0,0,0,0},
                {n[0]/distance, n[1]/distance, n[2]/distance, -Math.dot(n, eye)/distance}
        };
    }

    public static class Builder {
        private float[][] matrix = I;
        private float[][] points;

        public Builder() {
        }

        public Builder(float[][] points) {
            this.points = points;
        }

        public float[][] build() {
            if (points != null) {
                return Math.matrixPointMult(matrix, points);
            }
            return matrix;
        }

        public Builder translate(float[] translate) {
            matrix = Math.matrixMatrixMult(matrix,
                    makeTranslationMatrix(translate[0], translate[1], translate[2]));
            return this;
        }

        public Builder translate(float x, float y, float z) {
            matrix = Math.matrixMatrixMult(matrix, makeTranslationMatrix(x, y, z));
            return this;
        }

        public Builder rotate(float angle, int axis) {
            matrix = Math.matrixMatrixMult(matrix, makeRotationMatrix(angle, axis));
            return this;
        }

        public Builder scale(float[] scale) {
            matrix = Math.matrixMatrixMult(matrix,
                    makeTranslationMatrix(scale[0], scale[1], scale[2]));
            return this;
        }

        public Builder scale(float x, float y, float z) {
            matrix = Math.matrixMatrixMult(matrix, makeScaleMatrix(x, y, z));
            return this;
        }

        public Builder project(float[] eye, float[] up, float[] at) {
            matrix = Math.matrixMatrixMult(matrix, makeProjectionMatrix(eye, up, at, DEFAULT_DISTANCE));
            return this;
        }
    }
}
