package base;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Sortable triangle class, to be used as an intermediary between rendering and drawing
 * Created by Tara on 5/3/2016.
 */
public class SortableTriangle implements Comparable{
    public double[][] coords;
    public int color;
    public double depth;

    public SortableTriangle(double[][] coords, int color, double depth){
        this.coords=coords;
        this.color=color;
        this.depth=depth;
    }

    public void render(GraphicsContext context){
        int[] color = {(this.color & 0xff0000) >> 16, (this.color & 0x00ff00) >> 8, this.color & 0x0000ff};
        context.setFill(Color.rgb(color[0], color[1], color[2]));
        context.fillPolygon(coords[0], coords[1], 3);
    }


    @Override
    public int compareTo(Object t) {
        if (t instanceof SortableTriangle) {
            return (int)(depth - ((SortableTriangle)t).depth);
        }
        return 0;
    }
}
