package at.fhv.sysarch.lab3.pipeline.pull;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import javafx.scene.paint.Color;

public class ColoringPullFilter extends AbstractPullFilter<Face, Pair<Face, Color>> {
    private final Color modelColor;

    public ColoringPullFilter(Color modelColor) {
        this.modelColor = modelColor;
    }

    @Override
    public Pair<Face, Color> pull() {
        Face input = predecessor.pull();
        if (input == null) return null;
        return new Pair<>(input, modelColor);
    }
}