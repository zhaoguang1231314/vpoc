package io.vizit.vpoc.vkafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import static io.vizit.vpoc.vkafka.Constants.TOPIC_ARTICLE;

public class ArticleConsumer {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "localhost:9092");
        props.setProperty("group.id", "ArticleConsumer");
        props.setProperty("enable.auto.commit", "true");
        props.setProperty("auto.commit.interval.ms", "1000");
        props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getCanonicalName());
        props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, AvroDeserializer.class.getCanonicalName());
        KafkaConsumer<String, Article> consumer = new KafkaConsumer<>(props, new StringDeserializer(), new AvroDeserializer<>(Article.class));
        consumer.subscribe(Arrays.asList(TOPIC_ARTICLE));
        while (true) {
            ConsumerRecords<String, Article> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, Article> record : records) {
                Article value = record.value();
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), value);
            }
        }
    }
}
