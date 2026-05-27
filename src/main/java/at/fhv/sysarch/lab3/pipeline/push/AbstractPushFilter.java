package at.fhv.sysarch.lab3.pipeline.push;

import java.util.Objects;

public abstract class AbstractPushFilter<I, O> implements PushPipe<I> {
    protected PushPipe<O> successor;

    public void setSuccessor(PushPipe<O> successor) {
        this.successor = Objects.requireNonNull(successor, "successor must not be null");
    }

    @Override
    public void flush() {
        if (successor != null) {
            successor.flush();
        }
    }
}