package io.vizit.vpoc.exercise;

import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.watermark.Watermark;

public abstract class TestSource<T> implements SourceFunction<T> {
    private volatile boolean running = true;
    // T or watermark (Long)
    protected Object[] testStream;

    @Override
    public void run(SourceContext<T> ctx) {
        for (int i = 0; (i < testStream.length) && running; i++) {
            if (testStream[i] instanceof Long) {
                Long ts = (Long) testStream[i];
                ctx.emitWatermark(new Watermark(ts));
            } else {
                //noinspection unchecked
                T element = (T) testStream[i];
                ctx.collectWithTimestamp(element, getTimestamp(element));
            }
        }
        // test sources are finite, so they have a Long.MAX_VALUE watermark when they finishes
    }

    abstract long getTimestamp(T element);

    @Override
    public void cancel() {
        running = false;
    }
}
