package io.vizit.vpoc.serde;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.reflect.ReflectData;
import org.apache.avro.reflect.ReflectDatumWriter;

import java.io.FileOutputStream;
import java.io.OutputStream;

public class Avro {
    public static void main(String[] args) {
        Schema schema = ReflectData.get().getSchema(User.class);
        DatumWriter datumWriter = new ReflectDatumWriter<>(schema);
        try (OutputStream outputStream = new FileOutputStream("user.avro");
             DataFileWriter dataFileWriter = new DataFileWriter<>(datumWriter)) {
            dataFileWriter.create(schema, outputStream);
            for (int i = 0; i < 1000; i++) {
                User user = new User(i, String.format("user-%d", i));
                dataFileWriter.append(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
