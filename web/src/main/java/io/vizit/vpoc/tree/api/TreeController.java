package io.vizit.vpoc.tree.api;

import io.vizit.vpoc.tree.TreeSupervisor;
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
    List<Integer> insert(@RequestBody InsertRequest request) {
        return supervisor.insert(request);
    }
}
