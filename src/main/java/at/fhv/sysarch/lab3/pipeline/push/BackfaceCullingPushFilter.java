package at.fhv.sysarch.lab3.pipeline.push;

import at.fhv.sysarch.lab3.obj.Face;
import com.hackoeur.jglm.Vec4;

public final class BackfaceCullingPushFilter extends AbstractPushFilter<Face, Face> {
    @Override
    public void process(Face data) {
        Vec4 v1 = data.getV1();
        Vec4 n1 = data.getN1();

        float dot = v1.getX() * n1.getX()
                + v1.getY() * n1.getY()
                + v1.getZ() * n1.getZ();

        if (dot > 0.0f) {
            return;
        }

        successor.process(data);
    }
}
