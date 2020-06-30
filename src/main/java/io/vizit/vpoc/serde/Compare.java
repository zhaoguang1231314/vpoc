package io.vizit.vpoc.serde;

import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.buf.HexUtils;

import java.io.File;
import java.io.IOException;

public class Compare {
    public static void main(String[] args) {
        try {
            byte[] avroBytes = FileUtils.readFileToByteArray(new File("user.avro"));
            System.out.println(avroBytes.length);
            String toAvroHexString = HexUtils.toHexString(avroBytes);
            System.out.println(toAvroHexString);

            byte[] jsonBytes = FileUtils.readFileToByteArray(new File("user.json"));
            System.out.println(jsonBytes.length);
            String toJsonHexString = HexUtils.toHexString(jsonBytes);
            System.out.println(toJsonHexString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
