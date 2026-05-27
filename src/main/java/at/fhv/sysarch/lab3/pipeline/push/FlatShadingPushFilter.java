package at.fhv.sysarch.lab3.pipeline.push;

import java.util.Objects;
import com.hackoeur.jglm.Vec3;
import com.hackoeur.jglm.Vec4;
import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import javafx.scene.paint.Color;

public final class FlatShadingPushFilter extends AbstractPushFilter<Pair<Face, Color>, Pair<Face, Color>> {
    private final Vec3 lightPosInViewSpace;

    public FlatShadingPushFilter(Vec3 lightPosInViewSpace) {
        this.lightPosInViewSpace = Objects.requireNonNull(lightPosInViewSpace);
    }

    @Override
    public void process(Pair<Face, Color> data) {
        Face face = data.fst();
        Color base = data.snd();

        Vec4 v1 = face.getV1();
        Vec4 v2 = face.getV2();
        Vec4 v3 = face.getV3();
        Vec3 centre = new Vec3(
                (v1.getX() + v2.getX() + v3.getX()) / 3.0f,
                (v1.getY() + v2.getY() + v3.getY()) / 3.0f,
                (v1.getZ() + v2.getZ() + v3.getZ()) / 3.0f);

        Vec3 toLight = lightPosInViewSpace.subtract(centre).getUnitVector();

        Vec4 normal = face.getN1();
        float cosTheta = toLight.getX() * normal.getX()
                + toLight.getY() * normal.getY()
                + toLight.getZ() * normal.getZ();

        if (cosTheta <= 0.0f) {
            successor.process(new Pair<>(face, Color.BLACK));
            return;
        }

        Color shaded = Color.color(
                base.getRed()   * cosTheta,
                base.getGreen() * cosTheta,
                base.getBlue()  * cosTheta);

        successor.process(new Pair<>(face, shaded));
    }
}