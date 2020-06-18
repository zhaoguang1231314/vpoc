package io.vizit.vpoc.jvm.model;

import io.vizit.vpoc.jvm.GcSupervisor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static io.vizit.vpoc.config.ApplicationContextProvider.getBeanList;

@Getter
@Setter
@Component("G1Heap")
public class G1Heap implements Heap {
    private int regionCount = 25;
    private AtomicLong sequence = new AtomicLong(1);
    private List<Region> regionList;
    private final GcSupervisor gcSupervisor;

    public G1Heap(GcSupervisor gcSupervisor) {
        this.regionList = getBeanList(Region.class, regionCount);
        for (int i = 0; i < regionCount; i++) {
            this.regionList.get(i).setId(i);
        }
        this.gcSupervisor = gcSupervisor;
    }

    @Override
    public ObjectBO allocate(int size) {
        Region niceEden = findNiceEden();
        if (niceEden == null) {
            gc();
        }
        niceEden = findNiceEden();
        return niceEden.allocate(sequence.getAndIncrement(), 1);
    }

    private void gc() {
    }

    private Region findNiceEden() {
        for (Region region : regionList) {
            if (region.regionType == Region.RegionType.EDEN && region.available(1)) {
                return region;
            }
        }
        return null;
    }

    @Override
    public void clear() {
        sequence = new AtomicLong(1);
        regionList.forEach(region -> region.sweep());
        gcSupervisor.go();
    }

}
