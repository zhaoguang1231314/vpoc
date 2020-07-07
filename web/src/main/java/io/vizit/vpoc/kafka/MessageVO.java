package io.vizit.vpoc.kafka;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class MessageVO {
    private Partition partition;
    private long start;
    private long end;
}
