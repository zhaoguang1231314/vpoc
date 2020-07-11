package io.vizit.vpoc;


import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.walkthrough.common.sink.AlertSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaDetector extends ProcessFunction<String, String> {
    private static final Logger LOG = LoggerFactory.getLogger(AlertSink.class);

    @Override
    public void processElement(String value, Context ctx, Collector<String> out) throws Exception {
        LOG.info(value);
        out.collect(value.toUpperCase());
    }
}
