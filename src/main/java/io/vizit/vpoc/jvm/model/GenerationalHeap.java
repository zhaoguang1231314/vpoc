package io.vizit.vpoc.jvm.model;

import io.vizit.vpoc.jvm.GcSupervisor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Getter
@Setter
@Component("GenerationalHeap")
public class GenerationalHeap implements Heap {
    private int capacity = 240000;
    private final Young young;
    private final Old old;
    private AtomicLong sequence = new AtomicLong(1);
    private final GcSupervisor gcSupervisor;

    public GenerationalHeap(Young young, Old old, GcSupervisor gcSupervisor) {
        this.young = young;
        this.old = old;
        this.gcSupervisor = gcSupervisor;
    }

    @Override
    public ObjectBO allocate(int size) {
        return young.allocate(sequence.getAndIncrement(), size);
    }

    @Override
    public void clear() {
        young.clear();
        old.clear();
        sequence = new AtomicLong(1);
        gcSupervisor.go();
    }

}
