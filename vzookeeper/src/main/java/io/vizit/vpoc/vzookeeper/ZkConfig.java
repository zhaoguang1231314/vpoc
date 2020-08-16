package io.vizit.vpoc.vzookeeper;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ZkConfig {
    @Value("${data.dir}")
    private String dataDir;

}
