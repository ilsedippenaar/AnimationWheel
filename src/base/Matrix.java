package base;

/**
 * Created by Jaco on 4/16/16.
 *
 * Class for building transformation matrices
 */
public class Matrix {
    public static final int xAxis = 0;
    public static final int yAxis = 1;
    public static final float DEFAULT_DISTANCE = 100;

    public static float[][] identity() {
        return new float[][] {{1,0,0,0},{0,1,0,0},{0,0,1,0},{0,0,0,1}};
    }
    /**
     * @param transX Translation in the x direction
     * @param transY Translation in the y direction
     * @param transZ Translation in the z direction
     * @return A translation transformMatrix
     */
    public static float[][] makeTranslationMatrix(float transX, float transY, float transZ) {
        float[][] matrix = identity();
        matrix[0][3] = transX;
        matrix[1][3] = transY;
        matrix[2][3] = transZ;
        return matrix;
    }

    /**
     * @param angle The angle to rotate around in degrees
     * @param axis Either {@link #xAxis Matrix.xAxis} or {@link #yAxis Matrix.yAxis}
     * @return A rotation transformMatrix
     */
    public static float[][] makeRotationMatrix(float angle, int axis) {
        angle = angle < -90 ? -90 : angle > 90 ? 90 : angle;
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
     * @return A scale transformMatrix
     */
    public static float[][] makeScaleMatrix(float scaleX, float scaleY, float scaleZ) {
        float[][] matrix = identity();
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
     * @return A projection transformMatrix
     */
    @Deprecated
    public static float[][] makeProjectionMatrixOld(float[] eye, float[] up, float[] at, float distance) {
        float[] n = Math.normalize(Math.subtract(at, eye));
        float[] y = Math.normalize(Math.subtract(up, Math.project(n, up)));
        float[] x = Math.normalize(Math.cross(n, y));
        return new float[][] {
                {x[0],x[1],x[2], -Math.dot(x, eye)},
                {y[0],y[1],y[2], -Math.dot(y, eye)},
                {n[0]/distance, n[1]/distance, n[2]/distance, -Math.dot(n, eye)/distance},
                {0,0,0,0}
        };
    }

    public static class Builder {
        private float[][] translation = identity(),
                rotation = identity(),
                scaleMat = identity(),
                projection = identity();
        private float[][] points;

        public Builder() {}

        public Builder(float[][] points) {
            this.points = points;
        }

        public float[][] build() {
            // TODO: make more efficient
            // order: Translation * Rotation * Scale
            float[][] matrix = Math.matrixMatrixMult(rotation, scaleMat);
            matrix = Math.matrixMatrixMult(translation, matrix);
            matrix = Math.matrixMatrixMult(projection, matrix);
            if (points != null) {
                return Math.matrixPointMult(matrix, points);
            }
            return matrix;
        }

        public Builder translate(float[] translate) {
            translation = makeTranslationMatrix(translate[0], translate[1], translate[2]);
            return this;
        }

        public Builder translate(float x, float y, float z) {
            translation = makeTranslationMatrix(x, y, z);
            return this;
        }

        public Builder rotate(float angle, int axis) {
            rotation = makeRotationMatrix(angle, axis);
            return this;
        }

        public Builder scale(float[] scale) {
            scaleMat = makeScaleMatrix(scale[0], scale[1], scale[2]);
            return this;
        }

        public Builder scale(float x, float y, float z) {
            scaleMat = makeScaleMatrix(x, y, z);
            return this;
        }

        public Builder scale(float scale) {
            scaleMat = makeScaleMatrix(scale, scale, scale);
            return this;
        }

        public Builder setPoints(float[][] points) {
            this.points = points;
            return this;
        }

        /**
         * Uses default values for up (0,1,0) and distance (100)
         */
        public Builder project(float[] eye, float[] at) {
            projection = makeProjectionMatrixOld(eye, new float[]{0, 1, 0}, at, DEFAULT_DISTANCE);
            return this;
        }
    }
}
