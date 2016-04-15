package shapes;

import base.Triangle;

import java.util.List;

/**
 * Created by Jaco on 4/14/16
 */

public abstract class AbstractShape {
    public int color;
    public List<Triangle> mesh;

    public AbstractShape(List<Triangle> mesh, int color) {
        this.mesh = mesh;
        this.color = color;
    }

    public void addTriangle(Triangle t) {
        mesh.add(t);
    }

    public void addTriangles(List<Triangle> triangles) {
        mesh.addAll(triangles);
    }

    public void setMesh(List<Triangle> mesh) {
        this.mesh = mesh;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
