package at.fhv.sysarch.lab3.pipeline.push;

import at.fhv.sysarch.lab3.obj.Face;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Vec4;

import java.util.Objects;

public final class ModelViewPushFilter extends AbstractPushFilter<Face, Face> {
    private Mat4 modelViewMatrix;

    public ModelViewPushFilter(Mat4 initialModelViewMatrix) {
        this.modelViewMatrix = Objects.requireNonNull(initialModelViewMatrix);
    }

    public void setModelViewMatrix(Mat4 matrix) {
        this.modelViewMatrix = Objects.requireNonNull(matrix);
    }

    @Override
    public void process(Face data) {
        Vec4 v1 = modelViewMatrix.multiply(data.getV1());
        Vec4 v2 = modelViewMatrix.multiply(data.getV2());
        Vec4 v3 = modelViewMatrix.multiply(data.getV3());
        Vec4 n1 = modelViewMatrix.multiply(data.getN1());
        Vec4 n2 = modelViewMatrix.multiply(data.getN2());
        Vec4 n3 = modelViewMatrix.multiply(data.getN3());

        Face viewSpaceFace = new Face(v1, v2, v3, n1, n2, n3);
        successor.process(viewSpaceFace);
    }
}