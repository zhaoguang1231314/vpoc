package io.vizit.vpoc.kafka;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/visualize/kafka")
public class VisualizeApi {
    private static final String TOPIC_KAFKA = "/topic/kafka";
    private final SimpMessageSendingOperations messagingTemplate;

    public VisualizeApi(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @RequestMapping(value = "/report", method = RequestMethod.POST)
    public @ResponseBody
    Boolean report(@RequestBody KafkaRequest request) {
        messagingTemplate.convertAndSend(TOPIC_KAFKA, request);
        return true;
    }
}
