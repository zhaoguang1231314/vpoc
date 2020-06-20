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
    private Region fromRegion;
    private Region toRegion;

    public Copy(Region from, Region to, ObjectBO objectBO) {
        this.objectBO = objectBO;
        this.fromRegion = from;
        this.toRegion = to;
    }

    public Copy(SpaceEnum from, SpaceEnum to, ObjectBO objectBO) {
        this.from = from;
        this.to = to;
        this.objectBO = objectBO;
    }
}
