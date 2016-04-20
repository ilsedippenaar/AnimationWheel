package base;

/**
 * Created by Jaco on 4/16/16.
 *
 * Class for building transformation matrices
 */
public class Matrix {
    public static final int xAxis = 0;
    public static final int yAxis = 1;
    public static final int zAxis = 2;
    public static final float DEFAULT_DISTANCE = 100;

    public static float[][] identity() {
        return new float[][] {{1,0,0,0},{0,1,0,0},{0,0,1,0},{0,0,0,1}};
    }
    /**
     * @param transX Translation in the x direction
     * @param transY Translation in the y direction
     * @param transZ Translation in the z direction
     * @return A translation worldView
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
     * @return A rotation worldView
     */
    public static float[][] makeRotationMatrix(float angle, int axis) {
        angle = angle < -90 ? -90 : angle > 90 ? 90 : angle;
        angle = angle * (float) Math.PI/180;
        switch (axis) {
            case xAxis:
                return new float[][] {
                        {1,0,0,0},
                        {0, (float) Math.cos(angle), (float) Math.sin(angle),0},
                        {0, (float) -Math.sin(angle), (float) Math.cos(angle),0},
                        {0,0,0,1}};
            case yAxis:
                return new float[][] {
                        {(float) Math.cos(angle), 0, (float) -Math.sin(angle),0},
                        {0,1,0,0},
                        {(float) Math.sin(angle), 0, (float) Math.cos(angle),0},
                        {0,0,0,1}
                };
            case zAxis:
                return new float[][] {
                        {(float) Math.cos(angle), 0, (float) Math.sin(angle),0},
                        {(float) -Math.sin(angle), 0, (float) Math.cos(angle),0},
                        {0,1,0,0},
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
     * @return A scale worldView
     */
    public static float[][] makeScaleMatrix(float scaleX, float scaleY, float scaleZ) {
        float[][] matrix = identity();
        matrix[0][0] = scaleX;
        matrix[1][1] = scaleY;
        matrix[2][2] = scaleZ;
        return matrix;
    }

    public static class Builder {
        private float[][] translation = identity(),
                rotationX = identity(),
                rotationY = identity(),
                rotationZ = identity(),
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
            float[][] matrix = Mat.matrixMatrixMult(rotationX, scaleMat);
            matrix = Mat.matrixMatrixMult(rotationY, matrix);
            matrix = Mat.matrixMatrixMult(rotationZ, matrix);
            matrix = Mat.matrixMatrixMult(translation, matrix);
            matrix = Mat.matrixMatrixMult(projection, matrix);
            if (points != null) {
                return Mat.matrixPointMult(matrix, points);
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

        /**
         * Prone to gimbal lock
         */
        public Builder rotateX(float angle) {
            rotationX = makeRotationMatrix(angle, xAxis);
            return this;
        }

        public Builder rotateY(float angle) {
            rotationY = makeRotationMatrix(angle, yAxis);
            return this;
        }

        public Builder rotateZ(float angle) {
            rotationZ = makeRotationMatrix(angle, zAxis);
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
    }
}
