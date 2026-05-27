package at.fhv.sysarch.lab3.pipeline.pull;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import com.hackoeur.jglm.Vec3;
import com.hackoeur.jglm.Vec4;
import javafx.scene.paint.Color;

public class FlatShadingPullFilter extends AbstractPullFilter<Pair<Face, Color>, Pair<Face, Color>> {
    private final Vec3 lightPos;

    public FlatShadingPullFilter(Vec3 lightPos) {
        this.lightPos = lightPos;
    }

    @Override
    public Pair<Face, Color> pull() {
        Pair<Face, Color> input = predecessor.pull();
        if (input == null) return null;

        Face face = input.fst();
        Color base = input.snd();

        Vec4 v1 = face.getV1();
        Vec4 v2 = face.getV2();
        Vec4 v3 = face.getV3();

        // Compute face centre
        Vec3 centre = new Vec3(
                (v1.getX() + v2.getX() + v3.getX()) / 3.0f,
                (v1.getY() + v2.getY() + v3.getY()) / 3.0f,
                (v1.getZ() + v2.getZ() + v3.getZ()) / 3.0f
        );

        // Direction from face centre to light, normalised
        Vec3 toLight = lightPos.subtract(centre).getUnitVector();

        // Use first vertex normal for flat shading
        Vec4 normal = face.getN1();
        float cosTheta = toLight.getX() * normal.getX()
                + toLight.getY() * normal.getY()
                + toLight.getZ() * normal.getZ();

        if (cosTheta <= 0.0f) {
            return new Pair<>(face, Color.BLACK);
        }

        Color shaded = Color.color(
                base.getRed()   * cosTheta,
                base.getGreen() * cosTheta,
                base.getBlue()  * cosTheta
        );

        return new Pair<>(face, shaded);
    }
}