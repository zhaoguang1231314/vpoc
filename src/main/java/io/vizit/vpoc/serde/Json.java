package io.vizit.vpoc.serde;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class Json {
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            User user = new User(i, String.format("user-%d", i));
            userList.add(user);
        }
        try (OutputStream outputStream = new FileOutputStream("user.json")) {
            mapper.writeValue(outputStream, userList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
