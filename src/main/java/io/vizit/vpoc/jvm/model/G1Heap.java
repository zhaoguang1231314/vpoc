package io.vizit.vpoc.jvm.model;

import io.vizit.vpoc.config.ApplicationContextProvider;
import io.vizit.vpoc.jvm.GcSupervisor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Getter
@Setter
@Component("G1Heap")
public class G1Heap implements Heap {
    private int capacity = 625;
    private AtomicLong sequence = new AtomicLong(1);
    private List<Eden> edenList;
    private List<Survivor> survivorList;
    private List<Old> oldList;
    private final GcSupervisor gcSupervisor;

    public G1Heap(GcSupervisor gcSupervisor) {
        this.edenList = buildEdenList();
        this.survivorList = survivorList;
        this.oldList = oldList;
        this.gcSupervisor = gcSupervisor;
    }

    private List<Eden> buildEdenList() {
        List<Eden> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
//            Eden eden = ApplicationContextProvider.getBean("Eden", Eden.class);
//            list.add(eden);
        }
        return list;
    }

    @Override
    public ObjectBO allocate(int size) {
        for (int i = 0; i < edenList.size(); i++) {

        }
        return null;
    }

    @Override
    public void clear() {
        sequence = new AtomicLong(1);
        gcSupervisor.go();
    }

}
