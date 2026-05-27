package at.fhv.sysarch.lab3.pipeline.push;

public interface PushPipe<I> {
    void process(I data);
    void flush();
}
