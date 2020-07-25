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
public class Broker {
    private int id;
    private List<Partition> partitions;
    private final KafkaSupervisor supervisor;

    public Broker(KafkaSupervisor supervisor) {
        this.supervisor = supervisor;
        this.partitions = ApplicationContextProvider.getBeanList(Partition.class, 3);
    }

    public Partition getLeader(String topic, int partition) {
        for (Partition p : partitions) {
            if (p.getTopic().equalsIgnoreCase(topic) && p.getId() == partition) {
                return p;
            }
        }
        return null;
    }

    public KafkaResponse send(Partition leader, int count) {
        return leader.send(count);
    }
}
