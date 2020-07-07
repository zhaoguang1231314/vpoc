package io.vizit.vpoc.tree;

import io.vizit.vpoc.tree.api.InsertRequest;
import lombok.Setter;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import java.util.List;
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

    public List<Integer> insert(InsertRequest request) {
        this.setDelay(request.getDelay());
        this.setDebug(request.isDebug());
        report(TOPIC_INSERT, request.getNodes());
        return request.getNodes();
    }

    private void report(String topic, Object object) {
        try {
            Thread.sleep(delay);
            messagingTemplate.convertAndSend(topic, object);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
