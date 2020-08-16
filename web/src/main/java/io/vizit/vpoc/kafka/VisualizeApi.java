package io.vizit.vpoc.kafka;

import io.vizit.vpoc.reporter.ZkChange;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/visualize/kafka")
public class VisualizeApi {
    private static final String TOPIC_KAFKA = "/topic/kafka";
    private static final String TOPIC_ZK = "/topic/zk";
    private final SimpMessageSendingOperations messagingTemplate;

    public VisualizeApi(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @RequestMapping(value = "/broker", method = RequestMethod.POST)
    public @ResponseBody
    Boolean report(@RequestBody KafkaRequest request) {
        messagingTemplate.convertAndSend(TOPIC_KAFKA, request);
        return true;
    }

    @RequestMapping(value = "/zk", method = RequestMethod.POST)
    public @ResponseBody
    Boolean zk(@RequestBody ZkChange request) {
        messagingTemplate.convertAndSend(TOPIC_ZK, request);
        return true;
    }
}
