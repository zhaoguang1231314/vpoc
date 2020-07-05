package io.vizit.vpoc.kafka;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import lombok.Setter;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
@Setter
@Getter
public class KafkaSupervisor {
    public static final String TOPIC_KAFKA_SEND = "/topic/kafka/send";
    public static final String TOPIC_KAFKA_PAUSE = "/topic/kafka/pause";
    private final SimpMessageSendingOperations messagingTemplate;
    private int delay = 1000;
    private boolean debug;
    private Lock lock = new ReentrantLock();
    private Condition go = lock.newCondition();
    private transient boolean stop = false;

    public KafkaSupervisor(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void send(MessageVO messageVO) {
        try {
            debug(String.format("send %s", messageVO));
            Thread.sleep(delay);
            messagingTemplate.convertAndSend(TOPIC_KAFKA_SEND, messageVO);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void debug(String msg) {
        if (debug) {
            lock.lock();
            try {
                messagingTemplate.convertAndSend(TOPIC_KAFKA_PAUSE, ImmutableMap.of("msg", msg));
                go.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }
    }

}
