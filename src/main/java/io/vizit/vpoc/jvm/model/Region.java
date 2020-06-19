package io.vizit.vpoc.jvm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.vizit.vpoc.jvm.GcSupervisor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
@Getter
@Setter
@Component
@Scope("prototype")
public class Region {
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

    public synchronized boolean available(int size) {
        return allocatedPointer.get() + size <= capacity;
    }

    public synchronized void sweep() {
        allocatedObjects.clear();
        liveObjects.clear();
        allocatedPointer.set(0);
        gcSupervisor.sweep(new Sweep(SpaceEnum.EDEN));
    }

    public synchronized void mark() {
        Set<Integer> set = new HashSet<>();
        while (set.size() < 3) {
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
