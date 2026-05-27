package at.fhv.sysarch.lab3.pipeline.pull;

import java.util.Objects;

public abstract class AbstractPullFilter<I, O> implements PullPipe<O> {
    protected PullPipe<I> predecessor;

    public void setPredecessor(PullPipe<I> predecessor) {
        this.predecessor = Objects.requireNonNull(predecessor);
    }

    @Override
    public void reset() {
        if (predecessor != null) {
            predecessor.reset();
        }
    }
}