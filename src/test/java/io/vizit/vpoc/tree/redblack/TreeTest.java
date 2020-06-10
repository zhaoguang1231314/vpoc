package io.vizit.vpoc.tree.redblack;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class TreeTest {
    @Autowired
    Tree<Integer, String> tree;

    @Test
    void insert() {
        for (int i = 0; i < 10; i++) {
            int key = ThreadLocalRandom.current().nextInt();
            Node<Integer, String> node = new Node<>(key);
            Tree<Integer, String> insert = tree.insert(node);
        }

    }
}