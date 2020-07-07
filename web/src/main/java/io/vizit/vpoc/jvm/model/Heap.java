package io.vizit.vpoc.jvm.model;

public interface Heap {
    ObjectBO allocate(int size);

    void clear();
}
