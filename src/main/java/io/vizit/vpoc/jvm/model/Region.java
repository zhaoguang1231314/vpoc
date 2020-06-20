package io.vizit.vpoc.jvm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.vizit.vpoc.jvm.GcSupervisor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
@Component
@Scope("prototype")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Region {
    @EqualsAndHashCode.Include
    private int id;
    @JsonIgnore
    private int capacity = JvmConfig.getRegionSize();
    private AtomicInteger allocatedPointer = new AtomicInteger(0);
    @JsonIgnore
    List<ObjectBO> allocatedObjects = new ArrayList<>();
    @JsonIgnore
    List<ObjectBO> liveObjects = new ArrayList<>();
    @JsonIgnore
    private final GcSupervisor gcSupervisor;
    public RegionType regionType = RegionType.EDEN;

    public void copy(ObjectBO objectBO) {
        Region from = objectBO.getRegion();
        objectBO.setAddress(allocatedPointer.get());
        objectBO.setRegion(this);
        objectBO.increaseAge();
        allocatedObjects.add(objectBO);
        allocatedPointer.addAndGet(objectBO.getSize());
        gcSupervisor.copy(new Copy(from, this, objectBO));
    }

    public enum RegionType {
        EDEN, SURVIVOR, OLD, HUMONGOUS
    }

    public Region(GcSupervisor gcSupervisor) {
        this.gcSupervisor = gcSupervisor;
    }

    public synchronized ObjectBO allocate(long id, int size) {
        ObjectBO objectBO = new ObjectBO(id, size, allocatedPointer.get(), 0);
        objectBO.setRegion(this);
        allocatedObjects.add(objectBO);
        allocatedPointer.addAndGet(objectBO.getSize());
        gcSupervisor.reportNewObject(objectBO);
        return objectBO;
    }

    public synchronized boolean empty() {
        return allocatedObjects.isEmpty();
    }

    public synchronized boolean available(int size) {
        return allocatedPointer.get() + size <= capacity;
    }

    public synchronized void sweep() {
        allocatedObjects.clear();
        liveObjects.clear();
        allocatedPointer.set(0);
        gcSupervisor.sweep(new Sweep(this));
    }

    public synchronized void mark() {
        Collections.sort(allocatedObjects, Comparator.comparingInt(ObjectBO::getAge).reversed());
        Set<Integer> set = new HashSet<>();
        set.add(0);
        while (set.size() < 2) {
            int nextInt = ThreadLocalRandom.current().nextInt(allocatedObjects.size());
            set.add(nextInt);
        }
        set.forEach(i -> {
            ObjectBO objectBO = allocatedObjects.get(i);
            liveObjects.add(objectBO);
            gcSupervisor.mark(objectBO);
        });

    }
}
