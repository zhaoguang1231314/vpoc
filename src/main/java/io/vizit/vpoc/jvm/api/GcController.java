package io.vizit.vpoc.jvm.api;

import io.vizit.vpoc.jvm.GcSupervisor;
import io.vizit.vpoc.jvm.model.GcChoice;
import io.vizit.vpoc.jvm.model.Heap;
import io.vizit.vpoc.jvm.model.ObjectBO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/jvm/gc")
public class GcController {
    private final Heap g1Heap;
    private final Heap generationalHeap;
    private final GcSupervisor gcSupervisor;
    private final SimpMessageSendingOperations messagingTemplate;

    public GcController(@Qualifier("G1Heap") Heap g1Heap, @Qualifier("GenerationalHeap") Heap generationalHeap, SimpMessageSendingOperations messagingTemplate, GcSupervisor gcSupervisor) {
        this.g1Heap = g1Heap;
        this.generationalHeap = generationalHeap;
        this.messagingTemplate = messagingTemplate;
        this.gcSupervisor = gcSupervisor;
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public @ResponseBody
    List<ObjectBO> newObjects(@RequestBody NewRequest request) {
        Heap heap = this.generationalHeap;
        if (request.getGcChoice() == GcChoice.G1_YOUNG) {
            heap = this.g1Heap;
        }
        return gcSupervisor.newObjects(heap, request);
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
