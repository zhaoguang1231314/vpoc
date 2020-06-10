package io.vizit.vpoc.tree;

import lombok.Setter;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
@Setter
public class TreeSupervisor {
    public static final String TOPIC_INSERT = "/topic/tree/rb/insert";
    private final SimpMessageSendingOperations messagingTemplate;
    private int delay = 1000;
    private boolean debug; // pause before mark, copy, sweep
    private Lock lock = new ReentrantLock();
    private Condition go = lock.newCondition();

    public TreeSupervisor(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
}
