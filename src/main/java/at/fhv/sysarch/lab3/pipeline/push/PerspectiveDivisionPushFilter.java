package at.fhv.sysarch.lab3.pipeline.push;

import at.fhv.sysarch.lab3.obj.Face;
import com.hackoeur.jglm.Vec4;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import javafx.scene.paint.Color;

public final class PerspectiveDivisionPushFilter extends AbstractPushFilter<Pair<Face, Color>, Pair<Face, Color>> {

    @Override
    public void process(Pair<Face, Color> data) {
        Face f = data.fst();

        Vec4 v1 = divide(f.getV1());
        Vec4 v2 = divide(f.getV2());
        Vec4 v3 = divide(f.getV3());

        Face ndc = new Face(v1, v2, v3, f);
        successor.process(new Pair<>(ndc, data.snd()));
    }

    private static Vec4 divide(Vec4 v) {
        float w = v.getW();
        return new Vec4(v.getX() / w, v.getY() / w, v.getZ() / w, 1.0f);
    }
}
