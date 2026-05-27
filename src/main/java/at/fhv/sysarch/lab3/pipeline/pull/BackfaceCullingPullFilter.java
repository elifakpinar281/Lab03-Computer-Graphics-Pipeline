package at.fhv.sysarch.lab3.pipeline.pull;

import at.fhv.sysarch.lab3.obj.Face;
import com.hackoeur.jglm.Vec4;

public class BackfaceCullingPullFilter extends AbstractPullFilter<Face, Face> {

    @Override
    public Face pull() {
        while (true) {
            Face input = predecessor.pull();
            if (input == null) return null;

            Vec4 v1 = input.getV1();
            Vec4 n1 = input.getN1();

            float dot = v1.getX() * n1.getX()
                    + v1.getY() * n1.getY()
                    + v1.getZ() * n1.getZ();

            // dot > 0 means face points away from camera → cull it
            if (dot <= 0.0f) {
                return input;
            }
            // otherwise skip and pull next
        }
    }
}