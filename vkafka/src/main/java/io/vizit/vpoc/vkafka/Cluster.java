package io.vizit.vpoc.vkafka;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;


@Getter
@Setter
@Component
@Scope("prototype")
public class Cluster {
    private List<Broker> brokers;

    public Cluster() {
        brokers = ApplicationContextProvider.getBeanList(Broker.class, 3);
    }
    public KafkaResponse send(KafkaRequest request) {
        for (Broker broker : brokers) {
            Partition leader = broker.getLeader(request.getTopic(), request.getPartition());
            if (leader != null) {
                return broker.send(leader, request.getCount());
            }
        }
        return null;
    }
}
