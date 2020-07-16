package io.vizit.vpoc.vkafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.IOException;
import java.util.Properties;

import static io.vizit.vpoc.vkafka.Constants.TOPIC_ARTICLE;

public class ArticleProducer {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getCanonicalName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, AvroSerializer.class.getCanonicalName());
        Producer<String, Article> producer = new KafkaProducer<>(props);
        try {
//            String file = "/data/article.json";
//            Article article = mapper.readValue(ArticleProducer.class.getResourceAsStream(file), Article.class);
            //
            for (int i = 0; i < 100000; i++) {
                Article article = new Article(i, "Test Avro", "Vizit", "Kafka meets Avro!");
                producer.send(new ProducerRecord<>(TOPIC_ARTICLE, article.getTitle(), article));
                Thread.sleep(1000);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        producer.close();
    }
}
