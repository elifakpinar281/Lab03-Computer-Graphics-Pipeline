package at.fhv.sysarch.lab3.pipeline.push;

import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Vec4;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import javafx.scene.paint.Color;

import java.util.Objects;

public final class ViewportPushFilter extends AbstractPushFilter<Pair<Face, Color>, Pair<Face, Color>> {
    private final Mat4 viewportMatrix;

    public ViewportPushFilter(Mat4 viewportMatrix) {
        this.viewportMatrix = Objects.requireNonNull(viewportMatrix);
    }

    @Override
    public void process(Pair<Face, Color> data) {
        Face f = data.fst();

        Vec4 v1 = viewportMatrix.multiply(f.getV1());
        Vec4 v2 = viewportMatrix.multiply(f.getV2());
        Vec4 v3 = viewportMatrix.multiply(f.getV3());

        Face screen = new Face(v1, v2, v3, f);
        successor.process(new Pair<>(screen, data.snd()));
    }
}