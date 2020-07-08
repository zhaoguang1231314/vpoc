package io.vizit.vpoc.flink;

import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.time.LocalTime;
import java.util.Properties;

public class CounterJob {
    static String TOPIC_IN = "Topic1-IN";
    static String BOOTSTRAP_SERVER = "localhost:9092";

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        Properties props = new Properties();
        props.put("bootstrap.servers", BOOTSTRAP_SERVER);
        props.put("client.id", "flink-counter");

        FlinkKafkaConsumer<KafkaRecord> kafkaConsumer = new FlinkKafkaConsumer<>(TOPIC_IN, new MessageDeserializer(), props);
        kafkaConsumer.setStartFromLatest();

        DataStream<KafkaRecord> stream = env.addSource(kafkaConsumer);
        stream.timeWindowAll(Time.seconds(5))
                .reduce(new ReduceFunction<KafkaRecord>() {
                    KafkaRecord result = new KafkaRecord();

                    @Override
                    public KafkaRecord reduce(KafkaRecord record1, KafkaRecord record2) throws Exception {
                        System.out.println(LocalTime.now() + " -> " + record1 + "   " + record2);
                        result.setKey(record1.getKey());
                        result.setValue(record1.getValue() + record2.getValue());
                        return result;
                    }
                })
                .print();

        // for visual topology of the pipeline. Paste the below output in https://flink.apache.org/visualizer/
        System.out.println(env.getExecutionPlan());

        env.execute();

    }
}
