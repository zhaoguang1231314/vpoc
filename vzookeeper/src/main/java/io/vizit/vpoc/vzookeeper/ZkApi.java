package io.vizit.vpoc.vzookeeper;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/zk")
public class ZkApi {
    private final ZnodeManager znodeManager;

    public ZkApi(ZnodeManager znodeManager) {
        this.znodeManager = znodeManager;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public @ResponseBody
    ZkResponse create(@RequestBody ZnodeRequest request) {
        znodeManager.create(request);
        return new ZkResponse("created! " + request.getPath());
    }
}
