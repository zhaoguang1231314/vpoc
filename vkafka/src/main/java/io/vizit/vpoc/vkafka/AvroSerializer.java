package io.vizit.vpoc.vkafka;

import org.apache.avro.Schema;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.reflect.ReflectData;
import org.apache.avro.reflect.ReflectDatumWriter;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;

public class AvroSerializer<T> implements Closeable, Serializer<T> {
    private static final Logger logger = LoggerFactory.getLogger(AvroSerializer.class);

    @Override
    public byte[] serialize(String topic, T data) {
        try {
            byte[] result = null;

            if (data != null) {
                logger.info("data='{}'", data);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                BinaryEncoder binaryEncoder = EncoderFactory.get().binaryEncoder(byteArrayOutputStream, null);
                Schema schema = ReflectData.get().getSchema(data.getClass());
                DatumWriter<T> datumWriter = new ReflectDatumWriter<T>(schema);
                datumWriter.write(data, binaryEncoder);

                binaryEncoder.flush();
                byteArrayOutputStream.close();

                result = byteArrayOutputStream.toByteArray();
            }
            return result;
        } catch (Exception ex) {
            throw new SerializationException("Failed to serialize data='" + data + "', topic='" + topic + "'", ex);
        }
    }
}
