package io.vizit.vpoc.flink;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;
import org.apache.kafka.clients.consumer.ConsumerRecord;

@SuppressWarnings("serial")
public class MessageDeserializer implements KafkaDeserializationSchema<KafkaRecord> {
    @Override
    public boolean isEndOfStream(KafkaRecord nextElement) {
        return false;
    }

    @Override
    public KafkaRecord deserialize(ConsumerRecord<byte[], byte[]> record) throws Exception {
        KafkaRecord data = new KafkaRecord(new String(record.key()), new String(record.value()), record.timestamp());
        return data;
    }

    @Override
    public TypeInformation<KafkaRecord> getProducedType() {
        return TypeInformation.of(KafkaRecord.class);
    }
}
