package io.vizit.vpoc.kafka;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class KafkaRequest {
    private String topic;
    private int partition;
    private int count;
}
