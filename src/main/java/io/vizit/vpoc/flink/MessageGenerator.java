package io.vizit.vpoc.flink;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.Closeable;
import java.util.Properties;

public class MessageGenerator extends Thread implements Closeable {
    private final KafkaProducer<String, String> producer;
    int counter = 0;
    final String topic;
    String bootstrapServers = "localhost:9092";

    public MessageGenerator(String topic) {
        this.topic = topic;
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producer = new KafkaProducer<>(properties);
    }

    @Override
    public void run() {
        try {
            while (++counter > 0) {
                send(topic, String.valueOf(counter));
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void send(String topic, String message) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, "test", message);
        producer.send(record);
        producer.flush();
    }

    @Override
    public void close() {
        producer.close();
    }

    public static void main(String[] args) {
        new MessageGenerator("TOPIC-IN").start();
    }
}
