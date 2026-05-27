package at.fhv.sysarch.lab3.pipeline.pull;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Vec4;
import javafx.scene.paint.Color;

public class ProjectionPullFilter extends AbstractPullFilter<Pair<Face, Color>, Pair<Face, Color>> {
    private final Mat4 projectionMatrix;

    public ProjectionPullFilter(Mat4 projectionMatrix) {
        this.projectionMatrix = projectionMatrix;
    }

    @Override
    public Pair<Face, Color> pull() {
        Pair<Face, Color> input = predecessor.pull();
        if (input == null) return null;

        Face f = input.fst();
        Vec4 v1 = projectionMatrix.multiply(f.getV1());
        Vec4 v2 = projectionMatrix.multiply(f.getV2());
        Vec4 v3 = projectionMatrix.multiply(f.getV3());

        Face projected = new Face(v1, v2, v3, f);
        return new Pair<>(projected, input.snd());
    }
}