package io.vizit.vpoc.kafka;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@Scope("prototype")
public class Producer {
    private final Cluster cluster;

    public Producer(Cluster cluster) {
        this.cluster = cluster;
    }

    public KafkaResponse send(KafkaRequest request) {

        return null;
    }
}
