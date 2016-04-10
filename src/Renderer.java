import javafx.geometry.Point3D;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tara on 4/7/2016.
 */
public abstract class Renderer {

    protected ArrayList<Triangle3D> tris;

    public void setTris(ArrayList<Triangle3D> newTris){
        tris=newTris;
    }

    public abstract List<Triangle> render();
    public abstract void setCamera(Camera cam);


    // Both matrix multiplication methods can be optimized because the last row is always 1 or [0,0,0,1]
    // Memoizing the points somehow could reduce point transform operations by a factor of roughly 4

    /**
     * Combines two 4x4 transformation matricies into a single transformation matrix
     * @param L Left 4x4 matrix
     * @param R Right 4x4 matrix
     * @return L*R
     */
    protected double[][] matrixCombine(double[][] L, double[][] R){
        // TODO complete this
        return null;
    }

    /**
     * Applies a 4x4 transformation matrix to a (1x4? 4x1?) position matrix
     * @param L Left 4x4 matrix
     * @param R Right (1x4? 4x1?) matrix
     * @return L*R
     */
    protected double[] matrixMult(double[][] L, double[] R){
        // TODO complete this
        return null;
    }

    protected double[] matrixMult(double[][] L, Point3D R){
        return matrixMult(L, new double[]{R.getX(), R.getY(), R.getZ(), 1});
    }


}
