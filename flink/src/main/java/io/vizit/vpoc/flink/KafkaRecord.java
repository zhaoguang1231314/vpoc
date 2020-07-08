package io.vizit.vpoc.flink;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class KafkaRecord implements Serializable {
    private String key;
    private String value;
    private Long timestamp;

    @Override
    public String toString() {
        return key + ":" + value;
    }

}
