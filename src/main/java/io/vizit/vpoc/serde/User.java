package io.vizit.vpoc.serde;

import lombok.*;
import org.apache.avro.reflect.Nullable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
    private long id;
    private String name;
//    @Nullable
//    private String address;
}
