import base.Mat;
import base.Matrix;
import base.SortableTriangle;
import base.Triangle;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lights.AbstractLight;
import lights.AmbientLight;
import lights.PointLight;
import shapes.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class Main extends Application implements EventHandler<KeyEvent> {
    private int width = 800;
    private int height = 600;
    private long last;

    Camera camera = new Camera();
    ArrayList<AbstractShape> shapes = new ArrayList<>();
    ArrayList<AbstractLight> lights = new ArrayList<>();
    Matrix.Builder builder = new Matrix.Builder();
    private float angle = 0;

    public static void main(String[] args) throws InterruptedException {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {


        System.out.println("W/S to move Z\nA/D to move X\nR/F to move Y\nQ/E to rotate around Y axis\nC/X to rotate around X axis\nZ to reset facing towards origin");

        primaryStage.setTitle("AnimationWheel");
        Group root = new Group();
        Scene scene = new Scene(root);
        scene.setOnKeyPressed(this);
        primaryStage.setScene(scene);

        Canvas canvas = new Canvas(width, height);
        GraphicsContext context = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);

        AmbientLight ambientLight = new AmbientLight(0.3f, 0xffffff);
        lights.add(ambientLight);

        Cube lightCube = new Cube();
        float[] lightPos = new float[] {0,200,0,1};
        lightCube.setTransform(new Matrix.Builder().scale(10).translate(lightPos).build());
        PointLight pointLight = new PointLight(1, 0xffffff, lightPos, lightCube, 500);
        lights.add(pointLight);

        //Tetrahedron t = new Tetrahedron();
        //shapes.add(t);

        HollowCube h = new HollowCube();
        shapes.add(h);

        Cube c=new Cube();
        shapes.add(c);

        Axes a = new Axes();
        shapes.add(a);

        camera.setPos(new float[] {0, 0, 200, 1});
        camera.faceTowardsOrigin();


        last = System.nanoTime();

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                step(context, now);
            }
        }.start();

        primaryStage.show();
    }

    private void step(GraphicsContext context, long time) {
        // clear canvas to draw again
        context.clearRect(0, 0, width, height);

        angle = (angle + 1f*(time-last)/(16.66666f*1000000)) % 360;
        float[][] firsShapeMatrix = builder
                .reset()
                .scale(20)
                .translate(0,0,0)
                .rotateY(angle) // TODO  Fix: this causes the shape to turn inside-out
                .build();
        shapes.get(0).setTransform(firsShapeMatrix);

        float[][] secondShapeMatrix = builder
                .reset()
                .scale(20)
                .translate(0,0,-400)
                .rotateZ(-angle)
                .build();
        shapes.get(1).setTransform(secondShapeMatrix);

        //*
        ArrayList<SortableTriangle> renderedTris=new ArrayList<>();
        //*/

        for (AbstractShape s : shapes) {
            for (Triangle t : s.mesh) {
                /*
                render(context, t, s.transform, false);
                /*/
                SortableTriangle temp = (renderReturn(t, s.transform, false));
                if(temp!=null)
                    renderedTris.add(temp);
                //*/
            }
        }

        for (AbstractLight l : lights) {
            if (l.shape != null) {
                for (Triangle t : l.shape.mesh) {
                    /*
                    render(context, t, l.shape.transform, true);
                    /*/
                    SortableTriangle temp = (renderReturn(t, l.shape.transform, false));
                    if(temp!=null)
                        renderedTris.add(temp);
                    //*/
                }
            }
        }

        //*
        Collections.sort(renderedTris, Collections.reverseOrder()); // Reverse order is what we want
        for(SortableTriangle t:renderedTris){
            t.render(context);
        }
        //*/

        last = time;
    }

    private void render(GraphicsContext context, Triangle t, float[][] transform, boolean emissive) {
        // This applies the movements we've made to the shapes
        float[][] points=Mat.matrixPointMult(transform, t.points);

        // Cull tris facing away (which solves depth-checking for simple shapes)
        // This needs to be done before applying perspective
        float[] edge1= Mat.subtract(points[0], points[1]);
        float[] edge2= Mat.subtract(points[0], points[2]);
        float[] norm = Mat.cross(edge1, edge2);

        // With perspective instead of isometric, we use this instead of the raw facing
        float[] toTri = Mat.subtract(camera.pos,points[0]);

        if(Mat.dot(norm, toTri)<0) {
            Camera.PointsAndColor pc = new Camera.PointsAndColor(points, t.color);
            pc = camera.look(pc, lights, emissive);
            if (pc.points != null) {
                double[][] coords = pointsToDisplayPoints(pc.points);
                context.setFill(Color.rgb((pc.color & 0xff0000) >> 16, (pc.color & 0x00ff00) >> 8, pc.color & 0x0000ff));
                context.fillPolygon(coords[0], coords[1], 3);
            }
        }
    }

    /**
     * Same as render() except instead of drawing the tri, it returns a sortable triangle to be sorted & rendered later
     */
    private SortableTriangle renderReturn(Triangle t, float[][] transform, boolean emissive) {
        // This applies the movements we've made to the shapes
        float[][] points=Mat.matrixPointMult(transform, t.points);

        // Cull tris facing away (which solves depth-checking for simple shapes)
        // This needs to be done before applying perspective
        float[] edge1= Mat.subtract(points[0], points[1]);
        float[] edge2= Mat.subtract(points[0], points[2]);
        float[] norm = Mat.cross(edge1, edge2);

        // With perspective instead of isometric, we use this instead of the raw facing
        float[] toTri = Mat.subtract(camera.pos,points[0]);

        float[] dist=Mat.add(Mat.add(points[0],points[1]),points[2]);
        dist=Mat.subtract(dist, camera.pos);
        dist=Mat.subtract(dist, camera.pos);
        dist=Mat.subtract(dist, camera.pos);

        if(Mat.dot(norm, toTri)<0) {
            Camera.PointsAndColor pc = new Camera.PointsAndColor(points, t.color);
            pc = camera.look(pc, lights, emissive);
            if (pc.points != null) {
                double depth = Mat.magnitude(dist);
                double[][] coords = pointsToDisplayPoints(pc.points);
                return new SortableTriangle(coords, pc.color, depth);
            }
        }
        return null;
    }



    @Override
    public void handle(KeyEvent event) {
        switch (event.getCode()) {
            case W:
                camera.moveForward(5);
                break;
            case S:
                camera.moveForward(-5);
                break;
            case D:
                camera.moveRight(5);
                break;
            case A:
                camera.moveRight(-5);
                break;
            case E:
                camera.rotateYBy(5);
                break;
            case Q:
                camera.rotateYBy(-5);
                break;
            case R:
                camera.moveUp(5);
                break;
            case F:
                camera.moveUp(-5);
                break;
            case X:
                camera.rotateXBy(5);
                break;
            case C:
                camera.rotateXBy(-5);
                break;
            case Z:
                camera.faceTowardsOrigin();
                break;
        }
    }

    /**
     * @param points Points of a triangle in [-1,1] ready to be painted
     * @return A worldView with two rows, one for x values, the other for y values
     */
    private double[][] pointsToDisplayPoints(float [][] points) {
        int midHeight = height/2;
        int midWidth = width/2;
        double[][] newPoints = new double[2][points.length];
        for (int i = 0; i < points.length; i++) {
            newPoints[0][i] = (points[i][0]*width) + midWidth; // move and set x
            newPoints[1][i] = midHeight - (points[i][1]*height); // move and set y
        }
        return newPoints;
    }

}
