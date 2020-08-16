package io.vizit.vpoc.reporter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ZkChange {
    private String path;
    private Type type;

    public enum Type {
        CREATED
    }
}
