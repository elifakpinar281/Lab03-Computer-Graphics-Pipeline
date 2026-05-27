package at.fhv.sysarch.lab3.pipeline.pull;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.Model;

import java.util.List;

public class ModelPullSource implements PullPipe<Face> {
    private final Model model;
    private int index;

    public ModelPullSource(Model model) {
        this.model = model;
        this.index = 0;
    }

    @Override
    public Face pull() {
        List<Face> faces = model.getFaces();
        if (index >= faces.size()) {
            return null;
        }
        return faces.get(index++);
    }

    @Override
    public void reset() {
        index = 0;
    }
}