package base;

/**
 * Created by Jaco on 4/14/16
 */

public class Mat {

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
        return new float[] {u[0]+v[0], u[1]+v[1], u[2]+v[2]};
    }

    /**
     * @param u The first vector
     * @param v The second vector
     * @return {@code u} - {@code v} (Note that the difference of two points is a vector)
     */
    public static float[] subtract(float[] u, float[] v) {
        return new float[] {u[0]-v[0], u[1]-v[1], u[2]-v[2]};
    }

    /**
     * @param M A 4x4 matrix
     * @param v A vector of length 4
     * @return M*v
     */
    public static float[] matrixVecMult(float[][] M, float[] v) {
        float[] b = new float[4];
        for (int i = 0; i < 4; i++) {
            float sum = 0;
            for (int j = 0; j < 4; j++) {
                sum += M[i][j]*v[j];
            }
            b[i] = sum;
        }
        return b;
    }

    /**
     * Multiplies a 4-length vector by a 4x4 transformation matrix.
     * Optimized for knowing that M[3] is {0,0,0,1}
     * Should cut operation time by 40-50%
     * @param M 4x4 Transformation matrix (bottom row is 0,0,0,1)
     * @param V 4 length vector (last digit is 1)
     * @return M*V
     */
    public static float[] transformationMatrixVecMult(float[][] M, float[] V){
        float[] b = new float[3];
        // Skip the known elements in the matrix and vector
        // Since these parts can be accounted for without multiplication
        for (int i = 0; i < 3; i++) {
            float sum = 0;
            for (int j = 0; j < 3; j++) {
                sum += M[i][j]*V[j];
            }
            b[i] = sum;
        }
        // Account for the 1 in V[3]
        b[0]+=M[0][3];
        b[1]+=M[1][3];
        b[2]+=M[2][3];
        return b;
    }

    /**
     * Multiplies two 4x4 matrices
     * @param M 4x4 Matrix
     * @param N 4x4 Matrix
     * @return M*N
     */
    public static float[][] matrixMatrixMult(float[][] M, float[][] N){
        float[][] out = new float[4][4];

        for(int i=0; i<4; i++) {
            for (int j = 0; j < 4; j++) {
                float sum = 0;
                for (int k = 0; k < 4; k++) {
                    sum += M[i][k] * N[k][j];
                }
                out[i][j] = sum;
            }
        }

        return out;
    }

    /**
     * @param A A row-major order matrix (4x4)
     * @param P A column-major order matrix (i.e., an array of points) (4 x anything)
     * @return A*P
     */
    public static float[][] matrixPointMult(float[][] A, float[][] P) {
        float[][] C = new float[P.length][4];
        for (int i = 0; i < C.length; i++) {
            //C[i] = transformationMatrixVecMult(A,P[i]);
            C[i] = matrixVecMult(A, P[i]);
        }
        return C;
    }

    /**
     * Multiply two 4x4 transformation matricies
     * Optimizing for the fact that both matricies have {0,0,0,1} on the bottom
     * @param M 4x4 transformation matrix
     * @param N 4x4 transformation matrix
     * @return
     */
    public static float[][] transformationMatrixMatrixMult(float[][] M, float[][] N){
        float[][] out = new float[4][4];

        for(int i=0; i<3; i++) {
            for (int j = 0; j < 4; j++) {
                // We need to actually compute the first 3 rows in all 4 columns
                out[i][j] = 0;
                for (int k = 0; k < 3; k++) {
                    // Skip k=3 for now, because N[3] is {0,0,0,1}
                    out[i][j] += M[i][k] * N[k][j];
                }
            }
        }
        // Account for k=3, j=3
        out[0][3]+=M[0][3];
        out[1][3]+=M[1][3];
        out[2][3]+=M[2][3];
        out[3] = new float[]{0,0,0,1};
        return out;
    }


    public static float magnitude(float[] v) {
        return (float) java.lang.Math.sqrt(v[0]*v[0] + v[1]*v[1] + v[2]*v[2]);
    }

    public static float[] normalize(float[] v) {
        float n = magnitude(v);
        return new float[] {v[0]/n, v[1]/n, v[2]/n, 1f};
    }

    /**
     * Multiplies a vector by m
     * @param v Vector
     * @param m Multiple
     * @return
     */
    public static float[] mult(float[] v, float m){
        float[] out = new float[v.length];
        for(int i=0; i<v.length; i++){
            out[i]=v[i]*m;
        }
        return out;
    }

    public static float[][] transpose(float[][] A) {
        float[][] B = new float[A[0].length][A.length];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                B[j][i] = A[i][j];
            }
        }
        return B;
    }

    /**
     * <b>Assumes {@code u} is normalized</b>
     * @param u The vector to be projected onto
     * @param v The projected vector
     * @return proj_u v
     */
    public static float[] project(float[] u, float[] v) {
        return mult(u, dot(u, v));
    }

    /**
     * Multiplies each channel of {@param color} by {@param by}
     */
    public static int multByChannel(int color, float by) {
        int r = Math.min(Math.round(((color & 0xff0000) >> 16) * by), 0xff);
        int g = Math.min(Math.round(((color & 0xff00) >> 8) * by), 0xff);
        int b = Math.min(Math.round((color & 0xff) * by), 0xff);
        return (r<<16) + (g<<8) + b;
    }

    public static int addColorsWithThreshold(int[] colors) {
        int r = 0, g = 0, b = 0;
        for (int color : colors) {
            r += (color&0xff0000)>>16;
            g += (color&0xff00)>>8;
            b += color&0xff;
        }
        r = Math.min(r, 0xff);
        g = Math.min(g, 0xff);
        b = Math.min(b, 0xff);
        return (r<<16) + (g<<8) + b;
    }

    public static int averageColors(int[] colors) {
        int r = 0, g = 0, b = 0;
        for (int color : colors) {
            r += (color&0xff0000)>>16;
            g += (color&0xff00)>>8;
            b += color&0xff;
        }
        r /= colors.length;
        g /= colors.length;
        b /= colors.length;
        return (r<<16) + (g<<8) + b;
    }
}
