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
    private int regionCount = 25; // 2 + 16 + 36 = 54
    private AtomicLong sequence = new AtomicLong(1);
    private List<Region> regionList;
    private final GcSupervisor gcSupervisor;

    public G1Heap(GcSupervisor gcSupervisor) {
        this.regionList = getBeanList(Region.class, regionCount);
        for (int i = 0; i < regionCount; i++) {
            Region region = this.regionList.get(i);
            region.setId(i);
            if (i % 2 == 0) {
                region.setRegionType(Region.RegionType.EDEN);
            } else {
                region.setRegionType(Region.RegionType.OLD);
            }
            if (i == 11 || i == 13) {
                region.setRegionType(Region.RegionType.SURVIVOR);
            }
            if (i == 24) {
                region.setRegionType(Region.RegionType.HUMONGOUS);
            }
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
