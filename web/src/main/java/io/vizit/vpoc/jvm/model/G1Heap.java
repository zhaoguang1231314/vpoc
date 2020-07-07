package io.vizit.vpoc.jvm.model;

import io.vizit.vpoc.jvm.GcSupervisor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import static io.vizit.vpoc.config.ApplicationContextProvider.getBeanList;
import static io.vizit.vpoc.jvm.model.JvmConfig.MaxTenuringThreshold;
import static java.lang.Thread.sleep;

@Getter
@Setter
@Component("G1Heap")
public class G1Heap implements Heap {
    private int regionCount = 25; // 2 + 16 + 36 = 54
    private AtomicLong sequence = new AtomicLong(1);
    private List<Region> regionList;
    private List<Region> eList = new ArrayList<>();
    private List<Region> sList = new ArrayList<>();
    private List<Region> oList = new ArrayList<>();
    private List<Region> hList = new ArrayList<>();
    private final GcSupervisor gcSupervisor;

    public G1Heap(GcSupervisor gcSupervisor) {
        this.regionList = getBeanList(Region.class, regionCount);
        for (int i = 0; i < regionCount; i++) {
            Region region = this.regionList.get(i);
            region.setId(i);
            if (i == 11 || i == 13) {
                region.setRegionType(Region.RegionType.SURVIVOR);
                sList.add(region);
            } else if (i == 24) {
                region.setRegionType(Region.RegionType.HUMONGOUS);
                hList.add(region);
            } else if (i % 2 == 0 && i < 10) {
                region.setRegionType(Region.RegionType.EDEN);
                eList.add(region);
            } else {
                region.setRegionType(Region.RegionType.OLD);
                oList.add(region);
            }
        }
        this.gcSupervisor = gcSupervisor;
    }

    @Override
    public ObjectBO allocate(int size) {
        if (niceToYoungGc()) {
            youngGc();
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Region niceEden = findNiceEden();
        if (niceEden == null) {
            gc();
        }
        niceEden = findNiceEden();
        return niceEden.allocate(sequence.getAndIncrement(), 1);
    }

    private void youngGc() {
        Optional<Region> toS = sList.stream().filter(region -> region.empty()).findFirst();
        if (!toS.isPresent()) {
            throw new RuntimeException("No available Survivor Region!");
        }
        // mark
        Stream.concat(eList.stream(), sList.stream()).filter(region -> !region.empty()).forEach(region -> region.mark());
        // copy
        Stream.concat(eList.stream(), sList.stream()).filter(region -> !region.empty()).forEach(region -> {
            region.getLiveObjects().forEach(objectBO -> copy(objectBO, toS.get()));
        });
        // sweep
        Stream.concat(eList.stream(), sList.stream()).filter(region -> !region.empty() && !region.equals(toS.get())).forEach(region -> region.sweep());
    }

    public void copy(ObjectBO objectBO, Region to) {
        ObjectBO copy = (ObjectBO) objectBO.clone();
        if (copy.getAge() >= MaxTenuringThreshold) {
            Optional<Region> old = oList.stream().filter(region -> region.available(1)).findFirst();
            if (!old.isPresent()) {
                throw new RuntimeException("No available Old Region!");
            }
            old.get().copy(copy);
        } else {
            to.copy(objectBO);
        }
    }

    private boolean niceToYoungGc() {
        long fullCount = eList.stream().filter(region -> !region.available(1)).count();
        return fullCount == eList.size(); // full
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
        regionList.stream().filter(region -> !region.empty()).forEach(region -> region.sweep());
        gcSupervisor.go();
    }

}
