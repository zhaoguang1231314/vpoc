package io.vizit.vpoc.vkafka;

import org.apache.avro.Schema;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.reflect.ReflectData;
import org.apache.avro.reflect.ReflectDatumReader;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class AvroDeserializer<T> implements Deserializer<T> {

    private static final Logger logger = LoggerFactory.getLogger(AvroDeserializer.class);

    protected final Class<T> targetType;

    public AvroDeserializer(Class<T> targetType) {
        this.targetType = targetType;
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        try {
            T result = null;
            if (data != null) {
                Schema schema = ReflectData.get().getSchema(targetType);
                DatumReader<T> datumReader = new ReflectDatumReader<>(schema);
                Decoder decoder = DecoderFactory.get().binaryDecoder(data, null);
                result = datumReader.read(null, decoder);
                logger.info("topic={}, data='{}'", topic, result);
            }
            return result;
        } catch (Exception ex) {
            throw new SerializationException("Failed to deserialize data '" + Arrays.toString(data) + "', topic '" + topic + "'", ex);
        }
    }
}
