package io.vizit.vpoc.serde;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileStream;
import org.apache.avro.io.DatumReader;
import org.apache.avro.reflect.ReflectData;
import org.apache.avro.reflect.ReflectDatumReader;

import java.io.FileInputStream;
import java.io.IOException;

public class AvroReader {
    public static void main(String[] args) {
        Schema schema = ReflectData.get().getSchema(User.class);
        DatumReader<User> datumReader = new ReflectDatumReader<>(schema);
        try (FileInputStream inputStream = new FileInputStream("user.avro");
             DataFileStream<User> dataFileReader = new DataFileStream<>(inputStream, datumReader)) {
            while (dataFileReader.hasNext()) {
                User user = dataFileReader.next();
                System.out.println(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(schema);
    }
}
