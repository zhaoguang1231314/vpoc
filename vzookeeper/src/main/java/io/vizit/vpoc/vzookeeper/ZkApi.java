package io.vizit.vpoc.vzookeeper;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/zk")
public class ZkApi {
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public @ResponseBody
    ZkResponse create(@RequestBody ZnodeRequest request) {
        return new ZkResponse("created! " + request.getPath());
    }
}
