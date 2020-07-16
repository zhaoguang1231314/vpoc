# Create Topics
## TOPIC_ARTICLE  
```bash
bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic TOPIC_ARTICLE
```

## send to TOPIC_ARTICLE
```bash
bin/kafka-console-producer.sh --bootstrap-server localhost:9092 --topic TOPIC_ARTICLE
```
## consume TOPIC_ARTICLE
```bash
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic TOPIC_ARTICLE --from-beginning
```  
