package at.fhv.sysarch.lab3.pipeline.push;

import at.fhv.sysarch.lab3.obj.Face;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import at.fhv.sysarch.lab3.obj.Face;


public final class DepthSortingPushFilter extends AbstractPushFilter<Face, Face> {
    private final List<Face> buffer = new ArrayList<>();
    private static final Comparator<Face> FAR_TO_NEAR = (a, b) -> {
        float za = (a.getV1().getZ() + a.getV2().getZ() + a.getV3().getZ()) / 3.0f;
        float zb = (b.getV1().getZ() + b.getV2().getZ() + b.getV3().getZ()) / 3.0f;
        return Float.compare(za, zb);
    };

    @Override
    public void process(Face data) {
        buffer.add(data);
    }

    @Override
    public void flush() {
        buffer.sort(FAR_TO_NEAR);

        for (int i = 0; i < buffer.size(); i++) {
            successor.process(buffer.get(i));
        }
        buffer.clear();
        successor.flush();
    }
}