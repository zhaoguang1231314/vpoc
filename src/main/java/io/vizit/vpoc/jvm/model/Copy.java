package io.vizit.vpoc.jvm.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Copy {
    private SpaceEnum from;
    private SpaceEnum to;
    private ObjectBO objectBO;
    private Region toRegion;

    public Copy(ObjectBO objectBO, Region region) {
        this.objectBO = objectBO;
        this.toRegion = region;
    }

    public Copy(SpaceEnum from, SpaceEnum to, ObjectBO objectBO) {
        this.from = from;
        this.to = to;
        this.objectBO = objectBO;
    }
}
