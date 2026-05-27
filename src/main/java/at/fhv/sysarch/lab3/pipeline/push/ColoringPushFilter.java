package at.fhv.sysarch.lab3.pipeline.push;

import java.util.Objects;
import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import javafx.scene.paint.Color;

public final class ColoringPushFilter extends AbstractPushFilter<Face, Pair<Face, Color>> {

    private final Color modelColor;

    public ColoringPushFilter(Color modelColor) {
        this.modelColor = Objects.requireNonNull(modelColor);
    }

    @Override
    public void process(Face data) {
        successor.process(new Pair<>(data, modelColor));
    }
}