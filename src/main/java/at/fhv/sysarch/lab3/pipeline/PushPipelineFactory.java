package at.fhv.sysarch.lab3.pipeline;

import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Matrices;

import at.fhv.sysarch.lab3.animation.AnimationRenderer;
import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.Model;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import at.fhv.sysarch.lab3.pipeline.push.BackfaceCullingPushFilter;
import at.fhv.sysarch.lab3.pipeline.push.ColoringPushFilter;
import at.fhv.sysarch.lab3.pipeline.push.DepthSortingPushFilter;
import at.fhv.sysarch.lab3.pipeline.push.FlatShadingPushFilter;
import at.fhv.sysarch.lab3.pipeline.push.ModelViewPushFilter;
import at.fhv.sysarch.lab3.pipeline.push.PerspectiveDivisionPushFilter;
import at.fhv.sysarch.lab3.pipeline.push.ProjectionPushFilter;
import at.fhv.sysarch.lab3.pipeline.push.PushPipe;
import at.fhv.sysarch.lab3.pipeline.push.RendererPushSink;
import at.fhv.sysarch.lab3.pipeline.push.ViewportPushFilter;
import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;

public class PushPipelineFactory {
    private static final float ROTATION_SPEED_RAD_PER_SEC = (float) Math.PI / 2.0f;

    public static AnimationTimer createPipeline(PipelineData pd) {
        RendererPushSink sink = new RendererPushSink(
                pd.getGraphicsContext(),
                pd.getRenderingMode());

        ViewportPushFilter viewport = new ViewportPushFilter(pd.getViewportTransform());
        viewport.setSuccessor(sink);

        PerspectiveDivisionPushFilter perspectiveDiv = new PerspectiveDivisionPushFilter();
        perspectiveDiv.setSuccessor(viewport);

        ProjectionPushFilter projection = new ProjectionPushFilter(pd.getProjTransform());
        projection.setSuccessor(perspectiveDiv);

        PushPipe<Pair<Face, Color>> afterColoring;
        if (pd.isPerformLighting()) {
            FlatShadingPushFilter shading = new FlatShadingPushFilter(pd.getLightPos());
            shading.setSuccessor(projection);
            afterColoring = shading;
        } else {
            afterColoring = projection;
        }

        ColoringPushFilter coloring = new ColoringPushFilter(pd.getModelColor());
        coloring.setSuccessor(afterColoring);

        DepthSortingPushFilter depthSort = new DepthSortingPushFilter();
        depthSort.setSuccessor(coloring);

        BackfaceCullingPushFilter culling = new BackfaceCullingPushFilter();
        culling.setSuccessor(depthSort);
        Mat4 initialMv = pd.getViewTransform().multiply(pd.getModelTranslation());
        ModelViewPushFilter modelView = new ModelViewPushFilter(initialMv);
        modelView.setSuccessor(culling);

        return new AnimationRenderer(pd) {

            private float rotationAngle = 0.0f;

            @Override
            protected void render(float fraction, Model model) {
                rotationAngle += ROTATION_SPEED_RAD_PER_SEC * fraction;
                Mat4 rotation = Matrices.rotate(rotationAngle, pd.getModelRotAxis());
                Mat4 modelMatrix = pd.getModelTranslation().multiply(rotation);
                Mat4 modelViewMatrix = pd.getViewTransform().multiply(modelMatrix);

                modelView.setModelViewMatrix(modelViewMatrix);

                for (Face f : model.getFaces()) {
                    modelView.process(f);
                }
                modelView.flush();
            }
        };
    }
}