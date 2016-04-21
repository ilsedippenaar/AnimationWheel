import base.Mat;
import base.Matrix;
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
import shapes.AbstractShape;
import shapes.Tetrahedron;

import java.util.ArrayList;

public class Main extends Application implements EventHandler<KeyEvent> {
    private int width = 800;
    private int height = 600;
    private long last;

    Camera camera = new Camera();
    ArrayList<AbstractShape> shapes = new ArrayList<>();
    Matrix.Builder builder = new Matrix.Builder();
    private float angle = 0;

    public static void main(String[] args) throws InterruptedException {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("AnimationWheel");
        Group root = new Group();
        Scene scene = new Scene(root);
        scene.setOnKeyPressed(this);
        primaryStage.setScene(scene);

        Canvas canvas = new Canvas(width, height);
        GraphicsContext context = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);

        Tetrahedron t = new Tetrahedron();
        shapes.add(t);

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
        float[] facing= Mat.normalize(camera.at);

        angle = (angle + 1f*(time-last)/(16.66666f*1000000)) % 360;
        float[][] tetrahedronMatrix = builder
                .scale(20)
                .translate(0,0,-100)
                .rotateY(angle)
                .build();
        shapes.get(0).setTransform(tetrahedronMatrix);

        for (AbstractShape s : shapes) {
            for (base.Triangle t : s.mesh) {
                float[][] points = camera.look(Mat.matrixPointMult(s.transform, t.points));

                // Cull tris facing away (which solves depth-checking for simple shapes)
                float[] edge1= Mat.subtract(points[0], points[1]);
                float[] edge2= Mat.subtract(points[0], points[2]);
                float[] norm = Mat.cross(edge1, edge2);

                if(Mat.dot(norm, facing)<0) {
                    double[][] coords = pointsToDisplayPoints(points);
                    context.setFill(Color.rgb((t.color&0xff0000)>>16, (t.color&0x00ff00)>>8, t.color&0x0000ff));
                    context.fillPolygon(coords[0], coords[1], 3);
                }
            }
        }
        last = time;
    }

    @Override
    public void handle(KeyEvent event) {
        switch (event.getCode()) {
            case W:
                camera.moveZ(-5);
                break;
            case S:
                camera.moveZ(5);
                break;
            case D:
                camera.moveX(5);
                break;
            case A:
                camera.moveX(-5);
                break;
            case R:
                camera.rotateYBy(2);
                break;
            case Q:
                camera.rotateYBy(-2);
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
