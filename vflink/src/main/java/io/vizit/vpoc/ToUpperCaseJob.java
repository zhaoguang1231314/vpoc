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
public class ToUpperCaseJob {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        properties.setProperty("group.id", "ToUpperCaseJob");
        properties.setProperty("transaction.timeout.ms", "100000");
        // 创建一个consumer，接受TOPIC-IN的消息
        FlinkKafkaConsumer<String> kafkaConsumer = new FlinkKafkaConsumer<>("TOPIC-IN", new SimpleStringSchema(), properties);
        kafkaConsumer.setStartFromLatest();
        DataStream<String> stream = env.addSource(kafkaConsumer)
                .name("kafkaConsumer");
        // 定义一个处理函数，把输入转成大写
        DataStream<String> operator = stream.process(new ToUpperCaseFunction())
                .name("ToUpperCaseOperator");
        // 创建一个producer，把消息发送到TOPIC-OUT
        FlinkKafkaProducer<String> kafkaProducer = new FlinkKafkaProducer<>(
                "TOPIC-OUT",
                new SimpleStringSchema(),
                properties);
        operator.addSink(kafkaProducer)
                .name("kafkaProducer");

        env.execute("ToUpperCase");
    }
}
