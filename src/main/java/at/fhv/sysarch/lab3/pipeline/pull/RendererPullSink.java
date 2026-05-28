package at.fhv.sysarch.lab3.pipeline.pull;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import at.fhv.sysarch.lab3.rendering.RenderingMode;
import com.hackoeur.jglm.Vec2;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class RendererPullSink {
    private final PullPipe<Pair<Face, Color>> source;
    private final GraphicsContext gc;
    private final RenderingMode mode;

    private final double[] xs = new double[3];
    private final double[] ys = new double[3];

    public RendererPullSink(PullPipe<Pair<Face, Color>> source,
                            GraphicsContext gc, RenderingMode mode) {
        this.source = source;
        this.gc = gc;
        this.mode = mode;
    }

    public void renderAll() {
        source.reset();

        Pair<Face, Color> result;
        while ((result = source.pull()) != null) {
            Face f = result.fst();
            Color c = result.snd();

            Vec2 p1 = f.getV1().toScreen();
            Vec2 p2 = f.getV2().toScreen();
            Vec2 p3 = f.getV3().toScreen();

            xs[0] = p1.getX(); xs[1] = p2.getX(); xs[2] = p3.getX();
            ys[0] = p1.getY(); ys[1] = p2.getY(); ys[2] = p3.getY();

            switch (mode) {
                case POINT:
                    gc.setStroke(c);
                    gc.strokeLine(xs[0], ys[0], xs[0]+1, ys[0]+1);
                    gc.strokeLine(xs[1], ys[1], xs[1]+1, ys[1]+1);
                    gc.strokeLine(xs[2], ys[2], xs[2]+1, ys[2]+1);
                    break;
                case WIREFRAME:
                    gc.setStroke(c);
                    gc.strokePolygon(xs, ys, 3);
                    break;
                case FILLED:
                    gc.setFill(c);
                    gc.fillPolygon(xs, ys, 3);
                    gc.setStroke(c);
                    gc.strokePolygon(xs, ys, 3);
                    break;
            }
        }
    }
}