package io.vizit.vpoc.jvm.model;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ObjectBO implements Cloneable {
    @EqualsAndHashCode.Include
    private long id;
    private int size;
    private long address;
    private int age;
    private Region region;

    public ObjectBO(long id, int size, long address, int age) {
        this.id = id;
        this.size = size;
        this.address = address;
        this.age = age;
    }

    @Override
    protected Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public void increaseAge() {
        this.age++;
    }
}
