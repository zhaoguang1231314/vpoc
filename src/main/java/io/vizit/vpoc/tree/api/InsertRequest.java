package io.vizit.vpoc.tree.api;

import io.vizit.vpoc.tree.redblack.Node;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InsertRequest {
    private List<Node> nodes;
    private int count = 1;
    private int delay = 100; // ms
    private boolean reset = false;
    private boolean debug = false;
}
