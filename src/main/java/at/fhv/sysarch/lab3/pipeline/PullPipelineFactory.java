package at.fhv.sysarch.lab3.pipeline;

import at.fhv.sysarch.lab3.animation.AnimationRenderer;
import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.Model;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import at.fhv.sysarch.lab3.pipeline.pull.*;
import at.fhv.sysarch.lab3.rendering.RenderingMode;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Matrices;
import com.hackoeur.jglm.Vec2;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class PullPipelineFactory {
    private static final float ROTATION_SPEED_RAD_PER_SEC = (float) Math.PI / 2.0f;

    public static AnimationTimer createPipeline(PipelineData pd) {
        ModelPullSource source = new ModelPullSource(pd.getModel());

        Mat4 initialMv = pd.getViewTransform().multiply(pd.getModelTranslation());
        ModelViewPullFilter modelView = new ModelViewPullFilter(initialMv);
        modelView.setPredecessor(source);

        BackfaceCullingPullFilter culling = new BackfaceCullingPullFilter();
        culling.setPredecessor(modelView);

        // NOTE: No depth sorting in pull pipeline!
        // Depth sorting requires collecting ALL faces first, which contradicts
        // the pull principle of processing one element at a time.

        ColoringPullFilter coloring = new ColoringPullFilter(pd.getModelColor());
        coloring.setPredecessor(culling);

        PullPipe<Pair<Face, Color>> beforeProjection;

        if (pd.isPerformLighting()) {
            FlatShadingPullFilter shading = new FlatShadingPullFilter(pd.getLightPos());
            shading.setPredecessor(coloring);
            beforeProjection = shading;
        } else {
            beforeProjection = coloring;
        }

        ProjectionPullFilter projection = new ProjectionPullFilter(pd.getProjTransform());
        projection.setPredecessor(beforeProjection);

        PerspectiveDivisionPullFilter perspDiv = new PerspectiveDivisionPullFilter();
        perspDiv.setPredecessor(projection);

        ViewportPullFilter viewport = new ViewportPullFilter(pd.getViewportTransform());
        viewport.setPredecessor(perspDiv);

        RendererPullSink sink = new RendererPullSink(
                viewport, pd.getGraphicsContext(), pd.getRenderingMode());

        // returning an animation renderer which handles clearing of the
        // viewport and computation of the praction
        return new AnimationRenderer(pd) {
            private float rotationAngle = 0.0f;

            /** This method is called for every frame from the JavaFX Animation
             * system (using an AnimationTimer, see AnimationRenderer). 
             * @param fraction the time which has passed since the last render call in a fraction of a second
             * @param model    the model to render 
             */
            @Override
            protected void render(float fraction, Model model) {
                rotationAngle += ROTATION_SPEED_RAD_PER_SEC * fraction;

                Mat4 rotation = Matrices.rotate(rotationAngle, pd.getModelRotAxis());
                Mat4 modelMatrix = pd.getModelTranslation().multiply(rotation);
                Mat4 modelViewMatrix = pd.getViewTransform().multiply(modelMatrix);

                modelView.setModelViewMatrix(modelViewMatrix);
                sink.renderAll();
            }
        };
    }
}