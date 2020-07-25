package io.vizit.vpoc.vkafka;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@Scope("prototype")
@NoArgsConstructor
public class Partition {
    private int id;
    private long lastOffset;
    private long committedOffset;
    private String topic = "test";
    private int brokerId;

    public Partition(String topic, int brokerId) {
        this.topic = topic;
        this.brokerId = brokerId;
    }

    public KafkaResponse send(int count) {
        lastOffset += count;
        KafkaResponse response = new KafkaResponse();
        response.setPartition(this);
        return response;
    }
}
