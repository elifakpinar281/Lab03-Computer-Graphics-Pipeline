package at.fhv.sysarch.lab3.pipeline.pull;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import com.hackoeur.jglm.Vec4;
import javafx.scene.paint.Color;

public class PerspectiveDivisionPullFilter extends AbstractPullFilter<Pair<Face, Color>, Pair<Face, Color>> {

    @Override
    public Pair<Face, Color> pull() {
        Pair<Face, Color> input = predecessor.pull();
        if (input == null) return null;

        Face f = input.fst();
        Vec4 v1 = divide(f.getV1());
        Vec4 v2 = divide(f.getV2());
        Vec4 v3 = divide(f.getV3());

        Face ndc = new Face(v1, v2, v3, f);
        return new Pair<>(ndc, input.snd());
    }

    private static Vec4 divide(Vec4 v) {
        float w = v.getW();
        return new Vec4(v.getX() / w, v.getY() / w, v.getZ() / w, 1.0f);
    }
}