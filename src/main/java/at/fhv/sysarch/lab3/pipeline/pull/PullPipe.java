package at.fhv.sysarch.lab3.pipeline.pull;

public interface PullPipe<O> {
    O pull();
    void reset();
}