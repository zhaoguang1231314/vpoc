package io.vizit.vpoc.pearls.chapter1;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomNumberGenerator {

    public static final int MIN = 1000000;
    public static final int MAX = 9999999;
    public static final int N = MAX - MIN + 1;

    public static void main(String[] args) {

        List<Integer> integers = new ArrayList<>(N);
        for (int i = MIN; i <= MAX; i++) {
            integers.add(i);
        }
        Collections.shuffle(integers);

        String filepath = "./pearls/phone_numbers.txt";
        File file = new File(filepath);
        try {
            FileUtils.writeLines(file, integers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
