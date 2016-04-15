package base;

/**
 * Created by Jaco on 4/14/16
 */

public class Math {

    /**
     * @param v1 The first vector
     * @param v2 The second vector
     * @return The dot product
     */
    public static float dot(float[] v1, float[] v2) {
        return v1[0]*v2[0] + v1[1]*v2[1] + v1[2]*v2[2];
    }

    /**
     * @param v1 The first vector
     * @param v2 The second vector
     * @return The cross product (right hand rule applies)
     */
    public static float[] cross(float[] v1, float[] v2) {
        return new float[] {
                v1[1]*v2[2] - v1[2]*v2[1],
                v1[2]*v2[0] - v1[0]*v2[2],
                v1[0]*v2[1] - v1[1]*v2[0]
        };
    }

    /**
     * @param u The first vector
     * @param v The second vector
     * @return {@code u} + {@code v}
     */
    public static float[] add(float[] u, float[] v) {
        return new float[] {u[0]+v[0], u[1]+v[1], u[2]+v[2], u[3]+v[3]};
    }

    /**
     * @param u The first vector
     * @param v The second vector
     * @return {@code u} - {@code v} (Note that the difference of two points is a vector)
     */
    public static float[] subtract(float[] u, float[] v) {
        return new float[] {u[0]-v[0], u[1]-v[1], u[2]-v[2], u[3]-v[3]};
    }

    /**
     * @param M A 4x4 matrix
     * @param v A vector of length 4
     * @return M*v
     */
    public static float[] matrixVecMult(float[][] M, float[] v) {
        float[] b = new float[4];
        for (int i = 0; i < 4; i++) {
            b[i] = 0;
            for (int j = 0; j < 4; j++) {
                b[i] += M[i][j]*v[j];
            }
        }
        return b;
    }

    public static float norm(float[] v) {
        return (float) java.lang.Math.sqrt(
                java.lang.Math.pow(v[0], 2) +
                java.lang.Math.pow(v[1], 2) +
                java.lang.Math.pow(v[2], 2));
    }

    public static float[] normalize(float[] v) {
        float n = norm(v);
        return new float[] {v[0]/n, v[1]/n, v[2]/n, 0};
    }
}
