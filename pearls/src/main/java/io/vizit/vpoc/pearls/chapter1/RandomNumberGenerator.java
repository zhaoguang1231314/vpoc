package io.vizit.vpoc.pearls.chapter1;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class RandomNumberGenerator {

    public static final int N = 10000000;

    public static void main(String[] args) {
        List<Integer> integers = ThreadLocalRandom.current()
                .ints(N, 0, N)
                .distinct()
                .boxed()
                .collect(Collectors.toList());

        String filepath = "./pearls/phone_numbers.txt";
        File file = new File(filepath);
        try {
            FileUtils.writeLines(file, integers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
