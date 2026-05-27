package at.fhv.sysarch.lab3.pipeline.pull;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Vec4;
import javafx.scene.paint.Color;

public class ViewportPullFilter extends AbstractPullFilter<Pair<Face, Color>, Pair<Face, Color>> {
    private final Mat4 viewportMatrix;

    public ViewportPullFilter(Mat4 viewportMatrix) {
        this.viewportMatrix = viewportMatrix;
    }

    @Override
    public Pair<Face, Color> pull() {
        Pair<Face, Color> input = predecessor.pull();
        if (input == null) return null;

        Face f = input.fst();
        Vec4 v1 = viewportMatrix.multiply(f.getV1());
        Vec4 v2 = viewportMatrix.multiply(f.getV2());
        Vec4 v3 = viewportMatrix.multiply(f.getV3());

        Face screen = new Face(v1, v2, v3, f);
        return new Pair<>(screen, input.snd());
    }
}