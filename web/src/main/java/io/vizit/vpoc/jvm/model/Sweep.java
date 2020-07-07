package io.vizit.vpoc.jvm.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Sweep {
    private SpaceEnum space;
    private Region region;

    public Sweep(SpaceEnum space) {
        this.space = space;
    }

    public Sweep(Region region) {
        this.region = region;
    }
}
