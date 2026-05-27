package at.fhv.sysarch.lab3.pipeline.push;

import at.fhv.sysarch.lab3.obj.Face;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Vec4;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import javafx.scene.paint.Color;

import java.util.Objects;

public final class ProjectionPushFilter extends AbstractPushFilter<Pair<Face, Color>, Pair<Face, Color>> {
    private final Mat4 projectionMatrix;

    public ProjectionPushFilter(Mat4 projectionMatrix) {
        this.projectionMatrix = Objects.requireNonNull(projectionMatrix);
    }

    @Override
    public void process(Pair<Face, Color> data) {
        Face f = data.fst();

        Vec4 v1 = projectionMatrix.multiply(f.getV1());
        Vec4 v2 = projectionMatrix.multiply(f.getV2());
        Vec4 v3 = projectionMatrix.multiply(f.getV3());

        Face projected = new Face(v1, v2, v3, f);
        successor.process(new Pair<>(projected, data.snd()));
    }
}