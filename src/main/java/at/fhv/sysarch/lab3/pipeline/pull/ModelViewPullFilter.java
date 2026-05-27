package at.fhv.sysarch.lab3.pipeline.pull;

import at.fhv.sysarch.lab3.obj.Face;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Vec4;

public class ModelViewPullFilter extends AbstractPullFilter<Face, Face> {
    private Mat4 modelViewMatrix;

    public ModelViewPullFilter(Mat4 initialModelViewMatrix) {
        this.modelViewMatrix = initialModelViewMatrix;
    }

    public void setModelViewMatrix(Mat4 matrix) {
        this.modelViewMatrix = matrix;
    }

    @Override
    public Face pull() {
        Face input = predecessor.pull();
        if (input == null) return null;

        Vec4 v1 = modelViewMatrix.multiply(input.getV1());
        Vec4 v2 = modelViewMatrix.multiply(input.getV2());
        Vec4 v3 = modelViewMatrix.multiply(input.getV3());
        Vec4 n1 = modelViewMatrix.multiply(input.getN1());
        Vec4 n2 = modelViewMatrix.multiply(input.getN2());
        Vec4 n3 = modelViewMatrix.multiply(input.getN3());

        return new Face(v1, v2, v3, n1, n2, n3);
    }
}