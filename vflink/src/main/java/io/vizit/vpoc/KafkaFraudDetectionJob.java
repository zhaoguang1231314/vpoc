package io.vizit.vpoc;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

import java.util.Properties;

/**
 * https://ci.apache.org/projects/flink/flink-docs-stable/dev/connectors/kafka.html
 */
public class KafkaFraudDetectionJob {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        properties.setProperty("group.id", "KafkaFraudDetectionJob");
        properties.setProperty("transaction.timeout.ms", "100000");
        FlinkKafkaConsumer<String> kafkaConsumer = new FlinkKafkaConsumer<>("TOPIC-IN", new SimpleStringSchema(), properties);
        kafkaConsumer.setStartFromEarliest();

        DataStream<String> stream = env.addSource(kafkaConsumer).name("kafkaConsumer");

        FlinkKafkaProducer<String> kafkaProducer = new FlinkKafkaProducer<>(
                "TOPIC-OUT",
                new SimpleStringSchema(),
                properties);

        DataStream<String> out = stream.process(new KafkaDetector())
                .name("flink-job");

        out.addSink(kafkaProducer).name("kafkaProducer");
        env.execute("Fraud Detection");
    }
}
