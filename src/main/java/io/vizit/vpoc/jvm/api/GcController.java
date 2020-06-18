package io.vizit.vpoc.jvm.api;

import io.vizit.vpoc.jvm.GcSupervisor;
import io.vizit.vpoc.jvm.model.HeapChoice;
import io.vizit.vpoc.jvm.model.Heap;
import io.vizit.vpoc.jvm.model.ObjectBO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping(value = "/jvm/gc")
public class GcController {
    private HeapChoice heapChoice = HeapChoice.G1;
    private Heap heap;
    private final Heap g1Heap;
    private final Heap generationalHeap;
    private final GcSupervisor gcSupervisor;
    private final SimpMessageSendingOperations messagingTemplate;

    public GcController(@Qualifier("G1Heap") Heap g1Heap, @Qualifier("GenerationalHeap") Heap generationalHeap, SimpMessageSendingOperations messagingTemplate, GcSupervisor gcSupervisor) {
        this.g1Heap = g1Heap;
        this.generationalHeap = generationalHeap;
        this.messagingTemplate = messagingTemplate;
        this.gcSupervisor = gcSupervisor;
        if (heapChoice == HeapChoice.G1) {
            this.heap = this.g1Heap;
        } else {
            this.heap = this.generationalHeap;
        }
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public @ResponseBody
    List<ObjectBO> newObject(@RequestBody NewRequest request) {
        List<ObjectBO> objects = new ArrayList<>();
        gcSupervisor.setDelay(request.getDelay());
        if (request.isReset()) {
            heap.clear();
        }
        gcSupervisor.setDebug(request.isDebug());

        for (int i = 0; i < request.getCount(); i++) {
            int size = request.getSize();
            if (request.getRandomSizeMax() > 0) {
                size = ThreadLocalRandom.current().nextInt(1, request.getRandomSizeMax());
            }
            ObjectBO objectBO = heap.allocate(size);
            objects.add(objectBO);
        }
        return objects;
    }

    @RequestMapping(value = "/debug/go", method = RequestMethod.POST)
    public @ResponseBody
    Boolean go() {
        return gcSupervisor.go();
    }

    @RequestMapping(value = "/debug/step", method = RequestMethod.POST)
    public @ResponseBody
    Boolean step() {
        return gcSupervisor.step();
    }

    @RequestMapping(value = "/debug/pause", method = RequestMethod.POST)
    public @ResponseBody
    Boolean pause() {
        gcSupervisor.setDebug(true);
        return true;
    }

}
