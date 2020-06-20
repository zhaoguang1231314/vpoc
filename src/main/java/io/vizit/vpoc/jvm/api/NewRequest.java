package io.vizit.vpoc.jvm.api;

import io.vizit.vpoc.jvm.model.GcChoice;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewRequest {
    private int size;
    private int count = 1;
    private int randomSizeMax;
    private int delay = 100; // ms
    private boolean debug = false;
    private GcChoice gcChoice = GcChoice.YOUNG;
}
