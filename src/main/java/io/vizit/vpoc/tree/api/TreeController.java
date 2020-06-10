package io.vizit.vpoc.tree.api;

import io.vizit.vpoc.tree.TreeSupervisor;
import io.vizit.vpoc.tree.redblack.Node;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/tree/rb")
public class TreeController {
    private final TreeSupervisor supervisor;

    public TreeController(TreeSupervisor treeSupervisor) {
        this.supervisor = treeSupervisor;
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public @ResponseBody
    List<Node> insert(@RequestBody InsertRequest request) {
        supervisor.setDelay(request.getDelay());
        supervisor.setDebug(request.isDebug());

        return request.getNodes();
    }
}
